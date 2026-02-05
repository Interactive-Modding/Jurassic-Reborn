package net.vit.jurassicreborn.common.blocks.wood;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Material;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.ModWoodTypes;
import net.vit.jurassicreborn.common.blocks.WoodButtonBlock;
import net.minecraftforge.registries.RegistryObject;
import net.vit.jurassicreborn.common.plants.PlantHandler;

import java.util.List;

public class WoodBlocks {

    //tree family
    public static final RegistryObject<RotatedPillarBlock> GINKGO_LOG = ModBlocks.MOD_BLOCKS.register("ginkgo_log",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_GINKGO_LOG = ModBlocks.MOD_BLOCKS.register("stripped_ginkgo_log",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<RotatedPillarBlock> GINKGO_WOOD = ModBlocks.MOD_BLOCKS.register("ginkgo_wood",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_GINKGO_WOOD = ModBlocks.MOD_BLOCKS.register("stripped_ginkgo_wood",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistryObject<Block> GINKGO_LEAVES = ModBlocks.MOD_BLOCKS.register("ginkgo_leaves",() -> new AncientLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));

    //plank family
    public static final RegistryObject<Block> GINKGO_PLANKS = ModBlocks.MOD_BLOCKS.register("ginkgo_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> GINKGO_BUTTON = ModBlocks.MOD_BLOCKS.register("ginkgo_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON)));
    public static final RegistryObject<Block> GINKGO_FENCE = ModBlocks.MOD_BLOCKS.register("ginkgo_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));
    public static final RegistryObject<Block> GINKGO_FENCE_GATE = ModBlocks.MOD_BLOCKS.register("ginkgo_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
    public static final RegistryObject<Block> GINKGO_PRESSURE_PLATE = ModBlocks.MOD_BLOCKS.register("ginkgo_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE), SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF));
    public static final RegistryObject<Block> GINKGO_SIGN = ModBlocks.MOD_BLOCKS.register("ginkgo_sign", () -> new StandingSignBlock(sign(), ModWoodTypes.ginkgo));
    public static final RegistryObject<Block> GINKGO_WALL_SIGN = ModBlocks.MOD_BLOCKS.register("ginkgo_wall_sign", () -> new WallSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WALL_SIGN).lootFrom(GINKGO_SIGN), ModWoodTypes.ginkgo));
    public static final RegistryObject<Block> GINKGO_SLAB = ModBlocks.MOD_BLOCKS.register("ginkgo_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static final RegistryObject<Block> GINKGO_STAIRS = ModBlocks.MOD_BLOCKS.register("ginkgo_stairs", () -> new StairBlock(() -> GINKGO_PLANKS.get().defaultBlockState(),BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final RegistryObject<Block> GINKGO_DOOR = ModBlocks.MOD_BLOCKS.register("ginkgo_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR), SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN));
    public static final RegistryObject<Block> GINKGO_TRAPDOOR = ModBlocks.MOD_BLOCKS.register("ginkgo_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR), SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN));


    //tree family
    public static final RegistryObject<RotatedPillarBlock> CALAMITES_LOG = ModBlocks.MOD_BLOCKS.register("calamites_log",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_CALAMITES_LOG = ModBlocks.MOD_BLOCKS.register("stripped_calamites_log",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<RotatedPillarBlock> CALAMITES_WOOD = ModBlocks.MOD_BLOCKS.register("calamites_wood",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_CALAMITES_WOOD = ModBlocks.MOD_BLOCKS.register("stripped_calamites_wood",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistryObject<Block> CALAMITES_LEAVES = ModBlocks.MOD_BLOCKS.register("calamites_leaves",() -> new AncientLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));

    //plank family
    public static final RegistryObject<Block> CALAMITES_PLANKS = ModBlocks.MOD_BLOCKS.register("calamites_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> CALAMITES_BUTTON = ModBlocks.MOD_BLOCKS.register("calamites_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON)));
    public static final RegistryObject<Block> CALAMITES_FENCE = ModBlocks.MOD_BLOCKS.register("calamites_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));
    public static final RegistryObject<Block> CALAMITES_FENCE_GATE = ModBlocks.MOD_BLOCKS.register("calamites_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
    public static final RegistryObject<Block> CALAMITES_PRESSURE_PLATE = ModBlocks.MOD_BLOCKS.register("calamites_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE), SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF));
    public static final RegistryObject<Block> CALAMITES_SIGN = ModBlocks.MOD_BLOCKS.register("calamites_sign", () -> new StandingSignBlock(sign(), ModWoodTypes.calamites));
    public static final RegistryObject<Block> CALAMITES_WALL_SIGN = ModBlocks.MOD_BLOCKS.register("calamites_wall_sign", () -> new WallSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WALL_SIGN).lootFrom(CALAMITES_SIGN), ModWoodTypes.calamites));
    public static final RegistryObject<Block> CALAMITES_SLAB = ModBlocks.MOD_BLOCKS.register("calamites_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static final RegistryObject<Block> CALAMITES_STAIRS = ModBlocks.MOD_BLOCKS.register("calamites_stairs", () -> new StairBlock(() -> CALAMITES_PLANKS.get().defaultBlockState(),BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final RegistryObject<Block> CALAMITES_DOOR = ModBlocks.MOD_BLOCKS.register("calamites_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR), SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN));
    public static final RegistryObject<Block> CALAMITES_TRAPDOOR = ModBlocks.MOD_BLOCKS.register("calamites_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR), SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN));

    //tree family
    public static final RegistryObject<RotatedPillarBlock> ARAUCARIA_LOG = ModBlocks.MOD_BLOCKS.register("araucaria_log",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_ARAUCARIA_LOG = ModBlocks.MOD_BLOCKS.register("stripped_araucaria_log",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<RotatedPillarBlock> ARAUCARIA_WOOD = ModBlocks.MOD_BLOCKS.register("araucaria_wood",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_ARAUCARIA_WOOD = ModBlocks.MOD_BLOCKS.register("stripped_araucaria_wood",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistryObject<Block> ARAUCARIA_LEAVES = ModBlocks.MOD_BLOCKS.register("araucaria_leaves",() -> new AncientLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));

    //plank family
    public static final RegistryObject<Block> ARAUCARIA_PLANKS = ModBlocks.MOD_BLOCKS.register("araucaria_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> ARAUCARIA_BUTTON = ModBlocks.MOD_BLOCKS.register("araucaria_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON)));
    public static final RegistryObject<Block> ARAUCARIA_FENCE = ModBlocks.MOD_BLOCKS.register("araucaria_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));
    public static final RegistryObject<Block> ARAUCARIA_FENCE_GATE = ModBlocks.MOD_BLOCKS.register("araucaria_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
    public static final RegistryObject<Block> ARAUCARIA_PRESSURE_PLATE = ModBlocks.MOD_BLOCKS.register("araucaria_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE), SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF));
    public static final RegistryObject<Block> ARAUCARIA_SIGN = ModBlocks.MOD_BLOCKS.register("araucaria_sign", () -> new StandingSignBlock(sign(), ModWoodTypes.araucaria));
    public static final RegistryObject<Block> ARAUCARIA_WALL_SIGN = ModBlocks.MOD_BLOCKS.register("araucaria_wall_sign", () -> new WallSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WALL_SIGN).lootFrom(ARAUCARIA_SIGN), ModWoodTypes.araucaria));
    public static final RegistryObject<Block> ARAUCARIA_SLAB = ModBlocks.MOD_BLOCKS.register("araucaria_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static final RegistryObject<Block> ARAUCARIA_STAIRS = ModBlocks.MOD_BLOCKS.register("araucaria_stairs", () -> new StairBlock(() -> ARAUCARIA_PLANKS.get().defaultBlockState(),BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final RegistryObject<Block> ARAUCARIA_DOOR = ModBlocks.MOD_BLOCKS.register("araucaria_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR), SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN));
    public static final RegistryObject<Block> ARAUCARIA_TRAPDOOR = ModBlocks.MOD_BLOCKS.register("araucaria_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR), SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN));

    //tree family
    public static final RegistryObject<RotatedPillarBlock> PHOENIX_LOG = ModBlocks.MOD_BLOCKS.register("phoenix_log",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_PHOENIX_LOG = ModBlocks.MOD_BLOCKS.register("stripped_phoenix_log",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<RotatedPillarBlock> PHOENIX_WOOD = ModBlocks.MOD_BLOCKS.register("phoenix_wood",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_PHOENIX_WOOD = ModBlocks.MOD_BLOCKS.register("stripped_phoenix_wood",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistryObject<Block> PHOENIX_LEAVES = ModBlocks.MOD_BLOCKS.register("phoenix_leaves",() -> new AncientLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));

    //plank family
    public static final RegistryObject<Block> PHOENIX_PLANKS = ModBlocks.MOD_BLOCKS.register("phoenix_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> PHOENIX_BUTTON = ModBlocks.MOD_BLOCKS.register("phoenix_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON)));
    public static final RegistryObject<Block> PHOENIX_FENCE = ModBlocks.MOD_BLOCKS.register("phoenix_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));
    public static final RegistryObject<Block> PHOENIX_FENCE_GATE = ModBlocks.MOD_BLOCKS.register("phoenix_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
    public static final RegistryObject<Block> PHOENIX_PRESSURE_PLATE = ModBlocks.MOD_BLOCKS.register("phoenix_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE), SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF));
    public static final RegistryObject<Block> PHOENIX_SIGN = ModBlocks.MOD_BLOCKS.register("phoenix_sign", () -> new StandingSignBlock(sign(), ModWoodTypes.phoenix));
    public static final RegistryObject<Block> PHOENIX_WALL_SIGN = ModBlocks.MOD_BLOCKS.register("phoenix_wall_sign", () -> new WallSignBlock(sign().lootFrom(PHOENIX_SIGN), ModWoodTypes.phoenix));
    public static final RegistryObject<Block> PHOENIX_SLAB = ModBlocks.MOD_BLOCKS.register("phoenix_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static final RegistryObject<Block> PHOENIX_STAIRS = ModBlocks.MOD_BLOCKS.register("phoenix_stairs", () -> new StairBlock(() -> PHOENIX_PLANKS.get().defaultBlockState(),BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final RegistryObject<Block> PHOENIX_DOOR = ModBlocks.MOD_BLOCKS.register("phoenix_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR), SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN));
    public static final RegistryObject<Block> PHOENIX_TRAPDOOR = ModBlocks.MOD_BLOCKS.register("phoenix_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR), SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN));


    //tree family
    public static final RegistryObject<RotatedPillarBlock> PSARONIUS_LOG = ModBlocks.MOD_BLOCKS.register("psaronius_log",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_PSARONIUS_LOG = ModBlocks.MOD_BLOCKS.register("stripped_psaronius_log",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<RotatedPillarBlock> PSARONIUS_WOOD = ModBlocks.MOD_BLOCKS.register("psaronius_wood",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_PSARONIUS_WOOD = ModBlocks.MOD_BLOCKS.register("stripped_psaronius_wood",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistryObject<Block> PSARONIUS_LEAVES = ModBlocks.MOD_BLOCKS.register("psaronius_leaves",() -> new AncientLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));

    //plank family
    public static final RegistryObject<Block> PSARONIUS_PLANKS = ModBlocks.MOD_BLOCKS.register("psaronius_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> PSARONIUS_BUTTON = ModBlocks.MOD_BLOCKS.register("psaronius_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON)));
    public static final RegistryObject<Block> PSARONIUS_FENCE = ModBlocks.MOD_BLOCKS.register("psaronius_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));
    public static final RegistryObject<Block> PSARONIUS_FENCE_GATE = ModBlocks.MOD_BLOCKS.register("psaronius_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
    public static final RegistryObject<Block> PSARONIUS_PRESSURE_PLATE = ModBlocks.MOD_BLOCKS.register("psaronius_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE), SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF));
    public static final RegistryObject<Block> PSARONIUS_SIGN = ModBlocks.MOD_BLOCKS.register("psaronius_sign", () -> new StandingSignBlock(sign(), ModWoodTypes.psaronius));
    public static final RegistryObject<Block> PSARONIUS_WALL_SIGN = ModBlocks.MOD_BLOCKS.register("psaronius_wall_sign", () -> new WallSignBlock(sign().lootFrom(PSARONIUS_SIGN), ModWoodTypes.psaronius));
    public static final RegistryObject<Block> PSARONIUS_SLAB = ModBlocks.MOD_BLOCKS.register("psaronius_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static final RegistryObject<Block> PSARONIUS_STAIRS = ModBlocks.MOD_BLOCKS.register("psaronius_stairs", () -> new StairBlock(() -> PSARONIUS_PLANKS.get().defaultBlockState(),BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final RegistryObject<Block> PSARONIUS_DOOR = ModBlocks.MOD_BLOCKS.register("psaronius_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR), SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN));
    public static final RegistryObject<Block> PSARONIUS_TRAPDOOR = ModBlocks.MOD_BLOCKS.register("psaronius_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR), SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN));


    //tree family
    public static final RegistryObject<RotatedPillarBlock> MAGNOLIA_LOG = ModBlocks.MOD_BLOCKS.register("magnolia_log",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_MAGNOLIA_LOG = ModBlocks.MOD_BLOCKS.register("stripped_magnolia_log",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));
    public static final RegistryObject<RotatedPillarBlock> MAGNOLIA_WOOD = ModBlocks.MOD_BLOCKS.register("magnolia_wood",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_MAGNOLIA_WOOD = ModBlocks.MOD_BLOCKS.register("stripped_magnolia_wood",() -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));
    public static final RegistryObject<Block> MAGNOLIA_LEAVES = ModBlocks.MOD_BLOCKS.register("magnolia_leaves",() -> new AncientLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));

    //plank family
    public static final RegistryObject<Block> MAGNOLIA_PLANKS = ModBlocks.MOD_BLOCKS.register("magnolia_planks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> MAGNOLIA_BUTTON = ModBlocks.MOD_BLOCKS.register("magnolia_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.copy(Blocks.OAK_BUTTON)));
    public static final RegistryObject<Block> MAGNOLIA_FENCE = ModBlocks.MOD_BLOCKS.register("magnolia_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE)));
    public static final RegistryObject<Block> MAGNOLIA_FENCE_GATE = ModBlocks.MOD_BLOCKS.register("magnolia_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_FENCE_GATE), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
    public static final RegistryObject<Block> MAGNOLIA_PRESSURE_PLATE = ModBlocks.MOD_BLOCKS.register("magnolia_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, BlockBehaviour.Properties.copy(Blocks.OAK_PRESSURE_PLATE), SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_ON, SoundEvents.WOODEN_PRESSURE_PLATE_CLICK_OFF));
    public static final RegistryObject<Block> MAGNOLIA_SIGN = ModBlocks.MOD_BLOCKS.register("magnolia_sign", () -> new StandingSignBlock(sign(), ModWoodTypes.magnolia));
    public static final RegistryObject<Block> MAGNOLIA_WALL_SIGN = ModBlocks.MOD_BLOCKS.register("magnolia_wall_sign", () -> new WallSignBlock(sign().lootFrom(MAGNOLIA_SIGN), ModWoodTypes.magnolia));
    public static final RegistryObject<Block> MAGNOLIA_SLAB = ModBlocks.MOD_BLOCKS.register("magnolia_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SLAB)));
    public static final RegistryObject<Block> MAGNOLIA_STAIRS = ModBlocks.MOD_BLOCKS.register("magnolia_stairs", () -> new StairBlock(() -> MAGNOLIA_PLANKS.get().defaultBlockState(),BlockBehaviour.Properties.copy(Blocks.OAK_STAIRS)));
    public static final RegistryObject<Block> MAGNOLIA_DOOR = ModBlocks.MOD_BLOCKS.register("magnolia_door", () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_DOOR), SoundEvents.WOODEN_DOOR_CLOSE, SoundEvents.WOODEN_DOOR_OPEN));
    public static final RegistryObject<Block> MAGNOLIA_TRAPDOOR = ModBlocks.MOD_BLOCKS.register("magnolia_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.OAK_TRAPDOOR), SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundEvents.WOODEN_TRAPDOOR_OPEN));

    public static final RegistryObject<PetrifiedLogBlock> PETRIFIED_ARAUCARIA_LOG = ModBlocks.MOD_BLOCKS.register("petrified_araucaria_log", () -> new PetrifiedLogBlock(PlantHandler.ARAUCARIA, BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<PetrifiedLogBlock> PETRIFIED_CALAMITES_LOG = ModBlocks.MOD_BLOCKS.register("petrified_calamites_log", () -> new PetrifiedLogBlock(PlantHandler.CALAMITES, BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<PetrifiedLogBlock> PETRIFIED_GINKGO_LOG = ModBlocks.MOD_BLOCKS.register("petrified_ginkgo_log", () -> new PetrifiedLogBlock(PlantHandler.GINKGO, BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<PetrifiedLogBlock> PETRIFIED_MAGNOLIA_LOG = ModBlocks.MOD_BLOCKS.register("petrified_magnolia_log", () -> new PetrifiedLogBlock(PlantHandler.MAGNOLIA, BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<PetrifiedLogBlock> PETRIFIED_PSARONIUS_LOG = ModBlocks.MOD_BLOCKS.register("petrified_psaronius_log", () -> new PetrifiedLogBlock(PlantHandler.PSARONIUS, BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<PetrifiedLogBlock> PETRIFIED_PHOENIX_LOG = ModBlocks.MOD_BLOCKS.register("petrified_phoenix_log", () -> new PetrifiedLogBlock(PlantHandler.PHOENIX, BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static BlockBehaviour.Properties sign() {
        return BlockBehaviour.Properties.of(Material.WOOD).noCollission().strength(1.0F).sound(SoundType.WOOD);
    }

    public static void register() {
        System.out.println("REGISTERING WOOD BLOCKS");
    }

    public static List<RegistryObject<Block>> petrifiedLogs() {
        return ModBlocks.MOD_BLOCKS.getEntries().stream().filter(blockRegistryObject -> blockRegistryObject.getId().getPath().startsWith("petrified")).toList();
    }

    public static List<Block> getSigns() {
        return ModBlocks.MOD_BLOCKS.getEntries().stream().map(RegistryObject::get).filter(block -> block instanceof SignBlock).toList();
    }

    public static BushBlock getSaplingForType(WoodType type) {
        //i was an idiot and yeah I have to hardcode this, not redoing other registry stuff...
        if (type.equals(ModWoodTypes.araucaria)) {
            return ModBlocks.ARAUCARIA_SAPLING.get();
        } else if (type.equals(ModWoodTypes.calamites)) {
            return ModBlocks.CALAMITES_SAPLING.get();
        } else if (type.equals(ModWoodTypes.psaronius)) {
            return ModBlocks.PSARONIUS_SAPLING.get();
        } else if (type.equals(ModWoodTypes.magnolia)) {
            return ModBlocks.MAGNOLIA_SAPLING.get();
        } else if (type.equals(ModWoodTypes.ginkgo)) {
            return ModBlocks.GINKGO_SAPLING.get();
        } else if (type.equals(ModWoodTypes.phoenix)) {
            return ModBlocks.PHOENIX_SAPLING.get();
        }
        return null;
    }
}