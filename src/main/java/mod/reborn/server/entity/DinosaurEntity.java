package mod.reborn.server.entity;

import com.google.common.collect.Lists;
import com.sun.javafx.geom.Vec3d;
import io.netty.buffer.ByteBuf;
import javafx.animation.Animation;
import javafx.scene.chart.Axis;
import mod.reborn.RebornMod;
import mod.reborn.server.RebornConfig;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.food.FoodType;
import mod.reborn.server.items.ItemHandler;
import mod.reborn.server.util.LangUtils;
import net.minecraft.advancements.criterion.MobEffectsPredicate;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.Path;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.MixinEnvironment;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DinosaurEntity extends CreatureEntity implements IEntityAdditionalSpawnData {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final DataParameter<Boolean> WATCHER_IS_CARCASS = EntityDataManager.createKey(DinosaurEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> WATCHER_AGE = EntityDataManager.createKey(DinosaurEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> WATCHER_IS_SLEEPING = EntityDataManager.createKey(DinosaurEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<String> WATCHER_OWNER_IDENTIFIER = EntityDataManager.createKey(DinosaurEntity.class, DataSerializers.STRING);
    private static final DataParameter<Byte> WATCHER_CURRENT_ORDER = EntityDataManager.createKey(DinosaurEntity.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> WATCHER_IS_RUNNING = EntityDataManager.createKey(DinosaurEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> WATCHER_WAS_FED = EntityDataManager.createKey(DinosaurEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> WATCHER_WAS_MOVED = EntityDataManager.createKey(DinosaurEntity.class, DataSerializers.BOOLEAN);
    public HashMap<Animation, Byte> variants = new HashMap<>();
    private final InventoryDinosaur inventory;
    private final MetabolismContainer metabolism;
    protected Dinosaur dinosaur;
    protected GoalSelector animationTasks;
    protected Order order = Order.WANDER;
    private boolean isCarcass;
    private boolean wasMoved;
    private boolean blocked;
    private boolean isMale;
    private boolean isSleeping;
    private boolean useInertialTweens;
    private boolean eatsEggs = false;
    private int carcassHealth;
    private int geneticsQuality;
    private int tranquilizerTicks;
    private int stayAwakeTime;
    private int growthSpeedOffset;
    protected int dinosaurAge;
    protected int prevAge;
    private UUID owner;
    private List<Class<? extends LivingEntity>> attackTargets = new ArrayList<>();
    private String genetics;
    public boolean tranqed;
    private boolean deserializing;
    private int ticksUntilDeath;
    private int attackCooldown;
    public FixedChainBuffer tailBuffer;
    public Herd herd;
    public Family family;
    public Set<Relationship> relationships = new HashSet<>();
    public int wireTicks;
    public int disableHerdingTicks;
    private boolean isSittingNaturally;
    private Animation animation;
    private int animationTick;
    private int animationLength;
    private DinosaurLookHelper lookHelper;
    private BlockPos closestFeeder;
    private int feederSearchTick;
    private boolean inLava;
    private DinosaurAttributes attributes;
    private int breedCooldown;
    private DinosaurEntity breeding;
    private Set<DinosaurEntity> children = new HashSet<>();
    private int pregnantTime;
    private int jumpHeight;
    private final LegSolver legSolver;
    private boolean isSkeleton;
    private byte skeletonVariant;
    private boolean isFossile;
    public boolean isRendered;
    private int moveTicks = -5;
    private int messageTick = 0;


    public DinosaurEntity(EntityType<? extends CreatureEntity> type, World worldIn) {
        super(type, worldIn);
        this.blocked = false;
        setSize(1, 1);
    }

    @Nullable
    protected LegSolver createLegSolver() {
        return null;
    }



    private void eatEggs() {
        for (Entity egg : world.getEntitiesWithinAABB(DinosaurEggEntity.class, new AxisAlignedBB()))
            if (egg instanceof DinosaurEggEntity) {
                if (getDistance(egg) < 0.5) {
                    egg.setDead();
                    this.getMetabolism().setEnergy((int) (this.getMetabolism().getEnergy() + ((DinosaurEggEntity) egg).getDinosaur().getAdultHealth() * 0.4));
                }
            }
    }

    public InventoryDinosaur getInventory() {
        return inventory;
    }

    protected boolean getDoesEatEggs() {
        return this.eatsEggs;
    }

    protected void doesEatEggs(boolean eatsEggs) {
        this.eatsEggs = eatsEggs;
    }

    @Override
    protected EntityBodyHelper createBodyHelper() {
        return new SmartBodyHelper(this);
    }

    @Override
    public DinosaurLookHelper getLookHelper() {
        return this.lookHelper;
    }

    private void initClient() {
        this.tailBuffer = new FixedChainBuffer();
    }

    public boolean shouldSleep() {
        if (this.metabolism.isDehydrated() || this.metabolism.isStarving()) {
            return false;
        }
        SleepTime sleepTime = this.dinosaur.getSleepTime();
        return sleepTime.shouldSleep() && this.getDinosaurTime() > sleepTime.getAwakeTime() && !this.hasPredators() && (this.herd == null || this.herd.enemies.isEmpty());
    }

    private boolean hasPredators() {
        for (LivingEntity predator : this.world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(this.getPosX() - 10F, this.getPosY() - 5F, this.getPosZ() - 10F, this.getPosX() + 10F, this.getPosY() + 5F, this.getPosZ() + 10F), e -> e != DinosaurEntity.this)) {
            boolean hasDinosaurPredator = false;

            if (predator instanceof DinosaurEntity) {
                DinosaurEntity dinosaur = (DinosaurEntity) predator;

                if (!dinosaur.isCarcass() || dinosaur.isSleeping) {
                    for (Class<? extends LivingEntity> target : dinosaur.getAttackTargets()) {
                        if (target.isAssignableFrom(this.getClass())) {
                            hasDinosaurPredator = true;
                            break;
                        }
                    }
                }
            }

            if (this.getLastAttackedEntity() == predator || predator.getAttackTarget() == this || hasDinosaurPredator) {
                return true;
            }
        }

        return false;
    }

    public int getDinosaurTime() {
        SleepTime sleepTime = this.dinosaur.getSleepTime();

        long time = (this.world.getDayTime() % 24000) - sleepTime.getWakeUpTime();

        if (time < 0) {
            time += 24000;
        }

        return (int) time;
    }

    //public boolean hasTracker() {
    //    return this.dataManager.get(WATCHER_HAS_TRACKER);
    // }

    // public void setHasTracker(boolean hasTracker) {
    //     this.hasTracker = hasTracker;
    //     if (!this.world.isRemote) {
    //        this.dataManager.set(WATCHER_HAS_TRACKER, hasTracker);
    //     }
    // }

    public UUID getOwner() {
        return this.owner;
    }

    public void setOwner(PlayerEntity player) {
        if (this.dinosaur.isImprintable()) {
            UUID prevOwner = this.owner;
            this.owner = player.getUniqueID();

            if (!this.owner.equals(prevOwner)) {
                ArrayList<String> vowels = buildArray("a", "e", "i", "o", "u");
                boolean hasvowel = false;
                for(String vowel : vowels) {
                    if(dinosaur.getName().toLowerCase().startsWith(vowel)) {
                        hasvowel = true;
                        break;
                    }
                }
                if(hasvowel) {
                    player.sendMessage(new StringTextComponent(LangUtils.translate(LangUtils.TAME).replace("{dinosaur}", LangUtils.getDinoName(this.dinosaur))));
                } else {
                    player.sendMessage(new StringTextComponent(LangUtils.translate(LangUtils.TAME).replace("{dinosaur}", LangUtils.getDinoName(this.dinosaur)).replace("an", "a")));
                }
            }
        }
    }

    public ArrayList<String> buildArray(String... strings) {
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, strings);
        return list;
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if (entity instanceof DinosaurEntity && ((DinosaurEntity) entity).isCarcass() && this.canEatEntity(entity)) {
            this.setAnimation(EntityAnimation.EATING.get());
        } else {
            this.setAnimation(EntityAnimation.ATTACKING.get());
        }

        while (entity.getRidingEntity() != null) {
            entity = entity.getRidingEntity();
        }

        float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);

        if (entity.attackEntityFrom(new DinosaurDamageSource("mob", this), damage)) {
            if (entity instanceof DinosaurEntity && ((DinosaurEntity) entity).isCarcass()) {
                DinosaurEntity dinosaur = (DinosaurEntity) entity;
                if (dinosaur.herd != null && this.herd != null && dinosaur.herd.fleeing && dinosaur.herd.enemies.contains(this)) {
                    this.herd.enemies.removeAll(dinosaur.herd.members);
                    for (DinosaurEntity member : this.herd) {
                        if (member.getAttackTarget() != null && dinosaur.herd.members.contains(member.getAttackTarget())) {
                            member.setAttackTarget(null);
                        }
                    }
                    this.herd.state = Herd.State.IDLE;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public LivingEntity getAttackTarget() {
        if(super.getAttackTarget() != null && super.getAttackTarget().getShouldBeDead()) {
            this.setAttackTarget(null);
            return null;
        } else {
            return super.getAttackTarget();
        }
    }

    private boolean canEatEntity(DinosaurEntity entity) {
        boolean isMarine = entity.getDinosaur().isMarineCreature();
        if(!isMarine) return entity.dinosaur.getDiet().canEat(entity, FoodType.MEAT);
        else return entity.dinosaur.getDiet().canEat(entity, FoodType.FISH);
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float amount) {
        boolean canHarmInCreative = damageSource.canHarmInCreative();
        Entity attacker = damageSource.getTrueSource();

        if (!this.isCarcass()) {
            if (this.getHealth() - amount <= 0.0F) {
                if (!canHarmInCreative) {
                    this.playSound(this.getSoundForAnimation(EntityAnimation.DYING.get()), this.getSoundVolume(), this.getSoundPitch());
                    this.setHealth(this.getMaxHealth());
                    this.setCarcass(true);
                    return true;
                }

                if (attacker instanceof DinosaurEntity) {
                    this.getRelationship(attacker, true).onAttacked(amount);
                }

                return super.attackEntityFrom(damageSource, amount);
            } else {
                if (this.getAnimation() == EntityAnimation.RESTING.get() && !this.world.isRemote) {
                    this.setAnimation(EntityAnimation.IDLE.get());
                    this.isSittingNaturally = false;
                }

                if (!this.world.isRemote) {
                    if (!((float)this.hurtResistantTime > (float)this.maxHurtResistantTime / 2.0F))
                        this.setAnimation(EntityAnimation.INJURED.get());
                }

                if (this.shouldSleep()) {
                    this.disturbSleep();
                }

                if(attacker instanceof LivingEntity) {
                    this.respondToAttack((EntityLivingBase)attacker);
                }

                return super.attackEntityFrom(damageSource, amount);
            }
        } else if (!this.world.isRemote) {

            if(!(((float)this.hurtResistantTime > (float)this.maxHurtResistantTime / 2.0F))) {
                boolean carcassAllowed = RebornConfig.ENTITIES_CONFIG.allowCarcass.get();
                if(!carcassAllowed) {
                    this.dropMeat(attacker);
                    this.onDeath(damageSource);
                    this.setDead();
                }

                if (damageSource != DamageSource.DROWN) {
                    if (!this.dead && this.carcassHealth >= 0 && this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
                        this.dropMeat(attacker);
                    }

                    if (this.carcassHealth <= 0) {
                        this.onDeath(damageSource);
                        this.setDead();
                    }

                    this.carcassHealth--;
                }

                if (canHarmInCreative) {
                    return super.attackEntityFrom(damageSource, amount);
                }

                if (this.hurtResistantTime <= this.maxHurtResistantTime / 2.0F) {
                    this.hurtTime = this.maxHurtTime = 10;
                }
            }
        }

        return false;
    }

    private Relationship getRelationship(Entity entity, boolean create) {
        for (Relationship relationship : this.relationships) {
            if (relationship.getUUID().equals(entity.getUniqueID())) {
                return relationship;
            }
        }
        if (create) {
            Relationship relationship = new Relationship(entity.getUniqueID(), (short) 0);
            this.relationships.add(relationship);
            return relationship;
        }
        return null;
    }

    private void dropMeat(Entity attacker) {
        int fortune = 0;
        if (attacker instanceof LivingEntity) {
            fortune = EnchantmentHelper.getLootingModifier((LivingEntity) attacker);
        }

        int count = this.rand.nextInt(2) + 1 + fortune;

        boolean burning = this.isBurning();

        for (int i = 0; i < count; ++i) {
            int meta = EntityHandler.getDinosaurId(this.dinosaur);

            if (burning) {
                this.entityDropItem(new ItemStack(ItemHandler.DINOSAUR_STEAK, 1, meta), 0.0F);
            } else {
                this.dropStackWithGenetics(new ItemStack(ItemHandler.DINOSAUR_MEAT, 1, meta));
            }
        }
    }

    @Override
    public boolean canBePushed() {
        return super.canBePushed() && !this.isCarcass() && !this.isSleeping();
    }

    @Override
    public ItemEntity entityDropItem(ItemStack stack, float offsetY) {
        if (stack.getCount() != 0) {
            Random rand = new Random();

            ItemEntity item = new ItemEntity(this.world, this.getPosX() + ((rand.nextFloat() * this.getWidth()) - this.getWidth() / 2), this.getPosY() + (double) offsetY, this.getPosZ() + ((rand.nextFloat() * this.getWidth()) - this.getWidth() / 2), stack);
            item.setDefaultPickupDelay();

            if (this.captureDrops().isEmpty()) {
                this.captureDrops().add(item);
            } else {
                this.world.addEntity(item);
            }

            return item;
        } else {
            return null;
        }
    }

    @Override
    public void knockBack(Entity entity, float p_70653_2_, double motionX, double motionZ) {
        if (this.rand.nextDouble() >= this.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE)) {
            this.isAirBorne = true;
            float distance = MathHelper.sqrt(motionX * motionX + motionZ * motionZ);
            float multiplier = 0.4F;
            this.setMotion(this.getMotion().x / 2.0D, this.getMotion().getY(), this.getMotion().z / 2.0D);
            this.setMotion(this.getMotion().x - motionX / distance * multiplier, this.getMotion().getY(), this.getMotion().z - motionZ / distance * multiplier);

            // TODO We should make knockback bigger and into air if dino is much smaller than attacking dino
        }
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);

        if (this.herd != null) {
            if (this.herd.leader == this) {
                this.herd.updateLeader();
            }

            this.herd.members.remove(this);
        }

        if (this.family != null) {
            UUID head = this.family.getHead();
            if (head == null || head.equals(this.getUniqueID())) {
                this.family.update(this);
            }
        }

        if (cause.getTrueSource() instanceof LivingEntity) {
            this.respondToAttack((LivingEntity) cause.getTrueSource());
        }
    }

    @Override
    public void playLivingSound() {
        if (this.getAnimation() == EntityAnimation.IDLE.get()) {
            this.setAnimation(EntityAnimation.SPEAK.get());
            super.playLivingSound();
        }
    }

    @Override
    public void entityInit() {
        super.entityInit();

        this.dataManager.register(WATCHER_IS_CARCASS, this.isCarcass);
        this.dataManager.register(WATCHER_AGE, this.dinosaurAge);
        this.dataManager.register(WATCHER_IS_SLEEPING, this.isSleeping);
        //this.dataManager.register(WATCHER_HAS_TRACKER, this.hasTracker);
        this.dataManager.register(WATCHER_OWNER_IDENTIFIER, "");
        this.dataManager.register(WATCHER_CURRENT_ORDER, (byte) 0);
        this.dataManager.register(WATCHER_IS_RUNNING, false);
        this.dataManager.register(WATCHER_WAS_FED, false);
        this.dataManager.register(WATCHER_WAS_MOVED, this.wasMoved);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();

        this.dinosaur = EntityHandler.getDinosaurByClass(this.getClass());
        this.attributes = DinosaurAttributes.create(this);
        this.setAttributes(Attributes.ATTACK_DAMAGE);
    }

    public void updateAttributes() {
        double prevHealth = this.getMaxHealth();
        double newHealth = Math.max(1.0F, this.interpolate(dinosaur.getBabyHealth(), dinosaur.getAdultHealth()) * this.attributes.getHealthModifier());
        double speed = this.interpolate(dinosaur.getBabySpeed(), dinosaur.getAdultSpeed()) * this.attributes.getSpeedModifier();
        double strength = this.getAttackDamage() * this.attributes.getDamageModifier();
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(newHealth);
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(strength);
        this.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(64D);

        if (prevHealth != newHealth) {
            this.heal((float) (newHealth - prevHealth));
        }
    }

    private void updateBounds() {
        float scale = this.attributes.getScaleModifier();
        float width = MathHelper.clamp((float) this.interpolate(dinosaur.getBabySizeX(), dinosaur.getAdultSizeX()) * scale, 0.3F, 4.0F);
        float height = MathHelper.clamp((float) this.interpolate(dinosaur.getBabySizeY(), dinosaur.getAdultSizeY()) * scale, 0.3F, 4.0F);

        this.stepHeight = Math.max(1.0F, (float) (Math.ceil(height / 2.0F) / 2.0F));

        if (this.isCarcass) {
            this.setSize(Math.min(5.0F, height), width);
        } else {
            this.setSize(width, height);
        }
    }

    public double getRotationAngle() {
        return interpolate(dinosaur.getBabyrotation(), dinosaur.getRotationAngle());
    }

    public double interpolate(double baby, double adult) {
        int dinosaurAge = this.dinosaurAge;
        int maxAge = this.dinosaur.getMaximumAge();
        if (dinosaurAge > maxAge) {
            dinosaurAge = maxAge;
        }
        return (adult - baby) / maxAge * dinosaurAge + baby;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isInRangeToRenderDist(double distance) {
        return true;
    }

    public void setupDisplay(boolean isMale) {
        this.setFullyGrown();
        this.setMale(isMale);
        this.ticksExisted = 4;
    }

    @Override
    public int getTalkInterval() {
        return 200;
    }

    @Override
    public float getSoundPitch() {
        return (float) this.interpolate(2.5F, 1.0F) + ((this.rand.nextFloat() - 0.5F) * 0.125F);
    }

    @Override
    public float getSoundVolume() {
        return (this.isCarcass() || this.isSleeping) ? 0.0F : (2.0F * ((float) this.interpolate(0.2F, 1.0F)));
    }

    public String getGenetics() {
        return this.genetics;
    }

    public void setGenetics(String genetics) {
        this.genetics = genetics;
    }

    public boolean isEntityFreindly(Entity entity) {
        return this.getClass().isAssignableFrom(entity.getClass());
    }

    public boolean canEatEntity(Entity entity) {
        if(entity instanceof PlayerEntity && (((PlayerEntity)entity).isCreative() || ((PlayerEntity)entity).isSpectator())) {
            return false;
        }
        return !isEntityFreindly(entity);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();


        if(!RebornConfig.ENTITIES_CONFIG.allowCarcass.get() && this.isCarcass) {
            this.attackEntityFrom(DamageSource.ANVIL, 1000);
        }
        if(this.getAttackTarget() instanceof DinosaurEntity) {
            DinosaurEntity entity = (DinosaurEntity) this.getAttackTarget();
            if(entity != null && entity.isCarcass) {
                this.setAttackTarget(null);
            }
        }

        if(!GameRuleHandler.DINO_METABOLISM.getBoolean(this.world)) {
            if(this.getMetabolism().getEnergy() < this.getMetabolism().getMaxEnergy()) {
                this.getMetabolism().setEnergy(this.getMetabolism().getMaxEnergy());
            }

            if(this.getMetabolism().getWater() < this.getMetabolism().getMaxWater()) {
                this.getMetabolism().setWater(this.getMetabolism().getMaxWater());
            }
        }

        if(this.animation != null && EntityAnimation.getAnimation(this.animation).doesBlockMovement()) {
            this.blocked = true;
        } else {
            this.blocked = false;
        }
        if (!this.world.isRemote && this instanceof TyrannosaurusEntity) {
            if (this.moveTicks > 0) {
                this.moveTicks--;
                this.setMotion(0, this.getMotion().getY(), 0);
                this.setMotion(this.getMotion().x + MathHelper.sin(-(float) Math.toRadians(this.rotationYaw - 90)) * 0.03, this.getMotion().getY(), this.getMotion().z + MathHelper.cos((float) Math.toRadians(this.rotationYaw - 90)) * 0.03);
                this.setMotion(this.getMotion().x * 6.3, this.getMotion().getY(), this.getMotion().z * 6.3);
            }
            if (this.moveTicks > -5) {
                this.moveTicks--;

                if (this.moveTicks == -4) {
                    this.wasMoved = true;
                }
            }
        }
        if(this.isCarcass() && (!(this instanceof TyrannosaurusEntity) || this.wasMoved) && !(this instanceof MicroraptorEntity)){
            this.setMotion(0, this.getMotion().getY(), 0);
        }

        if (this.breedCooldown > 0) {
            this.breedCooldown--;
        }

        if(!this.world.isRemote && dinosaur.getDiet().canEat(this, FoodType.MEAT) && this.getMetabolism().isHungry()) {
            world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(10, 10, 10), this::canEatEntity).stream().findAny().ifPresent(this::setAttackTarget);
        }

        if (!this.isMale() && !this.world.isRemote && !this.dinosaur.isHybrid) {
            if (this.isPregnant()) {
                if (--this.pregnantTime <= 0) {
                    this.navigator.clearPath();
                    this.setAnimation(dinosaur.givesDirectBirth() ? EntityAnimation.GIVING_BIRTH.get() : EntityAnimation.LAYING_EGG.get());
                    if(this.family != null) {
                        this.family.setHome(this.getPosition(), 6000);
                    }
                }
            }
            if ((this.getAnimation() == EntityAnimation.LAYING_EGG.get() || this.getAnimation() == EntityAnimation.GIVING_BIRTH.get()) && this.animationTick == this.getAnimationLength() / 2) {
                for (DinosaurEntity child : this.children) {
                    Entity entity;
                    if (dinosaur.givesDirectBirth()) {
                        entity = child;
                        child.setAge(0);
                        if(this.family != null) {
                            this.family.addChild(entity.getUniqueID());
                        }
                        if(child instanceof MammothEntity){
                            ((MammothEntity) child).setVariant(((MammothEntity)this).getVariant());
                        }
                    } else {
                        entity = new DinosaurEggEntity(this.world, child, this);
                    }
                    entity.setPosition(this.getPosX() + (this.rand.nextFloat() - 0.5F), this.getPosY() + 0.5F, this.getPosZ() + (this.rand.nextFloat() - 0.5F));
                    this.world.addEntity(entity);
                }
            }
        }

        if (this.breeding != null) {
            if (this.ticksExisted % 10 == 0) {
                this.getNavigator().tryMoveToEntityLiving(this.breeding, 1.0);
            }
            boolean dead = this.breeding.getShouldBeDead() || this.breeding.isCarcass();
            if (dead || this.getBoundingBox().intersects(this.breeding.getBoundingBox().expand(3, 3, 3))) {
                if (!dead) {
                    this.breedCooldown = dinosaur.getBreedCooldown();
                    if (!this.isMale()) {
                        int minClutch = dinosaur.getMinClutch();
                        int maxClutch = dinosaur.getMaxClutch();
                        for (int i = 0; i < this.rand.nextInt(maxClutch - minClutch) + minClutch; i++) {
                            try {
                                DinosaurEntity child = this.getClass().getConstructor(World.class).newInstance(this.world);
                                child.setAge(0);
                                child.setMale(this.rand.nextDouble() > 0.5);
                                child.setDNAQuality(100);
                                DinosaurAttributes attributes = DinosaurAttributes.combine(this, this.getAttributes(), this.breeding.getAttributes());
                                StringBuilder genetics = new StringBuilder();
                                for (int c = 0; c < this.genetics.length(); c++) {
                                    if (this.rand.nextBoolean()) {
                                        genetics.append(this.genetics.charAt(i));
                                    } else {
                                        genetics.append(this.breeding.genetics.charAt(i));
                                    }
                                }
                                child.setGenetics(genetics.toString());
                                child.setAttributes(attributes);
                                this.children.add(child);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        this.pregnantTime = 9600;
                    }
                }
                this.breeding = null;
            }
        }

        if (this.ticksExisted % 10 == 0) {
            this.inLava = this.isInLava();
        }

        if (this.isClimbing()) {
            this.prevLimbSwingAmount = this.limbSwingAmount;
            double deltaY = (this.getPosY() - this.prevPosY) * 4.0F;
            if (deltaY > 1.0F) {
                deltaY = 1.0F;
            }
            this.limbSwingAmount += (deltaY - this.limbSwingAmount) * 0.4F;
            this.limbSwing += this.limbSwingAmount;
        }

        if (!this.isCarcass) {
            if (this.firstUpdate) {
                this.updateAttributes();
            }

            this.updateGrowth();

            if (!this.world.isRemote) {
                if (this.metabolism.isHungry()) {
                    List<ItemEntity> entitiesWithinAABB = this.world.getEntitiesWithinAABB(ItemEntity.class, this.getBoundingBox().expand(1.0, 1.0, 1.0));
                    for (ItemEntity itemEntity : entitiesWithinAABB) {
                        Item item = itemEntity.getItem().getItem();
                        if (FoodHelper.isEdible(this, dinosaur.getDiet(), item)) {
                            this.setAnimation(EntityAnimation.EATING.get());

                            if (itemEntity.getItem().getCount() > 1) {
                                itemEntity.getItem().shrink(1);
                            } else {
                                itemEntity.remove();
                            }

                            this.getMetabolism().eat(FoodHelper.getHealAmount(item));
                            FoodHelper.applyEatEffects(this, item);
                            this.heal(10.0F);

                            break;
                        }
                    }
                }

                this.metabolism.update();
            }

            if (this.ticksExisted % 62 == 0) {
                this.playSound(this.getBreathingSound(), this.getSoundVolume(), this.getSoundPitch());
            }

            if (!dinosaur.isMarineCreature()) {
                if (this.isInsideOfMaterial(Material.WATER) || (this.getNavigator().noPath() && this.inWater() || this.inLava())) {
                    this.getJumpHelper().setJumping();
                } else {
                    if (this.isSwimming()) {
                        Path path = this.getNavigator().getPath();
                        if (path != null) {
                            AxisAlignedBB detectionBox = this.getBoundingBox().expand(0.5, 0.5, 0.5);

                            if (!this.world.hasNoCollisions(detectionBox)) {
                                List<AxisAlignedBB> colliding = new ArrayList<>();
                                this.world.getCollisionShapes(this.getAttackingEntity(), detectionBox).forEach(shape -> {
                                    colliding.add(shape.getBoundingBox());
                                });
                                boolean swimUp = false;
                                for (AxisAlignedBB bound : colliding) {
                                    if (bound.maxY > this.getBoundingBox().minY) {
                                        swimUp = true;
                                        break;
                                    }
                                }
                                if (swimUp) {
                                    this.getJumpHelper().setJumping();
                                }
                            }
                        }
                    }
                }
            }

            if (this.herd == null) {
                this.herd = new Herd(this);
            }

            if (!this.world.isRemote) {
                if (this.order == Order.WANDER) {
                    if (this.herd.state == Herd.State.IDLE && this.getAttackTarget() == null && !this.metabolism.isThirsty() && !this.metabolism.isHungry() && this.getNavigator().noPath()) {
                        if (!this.isSleeping && this.onGround && !this.isInWater() && this.getAnimation() == EntityAnimation.IDLE.get() && this.rand.nextInt(800) == 0) {
                            this.setAnimation(EntityAnimation.RESTING.get());
                            this.isSittingNaturally = true;
                        }
                    } else if (this.getAnimation() == EntityAnimation.RESTING.get()) {
                        this.setAnimation(EntityAnimation.IDLE.get());
                        this.isSittingNaturally = false;
                    }
                }

                if (this == this.herd.leader && !this.dinosaur.isMarineCreature()) {
                    this.herd.update();
                }

                if (this.ticksExisted % 10 == 0) {
                    if (this.family != null && (this.family.getHead() == null || this.family.getHead().equals(this.getUniqueID()))) {
                        if (this.family.update(this)) {
                            this.family = null;
                        }
                    } else if (this.family == null && this.getAttackTarget() == null) {
                        if (this.relationships.size() > 0 && this.rand.nextDouble() > 0.9) {
                            DinosaurEntity chosen = null;
                            Relationship chosenRelationship = null;
                            for (Relationship relationship : this.relationships) {
                                if (relationship.getScore() > Relationship.MAX_SCORE * 0.9) {
                                    DinosaurEntity entity = relationship.get(this);
                                    if (entity != null && this.isMale != entity.isMale) {
                                        chosen = entity;
                                        chosenRelationship = relationship;
                                        break;
                                    }
                                }
                            }
                            if (chosen != null) {
                                this.family = new Family(this.getUniqueID(), chosen.getUniqueID());
                                chosenRelationship.setFamily();
                                this.breedCooldown = this.rand.nextInt(1000) + 1000;
                                chosen.breedCooldown = this.breedCooldown;
                            }
                        }
                    }
                    if (this.herd != null) {
                        for (DinosaurEntity herdMember : this.herd.members) {
                            if (herdMember != this) {
                                Relationship relationship = this.getRelationship(herdMember, true);
                                relationship.updateHerd(this);
                            }
                        }
                        for (LivingEntity enemy : this.herd.enemies) {
                            if (enemy instanceof DinosaurEntity) {
                                Relationship relationship = new Relationship(enemy.getUniqueID(), (short) -30);
                                if (!this.relationships.contains(relationship)) {
                                    this.relationships.add(relationship);
                                }
                            }
                        }
                    }
                    if (this.relationships.size() > 0) {
                        Set<Relationship> removal = new HashSet<>();
                        for (Relationship relationship : this.relationships) {
                            if (relationship.update(this)) {
                                removal.add(relationship);
                            }
                        }
                        this.relationships.removeAll(removal);
                    }
                }

                if (!this.getNavigator().noPath()) {
                    if (this.isSittingNaturally && this.getAnimation() == EntityAnimation.RESTING.get()) {
                        this.setAnimation(EntityAnimation.IDLE.get());
                        this.isSittingNaturally = false;
                    }
                }
            }
        }

        if (this.isServerWorld()) {
            this.lookHelper.onUpdateLook();
        }

        if(this.getDoesEatEggs()){
            eatEggs();
        }

        if(!this.world.isRemote && this.ticksExisted % 20 == 0)
            this.dataManager.set(WATCHER_WAS_FED, false);
    }

    private void updateGrowth() {
        if (!this.getShouldBeDead() && this.ticksExisted % 8 == 0 && !this.world.isRemote) {
            if (GameRuleHandler.DINO_GROWTH.getBoolean(this.world)) {
                this.dinosaurAge += Math.min(this.growthSpeedOffset, 960) + 1;
                this.metabolism.decreaseEnergy((int) ((Math.min(this.growthSpeedOffset, 960) + 1) * 0.1));
            }

            if (this.growthSpeedOffset > 0) {
                this.growthSpeedOffset -= 10;

                if (this.growthSpeedOffset < 0) {
                    this.growthSpeedOffset = 0;
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(this.world.isRemote && this.dataManager.get(WATCHER_WAS_FED)) {
            this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getPosX() + (double)(this.rand.nextFloat() * this.getWidth() * 2.0F) - (double)this.getWidth(), this.getPosY() + 0.5D + (double)(this.rand.nextFloat() * this.getHeight()), this.getPosZ() + (double)(this.rand.nextFloat() * this.getWidth() * 2.0F) - (double)this.getWidth(), 0.0D, 0.0D, 0.0D);
        }
        if(this.ticksUntilDeath > 0) {
            if(--this.ticksUntilDeath == 0) {
                this.playSound(this.getSoundForAnimation(EntityAnimation.DYING.get()), this.getSoundVolume(), this.getSoundPitch());
                this.setHealth(this.getMaxHealth());
                this.setCarcass(true);
            }
        }

        if (this.attackCooldown > 0) {
            this.attackCooldown--;
        }

        if (!this.world.isRemote) {
            if (this.animation == EntityAnimation.LEAP.get()) {
                if (this.getMotion().getY() < 0) {
                    this.setAnimation(EntityAnimation.LEAP_LAND.get());
                }
            } else if (this.animation == EntityAnimation.LEAP_LAND.get() && (this.onGround || this.isSwimming())) {
                this.setAnimation(EntityAnimation.IDLE.get());
            }
        }

        if (this.animation != null && this.animation != EntityAnimation.IDLE.get()) {
            boolean shouldHold = EntityAnimation.getAnimation(this.animation).shouldHold();

            if (this.animationTick < this.animationLength) {
                this.animationTick++;
            } else if (!shouldHold) {
                this.animationTick = 0;
                if (this.animation == EntityAnimation.PREPARE_LEAP.get()) {
                    this.setAnimation(EntityAnimation.LEAP.get());
                } else {
                    this.setAnimation(EntityAnimation.IDLE.get());
                }
            } else {
                this.animationTick = this.animationLength - 1;
            }
        }

        if (!this.world.isRemote) {
            this.dataManager.set(WATCHER_WAS_MOVED, this.wasMoved);
            this.dataManager.set(WATCHER_AGE, this.dinosaurAge);
            this.dataManager.set(WATCHER_IS_SLEEPING, this.isSleeping);
            this.dataManager.set(WATCHER_IS_CARCASS, this.isCarcass);
            //  this.dataManager.set(WATCHER_HAS_TRACKER, this.hasTracker);
            this.dataManager.set(WATCHER_CURRENT_ORDER, (byte) this.order.ordinal());
            this.dataManager.set(WATCHER_OWNER_IDENTIFIER, this.owner != null ? this.owner.toString() : "");
            this.dataManager.set(WATCHER_IS_RUNNING, this.getAIMoveSpeed() > this.getAttributeValue(Attributes.MOVEMENT_SPEED));
        } else {
            this.updateTailBuffer();
            this.wasMoved = this.dataManager.get(WATCHER_WAS_MOVED);
            this.dinosaurAge = this.dataManager.get(WATCHER_AGE);
            this.isSleeping = this.dataManager.get(WATCHER_IS_SLEEPING);
            this.isCarcass = this.dataManager.get(WATCHER_IS_CARCASS);
            //   this.hasTracker = this.dataManager.get(WATCHER_HAS_TRACKER);
            String owner = this.dataManager.get(WATCHER_OWNER_IDENTIFIER);
            this.order = Order.values()[this.dataManager.get(WATCHER_CURRENT_ORDER)];

            if (owner.length() > 0 && (this.owner == null || !owner.equals(this.owner.toString()))) {
                this.owner = UUID.fromString(owner);
            } else if (owner.length() == 0) {
                this.owner = null;
            }
        }



        if (this.ticksExisted % 20 == 0) {
            this.updateAttributes();
            this.updateBounds();
        }

        if (this.isCarcass) {
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.rotationYaw;
        }

        if (this.isSleeping) {
            if (this.getAnimation() != EntityAnimation.SLEEPING.get()) {
                this.setAnimation(EntityAnimation.SLEEPING.get());
            }
        } else if (this.getAnimation() == EntityAnimation.SLEEPING.get()) {
            this.setAnimation(EntityAnimation.IDLE.get());
        }

        if (!this.world.isRemote) {
            if (this.isCarcass) {
                if (this.getAnimation() != EntityAnimation.DYING.get()) {
                    this.setAnimation(EntityAnimation.DYING.get());
                }

                if (this.ticksExisted % 1000 == 0) {
                    this.attackEntityFrom(DamageSource.GENERIC, 1.0F);
                }
            } else {
                if (this.isSleeping) {
                    if (this.ticksExisted % 20 == 0) {
                        if (this.stayAwakeTime <= 0 && this.hasPredators()) {
                            this.disturbSleep();
                        }
                    }

                    if (!this.shouldSleep() && !this.world.isRemote && tranquilizerTicks-- <= 0) {
                        this.isSleeping = false;
                        this.tranquilizerTicks = 0;
                        this.tranqed = false;
                    }
                } else if (this.getAnimation() == EntityAnimation.SLEEPING.get()) {
                    this.setAnimation(EntityAnimation.IDLE.get());
                }

                if (!this.isSleeping) {
                    if (this.order == Order.SIT) {
                        if (this.getAnimation() != EntityAnimation.RESTING.get()) {
                            this.setAnimation(EntityAnimation.RESTING.get());
                        }
                    } else if (!this.isSittingNaturally && this.getAnimation() == EntityAnimation.RESTING.get() && !this.world.isRemote) {
                        this.setAnimation(EntityAnimation.IDLE.get());
                    }
                }
            }
        }

        if (!this.shouldSleep() && !this.isSleeping) {
            this.stayAwakeTime = 0;
        }

        if (this.isServerWorld()) {
            this.animationTasks.onUpdateTasks();
        }

        if (this.stayAwakeTime > 0) {
            this.stayAwakeTime--;
        }
        if (this.wireTicks > 0) {
            this.wireTicks--;
        }
        if (this.disableHerdingTicks > 0) {
            this.disableHerdingTicks--;
        }

        if (this.legSolver != null) {
            double msc = dinosaur.getScaleInfant() / dinosaur.getScaleAdult();
            this.legSolver.update(this, (float) this.interpolate(msc, 1.0) * this.getAttributes().getScaleModifier());
        }

        this.prevAge = this.dinosaurAge;
    }

    private void updateTailBuffer() {
        this.tailBuffer.calculateChainSwingBuffer(68.0F, 3, 7.0F, this);
    }

    @Override
    public boolean isMovementBlocked() {
        return this.isCarcass() || this.isSleeping() || blocked;
    }

    @Override
    protected float updateDistance(float angle, float distance) {
        if (!this.isMovementBlocked()) {
            return super.updateDistance(angle, distance);
        }
        return distance;
    }

    public int getDaysExisted() {
        return (int) Math.floor((this.dinosaurAge * 8.0F) / 24000.0F);
    }

    public void setFullyGrown() {
        this.setAge(this.dinosaur.getMaximumAge());
    }

    public Dinosaur getDinosaur() {
        return this.dinosaur;
    }

    @Override
    public boolean canDespawn() {
        return false;
    }

    public int getDinosaurAge() {
        return this.dinosaurAge;
    }

    public void setAge(int age) {
        this.dinosaurAge = age;
        if (!this.world.isRemote) {
            this.dataManager.set(WATCHER_AGE, this.dinosaurAge);
        }
    }

    @Override
    public float getEyeHeight() {
        return (float) this.interpolate(dinosaur.getBabyEyeHeight(), dinosaur.getAdultEyeHeight()) * this.attributes.getScaleModifier();
    }

    @Override
    protected void dropFewItems(boolean playerAttack, int looting) {
        for (String bone : this.dinosaur.getBones()) {
            if (this.rand.nextInt(10) != 0) {
                this.dropStackWithGenetics(new ItemStack(ItemHandler.FRESH_FOSSILS.get(bone), 1, EntityHandler.getDinosaurId(this.dinosaur)));
            }
        }
    }

    private void dropStackWithGenetics(ItemStack stack) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("DNAQuality", this.geneticsQuality);
        nbt.putInt("Dinosaur", EntityHandler.getDinosaurId(this.dinosaur));
        nbt.putString("Genetics", this.genetics);
        stack.setTag(nbt);

        this.entityDropItem(stack, 0.0F);
    }

    @Override
    public boolean isCarcass() {
        return this.isCarcass;
    }

    public void setCarcass(boolean carcass) {
        if(!this.world.isRemote && carcass != this.isCarcass && !this.wasMoved) {
            this.moveTicks = 18;
        }
        this.isCarcass = carcass;

        boolean carcassAllowed = RebornConfig.ENTITIES_CONFIG.allowCarcass.get();
        if (!this.world.isRemote) {
            this.dataManager.set(WATCHER_IS_CARCASS, this.isCarcass);
        }
        if (carcass && carcassAllowed) {
            this.setAnimation(EntityAnimation.DYING.get());
            this.carcassHealth = MathHelper.clamp(Math.max(1, (int) Math.sqrt(this.width * this.height)), 0, 8);
            this.ticksExisted = 0;
            this.inventory.dropItems(this.world, this.rand);
        }else if (carcass){
            this.setAnimation(EntityAnimation.DYING.get());
            this.carcassHealth = 0;
            this.inventory.dropItems(this.world, this.rand);
        }
    }

    @Override
    public boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking() && hand == Hand.MAIN_HAND) {
            if (this.isOwner(player)) {
                if (this.getAgePercentage() > 75) {
                    player.displayGUIChest(this.inventory);
                } else {
                    if (this.world.isRemote) {
                        StringTextComponent denied = new StringTextComponent(LangUtils.translate("message.too_young.name"));
                        denied.getStyle().setColor(Color.fromTextFormatting(TextFormatting.RED));
                        ClientProxy.MC.ingameGUI.addChatMessage(ChatType.GAME_INFO, denied);
                    }
                }
            } else {
                if (this.world.isRemote) {
                    StringTextComponent denied = new StringTextComponent(LangUtils.translate("message.not_owned.name"));
                    denied.getStyle().setColor(Color.fromTextFormatting(TextFormatting.RED));
                    ClientProxy.MC.ingameGUI.addChatMessage(ChatType.GAME_INFO, denied);
                }
            }
        } else {
            if (stack.isEmpty() && hand == Hand.MAIN_HAND && this.world.isRemote) {
                if (this.isOwner(player)) {
                    RebornMod.NETWORK_WRAPPER.sendToServer(new BiPacketOrder(this));
                } else {
                    StringTextComponent denied = new StringTextComponent(LangUtils.translate("message.not_owned.name"));
                    denied.getStyle().setColor(Color.fromTextFormatting(TextFormatting.RED));
                    ClientProxy.MC.ingameGUI.addChatMessage(ChatType.GAME_INFO, denied);
                }
            } else if (!stack.isEmpty()&& (this.metabolism.isThirsty() || this.metabolism.isHungry())) {
                if (!this.world.isRemote) {
                    Item item = stack.getItem();
                    boolean fed = false;
                    if (item == Items.POTION) {
                        fed = true;
                        this.metabolism.increaseWater(1000);
                        this.setAnimation(EntityAnimation.DRINKING.get());
                    } else if (FoodHelper.isEdible(this, this.dinosaur.getDiet(), item)) {
                        fed = true;
                        this.metabolism.eat(FoodHelper.getHealAmount(item));
                        this.setAnimation(EntityAnimation.EATING.get());
                        FoodHelper.applyEatEffects(this, item);
                    }
                    if (fed) {
                        this.dataManager.set(WATCHER_WAS_FED, true);
                        if (!player.isCreative()) {
                            stack.shrink(1);
                            if (item == Items.POTION) {
                                player.inventory.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE));
                            }
                        }
                        if (!this.isOwner(player)) {
                            if (this.rand.nextFloat() < 0.30) {
                                if (this.dinosaur.getDinosaurType() == Dinosaur.DinosaurType.AGGRESSIVE) {
                                    if (this.rand.nextFloat() * 4.0F < (float) this.herd.members.size() / this.dinosaur.getMaxHerdSize()) {
                                        this.herd.enemies.add(player);
                                    } else {
                                        this.attackEntityAsMob(player);
                                    }
                                } else if (this.dinosaur.getDinosaurType() == Dinosaur.DinosaurType.SCARED) {
                                    this.herd.fleeing = true;
                                    this.herd.enemies.add(player);
                                }
                            }
                        }
                        player.swingArm(hand);
                    }
                }
            }
        }

        return false;
    }

    public boolean isOwner(PlayerEntity player) {
        return player.getUniqueID().equals(this.getOwner());
    }

    @Override
    public boolean canBeLeashedTo(PlayerEntity player) {
        return !this.getLeashed() && (this.getWidth() < 1.5);
    }

    public int getDNAQuality() {
        return this.geneticsQuality;
    }

    public void setDNAQuality(int quality) {
        this.geneticsQuality = quality;
    }

    @Override
    public Animation[] getAnimations() {
        return EntityAnimation.getAnimations();
    }

    @Override
    public Animation getAnimation() {
        return this.animation;
    }

    @Override
    public void setAnimation(Animation newAnimation) {
        if (this.isSleeping()) {
            newAnimation = EntityAnimation.SLEEPING.get();
        }

        if (this.isCarcass()) {
            newAnimation = EntityAnimation.DYING.get();
        }
        Animation oldAnimation = this.animation;
        this.animation = newAnimation;
        if (oldAnimation != newAnimation) {
            this.animationTick = 0;
            this.animationLength = (int) this.dinosaur.getPoseHandler().getAnimationLength(newAnimation, this.getGrowthStage());
            AnimationHandler.INSTANCE.sendAnimationMessage(this, newAnimation);
        }

    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }

    public boolean isBusy() {
        return !this.isAlive() ||
                this.getAnimation() == EntityAnimation.IDLE.get() ||
                (this.herd != null && this.herd.isBusy()) ||
                this.getAttackTarget() != null ||
                this.isSwimming() ||
                this.shouldSleep();
    }

    public boolean isAlive() {
        return !this.isCarcass && !this.getShouldBeDead();
    }

    @Override
    public SoundEvent getAmbientSound() {
        return this.getSoundForAnimation(EntityAnimation.SPEAK.get());
    }


    @Override
    public SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return this.getSoundForAnimation(EntityAnimation.INJURED.get());
    }

    @Override
    public SoundEvent getDeathSound() {
        return this.getSoundForAnimation(EntityAnimation.DYING.get());
    }

    public SoundEvent getSoundForAnimation(Animation animation) {
        return null;
    }

    public SoundEvent getBreathingSound() {
        return null;
    }

    public double getAttackDamage() {
        return this.interpolate(dinosaur.getBabyStrength(), dinosaur.getAdultStrength());
    }

    public boolean isMale() {
        return this.isMale;
    }

    public boolean isPregnant() {
        return !this.isMale() && this.pregnantTime > 0;
    }

    public void setMale(boolean male) {
        this.isMale = male;
    }

    public int getAgePercentage() {
        int age = this.getDinosaurAge();
        return age != 0 ? age * 100 / this.dinosaur.getMaximumAge() : 0;
    }

    @Override
    public GrowthStage getGrowthStage() {

        if (this.isSkeleton) {
            return GrowthStage.SKELETON;
        }
        int percent = this.getAgePercentage();
        return percent > 75 ? GrowthStage.ADULT : percent > 50 ? GrowthStage.ADOLESCENT : percent > 25 ? GrowthStage.JUVENILE : GrowthStage.INFANT;
    }

    public void increaseGrowthSpeed() {
        this.growthSpeedOffset += 240;
    }

    public int getBreedCooldown() {
        return this.breedCooldown;
    }

    public void breed(DinosaurEntity partner) {
        this.breeding = partner;
    }

    @Override
    public boolean isSwimming() {
        return (this.isInWater() || this.inLava()) && !this.onGround;
    }

    @Override
    public CompoundNBT writeToNBT(CompoundNBT nbt) {
        nbt = super.writeToNBT(nbt);

        nbt.putInt("DinosaurAge", this.dinosaurAge);
        nbt.putBoolean("IsCarcass", this.isCarcass);
        nbt.putInt("DNAQuality", this.geneticsQuality);
        nbt.putString("Genetics", this.genetics);
        nbt.putBoolean("IsMale", this.isMale);
        nbt.putInt("GrowthSpeedOffset", this.growthSpeedOffset);
        nbt.putInt("StayAwakeTime", this.stayAwakeTime);
        nbt.putBoolean("IsSleeping", this.isSleeping);
        nbt.putByte("Order", (byte) this.order.ordinal());
        nbt.putInt("CarcassHealth", this.carcassHealth);
        nbt.putInt("BreedCooldown", this.breedCooldown);
        nbt.putInt("PregnantTime", this.pregnantTime);
        nbt.putBoolean("WasMoved", this.wasMoved);

        this.metabolism.writeToNBT(nbt);

        if (this.owner != null) {
            nbt.putString("OwnerUUID", this.owner.toString());
        }

        this.inventory.writeToNBT(nbt);

        if (this.family != null && (this.family.getHead() == null || this.family.getHead().equals(this.getUniqueID()))) {
            CompoundNBT familyTag = new CompoundNBT();
            this.family.writeToNBT(familyTag);
            nbt.put("Family", familyTag);
        }

        List<CompoundNBT> relationshipList = new ArrayList<>();

        for (Relationship relationship : this.relationships) {
            CompoundNBT compound = new CompoundNBT();
            relationship.writeToNBT(compound);
            relationshipList.add(compound);
        }

        nbt.putString("Relationships", relationshipList.toString());

        NBTTagCompound attributes = new NBTTagCompound();
        this.attributes.writeToNBT(attributes);
        nbt.setTag("GeneticAttributes", attributes);

        if (this.children.size() > 0) {
            NBTTagList children = new NBTTagList();
            for (DinosaurEntity child : this.children) {
                if (child != null) {
                    children.appendTag(child.writeToNBT(new NBTTagCompound()));
                }
            }
            nbt.setTag("Children", children);
        }

        nbt.setInteger("TranquilizerTicks", tranquilizerTicks);
        nbt.setInteger("TicksUntilDeath", ticksUntilDeath);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.deserializing = true;

        super.readFromNBT(nbt);
        this.wasMoved = nbt.getBoolean("WasMoved");
        this.setAge(nbt.getInteger("DinosaurAge"));
        this.setCarcass(nbt.getBoolean("IsCarcass"));
        this.geneticsQuality = nbt.getInteger("DNAQuality");
        this.genetics = nbt.getString("Genetics");
        this.isMale = nbt.getBoolean("IsMale");
        this.growthSpeedOffset = nbt.getInteger("GrowthSpeedOffset");
        this.stayAwakeTime = nbt.getInteger("StayAwakeTime");
        this.setSleeping(nbt.getBoolean("IsSleeping"));
        this.carcassHealth = nbt.getInteger("CarcassHealth");
        this.order = Order.values()[nbt.getByte("Order")];
        this.breedCooldown = nbt.getInteger("BreedCooldown");
        this.pregnantTime = nbt.getInteger("PregnantTime");
        this.metabolism.readFromNBT(nbt);

        String ownerUUID = nbt.getString("OwnerUUID");

        if (ownerUUID.length() > 0) {
            this.owner = UUID.fromString(ownerUUID);
        }

        if (nbt.hasKey("Family")) {
            NBTTagCompound familyTag = nbt.getCompoundTag("Family");
            this.family = Family.readFromNBT(familyTag);
        }

        this.inventory.readFromNBT(nbt);

        NBTTagList relationships = nbt.getTagList("Relationships", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < relationships.tagCount(); i++) {
            NBTTagCompound compound = relationships.getCompoundTagAt(i);
            this.relationships.add(Relationship.readFromNBT(compound));
        }

        if (nbt.hasKey("GeneticAttributes")) {
            NBTTagCompound attributes = nbt.getCompoundTag("GeneticAttributes");
            this.attributes = DinosaurAttributes.from(attributes);
        }

        if (nbt.hasKey("Children")) {
            NBTTagList children = nbt.getTagList("Children", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < children.tagCount(); i++) {
                NBTTagCompound childTag = children.getCompoundTagAt(i);
                Entity entity = EntityList.createEntityFromNBT(childTag, this.world);
                if (entity instanceof DinosaurEntity) {
                    this.children.add((DinosaurEntity) entity);
                }
            }
        }

        tranquilizerTicks = nbt.getInteger("TranquilizerTicks");
        ticksUntilDeath = nbt.getInteger("TicksUntilDeath");

        this.updateAttributes();
        this.updateBounds();

        this.deserializing = false;
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(this.dinosaurAge);
        buffer.writeBoolean(this.isCarcass);
        buffer.writeInt(this.geneticsQuality);
        buffer.writeBoolean(this.isMale);
        buffer.writeInt(this.growthSpeedOffset);
        this.attributes.write(buffer);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
        this.dinosaurAge = additionalData.readInt();
        this.isCarcass = additionalData.readBoolean();
        this.geneticsQuality = additionalData.readInt();
        this.isMale = additionalData.readBoolean();
        this.growthSpeedOffset = additionalData.readInt();
        this.attributes = DinosaurAttributes.from(additionalData);

        if (this.isCarcass) {
            this.setAnimation(EntityAnimation.DYING.get());
        }

        this.updateAttributes();
        this.updateBounds();
    }

    public MetabolismContainer getMetabolism() {
        return this.metabolism;
    }

    public boolean setSleepLocation(BlockPos sleepLocation, boolean moveTo) {
        return !moveTo || this.getNavigator().tryMoveToXYZ(sleepLocation.getX(), sleepLocation.getY(), sleepLocation.getZ(), 1.0);
    }

    @Override
    public boolean isSleeping() {
        return this.isSleeping;
    }

    public void setSleeping(boolean sleeping) {
        this.isSleeping = sleeping;
        if (!this.world.isRemote) {
            this.dataManager.set(WATCHER_IS_SLEEPING, this.isSleeping);
        }
    }

    public void tranquilize(int ticks) {
        tranquilizerTicks = 50 + ticks + this.rand.nextInt(50);
        setSleeping(true);
        this.tranqed = true;
    }

    public int getStayAwakeTime() {
        return this.stayAwakeTime;
    }

    public void disturbSleep() {
        if(tranquilizerTicks == 0) {
            this.isSleeping = false;
            this.stayAwakeTime = 400;
        }
    }

    public void writeStatsToLog() {
        LOGGER.info(this);
    }

    @Override
    public String toString() {
        return "DinosaurEntity{ " +
                this.dinosaur.getName() +
                ", id=" + this.getEntityId() +
                ", remote=" + this.getEntityWorld().isRemote +
                ", isDead=" + this.getShouldBeDead() +
                ", isCarcass=" + this.isCarcass +
                ", isSleeping=" + this.isSleeping +
                ", stayAwakeTime=" + this.stayAwakeTime +
                "\n    " +
                ", dinosaurAge=" + this.dinosaurAge +
                ", prevAge=" + this.prevAge +
                ", maxAge" + this.dinosaur.getMaximumAge() +
                ", ticksExisted=" + this.ticksExisted +
                ", entityAge=" + this.idleTime +
                ", isMale=" + this.isMale +
                ", growthSpeedOffset=" + this.growthSpeedOffset +
                "\n    " +
                ", food=" + this.metabolism.getEnergy() + " / " + this.metabolism.getMaxEnergy() + " (" + this.metabolism.getMaxEnergy() * 0.875 + ")" +
                ", water=" + this.metabolism.getWater() + " / " + this.metabolism.getMaxWater() + " (" + this.metabolism.getMaxWater() * 0.875 + ")" +
                ", digestingFood=" + this.metabolism.getDigestingFood() + " / " + MetabolismContainer.MAX_DIGESTION_AMOUNT +
                ", health=" + this.getHealth() + " / " + this.getMaxHealth() +
                "\n    " +
                ", pos=" + this.getPosition() +
                ", eyePos=" + this.getHeadPos() +
                ", eyeHeight=" + this.getEyeHeight() +
                ", lookX=" + this.getLookHelper().getLookPosX() + ", lookY=" + this.getLookHelper().getLookPosY() + ", lookZ=" + this.getLookHelper().getLookPosZ() +
                "\n    " +
                ", width=" + this.getWidth() +
                ", bb=" + this.getBoundingBox() +
//                "\n    " +
//                ", anim=" + animation + (animation != null ? ", duration" + animation.duration : "" ) +

//                "dinosaur=" + dinosaur +
//                ", genetics=" + genetics +
//                ", geneticsQuality=" + geneticsQuality +
//                ", currentAnim=" + currentAnim +
//                ", animation=" + animation +
//                ", animTick=" + animTick +
//                ", hasTracker=" + hasTracker +
//                ", tailBuffer=" + tailBuffer +
                ", owner=" + owner +
                ", inventory=" + inventory +
//                ", metabolism=" + metabolism +
                " }";
    }

    public Vec3d getHeadPos() {
        double scale = this.interpolate(dinosaur.getScaleInfant(), dinosaur.getScaleAdult());

        double[] headPos = this.dinosaur.getHeadPosition(this.getGrowthStage(), ((360 - this.rotationYawHead)) % 360 - 180);

        double headX = ((headPos[0] * 0.0625F) - dinosaur.getOffsetX()) * scale;
        double headY = (((24 - headPos[1]) * 0.0625F) - dinosaur.getOffsetY()) * scale;
        double headZ = ((headPos[2] * 0.0625F) - dinosaur.getOffsetZ()) * scale;

        return new Vec3d(this.getPosX() + headX, this.getPosY() + (headY), this.getPosZ() + headZ);
    }

    public boolean areEyelidsClosed() {
        return this.ticksExisted != 4 && !this.dinosaur.isMarineCreature() && ((this.isCarcass || this.isSleeping) || this.ticksExisted % 100 < 4);
    }

    @Override
    public boolean shouldUseInertia() {
        return this.useInertialTweens;
    }

    public void setUseInertialTweens(boolean parUseInertialTweens) {
        this.useInertialTweens = parUseInertialTweens;
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(ItemHandler.SPAWN_EGG, 1, EntityHandler.getDinosaurId(this.dinosaur));
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.9F;
    }

    @Override
    public void moveRelative(float strafe, float up, float forward, float friction) {
        if(this.inWater() && !this.canDinoSwim()) {
            friction *= 10;
        }
        super.moveRelative(friction, new Vector3d(strafe, up, forward));
    }

    public void giveBirth() {
        pregnantTime = 1;
    }

    public void setDeathIn(int ticks) {
        this.ticksUntilDeath = ticks;
        this.addPotionEffect(new EffectInstance(Effects.POISON, ticks));
    }

    @Override
    public void collideWithEntity(Entity entity) {
        super.collideWithEntity(entity);

        if (this.isSleeping && !this.isRidingSameEntity(entity)) {
            if (!entity.noClip && !this.noClip) {
                if (entity.getClass() != this.getClass()) {
                    this.disturbSleep();
                }
            }
        }
    }

    @Override
    protected void setSize(float width, float height) {
        if (width != this.getWidth() || height != this.getHeight()) {
            float prevWidth = this.getWidth();
            this.getWidth() = width;
            this.getHeight() = height;
            if (!this.deserializing) {
                AxisAlignedBB bounds = this.getBoundingBox();
                AxisAlignedBB newBounds = new AxisAlignedBB(bounds.minX, bounds.minY, bounds.minZ, bounds.minX + this.getWidth(), bounds.minY + this.getHeight(), bounds.minZ + this.getWidth());
                if (this.world.hasNoCollisions(newBounds)) {
                    this.setBoundingBox(newBounds);
                    if (this.getWidth() > prevWidth && !this.firstUpdate && !this.world.isRemote) {
                        this.move(MoverType.SELF, prevWidth - this.getWidth(), 0.0F, prevWidth - this.getWidth());
                    }
                }
            } else {
                float halfWidth = this.getWidth() / 2.0F;
                this.setBoundingBox(new AxisAlignedBB(this.getPosX() - halfWidth, this.getPosY(), this.getPosZ() - halfWidth, this.getPosX() + halfWidth, this.getPosY() + this.getHeight(), this.getPosZ() + halfWidth));
            }
        }
    }

    public Order getOrder() {
        return this.order;
    }

    public void setFieldOrder(Order order) {

        this.order = order;
        this.dataManager.set(WATCHER_CURRENT_ORDER, (byte) order.ordinal());

    }

    public void setOrder(Order order) {

        if (this.world.isRemote) {
            if (this.owner != null) {
                PlayerEntity player = this.world.getPlayerByUuid(this.owner);

                if (player != null) {
                    StringTextComponent change = new StringTextComponent(LangUtils.translate(LangUtils.SET_ORDER).replace("{order}", LangUtils.translate(LangUtils.ORDER_VALUE.get(order.name().toLowerCase(Locale.ENGLISH)))));
                    change.getStyle().setColor(TextFormatting.GOLD);
                    ClientProxy.MC.ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
                }
            }

            RebornMod.NETWORK_WRAPPER.sendToServer(new SetOrderMessage(this));
        }
    }

    @SafeVarargs
    public final void target(Class<? extends LivingEntity>... targets) {
        this.goalSelector.addGoal(1, new SelectTargetEntityAI(this, targets));

        this.attackTargets.addAll(Lists.newArrayList(targets));
    }

    public Goal getAttackAI() {
        return new DinosaurAttackMeleeEntityAI(this, this.dinosaur.getAttackSpeed(), false);
    }

    public List<Class<? extends LivingEntity>> getAttackTargets() {
        return this.attackTargets;
    }

    @Override
    public ILivingEntityData onInitialSpawn(DifficultyInstance difficulty, ILivingEntityData data) {
        this.metabolism.setEnergy(this.metabolism.getMaxEnergy());
        this.metabolism.setWater(this.metabolism.getMaxWater());
        this.genetics = GeneticsHelper.randomGenetics(this.rand);
        this.setFullyGrown();
        this.setMale(this.rand.nextBoolean());
        this.setDNAQuality(100);

        // Assure that the width and height attributes have proper values
        this.updateBounds();
        return data;
    }

    public int getAttackCooldown() {
        return this.attackCooldown;
    }

    public void resetAttackCooldown() {
        this.attackCooldown = 50 + this.getRNG().nextInt(20);
    }

    public void respondToAttack(LivingEntity attacker) {
        if (attacker != null && !attacker.getShouldBeDead() && !(attacker instanceof PlayerEntity && ((PlayerEntity) attacker).capabilities.isCreativeMode)) {
            List<LivingEntity> enemies = new LinkedList<>();

            if (attacker instanceof DinosaurEntity) {
                DinosaurEntity enemyDinosaur = (DinosaurEntity) attacker;

                if (enemyDinosaur.herd != null) {
                    enemies.addAll(enemyDinosaur.herd.members);
                }
            } else {
                enemies.add(attacker);
            }

            if (enemies.size() > 0) {
                Herd herd = this.herd;

                if (herd != null) {
                    herd.fleeing = !herd.shouldDefend(enemies) || this.dinosaur.shouldFlee();

                    for (LivingEntity entity : enemies) {
                        if (!herd.enemies.contains(entity)) {
                            herd.enemies.add(entity);
                        }
                    }
                } else {
                    this.setAttackTarget(enemies.get(this.getRNG().nextInt(enemies.size())));
                }
            }
        }
    }

    public int getAnimationLength() {
        return this.animationLength;
    }

    @Override
    public boolean isRunning() {
        return this.dataManager.get(WATCHER_IS_RUNNING);
    }

    @Override
    public boolean getCanSpawnHere() {
        return true;
    }

    public boolean shouldEscapeWaterFast() {
        return true;
    }

    public BlockPos getClosestFeeder() {
        if (this.ticksExisted - this.feederSearchTick > 200) {
            this.feederSearchTick = this.ticksExisted;
            OnionTraverser traverser = new OnionTraverser(this.getPosition(), 32);
            for (BlockPos pos : traverser) {
                BlockState state = this.world.getBlockState(pos);
                if (state.getBlock() instanceof FeederBlock) {
                    TileEntity tile = this.world.getTileEntity(pos);
                    if (tile instanceof FeederBlockEntity) {
                        FeederBlockEntity feeder = (FeederBlockEntity) tile;
                        if (feeder.canFeedDinosaur(this) && feeder.getFeeding() == null && feeder.openAnimation == 0) {
                            Path path = this.getNavigator().getPathToPos(pos, getDistance(feeder));
                            if (path != null && path.getCurrentPathLength() != 0) {
                                return this.closestFeeder = pos;
                            }
                        }
                    }
                }
            }
        }
        return this.closestFeeder;
    }

    @Override
    public boolean isClimbing() {
        return false;
    }

    @Override
    public boolean isMoving() {
        float deltaX = (float) (this.getPosX() - this.prevPosX);
        float deltaZ = (float) (this.getPosZ() - this.prevPosZ);
        return deltaX * deltaX + deltaZ * deltaZ > 0.001F;
    }

    @Override
    public boolean canUseGrowthStage(GrowthStage growthStage) {
        return this.dinosaur.doesSupportGrowthStage(growthStage);
    }

    @Override
    public boolean isMarineCreature() {
        return this.dinosaur.isMarineCreature();
    }

    @Override
    public <ENTITY extends LivingEntity & Animatable> PoseHandler<ENTITY> getPoseHandler() {
        return (PoseHandler<ENTITY>) this.dinosaur.getPoseHandler();
    }

    @Override
    public boolean inWater() {
        return this.isInWater();
    }

    @Override
    public boolean inLava() {
        return this.inLava;
    }

    public DinosaurAttributes getAttributes() {
        return this.attributes;
    }

    public boolean isBreeding() {
        return this.breeding != null;
    }

    public void setAttributes(DinosaurAttributes attributes) {
        this.attributes = attributes;
    }

    public void setJumpHeight(int jumpHeight) {
        this.jumpHeight = jumpHeight;
    }

    @Override
    protected float getJumpUpwardsMotion() {
        return (float) Math.sqrt((this.jumpHeight + 0.2) * 0.27);
    }

    public boolean isSkeleton() {
        return this.getGrowthStage() == GrowthStage.SKELETON;
    }

    public void setSkeleton(boolean isSkeleton) {
        this.isSkeleton = isSkeleton;
    }

    public void setSkeletonVariant(byte variant) {
        this.skeletonVariant = variant;
    }

    public byte getSkeletonVariant() {
        return this.skeletonVariant;
    }

    public void setIsFossile(boolean isFossile) {
        this.isFossile = isFossile;
    }

    public boolean getIsFossile() {
        return this.isFossile;
    }

    public boolean canDinoSwim() {
        return true;
    }

    public Vector3f getDinosaurCultivatorRotation() {
        this.setAnimation(EntityAnimation.GESTATED.get());
        return new Vector3f();
    }

    @Override
    protected void handleJumpWater() {
        if(this.canDinoSwim()) {
            super.handleJumpWater();
        }
    }

    public DinosaurEntity(World world) {
        super(null, world);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {

    }

    public static class FieldGuideInfo {
        public int hunger;
        public int thirst;
        public boolean flocking;
        public boolean scared;
        public boolean hungry;
        public boolean thirsty;
        public boolean poisoned;

        public static FieldGuideInfo deserialize(ByteBuf buf) {
            FieldGuideInfo info = new FieldGuideInfo();
            info.flocking = buf.readBoolean();
            info.scared = buf.readBoolean();
            info.hunger = buf.readInt();
            info.thirst = buf.readInt();
            info.hungry = buf.readBoolean();
            info.thirsty = buf.readBoolean();
            info.poisoned = buf.readBoolean();
            return info;
        }

        public static FieldGuideInfo serialize(ByteBuf buf, DinosaurEntity entity) {
            MetabolismContainer metabolism = entity.getMetabolism();
            Herd herd = entity.herd;
            FieldGuideInfo info = new FieldGuideInfo();
            info.flocking = herd != null && herd.members.size() > 1 && herd.state == Herd.State.MOVING;
            info.scared = herd != null && herd.fleeing;
            info.hunger = metabolism.getEnergy();
            info.thirst = metabolism.getWater();
            info.hungry = metabolism.isHungry();
            info.thirsty = metabolism.isThirsty();
            info.poisoned = entity.isPotionActive(Effects.POISON);
            buf.writeBoolean(info.flocking);
            buf.writeBoolean(info.scared);
            buf.writeInt(info.hunger);
            buf.writeInt(info.thirst);
            buf.writeBoolean(info.hungry);
            buf.writeBoolean(info.thirsty);
            buf.writeBoolean(info.poisoned);
            return info;
        }
    }

    public enum Order {
        WANDER, FOLLOW, SIT
    }
}
