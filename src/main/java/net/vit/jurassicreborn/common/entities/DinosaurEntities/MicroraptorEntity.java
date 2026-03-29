package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.client.input.DinosaurKeyHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.ai.LeapingMeleeEntityAI;
import net.vit.jurassicreborn.common.entities.ai.RaptorClimbTreeAI;
import net.vit.jurassicreborn.common.entities.ai.RaptorLeapEntityAI;
import net.vit.jurassicreborn.common.entities.ai.animations.BirdPreenAnimationAI;
import net.vit.jurassicreborn.common.entities.ai.animations.TailDisplayAnimationAI;
import net.vit.jurassicreborn.common.network.MicroraptorDismountMessage;
import net.vit.jurassicreborn.common.network.Network;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;

import com.github.alexthe666.citadel.animation.Animation;

public class MicroraptorEntity extends DinosaurEntity {

    private int flyTime;
    private int groundHeight;
    private Vec3 glidingPos;
    public static final EntityDataAccessor<Boolean> ON_SHOULDER =
            SynchedEntityData.defineId(MicroraptorEntity.class, EntityDataSerializers.BOOLEAN);

    public MicroraptorEntity(EntityType<MicroraptorEntity> type, Level world) {
        super(world, type, DinosaurHandler.MICRORAPTOR);
        this.target(Chicken.class, Rabbit.class, CompsognathusEntity.class,
                HypsilophodonEntity.class, LeptictidiumEntity.class,
                MicroceratusEntity.class, OthnieliaEntity.class);
        this.addTask(1, new LeapingMeleeEntityAI(this, this.dinosaur.getAttackSpeed()));
        this.addTask(2, new RaptorClimbTreeAI(this, 1.0f));
        this.addTask(3, new BirdPreenAnimationAI(this));
        this.addTask(3, new TailDisplayAnimationAI(this));
    }

    @Override
    public void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ON_SHOULDER, false);
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
        boolean climbing = curAni == EntityAnimation.CLIMBING.get()
                || curAni == EntityAnimation.START_CLIMBING.get();

        if (climbing) {
            BlockPos trunk = BlockPos.containing(
                    this.getX(), this.getBoundingBox().minY, this.getZ());
            for (net.minecraft.core.Direction dir : net.minecraft.core.Direction.Plane.HORIZONTAL) {
                BlockPos neighbour = trunk.relative(dir);
                if (!level().isEmptyBlock(neighbour)
                        && this.level().getBlockState(neighbour)
                        .isRedstoneConductor(level(), neighbour)) {
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
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        if (this.getAnimation() != EntityAnimation.LEAP_LAND.get()) {
            return super.causeFallDamage(distance / 2.0f, damageMultiplier, source);
        }
        return false;
    }

    @Override
    public int getMaxFallDistance() {
        return 100;
    }

    @Override
    public void travel(Vec3 travelVector) {
        float prevPitch = this.getXRot();
        if (this.getAnimation() == EntityAnimation.GLIDING.get() && glidingPos != null) {
            double dist = glidingPos.distanceTo(this.position());
            if (dist > 0.0001) {
                double dy = (this.glidingPos.y - this.getY()) / dist;
                this.setXRot((float) -Math.toDegrees(Math.asin(Mth.clamp(dy, -1.0, 1.0))));
            }
        }
        super.travel(travelVector);
        this.setXRot(prevPitch);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (this.level().isClientSide) {
            this.updateClientControls();
        }

        Animation curAni = this.getAnimation();
        boolean landing = curAni == EntityAnimation.LEAP_LAND.get();
        boolean gliding = curAni == EntityAnimation.GLIDING.get();
        boolean climbing = curAni == EntityAnimation.CLIMBING.get()
                || curAni == EntityAnimation.START_CLIMBING.get();
        boolean leaping = curAni == EntityAnimation.LEAP.get();

        if (this.onGround() || this.isInWater() || this.isInLava() || this.isSwimming()) {
            this.flyTime = 0;
            if (gliding || landing) {
                this.setAnimation(EntityAnimation.IDLE.get());
                this.setSharedFlag(7, false);
            }
        } else {
            this.flyTime++;
            if (this.flyTime > 4 && !leaping) {
                if (!landing) {
                    if (!gliding) {
                        if (!climbing) {
                            this.setAnimation(EntityAnimation.GLIDING.get());
                        }
                    } else if (!this.level().isEmptyBlock(this.blockPosition().below())) {
                        this.setAnimation(EntityAnimation.LEAP_LAND.get());
                    }
                }
                if (gliding) {
                    this.setSharedFlag(7, true);
                }
            }
        }

        if (this.isFallFlying()) {
            this.groundHeight = 0;
            BlockPos pos = this.blockPosition();
            while (this.groundHeight <= 10) {
                if (this.level().getBlockState(pos).isFaceSturdy(
                        this.level(), pos, net.minecraft.core.Direction.UP)) {
                    break;
                }
                pos = pos.below();
                this.groundHeight++;
            }
        }

        if (!this.level().isClientSide) {
            this.getLookControl().tick();
        }
    }

    private boolean setEntityOnShoulder(ServerPlayer player) {
        if (!this.isAlive()) return false;

        CompoundTag tag = new CompoundTag();
        tag.putString("id", this.getEncodeId());
        this.saveWithoutId(tag);

        if (!player.setEntityOnShoulder(tag)) {
            return false;
        }

        this.entityData.set(ON_SHOULDER, true);
        this.setInvisible(true);

        this.discard();
        return true;
    }
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) return super.mobInteract(player, hand);

        if (player.isShiftKeyDown() && this.isOwner(player) && this.order == Order.SIT) {
            if (!level().isClientSide && player instanceof ServerPlayer sp) {
                if (this.setEntityOnShoulder(sp)) {
                    return InteractionResult.CONSUME;
                }
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        return super.mobInteract(player, hand);
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
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        if (this.getAnimation() == EntityAnimation.GLIDING.get()
                && slot == EquipmentSlot.CHEST) {
            return new ItemStack(Items.ELYTRA);
        }
        return super.getItemBySlot(slot);
    }

    public Goal getAttackAI() {
        return new RaptorLeapEntityAI(this);
    }

    protected PathNavigation createNavigation(Level level) {
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
        BlockPos min = new BlockPos(
                Mth.floor(this.getX() - radiusXZ),
                Mth.floor(this.getY()),
                Mth.floor(this.getZ() - radiusXZ));
        BlockPos max = new BlockPos(
                Mth.ceil(this.getX() + radiusXZ),
                Mth.ceil(this.getY()),
                Mth.ceil(this.getZ() + radiusXZ));
        for (BlockPos pos : BlockPos.betweenClosed(min, max)) {
            if (level().getBlockState(pos).getFluidState().isEmpty()) {
                return false;
            }
        }
        return false;
    }

    public void setGlidingTo(Vec3 glidingPos) {
        this.glidingPos = glidingPos;
    }
}
