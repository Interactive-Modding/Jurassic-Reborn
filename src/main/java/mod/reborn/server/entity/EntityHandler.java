package mod.reborn.server.entity;

import mod.reborn.server.api.Hybrid;
import mod.reborn.server.conf.RebornConfig;
import mod.reborn.server.dinosaur.*;
import mod.reborn.server.entity.animal.EntityCrab;
import mod.reborn.server.entity.animal.EntityShark;
import mod.reborn.server.entity.animal.GoatEntity;
import mod.reborn.server.entity.dinosaur.ArsinoitheriumEntity;
import mod.reborn.server.entity.item.*;
import mod.reborn.server.entity.vehicle.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import mod.reborn.RebornMod;
import mod.reborn.server.period.TimePeriod;

import java.util.*;

public class EntityHandler {
    public static final Dinosaur BRACHIOSAURUS = new BrachiosaurusDinosaur();
    public static final Dinosaur DODO = new DodoDinosaur();
    public static final Dinosaur ACHILLOBATOR = new AchillobatorDinosaur();
    public static final Dinosaur ANKYLOSAURUS = new AnkylosaurusDinosaur();
    public static final Dinosaur CARNOTAURUS = new CarnotaurusDinosaur();
    public static final Dinosaur COELACANTH = new CoelacanthDinosaur();
    public static final Dinosaur COMPSOGNATHUS = new CompsognathusDinosaur();
    public static final Dinosaur DUNKLEOSTEUS = new DunkleosteusDinosaur();
    public static final Dinosaur GIGANOTOSAURUS = new GiganotosaurusDinosaur();
    public static final Dinosaur HYPSILOPHODON = new HypsilophodonDinosaur();
    public static final Dinosaur INDOMINUS = new IndominusDinosaur();
    public static final Dinosaur MAJUNGASAURUS = new MajungasaurusDinosaur();
    public static final Dinosaur PTERANODON = new PteranodonDinosaur();
    public static final Dinosaur RUGOPS = new RugopsDinosaur();
    public static final Dinosaur SEGISAURUS = new SegisaurusDinosaur();
    public static final Dinosaur SPINOSAURUS = new SpinosaurusDinosaur();
    public static final Dinosaur LEPTICTIDIUM = new LeptictidiumDinosaur();
    public static final Dinosaur MICROCERATUS = new MicroceratusDinosaur();
    public static final Dinosaur APATOSAURUS = new ApatosaurusDinosaur();
    public static final Dinosaur OTHNIELIA = new OthnieliaDinosaur();
    public static final Dinosaur DIMORPHODON = new DimorphodonDinosaur();
    public static final Dinosaur TYLOSAURUS = new TylosaurusDinosaur();
    public static final Dinosaur LUDODACTYLUS = new LudodactylusDinosaur();
    public static final Dinosaur PROTOCERATOPS = new ProtoceratopsDinosaur();
    public static final Dinosaur TROPEOGNATHUS = new TropeognathusDinosaur();
    public static final Dinosaur LEAELLYNASAURA = new LeaellynasauraDinosaur();
    public static final Dinosaur HERRERASAURUS = new HerrerasaurusDinosaur();
    public static final Dinosaur BLUE = new VelociraptorSquad.VelociraptorBlueDinosaur();
    public static final Dinosaur DELTA = new VelociraptorSquad.VelociraptorDeltaDinosaur();
    public static final Dinosaur CHARLIE = new VelociraptorSquad.VelociraptorCharlieDinosaur();
    public static final Dinosaur ECHO = new VelociraptorSquad.VelociraptorEchoDinosaur();
    public static final Dinosaur THERIZINOSAURUS = new TherizinosaurusDinosaur();
    public static final Dinosaur MEGAPIRANHA = new MegapiranhaDinosaur();
    public static final Dinosaur BARYONYX = new BaryonyxDinosaur();
    public static final Dinosaur CEARADACTYLUS = new CearadactylusDinosaur();
    public static final Dinosaur MAMENCHISAURUS = new MamenchisaurusDinosaur();
    public static final Dinosaur CHASMOSAURUS = new ChasmosaurusDinosaur();
    public static final Dinosaur CORYTHOSAURUS = new CorythosaurusDinosaur();
    public static final Dinosaur EDMONTOSAURUS = new EdmontosaurusDinosaur();
    public static final Dinosaur LAMBEOSAURUS = new LambeosaurusDinosaur();
    public static final Dinosaur METRIACANTHOSAURUS = new MetriacanthosaurusDinosaur();
    public static final Dinosaur MOGANOPTERUS = new MoganopterusDinosaur();
    public static final Dinosaur ORNITHOMIMUS = new OrnithomimusDinosaur();
    public static final Dinosaur ZHENYUANOPTERUS = new ZhenyuanopterusDinosaur();
    public static final Dinosaur TROODON = new TroodonDinosaur();
    public static final Dinosaur PACHYCEPHALOSAURUS = new PachycephalosaurusDinosaur();
    public static final Dinosaur DILOPHOSAURUS = new DilophosaurusDinosaur();
    public static final Dinosaur GALLIMIMUS = new GallimimusDinosaur();
    public static final Dinosaur PARASAUROLOPHUS = new ParasaurolophusDinosaur();
    public static final Dinosaur MICRORAPTOR = new MicroraptorDinosaur();
    public static final Dinosaur MUSSAURUS = new MussaurusDinosaur();
    public static final Dinosaur TRICERATOPS = new TriceratopsDinosaur();
    public static final Dinosaur TYRANNOSAURUS = new TyrannosaurusDinosaur();
    public static final Dinosaur VELOCIRAPTOR = new VelociraptorDinosaur();
    public static final Dinosaur ALLIGATORGAR = new AlligatorGarDinosaur();
    public static final Dinosaur STEGOSAURUS = new StegosaurusDinosaur();
    public static final Dinosaur OVIRAPTOR = new OviraptorDinosaur();
    public static final Dinosaur MOSASAURUS = new MosasaurusDinosaur();
    public static final Dinosaur ALVAREZSAURUS = new AlvarezsaurusDinosaur();
    public static final Dinosaur BEELZEBUFO = new BeelzebufoDinosaur();
    public static final Dinosaur CERATOSAURUS = new CeratosaurusDinosaur();
    public static final Dinosaur PROCERATOSAURUS = new ProceratosaurusDinosaur();
    public static final Dinosaur CARCHARODONTOSAURUS = new CarcharodontosaurusDinosaur();
    public static final Dinosaur CHILESAURUS = new ChilesaurusDinosaur();
    public static final Dinosaur CRASSIGYRINUS = new CrassigyrinusDinosaur();
    public static final Dinosaur DIPLOCAULUS = new DiplocaulusDinosaur();
    public static final Dinosaur GUANLONG = new GuanlongDinosaur();
    public static final Dinosaur HYAENODON = new HyaenodonDinosaur();
    public static final Dinosaur AMMONITE = new AmmoniteDinosaur();
    public static final Dinosaur POSTOSUCHUS = new PostosuchusDinosaur();
    public static final Dinosaur STYRACOSAURUS = new StyracosaurusDinosaur();
    public static final Dinosaur SUCHOMIMUS = new SuchomimusDinosaur();
    public static final Dinosaur ALLOSAURUS = new AllosaurusDinosaur();
    public static final Dinosaur MAMMOTH = new MammothDinosaur();
    public static final Dinosaur QUETZAL = new QuetzalDinosaur();
    public static final Dinosaur COELURUS = new CoelurusDinosaur();
    public static final Dinosaur MAWSONIA = new MawsoniaDinosaur();
    public static final Dinosaur INDORAPTOR = new IndoraptorDinosaur();
    public static final Dinosaur DREADNOUGHTUS = new DreadnoughtusDinosaur();
    public static final Dinosaur SINOCERATOPS = new SinoceratopsDinosaur();
    public static final Dinosaur ARSINOITHERIUM = new ArsinoitheriumDinosaur();

    private static final Map<Integer, Dinosaur> DINOSAURS = new HashMap<>();
    private static final Map<Dinosaur, Integer> DINOSAUR_IDS = new HashMap<>();
    private static final HashMap<TimePeriod, List<Dinosaur>> DINOSAUR_PERIODS = new HashMap<>();

    private static int entityId;
    private static int dinoId;

    private static ProgressManager.ProgressBar dinosaurProgress;
    private static int highestID;

    public static List<Dinosaur> getMarineCreatures() {
        List<Dinosaur> marineDinosaurs = new ArrayList<>();
        for (Dinosaur dino : getRegisteredDinosaurs()) {
            if (dino.isMarineCreature() && !(dino instanceof Hybrid)) {
                marineDinosaurs.add(dino);
            }
        }
        return marineDinosaurs;
    }

    public static void init() {
        registerDinosaur(0,VELOCIRAPTOR);
        registerDinosaur(2,COELACANTH);
        registerDinosaur(3,MICRORAPTOR);
        registerDinosaur(4,BRACHIOSAURUS);
        registerDinosaur(5,MUSSAURUS);
        registerDinosaur(6,ACHILLOBATOR);
        registerDinosaur(7,ANKYLOSAURUS);
        registerDinosaur(8,DILOPHOSAURUS);
        registerDinosaur(9,COMPSOGNATHUS);
        registerDinosaur(10,GALLIMIMUS);
        registerDinosaur(11,CARNOTAURUS);
        registerDinosaur(12,DUNKLEOSTEUS);
        registerDinosaur(13,GIGANOTOSAURUS);
        registerDinosaur(14,PARASAUROLOPHUS);
        registerDinosaur(15,INDOMINUS);
        registerDinosaur(16,MAJUNGASAURUS);
        registerDinosaur(17,PTERANODON);
        registerDinosaur(18,RUGOPS);
        registerDinosaur(19,SEGISAURUS);
        registerDinosaur(20,TRICERATOPS);
        registerDinosaur(21,TYRANNOSAURUS);
        registerDinosaur(22,ALLIGATORGAR);
        registerDinosaur(23,STEGOSAURUS);
        registerDinosaur(24,SPINOSAURUS);
        registerDinosaur(25,HYPSILOPHODON);
        registerDinosaur(26,DODO);
        registerDinosaur(27,LEPTICTIDIUM);
        registerDinosaur(28,MICROCERATUS);
        registerDinosaur(29,APATOSAURUS);
        registerDinosaur(30,OTHNIELIA);
        registerDinosaur(31,DIMORPHODON);
        registerDinosaur(32,TYLOSAURUS);
        registerDinosaur(33,LUDODACTYLUS);
        registerDinosaur(34,PROTOCERATOPS);
        registerDinosaur(35,TROPEOGNATHUS);
        registerDinosaur(36,LEAELLYNASAURA);
        registerDinosaur(37,HERRERASAURUS);
        registerDinosaur(38,BLUE);
        registerDinosaur(39,CHARLIE);
        registerDinosaur(40,DELTA);
        registerDinosaur(41,ECHO);
        registerDinosaur(42,THERIZINOSAURUS);
        registerDinosaur(43,MEGAPIRANHA);
        registerDinosaur(44,BARYONYX);
        registerDinosaur(45,CEARADACTYLUS);
        registerDinosaur(46,MAMENCHISAURUS);
        registerDinosaur(47,CHASMOSAURUS);
        registerDinosaur(48,CORYTHOSAURUS);
        registerDinosaur(49,EDMONTOSAURUS);
        registerDinosaur(50,LAMBEOSAURUS);
        registerDinosaur(51,METRIACANTHOSAURUS);
        registerDinosaur(52,MOGANOPTERUS);
        registerDinosaur(53,ORNITHOMIMUS);
        registerDinosaur(54,ZHENYUANOPTERUS);
        registerDinosaur(55,TROODON);
        registerDinosaur(56,PACHYCEPHALOSAURUS);
        registerDinosaur(57,OVIRAPTOR);
        registerDinosaur(58,MOSASAURUS);
        registerDinosaur(59,ALVAREZSAURUS);
        registerDinosaur(60,BEELZEBUFO);
        registerDinosaur(61,CERATOSAURUS);
        registerDinosaur(62,PROCERATOSAURUS);
        registerDinosaur(63,CARCHARODONTOSAURUS);
        registerDinosaur(64,CHILESAURUS);
        registerDinosaur(65,CRASSIGYRINUS);
        registerDinosaur(66,DIPLOCAULUS);
        registerDinosaur(67,GUANLONG);
        registerDinosaur(68,HYAENODON);
        registerDinosaur(69,AMMONITE);
        registerDinosaur(70,POSTOSUCHUS);
        registerDinosaur(71,STYRACOSAURUS);
        registerDinosaur(72,SUCHOMIMUS);
        registerDinosaur(73,ALLOSAURUS);
        registerDinosaur(74,MAMMOTH);
        registerDinosaur(75,QUETZAL);
        registerDinosaur(76,COELURUS);
        registerDinosaur(77,MAWSONIA);
        registerDinosaur(78,INDORAPTOR);
        registerDinosaur(79,DREADNOUGHTUS);
        registerDinosaur(80,SINOCERATOPS);
        registerDinosaur(81,ARSINOITHERIUM);

        dinosaurProgress = ProgressManager.push("Loading dinosaurs", DINOSAURS.size());

        initDinosaurs();

        ProgressManager.pop(dinosaurProgress);

        registerEntity(AttractionSignEntity.class, "Attraction Sign");
        registerEntity(PaddockSignEntity.class, "Paddock Sign");
        registerEntity(MuralEntity.class, "Mural");
        registerEntity(VenomEntity.class, "Venom");

        registerEntity(JeepWranglerEntity.class, "Jeep Wrangler");
        registerEntity(FordExplorerEntity.class, "Ford Explorer");
        registerEntity(FordExplorerSnowEntity.class, "Ford Explorer Snow");

        registerEntity(GoatEntity.class, "Goat", 0xEFEDE7, 0x7B3E20);
        registerEntity(EntityCrab.class, "Crab", 0xEFEDE7, 0x7B3E20);
        registerEntity(EntityShark.class, "Shark", 0xEFEDE7, 0x7B3E20);

        registerEntity(TranquilizerDartEntity.class, "Tranquilizer Dart");
        registerEntity(BulletEntity.class, "bullet");

        ArrayList<Biome> biomeBeach = new ArrayList<>(BiomeDictionary.getBiomes(BiomeDictionary.Type.BEACH));

        EntityRegistry.addSpawn(EntityCrab.class, 20, 4, 8, EnumCreatureType.CREATURE, biomeBeach.toArray(new Biome[biomeBeach.size()]));
    //EntityRegistry.addSpawn(EntityShark.class, 3, 1, 4, EnumCreatureType.WATER_CREATURE, Biomes.OCEAN);
        EntitySpawnPlacementRegistry.setPlacementType(EntityShark.class, EntityLiving.SpawnPlacementType.IN_WATER);

        registerEntity(DinosaurEggEntity.class, "Dinosaur Egg");
        registerEntity(HelicopterEntityNew.class, "Helicopter base");
    }

    private static void initDinosaurs() {
        for (Map.Entry<Integer, Dinosaur> entry : DINOSAURS.entrySet()) {
            Dinosaur dinosaur = entry.getValue();

            dinosaurProgress.step(dinosaur.getName());

            dinosaur.init();

            boolean canSpawn = !(dinosaur instanceof Hybrid);

            if (canSpawn) {
                TimePeriod period = dinosaur.getPeriod();
                List<Dinosaur> periods = DINOSAUR_PERIODS.get(period);
                if (periods == null) {
                    periods = new LinkedList<>();
                    DINOSAUR_PERIODS.put(period, periods);
                }
                periods.add(dinosaur);
            }

            Class<? extends DinosaurEntity> clazz = dinosaur.getDinosaurClass();

            registerEntity(clazz, dinosaur.getName());

            if (canSpawn && RebornConfig.ENTITIES.naturalSpawning) {
                if(dinosaur.getSpawnBiomes() != null)
                EntityRegistry.addSpawn(clazz, dinosaur.getSpawnChance(), 1, Math.min(6, dinosaur.getMaxHerdSize() / 2), dinosaur.isMarineCreature() ? EnumCreatureType.WATER_CREATURE : EnumCreatureType.CREATURE, dinosaur.getSpawnBiomes());
                if(dinosaur.isMarineCreature()) EntitySpawnPlacementRegistry.setPlacementType(EntityShark.class, EntityLiving.SpawnPlacementType.IN_WATER);
            }
        }
    }
    
    private static void registerEntity(Class<? extends Entity> entity, String name) {
        String formattedName = name.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        ResourceLocation registryName = new ResourceLocation("rebornmod:entities." + formattedName);
        EntityRegistry.registerModEntity(registryName, entity, "rebornmod." + formattedName, entityId++, RebornMod.INSTANCE, 85, 1, true);
    }
    
    

    private static void registerEntity(Class<? extends Entity> entity, String name, int primary, int secondary) {
        String formattedName = name.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        ResourceLocation registryName = new ResourceLocation("rebornmod:entities." + formattedName);
        EntityRegistry.registerModEntity(registryName, entity, "rebornmod." + formattedName, entityId++, RebornMod.INSTANCE, 85, 1, true, primary, secondary);
    }

    public static void registerDinosaur(Dinosaur dinosaur) {
        registerDinosaur(dinoId, dinosaur);
        dinoId++;
    }

    public static void registerDinosaur(int id, Dinosaur dinosaur) {
        if (id > highestID) {
            highestID = id;
        }

        DINOSAURS.put(id, dinosaur);
        DINOSAUR_IDS.put(dinosaur, id);
    }

    public static Dinosaur getDinosaurById(int id) {
        Dinosaur dinosaur = DINOSAURS.get(id);
        return dinosaur != null ? dinosaur : getDinosaurById(0);
    }

    public static int getDinosaurId(Dinosaur dinosaur) {
        return DINOSAUR_IDS.get(dinosaur);
    }

    public static List<Dinosaur> getDinosaursFromAmber() {
        List<Dinosaur> dinosaurs = new LinkedList<>();
        for (Dinosaur dinosaur : getRegisteredDinosaurs()) {
            if (!dinosaur.isMarineCreature() && !(dinosaur instanceof Hybrid)) {
                dinosaurs.add(dinosaur);
            }
        }
        return dinosaurs;
    }

    public static Map<Integer, Dinosaur> getDinosaurs() {
        return DINOSAURS;
    }

    public static List<Dinosaur> getRegisteredDinosaurs() {
        List<Dinosaur> dinosaurs = new LinkedList<>();

        for (Map.Entry<Integer, Dinosaur> entry : EntityHandler.DINOSAURS.entrySet()) {
            Dinosaur dinosaur = entry.getValue();

            dinosaurs.add(dinosaur);
        }

        return dinosaurs;
    }

    public static List<Dinosaur> getPrehistoricDinosaurs() {
        List<Dinosaur> dinosaurs = new LinkedList<>();
        for (Map.Entry<Integer, Dinosaur> entry : EntityHandler.DINOSAURS.entrySet()) {
            Dinosaur dinosaur = entry.getValue();
            if (!(dinosaur instanceof Hybrid)) {
                dinosaurs.add(dinosaur);
            }
        }
        return dinosaurs;
    }

    public static List<Dinosaur> getDinosaursFromPeriod(TimePeriod period) {
        return DINOSAUR_PERIODS.get(period);
    }

    public static Dinosaur getDinosaurByClass(Class<? extends DinosaurEntity> clazz) {
        for (Map.Entry<Integer, Dinosaur> entry : EntityHandler.DINOSAURS.entrySet()) {
            Dinosaur dinosaur = entry.getValue();

            if (dinosaur.getDinosaurClass().equals(clazz)) {
                return dinosaur;
            }
        }

        return null;
    }

    public static int getHighestID() {
        return highestID;
    }
}
