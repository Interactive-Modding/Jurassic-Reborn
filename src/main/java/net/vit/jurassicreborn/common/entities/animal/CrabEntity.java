package net.vit.jurassicreborn.common.entities.animal;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.google.common.collect.Lists;
import com.mojang.math.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.render.entity.animation.PoseHandler;
import net.vit.jurassicreborn.common.entities.EntityUtils.Animatable;
import net.vit.jurassicreborn.common.entities.EntityUtils.GrowthStage;
import net.vit.jurassicreborn.common.entities.EntityUtils.ai.SmartBodyHelper;
import net.vit.jurassicreborn.common.entities.ModEntities;
import net.vit.jurassicreborn.common.entities.ai.WanderAroundWaterAI;
import net.vit.jurassicreborn.common.items.ModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.vit.jurassicreborn.common.RebornConfig;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class CrabEntity extends Animal implements Animatable, IEntityAdditionalSpawnData {

    private static final PoseHandler<CrabEntity> CRAB_POSE_HANDLER = new PoseHandler<>("crab", Lists.newArrayList(GrowthStage.ADULT));

    public static final EntityDataAccessor<Boolean> CRAB_IS_RUNNING = SynchedEntityData.defineId(CrabEntity.class, EntityDataSerializers.BOOLEAN);

    private Animation animation;
    private int animationTick;
    private int animationLength;
    private boolean alternative;

    public CrabEntity(EntityType<? extends Animal> p_27557_, Level p_27558_) {
        super(p_27557_, p_27558_);
        this.setMaxUpStep(1.0f);
        this.animationTick = 0;
        this.setAnimation(EntityAnimation.IDLE.get());
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(10, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(10, new WanderAroundWaterAI(this, 1, 5, 2));
        this.goalSelector.addGoal(8, new AvoidEntityGoal<>(this, SharkEntity.class, 9.0F, 1.0F, 1.45F));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));

    }



    @Override
    protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        ArrayList<ItemStack> itemsToDrop = new ArrayList<>();
        if(this.isOnFire()){
            itemsToDrop.add(new ItemStack(ModItems.CRAB_MEAT_COOKED.get(), this.getRandom().nextInt(2)+1));
        }else {
            itemsToDrop.add(new ItemStack(ModItems.CRAB_MEAT_RAW.get(), this.getRandom().nextInt(2)+1));
        }
        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
        for(ItemStack stack : itemsToDrop){
            this.spawnAtLocation(stack);
        }

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
    public void tick() {
        super.tick();
        if (this.animation != null && this.animation != EntityAnimation.IDLE.get()) {
            boolean shouldHold = EntityAnimation.getAnimation(this.animation).shouldHold();
            if (this.animationTick < this.animationLength) {
                this.animationTick++;
            } else if (!shouldHold) {
                this.animationTick = 0;
                this.setAnimation(EntityAnimation.IDLE.get());
            } else {
                this.animationTick = this.animationLength - 1;
            }
        }
        if (!this.level().isClientSide) {
            this.entityData.set(CRAB_IS_RUNNING, this.getSpeed() > this.getAttributeValue(Attributes.MOVEMENT_SPEED));
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(CRAB_IS_RUNNING, false);
    }

    @Override
    public boolean isClimbing() {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 7.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FOLLOW_RANGE, 16.0);

    }



    @Override
    protected BodyRotationControl createBodyControl() {
        return new SmartBodyHelper(this);
    }

    @Override
    public boolean inWater() {
        return this.isInWater();
    }

    @Override
    public boolean isRunning() {
        return this.entityData.get(CRAB_IS_RUNNING);
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
    public PoseHandler<CrabEntity> getPoseHandler() {
        return CRAB_POSE_HANDLER;
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

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        return new CrabEntity(ModEntities.CRAB.get(), p_146743_);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {

    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {

    }

    @Override
    public void baseTick() {
        int i = this.getAirSupply();
        super.baseTick();

        if (this.isAlive() && !this.isInWater()) {
            --i;
            this.setAirSupply(i);
        } else {
            this.setAirSupply(300);
        }
    }

    @Override
    public void moveRelative(float friction, Vec3 rel) {
        double up = rel.y;
        double forward = rel.z;
        double strafe = rel.x;
        float f = (float) (strafe * strafe + up * up + forward * forward);
        if (f >= 1.0E-4F)
        {
            f = Mth.sqrt(f);
            if (f < 1.0F) f = 1.0F;
            f = friction / f;
            strafe = strafe * f;
            up = up * f;
            forward = forward * f;
            float f1 = Mth.sin(this.getYRot() * Constants.DEG_TO_RAD);
            float f2 = Mth.cos(this.getYRot() * Constants.DEG_TO_RAD);
            double deltaMotionX = (double)(strafe * f2 - forward * f1);
            double deltaMotionY = (double)up;
            double deltaMotionZ = (double)(forward * f2 + strafe * f1);
            Vec3 deltaMotion = new Vec3(deltaMotionX, deltaMotionY, deltaMotionZ);
            this.setDeltaMovement(this.getDeltaMovement().add(deltaMotion));
        }
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }



    public Type getCrabType() {
        return this.alternative ? Type.ALTERNATIVE : Type.CRAB;
    }
    @javax.annotation.Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @javax.annotation.Nullable SpawnGroupData pSpawnData, @javax.annotation.Nullable CompoundTag pDataTag) {
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }
    public static boolean checkCrabSpawnRules(EntityType<CrabEntity> type, LevelAccessor pLevel, MobSpawnType reason, BlockPos pPos, RandomSource random) {
        return RebornConfig.spawnCrabs && pPos.getY() < pLevel.getSeaLevel() + 4 && isBrightEnoughToSpawn(pLevel, pPos);
    }


    public enum Type {
        CRAB,
        ALTERNATIVE
    }
}
