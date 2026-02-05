package net.vit.jurassicreborn.common.blocks.entities.fence;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import java.util.Random;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/** Modern 1.19.2 rewrite of the electric fence base with fully reactive neighbour logic. */
public class ElectricFenceBaseBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty POLE = BooleanProperty.create("pole");
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final IntegerProperty CONNECTIONS = IntegerProperty.create("connections", 0, 4);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape EXTENDED_BOUNDS = Shapes.create(new AABB(0, 0, 0, 1, 1.5, 1));

    private final FenceType type;

    public ElectricFenceBaseBlock(FenceType type) {
        super(Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.METAL).strength(3.5F).noOcclusion());
        this.type = type;
        registerDefaultState(stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(POLE, false)
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(EAST, false)
                .setValue(CONNECTIONS, 0)
                .setValue(WATERLOGGED, false));
    }

    // ───────────────── Block‑entity ─────────────────
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ElectricFenceBaseBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return null;
    }

    // ───────────────── Placement & neighbour updates ────────────────
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction bias = ctx.getHorizontalDirection().getClockWise();
        boolean water = ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER;
        return updateConnections(ctx.getLevel(), ctx.getClickedPos(),
                defaultBlockState().setValue(FACING, bias).setValue(WATERLOGGED, water));
    }

    /** Rebuild connections after world load or neighbour replacement. */
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            level.setBlock(pos, updateConnections(level, pos, state), Block.UPDATE_ALL);
        }
    }

    @Override
    public BlockState updateShape(BlockState state,
                                  Direction dir,
                                  BlockState neighbour,
                                  LevelAccessor world,
                                  BlockPos pos,
                                  BlockPos neighbourPos) {

        // only do the pole side-effect on a real Level
        if (!world.isClientSide()
                && dir == Direction.UP
                && neighbour.getBlock() instanceof ElectricFencePoleBlock pole
                && world instanceof Level lvl) {
            pole.updateConnectedWires(lvl, neighbourPos);
        }

        BlockState updated = updateConnections(world, pos, state);

        if (updated.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        return updated;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos,
                                Block neighborBlock, BlockPos neighborPos, boolean moved) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, moved);

        if (level.isClientSide) {
            return;
        }

        BlockPos polePos = pos.above();
        BlockState poleState = level.getBlockState(polePos);
        if (!(poleState.getBlock() instanceof ElectricFencePoleBlock pole)) {
            return;
        }

        // Check the NEW power state immediately
        boolean wasPowered = poleState.getValue(ElectricFencePoleBlock.ACTIVE);
        boolean isPowered = ElectricFencePoleBlock.hasPoweredBase(level, polePos, pole.getType());
        boolean powerChanged = wasPowered != isPowered;

        // Update the pole's ACTIVE state immediately if power changed
        if (powerChanged) {
            level.setBlock(polePos,
                    poleState.setValue(ElectricFencePoleBlock.ACTIVE, isPowered),
                    Block.UPDATE_CLIENTS);

            // CRITICAL: Immediately update all connected wires with the new power state
            pole.updateConnectedWires(level, polePos, isPowered);
        }

        // Mark the pole's network as dirty if fence components changed
        BlockEntity be = level.getBlockEntity(polePos);
        BlockState neighbourState = level.getBlockState(neighborPos);
        boolean fenceComponent = neighbourState.getBlock() instanceof ElectricFenceWireBlock
                || neighbourState.getBlock() instanceof ElectricFencePoleBlock
                || neighbourState.getBlock() instanceof ElectricFenceBaseBlock
                || neighborBlock instanceof ElectricFenceWireBlock
                || neighborBlock instanceof ElectricFencePoleBlock
                || neighborBlock instanceof ElectricFenceBaseBlock;

        if (be instanceof ElectricFencePoleBlockEntity poleBe
                && (neighborPos.equals(polePos) || fenceComponent)) {
            poleBe.markNetworkDirty();
        }
    }

    private BlockState updateConnections(LevelAccessor world, BlockPos pos, BlockState state) {
        boolean n = canConnect(world.getBlockState(pos.north()));
        boolean s = canConnect(world.getBlockState(pos.south()));
        boolean w = canConnect(world.getBlockState(pos.west()));
        boolean e = canConnect(world.getBlockState(pos.east()));
        int con = (n ? 1 : 0) + (s ? 1 : 0) + (w ? 1 : 0) + (e ? 1 : 0);

        boolean pole = world.getBlockState(pos.above()).getBlock() instanceof ElectricFencePoleBlock;

        if (!pole && con == 0) { // bias arm
            con = 1;
            switch (state.getValue(FACING)) {
                case NORTH -> n = true;
                case SOUTH -> s = true;
                case EAST  -> e = true;
                default    -> w = true;
            }
        }

        return state
                .setValue(NORTH, n)
                .setValue(SOUTH, s)
                .setValue(WEST, w)
                .setValue(EAST, e)
                .setValue(POLE, pole)
                .setValue(CONNECTIONS, con);
    }

    // ───────────────── Visual / misc ─────────────────
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POLE, NORTH, SOUTH, EAST, WEST, CONNECTIONS, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state,
                              @Nullable BlockEntity blockEntity, ItemStack stack) {
        if (!level.isClientSide) {
            Random rand = level.random;
            for (int i = 0; i < 8; i++) {
                double dx = pos.getX() + 0.5 + (rand.nextDouble() - 0.5);
                double dy = pos.getY() + 0.5 + (rand.nextDouble() - 0.5);
                double dz = pos.getZ() + 0.5 + (rand.nextDouble() - 0.5);
                level.addParticle(ParticleTypes.ELECTRIC_SPARK, dx, dy, dz, 0, 0, 0);
            }
        }
        super.playerDestroy(level, player, pos, state, blockEntity, stack);
    }

    // ───────────────── Helpers ─────────────────
    private boolean canConnect(BlockState st) {
        return st.getBlock() instanceof ElectricFenceBaseBlock b && b.getType() == type;
    }

    public FenceType getType() {
        return type;
    }
}