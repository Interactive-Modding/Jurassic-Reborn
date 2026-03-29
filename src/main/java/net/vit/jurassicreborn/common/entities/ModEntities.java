package net.vit.jurassicreborn.common.entities;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Bone;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.animal.CrabEntity;
import net.vit.jurassicreborn.common.entities.animal.SharkEntity;
import net.vit.jurassicreborn.common.entities.animal.GoatEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.resources.ResourceLocation;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.item.*;
import net.vit.jurassicreborn.common.entities.vehicle.*;
import net.vit.jurassicreborn.common.entities.vehicle.boat.JurassicBoat;
import net.vit.jurassicreborn.common.entities.vehicle.boat.JurassicChestBoat;

import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;


public class ModEntities {
    public static HashMap<Dinosaur, Bone.BoneGroup> boneMap = new HashMap<>();

    public static DeferredRegister<EntityType<?>> MOD_ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, JurassicReborn.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<VenomEntity>> VENOM = MOD_ENTITY_TYPES.register("venom_entity", () -> EntityType.Builder.<VenomEntity>of(VenomEntity::new, MobCategory.MISC).sized(0.35F, 0.35F).clientTrackingRange(64).updateInterval(10).build("venom_entity"));

    public static DeferredHolder<EntityType<?>, EntityType<CrabEntity>> CRAB = MOD_ENTITY_TYPES.register("crab", () -> EntityType.Builder.of(CrabEntity::new, MobCategory.CREATURE).sized(0.4f, 0.3f).build("crab"));

    public static DeferredHolder<EntityType<?>, EntityType<SharkEntity>> SHARK = MOD_ENTITY_TYPES.register("shark", () -> EntityType.Builder.of(SharkEntity::new, MobCategory.WATER_CREATURE).sized(1.6F, 0.8F).build("shark"));

    public static DeferredHolder<EntityType<?>, EntityType<GoatEntity>> GOAT = MOD_ENTITY_TYPES.register("goat", () -> EntityType.Builder.of(GoatEntity::new, MobCategory.CREATURE).sized(0.6f, 1.2f).build("goat"));
    public static final DeferredHolder<EntityType<?>, EntityType<BulletEntity>> BULLET_ENTITY =
            MOD_ENTITY_TYPES.register("bullet",
                    () -> EntityType.Builder
                            .<BulletEntity>of(BulletEntity::new, MobCategory.MISC)
                            .sized(0.2F, 0.2F)
                            .clientTrackingRange(4)
                            .updateInterval(1)
                            .build("bullet"));

    public static final DeferredHolder<EntityType<?>, EntityType<DinosaurEggEntity>> DINOSAUR_EGG =
            MOD_ENTITY_TYPES.register("dinosaur_egg",
                    () -> EntityType.Builder.<DinosaurEggEntity>of(DinosaurEggEntity::new, MobCategory.MISC)
                            .sized(0.3F, 0.5F)
                            .build("dinosaur_egg"));
    public static final DeferredHolder<EntityType<?>, EntityType<TranquilizerDartEntity>> TRANQUILIZER_DART =
            MOD_ENTITY_TYPES.register("tranquilizer_dart",
                    () -> EntityType.Builder.<TranquilizerDartEntity>of(TranquilizerDartEntity::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f) // Set size appropriate for darts!
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("tranquilizer_dart"));
    public static final DeferredHolder<EntityType<?>, EntityType<BlueprintPaintingEntity>> BLUEPRINT_PAINTING =
            MOD_ENTITY_TYPES.register("blueprint_painting",
                    () -> EntityType.Builder.<BlueprintPaintingEntity>of(
                                    (type, level) -> new BlueprintPaintingEntity(type, level),
                                    net.minecraft.world.entity.MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build("blueprint_painting"));
    public static final DeferredHolder<EntityType<?>, EntityType<PaddockSignEntity>> PADDOCK_SIGN =
            MOD_ENTITY_TYPES.register("paddock_sign",
                    () -> EntityType.Builder.<PaddockSignEntity>of(
                                    (type, level) -> new PaddockSignEntity(type, level),
                                    net.minecraft.world.entity.MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build("paddock_sign"));
    public static final DeferredHolder<EntityType<?>, EntityType<AttractionSignEntity>> ATTRACTION_SIGN =
            MOD_ENTITY_TYPES.register("attraction_sign",
                    () -> EntityType.Builder.<AttractionSignEntity>of(
                                    (type, level) -> new AttractionSignEntity(type, level),
                                    MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build("attraction_sign"));
    public static final DeferredHolder<EntityType<?>, EntityType<MuralPaintingEntity>> MURAL_PAINTING =
            MOD_ENTITY_TYPES.register("mural_painting",
                    () -> EntityType.Builder.<MuralPaintingEntity>of(
                                    (type, level) -> new MuralPaintingEntity(type, level),
                                    net.minecraft.world.entity.MobCategory.MISC)
                            .sized(0.5F, 0.5F)
                            .build("mural_painting"));
    public static final DeferredHolder<EntityType<?>, EntityType<ParkBenchSeatLeftEntity>> PARK_BENCH_SEAT_LEFT =
            MOD_ENTITY_TYPES.register("park_bench_seat_left", () ->
                    EntityType.Builder.<ParkBenchSeatLeftEntity>of(ParkBenchSeatLeftEntity::new, MobCategory.MISC)
                            .sized(0.001f, 0.001f) // tiny, invisible
                            .clientTrackingRange(10)
                            .setShouldReceiveVelocityUpdates(false)
                            .build("park_bench_seat_left"));

    public static final DeferredHolder<EntityType<?>, EntityType<ParkBenchSeatRightEntity>> PARK_BENCH_SEAT_RIGHT =
            MOD_ENTITY_TYPES.register("park_bench_seat_right", () ->
                    EntityType.Builder.<ParkBenchSeatRightEntity>of(ParkBenchSeatRightEntity::new, MobCategory.MISC)
                            .sized(0.001f, 0.001f)
                            .clientTrackingRange(10)
                            .setShouldReceiveVelocityUpdates(false)
                            .build("park_bench_seat_right"));
//    public static final DeferredHolder<EntityType<?>, EntityType<DartPoisonCycasinEntity>> DART_POISON_CYCASIN =
//            MOD_ENTITY_TYPES.register("dart_poison_cycasin",
//                    () -> EntityType.Builder.<DartPoisonCycasinEntity>of(DartPoisonCycasinEntity::new, MobCategory.MISC)
//                            .sized(0.25f, 0.25f)
//                            .clientTrackingRange(4)
//                            .updateInterval(10)
//                            .build("dart_poison_cycasin"));
//
    public static final DeferredHolder<EntityType<?>, EntityType<TrackingDartEntity>> TRACKING_DART =
            MOD_ENTITY_TYPES.register("tracking_dart",
                    () -> EntityType.Builder.<TrackingDartEntity>of(TrackingDartEntity::new, MobCategory.MISC)
                            .sized(0.25f, 0.25f)
                            .clientTrackingRange(4)
                            .updateInterval(10)
                            .build("tracking_dart"));
    // ───────────────────────────────────────────────────────────── VEHICLES ──
    public static final DeferredHolder<EntityType<?>, EntityType<FordExplorerEntity>> FORD_EXPLORER =
            MOD_ENTITY_TYPES.register("ford_explorer",
                    () -> EntityType.Builder
                            .<FordExplorerEntity>of((type, level) -> new FordExplorerEntity(type, level),
                                    MobCategory.MISC)          // they’re not natural spawns
                            .sized(3.0f, 2.5f)                                   // width, height (match model)
                            .clientTrackingRange(10)                             // how far the client keeps it
                            .updateInterval(1)                                   // every tick
                            .build("ford_explorer"));
    public static final DeferredHolder<EntityType<?>, EntityType<FordExplorerSnowEntity>> FORD_EXPLORER_SNOW =
            MOD_ENTITY_TYPES.register("ford_explorer_snow",
                    () -> EntityType.Builder
                            .<FordExplorerSnowEntity>of((type, level) -> new FordExplorerSnowEntity(type, level),
                                    MobCategory.MISC)          // they’re not natural spawns
                            .sized(3.0f, 2.5f)                                   // width, height (match model)
                            .clientTrackingRange(10)                             // how far the client keeps it
                            .updateInterval(1)                                   // every tick
                            .build("ford_explorer_snow"));
    public static final DeferredHolder<EntityType<?>, EntityType<MonorailEntity>> MONORAIL =
            MOD_ENTITY_TYPES.register("monorail",
                    () -> EntityType.Builder
                            .<MonorailEntity>of((type, level) -> new MonorailEntity(type, level),
                                    MobCategory.MISC)          // they’re not natural spawns
                            .sized(3.0f, 2.5f)                                   // width, height (match model)
                            .clientTrackingRange(10)                             // how far the client keeps it
                            .updateInterval(1)                                   // every tick
                            .build("monorail"));
    public static final DeferredHolder<EntityType<?>, EntityType<JeepWranglerEntity>> JEEP_WRANGLER =
            MOD_ENTITY_TYPES.register("jeep_wrangler",
                    () -> EntityType.Builder
                            .<JeepWranglerEntity>of((type, level) -> new JeepWranglerEntity(type, level),
                                    MobCategory.MISC)          // they’re not natural spawns
                            .sized(3.0f, 2.5f)                                   // width, height (match model)
                            .clientTrackingRange(10)                             // how far the client keeps it
                            .updateInterval(1)                                   // every tick
                            .build("jeep_wrangler"));
    public static final DeferredHolder<EntityType<?>, EntityType<BlackJeepWranglerEntity>> BLACK_JEEP_WRANGLER =
            MOD_ENTITY_TYPES.register("black_jeep_wrangler",
                    () -> EntityType.Builder
                            .<BlackJeepWranglerEntity>of((type, level) -> new BlackJeepWranglerEntity(type, level),
                                    MobCategory.MISC)          // they’re not natural spawns
                            .sized(3.0f, 2.5f)                                   // width, height (match model)
                            .clientTrackingRange(10)                             // how far the client keeps it
                            .updateInterval(1)                                   // every tick
                            .build("black_jeep_wrangler"));
    public static final DeferredHolder<EntityType<?>, EntityType<BlueJeepWranglerEntity>> BLUE_JEEP_WRANGLER =
            MOD_ENTITY_TYPES.register("blue_jeep_wrangler",
                    () -> EntityType.Builder
                            .<BlueJeepWranglerEntity>of((type, level) -> new BlueJeepWranglerEntity(type, level),
                                    MobCategory.MISC)          // they’re not natural spawns
                            .sized(3.0f, 2.5f)                                   // width, height (match model)
                            .clientTrackingRange(10)                             // how far the client keeps it
                            .updateInterval(1)                                   // every tick
                            .build("blue_jeep_wrangler"));
    public static final DeferredHolder<EntityType<?>, EntityType<GreenJeepWranglerEntity>> GREEN_JEEP_WRANGLER =
            MOD_ENTITY_TYPES.register("green_jeep_wrangler",
                    () -> EntityType.Builder
                            .<GreenJeepWranglerEntity>of((type, level) -> new GreenJeepWranglerEntity(type, level),
                                    MobCategory.MISC)          // they’re not natural spawns
                            .sized(3.0f, 2.5f)                                   // width, height (match model)
                            .clientTrackingRange(10)                             // how far the client keeps it
                            .updateInterval(1)                                   // every tick
                            .build("green_jeep_wrangler"));
    public static final DeferredHolder<EntityType<?>, EntityType<LimeJeepWranglerEntity>> LIME_JEEP_WRANGLER =
            MOD_ENTITY_TYPES.register("lime_jeep_wrangler",
                    () -> EntityType.Builder
                            .<LimeJeepWranglerEntity>of((type, level) -> new LimeJeepWranglerEntity(type, level),
                                    MobCategory.MISC)          // they’re not natural spawns
                            .sized(3.0f, 2.5f)                                   // width, height (match model)
                            .clientTrackingRange(10)                             // how far the client keeps it
                            .updateInterval(1)                                   // every tick
                            .build("lime_jeep_wrangler"));
    public static final DeferredHolder<EntityType<?>, EntityType<PinkJeepWranglerEntity>> PINK_JEEP_WRANGLER =
            MOD_ENTITY_TYPES.register("pink_jeep_wrangler",
                    () -> EntityType.Builder
                            .<PinkJeepWranglerEntity>of((type, level) -> new PinkJeepWranglerEntity(type, level),
                                    MobCategory.MISC)          // they’re not natural spawns
                            .sized(3.0f, 2.5f)                                   // width, height (match model)
                            .clientTrackingRange(10)                             // how far the client keeps it
                            .updateInterval(1)                                   // every tick
                            .build("pink_jeep_wrangler"));
    public static final DeferredHolder<EntityType<?>, EntityType<PurpleJeepWranglerEntity>> PURPLE_JEEP_WRANGLER =
            MOD_ENTITY_TYPES.register("purple_jeep_wrangler",
                    () -> EntityType.Builder
                            .<PurpleJeepWranglerEntity>of((type, level) -> new PurpleJeepWranglerEntity(type, level),
                                    MobCategory.MISC)          // they’re not natural spawns
                            .sized(3.0f, 2.5f)                                   // width, height (match model)
                            .clientTrackingRange(10)                             // how far the client keeps it
                            .updateInterval(1)                                   // every tick
                            .build("purple_jeep_wrangler"));
    public static final DeferredHolder<EntityType<?>, EntityType<SornaJeepWranglerEntity>> SORNA_JEEP_WRANGLER =
            MOD_ENTITY_TYPES.register("sorna_jeep_wrangler",
                    () -> EntityType.Builder
                            .<SornaJeepWranglerEntity>of((type, level) -> new SornaJeepWranglerEntity(type, level),
                                    MobCategory.MISC)          // they’re not natural spawns
                            .sized(3.0f, 2.5f)                                   // width, height (match model)
                            .clientTrackingRange(10)                             // how far the client keeps it
                            .updateInterval(1)                                   // every tick
                            .build("sorna_jeep_wrangler"));
    public static final DeferredHolder<EntityType<?>, EntityType<GyrosphereEntity>> GYROSPHERE =
            MOD_ENTITY_TYPES.register("gyrosphere",
                    () -> EntityType.Builder
                            .<GyrosphereEntity>of((type, level) -> new GyrosphereEntity(type, level),
                                    MobCategory.MISC)          // they’re not natural spawns
                            .sized(3.0f, 2.5f)                                   // width, height (match model)
                            .clientTrackingRange(10)                             // how far the client keeps it
                            .updateInterval(1)                                   // every tick
                            .build("gyrosphere"));
    public static final DeferredHolder<EntityType<?>, EntityType<HelicopterBaseEntity>> HELICOPTER =            MOD_ENTITY_TYPES.register("helicopter",
            () -> EntityType.Builder
                    .<HelicopterBaseEntity>of(HelicopterBaseEntity::new,                                    MobCategory.MISC)
                    .sized(4.0f, 3.0f)
                    .clientTrackingRange(10)
                    .updateInterval(1)
                    .build("helicopter"));

    public static final DeferredHolder<EntityType<?>, EntityType<JurassicBoat>> JURASSIC_BOAT =
            MOD_ENTITY_TYPES.register("jurassic_boat",
                    () -> EntityType.Builder
                            .<JurassicBoat>of(JurassicBoat::new, MobCategory.MISC)
                            .sized(1.375F, 0.5625F)
                            .clientTrackingRange(10)
                            .build("jurassic_boat"));

    public static final DeferredHolder<EntityType<?>, EntityType<JurassicChestBoat>> JURASSIC_CHEST_BOAT =
            MOD_ENTITY_TYPES.register("jurassic_chest_boat",
                    () -> EntityType.Builder
                            .<JurassicChestBoat>of(JurassicChestBoat::new, MobCategory.MISC)
                            .sized(1.375F, 0.5625F)
                            .clientTrackingRange(10)
                            .build("jurassic_chest_boat"));

    public static DeferredHolder<EntityType<?>, EntityType<OviraptorEntity>> OVIRAPTOR = MOD_ENTITY_TYPES.register("oviraptor", () -> EntityType.Builder.of(OviraptorEntity::new, MobCategory.CREATURE).build("oviraptor"));
    public static DeferredHolder<EntityType<?>, EntityType<DeinotheriumEntity>> DEINOTHERIUM = MOD_ENTITY_TYPES.register("deinotherium", () -> EntityType.Builder.of(DeinotheriumEntity::new, MobCategory.CREATURE).build("deinotherium"));
    public static DeferredHolder<EntityType<?>, EntityType<MicroraptorEntity>> MICRORAPTOR = MOD_ENTITY_TYPES.register("microraptor", () -> EntityType.Builder.of(MicroraptorEntity::new, MobCategory.CREATURE).build("microraptor"));
    public static DeferredHolder<EntityType<?>, EntityType<MammothEntity>> MAMMOTH = MOD_ENTITY_TYPES.register("mammoth", () -> EntityType.Builder.of(MammothEntity::new, MobCategory.CREATURE).build("mammoth"));
    public static DeferredHolder<EntityType<?>, EntityType<DodoEntity>> DODO = MOD_ENTITY_TYPES.register("dodo", () -> EntityType.Builder.of(DodoEntity::new, MobCategory.CREATURE).build("dodo"));
    public static DeferredHolder<EntityType<?>, EntityType<ZhenyuanopterusEntity>> ZHENYUANOPTERUS = MOD_ENTITY_TYPES.register("zhenyuanopterus", () -> EntityType.Builder.of(ZhenyuanopterusEntity::new, MobCategory.CREATURE).build("zhenyuanopterus"));
    public static DeferredHolder<EntityType<?>, EntityType<PostosuchusEntity>> POSTOSUCHUS = MOD_ENTITY_TYPES.register("postosuchus", () -> EntityType.Builder.of(PostosuchusEntity::new, MobCategory.CREATURE).build("postosuchus"));
    public static DeferredHolder<EntityType<?>, EntityType<IndoraptorEntity>> INDORAPTOR = MOD_ENTITY_TYPES.register("indoraptor", () -> EntityType.Builder.of(IndoraptorEntity::new, MobCategory.CREATURE).build("indoraptor"));
    public static DeferredHolder<EntityType<?>, EntityType<OthnieliaEntity>> OTHNIELIA = MOD_ENTITY_TYPES.register("othnielia", () -> EntityType.Builder.of(OthnieliaEntity::new, MobCategory.CREATURE).build("othnielia"));
    public static DeferredHolder<EntityType<?>, EntityType<PteranodonEntity>> PTERANODON = MOD_ENTITY_TYPES.register("pteranodon", () -> EntityType.Builder.<PteranodonEntity>of((type, world) -> new PteranodonEntity(world, type), MobCategory.CREATURE).build("pteranodon"));
    public static DeferredHolder<EntityType<?>, EntityType<IndominusEntity>> INDOMINUS = MOD_ENTITY_TYPES.register("indominus", () -> EntityType.Builder.<IndominusEntity>of((type, world) -> new IndominusEntity(world, type), MobCategory.CREATURE).build("indominus"));
    public static DeferredHolder<EntityType<?>, EntityType<AnkylosaurusEntity>> ANKYLOSAURUS = MOD_ENTITY_TYPES.register("ankylosaurus", () -> EntityType.Builder.<AnkylosaurusEntity>of((type, world) -> new AnkylosaurusEntity(world, type), MobCategory.CREATURE).build("ankylosaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<ArsinoitheriumEntity>> ARSINOITHERIUM = MOD_ENTITY_TYPES.register("arsinoitherium", () -> EntityType.Builder.<ArsinoitheriumEntity>of((type, world) -> new ArsinoitheriumEntity(world, type), MobCategory.CREATURE).build("arsinoitherium"));
    public static DeferredHolder<EntityType<?>, EntityType<CrassigyrinusEntity>> CRASSIGYRINUS = MOD_ENTITY_TYPES.register("crassigyrinus", () -> EntityType.Builder.<CrassigyrinusEntity>of((type, world) -> new CrassigyrinusEntity(world, type), MobCategory.CREATURE).build("crassigyrinus"));
    public static DeferredHolder<EntityType<?>, EntityType<PerisphinctesEntity>> PERISPHINCTES = MOD_ENTITY_TYPES.register("perisphinctes", () -> EntityType.Builder.<PerisphinctesEntity>of((type, world) -> new PerisphinctesEntity(world, type), MobCategory.CREATURE).build("perisphinctes"));
    public static DeferredHolder<EntityType<?>, EntityType<ProceratosaurusEntity>> PROCERATOSAURUS = MOD_ENTITY_TYPES.register("proceratosaurus", () -> EntityType.Builder.of(ProceratosaurusEntity::new, MobCategory.CREATURE).build("proceratosaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<ApatosaurusEntity>> APATOSAURUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("apatosaurus", () -> EntityType.Builder.<ApatosaurusEntity>of((type, world) -> new ApatosaurusEntity(world, type), MobCategory.CREATURE).build("apatosaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<CarnotaurusEntity>> CARNOTAURUS = MOD_ENTITY_TYPES.register("carnotaurus", () -> EntityType.Builder.<CarnotaurusEntity>of((type, world) -> new CarnotaurusEntity(world, type), MobCategory.CREATURE).build("carnotaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<DunkleosteusEntity>> DUNKLEOSTEUS = MOD_ENTITY_TYPES.register("dunkleosteus", () -> EntityType.Builder.<DunkleosteusEntity>of((type, world) -> new DunkleosteusEntity(world, type), MobCategory.CREATURE).build("dunkleosteus"));
    public static DeferredHolder<EntityType<?>, EntityType<TyrannosaurusEntity>> TYRANNOSAURUS = MOD_ENTITY_TYPES.register("tyrannosaurus", () -> EntityType.Builder.<TyrannosaurusEntity>of((type, world) -> new TyrannosaurusEntity(world, type), MobCategory.CREATURE).build("tyrannosaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<RaphusrexEntity>> RAPHUSREX = MOD_ENTITY_TYPES.register("raphusrex", () -> EntityType.Builder.<RaphusrexEntity>of((type, world) -> new RaphusrexEntity(world, type), MobCategory.CREATURE).build("raphusrex"));
    public static DeferredHolder<EntityType<?>, EntityType<ChasmosaurusEntity>> CHASMOSAURUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("chasmosaurus", () -> EntityType.Builder.<ChasmosaurusEntity>of((type, world) -> new ChasmosaurusEntity(world, type), MobCategory.CREATURE).build("chasmosaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<MetriacanthosaurusEntity>> METRIACANTHOSAURUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("metriacanthosaurus", () -> EntityType.Builder.of(MetriacanthosaurusEntity::new, MobCategory.CREATURE).build("metriacanthosaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<TroodonEntity>> TROODON = MOD_ENTITY_TYPES.register("troodon", () -> EntityType.Builder.<TroodonEntity>of((type, world) -> new TroodonEntity(world, type), MobCategory.CREATURE).build("troodon"));
    public static DeferredHolder<EntityType<?>, EntityType<HerrerasaurusEntity>> HERRERASAURUS = MOD_ENTITY_TYPES.register("herrerasaurus", () -> EntityType.Builder.of(HerrerasaurusEntity::new, MobCategory.CREATURE).build("herrerasaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<BaryonyxEntity>> BARYONYX = MOD_ENTITY_TYPES.register("baryonyx", () -> EntityType.Builder.<BaryonyxEntity>of((type, world) -> new BaryonyxEntity(world, type), MobCategory.CREATURE).build("baryonyx"));
    public static DeferredHolder<EntityType<?>, EntityType<BeelzebufoEntity>> BEELZEBUFO_ENTITY_TYPE = MOD_ENTITY_TYPES.register("beelzebufo", () -> EntityType.Builder.<BeelzebufoEntity>of((type, world) -> new BeelzebufoEntity(world, type), MobCategory.CREATURE).build("beelzebufo"));
    public static DeferredHolder<EntityType<?>, EntityType<VelociraptorBlueEntity>> VELOCIRAPTORBLUE_ENTITY_TYPE = MOD_ENTITY_TYPES.register("velociraptorblue", () -> EntityType.Builder.of(VelociraptorBlueEntity::new, MobCategory.CREATURE).build("velociraptorblue"));
    public static DeferredHolder<EntityType<?>, EntityType<VelociraptorEchoEntity>> VELOCIRAPTORECHO_ENTITY_TYPE = MOD_ENTITY_TYPES.register("velociraptorecho", () -> EntityType.Builder.of(VelociraptorEchoEntity::new, MobCategory.CREATURE).build("velociraptorecho"));
    public static DeferredHolder<EntityType<?>, EntityType<SinoceratopsEntity>> SINOCERATOPS = MOD_ENTITY_TYPES.register("sinoceratops", () -> EntityType.Builder.<SinoceratopsEntity>of((type, world) -> new SinoceratopsEntity(world, type), MobCategory.CREATURE).build("sinoceratops"));
    public static DeferredHolder<EntityType<?>, EntityType<ParasaurolophusEntity>> PARASAUROLOPHUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("parasaurolophus", () -> EntityType.Builder.of(ParasaurolophusEntity::new, MobCategory.CREATURE).build("parasaurolophus"));
    public static DeferredHolder<EntityType<?>, EntityType<MamenchisaurusEntity>> MAMENCHISAURUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("mamenchisaurus", () -> EntityType.Builder.of(MamenchisaurusEntity::new, MobCategory.CREATURE).build("mamenchisaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<DimorphodonEntity>> DIMORPHODON_ENTITY_TYPE = MOD_ENTITY_TYPES.register("dimorphodon", () -> EntityType.Builder.<DimorphodonEntity>of((type, world) -> new DimorphodonEntity(world, type), MobCategory.CREATURE).build("dimorphodon"));
    public static DeferredHolder<EntityType<?>, EntityType<AllosaurusEntity>> ALLOSAURUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("allosaurus", () -> EntityType.Builder.<AllosaurusEntity>of((type, world) -> new AllosaurusEntity(world, type), MobCategory.CREATURE).build("allosaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<MosasaurusEntity>> MOSASAURUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("mosasaurus", () -> EntityType.Builder.<MosasaurusEntity>of((type, world) -> new MosasaurusEntity(world, type), MobCategory.CREATURE).build("mosasaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<MawsoniaEntity>> MAWSONIA_ENTITY_TYPE = MOD_ENTITY_TYPES.register("mawsonia", () -> EntityType.Builder.<MawsoniaEntity>of((type, world) -> new MawsoniaEntity(world, type), MobCategory.CREATURE).build("mawsonia"));
    public static DeferredHolder<EntityType<?>, EntityType<VelociraptorDeltaEntity>> VELOCIRAPTORDELTA = MOD_ENTITY_TYPES.register("velociraptordelta", () -> EntityType.Builder.of(VelociraptorDeltaEntity::new, MobCategory.CREATURE).build("velociraptordelta"));
    public static DeferredHolder<EntityType<?>, EntityType<AlvarezsaurusEntity>> ALVAREZSAURUS = MOD_ENTITY_TYPES.register("alvarezsaurus", () -> EntityType.Builder.of(AlvarezsaurusEntity::new, MobCategory.CREATURE).build("alvarezsaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<RugopsEntity>> RUGOPS = MOD_ENTITY_TYPES.register("rugops", () -> EntityType.Builder.of(RugopsEntity::new, MobCategory.CREATURE).build("rugops"));
    public static DeferredHolder<EntityType<?>, EntityType<CearadactylusEntity>> CEARADACTYLUS = MOD_ENTITY_TYPES.register("cearadactylus", () -> EntityType.Builder.<CearadactylusEntity>of((type, world) -> new CearadactylusEntity(world, type), MobCategory.CREATURE).build("cearadactylus"));
    public static DeferredHolder<EntityType<?>, EntityType<CorythosaurusEntity>> CORYTHOSAURUS = MOD_ENTITY_TYPES.register("corythosaurus", () -> EntityType.Builder.<CorythosaurusEntity>of((type, world) -> new CorythosaurusEntity(world, type), MobCategory.CREATURE).build("corythosaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<CompsognathusEntity>> COMPSOGNATHUS = MOD_ENTITY_TYPES.register("compsognathus", () -> EntityType.Builder.<CompsognathusEntity>of((type, world) -> new CompsognathusEntity(world, type), MobCategory.CREATURE).build("compsognathus"));
    public static DeferredHolder<EntityType<?>, EntityType<LudodactylusEntity>> LUDODACTYLUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("ludodactylus", () -> EntityType.Builder.<LudodactylusEntity>of((type, world) -> new LudodactylusEntity(world, type), MobCategory.CREATURE).build("ludodactylus"));
    public static DeferredHolder<EntityType<?>, EntityType<LeaellynasauraEntity>> LEAELLYNASAURA_ENTITY_TYPE = MOD_ENTITY_TYPES.register("leaellynasaura", () -> EntityType.Builder.of(LeaellynasauraEntity::new, MobCategory.CREATURE).build("leaellynasaura"));
    public static DeferredHolder<EntityType<?>, EntityType<MoganopterusEntity>> MOGANOPTERUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("moganopterus", () -> EntityType.Builder.<MoganopterusEntity>of((type, world) -> new MoganopterusEntity(world, type), MobCategory.CREATURE).build("moganopterus"));
    public static DeferredHolder<EntityType<?>, EntityType<SuchomimusEntity>> SUCHOMIMUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("suchomimus", () -> EntityType.Builder.<SuchomimusEntity>of((type, world) -> new SuchomimusEntity(world, type), MobCategory.CREATURE).build("suchomimus"));
    public static DeferredHolder<EntityType<?>, EntityType<MajungasaurusEntity>> MAJUNGASAURUS = MOD_ENTITY_TYPES.register("majungasaurus", () -> EntityType.Builder.of(MajungasaurusEntity::new, MobCategory.CREATURE).build("majungasaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<ProtoceratopsEntity>> PROTOCERATOPS = MOD_ENTITY_TYPES.register("protoceratops", () -> EntityType.Builder.of(ProtoceratopsEntity::new, MobCategory.CREATURE).build("protoceratops"));
    public static DeferredHolder<EntityType<?>, EntityType<TitanisEntity>> TITANIS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("titanis", () -> EntityType.Builder.<TitanisEntity>of((type, world) -> new TitanisEntity(world, type), MobCategory.CREATURE).build("titanis"));
    public static DeferredHolder<EntityType<?>, EntityType<CoelacanthEntity>> COELACANTH_ENTITY_TYPE = MOD_ENTITY_TYPES.register("coelacanth", () -> EntityType.Builder.<CoelacanthEntity>of((type, world) -> new CoelacanthEntity(world, type), MobCategory.CREATURE).build("coelacanth"));
    public static DeferredHolder<EntityType<?>, EntityType<GallimimusEntity>> GALLIMIMUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("gallimimus", () -> EntityType.Builder.<GallimimusEntity>of((type, world) -> new GallimimusEntity(world, type), MobCategory.CREATURE).build("gallimimus"));
    public static DeferredHolder<EntityType<?>, EntityType<CeratosaurusEntity>> CERATOSAURUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("ceratosaurus", () -> EntityType.Builder.<CeratosaurusEntity>of((type, world) -> new CeratosaurusEntity(world, type), MobCategory.CREATURE).build("ceratosaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<VelociraptorCharlieEntity>> VELOCIRAPTORCHARLIE_ENTITY_TYPE = MOD_ENTITY_TYPES.register("velociraptorcharlie", () -> EntityType.Builder.of(VelociraptorCharlieEntity::new, MobCategory.CREATURE).build("velociraptorcharlie"));
    public static DeferredHolder<EntityType<?>, EntityType<SpinosaurusEntity>> SPINOSAURUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("spinosaurus", () -> EntityType.Builder.<SpinosaurusEntity>of((type, world) -> new SpinosaurusEntity(world, type), MobCategory.CREATURE).build("spinosaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<PachycephalosaurusEntity>> PACHYCEPHALOSAURUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("pachycephalosaurus", () -> EntityType.Builder.of(PachycephalosaurusEntity::new, MobCategory.CREATURE).build("pachycephalosaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<QuetzalEntity>> QUETZAL = MOD_ENTITY_TYPES.register("quetzal", () -> EntityType.Builder.of(QuetzalEntity::new, MobCategory.CREATURE).build("quetzal"));
    public static DeferredHolder<EntityType<?>, EntityType<CarcharodontosaurusEntity>> CARCHARODONTOSAURUS = MOD_ENTITY_TYPES.register("carcharodontosaurus", () -> EntityType.Builder.of(CarcharodontosaurusEntity::new, MobCategory.CREATURE).build("carcharodontosaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<TylosaurusEntity>> TYLOSAURUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("tylosaurus", () -> EntityType.Builder.<TylosaurusEntity>of((type, world) -> new TylosaurusEntity(world, type), MobCategory.CREATURE).build("tylosaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<LivyatanEntity>> LIVYATAN_ENTITY_TYPE = MOD_ENTITY_TYPES.register("livyatan", () -> EntityType.Builder.<LivyatanEntity>of((type, world) -> new LivyatanEntity(world, type), MobCategory.CREATURE).build("livyatan"));
    public static DeferredHolder<EntityType<?>, EntityType<MegalodonEntity>> MEGALODON_ENTITY_TYPE = MOD_ENTITY_TYPES.register("megalodon", () -> EntityType.Builder.<MegalodonEntity>of((type, world) -> new MegalodonEntity(world, type), MobCategory.CREATURE).build("megalodon"));
    public static DeferredHolder<EntityType<?>, EntityType<OrnithomimusEntity>> ORNITHOMIMUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("ornithomimus", () -> EntityType.Builder.<OrnithomimusEntity>of((type, world) -> new OrnithomimusEntity(world, type), MobCategory.CREATURE).build("ornithomimus"));
    public static DeferredHolder<EntityType<?>, EntityType<MegapiranhaEntity>> MEGAPIRANHA_ENTITY_TYPE = MOD_ENTITY_TYPES.register("megapiranha", () -> EntityType.Builder.<MegapiranhaEntity>of((type, world) -> new MegapiranhaEntity(world, type), MobCategory.CREATURE).build("megapiranha"));
    public static DeferredHolder<EntityType<?>, EntityType<DiplodocusEntity>> DIPLODOCUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("diplodocus", () -> EntityType.Builder.<DiplodocusEntity>of((type, world) -> new DiplodocusEntity(world, type), MobCategory.CREATURE).build("diplodocus"));
    public static DeferredHolder<EntityType<?>, EntityType<DilophosaurusEntity>> DILOPHOSAURUS = MOD_ENTITY_TYPES.register("dilophosaurus", () -> EntityType.Builder.of(DilophosaurusEntity::new, MobCategory.CREATURE).build("dilophosaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<StyracosaurusEntity>> STYRACOSAURUS = MOD_ENTITY_TYPES.register("styracosaurus", () -> EntityType.Builder.of(StyracosaurusEntity::new, MobCategory.CREATURE).build("styracosaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<GuanlongEntity>> GUANLONG_ENTITY_TYPE = MOD_ENTITY_TYPES.register("guanlong", () -> EntityType.Builder.<GuanlongEntity>of((type, world) -> new GuanlongEntity(world, type), MobCategory.CREATURE).build("guanlong"));
    public static DeferredHolder<EntityType<?>, EntityType<CamarasaurusEntity>> CAMARASAURUS = MOD_ENTITY_TYPES.register("camarasaurus", () -> EntityType.Builder.of(CamarasaurusEntity::new, MobCategory.CREATURE).build("camarasaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<NigersaurusEntity>> NIGERSAURUS = MOD_ENTITY_TYPES.register("nigersaurus", () -> EntityType.Builder.of(NigersaurusEntity::new, MobCategory.CREATURE).build("nigersaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<HyaenodonEntity>> HYAENODON_ENTITY_TYPE = MOD_ENTITY_TYPES.register("hyaenodon", () -> EntityType.Builder.<HyaenodonEntity>of((type, world) -> new HyaenodonEntity(world, type), MobCategory.CREATURE).build("hyaenodon"));
    public static DeferredHolder<EntityType<?>, EntityType<CoelurusEntity>> COELURUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("coelurus", () -> EntityType.Builder.<CoelurusEntity>of((type, world) -> new CoelurusEntity(world, type), MobCategory.CREATURE).build("coelurus"));
    public static DeferredHolder<EntityType<?>, EntityType<DiplocaulusEntity>> DIPLOCAULUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("diplocaulus", () -> EntityType.Builder.<DiplocaulusEntity>of((type, world) -> new DiplocaulusEntity(world, type), MobCategory.CREATURE).build("diplocaulus"));
    public static DeferredHolder<EntityType<?>, EntityType<CalymeneEntity>> CALYMENE_ENTITY_TYPE = MOD_ENTITY_TYPES.register("calymene", () -> EntityType.Builder.<CalymeneEntity>of((type, world) -> new CalymeneEntity(world, type), MobCategory.CREATURE).build("calymene"));
    public static DeferredHolder<EntityType<?>, EntityType<DreadnoughtusEntity>> DREADNOUGHTUS = MOD_ENTITY_TYPES.register("dreadnoughtus", () -> EntityType.Builder.of(DreadnoughtusEntity::new, MobCategory.CREATURE).build("dreadnoughtus"));
    public static DeferredHolder<EntityType<?>, EntityType<MaiasauraEntity>> MAIASAURA = MOD_ENTITY_TYPES.register("maiasaura", () -> EntityType.Builder.of(MaiasauraEntity::new, MobCategory.CREATURE).build("maiasaura"));
    public static DeferredHolder<EntityType<?>, EntityType<PatagotitanEntity>> PATAGOTITAN = MOD_ENTITY_TYPES.register("patagotitan", () -> EntityType.Builder.of(PatagotitanEntity::new, MobCategory.CREATURE).build("patagotitan"));
    public static DeferredHolder<EntityType<?>, EntityType<EdmontosaurusEntity>> EDMONTOSAURUS = MOD_ENTITY_TYPES.register("edmontosaurus", () -> EntityType.Builder.of(EdmontosaurusEntity::new, MobCategory.CREATURE).build("edmontosaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<StegosaurusEntity>> STEGOSAURUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("stegosaurus", () -> EntityType.Builder.<StegosaurusEntity>of((type, world) -> new StegosaurusEntity(world, type), MobCategory.CREATURE).build("stegosaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<SpinoraptorEntity>> SPINORAPTOR_ENTITY_TYPE = MOD_ENTITY_TYPES.register("spinoraptor", () -> EntityType.Builder.<SpinoraptorEntity>of((type, world) -> new SpinoraptorEntity(world, type), MobCategory.CREATURE).build("spinoraptor"));
    public static DeferredHolder<EntityType<?>, EntityType<AchillobatorEntity>> ACHILLOBATOR_ENTITY_TYPE = MOD_ENTITY_TYPES.register("achillobator", () -> EntityType.Builder.<AchillobatorEntity>of((type, world) -> new AchillobatorEntity(world, type), MobCategory.CREATURE).build("achillobator"));
    public static DeferredHolder<EntityType<?>, EntityType<ChilesaurusEntity>> CHILESAURUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("chilesaurus", () -> EntityType.Builder.<ChilesaurusEntity>of((type, world) -> new ChilesaurusEntity(world, type), MobCategory.CREATURE).build("chilesaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<MegatheriumEntity>> MEGATHERIUM_ENTITY_TYPE = MOD_ENTITY_TYPES.register("megatherium", () -> EntityType.Builder.<MegatheriumEntity>of((type, world) -> new MegatheriumEntity(world, type), MobCategory.CREATURE).build("megatherium"));
    public static DeferredHolder<EntityType<?>, EntityType<SegisaurusEntity>> SEGISAURUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("segisaurus", () -> EntityType.Builder.<SegisaurusEntity>of((type, world) -> new SegisaurusEntity(world, type), MobCategory.CREATURE).build("segisaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<AnkylodocusEntity>> ANKYLODOCUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("ankylodocus", () -> EntityType.Builder.<AnkylodocusEntity>of((type, world) -> new AnkylodocusEntity(world, type), MobCategory.CREATURE).build("ankylodocus"));
    public static DeferredHolder<EntityType<?>, EntityType<BrachiosaurusEntity>> BRACHIOSAURUS = MOD_ENTITY_TYPES.register("brachiosaurus", () -> EntityType.Builder.of(BrachiosaurusEntity::new, MobCategory.CREATURE).build("brachiosaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<SmilodonEntity>> SMILODON_ENTITY_TYPE = MOD_ENTITY_TYPES.register("smilodon", () -> EntityType.Builder.<SmilodonEntity>of((type, world) -> new SmilodonEntity(world, type), MobCategory.CREATURE).build("smilodon"));
    public static DeferredHolder<EntityType<?>, EntityType<MicroceratusEntity>> MICROCERATUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("microceratus", () -> EntityType.Builder.<MicroceratusEntity>of((type, world) -> new MicroceratusEntity(world, type), MobCategory.CREATURE).build("microceratus"));
    public static DeferredHolder<EntityType<?>, EntityType<LeptictidiumEntity>> LEPTICTIDIUM_ENTITY_TYPE = MOD_ENTITY_TYPES.register("leptictidium", () -> EntityType.Builder.<LeptictidiumEntity>of((type, world) -> new LeptictidiumEntity(world, type), MobCategory.CREATURE).build("leptictidium"));
    public static DeferredHolder<EntityType<?>, EntityType<HypsilophodonEntity>> HYPSILOPHODON = MOD_ENTITY_TYPES.register("hypsilophodon", () -> EntityType.Builder.of(HypsilophodonEntity::new, MobCategory.CREATURE).build("hypsilophodon"));
    public static DeferredHolder<EntityType<?>, EntityType<TherizinosaurusEntity>> THERIZINOSAURUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("therizinosaurus", () -> EntityType.Builder.of(TherizinosaurusEntity::new, MobCategory.CREATURE).build("therizinosaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<VelociraptorEntity>> VELOCIRAPTOR_ENTITY_TYPE = MOD_ENTITY_TYPES.register("velociraptor", () -> EntityType.Builder.<VelociraptorEntity>of((type, world) -> new VelociraptorEntity(world, type), MobCategory.CREATURE).build("velociraptor"));
    public static DeferredHolder<EntityType<?>, EntityType<MussaurusEntity>> MUSSAURUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("mussaurus", () -> EntityType.Builder.<MussaurusEntity>of((type, world) -> new MussaurusEntity(world, type), MobCategory.CREATURE).build("mussaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<TriceratopsEntity>> TRICERATOPS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("triceratops", () -> EntityType.Builder.<TriceratopsEntity>of((type, world) -> new TriceratopsEntity(world, type), MobCategory.CREATURE).build("triceratops"));
    public static DeferredHolder<EntityType<?>, EntityType<GiganotosaurusEntity>> GIGANOTOSAURUS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("giganotosaurus", () -> EntityType.Builder.of(GiganotosaurusEntity::new, MobCategory.CREATURE).build("giganotosaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<TropeognathusEntity>> TROPEOGNATHUS = MOD_ENTITY_TYPES.register("tropeognathus", () -> EntityType.Builder.of(TropeognathusEntity::new, MobCategory.CREATURE).build("tropeognathus"));
    public static DeferredHolder<EntityType<?>, EntityType<LambeosaurusEntity>> LAMBEOSAURUS = MOD_ENTITY_TYPES.register("lambeosaurus", () -> EntityType.Builder.of(LambeosaurusEntity::new, MobCategory.CREATURE).build("lambeosaurus"));
    public static DeferredHolder<EntityType<?>, EntityType<AlligatorGarEntity>> ALLIGATOR_GAR = MOD_ENTITY_TYPES.register("alligatorgar", () -> EntityType.Builder.of(AlligatorGarEntity::new, MobCategory.CREATURE).build("alligatorgar"));
    public static DeferredHolder<EntityType<?>, EntityType<ElasmotheriumEntity>> ELASMOTHERIUM = MOD_ENTITY_TYPES.register("elasmotherium", () -> EntityType.Builder.of(ElasmotheriumEntity::new, MobCategory.CREATURE).build("elasmotherium"));
    public static DeferredHolder<EntityType<?>, EntityType<KairukuEntity>> KAIRUKU_ENTITY_TYPE = MOD_ENTITY_TYPES.register("kairuku", () -> EntityType.Builder.<KairukuEntity>of((type, world) -> new KairukuEntity(world, type), MobCategory.CREATURE).build("kairuku"));
    public static DeferredHolder<EntityType<?>, EntityType<DeinosuchusEntity>> DEINOSUCHUS = MOD_ENTITY_TYPES.register("deinosuchus", () -> EntityType.Builder.of(DeinosuchusEntity::new, MobCategory.CREATURE).build("deinosuchus"));

    public static DeferredHolder<EntityType<?>, EntityType<DimetrodonEntity>> DIMETRODON_ENTITY_TYPE = MOD_ENTITY_TYPES.register("dimetrodon", () -> EntityType.Builder.<DimetrodonEntity>of((type, world) -> new DimetrodonEntity(world, type), MobCategory.CREATURE).build("dimetrodon"));

    public static DeferredHolder<EntityType<?>, EntityType<AsterocerasEntity>> ASTEROCERAS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("asteroceras", () -> EntityType.Builder.<AsterocerasEntity>of((type, world) -> new AsterocerasEntity(world, type), MobCategory.CREATURE).build("asteroceras"));

    public static DeferredHolder<EntityType<?>, EntityType<TitanitesEntity>> TITANITES_ENTITY_TYPE = MOD_ENTITY_TYPES.register("titanites", () -> EntityType.Builder.<TitanitesEntity>of((type, world) -> new TitanitesEntity(world, type), MobCategory.CREATURE).build("titanites"));

    public static DeferredHolder<EntityType<?>, EntityType<ParapuzosiaEntity>> PARAPUZOSIA_ENTITY_TYPE = MOD_ENTITY_TYPES.register("parapuzosia", () -> EntityType.Builder.<ParapuzosiaEntity>of((type, world) -> new ParapuzosiaEntity(world, type), MobCategory.CREATURE).build("parapuzosia"));

    public static DeferredHolder<EntityType<?>, EntityType<VectipeltaEntity>> VECTIPELTA = MOD_ENTITY_TYPES.register("vectipelta", () -> EntityType.Builder.<VectipeltaEntity>of((type, world) -> new VectipeltaEntity(world, type), MobCategory.CREATURE).build("vectipelta"));

    public static DeferredHolder<EntityType<?>, EntityType<ParaceratheriumEntity>> PARACERATHERIUM = MOD_ENTITY_TYPES.register("paraceratherium", () -> EntityType.Builder.of(ParaceratheriumEntity::new, MobCategory.CREATURE).build("paraceratherium"));

    public static DeferredHolder<EntityType<?>, EntityType<CamerocerasEntity>> CAMEROCERAS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("cameroceras", () -> EntityType.Builder.<CamerocerasEntity>of((type, world) -> new CamerocerasEntity(world, type), MobCategory.CREATURE).build("cameroceras"));

    public static DeferredHolder<EntityType<?>, EntityType<EndocerasEntity>> ENDOCERAS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("endoceras", () -> EntityType.Builder.<EndocerasEntity>of((type, world) -> new EndocerasEntity(world, type), MobCategory.CREATURE).build("endoceras"));

    public static DeferredHolder<EntityType<?>, EntityType<OrthocerasEntity>> ORTHOCERAS_ENTITY_TYPE = MOD_ENTITY_TYPES.register("orthoceras", () -> EntityType.Builder.<OrthocerasEntity>of((type, world) -> new OrthocerasEntity(world, type), MobCategory.CREATURE).build("orthoceras"));




    public static void init(IEventBus modEventBus){
        MOD_ENTITY_TYPES.register(modEventBus);
    }

    public static Optional<EntityType<?>> getTypeForDinosaur(Dinosaur dinosaur) {
        ResourceLocation key = ResourceLocation.fromNamespaceAndPath(
                JurassicReborn.MODID,
                dinosaur.getName().toLowerCase(Locale.ROOT).replace(" ", "")
        );

        return BuiltInRegistries.ENTITY_TYPE.getOptional(key);
    }
}
