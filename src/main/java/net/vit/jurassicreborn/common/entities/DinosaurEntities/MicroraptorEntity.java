package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.input.DinosaurKeyHandler;
import net.vit.jurassicreborn.common.network.MicroraptorDismountMessage;
import net.vit.jurassicreborn.common.network.Network;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.ai.LeapingMeleeEntityAI;
import net.vit.jurassicreborn.common.entities.ai.RaptorClimbTreeAI;
import net.vit.jurassicreborn.common.entities.ai.RaptorLeapEntityAI;
import net.vit.jurassicreborn.common.RebornConfig;
// import net.vit.jurassicreborn.network.MicroraptorDismountMessage;
import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.client.Minecraft;
import net.vit.jurassicreborn.common.entities.ai.animations.BirdPreenAnimationAI;
import net.vit.jurassicreborn.common.entities.ai.animations.TailDisplayAnimationAI;

public class MicroraptorEntity extends DinosaurEntity {
    private int flyTime;
    private int groundHeight;
    private Vec3 glidingPos;

    public MicroraptorEntity(EntityType<MicroraptorEntity> type, Level world) {
        super(world, type, DinosaurHandler.MICRORAPTOR);
        this.target(Chicken.class, Rabbit.class, CompsognathusEntity.class, HypsilophodonEntity.class, LeptictidiumEntity.class, MicroceratusEntity.class, OthnieliaEntity.class);
        this.addTask(1, new LeapingMeleeEntityAI(this, this.dinosaur.getAttackSpeed()));
        this.addTask(2, new RaptorClimbTreeAI(this, 1.0f));
        this.addTask(3, new BirdPreenAnimationAI(this));
        this.addTask(3, new TailDisplayAnimationAI(this));
    }

    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        // add any extra data parameters here if needed
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createLivingAttributes().add(Attributes.FOLLOW_RANGE, 35.0D);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return !source.is(DamageTypes.FLY_INTO_WALL) && super.hurt(source, amount);
    }

    @Override
    public void tick() {
        super.tick();

        Animation curAni = this.getAnimation();
        boolean climbing = curAni == EntityAnimation.CLIMBING.get() || curAni == EntityAnimation.START_CLIMBING.get();

        if (climbing) {
            BlockPos trunk = BlockPos.containing(this.getX(), this.getBoundingBox().minY, this.getZ());
            for (net.minecraft.core.Direction dir : net.minecraft.core.Direction.Plane.HORIZONTAL) {
                if (!level.isEmptyBlock(trunk.relative(dir)) && this.level.getBlockState(trunk.relative(dir)).isRedstoneConductor(level, trunk.relative(dir))) {
                    float yaw = dir.toYRot();
                    this.setYHeadRot(yaw);
                    this.setYRot(yaw);
                    this.yRotO = yaw;
                    this.yHeadRotO = yaw;
                    this.setYBodyRot(yaw);
                    this.yBodyRotO = yaw;
                    this.noPhysics = false;
                    break;
                }
            }
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        float prevPitch = this.getXRot();
        if (this.getAnimation() == EntityAnimation.GLIDING.get() && glidingPos != null) {
            double dist = glidingPos.distanceTo(this.position());
            if (dist > 0.0001) {
                double dy = (this.glidingPos.y - this.getY()) / dist;
                this.setXRot((float)-Math.toDegrees(Math.asin(Mth.clamp(dy, -1.0, 1.0))));
            }
        }
        super.travel(travelVector);
        this.setXRot(prevPitch);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.level.isClientSide) {
            this.updateClientControls();
        }

        Animation curAni = this.getAnimation();
        boolean landing = curAni == EntityAnimation.LEAP_LAND.get();
        boolean gliding = curAni == EntityAnimation.GLIDING.get();
        boolean climbing = curAni == EntityAnimation.CLIMBING.get() || curAni == EntityAnimation.START_CLIMBING.get();
        boolean leaping  = curAni == EntityAnimation.LEAP.get();

        if (this.isOnGround() || this.isInWater() || this.isInLava() || this.isSwimming()) {
            this.flyTime = 0;
            if (gliding || landing) {
                this.setAnimation(EntityAnimation.IDLE.get());
                this.setSharedFlag(7, false); // fall-flying off
            }
        } else {
            this.flyTime++;
            if (this.flyTime > 4 && !leaping) {
                if (!landing) {
                    if (!gliding) {
                        if (!climbing) {
                            this.setAnimation(EntityAnimation.GLIDING.get());
                        }
                    } else if (!this.level.isEmptyBlock(this.blockPosition().below())) {
                        this.setAnimation(EntityAnimation.LEAP_LAND.get());
                    }
                }
                if (gliding) {
                    this.setSharedFlag(7, true); // fall-flying on
                }
            }
        }

        if (this.isFallFlying()) {
            this.groundHeight = 0;
            BlockPos pos = this.blockPosition();
            while (this.groundHeight <= 10) {
                if (this.level.getBlockState(pos).isFaceSturdy(this.level, pos, net.minecraft.core.Direction.UP)) {
                    break;
                }
                pos = pos.below();
                this.groundHeight++;
            }
        }

        if (!this.level.isClientSide) {
            this.getLookControl().tick();
        }
    }

    @Override
    public Vec3 getLookAngle() {
        if (this.getAnimation() == EntityAnimation.GLIDING.get() && glidingPos != null) {
            double d = glidingPos.distanceTo(this.position());
            if (d < 1.0E-6) return super.getLookAngle();
            return new Vec3(
                    (glidingPos.x - this.getX()) / d,
                    (glidingPos.y - this.getY()) / d,
                    (glidingPos.z - this.getZ()) / d
            );
        }
        return super.getLookAngle();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.isShiftKeyDown() && hand == InteractionHand.MAIN_HAND) {
            if (this.isOwner(player) && this.order == Order.SIT && player.getPassengers().size() < 2) {
                return this.startRiding(player, true) ? InteractionResult.sidedSuccess(this.level.isClientSide) : InteractionResult.PASS;
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        if (this.getAnimation() == EntityAnimation.GLIDING.get() && slot == EquipmentSlot.CHEST) {
            return new ItemStack(Items.ELYTRA);
        }
        return super.getItemBySlot(slot);
    }

    public Goal getAttackAI() {
        return new RaptorLeapEntityAI(this);
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        if (this.getAnimation() != EntityAnimation.LEAP_LAND.get()) {
            return false;
        }
        return false;
    }

    @Override
    protected WallClimberNavigation createNavigation(Level level) {
        return new WallClimberNavigation(this, level);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:     return SoundHandler.MICRORAPTOR_LIVING;
            case DYING:     return SoundHandler.MICRORAPTOR_DEATH;
            case INJURED:   return SoundHandler.MICRORAPTOR_HURT;
            case ATTACKING: return SoundHandler.MICRORAPTOR_ATTACK;
            case CALLING:   return SoundHandler.MICRORAPTOR_LIVING;
            default:        return null;
        }
    }

    @Override
    public int getMaxFallDistance() {
        return 100;
    }

    @Override
    public void positionRider(Entity passenger) {
        super.positionRider(passenger);
        Entity riding = this.getVehicle();
        if (!this.isPassenger() || !(riding instanceof Player player)) return;

        int idx = riding.getPassengers().indexOf(this); // 0/1 shoulders, 2 head

        if (player.isFallFlying()) {
            this.stopRiding();
            return;
        }

        float radius = (idx == 2 ? 0.0F : 0.35F) + (player.isFallFlying() ? 2.0F : 0.0F);
        float renderYawOffset = player.yBodyRot; // == renderYawOffset
        float add = (idx == 1 ? -90f : (idx == 0 ? 90f : 0f));
        float angle = (float) Math.toRadians(renderYawOffset + add);

        double offsetX = radius * Mth.sin((float) (Math.PI + angle));
        double offsetZ = radius * Mth.cos(angle);

        double offsetY = (player.isShiftKeyDown() ? 1.2 : 1.38) + (idx == 2 ? 0.4 : 0.0);

        this.setPos(riding.getX() + offsetX, riding.getY() + offsetY, riding.getZ() + offsetZ);

        float headYaw = player.getYHeadRot();
        this.setYRot(headYaw);
        this.setYHeadRot(headYaw);
        this.yRotO = headYaw;

        // Idle while perched
        this.setAnimation(EntityAnimation.IDLE.get());
    }




    @OnlyIn(Dist.CLIENT)
    protected void updateClientControls() {
        if (this.getVehicle() != null && this.getVehicle() == Minecraft.getInstance().player) {
            if (DinosaurKeyHandler.MICRORAPTOR_DISMOUNT.consumeClick()) {
                Network.sendToServer(new MicroraptorDismountMessage(this.getId()));
            }
        }
    }

    @Override
    public boolean canDinoSwim() {
        return true;
    }

    @Override
    public boolean shouldEscapeWaterFast() {
        int radiusXZ = 4;
        BlockPos min = new BlockPos(Mth.floor(this.getX() - radiusXZ), Mth.floor(this.getY()), Mth.floor(this.getZ() - radiusXZ));
        BlockPos max = new BlockPos(Mth.ceil(this.getX() + radiusXZ),  Mth.ceil(this.getY()),  Mth.ceil(this.getZ() + radiusXZ));
        for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
            if (!level.getBlockState(pos).getMaterial().isLiquid()) {
                return false;
            }
        }
        return false;
    }

    public void setGlidingTo(Vec3 glidingPos) {
        this.glidingPos = glidingPos;
    }
}
