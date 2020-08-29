package mod.reborn.server.block;

import mod.reborn.server.api.SubBlocksBlock;
import mod.reborn.server.block.entity.*;
import mod.reborn.server.block.machine.*;
import mod.reborn.server.block.plant.*;
import mod.reborn.server.block.tree.*;
import mod.reborn.server.conf.RebornConfig;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.util.RegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import mod.reborn.RebornMod;
import mod.reborn.server.block.fence.ElectricFenceBaseBlock;
import mod.reborn.server.block.fence.ElectricFencePoleBlock;
import mod.reborn.server.block.fence.ElectricFenceWireBlock;
import mod.reborn.server.block.fence.FenceType;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.EntityHandler;

import java.util.*;

public class BlockHandler
{
    public static final Map<TreeType, AncientPlanksBlock> ANCIENT_PLANKS = new HashMap<>();
    public static final Map<TreeType, AncientLogBlock> ANCIENT_LOGS = new HashMap<>();
    public static final Map<TreeType, AncientLeavesBlock> ANCIENT_LEAVES = new HashMap<>();
    public static final Map<TreeType, AncientSaplingBlock> ANCIENT_SAPLINGS = new HashMap<>();

    public static final Map<TreeType, AncientSlabHalfBlock> ANCIENT_SLABS = new HashMap<>();
    public static final Map<TreeType, AncientDoubleSlabBlock> ANCIENT_DOUBLE_SLABS = new HashMap<>();
    public static final Map<TreeType, AncientStairsBlock> ANCIENT_STAIRS = new HashMap<>();
    public static final Map<TreeType, AncientFenceBlock> ANCIENT_FENCES = new HashMap<>();
    public static final Map<TreeType, AncientFenceGateBlock> ANCIENT_FENCE_GATES = new HashMap<>();
    public static final Map<TreeType, AncientDoorBlock> ANCIENT_DOORS = new HashMap<>();

    public static final Map<TreeType, AncientLogBlock> PETRIFIED_LOGS = new HashMap<>();

    public static final List<FossilBlock> FOSSILS = new ArrayList<>();
    public static final List<EncasedFossilBlock> ENCASED_FOSSILS = new ArrayList<>();

    public static final PlantFossilBlock PLANT_FOSSIL = new PlantFossilBlock();

    public static final CleaningStationBlock CLEANING_STATION = new CleaningStationBlock();
    public static final FossilGrinderBlock FOSSIL_GRINDER = new FossilGrinderBlock();
    public static final DNASequencerBlock DNA_SEQUENCER = new DNASequencerBlock();
    public static final DNASynthesizerBlock DNA_SYNTHESIZER = new DNASynthesizerBlock();
    public static final EmbryonicMachineBlock EMBRYONIC_MACHINE = new EmbryonicMachineBlock();
    public static final EmbryoCalcificationMachineBlock EMBRYO_CALCIFICATION_MACHINE = new EmbryoCalcificationMachineBlock();
    public static final IncubatorBlock INCUBATOR = new IncubatorBlock();
    public static final DNAExtractorBlock DNA_EXTRACTOR = new DNAExtractorBlock();
    public static final DNACombinatorHybridizerBlock DNA_COMBINATOR_HYBRIDIZER = new DNACombinatorHybridizerBlock();

    public static final AmberBlock AMBER_ORE = new AmberBlock();
    public static final IceShardBlock ICE_SHARD = new IceShardBlock();

    public static final GypsumStoneBlock GYPSUM_STONE = new GypsumStoneBlock();
    public static final Block GYPSUM_COBBLESTONE = new BasicBlock(Material.ROCK).setHardness(1.5F);
    public static final Block GYPSUM_BRICKS = new BasicBlock(Material.ROCK).setHardness(2.0F);

    public static final Block REINFORCED_STONE = new BasicBlock(Material.ROCK).setHardness(2.0F);
    public static final Block REINFORCED_BRICKS = new BasicBlock(Material.ROCK).setHardness(3.0F);

    public static final CultivatorTopBlock CULTIVATOR_TOP = new CultivatorTopBlock();
    public static final CultivatorBottomBlock CULTIVATOR_BOTTOM = new CultivatorBottomBlock();

    public static final DisplayBlock DISPLAY_BLOCK = new DisplayBlock();

    public static final ClearGlassBlock CLEAR_GLASS = new ClearGlassBlock();

    public static final FossilizedTrackwayBlock FOSSILIZED_TRACKWAY = new FossilizedTrackwayBlock();
    public static final NestFossilBlock NEST_FOSSIL = new NestFossilBlock(false);
    public static final NestFossilBlock ENCASED_NEST_FOSSIL = new NestFossilBlock(true);

    public static final SmallRoyalFernBlock SMALL_ROYAL_FERN = new SmallRoyalFernBlock();
    public static final SmallChainFernBlock SMALL_CHAIN_FERN = new SmallChainFernBlock();
    public static final SmallCycadBlock SMALL_CYCAD = new SmallCycadBlock();
    public static final BennettitaleanCycadeoideaBlock CYCADEOIDEA = new BennettitaleanCycadeoideaBlock();
    public static final AncientPlantBlock CRY_PANSY = new AncientPlantBlock();
    public static final ScalyTreeFernBlock SCALY_TREE_FERN = new ScalyTreeFernBlock();
    public static final CycadZamitesBlock ZAMITES = new CycadZamitesBlock();
    public static final DicksoniaBlock DICKSONIA = new DicksoniaBlock();
    public static final DicroidiumZuberiBlock DICROIDIUM_ZUBERI = new DicroidiumZuberiBlock();
    public static final AjuginuculaSmithiiBlock AJUGINUCULA_SMITHII = new AjuginuculaSmithiiBlock();
    public static final WildOnionBlock WILD_ONION = new WildOnionBlock();
    public static final GracilariaBlock GRACILARIA = new GracilariaBlock();
    public static final DictyophyllumBlock DICTYOPHYLLUM = new DictyophyllumBlock();
    public static final WestIndianLilacBlock WEST_INDIAN_LILAC = new WestIndianLilacBlock();
    public static final SerennaVeriformansBlock SERENNA_VERIFORMANS = new SerennaVeriformansBlock();
    public static final SmallPlantBlock LADINIA_SIMPLEX = new SmallPlantBlock();
    public static final OrontiumMackiiBlock ORONTIUM_MACKII = new OrontiumMackiiBlock();
    public static final UmaltolepisBlock UMALTOLEPIS = new UmaltolepisBlock();
    public static final LiriodendritesBlock LIRIODENDRITES = new LiriodendritesBlock();
    public static final RaphaeliaBlock RAPHAELIA = new RaphaeliaBlock();
    public static final EncephalartosBlock ENCEPHALARTOS = new EncephalartosBlock();
    public static final WildPotatoBlock WILD_POTATO_PLANT = new WildPotatoBlock();
    public static final RhamnusSalifocifusBlock RHAMNUS_SALICIFOLIUS_PLANT = new RhamnusSalifocifusBlock();
    public static final SmallPlantBlock CINNAMON_FERN = new SmallPlantBlock();
    public static final SmallPlantBlock BRISTLE_FERN = new SmallPlantBlock();
    public static final DoublePlantBlock TEMPSKYA = new DoublePlantBlock();
    public static final SmallPlantBlock WOOLLY_STALKED_BEGONIA = new SmallPlantBlock();
    public static final SmallPlantBlock LARGESTIPULE_LEATHER_ROOT = new SmallPlantBlock();
    public static final DoublePlantBlock RHACOPHYTON = new DoublePlantBlock();
    public static final DoublePlantBlock GRAMINIDITES_BAMBUSOIDES = new DoublePlantBlock();
    public static final DoublePlantBlock HELICONIA = new DoublePlantBlock();

    public static final AncientCoralBlock ENALLHELIA = new AncientCoralBlock();
    public static final AncientCoralBlock AULOPORA = new AncientCoralBlock();
    public static final AncientCoralBlock CLADOCHONUS = new AncientCoralBlock();
    public static final AncientCoralBlock LITHOSTROTION = new AncientCoralBlock();
    public static final AncientCoralBlock STYLOPHYLLOPSIS = new AncientCoralBlock();
    public static final AncientCoralBlock HIPPURITES_RADIOSUS = new AncientCoralBlock();

    public static final PeatBlock PEAT = new PeatBlock();
    public static final Block PEAT_MOSS = new PeatMossBlock();
    public static final MossBlock MOSS = new MossBlock();

    public static final FeederBlock FEEDER = new FeederBlock();

    public static final BugCrateBlock BUG_CRATE = new BugCrateBlock();

    public static final SwarmBlock PLANKTON_SWARM = new SwarmBlock(() -> ItemHandler.PLANKTON);
    public static final SwarmBlock KRILL_SWARM = new SwarmBlock(() -> ItemHandler.KRILL);



    public static final TourRailBlock TOUR_RAIL = new TourRailBlock(TourRailBlock.SpeedType.NONE);


    public static final SkeletonAssemblyBlock SKELETON_ASSEMBLY = new SkeletonAssemblyBlock();

    public static ElectricFencePoleBlock LOW_SECURITY_FENCE_POLE = new ElectricFencePoleBlock(FenceType.LOW);
    public static ElectricFencePoleBlock MED_SECURITY_FENCE_POLE = new ElectricFencePoleBlock(FenceType.MED);
    public static ElectricFencePoleBlock HIGH_SECURITY_FENCE_POLE = new ElectricFencePoleBlock(FenceType.HIGH);

    public static ElectricFenceBaseBlock LOW_SECURITY_FENCE_BASE = new ElectricFenceBaseBlock(FenceType.LOW);
    public static ElectricFenceBaseBlock MED_SECURITY_FENCE_BASE = new ElectricFenceBaseBlock(FenceType.MED);
    public static ElectricFenceBaseBlock HIGH_SECURITY_FENCE_BASE = new ElectricFenceBaseBlock(FenceType.HIGH);

    public static ElectricFenceWireBlock LOW_SECURITY_FENCE_WIRE = new ElectricFenceWireBlock(FenceType.LOW);
    public static ElectricFenceWireBlock MED_SECURITY_FENCE_WIRE = new ElectricFenceWireBlock(FenceType.MED);
    public static ElectricFenceWireBlock HIGH_SECURITY_FENCE_WIRE = new ElectricFenceWireBlock(FenceType.HIGH);

    public static PaleoBaleBlock PALEO_BALE_CYCADEOIDEA = new PaleoBaleBlock(PaleoBaleBlock.Variant.CYCADEOIDEA);
    public static PaleoBaleBlock PALEO_BALE_CYCAD = new PaleoBaleBlock(PaleoBaleBlock.Variant.CYCAD);
    public static PaleoBaleBlock PALEO_BALE_FERN = new PaleoBaleBlock(PaleoBaleBlock.Variant.FERN);
    public static PaleoBaleBlock PALEO_BALE_LEAVES = new PaleoBaleBlock(PaleoBaleBlock.Variant.LEAVES);
    public static PaleoBaleBlock PALEO_BALE_OTHER = new PaleoBaleBlock(PaleoBaleBlock.Variant.OTHER);

    public static void init()
    {
        registerBlock(PLANT_FOSSIL, "Plant Fossil Block");
        registerBlock(FOSSILIZED_TRACKWAY, "Fossilized Trackway");
        registerBlock(NEST_FOSSIL, "Nest Fossil");
        registerBlock(ENCASED_NEST_FOSSIL, "Encased Nest Fossil");

        for (int i = 0; i < (int) Math.ceil((EntityHandler.getHighestID() + 1) / 16.0F); i++)
        {
            FossilBlock fossil = new FossilBlock(i * 16);
            EncasedFossilBlock encasedFossil = new EncasedFossilBlock(i * 16);

            FOSSILS.add(fossil);
            ENCASED_FOSSILS.add(encasedFossil);

            registerBlock(fossil, "Fossil Block " + i);
            registerBlock(encasedFossil, "Encased Fossil " + i);
        }

        for (TreeType type : TreeType.values())
        {
            registerTreeType(type);
        }

        registerBlock(AMBER_ORE, "Amber Ore");
        registerBlock(ICE_SHARD, "Ice Shard");
        registerBlock(GYPSUM_STONE, "Gypsum Stone");
        registerBlock(GYPSUM_COBBLESTONE, "Gypsum Cobblestone");
        registerBlock(GYPSUM_BRICKS, "Gypsum Bricks");
        registerBlock(REINFORCED_STONE, "Reinforced Stone");
        registerBlock(REINFORCED_BRICKS, "Reinforced Bricks");

        registerBlock(AJUGINUCULA_SMITHII, "Ajuginucula Smithii");
        registerBlock(SMALL_ROYAL_FERN, "Small Royal Fern");
        registerBlock(SMALL_CHAIN_FERN, "Small Chain Fern");
        registerBlock(SMALL_CYCAD, "Small Cycad");
        registerBlock(CYCADEOIDEA, "Bennettitalean Cycadeoidea");
        registerBlock(CRY_PANSY, "Cry Pansy");
        registerBlock(SCALY_TREE_FERN, "Scaly Tree Fern");
        registerBlock(ZAMITES, "Cycad Zamites");
        registerBlock(DICKSONIA, "Dicksonia");
        registerBlock(WILD_ONION, "Wild Onion Plant");
        registerBlock(GRACILARIA, "Gracilaria Seaweed");
        registerBlock(DICROIDIUM_ZUBERI, "Dicroidium Zuberi");
        registerBlock(DICTYOPHYLLUM, "Dictyophyllum");
        registerBlock(WEST_INDIAN_LILAC, "West Indian Lilac");
        registerBlock(SERENNA_VERIFORMANS, "Serenna Veriformans");
        registerBlock(LADINIA_SIMPLEX, "Ladinia Simplex");
        registerBlock(ORONTIUM_MACKII, "Orontium Mackii");
        registerBlock(UMALTOLEPIS, "Umaltolepis");
        registerBlock(LIRIODENDRITES, "Liriodendrites");
        registerBlock(RAPHAELIA, "Raphaelia");
        registerBlock(ENCEPHALARTOS, "Encephalartos");
        registerBlock(WILD_POTATO_PLANT, "Wild Potato Plant");
        registerBlock(RHAMNUS_SALICIFOLIUS_PLANT, "Rhamnus Salicifolius");
        registerBlock(BRISTLE_FERN, "Bristle Fern");
        registerBlock(CINNAMON_FERN, "Cinnamon Fern");
        registerBlock(TEMPSKYA, "Tempskya");
        registerBlock(WOOLLY_STALKED_BEGONIA, "Woolly Stalked Begonia");
        registerBlock(LARGESTIPULE_LEATHER_ROOT, "Largestipule Leather Root");
        registerBlock(RHACOPHYTON, "Rhacophyton");
        registerBlock(GRAMINIDITES_BAMBUSOIDES, "Graminidites Bambusoides");
        registerBlock(ENALLHELIA, "Enallhelia");
        registerBlock(AULOPORA, "Aulopora");
        registerBlock(CLADOCHONUS, "Cladochonus");
        registerBlock(LITHOSTROTION, "Lithostrotion");
        registerBlock(STYLOPHYLLOPSIS, "Stylophyllopsis");
        registerBlock(HIPPURITES_RADIOSUS, "Hippurites Radiosus");
        registerBlock(HELICONIA, "Heliconia");

        registerBlock(MOSS, "Moss");
        registerBlock(PEAT, "Peat");
        registerBlock(PEAT_MOSS, "Peat Moss");

        registerBlock(CLEAR_GLASS, "Clear Glass");

        registerBlock(PLANKTON_SWARM, "Plankton Swarm");
        registerBlock(KRILL_SWARM, "Krill Swarm");
        if(RebornConfig.VEHICLES.tourRailBlockEnabled) {
            registerBlock(TourRailBlockEntity.class, TOUR_RAIL, "Tour Rail");
        }
        registerBlock(SKELETON_ASSEMBLY, "Skeleton Assembly");
//        registerBlock(JP_MAIN_GATE_BLOCK, "Jurassic Park Gate");

        registerBlock(CultivatorBlockEntity.class, CULTIVATOR_BOTTOM, "Cultivate Bottom");
        registerBlock(CULTIVATOR_TOP, "Cultivate Top");
        registerBlock(CleaningStationBlockEntity.class, CLEANING_STATION, "Cleaning Station");
        registerBlock(FossilGrinderBlockEntity.class, FOSSIL_GRINDER, "Fossil Grinder");
        registerBlock(DNASequencerBlockEntity.class, DNA_SEQUENCER, "DNA Sequencer");
        registerBlock(DNASynthesizerBlockEntity.class, DNA_SYNTHESIZER, "DNA Synthesizer");
        registerBlock(EmbryonicMachineBlockEntity.class, EMBRYONIC_MACHINE, "Embryonic Machine");
        registerBlock(EmbryoCalcificationMachineBlockEntity.class, EMBRYO_CALCIFICATION_MACHINE, "Embryo Calcification Machine");
        registerBlock(DNAExtractorBlockEntity.class, DNA_EXTRACTOR, "DNA Extractor");
        registerBlock(DNACombinatorHybridizerBlockEntity.class, DNA_COMBINATOR_HYBRIDIZER, "DNA Combinator Hybridizer");
        registerBlock(IncubatorBlockEntity.class, INCUBATOR, "Incubator");
        registerBlock(DisplayBlockEntity.class, DISPLAY_BLOCK, "Display Block");
        registerBlock(FeederBlockEntity.class, FEEDER, "Feeder");
        registerBlock(BugCrateBlockEntity.class, BUG_CRATE, "Bug Crate");

        registerBlock(ElectricFenceWireBlockEntity.class, "tileEntityElectricFence", LOW_SECURITY_FENCE_WIRE, "Low Security Fence Wire");
//        registerBlock(MED_SECURITY_FENCE_WIRE, "Med Security Fence Wire");
//        registerBlock(HIGH_SECURITY_FENCE_WIRE, "High Security Fence Wire");

        registerBlock(ElectricFencePoleBlockEntity.class, "tileEntityElectricPole", LOW_SECURITY_FENCE_POLE, "Low Security Fence Pole");
//        registerBlock(MED_SECURITY_FENCE_POLE, "Med Security Fence Pole");
//        registerBlock(HIGH_SECURITY_FENCE_POLE, "High Security Fence Pole");

        registerBlock(ElectricFenceBaseBlockEntity.class, "tileEntityElectricBase", LOW_SECURITY_FENCE_BASE, "Low Security Fence Base");
//        registerBlock(MED_SECURITY_FENCE_BASE, "Med Security Fence Base");
//        registerBlock(HIGH_SECURITY_FENCE_BASE, "High Security Fence Base");


        registerBlock(PALEO_BALE_OTHER, "Paleo Bale Other");
        registerBlock(PALEO_BALE_CYCADEOIDEA, "Paleo Bale Cycadeoidea");
        registerBlock(PALEO_BALE_CYCAD, "Paleo Bale Cycad");
        registerBlock(PALEO_BALE_FERN, "Paleo Bale Fern");
        registerBlock(PALEO_BALE_LEAVES, "Paleo Bale Leaves");
    }

    public static void registerTreeType(TreeType type)
    {
        AncientPlanksBlock planks = new AncientPlanksBlock(type);
        AncientLogBlock log = new AncientLogBlock(type, false);
        AncientLogBlock petrifiedLog = new AncientLogBlock(type, true);
        AncientLeavesBlock leaves = new AncientLeavesBlock(type);
        AncientSaplingBlock sapling = new AncientSaplingBlock(type);
        AncientStairsBlock stair = new AncientStairsBlock(type, planks.getDefaultState());
        AncientSlabHalfBlock slab = new AncientSlabHalfBlock(type, planks.getDefaultState());
        AncientDoubleSlabBlock doubleSlab = new AncientDoubleSlabBlock(type, slab, planks.getDefaultState());
        AncientFenceBlock fence = new AncientFenceBlock(type);
        AncientFenceGateBlock fenceGate = new AncientFenceGateBlock(type);
        AncientDoorBlock door = new AncientDoorBlock(type);

        ANCIENT_PLANKS.put(type, planks);
        ANCIENT_LOGS.put(type, log);
        ANCIENT_LEAVES.put(type, leaves);
        ANCIENT_SAPLINGS.put(type, sapling);
        ANCIENT_STAIRS.put(type, stair);
        ANCIENT_SLABS.put(type, slab);
        ANCIENT_DOUBLE_SLABS.put(type, doubleSlab);
        ANCIENT_FENCES.put(type, fence);
        ANCIENT_FENCE_GATES.put(type, fenceGate);
        ANCIENT_DOORS.put(type, door);
        PETRIFIED_LOGS.put(type, petrifiedLog);

        String typeName = type.name();

        registerBlock(planks, typeName + " Planks");
        registerBlock(log, typeName + " Log");
        registerBlock(petrifiedLog, typeName + " Log Petrified");
        registerBlock(leaves, typeName + " Leaves");
        registerBlock(sapling, typeName + " Sapling");
        registerBlock(stair, typeName + " Stairs");
        registerBlock(slab, typeName + " Slab");
        registerBlock(doubleSlab, typeName + " Double Slab");
        registerBlock(fence, typeName + " Fence");
        registerBlock(fenceGate, typeName + " Fence Gate");
        registerBlock(door, typeName + " Door");

        Blocks.FIRE.setFireInfo(leaves, 30, 60);
        Blocks.FIRE.setFireInfo(planks, 5, 20);
        Blocks.FIRE.setFireInfo(log, 5, 5);
        Blocks.FIRE.setFireInfo(petrifiedLog, 5, 5);
        Blocks.FIRE.setFireInfo(doubleSlab, 5, 20);
        Blocks.FIRE.setFireInfo(slab, 5, 20);
        Blocks.FIRE.setFireInfo(stair, 5, 20);
        Blocks.FIRE.setFireInfo(fence, 5, 20);
        Blocks.FIRE.setFireInfo(fenceGate, 5, 20);
    }

    /**
     * Ores need to be registered after blocks and items.
     */
    public static void registerOres()
    {
        for(int i = 0; i < TreeType.values().length; i++)
        {
            OreDictionary.registerOre("logWood", ANCIENT_LOGS.get(TreeType.values()[i]));
            OreDictionary.registerOre("logWood", PETRIFIED_LOGS.get(TreeType.values()[i]));
            OreDictionary.registerOre("plankWood", ANCIENT_PLANKS.get(TreeType.values()[i]));
            OreDictionary.registerOre("treeLeaves", ANCIENT_LEAVES.get(TreeType.values()[i]));
            OreDictionary.registerOre("treeSapling", ANCIENT_SAPLINGS.get(TreeType.values()[i]));
            OreDictionary.registerOre("slabWood", ANCIENT_SLABS.get(TreeType.values()[i]));
            OreDictionary.registerOre("stairWood", ANCIENT_STAIRS.get(TreeType.values()[i]));
            OreDictionary.registerOre("fenceWood", ANCIENT_FENCES.get(TreeType.values()[i]));
            OreDictionary.registerOre("gateWood", ANCIENT_FENCE_GATES.get(TreeType.values()[i]));
            OreDictionary.registerOre("fenceGateWood", ANCIENT_FENCE_GATES.get(TreeType.values()[i]));
            OreDictionary.registerOre("doorWood", ANCIENT_DOORS.get(TreeType.values()[i]));
        }
        for(FossilBlock fossil : FOSSILS)
        {
            OreDictionary.registerOre("fossil", fossil);
        }
    }

    public static FossilBlock getFossilBlock(Dinosaur dinosaur)
    {
        return getFossilBlock(EntityHandler.getDinosaurId(dinosaur));
    }

    private static int getBlockId(int id)
    {
        return (int) (Math.floor((((float) id + 1.0F) / 16.0F) - 0.0625F));
    }

    public static EncasedFossilBlock getEncasedFossil(Dinosaur dinosaur)
    {
        return getEncasedFossil(EntityHandler.getDinosaurId(dinosaur));
    }

    public static EncasedFossilBlock getEncasedFossil(int id)
    {
        return ENCASED_FOSSILS.get(getBlockId(id));
    }

    public static FossilBlock getFossilBlock(int id)
    {
        return FOSSILS.get(getBlockId(id));
    }

    public static int getDinosaurId(FossilBlock fossil, int metadata)
    {
        return (FOSSILS.indexOf(fossil) * 16) + metadata;
    }

    public static int getDinosaurId(EncasedFossilBlock fossil, int metadata)
    {
        return (ENCASED_FOSSILS.indexOf(fossil) * 16) + metadata;
    }

    public static int getMetadata(int id)
    {
        return id % 16;
    }

    public static int getMetadata(Dinosaur dinosaur)
    {
        return getMetadata(EntityHandler.getDinosaurId(dinosaur));
    }

    public static void registerBlock(Class<? extends TileEntity> tileEntity, Block block, String name)
    {
        registerBlock(tileEntity, name.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_"), block, name);
    }

    public static void registerBlock(Class<? extends TileEntity> tileEntity, String tileEntityName,  Block block, String name)
    {
        registerBlock(block, name);
        GameRegistry.registerTileEntity(tileEntity, new ResourceLocation(RebornMod.MODID, tileEntityName));
    }

    public static void registerBlock(Block block, String name)
    {
        if(block instanceof SubBlocksBlock)
            RegistryHandler.registerBlockWithCustomItem(block, ((SubBlocksBlock) block).getItemBlock(), name.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_"));
        else
            RegistryHandler.registerBlockWithItem(block, name.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_"));
    }
}
