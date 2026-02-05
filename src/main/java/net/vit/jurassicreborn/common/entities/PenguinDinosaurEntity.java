package net.vit.jurassicreborn.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.ai.navigation.DinosaurMoveHelper;
import net.vit.jurassicreborn.common.entities.ai.navigation.DinosaurPathNavigate;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public abstract class PenguinDinosaurEntity extends DinosaurEntity {
    private boolean getOut = false;
    private boolean getInWater = false;
    private boolean blocked;

    // Use two navigators and swap them
    protected final PathNavigation navigationSwimmer;
    protected final PathNavigation navigationLand;

    // Swap-able controls
    private final SmoothSwimmingMoveControl waterMoveControl;
    private final SmoothSwimmingLookControl waterLookControl;
    private final DinosaurMoveHelper        landMoveControl;
    private final LookControl               landLookControl;

    private int waterTicks;
    private int landTicks;

    public PenguinDinosaurEntity(Level world, EntityType type, Dinosaur dino) {
        super(world, type, dino);

        // WATER: smooth swimming controls + navigator
        this.waterMoveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.waterLookControl = new SmoothSwimmingLookControl(this, 10);
        this.navigationSwimmer = new WaterBoundPathNavigation(this, world);

        // LAND: use the project’s walking stack (move helper + path navigator)
        this.landMoveControl = new DinosaurMoveHelper(this);
        this.landLookControl = new LookControl(this); // vanilla look control is fine for land
        this.navigationLand  = new DinosaurPathNavigate(this, world);
        this.navigationLand.setCanFloat(true);

        // Start on land by default
        this.moveControl = this.landMoveControl;
        this.lookControl = this.landLookControl;
        this.navigation  = this.navigationLand;

        this.blocked = false;


        this.goalSelector.addGoal(5,  new MoveUnderwaterGoal());
        this.goalSelector.addGoal(10, new FindWaterGoal());
        this.goalSelector.addGoal(10, new WanderGoal());
    }

    @Override
    public boolean isMovementBlocked() {
        return this.isCarcass() || this.isSleeping() || blocked;
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide && this.isAlive()) {
            if (this.isInWater()) {
                waterTicks++;
                landTicks = 0;
                this.setAirSupply(300);
                getOut = waterTicks > 200;  // lingered long → prefer land soon
                getInWater = false;
                this.navigation = navigationSwimmer;
            } else {
                landTicks++;
                waterTicks = 0;

                int air = this.getAirSupply() - 1;
                this.setAirSupply(air);
                if (air <= -20) {
                    this.setAirSupply(0);
                    this.hurt(this.damageSources().drown(), 2.0F);
                }
                getInWater = air < 40;
                getOut = false;
                this.navigation = navigationLand;
            }
        }

        // Swap controls every tick so both sides (S/C) stay in sync
        if (this.isInWater()) {
            if (this.moveControl != this.waterMoveControl) this.moveControl = this.waterMoveControl;
            if (this.lookControl != this.waterLookControl) this.lookControl = this.waterLookControl;
            if (this.navigation != this.navigationSwimmer)  this.navigation  = this.navigationSwimmer;
        } else {
            if (this.moveControl != this.landMoveControl) this.moveControl = this.landMoveControl;
            if (this.lookControl != this.landLookControl) this.lookControl = this.landLookControl;
            if (this.navigation != this.navigationLand)    this.navigation  = this.navigationLand;
        }
    }
    public void tick() {
        super.tick();
        if (this.level().isClientSide && this.isInWater() && this.getDeltaMovement().lengthSqr() > 0.005) {
            Vec3 viewVec = this.getViewVector(0.0F);
            float offsetX = Mth.cos(this.getYRot() * 0.017453292F) * 0.3F;
            float offsetZ = Mth.sin(this.getYRot() * 0.017453292F) * 0.3F;
            float distance = 1.2F - this.random.nextFloat() * 0.7F;

            for (int i = 0; i < 2; ++i) {
                this.level().addParticle(
                        ParticleTypes.DOLPHIN,
                        this.getX() - viewVec.x * (double) distance + (double) offsetX,
                        this.getY() - viewVec.y,
                        this.getZ() - viewVec.z * (double) distance + (double) offsetZ,
                        0.0, 0.0, 0.0
                );
                this.level().addParticle(
                        ParticleTypes.DOLPHIN,
                        this.getX() - viewVec.x * (double) distance - (double) offsetX,
                        this.getY() - viewVec.y,
                        this.getZ() - viewVec.z * (double) distance - (double) offsetZ,
                        0.0, 0.0, 0.0
                );
            }
        }
    }
    @Override
    public void travel(Vec3 vec) {
        float strafe  = (float) vec.x;
        float vertical= (float) vec.y;
        float forward = (float) vec.z;
        boolean noInput = strafe == 0 && vertical == 0 && forward == 0;

        if (!this.level().isClientSide && this.isInWater() && !this.isCarcass()) {
            this.moveRelative(0.25F, new Vec3(strafe, vertical, forward));
            this.move(MoverType.SELF, this.getDeltaMovement());
            Vec3 movement = this.getDeltaMovement().multiply(0.7, 0.7, 0.7);
            if (noInput) movement = movement.add(0.0D, -0.005D, 0.0D);
            this.setDeltaMovement(movement);
        } else {
            // On land, delegate to DinosaurEntity walking logic
            super.travel(vec);
        }
    }

    // === Goals ===

    class MoveUnderwaterGoal extends Goal {
        private double x, y, z;

        public MoveUnderwaterGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            RandomSource rng = PenguinDinosaurEntity.this.getRandom();
            if (PenguinDinosaurEntity.this.getTarget() != null) return false;
            if (rng.nextFloat() < 0.50F && PenguinDinosaurEntity.this.isBusy()) return false;

            Vec3 target = getOut
                    ? getRandomLandPos(PenguinDinosaurEntity.this, 6, 6)
                    : getRandomWaterPos(PenguinDinosaurEntity.this, 6, 6);

            if (target == null) return false;
            this.x = target.x; this.y = target.y; this.z = target.z;
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            if (PenguinDinosaurEntity.this.getTarget() != null) return false;
            return !PenguinDinosaurEntity.this.getNavigation().isDone();
        }

        @Override
        public void start() {
            PenguinDinosaurEntity.this.getNavigation().moveTo(this.x, this.y, this.z, 1.0D);
        }

        @Override
        public boolean isInterruptable() { return true; }

        @Nullable
        private Vec3 getRandomWaterPos(Mob mob, int hr, int vr) {
            for (int i = 0; i < 10; i++) {
                double x = mob.getX() + mob.getRandom().nextInt(hr * 2 + 1) - hr;
                double y = mob.getY() + mob.getRandom().nextInt(vr * 2 + 1) - vr;
                double z = mob.getZ() + mob.getRandom().nextInt(hr * 2 + 1) - hr;
                BlockPos pos = BlockPos.containing(x, y, z);
                if (!mob.level().getBlockState(pos).getFluidState().isEmpty()) {
                    return new Vec3(x, y, z);
                }
            }
            return null;
        }

        @Nullable
        private Vec3 getRandomLandPos(Mob mob, int hr, int vr) {
            for (int i = 0; i < 10; i++) {
                double x = mob.getX() + mob.getRandom().nextInt(hr * 2 + 1) - hr;
                double y = mob.getY() + mob.getRandom().nextInt(vr * 2 + 1) - vr;
                double z = mob.getZ() + mob.getRandom().nextInt(hr * 2 + 1) - hr;
                BlockPos pos = BlockPos.containing(x, y, z);
                if (mob.level().getBlockState(pos).isSolid()) {
                    return new Vec3(x, y, z);
                }
            }
            return null;
        }
    }

    class FindWaterGoal extends Goal {
        public FindWaterGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }
        @Override
        public boolean canUse() {

            return false;
        }
    }

    class WanderGoal extends RandomStrollGoal {
        public WanderGoal() {
            super(PenguinDinosaurEntity.this, 1.0D, 10);
        }
        @Override
        public boolean canUse() {
            if (getInWater) return false;                  // don’t wander if we need water
            return !PenguinDinosaurEntity.this.isInWater() && super.canUse();
        }
    }
}
