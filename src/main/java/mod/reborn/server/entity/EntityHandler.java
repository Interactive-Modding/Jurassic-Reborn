package mod.reborn.server.entity;

import mod.reborn.RebornMod;
import mod.reborn.server.RebornConfig;
import mod.reborn.server.dinosaur.AchillobatorDinosaur;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;

public class EntityHandler {
    //public static final DeferredRegister<EntityType<?>> REGISTERS = DeferredRegister.create(ForgeRegistries.ENTITIES, RebornMod.MOD_ID);
    private static final HashMap<TimePeriod, List<Dinosaur>> DINOSAUR_PERIODS = new HashMap<>();
    private static final Map<Integer, Dinosaur> DINOSAURS = new HashMap<>();
    private static Map<Dinosaur, Integer> DINOSAURS_ID() {
        return DINOSAURS.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    public static final Dinosaur ACHILLOBATOR = new AchillobatorDinosaur();

    //private static ProgressManager.ProgressBar dinosaurProgress;
    private static int highestID;

    public static void init() {
        registerDinosaur(6, ACHILLOBATOR);
    }

    private static void initDinosaurs() {
        for (Map.Entry<Integer, Dinosaur> entry : DINOSAURS.entrySet()) {
            Dinosaur dinosaur = entry.getValue();

            //dinosaurProgress.step(dinosaur.getName());

            //dinosaur.init();

            //boolean canSpawn = !(dinosaur instanceof Hybrid);
            boolean canSpawn = true;
            /*if (canSpawn) {
                TimePeriod period = dinosaur.getPeriod();
                List<Dinosaur> periods = DINOSAUR_PERIODS.get(period);
                if (periods == null) {
                    periods = new LinkedList<>();
                    DINOSAUR_PERIODS.put(period, periods);
                }
                periods.add(dinosaur);
            }*/

            Class<? extends DinosaurEntity> clazz = dinosaur.getDinosaurClass();

            registerEntity(clazz, dinosaur.getName());

            /*if (canSpawn && RebornConfig.ENTITIES_CONFIG.naturalSpawning) {
                if(dinosaur.getSpawnBiomes() != null)
                    EntityRegistry.addSpawn(clazz, dinosaur.getSpawnChance(), 1, Math.min(1, dinosaur.getMaxHerdSize()/2), dinosaur.isMarineCreature() ? EnumCreatureType.WATER_CREATURE : EnumCreatureType.CREATURE, dinosaur.getSpawnBiomes());
                if(dinosaur.isMarineCreature()) {
                    //EntitySpawnPlacementRegistry.setPlacementType(EntityShark.class, EntityLiving.SpawnPlacementType.IN_WATER);
                }
            }*/
        }
    }

    private static void registerEntity(Class<? extends DinosaurEntity> entity, String name) {
        String formattedName = name.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        ResourceLocation registryName = new ResourceLocation("rebornmod:entities." + formattedName);
        //EntityRegistry.registerModEntity(registryName, entity, "rebornmod." + formattedName, entityId++, RebornMod, 85, 1, true);
    }

    private static void registerEntity(Class<? extends DinosaurEntity> entity, String name, int primary, int secondary) {
        String formattedName = name.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        ResourceLocation registryName = new ResourceLocation("rebornmod:entities." + formattedName);
        //EntityRegistry.registerModEntity(registryName, entity, "rebornmod." + formattedName, entityId++, RebornMod.INSTANCE, 85, 1, true, primary, secondary);
    }

    public static void registerDinosaur(Dinosaur dinosaur) {
        registerDinosaur(DINOSAURS.size(), dinosaur);
    }

    public static void registerDinosaur(int id, Dinosaur dinosaur) {
        if (id > highestID) {
            highestID = id;
        }

        DINOSAURS.put(id, dinosaur);
        DINOSAURS_ID().put(dinosaur, id);
    }

    public static Dinosaur getDinosaurById(int id) {
        Dinosaur dinosaur = DINOSAURS.get(id);
        return dinosaur != null ? dinosaur : getDinosaurById(0);
    }

    public static int getDinosaurId(Dinosaur dinosaur) {
        return DINOSAURS_ID().get(dinosaur);
    }

    public static List<Dinosaur> getDinosaursFromAmber() {
        List<Dinosaur> dinosaurs = new LinkedList<>();
        for (Dinosaur dinosaur : getRegisteredDinosaurs()) {
            /*if (!dinosaur.isMarineCreature() && !(dinosaur instanceof Hybrid)) {
                dinosaurs.add(dinosaur);
            }*/
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
            /*if (!(dinosaur instanceof Hybrid)) {
                dinosaurs.add(dinosaur);
            }*/
        }
        return dinosaurs;
    }

    public static List<Dinosaur> getDinosaursFromPeriod(TimePeriod period) {
        return DINOSAUR_PERIODS.get(period);
    }

    public static Dinosaur getDinosaurByClass(Class<? extends DinosaurEntity> clazz) {
        for (Map.Entry<Integer, Dinosaur> entry : EntityHandler.DINOSAURS.entrySet()) {
            Dinosaur dinosaur = entry.getValue();

            if (dinosaur.getClass().equals(clazz)) {
                return dinosaur;
            }
        }

        return null;
    }

    public static int getHighestID() {
        return highestID;
    }

}
