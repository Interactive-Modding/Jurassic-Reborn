package net.vit.jurassicreborn.common.blocks.entities.cleaner;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.util.InventoryUtil;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class CleanerBlock extends BaseEntityBlock {

    /* ---------------------------------------------------------------------
       CODEC (REQUIRED IN 1.21)
       --------------------------------------------------------------------- */
    public static final MapCodec<CleanerBlock> CODEC =
            Block.simpleCodec(CleanerBlock::new);

    public static final DirectionProperty FACING =
            BlockStateProperties.HORIZONTAL_FACING;

    /* ---------------------------------------------------------------------
       SHAPES
       --------------------------------------------------------------------- */
    public static final VoxelShape MODEL_SHAPE_SOUTH = Stream.of(
            Block.box(1, 0, 2, 15, 2, 13),
            Block.box(1, 2, 3, 2, 11, 13),
            Block.box(14, 2, 3, 15, 11, 13),
            Block.box(1, 0, 13, 15, 11, 14),
            Block.box(1, 2, 2, 15, 11, 3),
            Block.box(2.5, 11.7, 12, 3.5, 12.7, 13),
            Block.box(12.5, 11.7, 12, 13.5, 12.7, 13),
            Block.box(1, 11, 2, 15, 12, 14)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static VoxelShape MODEL_SHAPE_EAST = Stream.of(
            Block.box(2, 0, 1, 13, 2, 15),
            Block.box(3, 2, 14, 13, 11, 15),
            Block.box(3, 2, 1, 13, 11, 2),
            Block.box(13, 0, 1, 14, 11, 15),
            Block.box(2, 2, 1, 3, 11, 15),
            Block.box(12, 11.7, 12.5, 13, 12.7, 13.5),
            Block.box(12, 11.7, 2.5, 13, 12.7, 3.5),
            Block.box(2, 11, 1, 14, 12, 15)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public static VoxelShape MODEL_SHAPE_NORTH = Stream.of(
            Block.box(1, 0, 3, 15, 2, 14),
            Block.box(14, 2, 3, 15, 11, 13),
            Block.box(1, 2, 3, 2, 11, 13),
            Block.box(1, 0, 2, 15, 11, 3),
            Block.box(1, 2, 13, 15, 11, 14),
            Block.box(12.5, 11.7, 3, 13.5, 12.7, 4),
            Block.box(2.5, 11.7, 3, 3.5, 12.7, 4),
            Block.box(1, 11, 2, 15, 12, 14)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public static VoxelShape MODEL_SHAPE_WEST = Stream.of(
            Block.box(3, 0, 1, 14, 2, 15),
            Block.box(3, 2, 1, 13, 11, 2),
            Block.box(3, 2, 14, 13, 11, 15),
            Block.box(2, 0, 1, 3, 11, 15),
            Block.box(13, 2, 1, 14, 11, 15),
            Block.box(3, 11.7, 2.5, 4, 12.7, 3.5),
            Block.box(3, 11.7, 12.5, 4, 12.7, 13.5),
            Block.box(2, 11, 1, 14, 12, 15)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    // (EAST / NORTH / WEST shapes unchanged – keep yours as-is)

    /* ---------------------------------------------------------------------
       CONSTRUCTOR
       --------------------------------------------------------------------- */
    public CleanerBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.getStateDefinition().any().setValue(FACING, Direction.NORTH)
        );
    }

    /* ---------------------------------------------------------------------
       BLOCKSTATE
       --------------------------------------------------------------------- */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    /* ---------------------------------------------------------------------
       PLACEMENT
       --------------------------------------------------------------------- */
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    /* ---------------------------------------------------------------------
       BLOCK ENTITY
       --------------------------------------------------------------------- */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CleanerBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type
    ) {
        return level.isClientSide
                ? null
                : createTickerHelper(
                type,
                ModBlockEntities.CLEANING_STATION.value(),
                CleanerBlockEntity::tick
        );
    }

    /* ---------------------------------------------------------------------
       INTERACTION (1.21 CORRECT)
       --------------------------------------------------------------------- */
    @Override
    public InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit
    ) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof CleanerBlockEntity cleaner) {
                player.openMenu(cleaner);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /* ---------------------------------------------------------------------
       RENDER / SHAPE
       --------------------------------------------------------------------- */
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(
            BlockState state, BlockGetter level, BlockPos pos, CollisionContext context
    ) {
        return switch (state.getValue(FACING)) {
            case EAST -> MODEL_SHAPE_EAST;
            case SOUTH -> MODEL_SHAPE_SOUTH;
            case NORTH -> MODEL_SHAPE_NORTH;
            case WEST -> MODEL_SHAPE_WEST;
            default -> Shapes.block();
        };
    }

    /* ---------------------------------------------------------------------
       REMOVAL
       --------------------------------------------------------------------- */
    @Override
    public void onRemove(
            BlockState oldState, Level level, BlockPos pos,
            BlockState newState, boolean isMoving
    ) {
        if (!oldState.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof CleanerBlockEntity cleaner) {
                InventoryUtil.dropContents(level, pos, cleaner.getItemHandler());
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(oldState, level, pos, newState, isMoving);
        }
    }
}
