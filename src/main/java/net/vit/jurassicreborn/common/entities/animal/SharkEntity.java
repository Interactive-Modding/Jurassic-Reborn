package net.vit.jurassicreborn.common.entities.animal;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.render.entity.animation.FixedChainBuffer;
import net.vit.jurassicreborn.client.render.entity.animation.PoseHandler;
import net.vit.jurassicreborn.common.JurassicConfig;
import net.vit.jurassicreborn.common.entities.EntityUtils.Animatable;
import net.vit.jurassicreborn.common.entities.EntityUtils.GrowthStage;
import net.vit.jurassicreborn.common.entities.EntityUtils.ai.SmartBodyHelper;
import net.vit.jurassicreborn.common.entities.ModEntities;
import net.vit.jurassicreborn.common.items.ModItems;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.EnumSet;

public class SharkEntity extends Animal implements Animatable, IEntityWithComplexSpawn {

    public static final PoseHandler<SharkEntity> SHARK_POSE_HANDLER =
            new PoseHandler<>("shark", Lists.newArrayList(GrowthStage.ADULT));

    private static final EntityDataAccessor<Boolean> SHARK_IS_RUNNING =
            SynchedEntityData.defineId(SharkEntity.class, EntityDataSerializers.BOOLEAN);

    private static final double IDLE_SINK = 0.010D;
    private static final double SURFACE_SINK = 0.040D;
    private static final double CHASE_SURFACE_SINK = 0.018D;
    private static final double MAX_SURFACE_RISE_IDLE = 0.0D;
    private static final double MAX_SURFACE_RISE_CHASE = 0.025D;

    public FixedChainBuffer tailBuffer;

    private Animation animation;
    private int animationTick;
    private int animationLength;

    public SharkEntity(EntityType<? extends SharkEntity> type, Level level) {
        super(type, level);
        this.animationTick = 0;

        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 0.0F);

        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.10F, false);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);

        if (level.isClientSide) {
            this.tailBuffer = new FixedChainBuffer();
        }

        this.setAnimation(EntityAnimation.IDLE.get());
    }
    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }
    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(1, new SharkAttackGoal(this, 1.35D));
        this.goalSelector.addGoal(2, new SharkReturnToWaterGoal(this, 1.2D));
        this.goalSelector.addGoal(3, new SharkSwimGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, CrabEntity.class, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Squid.class, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractFish.class, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, TropicalFish.class, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Salmon.class, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Cod.class, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Turtle.class, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Drowned.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Goat.class, 10, false, false,
                living -> living.isInWaterOrBubble()));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SHARK_IS_RUNNING, false);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            AnimationHandler.INSTANCE.updateAnimations(this);
            if (this.tailBuffer != null) {
                this.tailBuffer.calculateChainSwingBuffer(68.0F, 3, 7.0F, this);
            }
        }

        if (!this.level().isClientSide) {
            this.entityData.set(SHARK_IS_RUNNING, this.getDeltaMovement().horizontalDistanceSqr() > 0.0025D);
        }

        this.updateAnimationState();
        this.handleAirSupply();

        if (!this.isInWaterOrBubble() && this.onGround()) {
            if (this.tickCount % 10 == 0) {
                this.setDeltaMovement(
                        (this.random.nextDouble() - 0.5D) * 0.12D,
                        0.18D,
                        (this.random.nextDouble() - 0.5D) * 0.12D
                );
                this.hasImpulse = true;
            }
        }
    }

    private void updateAnimationState() {
        if (this.animation == null) {
            this.setAnimation(EntityAnimation.IDLE.get());
            return;
        }

        if (this.animation == EntityAnimation.ATTACKING.get()) {
            if (this.animationTick < this.animationLength) {
                this.animationTick++;
            } else {
                this.animationTick = 0;
                this.setAnimation(EntityAnimation.IDLE.get());
            }
        } else {
            this.animationTick = 0;
        }
    }

    private void handleAirSupply() {
        if (this.isInWaterOrBubble()) {
            this.setAirSupply(this.getMaxAirSupply());
            return;
        }

        int air = this.getAirSupply() - 1;
        this.setAirSupply(air);

        if (air <= -20) {
            this.setAirSupply(0);
            this.hurt(this.damageSources().drown(), 2.0F);
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(0.02F, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());

            Vec3 movement = this.getDeltaMovement().scale(0.90D);
            boolean chasing = this.getTarget() != null;
            boolean nearSurface = this.isNearSurface();

            if (!chasing) {
                movement = movement.add(0.0D, -IDLE_SINK, 0.0D);
            }

            if (nearSurface) {
                if (chasing) {
                    movement = movement.add(0.0D, -CHASE_SURFACE_SINK, 0.0D);
                    movement = new Vec3(movement.x, Math.min(movement.y, MAX_SURFACE_RISE_CHASE), movement.z);
                } else {
                    movement = movement.add(0.0D, -SURFACE_SINK, 0.0D);
                    movement = new Vec3(movement.x, Math.min(movement.y, MAX_SURFACE_RISE_IDLE), movement.z);
                }
            }

            if (!chasing && !this.isUnderWater()) {
                movement = movement.add(0.0D, -0.010D, 0.0D);
            }

            this.setDeltaMovement(movement);
        } else {
            super.travel(travelVector);
        }
    }

    private boolean isNearSurface() {
        BlockPos.MutableBlockPos cursor = this.blockPosition().mutable();

        for (int i = 1; i <= 2; i++) {
            cursor.move(0, 1, 0);
            if (!this.level().getFluidState(cursor).is(FluidTags.WATER)) {
                return true;
            }
        }

        return false;
    }


    @Override
    public int getMaxAirSupply() {
        return 4800;
    }

    @Override
    protected float getWaterSlowDown() {
        return 1.0F;
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new SmartBodyHelper(this);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean isCarcass() {
        return false;
    }

    @Override
    public boolean isMoving() {
        Vec3 motion = this.getDeltaMovement();
        return motion.lengthSqr() > 0.001D;
    }

    @Override
    public boolean isClimbing() {
        return false;
    }

    @Override
    public boolean inWater() {
        return this.isInWater();
    }

    @Override
    public boolean inLava() {
        return this.isInLava();
    }

    @Override
    public boolean isRunning() {
        return this.entityData.get(SHARK_IS_RUNNING);
    }

    @Override
    public boolean canUseGrowthStage(GrowthStage growthStage) {
        return growthStage == GrowthStage.ADULT;
    }

    @Override
    public boolean isMarineCreature() {
        return true;
    }

    @Override
    public boolean shouldUseInertia() {
        return true;
    }

    @Override
    public GrowthStage getGrowthStage() {
        return GrowthStage.ADULT;
    }

    @Override
    public PoseHandler<SharkEntity> getPoseHandler() {
        return SHARK_POSE_HANDLER;
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int i) {
        this.animationTick = i;
    }

    @Override
    public Animation getAnimation() {
        return this.animation;
    }

    @Override
    public void setAnimation(Animation newAnimation) {
        Animation oldAnimation = this.animation;
        this.animation = newAnimation;
        if (oldAnimation != newAnimation) {
            this.animationTick = 0;
            this.animationLength = (int) this.getPoseHandler().getAnimationLength(this.animation, this.getGrowthStage());
            AnimationHandler.INSTANCE.sendAnimationMessage(this, newAnimation);
        }
    }

    @Override
    public Animation[] getAnimations() {
        return EntityAnimation.getAnimations();
    }

    @Override
    protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean recentlyHit) {
        ArrayList<ItemStack> itemsToDrop = new ArrayList<>();

        if (this.isOnFire()) {
            itemsToDrop.add(new ItemStack(ModItems.SHARK_MEAT_COOKED.get(), this.getRandom().nextInt(2) + 1));
        } else {
            itemsToDrop.add(new ItemStack(ModItems.SHARK_MEAT_RAW.get(), this.getRandom().nextInt(2) + 1));
        }

        super.dropCustomDeathLoot(level, source, recentlyHit);

        for (ItemStack stack : itemsToDrop) {
            this.spawnAtLocation(stack);
        }
    }
    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return new SharkEntity(ModEntities.SHARK.get(), level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 18.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.28D)
                .add(Attributes.ATTACK_DAMAGE, 4.0D)
                .add(Attributes.FOLLOW_RANGE, 24.0D);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        this.setAnimation(EntityAnimation.ATTACKING.get());

        float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        boolean flag = target.hurt(this.damageSources().mobAttack(this), damage);

        if (flag) {
            if (target instanceof LivingEntity living) {
                float knockback = 0.5F;
                living.knockback(
                        knockback,
                        Mth.sin(this.getYRot() * ((float) Math.PI / 180F)),
                        -Mth.cos(this.getYRot() * ((float) Math.PI / 180F))
                );
            }

            this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
        }

        return flag;
    }
    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf buffer) {
    }
    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level,
                                        DifficultyInstance difficulty,
                                        MobSpawnType reason,
                                        @Nullable SpawnGroupData spawnData) {
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    public static boolean checkSharkSpawnRules(EntityType<SharkEntity> type, LevelAccessor level, MobSpawnType reason,
                                               BlockPos pos, RandomSource random) {
        return JurassicConfig.spawnSharks
                && pos.getY() <= level.getSeaLevel()
                && level.getFluidState(pos).is(FluidTags.WATER)
                && level.getFluidState(pos.above()).is(FluidTags.WATER);
    }

    private static class SharkAttackGoal extends Goal {
        private final SharkEntity shark;
        private final double speedModifier;
        private int attackCooldown;
        private int repathTime;

        public SharkAttackGoal(SharkEntity shark, double speedModifier) {
            this.shark = shark;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.shark.getTarget();
            return target != null && target.isAlive() && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target);
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = this.shark.getTarget();
            return target != null
                    && target.isAlive()
                    && EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)
                    && (this.shark.isInWaterOrBubble() || target.isInWaterOrBubble());
        }

        @Override
        public void start() {
            this.attackCooldown = 0;
            this.repathTime = 0;
        }

        @Override
        public void stop() {
            this.shark.getNavigation().stop();
        }

        @Override
        public void tick() {
            LivingEntity target = this.shark.getTarget();
            if (target == null) {
                return;
            }

            this.shark.getLookControl().setLookAt(target, 30.0F, 30.0F);

            if (this.repathTime-- <= 0) {
                this.repathTime = 8;
                this.shark.getNavigation().moveTo(target, this.speedModifier);
            }

            Vec3 toTarget = target.position().add(0.0D, target.getBbHeight() * 0.5D, 0.0D)
                    .subtract(this.shark.position());
            double distSq = toTarget.lengthSqr();
            double attackReachSq = this.getAttackReachSq(target);

            if (this.shark.isInWater()) {
                Vec3 dir = toTarget.normalize();
                Vec3 motion = this.shark.getDeltaMovement();

                this.shark.setDeltaMovement(
                        Mth.lerp(0.18D, motion.x, dir.x * 0.16D),
                        Mth.lerp(0.10D, motion.y, dir.y * 0.07D),
                        Mth.lerp(0.18D, motion.z, dir.z * 0.16D)
                );
                this.shark.hasImpulse = true;
            }

            if (this.attackCooldown > 0) {
                this.attackCooldown--;
            }

            if (distSq <= attackReachSq && this.attackCooldown <= 0) {
                this.attackCooldown = 18;
                this.shark.doHurtTarget(target);
            }
        }

        private double getAttackReachSq(LivingEntity target) {
            double width = this.shark.getBbWidth() * 1.8D;
            return width * width + target.getBbWidth();
        }
    }

    private static class SharkReturnToWaterGoal extends Goal {
        private final SharkEntity shark;
        private final double speedModifier;
        @Nullable
        private BlockPos targetPos;

        public SharkReturnToWaterGoal(SharkEntity shark, double speedModifier) {
            this.shark = shark;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.shark.isInWaterOrBubble()) {
                return false;
            }
            this.targetPos = findNearestWater();
            return this.targetPos != null;
        }

        @Override
        public boolean canContinueToUse() {
            return !this.shark.isInWaterOrBubble()
                    && this.targetPos != null
                    && !this.shark.getNavigation().isDone();
        }

        @Override
        public void start() {
            if (this.targetPos != null) {
                this.shark.getNavigation().moveTo(
                        this.targetPos.getX() + 0.5D,
                        this.targetPos.getY() + 0.5D,
                        this.targetPos.getZ() + 0.5D,
                        this.speedModifier
                );
            }
        }

        @Override
        public void stop() {
            this.targetPos = null;
        }

        @Nullable
        private BlockPos findNearestWater() {
            BlockPos origin = this.shark.blockPosition();

            for (int r = 2; r <= 12; r += 2) {
                for (int dx = -r; dx <= r; dx++) {
                    for (int dz = -r; dz <= r; dz++) {
                        for (int dy = -3; dy <= 3; dy++) {
                            BlockPos pos = origin.offset(dx, dy, dz);
                            if (this.shark.level().getFluidState(pos).is(FluidTags.WATER)
                                    && this.shark.level().getFluidState(pos.above()).is(FluidTags.WATER)) {
                                return pos;
                            }
                        }
                    }
                }
            }
            return null;
        }
    }

    private static class SharkSwimGoal extends Goal {
        private final SharkEntity shark;
        private final double speedModifier;
        @Nullable
        private BlockPos targetPos;

        public SharkSwimGoal(SharkEntity shark, double speedModifier) {
            this.shark = shark;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.shark.getTarget() != null) {
                return false;
            }

            if (this.shark.getRandom().nextInt(40) != 0) {
                return false;
            }

            this.targetPos = this.findWaterTarget();
            return this.targetPos != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.shark.getTarget() == null
                    && this.targetPos != null
                    && !this.shark.getNavigation().isDone();
        }

        @Override
        public void start() {
            if (this.targetPos != null) {
                this.shark.getNavigation().moveTo(
                        this.targetPos.getX() + 0.5D,
                        this.targetPos.getY() + 0.5D,
                        this.targetPos.getZ() + 0.5D,
                        this.speedModifier
                );
            }
        }

        @Override
        public void stop() {
            this.targetPos = null;
        }

        private BlockPos findWaterTarget() {
            BlockPos origin = this.shark.blockPosition();

            for (int i = 0; i < 28; i++) {
                int dx = this.shark.getRandom().nextInt(25) - 12;
                int dz = this.shark.getRandom().nextInt(25) - 12;
                int dy = this.shark.getRandom().nextInt(9) - 6;

                BlockPos pos = origin.offset(dx, dy, dz);
                if (isGoodWaterPos(pos)) {
                    return pos;
                }
            }

            return null;
        }

        private boolean isGoodWaterPos(BlockPos pos) {
            return this.shark.level().getFluidState(pos).is(FluidTags.WATER)
                    && this.shark.level().getFluidState(pos.above()).is(FluidTags.WATER)
                    && this.shark.level().getFluidState(pos.below()).is(FluidTags.WATER);
        }
    }
}
