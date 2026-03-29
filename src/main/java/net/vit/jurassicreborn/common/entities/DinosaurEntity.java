package net.vit.jurassicreborn.common.entities;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vit.jurassicreborn.common.JurassicConfig;
import net.vit.jurassicreborn.common.blocks.entities.feeder.FeederRegistry;
import net.vit.jurassicreborn.common.util.ItemStackNbtUtil;
import org.joml.Vector3f;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.vit.jurassicreborn.client.JurassicClient;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.render.entity.animation.FixedChainBuffer;
import net.vit.jurassicreborn.client.render.entity.animation.PoseHandler;
import net.vit.jurassicreborn.common.JurassicConfig;
import net.vit.jurassicreborn.common.blocks.entities.feeder.FeederBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.grinder.FossilGrinderBlockEntity;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.EntityUtils.*;
import net.vit.jurassicreborn.common.entities.EntityUtils.ai.*;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.Dinosaurs.InventoryDinosaur;
import net.vit.jurassicreborn.common.entities.ai.*;
import net.vit.jurassicreborn.common.entities.ai.animations.CallAnimationAI;
import net.vit.jurassicreborn.common.entities.ai.animations.HeadCockAnimationAI;
import net.vit.jurassicreborn.common.entities.ai.animations.LookAnimationAI;
import net.vit.jurassicreborn.common.entities.ai.animations.RoarAnimationAI;
import net.vit.jurassicreborn.common.entities.ai.metabolism.DrinkEntityAI;
import net.vit.jurassicreborn.common.entities.ai.metabolism.EatFoodItemEntityAI;
import net.vit.jurassicreborn.common.entities.ai.metabolism.FeederEntityAI;
import net.vit.jurassicreborn.common.entities.ai.metabolism.GrazeEntityAI;
import net.vit.jurassicreborn.common.entities.ai.navigation.DinosaurJumpHelper;
import net.vit.jurassicreborn.common.entities.ai.navigation.DinosaurMoveHelper;
import net.vit.jurassicreborn.common.entities.ai.navigation.DinosaurPathNavigate;
import net.vit.jurassicreborn.common.entities.ai.util.AIUtils;
import net.vit.jurassicreborn.common.genetics.DinoDNA;
import net.vit.jurassicreborn.common.genetics.GeneticsHelper;
import net.vit.jurassicreborn.common.items.Food.FoodHelper;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.genetics.StorageDiscItem;
import net.vit.jurassicreborn.common.util.GameRuleHandler;
import net.vit.jurassicreborn.common.util.ItemsUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.vit.jurassicreborn.common.util.api.DinosaurItem;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Supplier;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class DinosaurEntity extends PathfinderMob implements IEntityWithComplexSpawn, Animatable {
    private static final Logger LOGGER = LogManager.getLogManager().getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final EntityDataAccessor<Boolean> WATCHER_IS_CARCASS = SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> WATCHER_AGE = SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> WATCHER_IS_SLEEPING = SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> TRACKER_UUIDS = SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> WATCHER_OWNER_IDENTIFIER = SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Byte> WATCHER_CURRENT_ORDER = SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> WATCHER_IS_RUNNING = SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> WATCHER_WAS_FED = SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> WATCHER_WAS_MOVED = SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> WATCHER_VARIANT = SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> WATCHER_IS_MALE = SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> WATCHER_GENETICS_QUALITY = SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> WATCHER_GROWTH_SPEED_OFFSET = SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<CompoundTag> WATCHER_ATTRIBUTES = SynchedEntityData.defineId(DinosaurEntity.class, EntityDataSerializers.COMPOUND_TAG);
    public HashMap<Animation, Byte> variants = new HashMap<>();
    private InventoryDinosaur inventory;
    private MetabolismContainer metabolism;
    protected Dinosaur dinosaur;
    //    protected EntityAITasks animationTasks;
    protected Order order = Order.WANDER;
    private boolean isCarcass;
    private boolean wasMoved;
    private boolean blocked;
    private boolean isMale;
    private boolean genderInitialized = false;
    //private boolean hasTracker;
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
    private boolean goalsRegistered;
    private boolean bonesDropped;
    private static final double NATURAL_SPAWN_SAFE_RADIUS = 64.0D;
    private boolean naturalSpawnedDino = false;


    private boolean deserializing;

    private int ticksUntilDeath;

    private int attackCooldown;

    @Nullable
    private TaskHelper taskHelper;

    @OnlyIn(Dist.CLIENT)
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
    private GrowthStage lastGrowthStage = null;
    private boolean inLava;
    private boolean postLoadFixPending;
    private DinosaurAttributes attributes;
    private int nearbyFoodScanCooldown = 0;
    private int breedCooldown;
    private int predatorCheckCooldown = 0;
    private boolean cachedHasPredators = false;
    private DinosaurEntity breeding;
    private Set<DinosaurEntity> children = new HashSet<>();
    private int pregnantTime;
    private int jumpHeight;

    private final LegSolver legSolver;

    private boolean isSkeleton;
    private boolean animSyncInProgress = false;

    private byte skeletonVariant;

    private boolean isFossile;

    public boolean isRendered;

    private int moveTicks = -5;

    private int messageTick = 0;
    private int targetSearchCooldown;
    private final List<String> trackersUUID = new ArrayList<>();

    @Nullable
    private BlockPos rejectedFeeder;
    private int rejectedFeederUntilTick;

    public List<UUID> getTrackers() {
        String data = this.entityData.get(TRACKER_UUIDS);
        if (data == null || data.isEmpty()) return new ArrayList<>();
        List<UUID> out = new ArrayList<>();
        for (String s : data.split(";")) {
            try { out.add(UUID.fromString(s)); } catch (Exception ignored) {}
        }
        return out;
    }
    public void addTracker(UUID uuid) {
        List<UUID> uuids = getTrackers();
        if (!uuids.contains(uuid)) {
            uuids.add(uuid);
            setTrackers(uuids);
        }
    }

    public void setTrackers(List<UUID> uuids) {
        StringBuilder sb = new StringBuilder();
        for (UUID u : uuids) {
            if (sb.length() > 0) sb.append(";");
            sb.append(u.toString());
        }
        this.entityData.set(TRACKER_UUIDS, sb.toString());
    }

    public static final Map<
            Class<? extends DinosaurEntity>,
            DeferredHolder<EntityType<?>, ? extends EntityType<? extends DinosaurEntity>>
            > CLASS_TYPE_LIST = new HashMap<>();

    static{
        CLASS_TYPE_LIST.put(AchillobatorEntity.class, ModEntities.ACHILLOBATOR_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(AlligatorGarEntity.class, ModEntities.ALLIGATOR_GAR);
        CLASS_TYPE_LIST.put(AllosaurusEntity.class, ModEntities.ALLOSAURUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(AlvarezsaurusEntity.class, ModEntities.ALVAREZSAURUS);
        CLASS_TYPE_LIST.put(AnkylodocusEntity.class, ModEntities.ANKYLODOCUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(AnkylosaurusEntity.class, ModEntities.ANKYLOSAURUS);
        CLASS_TYPE_LIST.put(ApatosaurusEntity.class, ModEntities.APATOSAURUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(ArsinoitheriumEntity.class, ModEntities.ARSINOITHERIUM);
        CLASS_TYPE_LIST.put(BaryonyxEntity.class, ModEntities.BARYONYX);
        CLASS_TYPE_LIST.put(BeelzebufoEntity.class, ModEntities.BEELZEBUFO_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(BrachiosaurusEntity.class, ModEntities.BRACHIOSAURUS);
        CLASS_TYPE_LIST.put(CamarasaurusEntity.class, ModEntities.CAMARASAURUS);
        CLASS_TYPE_LIST.put(NigersaurusEntity.class, ModEntities.NIGERSAURUS);
        CLASS_TYPE_LIST.put(CarcharodontosaurusEntity.class, ModEntities.CARCHARODONTOSAURUS);
        CLASS_TYPE_LIST.put(CarnotaurusEntity.class, ModEntities.CARNOTAURUS);
        CLASS_TYPE_LIST.put(CearadactylusEntity.class, ModEntities.CEARADACTYLUS);
        CLASS_TYPE_LIST.put(CeratosaurusEntity.class, ModEntities.CERATOSAURUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(ChasmosaurusEntity.class, ModEntities.CHASMOSAURUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(ChilesaurusEntity.class, ModEntities.CHILESAURUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(CoelacanthEntity.class, ModEntities.COELACANTH_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(CoelurusEntity.class, ModEntities.COELURUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(CompsognathusEntity.class, ModEntities.COMPSOGNATHUS);
        CLASS_TYPE_LIST.put(CorythosaurusEntity.class, ModEntities.CORYTHOSAURUS);
        CLASS_TYPE_LIST.put(CrassigyrinusEntity.class, ModEntities.CRASSIGYRINUS);
        CLASS_TYPE_LIST.put(DeinotheriumEntity.class, ModEntities.DEINOTHERIUM);
        CLASS_TYPE_LIST.put(DilophosaurusEntity.class, ModEntities.DILOPHOSAURUS);
        CLASS_TYPE_LIST.put(DimorphodonEntity.class, ModEntities.DIMORPHODON_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(DiplocaulusEntity.class, ModEntities.DIPLOCAULUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(DiplodocusEntity.class, ModEntities.DIPLODOCUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(DodoEntity.class, ModEntities.DODO);
        CLASS_TYPE_LIST.put(DreadnoughtusEntity.class, ModEntities.DREADNOUGHTUS);
        CLASS_TYPE_LIST.put(DunkleosteusEntity.class, ModEntities.DUNKLEOSTEUS);
        CLASS_TYPE_LIST.put(EdmontosaurusEntity.class, ModEntities.EDMONTOSAURUS);
        CLASS_TYPE_LIST.put(ElasmotheriumEntity.class, ModEntities.ELASMOTHERIUM);
        CLASS_TYPE_LIST.put(GallimimusEntity.class, ModEntities.GALLIMIMUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(GiganotosaurusEntity.class, ModEntities.GIGANOTOSAURUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(GuanlongEntity.class, ModEntities.GUANLONG_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(HerrerasaurusEntity.class, ModEntities.HERRERASAURUS);
        CLASS_TYPE_LIST.put(HyaenodonEntity.class, ModEntities.HYAENODON_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(HypsilophodonEntity.class, ModEntities.HYPSILOPHODON);
        CLASS_TYPE_LIST.put(IndominusEntity.class, ModEntities.INDOMINUS);
        CLASS_TYPE_LIST.put(IndoraptorEntity.class, ModEntities.INDORAPTOR);
        CLASS_TYPE_LIST.put(LambeosaurusEntity.class, ModEntities.LAMBEOSAURUS);
        CLASS_TYPE_LIST.put(LeaellynasauraEntity.class, ModEntities.LEAELLYNASAURA_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(LeptictidiumEntity.class, ModEntities.LEPTICTIDIUM_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(LudodactylusEntity.class, ModEntities.LUDODACTYLUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(MajungasaurusEntity.class, ModEntities.MAJUNGASAURUS);
        CLASS_TYPE_LIST.put(MamenchisaurusEntity.class, ModEntities.MAMENCHISAURUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(MammothEntity.class, ModEntities.MAMMOTH);
        CLASS_TYPE_LIST.put(MawsoniaEntity.class, ModEntities.MAWSONIA_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(MegapiranhaEntity.class, ModEntities.MEGAPIRANHA_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(MegatheriumEntity.class, ModEntities.MEGATHERIUM_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(MetriacanthosaurusEntity.class, ModEntities.METRIACANTHOSAURUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(MicroceratusEntity.class, ModEntities.MICROCERATUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(MicroraptorEntity.class, ModEntities.MICRORAPTOR);
        CLASS_TYPE_LIST.put(MoganopterusEntity.class, ModEntities.MOGANOPTERUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(MosasaurusEntity.class, ModEntities.MOSASAURUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(MussaurusEntity.class, ModEntities.MUSSAURUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(OrnithomimusEntity.class, ModEntities.ORNITHOMIMUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(OthnieliaEntity.class, ModEntities.OTHNIELIA);
        CLASS_TYPE_LIST.put(OviraptorEntity.class, ModEntities.OVIRAPTOR);
        CLASS_TYPE_LIST.put(PachycephalosaurusEntity.class, ModEntities.PACHYCEPHALOSAURUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(ParasaurolophusEntity.class, ModEntities.PARASAUROLOPHUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(PostosuchusEntity.class, ModEntities.POSTOSUCHUS);
        CLASS_TYPE_LIST.put(ProceratosaurusEntity.class, ModEntities.PROCERATOSAURUS);
        CLASS_TYPE_LIST.put(ProtoceratopsEntity.class, ModEntities.PROTOCERATOPS);
        CLASS_TYPE_LIST.put(PteranodonEntity.class, ModEntities.PTERANODON);
        CLASS_TYPE_LIST.put(QuetzalEntity.class, ModEntities.QUETZAL);
        CLASS_TYPE_LIST.put(RaphusrexEntity.class, ModEntities.RAPHUSREX);
        CLASS_TYPE_LIST.put(RugopsEntity.class, ModEntities.RUGOPS);
        CLASS_TYPE_LIST.put(SegisaurusEntity.class, ModEntities.SEGISAURUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(SinoceratopsEntity.class, ModEntities.SINOCERATOPS);
        CLASS_TYPE_LIST.put(SmilodonEntity.class, ModEntities.SMILODON_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(SpinoraptorEntity.class, ModEntities.SPINORAPTOR_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(SpinosaurusEntity.class, ModEntities.SPINOSAURUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(StegosaurusEntity.class, ModEntities.STEGOSAURUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(StyracosaurusEntity.class, ModEntities.STYRACOSAURUS);
        CLASS_TYPE_LIST.put(SuchomimusEntity.class, ModEntities.SUCHOMIMUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(TherizinosaurusEntity.class, ModEntities.THERIZINOSAURUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(TitanisEntity.class, ModEntities.TITANIS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(TriceratopsEntity.class, ModEntities.TRICERATOPS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(TroodonEntity.class, ModEntities.TROODON);
        CLASS_TYPE_LIST.put(TropeognathusEntity.class, ModEntities.TROPEOGNATHUS);
        CLASS_TYPE_LIST.put(TylosaurusEntity.class, ModEntities.TYLOSAURUS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(TyrannosaurusEntity.class, ModEntities.TYRANNOSAURUS);
        CLASS_TYPE_LIST.put(VelociraptorBlueEntity.class, ModEntities.VELOCIRAPTORBLUE_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(VelociraptorCharlieEntity.class, ModEntities.VELOCIRAPTORCHARLIE_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(VelociraptorDeltaEntity.class, ModEntities.VELOCIRAPTORDELTA);
        CLASS_TYPE_LIST.put(VelociraptorEchoEntity.class, ModEntities.VELOCIRAPTORECHO_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(VelociraptorEntity.class, ModEntities.VELOCIRAPTOR_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(ZhenyuanopterusEntity.class, ModEntities.ZHENYUANOPTERUS);
        CLASS_TYPE_LIST.put(DimetrodonEntity.class, ModEntities.DIMETRODON_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(AsterocerasEntity.class, ModEntities.ASTEROCERAS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(TitanitesEntity.class, ModEntities.TITANITES_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(ParapuzosiaEntity.class, ModEntities.PARAPUZOSIA_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(CamerocerasEntity.class, ModEntities.CAMEROCERAS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(EndocerasEntity.class, ModEntities.ENDOCERAS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(OrthocerasEntity.class, ModEntities.ORTHOCERAS_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(VectipeltaEntity.class, ModEntities.VECTIPELTA);
        CLASS_TYPE_LIST.put(ParaceratheriumEntity.class, ModEntities.PARACERATHERIUM);
        CLASS_TYPE_LIST.put(PerisphinctesEntity.class, ModEntities.PERISPHINCTES);
        CLASS_TYPE_LIST.put(CalymeneEntity.class, ModEntities.CALYMENE_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(LivyatanEntity.class, ModEntities.LIVYATAN_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(MegalodonEntity.class, ModEntities.MEGALODON_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(KairukuEntity.class, ModEntities.KAIRUKU_ENTITY_TYPE);
        CLASS_TYPE_LIST.put(DeinosuchusEntity.class, ModEntities.DEINOSUCHUS);
        CLASS_TYPE_LIST.put(MaiasauraEntity.class, ModEntities.MAIASAURA);
        CLASS_TYPE_LIST.put(PatagotitanEntity.class, ModEntities.PATAGOTITAN);



    }

    public DinosaurEntity(Level level, EntityType<? extends DinosaurEntity> type, Dinosaur dino) {
        super(type, level);
        this.dinosaur = dino;

        this.blocked  = false;
        this.setFullyGrown();
        this.metabolism = new MetabolismContainer(this);
        this.inventory  = new InventoryDinosaur(this);

        this.setPathfindingMalus(PathType.DOOR_WOOD_CLOSED, 0);
        this.setPathfindingMalus(PathType.DOOR_IRON_CLOSED, 0);
        this.setPathfindingMalus(PathType.POWDER_SNOW, 0.0F);
        this.setPathfindingMalus(PathType.DANGER_POWDER_SNOW, 0.0F);

        if (dino.isMarineCreature()) {
            this.navigation  = new WaterBoundPathNavigation(this, level);
            this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.10F, true);
            this.lookControl = new DinosaurLookHelper(this);
        } else {
            this.navigation  = new DinosaurPathNavigate(this, level);
            this.getNavigation().setCanFloat(true);
            this.moveControl = new DinosaurMoveHelper(this);
            this.lookControl = new DinosaurLookHelper(this);
        }
        this.jumpControl = new DinosaurJumpHelper(this);

        // Client-only helpers
        this.legSolver = level().isClientSide ? this.createLegSolver() : null;
        if (level().isClientSide) this.initClient();

        // Genetics/defaults
        if (this.genetics == null || this.genetics.isEmpty()) {
            this.genetics = GeneticsHelper.randomGenetics(this.random);
        }
        this.resetAttackCooldown();

        // Anim state
        this.animationTick = 0;
        this.setAnimation(EntityAnimation.IDLE.get());
        this.setUseInertialTweens(true);

        // Render/attributes
        this.noCulling = true;
        this.setSkeleton(false);
        this.attributes = DinosaurAttributes.create(this);
        this.updateAttributes();
        if (!this.level().isClientSide) {
            syncDinosaurData();
        }

        // We’ll own the goal setup in registerGoals() only, to avoid duplicates
        this.goalSelector.getAvailableGoals().clear();
        this.targetSelector.getAvailableGoals().clear();
        this.registerGoals();           // <- add all goals here (see below)
        this.goalsRegistered = true;
    }


    @Nullable
    protected LegSolver createLegSolver() {
        return null;
    }


    private void eatEggs() {
        if (this.level().isClientSide) {
            return;
        }

        List<DinosaurEggEntity> eggs = this.level().getEntitiesOfClass(
                DinosaurEggEntity.class,
                this.getBoundingBox().inflate(1.0D)
        );

        for (DinosaurEggEntity egg : eggs) {
            if (egg.getEyePosition().distanceTo(this.position()) < 0.5D) {
                egg.kill();
                this.getMetabolism().setEnergy(
                        (int) (this.getMetabolism().getEnergy() + egg.getDinosaur().getAdultHealth() * 0.4D)
                );
                break;
            }
        }
    }

    @Override
    public EntityType<?> getType() {
        return super.getType();
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
    protected BodyRotationControl createBodyControl() {
        return new SmartBodyHelper(this);
    }


    private void initClient() {
        this.tailBuffer = new FixedChainBuffer();
    }
    private boolean hasPredatorsCached() {
        if (this.predatorCheckCooldown-- <= 0) {
            this.predatorCheckCooldown = 20; // once per second
            this.cachedHasPredators = this.hasPredators();
        }
        return this.cachedHasPredators;
    }
    public boolean shouldSleep() {
        if (this.metabolism.isDehydrated() || this.metabolism.isStarving()) {
            return false;
        }

        SleepTime sleepTime = this.dinosaur.getSleepTime();
        return sleepTime.shouldSleep()
                && this.getDinosaurTime() > sleepTime.getAwakeTime()
                && !this.hasPredatorsCached()
                && (this.herd == null || this.herd.enemies.isEmpty());
    }

    private boolean hasPredators() {
        AABB box = new AABB(
                this.getX() - 10.0D, this.getY() - 5.0D, this.getZ() - 10.0D,
                this.getX() + 10.0D, this.getY() + 5.0D, this.getZ() + 10.0D
        );

        for (LivingEntity predator : this.level().getEntitiesOfClass(
                LivingEntity.class,
                box,
                e -> e != this && e.isAlive()
        )) {
            boolean threat = false;

            if (predator instanceof DinosaurEntity dinosaur) {
                // carcasses and sleeping dinos are not active threats
                if (dinosaur.isCarcass() || dinosaur.isSleeping()) {
                    continue;
                }

                // herd mates should not keep each other awake
                if (this.herd != null && dinosaur.herd == this.herd) {
                    continue;
                }

                for (Class<? extends LivingEntity> target : dinosaur.getAttackTargets()) {
                    if (target.isAssignableFrom(this.getClass())) {
                        threat = true;
                        break;
                    }
                }

                // recent attacker also counts
                if (!threat && this.getLastHurtByMob() != predator) {
                    continue;
                }

                // opaque wall / blocked vision = not an immediate sleep threat
                if (!dinosaur.hasLineOfSight(this)) {
                    continue;
                }
            } else {
                if (this.getLastHurtByMob() != predator) {
                    continue;
                }

                if (!predator.hasLineOfSight(this)) {
                    continue;
                }
            }

            return true;
        }

        return false;
    }

    public int getDinosaurTime() {
        SleepTime sleepTime = this.dinosaur.getSleepTime();

        long time = (this.level().getDayTime() % 24000) - sleepTime.getWakeUpTime();
        if (time < 0) {
            time += 24000;
        }

        return (int) time;
    }
    @Override
    public ItemStack getPickedResult(HitResult target) {
        var eggItem = ModItems.getSpawnEgg(this.getDinosaur());
        if (eggItem == null) {
            return ItemStack.EMPTY;
        }
        ItemStack egg = new ItemStack(eggItem.get());
        CompoundTag tag = ItemStackNbtUtil.getOrCreateTag(egg);
        tag.putInt("GenderMode", this.isMale() ? 1 : 2);
        ItemStackNbtUtil.setTag(egg, tag);
        return egg;
    }


    public UUID getOwner() {
        return this.owner;
    }

    public void setOwner(Player player) {
        if (this.dinosaur.isImprintable()) {
            UUID prevOwner = this.owner;
            this.owner = player.getUUID();

            if (!this.owner.equals(prevOwner)) {
                if (!this.level().isClientSide) {
                    this.entityData.set(WATCHER_OWNER_IDENTIFIER, this.owner.toString());
                }

                ArrayList<String> vowels = buildArray("a", "e", "i", "o", "u");
                boolean hasVowel = false;
                for (String vowel : vowels) {
                    if (dinosaur.getName().toLowerCase().startsWith(vowel)) {
                        hasVowel = true;
                        break;
                    }
                }
                String msg = Component.translatable("message.tame").getString().replace("{dinosaur}", dinosaur.getName());
                if (!hasVowel) {
                    msg = msg.replace("an", "a");
                }
                player.sendSystemMessage(Component.literal(msg));
            }
        }
    }

    public ArrayList<String> buildArray(String... strings) {
        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, strings);
        return list;
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (this.isNaturalSpawnProtected()) {
            this.setTarget(null);
            return false;
        }
        if (entity instanceof DinosaurEntity && ((DinosaurEntity) entity).isCarcass() && this.canEatEntity(entity)) {
            this.setAnimation(EntityAnimation.EATING.get());
        } else {
            this.setAnimation(EntityAnimation.ATTACKING.get());
        }

        while (entity.getVehicle() != null) {
            entity = entity.getVehicle();
        }

        float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);

        if (entity.hurt(this.damageSources().mobAttack(this), damage)) {
            if (entity instanceof DinosaurEntity && ((DinosaurEntity) entity).isCarcass()) {
                DinosaurEntity dinosaur = (DinosaurEntity) entity;
                if (dinosaur.herd != null && this.herd != null && dinosaur.herd.fleeing && dinosaur.herd.enemies.contains(this)) {
                    this.herd.enemies.removeAll(dinosaur.herd.members);
                    for (DinosaurEntity member : this.herd) {
                        if (member.getTarget() != null && dinosaur.herd.members.contains(member.getTarget())) {
                            member.setTarget(null);
                        }
                    }
                    this.herd.state = Herd.State.IDLE;
                }
            }
            return true;
        }
        return false;
    }



    public LivingEntity getAttackTarget() {
        if(super.getTarget() != null && super.getTarget().isDeadOrDying()) {
            this.setTarget(null);
            return null;
        } else {
            return super.getTarget();
        }
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if (target instanceof Player player) {
            UUID ownerId = this.getOwner();
            if (ownerId != null && ownerId.equals(player.getUUID())) {
                super.setTarget(null);
                return;
            }
        }

        if (target != null && this.isNaturalSpawnProtected()) {
            super.setTarget(null);
            return;
        }

        super.setTarget(target);
    }

    private boolean canEatEntity(DinosaurEntity entity) {
        boolean isMarine = entity.getDinosaur().isMarineCreature();
        if(!isMarine) return entity.dinosaur.getDiet().canEat(entity, FoodType.MEAT);
        else return entity.dinosaur.getDiet().canEat(entity, FoodType.FISH);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        boolean canHarmInCreative = damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY);
        Entity attacker = damageSource.getEntity();

        if (!this.isCarcass()) {
            if (this.getHealth() - amount <= 0.0F) {
                if (!canHarmInCreative) {
                    this.playSound(this.getSoundForAnimation(EntityAnimation.DYING.get()), this.getSoundVolume(), this.getVoicePitch());
                    this.setHealth(this.getMaxHealth());
                    this.setCarcass(true);
                    return true;
                }

                if (attacker instanceof DinosaurEntity) {
                    this.getRelationship(attacker, true).onAttacked(amount);
                }
                if (!this.level().isClientSide && !JurassicConfig.allowCarcass && !canHarmInCreative) {
                    this.setAnimation(EntityAnimation.DYING.get());
                }
                return super.hurt(damageSource, amount);
            } else {
                if (this.getAnimation() == EntityAnimation.RESTING.get() && !this.level().isClientSide) {
                    this.setAnimation(EntityAnimation.IDLE.get());
                    this.isSittingNaturally = false;
                }

                if (!this.level().isClientSide) {
                    if (!((float)this.invulnerableTime > (float)this.invulnerableDuration / 2.0F))
                        this.setAnimation(EntityAnimation.INJURED.get());
                }

                if (this.shouldSleep()) {
                    this.disturbSleep();
                }

                if(attacker instanceof LivingEntity) {
                    this.respondToAttack((LivingEntity)attacker);
                }

                return super.hurt(damageSource, amount);
            }
        } else if (!this.level().isClientSide) {
            if (!damageSource.is(DamageTypeTags.IS_DROWNING)) {
                if (this.carcassHealth >= 0
                        && this.level().getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
                    this.dropMeat(attacker);

                    if (!this.bonesDropped) {
                        this.dropBones();
                        this.bonesDropped = true; // ✅ ONLY ONCE
                    }
                }
                this.carcassHealth--;
                if (this.carcassHealth < 0 || !JurassicConfig.allowCarcass) {
                    this.discard();
                    return true;
                }
            }
            if (canHarmInCreative) {
                return super.hurt(damageSource, amount);
            }
            if (this.invulnerableTime <= this.invulnerableDuration / 2.0F) {
                this.hurtTime = this.hurtDuration = 10;
            }
            return true;
        }

        return false;
    }

    private Relationship getRelationship(Entity entity, boolean create) {
        for (Relationship relationship : this.relationships) {
            if (relationship.getUUID().equals(entity.getUUID())) {
                return relationship;
            }
        }
        if (create) {
            Relationship relationship = new Relationship(entity.getUUID(), (short) 0);
            this.relationships.add(relationship);
            return relationship;
        }
        return null;
    }

    private void dropMeat(Entity attacker) {
        int fortune = (attacker instanceof LivingEntity le)
                ? EnchantmentHelper.getEnchantmentLevel(getEnchantmentHolder(le, Enchantments.LOOTING), le)
                : 0;
        int count   = this.random.nextInt(2) + 1 + fortune;

        boolean burning = this.isOnFire();

        for (int i = 0; i < count; ++i) {
            Item dinoMeat = burning
                    ? ItemsUtil.getSteakForDinosaur(this.dinosaur)
                    : ItemsUtil.getMeatForDinosaur(this.dinosaur);

            // Fallback to the other type if one isn't registered
            if (dinoMeat == null) {
                dinoMeat = burning
                        ? ItemsUtil.getMeatForDinosaur(this.dinosaur)
                        : ItemsUtil.getSteakForDinosaur(this.dinosaur);
            }
            if (dinoMeat == null) continue; // nothing registered for this dino

            // Route through the DNA copier so NBT matches grinder/soft tissue/disc output exactly
            this.dropStackWithGenetics(new ItemStack(dinoMeat, 1));
        }
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && !this.isCarcass() && !this.isSleeping();
    }
    private static Holder<Enchantment> getEnchantmentHolder(LivingEntity entity, ResourceKey<Enchantment> key) {
        return entity.level().registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(key);
    }
    public boolean canStandOnPowderSnow() {return true;}

    @Override
    public ItemEntity spawnAtLocation(ItemStack stack, float offsetY) {
        if (stack.getCount() != 0 && stack.getItem() != null) {
            Random rand = new Random();

            ItemEntity item = new ItemEntity(this.level(), this.getX() + ((rand.nextFloat() * this.getBbWidth()) - this.getBbWidth() / 2), this.getY() + (double) offsetY, this.getZ() + ((rand.nextFloat() * this.getBbWidth()) - this.getBbWidth() / 2), stack);
            item.setDefaultPickUpDelay();

            if (this.shouldDropExperience()) {
                var thingie = this.captureDrops();
                if (thingie != null) {
                    thingie.add(item);
                }
            } else {
                this.level().addFreshEntity(item);
            }

            return item;
        } else {
            return null;
        }
    }

    public AttributeInstance getAttribute(Holder<Attribute> attr) {
        return this.getAttributes().getInstance(attr);
    }


//    @Override <--- handled in vanilla now I believe - gamma
//    public void knockback(Entity entity, float p_70653_2_, double motionX, double motionZ) {
//        if (this.random.nextDouble() >= this.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue()) {
//            this.hasImpulse = true;
//            float distance = Float.parseFloat(Double.toString(Math.sqrt(motionX * motionX + motionZ * motionZ)));
//            float multiplier = 0.4F;
//
//            this.setDeltaMovement(new Vec3(this.getDeltaMovement().x/2, this.getDeltaMovement().y, this.getDeltaMovement().z/2));
//            this.setDeltaMovement(this.getDeltaMovement().x - motionX / distance * multiplier, this.getDeltaMovement().y, this.getDeltaMovement().z-motionZ / distance * multiplier);
//
//
//            // TODO We should make knockback bigger and into air if dino is much smaller than attacking dino
//        }
//    }

    @Override
    public void die(DamageSource cause) {
        super.die(cause);

        if (this.herd != null) {
            if (this.herd.leader == this) {
                this.herd.updateLeader();
            }

            this.herd.members.remove(this);
        }

        if (this.family != null) {
            UUID head = this.family.getHead();
            if (head == null || head.equals(this.getUUID())) {
                this.family.update(this);
            }
        }

        if (cause.getEntity() instanceof LivingEntity) {
            this.respondToAttack((LivingEntity) cause.getEntity());
        }
    }

    @Override
    public void playAmbientSound() {
        if (this.getAnimation() == EntityAnimation.IDLE.get()) {
            this.setAnimation(EntityAnimation.SPEAK.get());
            super.playAmbientSound();
        }
    }

    @Override
    public void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TRACKER_UUIDS, "");
        builder.define(WATCHER_IS_CARCASS, this.isCarcass);
        builder.define(WATCHER_AGE, this.dinosaurAge);
        builder.define(WATCHER_IS_SLEEPING, this.isSleeping);
        builder.define(WATCHER_OWNER_IDENTIFIER, "");
        builder.define(WATCHER_CURRENT_ORDER, (byte) 0);
        builder.define(WATCHER_IS_RUNNING, false);
        builder.define(WATCHER_WAS_FED, false);
        builder.define(WATCHER_WAS_MOVED, this.wasMoved);
        builder.define(WATCHER_IS_MALE, false);
        builder.define(WATCHER_GENETICS_QUALITY, 0);
        builder.define(WATCHER_GROWTH_SPEED_OFFSET, 0);
        builder.define(WATCHER_ATTRIBUTES, new CompoundTag());
        builder.define(WATCHER_VARIANT, 0);
    }
    public int getVariant() {
        return this.entityData.get(WATCHER_VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(WATCHER_VARIANT, variant);
    }
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (WATCHER_IS_MALE.equals(key)) {
            this.isMale = this.entityData.get(WATCHER_IS_MALE);
        } else if (WATCHER_IS_CARCASS.equals(key)) {
            this.isCarcass = this.entityData.get(WATCHER_IS_CARCASS);
        } else if (WATCHER_IS_SLEEPING.equals(key)) {
            this.isSleeping = this.entityData.get(WATCHER_IS_SLEEPING);
        } else if (WATCHER_IS_CARCASS.equals(key)) {
            this.isCarcass = this.entityData.get(WATCHER_IS_CARCASS);
            if (this.isCarcass) {
                this.setAnimationSilently(EntityAnimation.DYING.get());
            }
            this.refreshDimensions();
        } else if (WATCHER_CURRENT_ORDER.equals(key)) {
            this.order = Order.values()[this.entityData.get(WATCHER_CURRENT_ORDER)];
            if (this.order == Order.SIT) {
                this.isSittingNaturally = false;
                if (!this.isSleeping && this.getAnimation() != EntityAnimation.RESTING.get()) {
                    this.setAnimationSilently(EntityAnimation.RESTING.get());
                }
            } else if (!this.isSleeping
                    && !this.isSittingNaturally
                    && this.getAnimation() == EntityAnimation.RESTING.get()) {
                this.setAnimationSilently(EntityAnimation.IDLE.get());
            }
        } else if (WATCHER_OWNER_IDENTIFIER.equals(key)) {
            String ownerStr = this.entityData.get(WATCHER_OWNER_IDENTIFIER);
            if (!ownerStr.isEmpty() && (this.owner == null || !ownerStr.equals(this.owner.toString()))) {
                this.owner = UUID.fromString(ownerStr);
            } else if (ownerStr.isEmpty()) {
                this.owner = null;
            }
        }
    }

//    @Override todo: modernize attributes
//    protected void applyEntityAttributes() {
//        super.applyEntityAttributes();
//
//        this.dinosaur = EntityHandler.getDinosaurByClass(this.getClass());
//
//
//        this.getAttributes().getInstance(Attributes.ATTACK_DAMAGE);
//    }

    public void updateAttributes() {
        double prevHealth = this.getMaxHealth();
        double newHealth = Math.max(1.0F, this.interpolate(dinosaur.getBabyHealth(), dinosaur.getAdultHealth()) * this.attributes.getHealthModifier());

        double speed = this.interpolate(dinosaur.getBabySpeed(), dinosaur.getAdultSpeed()) * this.attributes.getSpeedModifier();
        double strength = this.getAttackDamage() * this.attributes.getDamageModifier();

        this.getAttributes().getInstance(Attributes.MAX_HEALTH).setBaseValue(newHealth);
        this.getAttributes().getInstance(Attributes.MOVEMENT_SPEED).setBaseValue(speed);
        this.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).setBaseValue(strength);

        this.getAttributes().getInstance(Attributes.FOLLOW_RANGE).setBaseValue(64.0D);

        if (prevHealth != newHealth) {
            this.heal((float) (newHealth - prevHealth));
        }
    }

    public static AttributeSupplier.Builder createAttributes(){
        return LivingEntity.createLivingAttributes().add(Attributes.MAX_HEALTH).add(Attributes.MOVEMENT_SPEED).add(Attributes.ATTACK_KNOCKBACK).add(Attributes.FOLLOW_RANGE).add(Attributes.ATTACK_DAMAGE);
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
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        float width = (float) this.interpolate(this.dinosaur.getBabySizeX(), this.dinosaur.getAdultSizeX());
        float height = (float) this.interpolate(this.dinosaur.getBabySizeY(), this.dinosaur.getAdultSizeY());
        float scale = this.attributes.getScaleModifier();
        return EntityDimensions.fixed(width * scale, height * scale);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    public void setupDisplay(boolean isMale) {
        this.setFullyGrown();
        this.setMale(isMale);



        this.tickCount = 4;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }
    protected void onPostLoadFixup() {
    }
    @Override
    public float getVoicePitch() {
        return (float) this.interpolate(2.5F, 1.0F) + ((this.random.nextFloat() - 0.5F) * 0.125F);
    }

    @Override
    public float getSoundVolume() {
        if (this.isCarcass() || this.isSleeping) {
            return 0.0F;
        }

        float largestDimension = Math.max(this.getBbWidth(), this.getBbHeight());
        float scaledVolume = 0.25F + (largestDimension * 0.25F);

        return Mth.clamp(scaledVolume, 0.15F, 1.75F);
    }
    @Override
    public void playSound(@Nullable SoundEvent sound, float volume, float pitch) {
        if (sound == null) {
            return;
        }
        super.playSound(sound, volume, pitch);
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
        if(entity instanceof Player && (((Player)entity).isCreative() || ((Player)entity).isSpectator())) {
            return false;
        }
        return !isEntityFreindly(entity);
    }
    private static void setVariantFromParent(DinosaurEntity child, DinosaurEntity parent) {
        if (child instanceof IHasVariants && parent instanceof IHasVariants) {
            ((IHasVariants) child).setVariant(((IHasVariants) parent).getVariant());
        }
    }
    @Override
    protected void updateWalkAnimation(float partialTicks) {
        float amount;

        if (this.isClimbing()) {
            // Vertical distance for climbing animation
            double dy = (this.getY() - this.yOld) * 4.0F;
            if (dy > 1.0F) dy = 1.0F;
            amount = (float) dy;
        } else {
            // Normal walking animation based on X/Z movement
            double dx = this.getX() - this.xo;
            double dz = this.getZ() - this.zo;

            float dist = Mth.sqrt((float) (dx * dx + dz * dz)) * 4.0F;
            if (dist > 1.0F) dist = 1.0F;

            amount = dist;
        }

        // Apply movement → animation
        this.walkAnimation.update(amount, 0.4F);
    }
    private boolean isNaturalSpawnProtected() {
        if (this.level() == null || this.level().isClientSide) {
            return false;
        }

        if (!JurassicConfig.naturalspawningaddon) {
            return false;
        }

        if (!this.naturalSpawnedDino) {
            return false;
        }

        BlockPos spawn = this.level().getSharedSpawnPos();
        double dx = this.getX() - (spawn.getX() + 0.5D);
        double dz = this.getZ() - (spawn.getZ() + 0.5D);

        return (dx * dx + dz * dz) <= (NATURAL_SPAWN_SAFE_RADIUS * NATURAL_SPAWN_SAFE_RADIUS);
    }
    @Override
    public void aiStep() {
        super.aiStep();

        if (!JurassicConfig.allowCarcass && this.isCarcass) {
            this.remove(RemovalReason.DISCARDED);
        }

        if (this.getAttackTarget() instanceof DinosaurEntity entity && entity.isCarcass()) {
            var diet = this.getDinosaur().getDiet();
            boolean carnivore = diet.canEat(this, FoodType.MEAT) || diet.canEat(this, FoodType.FISH);
            if (!this.getMetabolism().isHungry() || !carnivore) {
                this.setTarget(null);
            }
        }

        if (!this.level().getGameRules().getBoolean(net.vit.jurassicreborn.common.util.GameRuleHandler.DINO_METABOLISM)) {
            if (this.getMetabolism().getEnergy() < this.getMetabolism().getMaxEnergy()) {
                this.getMetabolism().setEnergy(this.getMetabolism().getMaxEnergy());
            }

            if (this.getMetabolism().getWater() < this.getMetabolism().getMaxWater()) {
                this.getMetabolism().setWater(this.getMetabolism().getMaxWater());
            }
        }

        this.blocked = this.animation != null && EntityAnimation.getAnimation(this.animation).doesBlockMovement();

        if (!this.level().isClientSide && this instanceof TyrannosaurusEntity) { // TODO : ADD OTHER LARGE CARNIVORES
            if (this.moveTicks > 0) {
                this.moveTicks--;
                double yaw = Math.toRadians(this.getYRot() - 90);
                double motionX = Math.sin(-yaw) * 0.03 * 6.3;
                double motionZ = Math.cos(yaw) * 0.03 * 6.3;
                this.setDeltaMovement(motionX, this.getDeltaMovement().y, motionZ);
            }
            if (this.moveTicks > -5) {
                this.moveTicks--;
                if (this.moveTicks == -4) {
                    this.wasMoved = true;
                }
            }
        }

        if (this.isCarcass()) {
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
        }

        if (this.breedCooldown > 0) {
            this.breedCooldown--;
        }

        if (!this.level().isClientSide
                && !this.isNaturalSpawnProtected()
                && (dinosaur.getDiet().canEat(this, FoodType.MEAT) || dinosaur.getDiet().canEat(this, FoodType.FISH))
                && (!JurassicConfig.attackOnlyWhenHungry || this.getMetabolism().isHungry())
                && (this.getTarget() == null || !this.getTarget().isAlive())) {

            if (this.targetSearchCooldown-- <= 0) {
                this.targetSearchCooldown = 30 + this.random.nextInt(20);

                LivingEntity best = null;
                double bestDist2 = Double.MAX_VALUE;

                List<LivingEntity> nearby = this.level().getEntitiesOfClass(
                        LivingEntity.class,
                        this.getBoundingBox().inflate(12.0D),
                        this::canEatEntity
                );

                for (LivingEntity cand : nearby) {
                    if (!this.attackTargets.stream().anyMatch(clazz -> clazz.isAssignableFrom(cand.getClass()))) continue;
                    if (!this.hasLineOfSight(cand)) continue;

                    double d2 = this.distanceToSqr(cand);
                    if (d2 < bestDist2) {
                        bestDist2 = d2;
                        best = cand;
                    }
                }

                if (best != null) {
                    Path chasePath = this.getNavigation().createPath(best, 0);
                    if (chasePath != null || this.distanceToSqr(best) <= 4.0D) {
                        this.setTarget(best);
                    }
                }
            }
        }

        if (!this.isMale() && !this.level().isClientSide && !this.dinosaur.isHybrid) {
            if (this.isPregnant()) {
                if (--this.pregnantTime <= 0) {
                    this.navigation.stop();
                    this.setAnimation(dinosaur.givesDirectBirth() ? EntityAnimation.GIVING_BIRTH.get() : EntityAnimation.LAYING_EGG.get());
                    if (this.family != null) {
                        this.family.setHome(this.getOnPos(), 6000);
                    }
                }
            }

            if ((this.getAnimation() == EntityAnimation.LAYING_EGG.get() || this.getAnimation() == EntityAnimation.GIVING_BIRTH.get())
                    && this.animationTick == this.getAnimationLength() / 2) {
                for (DinosaurEntity child : this.children) {
                    Entity spawned;
                    setVariantFromParent(child, this);

                    if (dinosaur.givesDirectBirth()) {
                        spawned = child;
                        child.setAge(0);
                        if (this.family != null) {
                            this.family.addChild(spawned.getUUID());
                        }
                    } else {
                        spawned = new DinosaurEggEntity(ModEntities.DINOSAUR_EGG.get(), this.level(), child, this);
                    }

                    spawned.setPos(
                            this.getX() + (this.random.nextFloat() - 0.5F),
                            this.getY(),
                            this.getZ() + (this.random.nextFloat() - 0.5F)
                    );
                    this.level().addFreshEntity(spawned);
                }
            }
        }

        if (this.breeding != null) {
            if (this.tickCount % 10 == 0) {
                this.getNavigation().moveTo(this.breeding, 1.0);
            }
            boolean dead = this.breeding.dead || this.breeding.isCarcass();
            if (dead || this.getBoundingBox().intersects(this.breeding.getBoundingBox().inflate(3))) {
                if (!dead) {
                    this.breedCooldown = dinosaur.getBreedCooldown();
                    if (!this.isMale()) {
                        int minClutch = dinosaur.getMinClutch();
                        int maxClutch = dinosaur.getMaxClutch();
                        int clutchSize = this.random.nextInt(maxClutch - minClutch + 1) + minClutch;
                        for (int i = 0; i < clutchSize; i++) {
                            try {
                                Supplier<? extends EntityType<? extends DinosaurEntity>> reg = CLASS_TYPE_LIST.get(this.getClass());
                                if (reg == null) {
                                    continue;
                                }
                                DinosaurEntity child = (DinosaurEntity) reg.get().create(this.level());
                                if (child == null) {
                                    continue;
                                }
                                child.setAge(0);
                                child.setMale(this.random.nextDouble() > 0.5);
                                child.setDNAQuality(100);
//                                DinosaurAttributes attributes = DinosaurAttributes.combine(this, (DinosaurAttributes) this.getAttributes(), (DinosaurAttributes)/*should hope this is a dinosaur... lol - gamma*/ this.breeding.getAttributes());
                                StringBuilder genetics = new StringBuilder();
                                for (int c = 0; c < this.genetics.length(); c++) {
                                    if (this.random.nextBoolean()) {
                                        genetics.append(this.genetics.charAt(c));
                                    } else {
                                        genetics.append(this.breeding.genetics.charAt(c));
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

        if (this.tickCount % 10 == 0) {
            this.inLava = this.isInLava();
        }

        if (!this.isCarcass) {
            if (this.firstTick) {
                this.updateAttributes();
            }

            this.updateGrowth();

            if (!this.level().isClientSide) {
                if (this.metabolism.isHungry() && this.nearbyFoodScanCooldown-- <= 0) {
                    this.nearbyFoodScanCooldown = 10; // scan every 10 ticks

                    List<ItemEntity> entitiesWithinAABB = this.level().getEntitiesOfClass(
                            ItemEntity.class,
                            this.getBoundingBox().inflate(1.0D)
                    );

                    for (ItemEntity itemEntity : entitiesWithinAABB) {
                        Item item = itemEntity.getItem().getItem();
                        if (FoodHelper.isEdible(this, dinosaur.getDiet(), item)) {
                            this.setAnimation(EntityAnimation.EATING.get());

                            if (itemEntity.getItem().getCount() > 1) {
                                itemEntity.getItem().shrink(1);
                            } else {
                                itemEntity.kill();
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

            if (this.tickCount % 62 == 0) {
                SoundEvent breathingSound = this.getBreathingSound();
                if (breathingSound != null) {
                    this.playSound(breathingSound, this.getSoundVolume(), this.getVoicePitch());
                }
            }

            if (!dinosaur.isMarineCreature()
                    && !(this instanceof AmphibianDinosaurEntity)
                    && !(this instanceof PenguinDinosaurEntity)) {
                this.handleLandDinosaurInFluid();
            }

            if (this.herd == null) {
                this.herd = new Herd(this);
            }

            if (!this.level().isClientSide) {
                if (this.order == Order.WANDER) {
                    if (this.herd.state == Herd.State.IDLE
                            && this.getAttackTarget() == null
                            && !this.metabolism.isThirsty()
                            && !this.metabolism.isHungry()
                            && this.getNavigation().isDone()) {
                        if (!this.isSleeping
                                && !this.shouldSleep()
                                && this.onGround()
                                && !this.isInWater()
                                && this.getAnimation() == EntityAnimation.IDLE.get()
                                && this.random.nextInt(800) == 0) {
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

                if (this.tickCount % 10 == 0) {
                    if (this.family != null && (this.family.getHead() == null || this.family.getHead().equals(this.getUUID()))) {
                        if (this.family.update(this)) {
                            this.family = null;
                        }
                    } else if (this.family == null && this.getAttackTarget() == null) {
                        if (this.relationships.size() > 0 && this.random.nextDouble() > 0.9) {
                            DinosaurEntity chosen = null;
                            Relationship chosenRelationship = null;
                            for (Relationship relationship : this.relationships) {
                                if (relationship.getScore() > Relationship.MAX_SCORE * 0.9) {
                                    DinosaurEntity related = relationship.get(this);
                                    if (related != null && this.isMale != related.isMale) {
                                        chosen = related;
                                        chosenRelationship = relationship;
                                        break;
                                    }
                                }
                            }
                            if (chosen != null) {
                                this.family = new Family(this.getUUID(), chosen.getUUID());
                                chosenRelationship.setFamily();
                                this.breedCooldown = this.random.nextInt(1000) + 1000;
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
                                Relationship relationship = new Relationship(enemy.getUUID(), (short) -30);
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

                if (!this.getNavigation().isDone()) {
                    if (this.isSittingNaturally && this.getAnimation() == EntityAnimation.RESTING.get()) {
                        this.setAnimation(EntityAnimation.IDLE.get());
                        this.isSittingNaturally = false;
                    }
                }
            }
        }

        if (this.getDoesEatEggs()) {
            eatEggs();
        }

        if (!this.level().isClientSide && this.tickCount % 20 == 0) {
            this.entityData.set(WATCHER_WAS_FED, false);
        }
    }

    private void updateGrowth() {
        if (!this.dead && this.tickCount % 8 == 0 && !this.level().isClientSide) {
            if (/*GameRuleHandler.DINO_GROWTH.getBoolean(this.level()) todo: gamerules*/true) {
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
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.isNaturalSpawnProtected() && this.getTarget() != null) {
            super.setTarget(null);
        }
        if (!this.level().isClientSide && this.postLoadFixPending) {
            this.postLoadFixPending = false;
            this.onPostLoadFixup();
        }
        // --- client anim system step (Citadel) ---
        if (level().isClientSide) {
            AnimationHandler.INSTANCE.updateAnimations(this);
        }

        // --- fed particles (client) ---
        if (this.level().isClientSide && this.entityData.get(WATCHER_WAS_FED)) {
            this.level().addParticle(
                    ParticleTypes.HAPPY_VILLAGER,
                    this.getX() + (double)(this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double)this.getBbWidth(),
                    this.getY() + 0.5D + (double)(this.random.nextFloat() * this.getBbHeight()),
                    this.getZ() + (double)(this.random.nextFloat() * this.getBbWidth() * 2.0F) - (double)this.getBbWidth(),
                    0.0D, 0.0D, 0.0D
            );
        }

        // --- timed death trigger ---
        if (this.ticksUntilDeath > 0 && --this.ticksUntilDeath == 0) {
            this.playSound(this.getSoundForAnimation(EntityAnimation.DYING.get()), this.getSoundVolume(), this.getVoicePitch());
            this.setHealth(this.getMaxHealth());
            this.setCarcass(true);
        }

        // --- combat cooldown ---
        if (this.attackCooldown > 0) {
            this.attackCooldown--;
        }

        // --- simple server-side animation transitions (physics-driven) ---
        if (!this.level().isClientSide) {
            if (this.animation == EntityAnimation.LEAP.get() && this.getDeltaMovement().y < 0) {
                this.setAnimation(EntityAnimation.LEAP_LAND.get());
            } else if (this.animation == EntityAnimation.LEAP_LAND.get() && (this.onGround() || this.isSwimming())) {
                this.setAnimation(EntityAnimation.IDLE.get());
            }
        }

        // --- advance current animation (1.12.2-style: tick counter + hold/no-hold) ---
        if (this.animation != null && this.animation != EntityAnimation.IDLE.get()) {
            boolean hold = EntityAnimation.getAnimation(this.animation).shouldHold();

            if (this.animationTick < this.animationLength) {
                this.animationTick++;
            } else if (!hold) {
                this.animationTick = 0;
                if (this.animation == EntityAnimation.PREPARE_LEAP.get()) {
                    this.setAnimation(EntityAnimation.LEAP.get());
                } else {
                    this.setAnimation(EntityAnimation.IDLE.get());
                }
            } else {
                // clamp at end for hold animations
                this.animationTick = Math.max(0, this.animationLength - 1);
            }
        } else {
            this.animationTick = 0;
        }

        // --- sync watchers (server) / pull watchers (client) ---
        if (!this.level().isClientSide) {
            boolean running = this.getSpeed() > this.getAttributeBaseValue(Attributes.MOVEMENT_SPEED);
            String ownerId = this.owner != null ? this.owner.toString() : "";
            byte orderByte = (byte) this.order.ordinal();

            if (this.entityData.get(WATCHER_WAS_MOVED) != this.wasMoved) {
                this.entityData.set(WATCHER_WAS_MOVED, this.wasMoved);
            }
            if (this.entityData.get(WATCHER_AGE) != this.dinosaurAge) {
                this.entityData.set(WATCHER_AGE, this.dinosaurAge);
            }
            if (this.entityData.get(WATCHER_IS_SLEEPING) != this.isSleeping) {
                this.entityData.set(WATCHER_IS_SLEEPING, this.isSleeping);
            }
            if (this.entityData.get(WATCHER_IS_CARCASS) != this.isCarcass) {
                this.entityData.set(WATCHER_IS_CARCASS, this.isCarcass);
            }
            if (this.entityData.get(WATCHER_CURRENT_ORDER) != orderByte) {
                this.entityData.set(WATCHER_CURRENT_ORDER, orderByte);
            }
            if (!Objects.equals(this.entityData.get(WATCHER_OWNER_IDENTIFIER), ownerId)) {
                this.entityData.set(WATCHER_OWNER_IDENTIFIER, ownerId);
            }
            if (this.entityData.get(WATCHER_IS_RUNNING) != running) {
                this.entityData.set(WATCHER_IS_RUNNING, running);
            }
        } else {
            this.updateTailBuffer();
            this.wasMoved     = this.entityData.get(WATCHER_WAS_MOVED);
            this.dinosaurAge  = this.entityData.get(WATCHER_AGE);
            this.isSleeping   = this.entityData.get(WATCHER_IS_SLEEPING);
            this.isCarcass    = this.entityData.get(WATCHER_IS_CARCASS);
            if (this.isCarcass && this.getAnimation() != EntityAnimation.DYING.get()) {
                this.setAnimationSilently(EntityAnimation.DYING.get());
            }
            String ownerStr   = this.entityData.get(WATCHER_OWNER_IDENTIFIER);
            this.order        = Order.values()[this.entityData.get(WATCHER_CURRENT_ORDER)];
            if (this.order == Order.SIT) {
                this.isSittingNaturally = false;
                if (!this.isSleeping && this.getAnimation() != EntityAnimation.RESTING.get()) {
                    this.setAnimationSilently(EntityAnimation.RESTING.get());
                }
            } else if (!this.isSleeping
                    && !this.isSittingNaturally
                    && this.getAnimation() == EntityAnimation.RESTING.get()) {
                this.setAnimationSilently(EntityAnimation.IDLE.get());
            }
            if (!ownerStr.isEmpty() && (this.owner == null || !ownerStr.equals(this.owner.toString()))) {
                this.owner = UUID.fromString(ownerStr);
            } else if (ownerStr.isEmpty()) {
                this.owner = null;
            }
        }

        // --- periodic attribute/size refresh ---
        GrowthStage currentStage = this.getGrowthStage();
        boolean stageChanged = currentStage != this.lastGrowthStage;

        if (this instanceof FlyingDinosaurEntity) {
            boolean growthChanged = this.dinosaurAge != this.prevAge;
            if (this.tickCount <= 1 || growthChanged) {
                this.updateAttributes();
                this.refreshDimensions();
            }
        } else if (this.tickCount <= 1 || stageChanged) {
            this.updateAttributes();
            this.refreshDimensions();
            this.lastGrowthStage = currentStage;
        } else if (!this.level().isClientSide && this.tickCount % 40 == 0) {
            this.updateAttributes();
        }


        // --- orientation lock for carcass ---
        if (this.isCarcass) {
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();
        }

        // --- state → animation enforcement (sleep & carcass) ---
        if (this.isSleeping) {
            if (this.getAnimation() != EntityAnimation.SLEEPING.get()) {
                this.setAnimation(EntityAnimation.SLEEPING.get());
            }
        } else if (this.getAnimation() == EntityAnimation.SLEEPING.get()) {
            this.setAnimation(EntityAnimation.IDLE.get());
        }

        if (!this.level().isClientSide) {
            if (this.isCarcass) {
                if (this.getAnimation() != EntityAnimation.DYING.get()) {
                    this.setAnimation(EntityAnimation.DYING.get());
                }
                if (this.tickCount % 1000 == 0) {
                    this.hurt(this.damageSources().generic(), 1.0F);
                }
            } else {
                if (this.isSleeping) {
                    if (this.metabolism.isHungry() || this.metabolism.isThirsty()) {
                        this.disturbSleep();
                    }
                    if (this.tickCount % 20 == 0 && this.stayAwakeTime <= 0 && this.hasPredators()) {
                        this.disturbSleep();
                    }
                    if (!this.shouldSleep() && tranquilizerTicks-- <= 0) {
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
                    } else if (!this.isSittingNaturally
                            && this.getAnimation() == EntityAnimation.RESTING.get()) {
                        this.setAnimation(EntityAnimation.IDLE.get());
                    }
                }
            }
        }

        if (!this.shouldSleep() && !this.isSleeping) {
            this.stayAwakeTime = 0;
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
            boolean skeleton = this.isSkeleton();
            double adultScale = dinosaur.getScaleAdult(skeleton);
            double infantScale = dinosaur.getScaleInfant(skeleton);
            double msc = adultScale != 0.0 ? infantScale / adultScale : 1.0;
            this.legSolver.update(this, (float) this.interpolate(msc, 1.0) * this.attributes.getScaleModifier());
        }


        this.prevAge = this.dinosaurAge;
    }

    private void updateTailBuffer() {
        this.tailBuffer.calculateChainSwingBuffer(68.0F, 3, 7.0F, this);
    }

    @Override
    public boolean isImmobile() {
        return this.isCarcass() || this.isSleeping() || this.order == Order.SIT;
    }

    public boolean isMovementBlocked() {
        return this.isCarcass() || this.isSleeping() || this.order == Order.SIT;
    }
    @Override
    protected float tickHeadTurn(float angle, float distance) {
        if (!this.isMovementBlocked()) {
            return super.tickHeadTurn(angle, distance);
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
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }



    public int getDinosaurAge() {
        return this.dinosaurAge;
    }

    public void setAge(int age) {
        this.dinosaurAge = age;
        if (!this.level().isClientSide) {
            this.entityData.set(WATCHER_AGE, this.dinosaurAge);
        }
    }


    private void dropBones() {
        for (String bone : this.dinosaur.getBones()) {
            Item boneItem = ItemsUtil.getFreshDinosaurBone(this.dinosaur, bone);

            if (boneItem == null) {
                continue;
            }

            // EXACT same drop path as meat
            this.dropStackWithGenetics(new ItemStack(boneItem, 1));
        }
    }

    @Override
    protected void dropAllDeathLoot(ServerLevel level, DamageSource source) {
        // Intentionally empty.
        // Carcass system handles all drops in hurt()
    }

    private void dropStackWithGenetics(ItemStack stack) {
        // 1) Build a temporary source item carrying this entity's DNA (same fields/format as soft tissue)
        ItemStack src = ModItems.STORAGE_DISC.get().getDefaultInstance();
        CompoundTag dnaTag = ItemStackNbtUtil.getOrCreateTag(src);
        new DinoDNA(this.dinosaur, this.geneticsQuality, this.genetics)
                .writeToNBT(dnaTag);
        ItemStackNbtUtil.setTag(src, dnaTag);
        StorageDiscItem.applyCustomModelData(src);

        // 2) Copy DNA to the real drop using the shared helper so NBT matches grinder output exactly
        FossilGrinderBlockEntity.copyDNA(src, stack);

        // (Optional but helpful for tooltips/UI that key off this) mark the dino on the item too
        DinosaurItem.setDino(stack, this.dinosaur);

        // 3) Spawn the item
        ItemEntity item = new ItemEntity(
                this.level(),
                this.getX() + ((this.random.nextFloat() * this.getBbWidth()) - this.getBbWidth() / 2),
                this.getY(),
                this.getZ() + ((this.random.nextFloat() * this.getBbWidth()) - this.getBbWidth() / 2),
                stack
        );
        item.setDefaultPickUpDelay();
        this.level().addFreshEntity(item);
    }

    @Override
    public boolean isCarcass() {
        return this.isCarcass;
    }

    public void setCarcass(boolean carcass) {
        if (!this.level().isClientSide && carcass != this.isCarcass && !this.wasMoved) {
            this.moveTicks = 18;
        }
        this.isCarcass = carcass;

        if (!this.level().isClientSide) {
            this.entityData.set(WATCHER_IS_CARCASS, this.isCarcass);
        }
        if (carcass && JurassicConfig.allowCarcass) {
            this.setAnimation(EntityAnimation.DYING.get());
            this.carcassHealth = Mth.clamp(Math.max(1, (int) Math.sqrt(this.getBbWidth() * this.getBbHeight())), 0, 8);
            this.tickCount = 0;
            this.inventory.dropItems(this.level(), this.random);
        } else if (carcass) {
            this.setAnimation(EntityAnimation.DYING.get());
            this.carcassHealth = 0;
            this.inventory.dropItems(this.level(), this.random);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        boolean isClient = this.level().isClientSide;

        if (player.isCrouching() && hand == InteractionHand.MAIN_HAND) {
            if (this.isOwner(player)) {
                if (this.getAgePercentage() > 75) {
                    // TODO: Open dino inventory
                    // player.openMenu(this.inventory);
                } else {
                    if (isClient) {
                        player.sendSystemMessage(
                                Component.translatable("message.too_young").withStyle(ChatFormatting.RED)
                        );
                    }
                }
            } else {
                if (isClient) {
                    player.sendSystemMessage(
                            Component.translatable("message.not_owned.name").withStyle(ChatFormatting.RED)
                    );
                }
            }
        } else {
            if (stack.isEmpty() && hand == InteractionHand.MAIN_HAND && isClient) {
                if (this.isOwner(player)) {
                    JurassicClient.openOrderMenu(this);
                } else {
                    player.sendSystemMessage(
                            Component.translatable("message.not_owned.name").withStyle(ChatFormatting.RED)
                    );
                }
            } else if (!stack.isEmpty() && (this.metabolism.isThirsty() || this.metabolism.isHungry())) {
                if (!this.level().isClientSide) {
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
                        this.entityData.set(WATCHER_WAS_FED, true);
                        if (!player.getAbilities().instabuild) {
                            stack.shrink(1);
                            if (item == Items.POTION) {
                                player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
                            }
                        }
                        if (!this.isOwner(player)) {
                            if (this.random.nextFloat() < 0.30F) {
                                if (this.dinosaur.getDinosaurType() == Dinosaur.DinosaurType.AGGRESSIVE) {
                                    if (this.random.nextFloat() * 4.0F < (float) this.herd.members.size() / this.dinosaur.getMaxHerdSize()) {
                                        this.herd.enemies.add(player);
                                    } else {
                                        this.doHurtTarget(player);
                                    }
                                } else if (this.dinosaur.getDinosaurType() == Dinosaur.DinosaurType.SCARED) {
                                    this.herd.fleeing = true;
                                    this.herd.enemies.add(player);
                                }
                            }
                        }
                        player.swing(hand, true);
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    public boolean isOwner(Player player) {
        return player.getUUID().equals(this.getOwner());
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

    private void setAnimationSilently(Animation newAnimation) {
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
        if (!this.isAlive()) return true;
        if (this.isCarcass() || this.isSleeping()) return true;
        Animation anim = this.getAnimation();
        return anim != null && EntityAnimation.getAnimation(anim).doesBlockMovement();
    }

    public boolean isAlive() {
        return !this.isCarcass && !this.dead;
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
        this.genderInitialized = true;

        if (!this.level().isClientSide) {
            this.entityData.set(WATCHER_IS_MALE, male);
        }
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
        if (!this.level().getGameRules().getRule(GameRuleHandler.DINO_BREEDING).get()) {
            return;
        }
        this.breeding = partner;
    }

    @Override
    public boolean isSwimming() {
        return (this.isInWater() || this.inLava()) && !this.onGround();
    }



    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putString("Trackers", this.entityData.get(TRACKER_UUIDS));
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
        nbt.putBoolean("NaturalSpawnedDino", this.naturalSpawnedDino);
        nbt.putInt("Variant", this.getVariant());
        this.metabolism.writeToNBT(nbt);

        if (this.owner != null) {
            nbt.putString("OwnerUUID", this.owner.toString());
        }

        this.inventory.writeToNBT(nbt, this.level().registryAccess());

        if (this.family != null && (this.family.getHead() == null || this.family.getHead().equals(this.getUUID()))) {
            CompoundTag familyTag = new CompoundTag();
            this.family.writeToNBT(familyTag);
            nbt.put("Family", familyTag);
        }

        ListTag relationshipList = new ListTag();

        for (Relationship relationship : this.relationships) {
            CompoundTag compound = new CompoundTag();
            relationship.writeToNBT(compound);
            relationshipList.add(compound);
        }

        nbt.put("Relationships", relationshipList);

        CompoundTag attributes = new CompoundTag();
        this.attributes.writeToNBT(attributes);
        nbt.put("GeneticAttributes", attributes);

        if (this.children.size() > 0) {
            ListTag children = new ListTag();
            for (DinosaurEntity child : this.children) {
                if (child != null) {
                    CompoundTag temp = new CompoundTag();
                    child.save(temp);
                    children.add(temp);
                }
            }
            nbt.put("Children", children);
        }

        nbt.putInt("TranquilizerTicks", tranquilizerTicks);
        nbt.putInt("TicksUntilDeath", ticksUntilDeath);
    }



    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        this.deserializing = true;
        if(nbt.contains("Trackers")) this.entityData.set(TRACKER_UUIDS, nbt.getString("Trackers"));
        super.readAdditionalSaveData(nbt);
        if (nbt.contains("Variant")) {
            this.setVariant(nbt.getInt("Variant"));
        }
        this.wasMoved = nbt.getBoolean("WasMoved");
        this.setAge(nbt.getInt("DinosaurAge"));
        this.setCarcass(nbt.getBoolean("IsCarcass"));
        this.geneticsQuality = nbt.getInt("DNAQuality");
        this.genetics = nbt.getString("Genetics");
        this.setMale(nbt.getBoolean("IsMale"));
        this.growthSpeedOffset = nbt.getInt("GrowthSpeedOffset");
        this.stayAwakeTime = nbt.getInt("StayAwakeTime");
        this.setSleeping(nbt.getBoolean("IsSleeping"));
        this.carcassHealth = nbt.getInt("CarcassHealth");
        this.order = Order.values()[nbt.getByte("Order")];
        this.entityData.set(WATCHER_CURRENT_ORDER, (byte) this.order.ordinal());
        if (this.order == Order.SIT) {
            this.isSittingNaturally = false;
            if (!this.isSleeping) {
                this.setAnimation(EntityAnimation.RESTING.get());
            }
            if (!this.level().isClientSide) {
                this.getNavigation().stop();
                this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
            }
        }
        this.breedCooldown = nbt.getInt("BreedCooldown");
        this.pregnantTime = nbt.getInt("PregnantTime");
        this.naturalSpawnedDino = nbt.getBoolean("NaturalSpawnedDino");
        this.metabolism.readFromNBT(nbt);

        String ownerUUID = nbt.getString("OwnerUUID");

        if (ownerUUID.length() > 0) {
            this.owner = UUID.fromString(ownerUUID);
        }

        if (nbt.contains("Family")) {
            CompoundTag familyTag = nbt.getCompound("Family");
            this.family = Family.readFromNBT(familyTag);
        }

        this.inventory.readFromNBT(nbt, this.level().registryAccess());

        ListTag relationships = nbt.getList("Relationships", Tag.TAG_COMPOUND);

        for (int i = 0; i < relationships.size(); i++) {
            CompoundTag compound = relationships.getCompound(i);
            this.relationships.add(Relationship.readFromNBT(compound));
        }

        if (nbt.contains("GeneticAttributes")) {
            CompoundTag attributes = nbt.getCompound("GeneticAttributes");
            this.attributes = DinosaurAttributes.from(attributes);
        }

        if (nbt.contains("Children")) {
            ListTag children = nbt.getList("Children", Tag.TAG_COMPOUND);
            for (int i = 0; i < children.size(); i++) {
                CompoundTag childTag = children.getCompound(i);
                Entity entity = EntityType.loadEntityRecursive(childTag, level(), (entity1) -> {
                    return entity1;
                });
                if (entity instanceof DinosaurEntity) {
                    this.children.add((DinosaurEntity) entity);
                }
            }
        }

        tranquilizerTicks = nbt.getInt("TranquilizerTicks");
        ticksUntilDeath = nbt.getInt("TicksUntilDeath");

        this.updateAttributes();
        this.refreshDimensions();
        if (!this.level().isClientSide) {
            syncDinosaurData();
        }

        if (this.isCarcass) {
            this.setSleeping(false);
            this.setAnimation(EntityAnimation.DYING.get());
        }
        this.deserializing = false;
        this.postLoadFixPending = true;
    }

    // Have to override this so that the IEntityAdditionalSpawnData methods work
    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
        buffer.writeInt(this.dinosaurAge);
        buffer.writeBoolean(this.isCarcass);
        buffer.writeInt(this.geneticsQuality);
        buffer.writeBoolean(this.isMale);
        buffer.writeInt(this.growthSpeedOffset);
        this.attributes.write(buffer);

        // Always sync base variant
        buffer.writeInt(this.getVariant());
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf buffer) {
        this.dinosaurAge = buffer.readInt();
        this.isCarcass = buffer.readBoolean();
        this.geneticsQuality = buffer.readInt();
        this.isMale = buffer.readBoolean();
        this.growthSpeedOffset = buffer.readInt();
        this.attributes = DinosaurAttributes.from(buffer);

        // Always read base variant
        this.setVariant(buffer.readInt());

        if (this.isCarcass) {
            this.setAnimation(EntityAnimation.DYING.get());
        } else {
            this.setAnimation(EntityAnimation.IDLE.get());
        }

        this.updateAttributes();
        this.refreshDimensions();
    }


    private CompoundTag getAttributesTag() {
        CompoundTag tag = new CompoundTag();
        if (this.attributes != null) {
            this.attributes.writeToNBT(tag);
        }
        return tag;
    }

    private void syncDinosaurData() {
        this.entityData.set(WATCHER_IS_MALE, this.isMale);
        this.entityData.set(WATCHER_GENETICS_QUALITY, this.geneticsQuality);
        this.entityData.set(WATCHER_GROWTH_SPEED_OFFSET, this.growthSpeedOffset);
        this.entityData.set(WATCHER_ATTRIBUTES, getAttributesTag());
    }


    public MetabolismContainer getMetabolism() {
        return this.metabolism;
    }

    public boolean setSleepLocation(BlockPos sleepLocation, boolean moveTo) {
        return !moveTo || this.getNavigation().moveTo(sleepLocation.getX(), sleepLocation.getY(), sleepLocation.getZ(), 1.0);
    }

    @Override
    public boolean isSleeping() {
        return this.isSleeping;
    }

    public void setSleeping(boolean sleeping) {
        this.isSleeping = sleeping;
        if (!this.level().isClientSide) {
            this.entityData.set(WATCHER_IS_SLEEPING, this.isSleeping);
        }
    }

    public void tranquilize(int ticks) {
        tranquilizerTicks = 50 + ticks + this.random.nextInt(50);
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
        LOGGER.info(this.toString());
    }

    @Override
    public String toString() {
        return "DinosaurEntity{ " +
                this.dinosaur.getName() +
                ", id=" + this.getId() +
                ", remote=" + this.level().isClientSide +
                ", isDead=" + this.isDeadOrDying() +
                ", isCarcass=" + this.isCarcass +
                ", isSleeping=" + this.isSleeping +
                ", stayAwakeTime=" + this.stayAwakeTime +
                "\n    " +
                ", dinosaurAge=" + this.dinosaurAge +
                ", prevAge=" + this.prevAge +
                ", maxAge" + this.dinosaur.getMaximumAge() +
                ", ticksExisted=" + this.tickCount +
                ", entityAge=" + this.noActionTime +
                ", isMale=" + this.isMale +
                ", growthSpeedOffset=" + this.growthSpeedOffset +
                "\n    " +
                ", food=" + this.metabolism.getEnergy() + " / " + this.metabolism.getMaxEnergy() + " (" + this.metabolism.getMaxEnergy() * 0.875 + ")" +
                ", water=" + this.metabolism.getWater() + " / " + this.metabolism.getMaxWater() + " (" + this.metabolism.getMaxWater() * 0.875 + ")" +
                ", digestingFood=" + this.metabolism.getDigestingFood() + " / " + MetabolismContainer.MAX_DIGESTION_AMOUNT +
                ", health=" + this.getHealth() + " / " + this.getMaxHealth() +
                "\n    " +
                ", pos=" + this.getEyePosition() +
                ", eyePos=" + this.getHeadPos() +
                ", eyeHeight=" + this.getEyeHeight() +
                ", lookX=" + this.getLookAngle().x + ", lookY=" + this.getLookAngle().y + ", lookZ=" + this.getLookAngle().z +
                "\n    " +
                ", width=" + this.getBbWidth() +
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

    public Vec3 getHeadPos() {
        boolean skeleton = this.isSkeleton();
        double scale = this.interpolate(dinosaur.getScaleInfant(skeleton), dinosaur.getScaleAdult(skeleton));

        double[] headPos = this.dinosaur.getHeadPosition(this.getGrowthStage(),
                ((360 - this.yHeadRot)) % 360 - 180);

        float offsetX = skeleton ? this.dinosaur.getSkeletonOffsetX() : this.dinosaur.getOffsetX();
        float offsetY = skeleton ? this.dinosaur.getSkeletonOffsetY() : this.dinosaur.getOffsetY();
        float offsetZ = skeleton ? this.dinosaur.getSkeletonOffsetZ() : this.dinosaur.getOffsetZ();

        double headX = ((headPos[0] * 0.0625F) - offsetX) * scale;
        double headY = (((24 - headPos[1]) * 0.0625F) - offsetY) * scale;
        double headZ = ((headPos[2] * 0.0625F) - offsetZ) * scale;

        return new Vec3(this.getX() + headX,
                this.getY() + headY,
                this.getZ() + headZ);
    }

    public boolean areEyelidsClosed() {
        return this.getDinosaurAge() != 4 && !this.dinosaur.isMarineCreature() && ((this.isCarcass || this.isSleeping) || this.tickCount % 100 < 4);
    }

    @Override
    public boolean shouldUseInertia() {
        return this.useInertialTweens;
    }

    public void setUseInertialTweens(boolean parUseInertialTweens) {
        this.useInertialTweens = parUseInertialTweens;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.9F;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isInWater() && !this.canDinoSwim()) {
            float friction = 0.91F * 10.0F;
            this.moveRelative(0.01F, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(friction));
        } else {
            super.travel(travelVector);
        }
    }

    public void giveBirth() {
        pregnantTime = 1;
    }

    public void setDeathIn(int ticks) { // :(
        this.ticksUntilDeath = ticks;

        this.addEffect(new MobEffectInstance(MobEffects.POISON, ticks));

    }



    @Override
    public void push(Entity entity) {
        super.push(entity);
        if (this.isSleeping && !this.isPassengerOfSameVehicle(entity)) {
            if (!entity.noPhysics && !this.noPhysics) {
                if (!entity.getClass().equals(this.getClass())) {
                    this.disturbSleep();
                }
            }
        }
    }

    public Order getOrder() {
        return this.order;
    }

    public void setFieldOrder(Order order) {

        this.order = order;
        this.entityData.set(WATCHER_CURRENT_ORDER, (byte) order.ordinal());
        if (!this.level().isClientSide) {
            if (order == Order.SIT) {
                this.isSittingNaturally = false;
                this.getNavigation().stop();
                this.setTarget(null);
                this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
                if (!this.isSleeping) {
                    this.setAnimation(EntityAnimation.RESTING.get());
                }
            } else if (!this.isSleeping
                    && !this.isSittingNaturally
                    && this.getAnimation() == EntityAnimation.RESTING.get()) {
                this.setAnimation(EntityAnimation.IDLE.get());
            }
        }

    }

//    public void setOrder(Order order) { todo: networking
//
//        if (this.level().isClientSide) {
//            if (this.owner != null) {
//                Player player = this.level().getPlayerEntityByUUID(this.owner);
//
//                if (player != null) {
//                    TextComponentString change = new TextComponentString(LangUtils.translate(LangUtils.SET_ORDER).replace("{order}", LangUtils.translate(LangUtils.ORDER_VALUE.get(order.name().toLowerCase(Locale.ENGLISH)))));
//                    change.getStyle().setColor(TextFormatting.GOLD);
//                    ClientProxy.MC.ingameGUI.addChatMessage(ChatType.GAME_INFO, change);
//                }
//            }
//
//            jurassicreborn.NETWORK_WRAPPER.sendToServer(new SetOrderMessage(this));
//        }
//    }


    public List<Class<? extends LivingEntity>> getAttackTargets() {
        return this.attackTargets;
    }

    @Override
    public SpawnGroupData finalizeSpawn(
            ServerLevelAccessor level,
            DifficultyInstance diff,
            MobSpawnType reason,
            @Nullable SpawnGroupData spawnData
    ) {
        SpawnGroupData out = super.finalizeSpawn(level, diff, reason, spawnData);

        this.metabolism.setEnergy(this.metabolism.getMaxEnergy());
        this.metabolism.setWater(this.metabolism.getMaxWater());

        if (this.genetics == null || this.genetics.isEmpty()) {
            this.genetics = GeneticsHelper.randomGenetics(this.random);
        }
        if (this.getDNAQuality() <= 0) {
            this.setDNAQuality(100);
        }

        this.setMale(this.random.nextBoolean());

        if (reason != MobSpawnType.CHUNK_GENERATION) {
            this.refreshDimensions();
        }
        return out;
    }

    public int getAttackCooldown() {
        return this.attackCooldown;
    }

    public void resetAttackCooldown() {
        this.attackCooldown = 50 + this.getRandom().nextInt(20);
    }

    public void respondToAttack(LivingEntity attacker) {
        if (attacker != null && !attacker.isDeadOrDying() && !(attacker instanceof Player && ((Player) attacker).isCreative())) {
            // Ignore accidental hits from herd members to prevent infighting
            if (attacker instanceof DinosaurEntity dinoAttacker) {
                if (this.herd != null && dinoAttacker.herd == this.herd) {
                    return;
                }
            }
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
                    this.setTarget(enemies.get(this.getRandom().nextInt(enemies.size())));
                }
            }
        }
    }

    public int getAnimationLength() {
        return this.animationLength;
    }

    @Override
    public boolean isRunning() {
        return this.entityData.get(WATCHER_IS_RUNNING);
    }

    //    @Override
//    public boolean checkSpawnObstruction(LevelReader pLevel) {
//        if(!level().isClientSide())
//            return super.checkSpawnObstruction(pLevel) && this.level().dimensionType().equals(getServer().overworld().dimensionType());
//        return false;
//    }
    public boolean shouldEscapeWaterFast() {
        return true;
    }//so many things in this mod just make me go ***why*** - gamma_02

    private void handleLandDinosaurInFluid() {
        boolean inWater = this.isInWaterOrBubble();
        boolean inLava = this.inLava();

        if (!inWater && !inLava) {
            return;
        }

        if (this.isSleeping()) {
            this.disturbSleep();
        }

        this.getNavigation().setCanFloat(true);

        float height = this.getBbHeight();
        Vec3 motion = this.getDeltaMovement();

        double sizeBoost = Mth.clamp(1.0F - height, 0.0F, 0.7F) * 0.08D;
        double upward = (inWater ? 0.04D : 0.03D) + sizeBoost;

        if ((inWater && (this.isUnderWater() || this.isEyeInFluid(FluidTags.WATER))) || inLava) {
            upward += 0.04D;
        }

        if (motion.y < upward) {
            this.setDeltaMovement(motion.x, upward, motion.z);
            this.hasImpulse = true;
            this.fallDistance = 0.0F;
        }

        boolean needsShoreSearch = (this.getNavigation().isDone() || (this.tickCount % 40 == 0 && this.shouldEscapeWaterFast()))
                && (this.inWater() || this.inLava());

        if (needsShoreSearch) {
            BlockPos shore = AIUtils.findShore(this.level(), this.blockPosition());
            boolean pathFound = false;
            if (shore != null) {
                double speed = 1.2D + Math.max(0.0F, 1.0F - height) * 0.6D;
                pathFound = this.getNavigation().moveTo(shore.getX() + 0.5D, shore.getY(), shore.getZ() + 0.5D, speed);
            }

            if (!pathFound) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.05D, 0.0D));
            }

            // Otherwise if we're in water and moving along a path, make sure we keep swimming upwards when colliding.
        } else if (this.isInWater()) {
            Path path = this.getNavigation().getPath();
            if (path != null) {
                AABB detectionBox = this.getBoundingBox().inflate(0.5D, 0.5D, 0.5D);
                if (!this.level().noCollision(detectionBox)) { // If it collides with something
                    // There is no getCollisionBoxes() in 1.19.2, but we can approximate by checking collision shapes
                    for (VoxelShape shape : this.level().getBlockCollisions(this, detectionBox)) {
                        AABB box = shape.bounds();
                        if (box.maxY > this.getBoundingBox().minY) {
                            this.setJumping(true); // JumpHelper is replaced by setJumping in LivingEntity
                            break;
                        }
                    }
                }
            }
        }
    }



    public void target(Class<? extends LivingEntity>... entities){
        if(this.taskHelper == null) {//don't want to initialize it twice
            this.taskHelper = new TaskHelper(this.getClass());
        }
        this.attackTargets.addAll(Arrays.asList(entities));
        this.taskHelper.addGoal(new NearestAttackableTargetGoal<>(this, LivingEntity.class, 5, true, true,
                livingEntity -> entityPredicate(livingEntity, entities)), 3/*hope this randomly assigned 3 doesn't mess anything up*/);
    }

    public static boolean entityPredicate(LivingEntity input, Class<? extends LivingEntity> ... entities){
        // Use isAssignableFrom rather than == to match subclasses.  This allows
        // specifying a base class (e.g. Animal.class) and having all derived
        // classes match as well.
        return Arrays.stream(entities).anyMatch((clazz) -> clazz.isAssignableFrom(input.getClass()));
    }

    public void addTask(int priority, Goal goal){
        if(this.taskHelper == null){//jesus christ
            this.taskHelper = new TaskHelper(this.getClass());
        }
        this.taskHelper.addGoal(goal, priority);

        // If goals have already been registered, immediately add new tasks
        // to the active selectors so they take effect.
        if (this.goalsRegistered) {
            if (goal instanceof TargetGoal) {
                this.targetSelector.addGoal(priority, (TargetGoal) goal);
            } else {
                this.goalSelector.addGoal(priority, goal);
            }
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        if (this.dinosaur == null) return;

        // Movement helpers
        if (!dinosaur.isMarineCreature() && !(this instanceof AmphibianDinosaurEntity) && !(this instanceof PenguinDinosaurEntity)) {
            this.goalSelector.addGoal(0, new AdvancedSwimEntityAI(this));
        }

        this.goalSelector.addGoal(0, new EscapeWireEntityAI(this));
        this.goalSelector.addGoal(0, new FeederEntityAI(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));

        this.goalSelector.addGoal(1, new RespondToAttackEntityAI(this));
        this.goalSelector.addGoal(1, new TemptNonAdultEntityAI(this, 0.6D));

        if (dinosaur.getDiet().canEat(this, FoodType.PLANT))
            this.goalSelector.addGoal(2, new GrazeEntityAI(this));
        if (dinosaur.getDiet().canEat(this, FoodType.MEAT) || dinosaur.getDiet().canEat(this, FoodType.FISH))
            this.goalSelector.addGoal(1, new TargetCarcassAI(this));

        if (dinosaur.shouldDefendOwner()) {
            this.goalSelector.addGoal(2, new DefendOwnerAI(this));
            this.goalSelector.addGoal(2, new AssistOwnerAI(this));
        }
        if (dinosaur.shouldFlee()) {
            this.goalSelector.addGoal(2, new FleeAI(this));
        }
        this.goalSelector.addGoal(2, new ProtectInfantAI<>(this));

        this.goalSelector.addGoal(1, new DinosaurAttackMeleeEntityAI(this, this.dinosaur.getAttackSpeed(), true));
        this.goalSelector.addGoal(3, new FollowOwnerAI(this));
        this.goalSelector.addGoal(3, new DinosaurWanderEntityAI(this, 0.8D, 2, 10));
        // Idle/look
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, LivingEntity.class, 6.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));

        // Animation-only
        this.goalSelector.addGoal(1, new SleepEntityAI(this));
        this.goalSelector.addGoal(2, new DrinkEntityAI(this));
        this.goalSelector.addGoal(1, new MateEntityAI(this));
        this.goalSelector.addGoal(3, new EatFoodItemEntityAI(this));
        this.goalSelector.addGoal(3, new CallAnimationAI(this));
        this.goalSelector.addGoal(3, new RoarAnimationAI(this));
        this.goalSelector.addGoal(3, new LookAnimationAI(this));
        this.goalSelector.addGoal(3, new HeadCockAnimationAI(this));

        // Targets
        if (this.dinosaur.getDinosaurType() == Dinosaur.DinosaurType.AGGRESSIVE) {
            this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        }


        if (this.taskHelper != null) {
            this.taskHelper.setSelectorsAndRegisterGoals(this.goalSelector, this.targetSelector);
        }
    }


    private static final int FEEDER_RANGE = 16;
    private static final int FEEDER_VERTICAL = 8;
    public void rejectFeeder(@Nullable BlockPos pos, int cooldownTicks) {
        if (pos != null) {
            this.rejectedFeeder = pos.immutable();
            this.rejectedFeederUntilTick = this.tickCount + cooldownTicks;
        }
        this.invalidateClosestFeeder();
    }

    private boolean isRejectedFeeder(@Nullable BlockPos pos) {
        if (pos == null || this.rejectedFeeder == null) {
            return false;
        }

        if (this.tickCount >= this.rejectedFeederUntilTick) {
            this.rejectedFeeder = null;
            this.rejectedFeederUntilTick = 0;
            return false;
        }

        return this.rejectedFeeder.equals(pos);
    }

    private boolean isFeederInSearchWindow(BlockPos pos) {
        BlockPos here = this.blockPosition();
        return Math.abs(pos.getX() - here.getX()) <= this.getFeederSearchRadiusXZ()
                && Math.abs(pos.getZ() - here.getZ()) <= this.getFeederSearchRadiusXZ()
                && Math.abs(pos.getY() - here.getY()) <= this.getFeederSearchRadiusY();
    }

    public boolean canReachFeeder(BlockPos pos) {
        Level lvl = this.level();
        if (lvl == null || pos == null || !lvl.hasChunkAt(pos)) {
            return false;
        }

        if (!this.isFeederInSearchWindow(pos) || this.isRejectedFeeder(pos)) {
            return false;
        }

        BlockEntity be = lvl.getBlockEntity(pos);
        if (!(be instanceof FeederBlockEntity feeder) || !feeder.canServe(this)) {
            return false;
        }

        Vec3 target = feeder.getFeedingPos(this);
        if (target == null) {
            return false;
        }

        if (this instanceof FlyingDinosaurEntity) {
            BlockPos.MutableBlockPos probe = BlockPos.containing(target.x, target.y, target.z).mutable();
            while (probe.getY() > lvl.getMinBuildHeight() && lvl.getBlockState(probe).isAir()) {
                probe.move(0, -1, 0);
            }
            return !lvl.getBlockState(probe).isAir();
        }

        if (this.usesAquaticFeederLogic()) {
            return true;
        }

        Path path = this.getNavigation().createPath(
                BlockPos.containing(target.x, target.y, target.z),
                0
        );

        return path != null;
    }
    public BlockPos getClosestFeeder() {
        Level lvl = this.level();
        if (lvl == null || lvl.isClientSide || !this.getMetabolism().isHungry()) {
            return null;
        }

        if (this.closestFeeder != null && this.canReachFeeder(this.closestFeeder)) {
            return this.closestFeeder;
        }

        if (this.tickCount < this.feederSearchTick) {
            return null;
        }

        int delay;
        if (this instanceof FlyingDinosaurEntity) {
            delay = 20;
        } else if (this.usesAquaticFeederLogic()) {
            delay = 24;
        } else {
            delay = 40;
        }

        this.feederSearchTick = this.tickCount + delay + this.getRandom().nextInt(Math.max(1, delay / 2));

        BlockPos found = FeederRegistry.findNearest(
                lvl,
                this.blockPosition(),
                this,
                this.getFeederSearchRadiusXZ(),
                this.getFeederSearchRadiusY()
        );

        this.closestFeeder = (found != null && this.canReachFeeder(found)) ? found.immutable() : null;
        return this.closestFeeder;
    }

    private boolean isFeederUsable(BlockPos pos) {
        Level lvl = this.level();
        if (lvl == null || pos == null || !lvl.hasChunkAt(pos)) {
            return false;
        }

        BlockEntity blockEntity = lvl.getBlockEntity(pos);
        if (!(blockEntity instanceof FeederBlockEntity feeder)) {
            return false;
        }

        return feeder.canServe(this);
    }


    @Override
    public boolean isClimbing() {
        return false;
    }

    @Override
    public boolean isMoving() {
        Vec3 motion = this.getDeltaMovement();
        return motion.horizontalDistanceSqr() > 1.0E-5D;
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
    public <E extends LivingEntity & Animatable> PoseHandler<E> getPoseHandler() {
        return (PoseHandler<E>) this.dinosaur.getPoseHandler();
    }

    @Override
    public boolean inWater() {
        return this.isInWater();
    }

    @Override
    public boolean inLava() {
        return this.inLava;
    }

    public DinosaurAttributes getLegacyAttributes() {
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

    //    @Override
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

    public Vector3f getDinosaurCultivatorRotation() {///...what????
        this.setAnimation(EntityAnimation.GESTATED.get());
        return new Vector3f();
    }

    public void invalidateClosestFeeder() {
        this.closestFeeder = null;
        this.feederSearchTick = 0;
    }
    public void feedFromFeeder(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return;

        Item item = stack.getItem();
        this.getMetabolism().eat(FoodHelper.getHealAmount(item));
        FoodHelper.applyEatEffects(this, item);
        this.setAnimation(EntityAnimation.EATING.get());

        if (!this.level().isClientSide) {
            this.entityData.set(WATCHER_WAS_FED, true);
        }
    }
    protected int getFeederSearchRadiusXZ() {
        if (this instanceof FlyingDinosaurEntity) return 24;
        if (this.usesAquaticFeederLogic()) return 20;
        return 16;
    }
    public boolean usesAquaticFeederLogic() {
        return !(this instanceof FlyingDinosaurEntity)
                && this.canDinoSwim()
                && (this.isInWaterOrBubble()
                || this.isMarineCreature()
                || this instanceof SwimmingDinosaurEntity
                || this instanceof AmphibianDinosaurEntity);
    }
    protected int getFeederSearchRadiusY() {
        if (this instanceof FlyingDinosaurEntity) return 12;
        if (this.usesAquaticFeederLogic()) return 10;
        return 6;
    }
    public FieldGuideInfo getFieldGuideInfo() {
        return FieldGuideInfo.fromEntity(this);
    }

    public static class FieldGuideInfo {
        public int hunger;
        public int thirst;
        public boolean flocking;
        public boolean scared;
        public boolean hungry;
        public boolean thirsty;
        public boolean poisoned;

        // Construct from a DinosaurEntity
        public static FieldGuideInfo fromEntity(DinosaurEntity entity) {
            MetabolismContainer metabolism = entity.getMetabolism();
            Herd herd = entity.herd;
            FieldGuideInfo info = new FieldGuideInfo();
            info.flocking = herd != null && herd.members.size() > 1 && herd.state == Herd.State.MOVING;
            info.scared = herd != null && herd.fleeing;
            info.hunger = metabolism.getEnergy();
            info.thirst = metabolism.getWater();
            info.hungry = metabolism.isHungry();
            info.thirsty = metabolism.isThirsty();
            info.poisoned = entity.hasEffect(net.minecraft.world.effect.MobEffects.POISON);
            return info;
        }

        // Write to a buffer
        public static void write(FriendlyByteBuf buf, DinosaurEntity.FieldGuideInfo info) {
            buf.writeBoolean(info.flocking);
            buf.writeBoolean(info.scared);
            buf.writeInt(info.hunger);
            buf.writeInt(info.thirst);
            buf.writeBoolean(info.hungry);
            buf.writeBoolean(info.thirsty);
            buf.writeBoolean(info.poisoned);
        }

        public static DinosaurEntity.FieldGuideInfo read(FriendlyByteBuf buf) {
            DinosaurEntity.FieldGuideInfo info = new DinosaurEntity.FieldGuideInfo();
            info.flocking = buf.readBoolean();
            info.scared = buf.readBoolean();
            info.hunger = buf.readInt();
            info.thirst = buf.readInt();
            info.hungry = buf.readBoolean();
            info.thirsty = buf.readBoolean();
            info.poisoned = buf.readBoolean();
            return info;
        }
    }

    public enum Order {
        WANDER, FOLLOW, SIT
    }
}
