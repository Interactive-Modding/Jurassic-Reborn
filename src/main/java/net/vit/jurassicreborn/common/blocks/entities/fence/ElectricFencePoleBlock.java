package net.vit.jurassicreborn.common.blocks.entities.fence;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

/** Forge 1.19.2 rewrite of the old ElectricFencePoleBlock. */
public class ElectricFencePoleBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final MapCodec<ElectricFencePoleBlock> LOW_CODEC =
            MapCodec.unit(() -> ModBlocks.LOW_SECURITY_FENCE_POLE.get());
    public static final MapCodec<ElectricFencePoleBlock> MED_CODEC =
            MapCodec.unit(() -> ModBlocks.MED_SECURITY_FENCE_POLE.get());
    public static final MapCodec<ElectricFencePoleBlock> HIGH_CODEC =
            MapCodec.unit(() -> ModBlocks.HIGH_SECURITY_FENCE_POLE.get());

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return switch (type) {
            case LOW -> LOW_CODEC;
            case MED -> MED_CODEC;
            case HIGH -> HIGH_CODEC;
        };
    }

    /* ------------------------- block-state properties ------------------------- */
    public static final BooleanProperty NORTH   = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH   = BooleanProperty.create("south");
    public static final BooleanProperty WEST    = BooleanProperty.create("west");
    public static final BooleanProperty EAST    = BooleanProperty.create("east");
    public static final BooleanProperty ACTIVE  = BooleanProperty.create("active");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    /* Post shape: 0.3425 – 0.6575 → (5.48, 10.52) on the 0-16 grid. */
    private static final VoxelShape POST_SHAPE =
            Block.box(5.48, 0.0, 5.48, 10.52, 16.0, 10.52);

    private final FenceType type;
    /* ------------------------------------------------------------------------- */
    public ElectricFencePoleBlock(FenceType type, BlockBehaviour.Properties props) {
        super(props.noOcclusion().strength(3.0F));
        this.type = type;

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ACTIVE, false)
                .setValue(NORTH,  false)
                .setValue(SOUTH,  false)
                .setValue(EAST,   false)
                .setValue(WEST,   false)
                .setValue(WATERLOGGED, false));
    }

    /* ------------------------------ shapes ----------------------------------- */
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level,
                               BlockPos pos, CollisionContext ctx) {
        return POST_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level,
                                        BlockPos pos, CollisionContext ctx) {
        return POST_SHAPE;
    }

    /* --------------------------- render & BE ---------------------------------- */
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;          // change to ENTITYBLOCK_ANIMATED if you use a BER
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ElectricFencePoleBlockEntity(pos, state);
    }

    /* ------------------------- state definition ------------------------------ */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(ACTIVE, NORTH, SOUTH, EAST, WEST, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    /* --------------------------- placement ----------------------------------- */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();

        boolean water = level.getFluidState(pos).getType() == Fluids.WATER;

        return defaultBlockState()
                .setValue(WATERLOGGED, water)
                .setValue(NORTH,  canConnect(level, pos.north(),  Direction.NORTH))
                .setValue(SOUTH,  canConnect(level, pos.south(),  Direction.SOUTH))
                .setValue(EAST,   canConnect(level, pos.east(),   Direction.EAST))
                .setValue(WEST,   canConnect(level, pos.west(),   Direction.WEST))
                .setValue(ACTIVE, hasPoweredBase(level, pos));
    }

    /*  When any neighbouring block changes, Mojang calls updateShape first,
        then neighbourChanged; updateShape is where we recalc booleans.          */
    @Override
    public BlockState updateShape(BlockState state, Direction dir,
                                  BlockState neighbour, LevelAccessor level,
                                  BlockPos pos, BlockPos neighbourPos) {


        if (dir == Direction.NORTH)
            state = state.setValue(NORTH, canConnect(level, neighbourPos, dir));
        else if (dir == Direction.SOUTH)
            state = state.setValue(SOUTH, canConnect(level, neighbourPos, dir));
        else if (dir == Direction.EAST)
            state = state.setValue(EAST,  canConnect(level, neighbourPos, dir));
        else if (dir == Direction.WEST)
            state = state.setValue(WEST,  canConnect(level, neighbourPos, dir));

        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        if (!level.isClientSide() && level instanceof Level lvl) {
            state = state.setValue(ACTIVE, hasPoweredBase(lvl, pos));
        }
        return state;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos,
                                Block neighborBlock, BlockPos neighborPos,
                                boolean moved) {

        if (level.isClientSide) {
            return;
        }

        boolean powered = hasPoweredBase(level, pos);
        boolean powerChanged = powered != state.getValue(ACTIVE);

        boolean columnUpdate = neighborPos.getX() == pos.getX()
                && neighborPos.getZ() == pos.getZ()
                && neighborPos.getY() != pos.getY();

        BlockState neighbourState = level.getBlockState(neighborPos);
        boolean fenceComponent = neighbourState.getBlock() instanceof ElectricFenceWireBlock
                || neighbourState.getBlock() instanceof ElectricFencePoleBlock
                || neighbourState.getBlock() instanceof ElectricFenceBaseBlock
                || neighborBlock instanceof ElectricFenceWireBlock
                || neighborBlock instanceof ElectricFencePoleBlock
                || neighborBlock instanceof ElectricFenceBaseBlock;

        if (powerChanged) {
            level.setBlock(pos,
                    state.setValue(ACTIVE, powered),
                    Block.UPDATE_CLIENTS);
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof ElectricFencePoleBlockEntity poleBe && (columnUpdate || fenceComponent)) {
            poleBe.markNetworkDirty();
        }

        if (powerChanged || columnUpdate || fenceComponent) {
            updateConnectedWires(level, pos, powered);
        }
    }

    /* --------------------------- redstone helpers ---------------------------- */
    /* ----------------------------- connectivity ------------------------------ */
    private boolean canConnect(LevelAccessor world, BlockPos pos,
                               Direction dir) {
        BlockState st = world.getBlockState(pos);
        Block blk = st.getBlock();

        if (blk instanceof ElectricFenceWireBlock wire &&
                wire.getType() == type) return true;

        if (blk instanceof ElectricFencePoleBlock pole &&
                pole.type == type) return true;

        BlockState down = world.getBlockState(pos.below());
        return down.getBlock() instanceof ElectricFenceWireBlock wire2 &&
                down.getValue(ElectricFenceWireBlock.UP_DIRECTION)
                        .getOpposite() == dir;
    }

    /* --------------------- recurse & power connected wires ------------------- */
    void updateConnectedWires(Level level, BlockPos pos) {
        updateConnectedWires(level, pos, hasPoweredBase(level, pos));
    }

    void updateConnectedWires(Level level, BlockPos pos, boolean powered) {
        Iterable<BlockPos> targets = getConnectedWirePositions(level, pos);
        for (BlockPos wirePos : targets) {
            BlockEntity be = level.getBlockEntity(wirePos);
            if (be instanceof ElectricFenceWireBlockEntity wire) {
                wire.power(pos, powered);
            }
        }
    }

    private Iterable<BlockPos> getConnectedWirePositions(Level level, BlockPos polePos) {
        BlockEntity be = level.getBlockEntity(polePos);
        if (be instanceof ElectricFencePoleBlockEntity poleBe) {
            return poleBe.getOrRebuildNetwork(level, polePos, type);
        }

        HashSet<BlockPos> bases = new HashSet<>();
        HashSet<BlockPos> wires = new HashSet<>();
        collectNetwork(level, polePos, type, bases, wires);
        return wires;
    }

    static void collectNetwork(Level level,
                               BlockPos polePos,
                               FenceType type,
                               Set<BlockPos> bases,
                               Set<BlockPos> wires) {
        BlockPos origin = polePos.below();
        if (origin.getY() < level.getMinBuildHeight()) {
            return;
        }

        processConnectedWires(level, origin, origin, type, bases, wires);
    }

    private static void processConnectedWires(Level level,
                                              BlockPos origin,
                                              BlockPos current,
                                              FenceType type,
                                              Set<BlockPos> bases,
                                              Set<BlockPos> wires) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (!((dx == dz || dx == -dz) && dy != 0)) {
                        BlockPos offset = current.offset(dx, dy, dz);
                        if (!bases.add(offset)) {
                            continue;
                        }

                        BlockState st = level.getBlockState(offset);
                        if (!(st.getBlock() instanceof ElectricFenceBaseBlock base)
                                || base.getType() != type) {
                            continue;
                        }

                        int dX = offset.getX() - origin.getX();
                        int dZ = offset.getZ() - origin.getZ();
                        if ((dX * dX) + (dZ * dZ) > 64) {
                            continue;
                        }

                        ChunkAccess chunk = level.getChunk(offset);
                        int y = offset.getY();
                        while (true) {
                            BlockPos wirePos = new BlockPos(offset.getX(), ++y, offset.getZ());
                            BlockState wireState = chunk.getBlockState(wirePos);
                            if (!(wireState.getBlock() instanceof ElectricFenceWireBlock wire)
                                    || wire.getType() != type) {
                                break;
                            }
                            wires.add(wirePos.immutable());
                        }

                        processConnectedWires(level, origin, offset, type, bases, wires);
                    }
                }
            }
        }
    }

    /* ------------------------------- misc ------------------------------------ */
    public FenceType getType() {
        return type;
    }

    private boolean hasPoweredBase(LevelAccessor level, BlockPos pos) {
        return hasPoweredBase(level, pos, this.type);
    }

    public static boolean hasPoweredBase(LevelAccessor level, BlockPos polePos, FenceType expectedType) {
        MutableBlockPos cursor = polePos.mutable();
        int min = level.getMinBuildHeight();

        while (cursor.getY() > min) {
            cursor.move(Direction.DOWN);
            BlockState belowState = level.getBlockState(cursor);
            Block block = belowState.getBlock();

            if (block instanceof ElectricFenceBaseBlock base) {
                if (base.getType() != expectedType) {
                    return false;
                }
                if (level instanceof Level lvl) {
                    return lvl.hasNeighborSignal(cursor);
                }
                return false;
            }

            if (block instanceof ElectricFencePoleBlock pole) {
                if (pole.getType() != expectedType) {
                    return false;
                }
                continue;
            }

            if (block instanceof ElectricFenceWireBlock wire) {
                if (wire.getType() != expectedType) {
                    return false;
                }
                continue;
            }

            if (!belowState.isAir()) {
                return false;
            }
        }

        return false;
    }
}
