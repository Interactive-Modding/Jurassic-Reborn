package net.vit.jurassicreborn.common.entities;

import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.vit.jurassicreborn.common.entities.ai.navigation.MoveUnderwaterEntityAI;

public abstract class SwimmingDinosaurEntity extends DinosaurEntity {
    public SwimmingDinosaurEntity(Level world, EntityType type, Dinosaur dino) {
        super(world, type, dino);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.goalSelector.addGoal(1, new MoveUnderwaterEntityAI(this));
        this.navigation = new WaterBoundPathNavigation(this, world);
    }

    @Override
    public void tick() {
        int air = this.getAirSupply();
        super.tick();
        if (this.isAlive() && !this.isInWater()) {
            --air;
            this.setAirSupply(air);
            if (this.getAirSupply() == -20) {
                this.setAirSupply(0);
                this.hurt(DamageSource.DROWN, 2.0F);
            }
        } else {
            this.setAirSupply(300);
        }
    }

    @Override
    public void travel(Vec3 vec) {
        float strafe = (float) vec.x;
        float vertical = (float) vec.y;
        float forward = (float) vec.z;
        boolean noInput = strafe == 0 && vertical == 0 && forward == 0;
        if (!this.level.isClientSide && this.isInWater() && !this.isCarcass()) {
            this.moveRelative(0.1F, new Vec3(strafe, vertical, forward));
            this.move(MoverType.SELF, this.getDeltaMovement());
            Vec3 movement = this.getDeltaMovement().multiply(0.7, 0.7, 0.7);
            if (noInput) {
                movement = movement.add(0.0D, -0.005D, 0.0D);
            }
            if (!this.isUnderWater()) {
                movement = movement.add(0.0D, -0.02D, 0.0D);
            }
            this.setDeltaMovement(movement);
        } else {
            super.travel(vec);
        }
    }

    protected void spawnInk() {
        this.playSound(SoundEvents.SQUID_SQUIRT, this.getSoundVolume(), this.getVoicePitch());
        Vec3 view = this.getViewVector(0.0F);
        double x = view.x;
        double y = view.y;
        double z = view.z;
        for (int i = 0; i < 30; ++i) {
            double dx = this.random.nextGaussian() * 0.02D;
            double dy = this.random.nextGaussian() * 0.02D;
            double dz = this.random.nextGaussian() * 0.02D;
            this.level.addParticle(ParticleTypes.SQUID_INK, this.getX() + x * 0.1D + (this.random.nextDouble() - 0.5D) * 0.6D, this.getY() + y * 0.1D + (this.random.nextDouble() - 0.5D) * 0.6D, this.getZ() + z * 0.1D + (this.random.nextDouble() - 0.5D) * 0.6D, dx, dy, dz);
        }
    }

    class SwimmingMoveHelper extends MoveControl {
        private final SwimmingDinosaurEntity swimmingEntity = SwimmingDinosaurEntity.this;

        public SwimmingMoveHelper() {
            super(SwimmingDinosaurEntity.this);
        }

        @Override
        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                double dx = this.wantedX - this.swimmingEntity.getX();
                double dy = this.wantedY - this.swimmingEntity.getY();
                double dz = this.wantedZ - this.swimmingEntity.getZ();
                double d2 = dx * dx + dy * dy + dz * dz;
                if (d2 < 1.0E-4D) {
                    this.swimmingEntity.setSpeed(0.0F);
                    this.operation = Operation.WAIT;
                    return;
                }
                double dist = Math.sqrt(d2);
                dy /= dist;
                float yaw = (float) (Math.atan2(dz, dx) * 180.0D / Math.PI) - 90.0F;
                this.swimmingEntity.setYRot(this.rotlerp(this.swimmingEntity.getYRot(), yaw, 30));
                float base = (float) this.swimmingEntity.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
                this.swimmingEntity.setSpeed((float) (base * this.speedModifier));
                double rise = (double) this.swimmingEntity.getSpeed() * dy * 0.05D;
                this.swimmingEntity.setDeltaMovement(this.swimmingEntity.getDeltaMovement().add(0, rise, 0));
            } else {
                this.swimmingEntity.setSpeed(0.0F);
            }
        }
    }
}
