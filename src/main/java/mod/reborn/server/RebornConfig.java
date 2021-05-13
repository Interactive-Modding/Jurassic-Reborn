package mod.reborn.server;

import net.minecraftforge.common.ForgeConfigSpec;

public class RebornConfig {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final EntitiesConfig ENTITIES_CONFIG = new EntitiesConfig(BUILDER);
    public static final MineralConfig MINERAL_CONFIG = new MineralConfig(BUILDER);
    public static final PlantConfig PLANT_CONFIG = new PlantConfig(BUILDER);
    public static final StructureConfig STRUCTURE_CONFIG = new StructureConfig(BUILDER);
    public static final VehicleConfig VEHICLE_CONFIG = new VehicleConfig(BUILDER);
    public static final ForgeConfigSpec spec = BUILDER.build();

    public static class EntitiesConfig {
        public final ForgeConfigSpec.ConfigValue<Boolean> naturalSpawning;
        public final ForgeConfigSpec.ConfigValue<Boolean> goatSpawning;
        public final ForgeConfigSpec.ConfigValue<Boolean> sharkSpawning;
        public final ForgeConfigSpec.ConfigValue<Boolean> crabSpawning;
        public final ForgeConfigSpec.ConfigValue<Boolean> huntWhenHungry;
        public final ForgeConfigSpec.ConfigValue<Boolean> allowCarcass;
        public final ForgeConfigSpec.ConfigValue<Boolean> canBabiesSwim;
        public final ForgeConfigSpec.ConfigValue<Boolean> featheredDinosaurSwimming;
        public final ForgeConfigSpec.ConfigValue<Integer> dinosaurWalkingRadius ;

        public EntitiesConfig(ForgeConfigSpec.Builder builder) {
            builder.push("Entities");
            naturalSpawning = builder.comment("Dinosaur Spawning - Requires 3+ Gigabytes of Ram").define("naturalSpawning", false);
            goatSpawning = builder.comment("Goat Spawning - Allow Goat Spawning").define("goatSpawning", true);
            sharkSpawning = builder.comment("Shark Spawning - Allow Shark Spawning").define("sharkSpawning", true);
            crabSpawning = builder.comment("Crab Spawning - Allow Crab Spawning").define("crabSpawning", true);
            huntWhenHungry = builder.comment("Only Hunt when Hungry").define("huntWhenHungry", false);
            allowCarcass = builder.comment("Allow Carcass Spawning").define("allowCarcass", true);
            canBabiesSwim = builder.comment("Allow baby's to swim").define("canBabiesSwim", true);
            featheredDinosaurSwimming = builder.comment("Allow Feathered Dinosaur Swimming").define("featheredDinosaurSwimming", false);
            dinosaurWalkingRadius = builder.comment("Dinosaur Walking Radius - This impacts performance a lot").define("dinosaurWalkingRadius", 10);
            builder.pop();
        }
    }

    public static class MineralConfig {
        public final ForgeConfigSpec.ConfigValue<Boolean> fossilGeneration;
        public final ForgeConfigSpec.ConfigValue<Boolean> nestFossilGeneration;
        public final ForgeConfigSpec.ConfigValue<Boolean> trackwayGeneration;
        public final ForgeConfigSpec.ConfigValue<Boolean> plantFossilGeneration;
        public final ForgeConfigSpec.ConfigValue<Boolean> amberGeneration;
        public final ForgeConfigSpec.ConfigValue<Boolean> iceShardGeneration;
        public final ForgeConfigSpec.ConfigValue<Boolean> gypsumGeneration;
        public final ForgeConfigSpec.ConfigValue<Boolean> petrifiedTreeGeneration;

        public MineralConfig(ForgeConfigSpec.Builder builder) {
            builder.push("MineralGeneration");
            fossilGeneration = builder.comment("Fossil Generation").define("fossilGeneration", true);
            nestFossilGeneration = builder.comment("Nest Fossil Generation").define("nestFossilGeneration", true);
            trackwayGeneration = builder.comment("Fossilized Trackway Generation").define("trackwayGeneration", true);
            plantFossilGeneration = builder.comment("Plant Fossil Generation").define("plantFossilGeneration", true);
            amberGeneration = builder.comment("Amber Generation").define("amberGeneration", true);
            iceShardGeneration = builder.comment("Ice Shard Generation").define("iceShardGeneration", true);
            gypsumGeneration = builder.comment("Gypsum Generation").define("gypsumGeneration", true);
            petrifiedTreeGeneration = builder.comment("Petrified Tree Generation").define("petrifiedTreeGeneration", true);
            builder.pop();
        }
    }

    public static class PlantConfig {
        public final ForgeConfigSpec.ConfigValue<Boolean> mossGeneration;
        public final ForgeConfigSpec.ConfigValue<Boolean> peatGeneration;
        public final ForgeConfigSpec.ConfigValue<Boolean> flowerGeneration;
        public final ForgeConfigSpec.ConfigValue<Boolean> gracilariaGeneration;
        public final ForgeConfigSpec.ConfigValue<Integer> peatSpreadSpeed;

        public PlantConfig(ForgeConfigSpec.Builder builder) {
            builder.push("PlantGeneration");
            mossGeneration = builder.comment("Moss Generation").define("mossGeneration", true);
            peatGeneration = builder.comment("Peat Generation").define("peatGeneration", true);
            flowerGeneration = builder.comment("Flower Generation").define("flowerGeneration", true);
            gracilariaGeneration = builder.comment("Gracilaria Generation").define("gracilariaGeneration", true);
            peatSpreadSpeed = builder.comment("Peat Spread Speed").define("peatSpreadSpeed", 5);
            builder.pop();
        }
    }

    public static class StructureConfig {
        public final ForgeConfigSpec.ConfigValue<Boolean> visitorcentergeneration;
        public final ForgeConfigSpec.ConfigValue<Boolean> raptorgeneration;

        public StructureConfig(ForgeConfigSpec.Builder builder) {
            builder.push("StructureGeneration");
            visitorcentergeneration = builder.comment("Visitor Center Generation").define("visitorcentergeneration", true);
            raptorgeneration = builder.comment("Raptor Paddock Generation").define("raptorgeneration", true);
            builder.pop();
        }
    }

    public static class VehicleConfig {
        public final ForgeConfigSpec.ConfigValue<Boolean> tourRailBlockEnabled;
        public final ForgeConfigSpec.ConfigValue<Boolean> destroyBlocks;
        public final ForgeConfigSpec.ConfigValue<Boolean> helicopterZoomout;

        public VehicleConfig(ForgeConfigSpec.Builder builder) {
            builder.push("Vehicles");
            tourRailBlockEnabled = builder.comment("Enable Tour Rail Blocks").define("tourRailBlockEnabled", true);
            destroyBlocks = builder.comment("Helicopter Can Destroy Blocks").define("destroyBlocks", false);
            helicopterZoomout = builder.comment("Helicopter Zoomout - NOT WORKING AT THE MOMENT, BREAKS OPTIFINE").define("helicopterZoomout", false);
            builder.pop();
        }
    }
}
