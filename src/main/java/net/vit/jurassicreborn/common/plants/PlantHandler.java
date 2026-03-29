package net.vit.jurassicreborn.common.plants;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import net.minecraft.world.level.block.Blocks;
import net.vit.jurassicreborn.JurassicReborn;
import net.minecraft.resources.ResourceLocation;
import net.vit.jurassicreborn.common.blocks.ModBlocks;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class PlantHandler {
    public static final Plant AJUGINUCULA_SMITHII = new AjuginuculaSmithiiPlant("Ajuginucula Smithii", ModBlocks.AJUGINUCULA_SMITHII,2000);
    public static final Plant SMALL_ROYAL_FERN = new Plant("Small Royal Fern",ModBlocks.SMALL_ROYAL_FERN,2000);
    public static final Plant CALAMITES = new CalamitesPlant("Calamites",ModBlocks.CALAMITES_SAPLING,1000);
    public static final Plant SMALL_CHAIN_FERN = new Plant("Small Chain Fern",ModBlocks.SMALL_CHAIN_FERN,2000);
    public static final Plant SMALL_CYCAD = new SmallCycadPlant("Small Cycad",ModBlocks.SMALL_CYCAD,2000);
    public static final Plant GINKGO = new Plant("Ginkgo",ModBlocks.GINKGO_SAPLING,1000);
    public static final Plant MAGNOLIA = new Plant("Magnolia",ModBlocks.MAGNOLIA_SAPLING,1000);
    public static final Plant CYCADEOIDEA = new Plant("Bennettitalean Cycadeoidea",ModBlocks.CYCADEOIDEA,2000);
    public static final Plant CRY_PANSY = new Plant("Cry Pansy",ModBlocks.CRY_PANSY,250);
    public static final Plant SCALY_TREE_FERN = new ScalyTreeFernPlant("Scaly Tree Fern",ModBlocks.SCALY_TREE_FERN,4000);
    public static final Plant ZAMITES = new Plant("Cycad Zamites",ModBlocks.ZAMITES,4000);
    public static final Plant DICKSONIA = new Plant("Dicksonia",ModBlocks.DICKSONIA,4000);
    public static final Plant WILD_ONION = new Plant("Wild Onion",ModBlocks.WILD_ONION,3000);
    public static final Plant DICROIDIUM_ZUBERI = new Plant("Dicroidium Zuberi",ModBlocks.DICROIDIUM_ZUBERI,4000);
    public static final Plant DICTYOPHYLLUM = new DictyophyllumPlant("Dictyophyllum",ModBlocks.DICTYOPHYLLUM,2000);
    public static final Plant WEST_INDIAN_LILAC = new WestIndianLilacPlant("West Indian Lilac",ModBlocks.WEST_INDIAN_LILAC,4000);
    public static final Plant SERENNA_VERIFORMANS = new SerennaVeriformansPlant("Serenna Veriformans",ModBlocks.SERENNA_VERIFORMANS,4000);
    public static final Plant LADINIA_SIMPLEX = new LadiniaSimplexPlant("Ladinia Simplex",ModBlocks.LADINIA_SIMPLEX, 2000);
    public static final Plant ORONTIUM_MACKII = new OrontiumMackiiPlant("Orontium Mackii",ModBlocks.ORONTIUM_MACKII,1500);
    public static final Plant UMALTOLEPIS = new Plant("Umaltolepis",ModBlocks.UMALTOLEPIS,4000);
    public static final Plant LIRIODENDRITES = new Plant("Liriodendrites",ModBlocks.LIRIODENDRITES,4000);
    public static final Plant RAPHAELIA = new Plant("Raphaelia",ModBlocks.RAPHAELIA,2000);
    public static final Plant ENCEPHALARTOS = new EncephalartosPlant("Encephalartos",ModBlocks.ENCEPHALARTOS,4000);
    public static final Plant PSARONIUS = new Plant("Psaronius",ModBlocks.PSARONIUS_SAPLING,1000);

    public static final Plant PHOENIX = new Plant("Phoenix",ModBlocks.PHOENIX_SAPLING,1000);
    public static final Plant WILD_POTATO = new Plant("Wild Potato",ModBlocks.WILD_POTATO_PLANT,3000);
    public static final Plant ARAUCARIA = new Plant("Araucaria", ModBlocks.ARAUCARIA_SAPLING,1000);
    public static final Plant BRISTLE_FERN = new Plant("Bristle Fern",ModBlocks.BRISTLE_FERN,2000);
    public static final Plant CINNAMON_FERN = new Plant("Cinnamon Fern",ModBlocks.CINNAMON_FERN,2000);
    public static final Plant TEMPSKYA = new Plant("Tempskya",ModBlocks.TEMPSKYA,4000);
    public static final Plant WOOLLY_STALKED_BEGONIA = new Plant("Woolly Stalked Begonia",ModBlocks.WOOLLY_STALKED_BEGONIA,2000);
    public static final Plant LARGESTIPULE_LEATHER_ROOT = new Plant("Largestipule Leather Root",ModBlocks.LARGESTIPULE_LEATHER_ROOT,2000);
    public static final Plant RHACOPHYTON = new Plant("Rhacophyton",ModBlocks.RHACOPHYTON,4000);
    public static final Plant GRAMINIDITES_BAMBUSOIDES = new Plant("Graminidites Bambusoides",ModBlocks.GRAMINIDITES_BAMBUSOIDES,4000);
    public static final Plant ENALLHELIA = new Plant("Enallhelia",ModBlocks.ENALLHELIA,2000);
    public static final Plant AULOPORA = new Plant("Aulopora",ModBlocks.AULOPORA,2000);
    public static final Plant CLADOCHONUS = new Plant("Cladochonus",ModBlocks.CLADOCHONUS,2000);
    public static final Plant LITHOSTROTION = new Plant("Lithostrotion",ModBlocks.LITHOSTROTION,2000);
    public static final Plant STYLOPHYLLOPSIS = new Plant("Stylophyllopsis",ModBlocks.STYLOPHYLLOPSIS,2000);
    public static final Plant HIPPURITES_RADIOSUS = new Plant("Hippurites Radiosus",ModBlocks.HIPPURITES_RADIOSUS,2000);
    public static final Plant HELICONIA = new HeliconiaPlant("Heliconia",ModBlocks.HELICONIA,4000);
    public static final Plant RHAMNUS_SALICIFOLIUS = new Plant("Rhamnus Salicifolius",ModBlocks.RHAMNUS_SALICIFOLIUS,3000);

    public static final Plant EMPTY = new Plant("None",() -> Blocks.AIR,false,0);

//    private static final List<Plant> PLANTS = new LinkedList<>();
    private static final LinkedList<Plant> PLANTS = new LinkedList<>();

    public static final Object2IntLinkedOpenHashMap<ResourceLocation> RESOURCE_LOCATION_MAP = new Object2IntLinkedOpenHashMap<>();
    public static void init() {
        registerPlant(AJUGINUCULA_SMITHII);
        registerPlant(SMALL_ROYAL_FERN);
        registerPlant(CALAMITES);
        registerPlant(SMALL_CHAIN_FERN);
        registerPlant(SMALL_CYCAD);
        registerPlant(GINKGO);
        registerPlant(CYCADEOIDEA);
        registerPlant(CRY_PANSY);
        registerPlant(SCALY_TREE_FERN);
        registerPlant(ZAMITES);
        registerPlant(DICKSONIA);
        registerPlant(WILD_ONION);
        registerPlant(DICROIDIUM_ZUBERI);
        registerPlant(DICTYOPHYLLUM);
        registerPlant(WEST_INDIAN_LILAC);
        registerPlant(SERENNA_VERIFORMANS);
        registerPlant(LADINIA_SIMPLEX);
        registerPlant(ORONTIUM_MACKII);
        registerPlant(UMALTOLEPIS);
        registerPlant(LIRIODENDRITES);
        registerPlant(RAPHAELIA);
        registerPlant(ENCEPHALARTOS);
        registerPlant(PSARONIUS);
        registerPlant(PHOENIX);
        registerPlant(WILD_POTATO);
        registerPlant(ARAUCARIA);
        registerPlant(CINNAMON_FERN);
        registerPlant(BRISTLE_FERN);
        registerPlant(TEMPSKYA);
        registerPlant(WOOLLY_STALKED_BEGONIA);
        registerPlant(LARGESTIPULE_LEATHER_ROOT);
        registerPlant(RHACOPHYTON);
        registerPlant(GRAMINIDITES_BAMBUSOIDES);
        registerPlant(ENALLHELIA);
        registerPlant(AULOPORA);
        registerPlant(CLADOCHONUS);
        registerPlant(LITHOSTROTION);
        registerPlant(STYLOPHYLLOPSIS);
        registerPlant(HIPPURITES_RADIOSUS);
        registerPlant(HELICONIA);
        registerPlant(RHAMNUS_SALICIFOLIUS);
        registerPlant(EMPTY);
    }

    public static Plant getPlantById(int id) {
        if (id >= PLANTS.size() || id < 0) {
            return null;
        }
        Plant plant = PLANTS.get(id);
        if(plant == null){
            return EMPTY;
        }


        return plant;
    }



    public static Plant getPlantById(ResourceLocation plant){
        if(RESOURCE_LOCATION_MAP.containsKey(plant)){
            int id = RESOURCE_LOCATION_MAP.getInt(plant);
            return getPlantById(id);//hand the return to the function that is meant for int ids
        }

        return EMPTY;
    }

    public static ResourceLocation getPlantId(Plant plant) {
        return ResourceLocation.parse(JurassicReborn.MODID + ":" + plant.getName().toLowerCase(Locale.ROOT).replace(' ', '_'));
    }

    public static List<Plant> getPlants() {
        return PLANTS;
    }

    public static void registerPlant(Plant plant) {
        if (!PLANTS.contains(plant)) {
            PLANTS.add(plant);
            int id = PLANTS.indexOf(plant);
            RESOURCE_LOCATION_MAP.put(ResourceLocation.parse(JurassicReborn.MODID + ":" + plant.getName().toLowerCase(Locale.ROOT).replace(' ', '_')), id);
        }

    }

    public static List<Plant> getPrehistoricPlantsAndTrees() {
        List<Plant> prehistoricPlants = new LinkedList<>();
        for (Plant plant : PLANTS) {
            if (plant.shouldRegister() && plant.isPrehistoric()) {
                prehistoricPlants.add(plant);
            }
        }
        return prehistoricPlants;
    }
    // In PlantHandler
    public static int getId(Plant plant) {
        return PLANTS.indexOf(plant);
    }

    public static List<Plant> getPrehistoricPlants() {
        List<Plant> prehistoricPlants = new LinkedList<>();
        for (Plant plant : PLANTS) {
            if (plant.shouldRegister() && plant.isPrehistoric() && !plant.isTree()) {
                prehistoricPlants.add(plant);
            }
        }
        return prehistoricPlants;
    }
}
