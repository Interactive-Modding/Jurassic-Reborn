package net.vit.jurassicreborn.common.entities.animal;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.level.ServerLevelAccessor;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.render.entity.animation.FixedChainBuffer;
import net.vit.jurassicreborn.client.render.entity.animation.PoseHandler;
import net.vit.jurassicreborn.common.entities.EntityUtils.Animatable;
import net.vit.jurassicreborn.common.entities.EntityUtils.GrowthStage;
import net.vit.jurassicreborn.common.entities.EntityUtils.ai.SmartBodyHelper;
import net.vit.jurassicreborn.common.entities.ModEntities;
import net.vit.jurassicreborn.common.items.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import java.util.Random;
import net.vit.jurassicreborn.common.RebornConfig;

import java.util.ArrayList;

public class SharkEntity extends Animal implements Animatable, IEntityAdditionalSpawnData {

    public static final PoseHandler<SharkEntity> SHARK_POSE_HANDLER = new PoseHandler<>("shark", Lists.newArrayList(GrowthStage.ADULT));

    private static final EntityDataAccessor<Boolean> SHARK_IS_RUNNING = SynchedEntityData.defineId(SharkEntity.class, EntityDataSerializers.BOOLEAN);
    public FixedChainBuffer tailBuffer;

    private Animation animation;
    private int animationTick;
    private int animationLength;

    public SharkEntity(EntityType<SharkEntity> sharkEntityEntityType, Level world){
        super(sharkEntityEntityType, world);
        this.maxUpStep = 1.0f;
        this.animationTick = 0;
        this.setPathfindingMalus(BlockPathTypes.WATER, 0);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.setAnimation(EntityAnimation.IDLE.get());
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public int getMaxAirSupply() {
        return 4800;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new HurtByTargetGoal(this, Player.class));
        this.goalSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Goat.class, false));
        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, CrabEntity.class, false));
        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Squid.class, false));
        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, TropicalFish.class, false));
        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Salmon.class, false));
        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Cod.class, false));
        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Turtle.class, false));
        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractFish.class, false));
        this.goalSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Drowned.class, false));

    }

    protected PathNavigation createNavigation(Level pLevel) {
        return new WaterBoundPathNavigation(this, pLevel);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        ArrayList<ItemStack> itemsToDrop = new ArrayList<>();
        if(this.isOnFire()){
            itemsToDrop.add(new ItemStack(ModItems.SHARK_MEAT_COOKED.get(), this.getRandom().nextInt(2)+1));
        }else {
            itemsToDrop.add(new ItemStack(ModItems.SHARK_MEAT_RAW.get(), this.getRandom().nextInt(2)+1));
        }
        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
        for(ItemStack stack : itemsToDrop){
            this.spawnAtLocation(stack);
        }

    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        return new SharkEntity(ModEntities.SHARK.get(), p_146743_);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createLivingAttributes().add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED,  0.25)
                .add(Attributes.ATTACK_DAMAGE, 3.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }


    public boolean doHurtTarget(Entity entityIn)
    {
        float f = (float)this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        int i = 0;

        if (entityIn instanceof LivingEntity l)
        {
            f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), l.getMobType());
            i += EnchantmentHelper.getKnockbackBonus(this);
        }

        boolean flag = entityIn.hurt(DamageSource.mobAttack(this), f);

        if (flag)
        {
            if (i > 0 && entityIn instanceof LivingEntity l)
            {
                l.knockback( (float)i * 0.5F, Mth.sin(this.getYRot() * 0.017453292F), (-Mth.cos(this.getYRot() * 0.017453292F)));
//                this.motionX *= 0.6D;
//                this.motionZ *= 0.6D;
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1, 0.6));
            }

            int j = EnchantmentHelper.getFireAspect(this);

            if (j > 0)
            {
                entityIn.setSecondsOnFire(j * 4);
            }

            if (entityIn instanceof Player entityplayer)
            {
                ItemStack itemstack = this.getMainHandItem();
                ItemStack itemstack1 = entityplayer.isUsingItem() ? entityplayer.getUseItem() : ItemStack.EMPTY;

                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.getItem().canPerformAction(itemstack1, ToolActions.SHIELD_BLOCK))
                {
                    float f1 = 0.25F + (float)EnchantmentHelper.getBlockEfficiency(this) * 0.05F;

                    if (this.random.nextFloat() < f1)
                    {
                        entityplayer.getCooldowns().addCooldown(itemstack1.getItem(), 100);
                        this.level.broadcastEntityEvent(entityplayer, (byte)30);
                    }
                }
            }

            this.doEnchantDamageEffects(this, entityIn);
        }

        return flag;
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(SHARK_IS_RUNNING, false);
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
    public boolean isCarcass() {
        return false;
    }

    @Override
    public boolean isMoving() {
        double powx = this.getDeltaMovement().x;
        powx *= powx;
        double powz = this.getDeltaMovement().z;
        powz *= powz;
        double powy = this.getDeltaMovement().y;
        powy *= powy;
        return powx + powz + powy > 0.001F;
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
    public boolean isRunning() {
        return this.entityData.get(SHARK_IS_RUNNING);
    }

    @Override//why. just why. In legacy code, this was a self-referential thing that stores a variable for the tick. just ***why***. - gamma
    public boolean inLava() {
        return this.isInLava();
    }

    @Override
    public boolean canUseGrowthStage(GrowthStage growthStage) {
        return growthStage.equals(GrowthStage.ADULT);
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
    public boolean isPushedByFluid() {
        return false;
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
    public void writeSpawnData(FriendlyByteBuf buffer) {

    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {

    }
    @javax.annotation.Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData, @javax.annotation.Nullable CompoundTag pDataTag) {
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }
    public static boolean checkSharkSpawnRules(EntityType<SharkEntity> type, LevelAccessor level, MobSpawnType reason, BlockPos pos, Random random) {
        return RebornConfig.spawnSharks
                && pos.getY() <= level.getSeaLevel()
                && level.getFluidState(pos).is(FluidTags.WATER)
                && level.getFluidState(pos.above()).is(FluidTags.WATER);
    }
}
