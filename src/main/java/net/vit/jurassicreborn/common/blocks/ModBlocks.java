package net.vit.jurassicreborn.common.blocks;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.*;
import net.vit.jurassicreborn.common.blocks.ancientplants.DoublePlantBlock;
import net.vit.jurassicreborn.common.blocks.entities.skeletonassembly.SkeletonAssemblyBlock;
import net.vit.jurassicreborn.common.blocks.fossil.EncasedFaunaFossilBlock;
import net.vit.jurassicreborn.common.blocks.ancientplants.*;
import net.vit.jurassicreborn.common.blocks.fossil.*;
import net.vit.jurassicreborn.common.blocks.ancientplants.moss.PeatBlock;
import net.vit.jurassicreborn.common.blocks.entities.ActionFigureBlock;
import net.vit.jurassicreborn.common.blocks.entities.HologramBlock;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNACombinatorHybridizer.DNACombinatorHybridizerBlock;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNAExtractor.DNAExtractorBlock;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASequencer.DNASequencerBlock;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASynthesizer.DNASynthesizerBlock;
import net.vit.jurassicreborn.common.blocks.entities.Embryoncis.EmbryoCalcificationMachine.EmbryoCalcificationMachineBlock;
import net.vit.jurassicreborn.common.blocks.entities.Embryoncis.EmbryonicMachine.EmbryonicMachineBlock;
import net.vit.jurassicreborn.common.blocks.entities.bugcrate.BugCrate;
import net.vit.jurassicreborn.common.blocks.entities.cleaner.CleanerBlock;
import net.vit.jurassicreborn.common.blocks.entities.cultivator.CultivatorBottomBlock;
import net.vit.jurassicreborn.common.blocks.entities.cultivator.CultivatorTopBlock;
import net.vit.jurassicreborn.common.blocks.entities.feeder.FeederBlock;
import net.vit.jurassicreborn.common.blocks.entities.fence.ElectricFenceBaseBlock;
import net.vit.jurassicreborn.common.blocks.entities.fence.ElectricFencePoleBlock;
import net.vit.jurassicreborn.common.blocks.entities.fence.ElectricFenceWireBlock;
import net.vit.jurassicreborn.common.blocks.entities.fence.FenceType;
import net.vit.jurassicreborn.common.blocks.entities.grinder.FossilGrinderBlock;
import net.vit.jurassicreborn.common.blocks.entities.incubator.IncubatorBlock;
import net.vit.jurassicreborn.common.blocks.entities.paleobale.PaleoBaleBlock;
import net.vit.jurassicreborn.common.blocks.fossil.AncientCoralBlock;
import net.vit.jurassicreborn.common.blocks.fossil.dinosaurs.*;
import net.vit.jurassicreborn.common.blocks.grower.TreeGrower;
import net.vit.jurassicreborn.common.blocks.parkBlocks.TourRailBlock;
import net.vit.jurassicreborn.common.blocks.parkBlocks.ParkBenchBlock;
import net.vit.jurassicreborn.common.blocks.entities.trashcan.TrashCanBlock;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.items.ModItems;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vit.jurassicreborn.common.plants.WestIndianLilacBlock;
import net.vit.jurassicreborn.common.worldgen.JRTreeFeatures;

import java.util.function.Supplier;

import static net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler.ACHILLOBATOR;

public class ModBlocks {

    public static DeferredRegister<Block> MOD_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "jurassicreborn");

    public static final RegistryObject<SaplingBlock> ARAUCARIA_SAPLING = MOD_BLOCKS.register("araucaria_sapling",
            () -> new SaplingBlock(new TreeGrower(() -> JRTreeFeatures.AraucariaTree), BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));

    public static final RegistryObject<SaplingBlock> GINKGO_SAPLING = MOD_BLOCKS.register("ginkgo_sapling",
            () -> new SaplingBlock(new TreeGrower(() -> JRTreeFeatures.GinkgoTree), BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));


    public static final RegistryObject<SaplingBlock> CALAMITES_SAPLING = MOD_BLOCKS.register("calamites_sapling",
            () -> new SaplingBlock(new TreeGrower(() -> JRTreeFeatures.CalamitesTreeFeature), BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));


    public static final RegistryObject<SaplingBlock> PHOENIX_SAPLING = MOD_BLOCKS.register("phoenix_sapling", () ->
            new SaplingBlock(new TreeGrower(() -> JRTreeFeatures.PhoenixTreeFeature), BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));

    public static final RegistryObject<SaplingBlock> PSARONIUS_SAPLING = MOD_BLOCKS.register("psaronius_sapling",
            () -> new SaplingBlock(new TreeGrower(() -> JRTreeFeatures.PsaroniusTree), BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));


    public static final RegistryObject<SaplingBlock> MAGNOLIA_SAPLING = MOD_BLOCKS.register("magnolia_sapling",
            () -> new SaplingBlock(new TreeGrower(() -> JRTreeFeatures.MagnoliaTreeFeature), BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));


    public static RegistryObject<FloraFossil> FLORA_FOSSIL = MOD_BLOCKS.register("flora_fossil", () -> new FloraFossil(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f).requiresCorrectToolForDrops()));
    public static RegistryObject<FloraFossil> DEEPSLATE_FLORA_FOSSIL = MOD_BLOCKS.register("deepslate_flora_fossil", () -> new FloraFossil(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f).requiresCorrectToolForDrops()));
    public static RegistryObject<FaunaFossil> FAUNA_FOSSIL = MOD_BLOCKS.register("fauna_fossil", () -> new FaunaFossil(BlockBehaviour.Properties.of(Material.STONE)));

    public static RegistryObject<Block> ENCASED_FAUNA_FOSSIL = MOD_BLOCKS.register("encased_fauna_fossil", () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(5.0F, 0.0F), Dinosaur.EMPTY));
    public static RegistryObject<Block> AMBER_ORE = MOD_BLOCKS.register("amber_ore", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f)));
    public static RegistryObject<Block> AMBER_BLOCK = MOD_BLOCKS.register("amber_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(3.0f).sound(SoundType.STONE).requiresCorrectToolForDrops().noOcclusion()));
    public static RegistryObject<Block> AMBER_MOSQUITO = MOD_BLOCKS.register("amber_mosquito", () -> new AmberMosquitoBlock(BlockBehaviour.Properties.of(Material.STONE).strength(0.5f).sound(SoundType.STONE).noOcclusion()));
    public static RegistryObject<Block> AMBER_APHID = MOD_BLOCKS.register("amber_aphid", () -> new AmberAphidBlock(BlockBehaviour.Properties.of(Material.STONE).strength(0.5f).sound(SoundType.STONE).noOcclusion()));
    public static RegistryObject<Block> SEA_LAMPREY = MOD_BLOCKS.register("sea_lamprey", () -> new SeaLampreyBlock(BlockBehaviour.Properties.of(Material.STONE).strength(0.5f).sound(SoundType.STONE).noOcclusion()));
    public static RegistryObject<Block> FROZEN_LEECH = MOD_BLOCKS.register("frozen_leech", () -> new SeaLampreyBlock(BlockBehaviour.Properties.of(Material.STONE).strength(0.5f).sound(SoundType.STONE).noOcclusion()));
    public static RegistryObject<Block> ICE_SHARD_ORE = MOD_BLOCKS.register("ice_shard_ore", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f).requiresCorrectToolForDrops()));

    public static RegistryObject<Block> DEEPSLATE_AMBER_ORE = MOD_BLOCKS.register("deepslate_amber_ore", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f).requiresCorrectToolForDrops()));

    public static RegistryObject<Block> DEEPSLATE_ICE_SHARD_ORE = MOD_BLOCKS.register("deepslate_ice_shard_ore", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f).requiresCorrectToolForDrops()));

    public static RegistryObject<CleanerBlock> CLEANING_STATION = MOD_BLOCKS.register("cleaning_station", () -> new CleanerBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f).requiresCorrectToolForDrops()));

    public static RegistryObject<FossilGrinderBlock> FOSSIL_GRINDER = MOD_BLOCKS.register("fossil_grinder", () -> new FossilGrinderBlock(defaultMachine().strength(2.0f).requiresCorrectToolForDrops()));

    public static RegistryObject<DNASequencerBlock> DNA_SEQUENCER = MOD_BLOCKS.register("dna_sequencer", () -> new DNASequencerBlock(defaultMachine().strength(2.0f).requiresCorrectToolForDrops()));

    public static RegistryObject<DNASynthesizerBlock> DNA_SYNTHESIZER = MOD_BLOCKS.register("dna_synthesizer", () -> new DNASynthesizerBlock(defaultMachine().strength(2.0f).requiresCorrectToolForDrops()));
    public static RegistryObject<EmbryoCalcificationMachineBlock> EMBRYO_CALCIFICATION_MACHINE = MOD_BLOCKS.register("embryo_calcification_machine", () -> new EmbryoCalcificationMachineBlock(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f).requiresCorrectToolForDrops()));


    public static RegistryObject<EmbryonicMachineBlock> EMBRYONIC_MACHINE = MOD_BLOCKS.register("embryonic_machine", () -> new EmbryonicMachineBlock(defaultMachine().strength(2.0f).requiresCorrectToolForDrops()));

    public static RegistryObject<IncubatorBlock> INCUBATOR = MOD_BLOCKS.register("incubator", () -> new IncubatorBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f).requiresCorrectToolForDrops()));//hehe

    public static RegistryObject<DNAExtractorBlock> DNA_EXTRACTOR = MOD_BLOCKS.register("dna_extractor", () -> new DNAExtractorBlock(defaultMachine().strength(2.0f).requiresCorrectToolForDrops()));

    public static RegistryObject<DNACombinatorHybridizerBlock> DNA_COMBINER_HYBRIDIZER = MOD_BLOCKS.register("dna_combinator_hybridizer", () -> new DNACombinatorHybridizerBlock(defaultMachine().strength(2.0f).requiresCorrectToolForDrops()));
    private static final Supplier<FlowerPotBlock> DEFAULT_FLOWER_POT = () -> (FlowerPotBlock) Blocks.FLOWER_POT;

    public static final RegistryObject<FlowerPotBlock> POTTED_ARAUCARIA_SAPLING = registerPottedSapling("araucaria",
            ARAUCARIA_SAPLING);

    public static final RegistryObject<FlowerPotBlock> POTTED_GINKGO_SAPLING = registerPottedSapling("ginkgo",
            GINKGO_SAPLING);

    public static final RegistryObject<FlowerPotBlock> POTTED_CALAMITES_SAPLING = registerPottedSapling("calamites",
            CALAMITES_SAPLING);

    public static final RegistryObject<FlowerPotBlock> POTTED_PHOENIX_SAPLING = registerPottedSapling("phoenix",
            PHOENIX_SAPLING);

    public static final RegistryObject<FlowerPotBlock> POTTED_PSARONIUS_SAPLING = registerPottedSapling("psaronius",
            PSARONIUS_SAPLING);

    public static final RegistryObject<FlowerPotBlock> POTTED_MAGNOLIA_SAPLING = registerPottedSapling("magnolia",
            MAGNOLIA_SAPLING);
    public static RegistryObject<GypsumStoneBlock> GYPSUM_STONE = MOD_BLOCKS.register("gypsum_stone", () -> new GypsumStoneBlock());
    public static final RegistryObject<Block> GYPSUM_STONE_STAIRS = MOD_BLOCKS.register("gypsum_stone_stairs", () -> new StairBlock(() -> GYPSUM_STONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_STAIRS)));
    public static final RegistryObject<Block> GYPSUM_STONE_SLAB = MOD_BLOCKS.register("gypsum_stone_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_SLAB)));
    public static final RegistryObject<Block> GYPSUM_STONE_PRESSURE_PLATE = MOD_BLOCKS.register("gypsum_stone_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.copy(Blocks.STONE_PRESSURE_PLATE), SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF));
    public static final RegistryObject<Block> GYPSUM_STONE_BUTTON = MOD_BLOCKS.register("gypsum_stone_button", () -> new ModStoneButtonBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BUTTON)));
    public static final RegistryObject<Block> GYPSUM_STONE_WALL = MOD_BLOCKS.register("gypsum_stone_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_WALL)));

    public static RegistryObject<Block> GYPSUM_COBBLESTONE = MOD_BLOCKS.register("gypsum_cobblestone", () -> new Block(defaultStone()));
    public static final RegistryObject<Block> GYPSUM_COBBLESTONE_STAIRS = MOD_BLOCKS.register("gypsum_cobblestone_stairs", () -> new StairBlock(() -> GYPSUM_COBBLESTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_STAIRS)));
    public static final RegistryObject<Block> GYPSUM_COBBLESTONE_SLAB = MOD_BLOCKS.register("gypsum_cobblestone_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_SLAB)));
    public static final RegistryObject<Block> GYPSUM_COBBLESTONE_PRESSURE_PLATE = MOD_BLOCKS.register("gypsum_cobblestone_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.copy(Blocks.STONE_PRESSURE_PLATE), SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF));
    public static final RegistryObject<Block> GYPSUM_COBBLESTONE_BUTTON = MOD_BLOCKS.register("gypsum_cobblestone_button", () -> new ModStoneButtonBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BUTTON)));
    public static final RegistryObject<Block> GYPSUM_COBBLESTONE_WALL = MOD_BLOCKS.register("gypsum_cobblestone_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_WALL)));

    public static RegistryObject<Block> GYPSUM_BRICKS = MOD_BLOCKS.register("gypsum_bricks", () -> new Block(defaultStone().strength(2.0f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> GYPSUM_BRICK_STAIRS = MOD_BLOCKS.register("gypsum_brick_stairs", () -> new StairBlock(() -> GYPSUM_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_STAIRS)));
    public static final RegistryObject<Block> GYPSUM_BRICK_SLAB = MOD_BLOCKS.register("gypsum_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_SLAB)));
    public static final RegistryObject<Block> GYPSUM_BRICK_PRESSURE_PLATE = MOD_BLOCKS.register("gypsum_brick_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.copy(Blocks.STONE_PRESSURE_PLATE), SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF));
    public static final RegistryObject<Block> GYPSUM_BRICK_BUTTON = MOD_BLOCKS.register("gypsum_brick_button", () -> new ModStoneButtonBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BUTTON)));
    public static final RegistryObject<Block> GYPSUM_BRICK_WALL = MOD_BLOCKS.register("gypsum_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_WALL)));

    public static final RegistryObject<Block> REINFORCED_STONE = MOD_BLOCKS.register("reinforced_stone", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> REINFORCED_STONE_STAIRS = MOD_BLOCKS.register("reinforced_stone_stairs", () -> new StairBlock(() -> REINFORCED_STONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_STAIRS)));
    public static final RegistryObject<Block> REINFORCED_STONE_SLAB = MOD_BLOCKS.register("reinforced_stone_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_SLAB)));
    public static final RegistryObject<Block> REINFORCED_STONE_PRESSURE_PLATE = MOD_BLOCKS.register("reinforced_stone_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.copy(Blocks.STONE_PRESSURE_PLATE), SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF));
    public static final RegistryObject<Block> REINFORCED_STONE_BUTTON = MOD_BLOCKS.register("reinforced_stone_button", () -> new ModStoneButtonBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BUTTON)));
    public static final RegistryObject<Block> REINFORCED_STONE_WALL = MOD_BLOCKS.register("reinforced_stone_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_WALL)));
    public static final RegistryObject<Block> REINFORCED_BRICKS = MOD_BLOCKS.register("reinforced_bricks", () -> new Block(BlockBehaviour.Properties.of(Material.STONE).strength(2.0f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> REINFORCED_BRICK_STAIRS = MOD_BLOCKS.register("reinforced_brick_stairs", () -> new StairBlock(() -> REINFORCED_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_STAIRS)));
    public static final RegistryObject<Block> REINFORCED_BRICK_SLAB = MOD_BLOCKS.register("reinforced_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_SLAB)));
    public static final RegistryObject<Block> REINFORCED_BRICK_PRESSURE_PLATE = MOD_BLOCKS.register("reinforced_brick_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.copy(Blocks.STONE_PRESSURE_PLATE), SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF));
    public static final RegistryObject<Block> REINFORCED_BRICK_BUTTON = MOD_BLOCKS.register("reinforced_brick_button", () -> new ModStoneButtonBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BUTTON)));
    public static final RegistryObject<Block> REINFORCED_BRICK_WALL = MOD_BLOCKS.register("reinforced_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_WALL)));
    public static final RegistryObject<Block> GYPSUM_COBBLESTONE_PATHWAY = MOD_BLOCKS.register("gypsum_cobblestone_pathway", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> GYPSUM_COBBLESTONE_PATHWAY_STAIRS = MOD_BLOCKS.register("gypsum_cobblestone_pathway_stairs", () -> new StairBlock(() -> GYPSUM_COBBLESTONE_PATHWAY.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_STAIRS)));
    public static final RegistryObject<Block> GYPSUM_COBBLESTONE_PATHWAY_SLAB = MOD_BLOCKS.register("gypsum_cobblestone_pathway_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_SLAB)));
    public static final RegistryObject<Block> GYPSUM_COBBLESTONE_PATHWAY_PRESSURE_PLATE = MOD_BLOCKS.register("gypsum_cobblestone_pathway_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.copy(Blocks.STONE_PRESSURE_PLATE), SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF));
    public static final RegistryObject<Block> GYPSUM_COBBLESTONE_PATHWAY_BUTTON = MOD_BLOCKS.register("gypsum_cobblestone_pathway_button", () -> new ModStoneButtonBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BUTTON)));
    public static final RegistryObject<Block> GYPSUM_COBBLESTONE_PATHWAY_WALL = MOD_BLOCKS.register("gypsum_cobblestone_pathway_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_WALL)));
    public static RegistryObject<SkullDisplayBlock> SKULL_DISPLAY = MOD_BLOCKS.register("skull_display", () -> new SkullDisplayBlock(BlockBehaviour.Properties.of(Material.STONE).strength(0.0F).noOcclusion().sound(SoundType.STONE).noLootTable()));
    public static final RegistryObject<TrashCanBlock> TRASH_CAN = MOD_BLOCKS.register("trash_can", TrashCanBlock::new);
    public static final RegistryObject<Block> GYPSUM_PATHWAY = MOD_BLOCKS.register("gypsum_pathway", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> GYPSUM_PATHWAY_STAIRS = MOD_BLOCKS.register("gypsum_pathway_stairs", () -> new StairBlock(() -> GYPSUM_PATHWAY.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_STAIRS)));
    public static final RegistryObject<Block> GYPSUM_PATHWAY_SLAB = MOD_BLOCKS.register("gypsum_pathway_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_SLAB)));
    public static final RegistryObject<Block> GYPSUM_PATHWAY_PRESSURE_PLATE = MOD_BLOCKS.register("gypsum_pathway_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.copy(Blocks.STONE_PRESSURE_PLATE), SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF));
    public static final RegistryObject<Block> GYPSUM_PATHWAY_BUTTON = MOD_BLOCKS.register("gypsum_pathway_button", () -> new ModStoneButtonBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BUTTON)));
    public static final RegistryObject<Block> GYPSUM_PATHWAY_WALL = MOD_BLOCKS.register("gypsum_pathway_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_WALL)));

    public static final RegistryObject<Block> GYPSUM_MIXED_PATH = MOD_BLOCKS.register("gypsum_mixed_path", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> GYPSUM_MIXED_PATH_STAIRS = MOD_BLOCKS.register("gypsum_mixed_path_stairs", () -> new StairBlock(() -> GYPSUM_MIXED_PATH.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_STAIRS)));
    public static final RegistryObject<Block> GYPSUM_MIXED_PATH_SLAB = MOD_BLOCKS.register("gypsum_mixed_path_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_SLAB)));
    public static final RegistryObject<Block> GYPSUM_MIXED_PATH_PRESSURE_PLATE = MOD_BLOCKS.register("gypsum_mixed_path_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.copy(Blocks.STONE_PRESSURE_PLATE), SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF));
    public static final RegistryObject<Block> GYPSUM_MIXED_PATH_BUTTON = MOD_BLOCKS.register("gypsum_mixed_path_button", () -> new ModStoneButtonBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BUTTON)));
    public static final RegistryObject<Block> GYPSUM_MIXED_PATH_WALL = MOD_BLOCKS.register("gypsum_mixed_path_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_WALL)));

    public static final RegistryObject<Block> GYPSUM_TILES = MOD_BLOCKS.register("gypsum_tiles", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> GYPSUM_TILES_STAIRS = MOD_BLOCKS.register("gypsum_tiles_stairs", () -> new StairBlock(() -> GYPSUM_TILES.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_STAIRS)));
    public static final RegistryObject<Block> GYPSUM_TILES_SLAB = MOD_BLOCKS.register("gypsum_tiles_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_SLAB)));
    public static final RegistryObject<Block> GYPSUM_TILES_PRESSURE_PLATE = MOD_BLOCKS.register("gypsum_tiles_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.copy(Blocks.STONE_PRESSURE_PLATE), SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF));
    public static final RegistryObject<Block> GYPSUM_TILES_BUTTON = MOD_BLOCKS.register("gypsum_tiles_button", () -> new ModStoneButtonBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BUTTON)));
    public static final RegistryObject<Block> GYPSUM_TILES_WALL = MOD_BLOCKS.register("gypsum_tiles_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_WALL)));

    public static final RegistryObject<Block> REFINED_GYPSUM_PANEL = MOD_BLOCKS.register("refined_gypsum_panel", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> REFINED_GYPSUM_PANEL_STAIRS = MOD_BLOCKS.register("refined_gypsum_panel_stairs", () -> new StairBlock(() -> REFINED_GYPSUM_PANEL.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_STAIRS)));
    public static final RegistryObject<Block> REFINED_GYPSUM_PANEL_SLAB = MOD_BLOCKS.register("refined_gypsum_panel_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_SLAB)));
    public static final RegistryObject<Block> REFINED_GYPSUM_PANEL_PRESSURE_PLATE = MOD_BLOCKS.register("refined_gypsum_panel_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.copy(Blocks.STONE_PRESSURE_PLATE), SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF));
    public static final RegistryObject<Block> REFINED_GYPSUM_PANEL_BUTTON = MOD_BLOCKS.register("refined_gypsum_panel_button", () -> new ModStoneButtonBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BUTTON)));
    public static final RegistryObject<Block> REFINED_GYPSUM_PANEL_WALL = MOD_BLOCKS.register("refined_gypsum_panel_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_WALL)));

    public static final RegistryObject<Block> GYPSUM_STONE_PANEL = MOD_BLOCKS.register("gypsum_stone_panel", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> GYPSUM_STONE_PANEL_STAIRS = MOD_BLOCKS.register("gypsum_stone_panel_stairs", () -> new StairBlock(() -> GYPSUM_STONE_PANEL.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_STAIRS)));
    public static final RegistryObject<Block> GYPSUM_STONE_PANEL_SLAB = MOD_BLOCKS.register("gypsum_stone_panel_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_SLAB)));
    public static final RegistryObject<Block> GYPSUM_STONE_PANEL_PRESSURE_PLATE = MOD_BLOCKS.register("gypsum_stone_panel_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.copy(Blocks.STONE_PRESSURE_PLATE), SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF));
    public static final RegistryObject<Block> GYPSUM_STONE_PANEL_BUTTON = MOD_BLOCKS.register("gypsum_stone_panel_button", () -> new ModStoneButtonBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BUTTON)));
    public static final RegistryObject<Block> GYPSUM_STONE_PANEL_WALL = MOD_BLOCKS.register("gypsum_stone_panel_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_WALL)));

    // REINFORCED
    public static final RegistryObject<Block> REINFORCED_STONE_TILES = MOD_BLOCKS.register("reinforced_stone_tiles", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> REINFORCED_STONE_TILES_STAIRS = MOD_BLOCKS.register("reinforced_stone_tiles_stairs", () -> new StairBlock(() -> REINFORCED_STONE_TILES.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_STAIRS)));
    public static final RegistryObject<Block> REINFORCED_STONE_TILES_SLAB = MOD_BLOCKS.register("reinforced_stone_tiles_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_SLAB)));
    public static final RegistryObject<Block> REINFORCED_STONE_TILES_PRESSURE_PLATE = MOD_BLOCKS.register("reinforced_stone_tiles_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.copy(Blocks.STONE_PRESSURE_PLATE), SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF));
    public static final RegistryObject<Block> REINFORCED_STONE_TILES_BUTTON = MOD_BLOCKS.register("reinforced_stone_tiles_button", () -> new ModStoneButtonBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BUTTON)));
    public static final RegistryObject<Block> REINFORCED_STONE_TILES_WALL = MOD_BLOCKS.register("reinforced_stone_tiles_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_WALL)));

    public static final RegistryObject<Block> REINFORCED_STONE_PATHWAY = MOD_BLOCKS.register("reinforced_stone_pathway", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> REINFORCED_STONE_PATHWAY_STAIRS = MOD_BLOCKS.register("reinforced_stone_pathway_stairs", () -> new StairBlock(() -> REINFORCED_STONE_PATHWAY.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_STAIRS)));
    public static final RegistryObject<Block> REINFORCED_STONE_PATHWAY_SLAB = MOD_BLOCKS.register("reinforced_stone_pathway_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_SLAB)));
    public static final RegistryObject<Block> REINFORCED_STONE_PATHWAY_PRESSURE_PLATE = MOD_BLOCKS.register("reinforced_stone_pathway_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.copy(Blocks.STONE_PRESSURE_PLATE), SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF));
    public static final RegistryObject<Block> REINFORCED_STONE_PATHWAY_BUTTON = MOD_BLOCKS.register("reinforced_stone_pathway_button", () -> new ModStoneButtonBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BUTTON)));
    public static final RegistryObject<Block> REINFORCED_STONE_PATHWAY_WALL = MOD_BLOCKS.register("reinforced_stone_pathway_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_WALL)));

    public static final RegistryObject<Block> REINFORCED_STONE_PANEL = MOD_BLOCKS.register("reinforced_stone_panel", () ->
            new Block(BlockBehaviour.Properties.of(Material.STONE).strength(2.0F).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> REINFORCED_STONE_PANEL_STAIRS = MOD_BLOCKS.register("reinforced_stone_panel_stairs", () -> new StairBlock(() -> REINFORCED_STONE_PANEL.get().defaultBlockState(), BlockBehaviour.Properties.copy(Blocks.STONE_STAIRS)));
    public static final RegistryObject<Block> REINFORCED_STONE_PANEL_SLAB = MOD_BLOCKS.register("reinforced_stone_panel_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.STONE_SLAB)));
    public static final RegistryObject<Block> REINFORCED_STONE_PANEL_PRESSURE_PLATE = MOD_BLOCKS.register("reinforced_stone_panel_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.copy(Blocks.STONE_PRESSURE_PLATE), SoundEvents.STONE_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_PRESSURE_PLATE_CLICK_OFF));
    public static final RegistryObject<Block> REINFORCED_STONE_PANEL_BUTTON = MOD_BLOCKS.register("reinforced_stone_panel_button", () -> new ModStoneButtonBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BUTTON)));
    public static final RegistryObject<Block> REINFORCED_STONE_PANEL_WALL = MOD_BLOCKS.register("reinforced_stone_panel_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICK_WALL)));

    public static final RegistryObject<DoorBlock> REINFORCED_DOOR = MOD_BLOCKS.register("reinforced_door", () -> new DoorBlock(BlockBehaviour.Properties.of(Material.METAL)
            .strength(3.0F, 5.0F)
            .requiresCorrectToolForDrops()
            .sound(SoundType.STONE)
            .noOcclusion(), SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN));
    public static final RegistryObject<DoorBlock> SECURITY_DOOR = MOD_BLOCKS.register("security_door", () -> new DoorBlock(BlockBehaviour.Properties.of(Material.METAL)
            .strength(3.0F, 5.0F)
            .requiresCorrectToolForDrops()
            .sound(SoundType.STONE)
            .noOcclusion(), SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN));

    public static RegistryObject<CultivatorTopBlock> CULTIVATE_TOP = MOD_BLOCKS.register("cultivate_top", () -> new CultivatorTopBlock(defaultMachine().noOcclusion()));

    public static RegistryObject<CultivatorBottomBlock> CULTIVATE_BOTTOM = MOD_BLOCKS.register("cultivate_bottom", () -> new CultivatorBottomBlock(defaultMachine().noOcclusion()));
    public static RegistryObject<ClearGlassBlock> CLEAR_GLASS = MOD_BLOCKS.register("clear_glass", () -> new ClearGlassBlock(BlockBehaviour.Properties.of(Material.GLASS).strength(0.2F).noOcclusion()));
    public static final RegistryObject<ClearGlassPaneBlock> CLEAR_GLASS_PANE = MOD_BLOCKS.register("clear_glass_pane", () -> new ClearGlassPaneBlock(BlockBehaviour.Properties.of(Material.GLASS).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));

    public static RegistryObject<FossilizedTrackwayBlock> FOSSILIZED_TRACKWAY_RAPTOR = MOD_BLOCKS.register("fossilized_trackway_raptor", () -> new FossilizedTrackwayBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F).requiresCorrectToolForDrops()));
    public static RegistryObject<FossilizedTrackwayBlock> FOSSILIZED_TRACKWAY_BIPED_SMALL = MOD_BLOCKS.register("fossilized_trackway_biped_small", () -> new FossilizedTrackwayBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F).requiresCorrectToolForDrops()));
    public static RegistryObject<FossilizedTrackwayBlock> FOSSILIZED_TRACKWAY_BIPED_MEDIUM = MOD_BLOCKS.register("fossilized_trackway_biped_medium", () -> new FossilizedTrackwayBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5F).requiresCorrectToolForDrops()));

    public static RegistryObject<NestFossilBlock> NEST_FOSSIL = MOD_BLOCKS.register("nest_fossil", () -> new NestFossilBlock (BlockBehaviour.Properties.of(Material.STONE).strength(1.5F).requiresCorrectToolForDrops()));
//    public static RegistryObject<NestFossilBlock> ENCASED_NEST_FOSSIL = modBlocks.register("encased_nest_fossil", () -> new NestFossilBlock(true, defaultMachine()));

    //plants
    public static RegistryObject<DoublePlantBlock> DICKSONIA = MOD_BLOCKS.register("dicksonia", () -> new DoublePlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));
    public static RegistryObject<DoublePlantBlock> DICROIDIUM_ZUBERI = MOD_BLOCKS.register("dicroidium_zuberi", () -> new DoublePlantBlock( BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));
    public static RegistryObject<AncientCrop> AJUGINUCULA_SMITHII = MOD_BLOCKS.register("ajuginucula_smithii", () -> new SevenStageAncientCrop(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));
    public static RegistryObject<AncientCrop> WILD_ONION = MOD_BLOCKS.register("wild_onion_plant", () -> new AncientCrop(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));
    public static RegistryObject<GracilariaBlock> GRACILARIA = MOD_BLOCKS.register("gracilaria", () -> new GracilariaBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_WATER_PLANT).noCollission().sound(SoundType.WET_GRASS)));
    public static RegistryObject<SmallPlantBlock> DICTYOPHYLLUM = MOD_BLOCKS.register("dictyophyllum", () -> new SmallPlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));
    public static RegistryObject<WestIndianLilacBlock> WEST_INDIAN_LILAC = MOD_BLOCKS.register("west_indian_lilac", () ->new WestIndianLilacBlock());
    public static RegistryObject<DoublePlantBlock> SERENNA_VERIFORMANS = MOD_BLOCKS.register("serenna_veriformans", () -> new DoublePlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));
    public static RegistryObject<SmallPlantBlock> LADINIA_SIMPLEX = MOD_BLOCKS.register("ladinia_simplex", () -> new SmallPlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));
    public static RegistryObject<SmallPlantBlock> ORONTIUM_MACKII = MOD_BLOCKS.register("orontium_mackii", () -> new SmallPlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));
    public static RegistryObject<DoublePlantBlock> UMALTOLEPIS = MOD_BLOCKS.register("umaltolepis", () -> new DoublePlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));
    public static RegistryObject<DoublePlantBlock> LIRIODENDRITES = MOD_BLOCKS.register("liriodendrites", () -> new DoublePlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));
    public static RegistryObject<SmallPlantBlock> RAPHAELIA = MOD_BLOCKS.register("raphaelia", () -> new SmallPlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));
    public static RegistryObject<DoublePlantBlock> ENCEPHALARTOS = MOD_BLOCKS.register("encephalartos", () -> new DoublePlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));
    public static RegistryObject<AncientCrop> WILD_POTATO_PLANT = MOD_BLOCKS.register("wild_potato_plant", () -> new AncientCrop(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));
    public static RegistryObject<AncientCrop> RHAMNUS_SALICIFOLIUS = MOD_BLOCKS.register("rhamnus_salicifolius", () -> new SevenStageAncientCrop(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));
    public static RegistryObject<SmallPlantBlock> CINNAMON_FERN = MOD_BLOCKS.register("cinnamon_fern",  () -> new SmallPlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));;
    public static RegistryObject<SmallPlantBlock> BRISTLE_FERN = MOD_BLOCKS.register("bristle_fern",  () -> new SmallPlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));;
    public static RegistryObject<DoublePlantBlock> TEMPSKYA = MOD_BLOCKS.register("tempskya", () -> new DoublePlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));
    public static RegistryObject<SmallPlantBlock> WOOLLY_STALKED_BEGONIA = MOD_BLOCKS.register("woolly_stalked_begonia",  () -> new SmallPlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));;
    public static RegistryObject<SmallPlantBlock> LARGESTIPULE_LEATHER_ROOT = MOD_BLOCKS.register("largestipule_leather_root",  () -> new SmallPlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));;
    public static RegistryObject<DoublePlantBlock> RHACOPHYTON = MOD_BLOCKS.register("rhacophyton", () -> new DoublePlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));;
    public static RegistryObject<DoublePlantBlock> GRAMINIDITES_BAMBUSOIDES = MOD_BLOCKS.register("graminidites_bambusoides", () -> new DoublePlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));
    public static RegistryObject<HeliconiaBlock> HELICONIA = MOD_BLOCKS.register("heliconia", HeliconiaBlock::new);
    public static RegistryObject<PeatBlock> PEAT = MOD_BLOCKS.register("peat", () -> new PeatBlock(defaultMoss().randomTicks()));
    public static RegistryObject<Block> PEAT_MOSS = MOD_BLOCKS.register("peat_moss", () -> new Block(defaultMoss().randomTicks()));
    public static RegistryObject<SmallPlantBlock> SMALL_ROYAL_FERN = MOD_BLOCKS.register("small_royal_fern",  () -> new SmallPlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));;
    public static RegistryObject<SmallPlantBlock> SMALL_CHAIN_FERN = MOD_BLOCKS.register("small_chain_fern",  () -> new SmallPlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));;
    public static RegistryObject<SmallPlantBlock> SMALL_CYCAD = MOD_BLOCKS.register("small_cycad",  () -> new SmallPlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));;
    public static RegistryObject<SmallPlantBlock> CYCADEOIDEA = MOD_BLOCKS.register("bennettitalean_cycadeoidea",  () -> new SmallPlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));;
    public static RegistryObject<SmallPlantBlock> CRY_PANSY = MOD_BLOCKS.register("cry_pansy",  () -> new SmallPlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));;
    public static RegistryObject<DoublePlantBlock> SCALY_TREE_FERN = MOD_BLOCKS.register("scaly_tree_fern", () -> new DoublePlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));
    public static RegistryObject<DoublePlantBlock> ZAMITES = MOD_BLOCKS.register("cycad_zamites", () -> new DoublePlantBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().sound(SoundType.GRASS)));
    // Dead Coral Blocks
    public static final RegistryObject<BaseCoralPlantBlock> DEAD_ENALLHELIA =
            MOD_BLOCKS.register("dead_enallhelia", () -> new BaseCoralPlantBlock(defaultDeadCoral()));

    public static final RegistryObject<BaseCoralPlantBlock> DEAD_AULOPORA =
            MOD_BLOCKS.register("dead_aulopora", () -> new BaseCoralPlantBlock(defaultDeadCoral()));

    public static final RegistryObject<BaseCoralPlantBlock> DEAD_CLADOCHONUS =
            MOD_BLOCKS.register("dead_cladochonus", () -> new BaseCoralPlantBlock(defaultDeadCoral()));

    public static final RegistryObject<BaseCoralPlantBlock> DEAD_LITHOSTROTION =
            MOD_BLOCKS.register("dead_lithostrotion", () -> new BaseCoralPlantBlock(defaultDeadCoral()));

    public static final RegistryObject<BaseCoralPlantBlock> DEAD_STYLOPHYLLOPSIS =
            MOD_BLOCKS.register("dead_stylophyllopsis", () -> new BaseCoralPlantBlock(defaultDeadCoral()));

    public static final RegistryObject<BaseCoralPlantBlock> DEAD_HIPPURITES_RADIOSUS =
            MOD_BLOCKS.register("dead_hippurites_radiosus", () -> new BaseCoralPlantBlock(defaultDeadCoral()));
    // Live Coral Blocks (linked to the dead ones)
    public static RegistryObject<AncientCoralBlock> ENALLHELIA = MOD_BLOCKS.register("enallhelia", () -> new AncientCoralBlock(DEAD_ENALLHELIA.get(), defaultAncientCoral()));
    public static RegistryObject<AncientCoralBlock> AULOPORA = MOD_BLOCKS.register("aulopora", () -> new AncientCoralBlock(DEAD_AULOPORA.get(), defaultAncientCoral()));
    public static RegistryObject<AncientCoralBlock> CLADOCHONUS = MOD_BLOCKS.register("cladochonus", () -> new AncientCoralBlock(DEAD_CLADOCHONUS.get(), defaultAncientCoral()));
    public static RegistryObject<AncientCoralBlock> LITHOSTROTION = MOD_BLOCKS.register("lithostrotion", () -> new AncientCoralBlock(DEAD_LITHOSTROTION.get(), defaultAncientCoral()));
    public static RegistryObject<AncientCoralBlock> STYLOPHYLLOPSIS = MOD_BLOCKS.register("stylophyllopsis", () -> new AncientCoralBlock(DEAD_STYLOPHYLLOPSIS.get(), defaultAncientCoral()));
    public static RegistryObject<AncientCoralBlock> HIPPURITES_RADIOSUS = MOD_BLOCKS.register("hippurites_radiosus", () -> new AncientCoralBlock(DEAD_HIPPURITES_RADIOSUS.get(), defaultAncientCoral()));
    public static RegistryObject<FeederBlock> FEEDER = MOD_BLOCKS.register("feeder", () -> new FeederBlock(defaultMachine()));
    public static RegistryObject<BugCrate> BUG_CRATE = MOD_BLOCKS.register("bug_crate", BugCrate::new);
    public static RegistryObject<Block> PARK_BENCH = MOD_BLOCKS.register("park_bench", () -> new ParkBenchBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().strength(1.5F).sound(SoundType.WOOD)));
    public static RegistryObject<SkeletonAssemblyBlock> SKELETON_ASSEMBLY = MOD_BLOCKS.register("skeleton_assembly", SkeletonAssemblyBlock::new);

    public static RegistryObject<SwarmBlock> KRILL_SWARM = MOD_BLOCKS.register("krill_swarm", () -> new SwarmBlock(ModItems.KRILL, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().randomTicks()));
    public static RegistryObject<SwarmBlock> PLANKTON_SWARM = MOD_BLOCKS.register("plankton_swarm", () -> new SwarmBlock(ModItems.PLANKTON, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().randomTicks()));
    public static RegistryObject<TourRailBlock> TOUR_RAIL = MOD_BLOCKS.register("tour_rail", () -> new TourRailBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL).requiresCorrectToolForDrops().strength(1), TourRailBlock.SpeedType.NONE));
    public static RegistryObject<TourRailBlock> TOUR_RAIL_SLOW = MOD_BLOCKS.register("tour_rail_slow", () -> new TourRailBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL).requiresCorrectToolForDrops().strength(1), TourRailBlock.SpeedType.SLOW));
    public static RegistryObject<TourRailBlock> TOUR_RAIL_MEDIUM = MOD_BLOCKS.register("tour_rail_medium", () -> new TourRailBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL).requiresCorrectToolForDrops().strength(1), TourRailBlock.SpeedType.MEDIUM));
    public static RegistryObject<TourRailBlock> TOUR_RAIL_FAST = MOD_BLOCKS.register("tour_rail_fast", () -> new TourRailBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL).requiresCorrectToolForDrops().strength(1), TourRailBlock.SpeedType.FAST));
    public static RegistryObject<PaleoBaleBlock> PALEO_BALE_CYCADEOIDEA = MOD_BLOCKS.register("paleo_bale_cycadeoidea", () -> new PaleoBaleBlock(PaleoBaleBlock.Variant.CYCADEOIDEA, BlockBehaviour.Properties.of(Material.GRASS).sound(SoundType.GRASS)));
    public static RegistryObject<PaleoBaleBlock> PALEO_BALE_CYCAD = MOD_BLOCKS.register("paleo_bale_cycad", () -> new PaleoBaleBlock(PaleoBaleBlock.Variant.CYCAD, BlockBehaviour.Properties.of(Material.GRASS).sound(SoundType.GRASS)));
    public static RegistryObject<PaleoBaleBlock> PALEO_BALE_FERN = MOD_BLOCKS.register("paleo_bale_fern", () -> new PaleoBaleBlock(PaleoBaleBlock.Variant.FERN, BlockBehaviour.Properties.of(Material.GRASS).sound(SoundType.GRASS)));
    public static RegistryObject<PaleoBaleBlock> PALEO_BALE_LEAVES = MOD_BLOCKS.register("paleo_bale_leaves", () -> new PaleoBaleBlock(PaleoBaleBlock.Variant.LEAVES, BlockBehaviour.Properties.of(Material.GRASS).sound(SoundType.GRASS)));
    public static RegistryObject<PaleoBaleBlock> PALEO_BALE_OTHER = MOD_BLOCKS.register("paleo_bale_other", () -> new PaleoBaleBlock(PaleoBaleBlock.Variant.OTHER, BlockBehaviour.Properties.of(Material.GRASS).sound(SoundType.GRASS)));
    public static RegistryObject<ElectricFenceBaseBlock> LOW_SECURITY_FENCE_BASE = MOD_BLOCKS.register("low_security_fence_base", () -> new ElectricFenceBaseBlock(FenceType.LOW));
    public static RegistryObject<ElectricFenceBaseBlock> MED_SECURITY_FENCE_BASE = MOD_BLOCKS.register("med_security_fence_base", () -> new ElectricFenceBaseBlock(FenceType.MED));
    public static RegistryObject<ElectricFenceBaseBlock> HIGH_SECURITY_FENCE_BASE = MOD_BLOCKS.register("high_security_fence_base", () -> new ElectricFenceBaseBlock(FenceType.HIGH));

    public static RegistryObject<ElectricFenceWireBlock> LOW_SECURITY_FENCE_WIRE = MOD_BLOCKS.register("low_security_fence_wire", () -> new ElectricFenceWireBlock(FenceType.LOW, defaultMachine()));
    public static RegistryObject<ElectricFenceWireBlock> MED_SECURITY_FENCE_WIRE = MOD_BLOCKS.register("med_security_fence_wire", () -> new ElectricFenceWireBlock(FenceType.MED, defaultMachine()));
    public static RegistryObject<ElectricFenceWireBlock> HIGH_SECURITY_FENCE_WIRE = MOD_BLOCKS.register("high_security_fence_wire", () -> new ElectricFenceWireBlock(FenceType.HIGH, defaultMachine()));

    public static RegistryObject<ElectricFencePoleBlock> LOW_SECURITY_FENCE_POLE = MOD_BLOCKS.register("low_security_fence_pole", () -> new ElectricFencePoleBlock(FenceType.LOW, defaultMachine()));
    public static RegistryObject<ElectricFencePoleBlock> MED_SECURITY_FENCE_POLE = MOD_BLOCKS.register("med_security_fence_pole", () -> new ElectricFencePoleBlock(FenceType.MED, defaultMachine()));
    public static RegistryObject<ElectricFencePoleBlock> HIGH_SECURITY_FENCE_POLE = MOD_BLOCKS.register("high_security_fence_pole", () -> new ElectricFencePoleBlock(FenceType.HIGH, defaultMachine()));

    public static RegistryObject<ActionFigureBlock> DISPLAY_BLOCK = MOD_BLOCKS.register("display_block", () -> new ActionFigureBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(0.0F, 0.0F).randomTicks().sound(SoundType.WOOD)));
    public static RegistryObject<HologramBlock> HOLOGRAM_BLOCK = MOD_BLOCKS.register("hologram_block", () -> new HologramBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion().strength(1.0F).sound(SoundType.METAL)));
    public static void register(IEventBus bus){
        MOD_BLOCKS.register(bus);
        //today I learned that DeferredRegister#getEntries() is a method
        //that's gonna be useful for items
        //really useful
        //-gamma
    }

    private static RegistryObject<FlowerPotBlock> registerPottedSapling(String name,
                                                                        Supplier<? extends Block> saplingSupplier) {
        return MOD_BLOCKS.register("potted_" + name + "_sapling",
                () -> new FlowerPotBlock(DEFAULT_FLOWER_POT, saplingSupplier,
                        BlockBehaviour.Properties.copy(Blocks.POTTED_OAK_SAPLING)));
    }
    //todo these should be fields in Dinosaur, not a giant if/else branch
    public static Block getEncasedBlockFor(Dinosaur dino) {
        if (dino.equals(DinosaurHandler.ACHILLOBATOR)) return ModBlocks.ENCASED_ACHILLOBATOR_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ALLOSAURUS)) return ModBlocks.ENCASED_ALLOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ALLIGATOR_GAR)) return ModBlocks.ENCASED_ALLIGATOR_GAR_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ALVAREZSAURUS)) return ModBlocks.ENCASED_ALVAREZSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ANKYLOSAURUS)) return ModBlocks.ENCASED_ANKYLOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.APATOSAURUS)) return ModBlocks.ENCASED_APATOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ARSINOITHERIUM)) return ModBlocks.ENCASED_ARSINOITHERIUM_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ASTEROCERAS)) return ModBlocks.ENCASED_ASTEROCERAS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.BARYONYX)) return ModBlocks.ENCASED_BARYONYX_FOSSIL.get();
        if (dino.equals(DinosaurHandler.BEELZEBUFO)) return ModBlocks.ENCASED_BEELZEBUFO_FOSSIL.get();
        if (dino.equals(DinosaurHandler.BRACHIOSAURUS)) return ModBlocks.ENCASED_BRACHIOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CAMARASAURUS)) return ModBlocks.ENCASED_CAMARASAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CARCHARODONTOSAURUS)) return ModBlocks.ENCASED_CARCHARODONTOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CARNOTAURUS)) return ModBlocks.ENCASED_CARNOTAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.NIGERSAURUS)) return ModBlocks.ENCASED_NIGERSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CEARADACTYLUS)) return ModBlocks.ENCASED_CEARADACTYLUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CERATOSAURUS)) return ModBlocks.ENCASED_CERATOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CHASMOSAURUS)) return ModBlocks.ENCASED_CHASMOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CHILESAURUS)) return ModBlocks.ENCASED_CHILESAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.COELACANTH)) return ModBlocks.ENCASED_COELACANTH_FOSSIL.get();
        if (dino.equals(DinosaurHandler.COELURUS)) return ModBlocks.ENCASED_COELURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.COMPSOGNATHUS)) return ModBlocks.ENCASED_COMPSOGNATHUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CORYTHOSAURUS)) return ModBlocks.ENCASED_CORYTHOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CRASSIGYRINUS)) return ModBlocks.ENCASED_CRASSIGYRINUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.DEINOTHERIUM)) return ModBlocks.ENCASED_DEINOTHERIUM_FOSSIL.get();
        if (dino.equals(DinosaurHandler.DILOPHOSAURUS)) return ModBlocks.ENCASED_DILOPHOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.DIMETRODON)) return ModBlocks.ENCASED_DIMETRODON_FOSSIL.get();
        if (dino.equals(DinosaurHandler.DIMORPHODON)) return ModBlocks.ENCASED_DIMORPHODON_FOSSIL.get();
        if (dino.equals(DinosaurHandler.DIPLOCAULUS)) return ModBlocks.ENCASED_DIPLOCAULUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.DIPLODOCUS)) return ModBlocks.ENCASED_DIPLODOCUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.DODO)) return ModBlocks.ENCASED_DODO_FOSSIL.get();
        if (dino.equals(DinosaurHandler.DREADNOUGHTUS)) return ModBlocks.ENCASED_DREADNOUGHTUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.DUNKLEOSTEUS)) return ModBlocks.ENCASED_DUNKLEOSTEUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.EDMONTOSAURUS)) return ModBlocks.ENCASED_EDMONTOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ELASMOTHERIUM)) return ModBlocks.ENCASED_ELASMOTHERIUM_FOSSIL.get();
        if (dino.equals(DinosaurHandler.GALLIMIMUS)) return ModBlocks.ENCASED_GALLIMIMUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.GIGANOTOSAURUS)) return ModBlocks.ENCASED_GIGANOTOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.GUANLONG)) return ModBlocks.ENCASED_GUANLONG_FOSSIL.get();
        if (dino.equals(DinosaurHandler.HERRERASAURUS)) return ModBlocks.ENCASED_HERRERASAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.HYAENODON)) return ModBlocks.ENCASED_HYAENODON_FOSSIL.get();
        if (dino.equals(DinosaurHandler.HYPSILOPHODON)) return ModBlocks.ENCASED_HYPSILOPHODON_FOSSIL.get();
        if (dino.equals(DinosaurHandler.LAMBEOSAURUS)) return ModBlocks.ENCASED_LAMBEOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.LEAELLYNASAURA)) return ModBlocks.ENCASED_LEAELLYNASAURA_FOSSIL.get();
        if (dino.equals(DinosaurHandler.LEPTICTIDIUM)) return ModBlocks.ENCASED_LEPTICTIDIUM_FOSSIL.get();
        if (dino.equals(DinosaurHandler.LUDODACTYLUS)) return ModBlocks.ENCASED_LUDODACTYLUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MAJUNGASAURUS)) return ModBlocks.ENCASED_MAJUNGASAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MAMENCHISAURUS)) return ModBlocks.ENCASED_MAMENCHISAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MAMMOTH)) return ModBlocks.ENCASED_MAMMOTH_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MAWSONIA)) return ModBlocks.ENCASED_MAWSONIA_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MEGAPIRANHA)) return ModBlocks.ENCASED_MEGAPIRANHA_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MEGATHERIUM)) return ModBlocks.ENCASED_MEGATHERIUM_FOSSIL.get();
        if (dino.equals(DinosaurHandler.METRIACANTHOSAURUS)) return ModBlocks.ENCASED_METRIACANTHOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MICROCERATUS)) return ModBlocks.ENCASED_MICROCERATUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MICRORAPTOR)) return ModBlocks.ENCASED_MICRORAPTOR_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MOGANOPTERUS)) return ModBlocks.ENCASED_MOGANOPTERUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MOSASAURUS)) return ModBlocks.ENCASED_MOSASAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MUSSAURUS)) return ModBlocks.ENCASED_MUSSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ORNITHOMIMUS)) return ModBlocks.ENCASED_ORNITHOMIMUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.OTHNIELIA)) return ModBlocks.ENCASED_OTHNIELIA_FOSSIL.get();
        if (dino.equals(DinosaurHandler.OVIRAPTOR)) return ModBlocks.ENCASED_OVIRAPTOR_FOSSIL.get();
        if (dino.equals(DinosaurHandler.PACHYCEPHALOSAURUS)) return ModBlocks.ENCASED_PACHYCEPHALOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.PARACERATHERIUM)) return ModBlocks.ENCASED_PARACERATHERIUM_FOSSIL.get();
        if (dino.equals(DinosaurHandler.PARAPUZOSIA)) return ModBlocks.ENCASED_PARAPUZOSIA_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ORTHOCERAS)) return ModBlocks.ENCASED_ORTHOCERAS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CAMEROCERAS)) return ModBlocks.ENCASED_CAMEROCERAS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.LIVYATAN)) return ModBlocks.ENCASED_LIVYATAN_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MEGALODON)) return ModBlocks.ENCASED_MEGALODON_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CALYMENE)) return ModBlocks.ENCASED_CALYMENE_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ENDOCERAS)) return ModBlocks.ENCASED_ENDOCERAS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.PARASAUROLOPHUS)) return ModBlocks.ENCASED_PARASAUROLOPHUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.PERISPHINCTES)) return ModBlocks.ENCASED_PERISPHINCTES_FOSSIL.get();
        if (dino.equals(DinosaurHandler.POSTOSUCHUS)) return ModBlocks.ENCASED_POSTOSUCHUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.PROCERATOSAURUS)) return ModBlocks.ENCASED_PROCERATOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.PROTOCERATOPS)) return ModBlocks.ENCASED_PROTOCERATOPS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.PTERANODON)) return ModBlocks.ENCASED_PTERANODON_FOSSIL.get();
        if (dino.equals(DinosaurHandler.QUETZAL)) return ModBlocks.ENCASED_QUETZAL_FOSSIL.get();
        if (dino.equals(DinosaurHandler.RUGOPS)) return ModBlocks.ENCASED_RUGOPS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.SEGISAURUS)) return ModBlocks.ENCASED_SEGISAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.SINOCERATOPS)) return ModBlocks.ENCASED_SINOCERATOPS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.SMILODON)) return ModBlocks.ENCASED_SMILODON_FOSSIL.get();
        if (dino.equals(DinosaurHandler.SPINOSAURUS)) return ModBlocks.ENCASED_SPINOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.STEGOSAURUS)) return ModBlocks.ENCASED_STEGOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.STYRACOSAURUS)) return ModBlocks.ENCASED_STYRACOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.SUCHOMIMUS)) return ModBlocks.ENCASED_SUCHOMIMUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.THERIZINOSAURUS)) return ModBlocks.ENCASED_THERIZINOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.TITANIS)) return ModBlocks.ENCASED_TITANIS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.TITANITES)) return ModBlocks.ENCASED_TITANITES_FOSSIL.get();
        if (dino.equals(DinosaurHandler.TRICERATOPS)) return ModBlocks.ENCASED_TRICERATOPS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.TROODON)) return ModBlocks.ENCASED_TROODON_FOSSIL.get();
        if (dino.equals(DinosaurHandler.TROPEOGNATHUS)) return ModBlocks.ENCASED_TROPEOGNATHUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.TYLOSAURUS)) return ModBlocks.ENCASED_TYLOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.TYRANNOSAURUS)) return ModBlocks.ENCASED_TYRANNOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.VECTIPELTA)) return ModBlocks.ENCASED_VECTIPELTA_FOSSIL.get();
        if (dino.equals(DinosaurHandler.VELOCIRAPTOR)) return ModBlocks.ENCASED_VELOCIRAPTOR_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ZHENYUANOPTERUS)) return ModBlocks.ENCASED_ZHENYUANOPTERUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.DEINOSUCHUS)) return ModBlocks.ENCASED_DEINOSUCHUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.KAIRUKU)) return ModBlocks.ENCASED_KAIRUKU_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MAIASAURA)) return ModBlocks.ENCASED_MAIASAURA_FOSSIL.get();
        if (dino.equals(DinosaurHandler.PATAGOTITAN)) return ModBlocks.ENCASED_PATAGOTITAN_FOSSIL.get();

        return null;
    }
    public static Block getFossilBlockFor(Dinosaur dino) {
        if (dino.equals(DinosaurHandler.ACHILLOBATOR)) return ModBlocks.ACHILLOBATOR_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ALLOSAURUS)) return ModBlocks.ALLOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ALLIGATOR_GAR)) return ModBlocks.ALLIGATOR_GAR_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ALVAREZSAURUS)) return ModBlocks.ALVAREZSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ANKYLOSAURUS)) return ModBlocks.ANKYLOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.APATOSAURUS)) return ModBlocks.APATOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ARSINOITHERIUM)) return ModBlocks.ARSINOITHERIUM_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ASTEROCERAS)) return ModBlocks.ASTEROCERAS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.BARYONYX)) return ModBlocks.BARYONYX_FOSSIL.get();
        if (dino.equals(DinosaurHandler.BEELZEBUFO)) return ModBlocks.BEELZEBUFO_FOSSIL.get();
        if (dino.equals(DinosaurHandler.BRACHIOSAURUS)) return ModBlocks.BRACHIOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CAMARASAURUS)) return ModBlocks.CAMARASAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CARCHARODONTOSAURUS)) return ModBlocks.CARCHARODONTOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CARNOTAURUS)) return ModBlocks.CARNOTAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CEARADACTYLUS)) return ModBlocks.CEARADACTYLUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CERATOSAURUS)) return ModBlocks.CERATOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CHASMOSAURUS)) return ModBlocks.CHASMOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CHILESAURUS)) return ModBlocks.CHILESAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.NIGERSAURUS)) return ModBlocks.NIGERSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.COELACANTH)) return ModBlocks.COELACANTH_FOSSIL.get();
        if (dino.equals(DinosaurHandler.COELURUS)) return ModBlocks.COELURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.COMPSOGNATHUS)) return ModBlocks.COMPSOGNATHUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CORYTHOSAURUS)) return ModBlocks.CORYTHOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CRASSIGYRINUS)) return ModBlocks.CRASSIGYRINUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.DEINOTHERIUM)) return ModBlocks.DEINOTHERIUM_FOSSIL.get();
        if (dino.equals(DinosaurHandler.DILOPHOSAURUS)) return ModBlocks.DILOPHOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.DIMETRODON)) return ModBlocks.DIMETRODON_FOSSIL.get();
        if (dino.equals(DinosaurHandler.DIMORPHODON)) return ModBlocks.DIMORPHODON_FOSSIL.get();
        if (dino.equals(DinosaurHandler.DIPLOCAULUS)) return ModBlocks.DIPLOCAULUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.DIPLODOCUS)) return ModBlocks.DIPLODOCUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.DODO)) return ModBlocks.DODO_FOSSIL.get();
        if (dino.equals(DinosaurHandler.DREADNOUGHTUS)) return ModBlocks.DREADNOUGHTUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.DUNKLEOSTEUS)) return ModBlocks.DUNKLEOSTEUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.DEINOSUCHUS)) return ModBlocks.DEINOSUCHUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.EDMONTOSAURUS)) return ModBlocks.EDMONTOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ELASMOTHERIUM)) return ModBlocks.ELASMOTHERIUM_FOSSIL.get();
        if (dino.equals(DinosaurHandler.GALLIMIMUS)) return ModBlocks.GALLIMIMUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.GIGANOTOSAURUS)) return ModBlocks.GIGANOTOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.GUANLONG)) return ModBlocks.GUANLONG_FOSSIL.get();
        if (dino.equals(DinosaurHandler.HERRERASAURUS)) return ModBlocks.HERRERASAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.HYAENODON)) return ModBlocks.HYAENODON_FOSSIL.get();
        if (dino.equals(DinosaurHandler.HYPSILOPHODON)) return ModBlocks.HYPSILOPHODON_FOSSIL.get();
        if (dino.equals(DinosaurHandler.KAIRUKU)) return ModBlocks.KAIRUKU_FOSSIL.get();
        if (dino.equals(DinosaurHandler.LAMBEOSAURUS)) return ModBlocks.LAMBEOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.LEAELLYNASAURA)) return ModBlocks.LEAELLYNASAURA_FOSSIL.get();
        if (dino.equals(DinosaurHandler.LEPTICTIDIUM)) return ModBlocks.LEPTICTIDIUM_FOSSIL.get();
        if (dino.equals(DinosaurHandler.LUDODACTYLUS)) return ModBlocks.LUDODACTYLUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MAJUNGASAURUS)) return ModBlocks.MAJUNGASAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MAMENCHISAURUS)) return ModBlocks.MAMENCHISAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MAMMOTH)) return ModBlocks.MAMMOTH_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MAWSONIA)) return ModBlocks.MAWSONIA_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MEGAPIRANHA)) return ModBlocks.MEGAPIRANHA_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MEGATHERIUM)) return ModBlocks.MEGATHERIUM_FOSSIL.get();
        if (dino.equals(DinosaurHandler.METRIACANTHOSAURUS)) return ModBlocks.METRIACANTHOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MICROCERATUS)) return ModBlocks.MICROCERATUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MICRORAPTOR)) return ModBlocks.MICRORAPTOR_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MOGANOPTERUS)) return ModBlocks.MOGANOPTERUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MOSASAURUS)) return ModBlocks.MOSASAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MUSSAURUS)) return ModBlocks.MUSSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ORNITHOMIMUS)) return ModBlocks.ORNITHOMIMUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.OTHNIELIA)) return ModBlocks.OTHNIELIA_FOSSIL.get();
        if (dino.equals(DinosaurHandler.OVIRAPTOR)) return ModBlocks.OVIRAPTOR_FOSSIL.get();
        if (dino.equals(DinosaurHandler.PACHYCEPHALOSAURUS)) return ModBlocks.PACHYCEPHALOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.PARACERATHERIUM)) return ModBlocks.PARACERATHERIUM_FOSSIL.get();
        if (dino.equals(DinosaurHandler.PARAPUZOSIA)) return ModBlocks.PARAPUZOSIA_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ORTHOCERAS)) return ModBlocks.ORTHOCERAS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CAMEROCERAS)) return ModBlocks.CAMEROCERAS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ENDOCERAS)) return ModBlocks.ENDOCERAS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.PARASAUROLOPHUS)) return ModBlocks.PARASAUROLOPHUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.PERISPHINCTES)) return ModBlocks.PERISPHINCTES_FOSSIL.get();
        if (dino.equals(DinosaurHandler.POSTOSUCHUS)) return ModBlocks.POSTOSUCHUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.PROCERATOSAURUS)) return ModBlocks.PROCERATOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.PROTOCERATOPS)) return ModBlocks.PROTOCERATOPS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.PTERANODON)) return ModBlocks.PTERANODON_FOSSIL.get();
        if (dino.equals(DinosaurHandler.QUETZAL)) return ModBlocks.QUETZAL_FOSSIL.get();
        if (dino.equals(DinosaurHandler.RUGOPS)) return ModBlocks.RUGOPS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.SEGISAURUS)) return ModBlocks.SEGISAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.SINOCERATOPS)) return ModBlocks.SINOCERATOPS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.SMILODON)) return ModBlocks.SMILODON_FOSSIL.get();
        if (dino.equals(DinosaurHandler.SPINOSAURUS)) return ModBlocks.SPINOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.STEGOSAURUS)) return ModBlocks.STEGOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.STYRACOSAURUS)) return ModBlocks.STYRACOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.SUCHOMIMUS)) return ModBlocks.SUCHOMIMUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.THERIZINOSAURUS)) return ModBlocks.THERIZINOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.TITANIS)) return ModBlocks.TITANIS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.TITANITES)) return ModBlocks.TITANITES_FOSSIL.get();
        if (dino.equals(DinosaurHandler.TRICERATOPS)) return ModBlocks.TRICERATOPS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.TROODON)) return ModBlocks.TROODON_FOSSIL.get();
        if (dino.equals(DinosaurHandler.TROPEOGNATHUS)) return ModBlocks.TROPEOGNATHUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.TYLOSAURUS)) return ModBlocks.TYLOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.TYRANNOSAURUS)) return ModBlocks.TYRANNOSAURUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.VECTIPELTA)) return ModBlocks.VECTIPELTA_FOSSIL.get();
        if (dino.equals(DinosaurHandler.VELOCIRAPTOR)) return ModBlocks.VELOCIRAPTOR_FOSSIL.get();
        if (dino.equals(DinosaurHandler.ZHENYUANOPTERUS)) return ModBlocks.ZHENYUANOPTERUS_FOSSIL.get();
        if (dino.equals(DinosaurHandler.LIVYATAN)) return ModBlocks.LIVYATAN_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MEGALODON)) return ModBlocks.MEGALODON_FOSSIL.get();
        if (dino.equals(DinosaurHandler.CALYMENE)) return ModBlocks.CALYMENE_FOSSIL.get();
        if (dino.equals(DinosaurHandler.MAIASAURA)) return ModBlocks.MAIASAURA_FOSSIL.get();
        if (dino.equals(DinosaurHandler.PATAGOTITAN)) return ModBlocks.PATAGOTITAN_FOSSIL.get();


        return null;
    }


    public static BlockBehaviour.Properties defaultMachine(){
        return BlockBehaviour.Properties.of(Material.STONE).strength(2.0F);
    }

    public static BlockBehaviour.Properties defaultStone(){
        return BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).explosionResistance(1.5f).requiresCorrectToolForDrops();
    }

    public static BlockBehaviour.Properties defaultPlant(){
        return BlockBehaviour.Properties.of(Material.PLANT).instabreak().randomTicks().sound(SoundType.GRASS);
    }

    public static BlockBehaviour.Properties defaultMoss() {
        return BlockBehaviour.Properties.of(Material.MOSS, MaterialColor.COLOR_GREEN).strength(0.1F).sound(SoundType.MOSS);
    }

    public static BlockBehaviour.Properties defaultAncientCoral() {
        return BlockBehaviour.Properties.of(Material.WATER_PLANT, MaterialColor.COLOR_BLUE).noCollission().instabreak().sound(SoundType.WET_GRASS);
    }
    public static BlockBehaviour.Properties defaultDeadCoral(){
        return BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).requiresCorrectToolForDrops().noCollission().instabreak();
    }

    public static final RegistryObject<Block> ACHILLOBATOR_FOSSIL = MOD_BLOCKS.register("achillobator_fossil",
            () -> new AchillobatorFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_ACHILLOBATOR_FOSSIL = MOD_BLOCKS.register("encased_achillobator_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), ACHILLOBATOR));

    public static final RegistryObject<Block> ALLIGATOR_GAR_FOSSIL = MOD_BLOCKS.register("alligator_gar_fossil",
            () -> new AlligatorGarFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_ALLIGATOR_GAR_FOSSIL = MOD_BLOCKS.register("encased_alligator_gar_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.ALLIGATOR_GAR));

    public static final RegistryObject<Block> ALLOSAURUS_FOSSIL = MOD_BLOCKS.register("allosaurus_fossil",
            () -> new AllosaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_ALLOSAURUS_FOSSIL = MOD_BLOCKS.register("encased_allosaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.ALLOSAURUS));

    public static final RegistryObject<Block> ALVAREZSAURUS_FOSSIL = MOD_BLOCKS.register("alvarezsaurus_fossil",
            () -> new AlvarezsaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_ALVAREZSAURUS_FOSSIL = MOD_BLOCKS.register("encased_alvarezsaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.ALVAREZSAURUS));

    public static final RegistryObject<Block> ANKYLOSAURUS_FOSSIL = MOD_BLOCKS.register("ankylosaurus_fossil",
            () -> new AnkylosaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_ANKYLOSAURUS_FOSSIL = MOD_BLOCKS.register("encased_ankylosaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.ANKYLOSAURUS));

    public static final RegistryObject<Block> APATOSAURUS_FOSSIL = MOD_BLOCKS.register("apatosaurus_fossil",
            () -> new ApatosaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> NIGERSAURUS_FOSSIL = MOD_BLOCKS.register("nigersaurus_fossil",
            () -> new NigersaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_NIGERSAURUS_FOSSIL = MOD_BLOCKS.register("encased_nigersaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.NIGERSAURUS));


    public static final RegistryObject<Block> ENCASED_APATOSAURUS_FOSSIL = MOD_BLOCKS.register("encased_apatosaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.APATOSAURUS));

    public static final RegistryObject<Block> ARSINOITHERIUM_FOSSIL = MOD_BLOCKS.register("arsinoitherium_fossil",
            () -> new ArsinoitheriumFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_ARSINOITHERIUM_FOSSIL = MOD_BLOCKS.register("encased_arsinoitherium_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.ARSINOITHERIUM));

    public static final RegistryObject<Block> ASTEROCERAS_FOSSIL = MOD_BLOCKS.register("asteroceras_fossil",
            () -> new AsterocerasFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_ASTEROCERAS_FOSSIL = MOD_BLOCKS.register("encased_asteroceras_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.ASTEROCERAS));

    public static final RegistryObject<Block> BARYONYX_FOSSIL = MOD_BLOCKS.register("baryonyx_fossil",
            () -> new BaryonyxFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_BARYONYX_FOSSIL = MOD_BLOCKS.register("encased_baryonyx_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.BARYONYX));

    public static final RegistryObject<Block> BEELZEBUFO_FOSSIL = MOD_BLOCKS.register("beelzebufo_fossil",
            () -> new BeelzebufoFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_BEELZEBUFO_FOSSIL = MOD_BLOCKS.register("encased_beelzebufo_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.BEELZEBUFO));

    public static final RegistryObject<Block> CALYMENE_FOSSIL = MOD_BLOCKS.register("calymene_fossil",
            () -> new CalymeneFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));

    public static final RegistryObject<Block> ENCASED_CALYMENE_FOSSIL = MOD_BLOCKS.register("encased_calymene_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.CALYMENE));

    public static final RegistryObject<Block> BRACHIOSAURUS_FOSSIL = MOD_BLOCKS.register("brachiosaurus_fossil",
            () -> new BrachiosaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_BRACHIOSAURUS_FOSSIL = MOD_BLOCKS.register("encased_brachiosaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.BRACHIOSAURUS));

    public static final RegistryObject<Block> CAMARASAURUS_FOSSIL = MOD_BLOCKS.register("camarasaurus_fossil",
            () -> new CamarasaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_CAMARASAURUS_FOSSIL = MOD_BLOCKS.register("encased_camarasaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.CAMARASAURUS));

    public static final RegistryObject<Block> CARCHARODONTOSAURUS_FOSSIL = MOD_BLOCKS.register("carcharodontosaurus_fossil",
            () -> new CarcharodontosaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_CARCHARODONTOSAURUS_FOSSIL = MOD_BLOCKS.register("encased_carcharodontosaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.CARCHARODONTOSAURUS));

    public static final RegistryObject<Block> CARNOTAURUS_FOSSIL = MOD_BLOCKS.register("carnotaurus_fossil",
            () -> new CarnotaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_CARNOTAURUS_FOSSIL = MOD_BLOCKS.register("encased_carnotaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.CARNOTAURUS));

    public static final RegistryObject<Block> CEARADACTYLUS_FOSSIL = MOD_BLOCKS.register("cearadactylus_fossil",
            () -> new CearadactylusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_CEARADACTYLUS_FOSSIL = MOD_BLOCKS.register("encased_cearadactylus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.CEARADACTYLUS));

    public static final RegistryObject<Block> CERATOSAURUS_FOSSIL = MOD_BLOCKS.register("ceratosaurus_fossil",
            () -> new CeratosaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_CERATOSAURUS_FOSSIL = MOD_BLOCKS.register("encased_ceratosaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.CERATOSAURUS));

    public static final RegistryObject<Block> CHASMOSAURUS_FOSSIL = MOD_BLOCKS.register("chasmosaurus_fossil",
            () -> new ChasmosaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_CHASMOSAURUS_FOSSIL = MOD_BLOCKS.register("encased_chasmosaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.CHASMOSAURUS));

    public static final RegistryObject<Block> CHILESAURUS_FOSSIL = MOD_BLOCKS.register("chilesaurus_fossil",
            () -> new ChilesaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_CHILESAURUS_FOSSIL = MOD_BLOCKS.register("encased_chilesaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.CHILESAURUS));

    public static final RegistryObject<Block> COELACANTH_FOSSIL = MOD_BLOCKS.register("coelacanth_fossil",
            () -> new CoelacanthFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_COELACANTH_FOSSIL = MOD_BLOCKS.register("encased_coelacanth_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.COELACANTH));

    public static final RegistryObject<Block> COELURUS_FOSSIL = MOD_BLOCKS.register("coelurus_fossil",
            () -> new CoelurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_COELURUS_FOSSIL = MOD_BLOCKS.register("encased_coelurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.COELURUS));

    public static final RegistryObject<Block> COMPSOGNATHUS_FOSSIL = MOD_BLOCKS.register("compsognathus_fossil",
            () -> new CompsognathusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_COMPSOGNATHUS_FOSSIL = MOD_BLOCKS.register("encased_compsognathus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.COMPSOGNATHUS));

    public static final RegistryObject<Block> CORYTHOSAURUS_FOSSIL = MOD_BLOCKS.register("corythosaurus_fossil",
            () -> new CorythosaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_CORYTHOSAURUS_FOSSIL = MOD_BLOCKS.register("encased_corythosaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.CORYTHOSAURUS));

    public static final RegistryObject<Block> CRASSIGYRINUS_FOSSIL = MOD_BLOCKS.register("crassigyrinus_fossil",
            () -> new CrassigyrinusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_CRASSIGYRINUS_FOSSIL = MOD_BLOCKS.register("encased_crassigyrinus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.CRASSIGYRINUS));

    public static final RegistryObject<Block> DEINOTHERIUM_FOSSIL = MOD_BLOCKS.register("deinotherium_fossil",
            () -> new DeinotheriumFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_DEINOTHERIUM_FOSSIL = MOD_BLOCKS.register("encased_deinotherium_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.DEINOTHERIUM));

    public static final RegistryObject<Block> DILOPHOSAURUS_FOSSIL = MOD_BLOCKS.register("dilophosaurus_fossil",
            () -> new DilophosaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_DILOPHOSAURUS_FOSSIL = MOD_BLOCKS.register("encased_dilophosaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.DILOPHOSAURUS));

    public static final RegistryObject<Block> DIMETRODON_FOSSIL = MOD_BLOCKS.register("dimetrodon_fossil",
            () -> new DimetrodonFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));

    public static final RegistryObject<Block> ENCASED_DIMETRODON_FOSSIL = MOD_BLOCKS.register("encased_dimetrodon_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.DIMETRODON));

    public static final RegistryObject<Block> DIMORPHODON_FOSSIL = MOD_BLOCKS.register("dimorphodon_fossil",
            () -> new DimorphodonFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_DIMORPHODON_FOSSIL = MOD_BLOCKS.register("encased_dimorphodon_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.DIMORPHODON));

    public static final RegistryObject<Block> DIPLOCAULUS_FOSSIL = MOD_BLOCKS.register("diplocaulus_fossil",
            () -> new DiplocaulusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));

    public static final RegistryObject<Block> ENCASED_DIPLOCAULUS_FOSSIL = MOD_BLOCKS.register("encased_diplocaulus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.DIPLOCAULUS));

    public static final RegistryObject<Block> DIPLODOCUS_FOSSIL = MOD_BLOCKS.register("diplodocus_fossil",
            () -> new DiplodocusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_DIPLODOCUS_FOSSIL = MOD_BLOCKS.register("encased_diplodocus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.DIPLODOCUS));

    public static final RegistryObject<Block> DODO_FOSSIL = MOD_BLOCKS.register("dodo_fossil",
            () -> new DodoFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_DODO_FOSSIL = MOD_BLOCKS.register("encased_dodo_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.DODO));

    public static final RegistryObject<Block> DREADNOUGHTUS_FOSSIL = MOD_BLOCKS.register("dreadnoughtus_fossil",
            () -> new DreadnoughtusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_DREADNOUGHTUS_FOSSIL = MOD_BLOCKS.register("encased_dreadnoughtus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.DREADNOUGHTUS));

    public static final RegistryObject<Block> DUNKLEOSTEUS_FOSSIL = MOD_BLOCKS.register("dunkleosteus_fossil",
            () -> new DunkleosteusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));

    public static final RegistryObject<Block> ENCASED_DUNKLEOSTEUS_FOSSIL = MOD_BLOCKS.register("encased_dunkleosteus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.DUNKLEOSTEUS));

    public static final RegistryObject<Block> EDMONTOSAURUS_FOSSIL = MOD_BLOCKS.register("edmontosaurus_fossil",
            () -> new EdmontosaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_EDMONTOSAURUS_FOSSIL = MOD_BLOCKS.register("encased_edmontosaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.EDMONTOSAURUS));

    public static final RegistryObject<Block> ELASMOTHERIUM_FOSSIL = MOD_BLOCKS.register("elasmotherium_fossil",
            () -> new ElasmotheriumFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_ELASMOTHERIUM_FOSSIL = MOD_BLOCKS.register("encased_elasmotherium_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.ELASMOTHERIUM));

    public static final RegistryObject<Block> GALLIMIMUS_FOSSIL = MOD_BLOCKS.register("gallimimus_fossil",
            () -> new GallimimusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_GALLIMIMUS_FOSSIL = MOD_BLOCKS.register("encased_gallimimus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.GALLIMIMUS));

    public static final RegistryObject<Block> GIGANOTOSAURUS_FOSSIL = MOD_BLOCKS.register("giganotosaurus_fossil",
            () -> new GiganotosaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_GIGANOTOSAURUS_FOSSIL = MOD_BLOCKS.register("encased_giganotosaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.GIGANOTOSAURUS));

    public static final RegistryObject<Block> GUANLONG_FOSSIL = MOD_BLOCKS.register("guanlong_fossil",
            () -> new GuanlongFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_GUANLONG_FOSSIL = MOD_BLOCKS.register("encased_guanlong_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.GUANLONG));

    public static final RegistryObject<Block> HERRERASAURUS_FOSSIL = MOD_BLOCKS.register("herrerasaurus_fossil",
            () -> new HerrerasaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_HERRERASAURUS_FOSSIL = MOD_BLOCKS.register("encased_herrerasaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.HERRERASAURUS));

    public static final RegistryObject<Block> HYAENODON_FOSSIL = MOD_BLOCKS.register("hyaenodon_fossil",
            () -> new HyaenodonFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_HYAENODON_FOSSIL = MOD_BLOCKS.register("encased_hyaenodon_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.HYAENODON));

    public static final RegistryObject<Block> HYPSILOPHODON_FOSSIL = MOD_BLOCKS.register("hypsilophodon_fossil",
            () -> new HypsilophodonFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_HYPSILOPHODON_FOSSIL = MOD_BLOCKS.register("encased_hypsilophodon_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.HYPSILOPHODON));

    public static final RegistryObject<Block> LAMBEOSAURUS_FOSSIL = MOD_BLOCKS.register("lambeosaurus_fossil",
            () -> new LambeosaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_LAMBEOSAURUS_FOSSIL = MOD_BLOCKS.register("encased_lambeosaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.LAMBEOSAURUS));

    public static final RegistryObject<Block> LEAELLYNASAURA_FOSSIL = MOD_BLOCKS.register("leaellynasaura_fossil",
            () -> new LeaellynasauraFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_LEAELLYNASAURA_FOSSIL = MOD_BLOCKS.register("encased_leaellynasaura_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.LEAELLYNASAURA));

    public static final RegistryObject<Block> LEPTICTIDIUM_FOSSIL = MOD_BLOCKS.register("leptictidium_fossil",
            () -> new LeptictidiumFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_LEPTICTIDIUM_FOSSIL = MOD_BLOCKS.register("encased_leptictidium_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.LEPTICTIDIUM));

    public static final RegistryObject<Block> LIVYATAN_FOSSIL = MOD_BLOCKS.register("livyatan_fossil",
            () -> new LivyatanFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_LIVYATAN_FOSSIL = MOD_BLOCKS.register("encased_livyatan_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.LIVYATAN));

    public static final RegistryObject<Block> MEGALODON_FOSSIL = MOD_BLOCKS.register("megalodon_fossil",
            () -> new MegalodonFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_MEGALODON_FOSSIL = MOD_BLOCKS.register("encased_megalodon_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.MEGALODON));

    public static final RegistryObject<Block> LUDODACTYLUS_FOSSIL = MOD_BLOCKS.register("ludodactylus_fossil",
            () -> new LudodactylusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_LUDODACTYLUS_FOSSIL = MOD_BLOCKS.register("encased_ludodactylus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.LUDODACTYLUS));

    public static final RegistryObject<Block> MAJUNGASAURUS_FOSSIL = MOD_BLOCKS.register("majungasaurus_fossil",
            () -> new MajungasaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_MAJUNGASAURUS_FOSSIL = MOD_BLOCKS.register("encased_majungasaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.MAJUNGASAURUS));

    public static final RegistryObject<Block> MAMENCHISAURUS_FOSSIL = MOD_BLOCKS.register("mamenchisaurus_fossil",
            () -> new MamenchisaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_MAMENCHISAURUS_FOSSIL = MOD_BLOCKS.register("encased_mamenchisaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.MAMENCHISAURUS));

    public static final RegistryObject<Block> MAMMOTH_FOSSIL = MOD_BLOCKS.register("mammoth_fossil",
            () -> new MammothFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_MAMMOTH_FOSSIL = MOD_BLOCKS.register("encased_mammoth_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.MAMMOTH));

    public static final RegistryObject<Block> MAWSONIA_FOSSIL = MOD_BLOCKS.register("mawsonia_fossil",
            () -> new MawsoniaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_MAWSONIA_FOSSIL = MOD_BLOCKS.register("encased_mawsonia_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.MAWSONIA));

    public static final RegistryObject<Block> MEGAPIRANHA_FOSSIL = MOD_BLOCKS.register("megapiranha_fossil",
            () -> new MegapiranhaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_MEGAPIRANHA_FOSSIL = MOD_BLOCKS.register("encased_megapiranha_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.MEGAPIRANHA));

    public static final RegistryObject<Block> MEGATHERIUM_FOSSIL = MOD_BLOCKS.register("megatherium_fossil",
            () -> new MegatheriumFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_MEGATHERIUM_FOSSIL = MOD_BLOCKS.register("encased_megatherium_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.MEGATHERIUM));

    public static final RegistryObject<Block> METRIACANTHOSAURUS_FOSSIL = MOD_BLOCKS.register("metriacanthosaurus_fossil",
            () -> new MetriacanthosaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_METRIACANTHOSAURUS_FOSSIL = MOD_BLOCKS.register("encased_metriacanthosaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.METRIACANTHOSAURUS));

    public static final RegistryObject<Block> MICROCERATUS_FOSSIL = MOD_BLOCKS.register("microceratus_fossil",
            () -> new MicroceratusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_MICROCERATUS_FOSSIL = MOD_BLOCKS.register("encased_microceratus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.MICROCERATUS));

    public static final RegistryObject<Block> MICRORAPTOR_FOSSIL = MOD_BLOCKS.register("microraptor_fossil",
            () -> new MicroraptorFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_MICRORAPTOR_FOSSIL = MOD_BLOCKS.register("encased_microraptor_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.MICRORAPTOR));

    public static final RegistryObject<Block> MOGANOPTERUS_FOSSIL = MOD_BLOCKS.register("moganopterus_fossil",
            () -> new MoganopterusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_MOGANOPTERUS_FOSSIL = MOD_BLOCKS.register("encased_moganopterus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.MOGANOPTERUS));

    public static final RegistryObject<Block> MOSASAURUS_FOSSIL = MOD_BLOCKS.register("mosasaurus_fossil",
            () -> new MosasaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_MOSASAURUS_FOSSIL = MOD_BLOCKS.register("encased_mosasaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.MOSASAURUS));

    public static final RegistryObject<Block> MUSSAURUS_FOSSIL = MOD_BLOCKS.register("mussaurus_fossil",
            () -> new MussaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_MUSSAURUS_FOSSIL = MOD_BLOCKS.register("encased_mussaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.MUSSAURUS));

    public static final RegistryObject<Block> ORNITHOMIMUS_FOSSIL = MOD_BLOCKS.register("ornithomimus_fossil",
            () -> new OrnithomimusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_ORNITHOMIMUS_FOSSIL = MOD_BLOCKS.register("encased_ornithomimus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.ORNITHOMIMUS));

    public static final RegistryObject<Block> OTHNIELIA_FOSSIL = MOD_BLOCKS.register("othnielia_fossil",
            () -> new OthnieliaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_OTHNIELIA_FOSSIL = MOD_BLOCKS.register("encased_othnielia_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.OTHNIELIA));

    public static final RegistryObject<Block> OVIRAPTOR_FOSSIL = MOD_BLOCKS.register("oviraptor_fossil",
            () -> new OviraptorFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_OVIRAPTOR_FOSSIL = MOD_BLOCKS.register("encased_oviraptor_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.OVIRAPTOR));

    public static final RegistryObject<Block> PACHYCEPHALOSAURUS_FOSSIL = MOD_BLOCKS.register("pachycephalosaurus_fossil",
            () -> new PachycephalosaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_PACHYCEPHALOSAURUS_FOSSIL = MOD_BLOCKS.register("encased_pachycephalosaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.PACHYCEPHALOSAURUS));

    public static final RegistryObject<Block> PARACERATHERIUM_FOSSIL = MOD_BLOCKS.register("paraceratherium_fossil",
            () -> new ParaceratheriumFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_PARACERATHERIUM_FOSSIL = MOD_BLOCKS.register("encased_paraceratherium_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.PARACERATHERIUM));

    public static final RegistryObject<Block> PARAPUZOSIA_FOSSIL = MOD_BLOCKS.register("parapuzosia_fossil",
            () -> new ParapuzosiaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_PARAPUZOSIA_FOSSIL = MOD_BLOCKS.register("encased_parapuzosia_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.PARAPUZOSIA));

    public static final RegistryObject<Block> CAMEROCERAS_FOSSIL = MOD_BLOCKS.register("cameroceras_fossil",
            () -> new CamerocerasFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));

    public static final RegistryObject<Block> ENCASED_CAMEROCERAS_FOSSIL = MOD_BLOCKS.register("encased_cameroceras_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.CAMEROCERAS));

    public static final RegistryObject<Block> ORTHOCERAS_FOSSIL = MOD_BLOCKS.register("orthoceras_fossil",
            () -> new OrthocerasFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));

    public static final RegistryObject<Block> ENCASED_ORTHOCERAS_FOSSIL = MOD_BLOCKS.register("encased_orthoceras_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.ORTHOCERAS));

    public static final RegistryObject<Block> ENDOCERAS_FOSSIL = MOD_BLOCKS.register("endoceras_fossil",
            () -> new EndocerasFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.DEEPSLATE)));

    public static final RegistryObject<Block> ENCASED_ENDOCERAS_FOSSIL = MOD_BLOCKS.register("encased_endoceras_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.ENDOCERAS));

    public static final RegistryObject<Block> PARASAUROLOPHUS_FOSSIL = MOD_BLOCKS.register("parasaurolophus_fossil",
            () -> new ParasaurolophusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_PARASAUROLOPHUS_FOSSIL = MOD_BLOCKS.register("encased_parasaurolophus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.PARASAUROLOPHUS));

    public static final RegistryObject<Block> PERISPHINCTES_FOSSIL = MOD_BLOCKS.register("perisphinctes_fossil",
            () -> new PerisphinctesFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_PERISPHINCTES_FOSSIL = MOD_BLOCKS.register("encased_perisphinctes_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.PERISPHINCTES));

    public static final RegistryObject<Block> POSTOSUCHUS_FOSSIL = MOD_BLOCKS.register("postosuchus_fossil",
            () -> new PostosuchusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_POSTOSUCHUS_FOSSIL = MOD_BLOCKS.register("encased_postosuchus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.POSTOSUCHUS));

    public static final RegistryObject<Block> PROCERATOSAURUS_FOSSIL = MOD_BLOCKS.register("proceratosaurus_fossil",
            () -> new ProceratosaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_PROCERATOSAURUS_FOSSIL = MOD_BLOCKS.register("encased_proceratosaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.PROCERATOSAURUS));

    public static final RegistryObject<Block> PROTOCERATOPS_FOSSIL = MOD_BLOCKS.register("protoceratops_fossil",
            () -> new ProtoceratopsFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_PROTOCERATOPS_FOSSIL = MOD_BLOCKS.register("encased_protoceratops_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.PROTOCERATOPS));

    public static final RegistryObject<Block> PTERANODON_FOSSIL = MOD_BLOCKS.register("pteranodon_fossil",
            () -> new PteranodonFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_PTERANODON_FOSSIL = MOD_BLOCKS.register("encased_pteranodon_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.PTERANODON));

    public static final RegistryObject<Block> QUETZAL_FOSSIL = MOD_BLOCKS.register("quetzal_fossil",
            () -> new QuetzalcoatlusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_QUETZAL_FOSSIL = MOD_BLOCKS.register("encased_quetzal_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.QUETZAL));

    public static final RegistryObject<Block> RUGOPS_FOSSIL = MOD_BLOCKS.register("rugops_fossil",
            () -> new RugopsFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_RUGOPS_FOSSIL = MOD_BLOCKS.register("encased_rugops_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.RUGOPS));

    public static final RegistryObject<Block> SEGISAURUS_FOSSIL = MOD_BLOCKS.register("segisaurus_fossil",
            () -> new SegisaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_SEGISAURUS_FOSSIL = MOD_BLOCKS.register("encased_segisaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.SEGISAURUS));

    public static final RegistryObject<Block> SINOCERATOPS_FOSSIL = MOD_BLOCKS.register("sinoceratops_fossil",
            () -> new SinoceratopsFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_SINOCERATOPS_FOSSIL = MOD_BLOCKS.register("encased_sinoceratops_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.SINOCERATOPS));

    public static final RegistryObject<Block> SMILODON_FOSSIL = MOD_BLOCKS.register("smilodon_fossil",
            () -> new SmilodonFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_SMILODON_FOSSIL = MOD_BLOCKS.register("encased_smilodon_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.SMILODON));

    public static final RegistryObject<Block> SPINOSAURUS_FOSSIL = MOD_BLOCKS.register("spinosaurus_fossil",
            () -> new SpinosaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_SPINOSAURUS_FOSSIL = MOD_BLOCKS.register("encased_spinosaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.SPINOSAURUS));

    public static final RegistryObject<Block> STEGOSAURUS_FOSSIL = MOD_BLOCKS.register("stegosaurus_fossil",
            () -> new StegosaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_STEGOSAURUS_FOSSIL = MOD_BLOCKS.register("encased_stegosaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.STEGOSAURUS));

    public static final RegistryObject<Block> STYRACOSAURUS_FOSSIL = MOD_BLOCKS.register("styracosaurus_fossil",
            () -> new StyracosaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_STYRACOSAURUS_FOSSIL = MOD_BLOCKS.register("encased_styracosaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.STYRACOSAURUS));

    public static final RegistryObject<Block> SUCHOMIMUS_FOSSIL = MOD_BLOCKS.register("suchomimus_fossil",
            () -> new SuchomimusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_SUCHOMIMUS_FOSSIL = MOD_BLOCKS.register("encased_suchomimus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.SUCHOMIMUS));

    public static final RegistryObject<Block> THERIZINOSAURUS_FOSSIL = MOD_BLOCKS.register("therizinosaurus_fossil",
            () -> new TherizinosaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_THERIZINOSAURUS_FOSSIL = MOD_BLOCKS.register("encased_therizinosaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.THERIZINOSAURUS));

    public static final RegistryObject<Block> TITANIS_FOSSIL = MOD_BLOCKS.register("titanis_fossil",
            () -> new TitanisFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_TITANIS_FOSSIL = MOD_BLOCKS.register("encased_titanis_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.TITANIS));

    public static final RegistryObject<Block> TITANITES_FOSSIL = MOD_BLOCKS.register("titanites_fossil",
            () -> new TitanitesFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_TITANITES_FOSSIL = MOD_BLOCKS.register("encased_titanites_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.TITANITES));

    public static final RegistryObject<Block> TRICERATOPS_FOSSIL = MOD_BLOCKS.register("triceratops_fossil",
            () -> new TriceratopsFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_TRICERATOPS_FOSSIL = MOD_BLOCKS.register("encased_triceratops_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.TRICERATOPS));

    public static final RegistryObject<Block> TROODON_FOSSIL = MOD_BLOCKS.register("troodon_fossil",
            () -> new TroodonFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_TROODON_FOSSIL = MOD_BLOCKS.register("encased_troodon_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.TROODON));

    public static final RegistryObject<Block> TROPEOGNATHUS_FOSSIL = MOD_BLOCKS.register("tropeognathus_fossil",
            () -> new TropeognathusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_TROPEOGNATHUS_FOSSIL = MOD_BLOCKS.register("encased_tropeognathus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.TROPEOGNATHUS));

    public static final RegistryObject<Block> TYLOSAURUS_FOSSIL = MOD_BLOCKS.register("tylosaurus_fossil",
            () -> new TylosaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_TYLOSAURUS_FOSSIL = MOD_BLOCKS.register("encased_tylosaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.TYLOSAURUS));

    public static final RegistryObject<Block> TYRANNOSAURUS_FOSSIL = MOD_BLOCKS.register("tyrannosaurus_fossil",
            () -> new TyrannosaurusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_TYRANNOSAURUS_FOSSIL = MOD_BLOCKS.register("encased_tyrannosaurus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.TYRANNOSAURUS));

    public static final RegistryObject<Block> VECTIPELTA_FOSSIL = MOD_BLOCKS.register("vectipelta_fossil",
            () -> new VectipeltaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_VECTIPELTA_FOSSIL = MOD_BLOCKS.register("encased_vectipelta_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.VECTIPELTA));

    public static final RegistryObject<Block> VELOCIRAPTOR_FOSSIL = MOD_BLOCKS.register("velociraptor_fossil",
            () -> new VelociraptorFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_VELOCIRAPTOR_FOSSIL = MOD_BLOCKS.register("encased_velociraptor_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.VELOCIRAPTOR));

    public static final RegistryObject<Block> ZHENYUANOPTERUS_FOSSIL = MOD_BLOCKS.register("zhenyuanopterus_fossil",
            () -> new ZhenyuanopterusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_ZHENYUANOPTERUS_FOSSIL = MOD_BLOCKS.register("encased_zhenyuanopterus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.ZHENYUANOPTERUS));

    public static final RegistryObject<Block> DEINOSUCHUS_FOSSIL = MOD_BLOCKS.register("deinosuchus_fossil",
            () -> new DeinosuchusFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_DEINOSUCHUS_FOSSIL = MOD_BLOCKS.register("encased_deinosuchus_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.DEINOSUCHUS));

    public static final RegistryObject<Block> KAIRUKU_FOSSIL = MOD_BLOCKS.register("kairuku_fossil",
            () -> new KairukuFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_KAIRUKU_FOSSIL = MOD_BLOCKS.register("encased_kairuku_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.KAIRUKU));
    public static final RegistryObject<Block> PATAGOTITAN_FOSSIL = MOD_BLOCKS.register("patagotitan_fossil",
            () -> new PatagotitanFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_PATAGOTITAN_FOSSIL = MOD_BLOCKS.register("encased_patagotitan_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.PATAGOTITAN));

    public static final RegistryObject<Block> MAIASAURA_FOSSIL = MOD_BLOCKS.register("maiasaura_fossil",
            () -> new MaiasauraFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<Block> ENCASED_MAIASAURA_FOSSIL = MOD_BLOCKS.register("encased_maiasaura_fossil",
            () -> new EncasedFaunaFossilBlock(BlockBehaviour.Properties.of(Material.STONE).strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE), DinosaurHandler.MAIASAURA));
}
