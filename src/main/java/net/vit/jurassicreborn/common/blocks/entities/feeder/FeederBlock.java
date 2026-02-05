package net.vit.jurassicreborn.common.blocks.entities.feeder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.items.PaleoPadItem;
import net.vit.jurassicreborn.common.paleopad.FeederTrackerApp;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class FeederBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static DirectionProperty FACING = DirectionalBlock.FACING;

    public static final VoxelShape MODEL_SHAPE_NORTH = Shapes.join(
            Block.box(0, 0, 0, 16, 3, 16),
            Stream.of(
                    Block.box(1.5, 2.45, 1.5, 14.5, 4.45, 14.5),
                    Stream.of(
                            Block.box(13.75, 2, 1.5, 15.25, 3.75, 14.5),
                            Block.box(13.95, 2.25, 1.5, 14.95, 4.075, 14.5),
                            Block.box(13.45, 2, 1.5, 15.55, 3.45, 14.5)
                    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
                    Stream.of(
                            Block.box(1.5, 2, 0.75, 14.5, 3.75, 1.95),
                            Block.box(1.5, 2.25, 1.05, 14.5, 4.075, 1.95),
                            Block.box(1.5, 2, 0.45, 14.5, 3.45, 1.95)
                    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
                    Stream.of(
                            Block.box(0.75, 2, 1.5, 1.95, 3.75, 14.5),
                            Block.box(1.05, 2.25, 1.5, 1.95, 4.075, 14.5),
                            Block.box(0.45, 2, 1.5, 1.975, 3.45, 14.5)
                    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
                    Stream.of(
                            Block.box(1.5, 2, 14.05, 14.5, 3.75, 15.25),
                            Block.box(1.5, 2.25, 14.05, 14.5, 4.075, 14.95),
                            Block.box(1.5, 2, 14.05, 14.5, 3.45, 15.55)
                    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
                    Stream.of(
                            Block.box(3, 2.45, 3, 13, 11.45, 13),
                            Stream.of(
                                    Block.box(2, 5.45, 2, 14, 6.45, 14),
                                    Block.box(2, 9.45, 2, 14, 10.45, 14),
                                    Block.box(2, 7.45, 2, 14, 8.45, 14)
                            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
                            Stream.of(
                                    Block.box(3.5, 10.95, 3.5, 12.5, 13.95, 12.5),
                                    Block.box(4, 13.45, 4, 12, 14.45, 8),
                                    Block.box(4, 13.45, 8, 12, 14.45, 12)
                            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()
                    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            BooleanOp.OR
    );

    public FeederBlock(Properties props) {
        super(props);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.UP).setValue(WATERLOGGED, false));
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        // Safe ticker: only tick our own BE type
        return type == ModBlockEntities.FEEDER.get()
                ? (lvl, pos, st, be) -> ((FeederBlockEntity) be).tick(lvl, pos, st, (FeederBlockEntity) be)
                : null;
    }

    @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new FeederBlockEntity(pos, state); }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!oldState.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof FeederBlockEntity feeder) {
                Containers.dropContents(level, pos, feeder);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            if (!level.isClientSide) {
                for (Player player : level.players()) {
                    FeederTrackerApp.removeFeeder(player, pos);
                }
            }
            super.onRemove(oldState, level, pos, newState, isMoving);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.getItemInHand(hand).getItem() instanceof PaleoPadItem) return InteractionResult.PASS;

        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof MenuProvider provider && player instanceof ServerPlayer sp) {
                NetworkHooks.openGui(sp, provider, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction dir = ctx.getHorizontalDirection().getOpposite();
        FluidState fs = ctx.getLevel().getFluidState(ctx.getClickedPos());
        return this.defaultBlockState()
                .setValue(FACING, dir)
                .setValue(WATERLOGGED, fs.getType() == Fluids.WATER);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(FACING, WATERLOGGED);
    }

    @Override
    public BlockState updateShape(BlockState state,
                                  Direction direction,
                                  BlockState neighbourState,
                                  LevelAccessor level,
                                  BlockPos pos,
                                  BlockPos neighbourPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighbourState, level, pos, neighbourPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getShape(BlockState st, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return switch (st.getValue(FACING)) {
            case EAST, WEST, NORTH, SOUTH -> MODEL_SHAPE_NORTH;
            default -> Block.box(1, 0, 1, 15, 14, 15);
        };
    }
}
