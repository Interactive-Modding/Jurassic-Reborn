package net.vit.jurassicreborn.common.entities.animal;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.google.common.collect.Lists;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.render.entity.animation.PoseHandler;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.entities.EntityUtils.Animatable;
import net.vit.jurassicreborn.common.entities.EntityUtils.GrowthStage;
import net.vit.jurassicreborn.common.entities.EntityUtils.ai.SmartBodyHelper;
import net.vit.jurassicreborn.common.entities.ModEntities;
import net.vit.jurassicreborn.common.items.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import java.util.Random;
import net.vit.jurassicreborn.common.RebornConfig;

public class GoatEntity extends Animal implements Animatable, IEntityAdditionalSpawnData {
    public static final PoseHandler<GoatEntity> BILLY_POSE_HANDLER = new PoseHandler<>("goat_billy", Lists.newArrayList(GrowthStage.ADULT));
    public static final PoseHandler<GoatEntity> KID_POSE_HANDLER   = new PoseHandler<>("goat_kid",   Lists.newArrayList(GrowthStage.INFANT));
    public static final PoseHandler<GoatEntity> NANNY_POSE_HANDLER = new PoseHandler<>("goat_nanny", Lists.newArrayList(GrowthStage.ADULT));

    private static final EntityDataAccessor<Boolean> WATCHER_IS_RUNNING = SynchedEntityData.defineId(GoatEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> WATCHER_IS_BILLY   = SynchedEntityData.defineId(GoatEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> WATCHER_VARIANT    = SynchedEntityData.defineId(GoatEntity.class, EntityDataSerializers.INT);

    private Animation animation;
    private int animationTick;
    private int animationLength;
    private boolean billy;
    private Variant variant = Variant.JURASSIC_PARK;
    private boolean milked;
    private boolean inLava;

    public GoatEntity(EntityType<? extends GoatEntity> type, Level level) {
        super(type, level);
        this.maxUpStep = 1.0F;
        this.animationTick = 0;
        this.setAnimation(EntityAnimation.IDLE.get());
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new SmartBodyHelper(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(Items.WHEAT), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(8, new AvoidEntityGoal<>(this, Wolf.class, 6.0F, 1.0D, 1.6D));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 7.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(WATCHER_IS_RUNNING, false);
        this.entityData.define(WATCHER_IS_BILLY, false);
        this.entityData.define(WATCHER_VARIANT, Variant.JURASSIC_PARK.ordinal());
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mate) {
        GoatEntity baby = ModEntities.GOAT.get().create(level);
        if (baby != null) {
            baby.finalizeSpawn(level, level.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.BREEDING, null, null);
        }
        return baby;
    }

    @Override public boolean isCarcass() { return false; }

    @Override
    public boolean isMoving() {
        float dx = (float)(this.getX() - this.xOld);
        float dz = (float)(this.getZ() - this.zOld);
        return dx*dx + dz*dz > 0.001F;
    }

    @Override public boolean isClimbing() { return false; }
    @Override public boolean isSwimming() { return (this.isInWater() || this.isInLava()) && !this.onGround; }
    @Override public boolean isRunning() { return this.entityData.get(WATCHER_IS_RUNNING); }
    @Override public boolean isMarineCreature() { return false; }
    @Override public boolean shouldUseInertia() { return true; }
    @Override public boolean isSleeping() { return false; }
    @Override public boolean inWater() { return this.isInWater(); }
    @Override public boolean inLava() { return this.inLava; }

    @Override
    public boolean canUseGrowthStage(GrowthStage growthStage) {
        return false;
    }

    // -------- Safety helpers -------------------------------------------------

    /** If kid pose pack lacks even IDLE for INFANT, drive kids with ADULT growth/poses. */
    private boolean kidPackIncomplete() {
        return KID_POSE_HANDLER.getAnimationLength(EntityAnimation.IDLE.get(), GrowthStage.INFANT) <= 0F;
    }

    /** Growth stage exposed to the animation system (can be coerced to ADULT for kids). */
    private GrowthStage effectiveGrowthStage() {
        if (this.isBaby() && kidPackIncomplete()) return GrowthStage.ADULT;
        return this.isBaby() ? GrowthStage.INFANT : GrowthStage.ADULT;
    }

    /** Pose handler that matches effectiveGrowthStage() to guarantee valid pose arrays. */
    private PoseHandler<?> effectivePoseHandler() {
        if (this.isBaby() && kidPackIncomplete()) {
            return this.entityData.get(WATCHER_IS_BILLY) ? BILLY_POSE_HANDLER : NANNY_POSE_HANDLER;
        }
        if (this.isBaby()) return KID_POSE_HANDLER;
        return this.entityData.get(WATCHER_IS_BILLY) ? BILLY_POSE_HANDLER : NANNY_POSE_HANDLER;
    }

    /** Coerce any requested animation to one that actually exists for the current handler+stage. */
    private Animation coerceSupported(Animation desired) {
        PoseHandler<?> ph = effectivePoseHandler();
        GrowthStage gs = effectiveGrowthStage();

        if (ph.getAnimationLength(desired, gs) > 0F) return desired;

        Animation idle = EntityAnimation.IDLE.get();
        if (ph.getAnimationLength(idle, gs) > 0F) return idle;

        for (Animation a : EntityAnimation.getAnimations()) {
            if (ph.getAnimationLength(a, gs) > 0F) return a;
        }
        return idle;
    }

    // ------------------------------------------------------------------------

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount % 10 == 0) this.inLava = this.isInLava();

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

        if (!this.level.isClientSide) {
            this.entityData.set(WATCHER_IS_RUNNING, this.getSpeed() > this.getAttributeValue(Attributes.MOVEMENT_SPEED));
        }
    }

    @Override public Animation[] getAnimations() { return EntityAnimation.getAnimations(); }
    @Override public Animation getAnimation() { return this.animation; }

    @Override
    public void setAnimation(Animation newAnimation) {
        // Always validate against the actually-used handler/stage
        Animation coerced = coerceSupported(newAnimation);
        Animation old = this.animation;
        this.animation = coerced;

        if (old != coerced) {
            this.animationTick = 0;
            this.animationLength = (int) effectivePoseHandler().getAnimationLength(this.animation, effectiveGrowthStage());
            AnimationHandler.INSTANCE.sendAnimationMessage(this, coerced);
        }
    }

    @Override public int getAnimationTick() { return this.animationTick; }
    @Override public void setAnimationTick(int tick) { this.animationTick = tick; }

    // IMPORTANT: expose effective growth stage to the animation system
    @Override
    public GrowthStage getGrowthStage() {
        return effectiveGrowthStage();
    }

    @Override
    public PoseHandler getPoseHandler() {
        return (PoseHandler) effectivePoseHandler();
    }

    public Type getGoatType() {
        boolean isBilly = this.entityData.get(WATCHER_IS_BILLY);
        return this.isBaby() ? Type.KID : (isBilly ? Type.BILLY : Type.NANNY);
    }

    @Override
    public void playAmbientSound() {
        super.playAmbientSound();
        if (this.getAnimation() == EntityAnimation.IDLE.get()) {
            this.setAnimation(EntityAnimation.SPEAK.get()); // will be coerced if not present
        }
    }

    @Override protected SoundEvent getAmbientSound() { return SoundHandler.GOAT_LIVING; }
    @Override protected SoundEvent getHurtSound(DamageSource source) { return SoundHandler.GOAT_HURT; }
    @Override protected SoundEvent getDeathSound() { return SoundHandler.GOAT_DEATH; }
    @Override protected float getJumpPower() { return 0.62F; }

    @Override
    public void ate() {
        super.ate();
        this.milked = false;
        this.setAnimation(EntityAnimation.EATING.get()); // will be coerced if not present
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (itemstack.getItem() == Items.BUCKET && !player.isCreative() && !this.isBaby() && !this.billy) {
            player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            itemstack.shrink(1);

            if (itemstack.isEmpty()) {
                player.setItemInHand(hand, new ItemStack(Items.MILK_BUCKET));
            } else if (!player.getInventory().add(new ItemStack(Items.MILK_BUCKET))) {
                player.drop(new ItemStack(Items.MILK_BUCKET), false);
            }
            this.milked = true;
            return InteractionResult.CONSUME;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason,
                                        @Nullable SpawnGroupData spawnData, @Nullable CompoundTag tag) {
        this.billy = this.getRandom().nextBoolean();
        this.variant = Variant.values()[this.getRandom().nextInt(Variant.values().length)];
        this.entityData.set(WATCHER_IS_BILLY, this.billy);
        this.entityData.set(WATCHER_VARIANT, this.variant.ordinal());
        return super.finalizeSpawn(level, difficulty, reason, spawnData, tag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Billy", this.billy);
        tag.putByte("Variant", (byte) this.variant.ordinal());
        tag.putBoolean("Milked", this.milked);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.billy = tag.getBoolean("Billy");
        this.variant = Variant.values()[tag.getByte("Variant")];
        this.milked = tag.getBoolean("Milked");
        this.entityData.set(WATCHER_IS_BILLY, this.billy);
        this.entityData.set(WATCHER_VARIANT, this.variant.ordinal());
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeBoolean(this.billy);
        buffer.writeByte(this.variant.ordinal());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buf) {
        this.billy = buf.readBoolean();
        this.variant = Variant.values()[buf.readByte()];
        this.entityData.set(WATCHER_IS_BILLY, this.billy);
        this.entityData.set(WATCHER_VARIANT, this.variant.ordinal());
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource src, int looting, boolean recentlyHit) {
        this.spawnAtLocation(Items.LEATHER, this.random.nextInt(2) + 1);
        if (this.random.nextBoolean()) {
            this.spawnAtLocation(new ItemStack(this.random.nextBoolean() ? Blocks.WHITE_WOOL : Blocks.BROWN_WOOL, 1), 0.0F);
        }
        this.spawnAtLocation(this.isOnFire() ? ModItems.GOAT_COOKED.get() : ModItems.GOAT_RAW.get(), this.random.nextInt(2) + 1);
    }

    @Override protected float getSoundVolume() { return super.getSoundVolume() * 0.8F; }

    public Variant getVariant() { return Variant.values()[this.entityData.get(WATCHER_VARIANT)]; }
    @Override public int getMaxSpawnClusterSize() { return 3; }
    @Override public int getAmbientSoundInterval() { return 300; }

    @Override
    public boolean canMate(Animal other) {
        if (other == this) return false;
        if (other.getClass() != this.getClass()) return false;
        if (this.billy != ((GoatEntity) other).billy) {
            return this.isInLove() && other.isInLove();
        }
        return false;
    }

    public static boolean checkGoatSpawnRules(EntityType<GoatEntity> type, LevelAccessor level, MobSpawnType reason, BlockPos pos, Random random) {
        return RebornConfig.spawnGoats && Animal.checkAnimalSpawnRules(type, level, reason, pos, random);
    }

    public enum Type { BILLY, NANNY, KID }
    public enum Variant { JURASSIC_WORLD, JURASSIC_PARK, JPOG }
}
