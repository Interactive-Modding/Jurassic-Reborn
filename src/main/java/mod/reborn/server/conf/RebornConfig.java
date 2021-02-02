package mod.reborn.server.conf;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import mod.reborn.RebornMod;

@Config(modid = RebornMod.MODID, category = "")
@Mod.EventBusSubscriber(modid = RebornMod.MODID)
public class RebornConfig { //TODO: move all structures to same parent package

    @Config.Name("entities")
    public static final Entities ENTITIES = new Entities();

    @Config.Name("mineral Generation")
    public static final MineralGeneration MINERAL_GENERATION = new MineralGeneration();

    @Config.Name("plant Generation")
    public static final PlantGeneration PLANT_GENERATION = new PlantGeneration();


    @Config.Name("structure Generation")
    public static final StructureGeneration STRUCTURE_GENERATION = new StructureGeneration();

    @Config.Name("vehicles")
    public static final Vehicles VEHICLES = new Vehicles();


    public static class Entities {
        @Config.Name("Dinosaur Spawning")
        @Config.Comment("Requires 3+ Gigabytes of Ram")
        public boolean naturalSpawning = false;
        
        @Config.Name("Goat Spawning")
        @Config.Comment("Allow Goat Spawning")
        public boolean goatSpawning = true;
        
        @Config.Name("Shark Spawning")
        @Config.Comment("Allow Shark Spawning")
        public boolean sharkSpawning = true;
        
        @Config.Name("Crab Spawning")
        @Config.Comment("Allow Crab Spawning")
        public boolean crabSpawning = true;

        @Config.Name("Only Hunt when Hungry")
        public boolean huntWhenHungry = false;

        @Config.Name("Allow Carcass Spawning")
        public boolean allowCarcass = true;

        @Config.Name("Allow baby's to swim")
        public boolean canBabysSwim = true;

        @Config.Name("Allow Feathered Dinosaur Swimming")
        public boolean featheredDinosaurSwimming = false;

        @Config.Name("Dinosaur Walking Radius")
        @Config.Comment("This impacts performance a lot")
        public int dinosaurWalkingRadius = 10;
    }

    public static class MineralGeneration {
        @Config.Name("Fossil Generation")
        public boolean fossilGeneration = true;

        @Config.Name("Nest Fossil Generation")
        public boolean nestFossilGeneration = true;

        @Config.Name("Fossilized Trackway Generation")
        public boolean trackwayGeneration = true;

        @Config.Name("Plant Fossil Generation")
        public boolean plantFossilGeneration = true;

        @Config.Name("Amber Generation")
        public boolean amberGeneration = true;

        @Config.Name("Ice Shard Generation")
        public boolean iceShardGeneration = true;

        @Config.Name("Gypsum Generation")
        public boolean gypsumGeneration = true;

        @Config.Name("Petrified Tree Generation")
        public boolean petrifiedTreeGeneration = true;
    }

    public static class PlantGeneration {
        @Config.Name("Moss Generation")
        public boolean mossGeneration = true;

        @Config.Name("Peat Generation")
        public boolean peatGeneration = true;

        @Config.Name("Flower Generation")
        public boolean flowerGeneration = true;

        @Config.Name("Gracilaria Generation")
        public boolean gracilariaGeneration = true;

        @Config.Name("Peat Spread Speed")
        public int peatSpreadSpeed = 5;
    }

    public static class StructureGeneration {
        @Config.Name("Visitor Center Generation")
        public boolean visitorcentergeneration = true;

        @Config.Name("Raptor Paddock Generation")
        public boolean raptorgeneration = true;
    }

    public static class Vehicles {
        @Config.Name("Enable Tour Rail Blocks")
        public boolean tourRailBlockEnabled = true;
        @Config.Name("Helicopter Can Destroy Blocks")
        public boolean destroyBlocks = false;
        @Config.Name("Helicopter Zoomout")
        @Config.Comment("NOT WORKING AT THE MOMENT, BREAKS OPTIFINE")
        public boolean helicopterZoomout = false;
    }
    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if(RebornMod.MODID.equals(event.getModID())) {
            ConfigManager.sync(RebornMod.MODID, Config.Type.INSTANCE);
        }
    }
}
