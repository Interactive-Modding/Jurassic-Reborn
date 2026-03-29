package net.vit.jurassicreborn.common.blocks.wood;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.ModWoodTypes;
import net.vit.jurassicreborn.common.blocks.WoodButtonBlock;
import net.vit.jurassicreborn.common.plants.PlantHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class WoodBlocks {

    //tree family
    public static final DeferredHolder<Block, ModStrippableLogBlock> GINKGO_LOG = ModBlocks.MOD_BLOCKS.register("ginkgo_log",() -> new ModStrippableLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredHolder<Block, RotatedPillarBlock> STRIPPED_GINKGO_LOG = ModBlocks.MOD_BLOCKS.register("stripped_ginkgo_log",() -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));
    public static final DeferredHolder<Block, ModStrippableLogBlock> GINKGO_WOOD = ModBlocks.MOD_BLOCKS.register("ginkgo_wood",() -> new ModStrippableLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
    public static final DeferredHolder<Block, RotatedPillarBlock> STRIPPED_GINKGO_WOOD = ModBlocks.MOD_BLOCKS.register("stripped_ginkgo_wood",() -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));
    public static final DeferredHolder<Block, Block> GINKGO_LEAVES = ModBlocks.MOD_BLOCKS.register("ginkgo_leaves",() -> new AncientLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));

    //plank family
    public static final DeferredHolder<Block, Block> GINKGO_PLANKS = ModBlocks.MOD_BLOCKS.register("ginkgo_planks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final DeferredHolder<Block, Block> GINKGO_BUTTON = ModBlocks.MOD_BLOCKS.register("ginkgo_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));
    public static final DeferredHolder<Block, Block> GINKGO_FENCE = ModBlocks.MOD_BLOCKS.register("ginkgo_fence", () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)));
    public static final DeferredHolder<Block, Block> GINKGO_FENCE_GATE = ModBlocks.MOD_BLOCKS.register("ginkgo_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
    public static final DeferredHolder<Block, Block> GINKGO_PRESSURE_PLATE = ModBlocks.MOD_BLOCKS.register("ginkgo_pressure_plate", () -> new PressurePlateBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));
    public static final DeferredHolder<Block, Block> GINKGO_SIGN = ModBlocks.MOD_BLOCKS.register("ginkgo_sign", () -> new ModStandingSignBlock(ModWoodTypes.ginkgo, sign()));
    public static final DeferredHolder<Block, Block> GINKGO_WALL_SIGN = ModBlocks.MOD_BLOCKS.register("ginkgo_wall_sign", () -> new ModWallSignBlock(ModWoodTypes.ginkgo, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN).lootFrom(GINKGO_SIGN)));
    public static final DeferredHolder<Block, Block> GINKGO_HANGING_SIGN = ModBlocks.MOD_BLOCKS.register("ginkgo_hanging_sign", () -> new ModCeilingHangingSignBlock(ModWoodTypes.ginkgo, hangingSign()));
    public static final DeferredHolder<Block, Block> GINKGO_WALL_HANGING_SIGN = ModBlocks.MOD_BLOCKS.register("ginkgo_wall_hanging_sign", () -> new ModWallHangingSignBlock(ModWoodTypes.ginkgo, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN).lootFrom(GINKGO_HANGING_SIGN)));
    public static final DeferredHolder<Block, Block> GINKGO_SLAB = ModBlocks.MOD_BLOCKS.register("ginkgo_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));
    public static final DeferredHolder<Block, Block> GINKGO_STAIRS = ModBlocks.MOD_BLOCKS.register("ginkgo_stairs", () -> new StairBlock(GINKGO_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final DeferredHolder<Block, Block> GINKGO_DOOR = ModBlocks.MOD_BLOCKS.register("ginkgo_door", () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)));
    public static final DeferredHolder<Block, Block> GINKGO_TRAPDOOR = ModBlocks.MOD_BLOCKS.register("ginkgo_trapdoor", () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)));


    //tree family
    public static final DeferredHolder<Block, ModStrippableLogBlock> CALAMITES_LOG = ModBlocks.MOD_BLOCKS.register("calamites_log",() -> new ModStrippableLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredHolder<Block, RotatedPillarBlock> STRIPPED_CALAMITES_LOG = ModBlocks.MOD_BLOCKS.register("stripped_calamites_log",() -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));
    public static final DeferredHolder<Block, ModStrippableLogBlock> CALAMITES_WOOD = ModBlocks.MOD_BLOCKS.register("calamites_wood",() -> new ModStrippableLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
    public static final DeferredHolder<Block, RotatedPillarBlock> STRIPPED_CALAMITES_WOOD = ModBlocks.MOD_BLOCKS.register("stripped_calamites_wood",() -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));
    public static final DeferredHolder<Block, Block> CALAMITES_LEAVES = ModBlocks.MOD_BLOCKS.register("calamites_leaves",() -> new AncientLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));

    //plank family
    public static final DeferredHolder<Block, Block> CALAMITES_PLANKS = ModBlocks.MOD_BLOCKS.register("calamites_planks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final DeferredHolder<Block, Block> CALAMITES_BUTTON = ModBlocks.MOD_BLOCKS.register("calamites_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));
    public static final DeferredHolder<Block, Block> CALAMITES_FENCE = ModBlocks.MOD_BLOCKS.register("calamites_fence", () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)));
    public static final DeferredHolder<Block, Block> CALAMITES_FENCE_GATE = ModBlocks.MOD_BLOCKS.register("calamites_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
    public static final DeferredHolder<Block, Block> CALAMITES_PRESSURE_PLATE = ModBlocks.MOD_BLOCKS.register("calamites_pressure_plate", () -> new PressurePlateBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));
    public static final DeferredHolder<Block, Block> CALAMITES_SIGN = ModBlocks.MOD_BLOCKS.register("calamites_sign", () -> new ModStandingSignBlock(ModWoodTypes.calamites, sign()));
    public static final DeferredHolder<Block, Block> CALAMITES_WALL_SIGN = ModBlocks.MOD_BLOCKS.register("calamites_wall_sign", () -> new ModWallSignBlock(ModWoodTypes.calamites, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN).lootFrom(CALAMITES_SIGN)));
    public static final DeferredHolder<Block, Block> CALAMITES_HANGING_SIGN = ModBlocks.MOD_BLOCKS.register("calamites_hanging_sign", () -> new ModCeilingHangingSignBlock(ModWoodTypes.calamites, hangingSign()));
    public static final DeferredHolder<Block, Block> CALAMITES_WALL_HANGING_SIGN = ModBlocks.MOD_BLOCKS.register("calamites_wall_hanging_sign", () -> new ModWallHangingSignBlock(ModWoodTypes.calamites, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN).lootFrom(CALAMITES_HANGING_SIGN)));
    public static final DeferredHolder<Block, Block> CALAMITES_SLAB = ModBlocks.MOD_BLOCKS.register("calamites_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));
    public static final DeferredHolder<Block, Block> CALAMITES_STAIRS = ModBlocks.MOD_BLOCKS.register("calamites_stairs", () -> new StairBlock(CALAMITES_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final DeferredHolder<Block, Block> CALAMITES_DOOR = ModBlocks.MOD_BLOCKS.register("calamites_door", () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)));
    public static final DeferredHolder<Block, Block> CALAMITES_TRAPDOOR = ModBlocks.MOD_BLOCKS.register("calamites_trapdoor", () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)));
    //tree family
    public static final DeferredHolder<Block, ModStrippableLogBlock> ARAUCARIA_LOG = ModBlocks.MOD_BLOCKS.register("araucaria_log",() -> new ModStrippableLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredHolder<Block, RotatedPillarBlock> STRIPPED_ARAUCARIA_LOG = ModBlocks.MOD_BLOCKS.register("stripped_araucaria_log",() -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));
    public static final DeferredHolder<Block, ModStrippableLogBlock> ARAUCARIA_WOOD = ModBlocks.MOD_BLOCKS.register("araucaria_wood",() -> new ModStrippableLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
    public static final DeferredHolder<Block, RotatedPillarBlock> STRIPPED_ARAUCARIA_WOOD = ModBlocks.MOD_BLOCKS.register("stripped_araucaria_wood",() -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));
    public static final DeferredHolder<Block, Block> ARAUCARIA_LEAVES = ModBlocks.MOD_BLOCKS.register("araucaria_leaves",() -> new AncientLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));

    //plank family
    public static final DeferredHolder<Block, Block> ARAUCARIA_PLANKS = ModBlocks.MOD_BLOCKS.register("araucaria_planks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final DeferredHolder<Block, Block> ARAUCARIA_BUTTON = ModBlocks.MOD_BLOCKS.register("araucaria_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));
    public static final DeferredHolder<Block, Block> ARAUCARIA_FENCE = ModBlocks.MOD_BLOCKS.register("araucaria_fence", () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)));
    public static final DeferredHolder<Block, Block> ARAUCARIA_FENCE_GATE = ModBlocks.MOD_BLOCKS.register("araucaria_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
    public static final DeferredHolder<Block, Block> ARAUCARIA_PRESSURE_PLATE = ModBlocks.MOD_BLOCKS.register("araucaria_pressure_plate", () -> new PressurePlateBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));
    public static final DeferredHolder<Block, Block> ARAUCARIA_SIGN = ModBlocks.MOD_BLOCKS.register("araucaria_sign", () -> new ModStandingSignBlock(ModWoodTypes.araucaria, sign()));
    public static final DeferredHolder<Block, Block> ARAUCARIA_WALL_SIGN = ModBlocks.MOD_BLOCKS.register("araucaria_wall_sign", () -> new ModWallSignBlock(ModWoodTypes.araucaria, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN).lootFrom(ARAUCARIA_SIGN)));
    public static final DeferredHolder<Block, Block> ARAUCARIA_HANGING_SIGN = ModBlocks.MOD_BLOCKS.register("araucaria_hanging_sign", () -> new ModCeilingHangingSignBlock(ModWoodTypes.araucaria, hangingSign()));
    public static final DeferredHolder<Block, Block> ARAUCARIA_WALL_HANGING_SIGN = ModBlocks.MOD_BLOCKS.register("araucaria_wall_hanging_sign", () -> new ModWallHangingSignBlock(ModWoodTypes.araucaria, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN).lootFrom(ARAUCARIA_HANGING_SIGN)));
    public static final DeferredHolder<Block, Block> ARAUCARIA_SLAB = ModBlocks.MOD_BLOCKS.register("araucaria_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));
    public static final DeferredHolder<Block, Block> ARAUCARIA_STAIRS = ModBlocks.MOD_BLOCKS.register("araucaria_stairs", () -> new StairBlock(ARAUCARIA_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final DeferredHolder<Block, Block> ARAUCARIA_DOOR = ModBlocks.MOD_BLOCKS.register("araucaria_door", () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)));
    public static final DeferredHolder<Block, Block> ARAUCARIA_TRAPDOOR = ModBlocks.MOD_BLOCKS.register("araucaria_trapdoor", () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)));
    //tree family
    public static final DeferredHolder<Block, ModStrippableLogBlock> PHOENIX_LOG = ModBlocks.MOD_BLOCKS.register("phoenix_log",() -> new ModStrippableLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredHolder<Block, RotatedPillarBlock> STRIPPED_PHOENIX_LOG = ModBlocks.MOD_BLOCKS.register("stripped_phoenix_log",() -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));
    public static final DeferredHolder<Block, ModStrippableLogBlock> PHOENIX_WOOD = ModBlocks.MOD_BLOCKS.register("phoenix_wood",() -> new ModStrippableLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
    public static final DeferredHolder<Block, RotatedPillarBlock> STRIPPED_PHOENIX_WOOD = ModBlocks.MOD_BLOCKS.register("stripped_phoenix_wood",() -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));
    public static final DeferredHolder<Block, Block> PHOENIX_LEAVES = ModBlocks.MOD_BLOCKS.register("phoenix_leaves",() -> new AncientLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));

    //plank family
    public static final DeferredHolder<Block, Block> PHOENIX_PLANKS = ModBlocks.MOD_BLOCKS.register("phoenix_planks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final DeferredHolder<Block, Block> PHOENIX_BUTTON = ModBlocks.MOD_BLOCKS.register("phoenix_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));
    public static final DeferredHolder<Block, Block> PHOENIX_FENCE = ModBlocks.MOD_BLOCKS.register("phoenix_fence", () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)));
    public static final DeferredHolder<Block, Block> PHOENIX_FENCE_GATE = ModBlocks.MOD_BLOCKS.register("phoenix_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
    public static final DeferredHolder<Block, Block> PHOENIX_PRESSURE_PLATE = ModBlocks.MOD_BLOCKS.register("phoenix_pressure_plate", () -> new PressurePlateBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));
    public static final DeferredHolder<Block, Block> PHOENIX_SIGN = ModBlocks.MOD_BLOCKS.register("phoenix_sign", () -> new ModStandingSignBlock(ModWoodTypes.phoenix, sign()));
    public static final DeferredHolder<Block, Block> PHOENIX_WALL_SIGN = ModBlocks.MOD_BLOCKS.register("phoenix_wall_sign", () -> new ModWallSignBlock(ModWoodTypes.phoenix, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN).lootFrom(PHOENIX_SIGN)));
    public static final DeferredHolder<Block, Block> PHOENIX_HANGING_SIGN = ModBlocks.MOD_BLOCKS.register("phoenix_hanging_sign", () -> new ModCeilingHangingSignBlock(ModWoodTypes.phoenix, hangingSign()));
    public static final DeferredHolder<Block, Block> PHOENIX_WALL_HANGING_SIGN = ModBlocks.MOD_BLOCKS.register("phoenix_wall_hanging_sign", () -> new ModWallHangingSignBlock(ModWoodTypes.phoenix, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN).lootFrom(PHOENIX_HANGING_SIGN)));
    public static final DeferredHolder<Block, Block> PHOENIX_SLAB = ModBlocks.MOD_BLOCKS.register("phoenix_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));
    public static final DeferredHolder<Block, Block> PHOENIX_STAIRS = ModBlocks.MOD_BLOCKS.register("phoenix_stairs", () -> new StairBlock(PHOENIX_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final DeferredHolder<Block, Block> PHOENIX_DOOR = ModBlocks.MOD_BLOCKS.register("phoenix_door", () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)));
    public static final DeferredHolder<Block, Block> PHOENIX_TRAPDOOR = ModBlocks.MOD_BLOCKS.register("phoenix_trapdoor", () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)));


    //tree family
    public static final DeferredHolder<Block, ModStrippableLogBlock> PSARONIUS_LOG = ModBlocks.MOD_BLOCKS.register("psaronius_log",() -> new ModStrippableLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredHolder<Block, RotatedPillarBlock> STRIPPED_PSARONIUS_LOG = ModBlocks.MOD_BLOCKS.register("stripped_psaronius_log",() -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));
    public static final DeferredHolder<Block, ModStrippableLogBlock> PSARONIUS_WOOD = ModBlocks.MOD_BLOCKS.register("psaronius_wood",() -> new ModStrippableLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
    public static final DeferredHolder<Block, RotatedPillarBlock> STRIPPED_PSARONIUS_WOOD = ModBlocks.MOD_BLOCKS.register("stripped_psaronius_wood",() -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));
    public static final DeferredHolder<Block, Block> PSARONIUS_LEAVES = ModBlocks.MOD_BLOCKS.register("psaronius_leaves",() -> new AncientLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));

    //plank family
    public static final DeferredHolder<Block, Block> PSARONIUS_PLANKS = ModBlocks.MOD_BLOCKS.register("psaronius_planks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final DeferredHolder<Block, Block> PSARONIUS_BUTTON = ModBlocks.MOD_BLOCKS.register("psaronius_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));
    public static final DeferredHolder<Block, Block> PSARONIUS_FENCE = ModBlocks.MOD_BLOCKS.register("psaronius_fence", () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)));
    public static final DeferredHolder<Block, Block> PSARONIUS_FENCE_GATE = ModBlocks.MOD_BLOCKS.register("psaronius_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
    public static final DeferredHolder<Block, Block> PSARONIUS_PRESSURE_PLATE = ModBlocks.MOD_BLOCKS.register("psaronius_pressure_plate", () -> new PressurePlateBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));
    public static final DeferredHolder<Block, Block> PSARONIUS_SIGN = ModBlocks.MOD_BLOCKS.register("psaronius_sign", () -> new ModStandingSignBlock(ModWoodTypes.psaronius, sign()));
    public static final DeferredHolder<Block, Block> PSARONIUS_WALL_SIGN = ModBlocks.MOD_BLOCKS.register("psaronius_wall_sign", () -> new ModWallSignBlock(ModWoodTypes.psaronius, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN).lootFrom(PSARONIUS_SIGN)));
    public static final DeferredHolder<Block, Block> PSARONIUS_HANGING_SIGN = ModBlocks.MOD_BLOCKS.register("psaronius_hanging_sign", () -> new ModCeilingHangingSignBlock(ModWoodTypes.psaronius, hangingSign()));
    public static final DeferredHolder<Block, Block> PSARONIUS_WALL_HANGING_SIGN = ModBlocks.MOD_BLOCKS.register("psaronius_wall_hanging_sign", () -> new ModWallHangingSignBlock(ModWoodTypes.psaronius, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN).lootFrom(PSARONIUS_HANGING_SIGN)));
    public static final DeferredHolder<Block, Block> PSARONIUS_SLAB = ModBlocks.MOD_BLOCKS.register("psaronius_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));
    public static final DeferredHolder<Block, Block> PSARONIUS_STAIRS = ModBlocks.MOD_BLOCKS.register("psaronius_stairs", () -> new StairBlock(PSARONIUS_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final DeferredHolder<Block, Block> PSARONIUS_DOOR = ModBlocks.MOD_BLOCKS.register("psaronius_door", () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)));
    public static final DeferredHolder<Block, Block> PSARONIUS_TRAPDOOR = ModBlocks.MOD_BLOCKS.register("psaronius_trapdoor", () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)));

    //tree family
    public static final DeferredHolder<Block, ModStrippableLogBlock> MAGNOLIA_LOG = ModBlocks.MOD_BLOCKS.register("magnolia_log",() -> new ModStrippableLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredHolder<Block, RotatedPillarBlock> STRIPPED_MAGNOLIA_LOG = ModBlocks.MOD_BLOCKS.register("stripped_magnolia_log",() -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));
    public static final DeferredHolder<Block, ModStrippableLogBlock> MAGNOLIA_WOOD = ModBlocks.MOD_BLOCKS.register("magnolia_wood",() -> new ModStrippableLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));
    public static final DeferredHolder<Block, RotatedPillarBlock> STRIPPED_MAGNOLIA_WOOD = ModBlocks.MOD_BLOCKS.register("stripped_magnolia_wood",() -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));
    public static final DeferredHolder<Block, Block> MAGNOLIA_LEAVES = ModBlocks.MOD_BLOCKS.register("magnolia_leaves",() -> new AncientLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));

    //plank family
    public static final DeferredHolder<Block, Block> MAGNOLIA_PLANKS = ModBlocks.MOD_BLOCKS.register("magnolia_planks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final DeferredHolder<Block, Block> MAGNOLIA_BUTTON = ModBlocks.MOD_BLOCKS.register("magnolia_button", () -> new WoodButtonBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));
    public static final DeferredHolder<Block, Block> MAGNOLIA_FENCE = ModBlocks.MOD_BLOCKS.register("magnolia_fence", () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)));
    public static final DeferredHolder<Block, Block> MAGNOLIA_FENCE_GATE = ModBlocks.MOD_BLOCKS.register("magnolia_fence_gate", () -> new FenceGateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE), SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE));
    public static final DeferredHolder<Block, Block> MAGNOLIA_PRESSURE_PLATE = ModBlocks.MOD_BLOCKS.register("magnolia_pressure_plate", () -> new PressurePlateBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));
    public static final DeferredHolder<Block, Block> MAGNOLIA_SIGN = ModBlocks.MOD_BLOCKS.register("magnolia_sign", () -> new ModStandingSignBlock(ModWoodTypes.magnolia, sign()));
    public static final DeferredHolder<Block, Block> MAGNOLIA_WALL_SIGN = ModBlocks.MOD_BLOCKS.register("magnolia_wall_sign", () -> new ModWallSignBlock(ModWoodTypes.magnolia, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN).lootFrom(MAGNOLIA_SIGN)));
    public static final DeferredHolder<Block, Block> MAGNOLIA_HANGING_SIGN = ModBlocks.MOD_BLOCKS.register("magnolia_hanging_sign", () -> new ModCeilingHangingSignBlock(ModWoodTypes.magnolia, hangingSign()));
    public static final DeferredHolder<Block, Block> MAGNOLIA_WALL_HANGING_SIGN = ModBlocks.MOD_BLOCKS.register("magnolia_wall_hanging_sign", () -> new ModWallHangingSignBlock(ModWoodTypes.magnolia, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN).lootFrom(MAGNOLIA_HANGING_SIGN)));
    public static final DeferredHolder<Block, Block> MAGNOLIA_SLAB = ModBlocks.MOD_BLOCKS.register("magnolia_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));
    public static final DeferredHolder<Block, Block> MAGNOLIA_STAIRS = ModBlocks.MOD_BLOCKS.register("magnolia_stairs", () -> new StairBlock(MAGNOLIA_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final DeferredHolder<Block, Block> MAGNOLIA_DOOR = ModBlocks.MOD_BLOCKS.register("magnolia_door", () -> new DoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)));
    public static final DeferredHolder<Block, Block> MAGNOLIA_TRAPDOOR = ModBlocks.MOD_BLOCKS.register("magnolia_trapdoor", () -> new TrapDoorBlock(BlockSetType.OAK, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)));

    public static final DeferredHolder<Block, PetrifiedLogBlock> PETRIFIED_ARAUCARIA_LOG = ModBlocks.MOD_BLOCKS.register("petrified_araucaria_log", () -> new PetrifiedLogBlock(PlantHandler.ARAUCARIA, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredHolder<Block, PetrifiedLogBlock> PETRIFIED_CALAMITES_LOG = ModBlocks.MOD_BLOCKS.register("petrified_calamites_log", () -> new PetrifiedLogBlock(PlantHandler.CALAMITES, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredHolder<Block, PetrifiedLogBlock> PETRIFIED_GINKGO_LOG = ModBlocks.MOD_BLOCKS.register("petrified_ginkgo_log", () -> new PetrifiedLogBlock(PlantHandler.GINKGO, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredHolder<Block, PetrifiedLogBlock> PETRIFIED_MAGNOLIA_LOG = ModBlocks.MOD_BLOCKS.register("petrified_magnolia_log", () -> new PetrifiedLogBlock(PlantHandler.MAGNOLIA, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredHolder<Block, PetrifiedLogBlock> PETRIFIED_PSARONIUS_LOG = ModBlocks.MOD_BLOCKS.register("petrified_psaronius_log", () -> new PetrifiedLogBlock(PlantHandler.PSARONIUS, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static final DeferredHolder<Block, PetrifiedLogBlock> PETRIFIED_PHOENIX_LOG = ModBlocks.MOD_BLOCKS.register("petrified_phoenix_log", () -> new PetrifiedLogBlock(PlantHandler.PHOENIX, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
    public static BlockBehaviour.Properties sign() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN).noCollission().strength(1.0F).sound(SoundType.WOOD);
    }

    public static BlockBehaviour.Properties hangingSign() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN).noCollission().strength(1.0F).sound(SoundType.WOOD);
    }

    public static void register() {
        System.out.println("REGISTERING WOOD BLOCKS");
    }

    public static List<DeferredHolder<Block, ? extends Block>> petrifiedLogs() {
        return ModBlocks.MOD_BLOCKS.getEntries().stream()
                .filter(holder -> holder.getId().getPath().startsWith("petrified"))
                .toList();
    }

    public static List<Block> getSigns() {
        return ModBlocks.MOD_BLOCKS.getEntries().stream()
                .map(DeferredHolder::get)
                .map(Block.class::cast)
                .filter(block -> block instanceof SignBlock)
                .toList();
    }

    private static List<Block> CACHED_HANGING_SIGNS;

    public static List<Block> getHangingSigns() {
        if (CACHED_HANGING_SIGNS == null) {
            CACHED_HANGING_SIGNS = ModBlocks.MOD_BLOCKS.getEntries().stream()
                    .map(holder -> (Block) holder.get()) // ⬅ explicit cast
                    .filter(block ->
                            block instanceof CeilingHangingSignBlock ||
                                    block instanceof WallHangingSignBlock
                    )
                    .toList();
        }
        return CACHED_HANGING_SIGNS;
    }

    private static Map<Block, Block> strippedByBlock;

    public static Map<Block, Block> getStrippedByBlock() {
        if (strippedByBlock == null) {
            strippedByBlock = Map.ofEntries(
                    Map.entry(WoodBlocks.GINKGO_LOG.get(), WoodBlocks.STRIPPED_GINKGO_LOG.get()),
                    Map.entry(WoodBlocks.GINKGO_WOOD.get(), WoodBlocks.STRIPPED_GINKGO_WOOD.get()),

                    Map.entry(WoodBlocks.CALAMITES_LOG.get(), WoodBlocks.STRIPPED_CALAMITES_LOG.get()),
                    Map.entry(WoodBlocks.CALAMITES_WOOD.get(), WoodBlocks.STRIPPED_CALAMITES_WOOD.get()),

                    Map.entry(WoodBlocks.ARAUCARIA_LOG.get(), WoodBlocks.STRIPPED_ARAUCARIA_LOG.get()),
                    Map.entry(WoodBlocks.ARAUCARIA_WOOD.get(), WoodBlocks.STRIPPED_ARAUCARIA_WOOD.get()),

                    Map.entry(WoodBlocks.PHOENIX_LOG.get(), WoodBlocks.STRIPPED_PHOENIX_LOG.get()),
                    Map.entry(WoodBlocks.PHOENIX_WOOD.get(), WoodBlocks.STRIPPED_PHOENIX_WOOD.get()),

                    Map.entry(WoodBlocks.PSARONIUS_LOG.get(), WoodBlocks.STRIPPED_PSARONIUS_LOG.get()),
                    Map.entry(WoodBlocks.PSARONIUS_WOOD.get(), WoodBlocks.STRIPPED_PSARONIUS_WOOD.get()),

                    Map.entry(WoodBlocks.MAGNOLIA_LOG.get(), WoodBlocks.STRIPPED_MAGNOLIA_LOG.get()),
                    Map.entry(WoodBlocks.MAGNOLIA_WOOD.get(), WoodBlocks.STRIPPED_MAGNOLIA_WOOD.get())
            );
        }
        return strippedByBlock;
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