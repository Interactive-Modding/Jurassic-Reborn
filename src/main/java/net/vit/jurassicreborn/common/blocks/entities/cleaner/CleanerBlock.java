package net.vit.jurassicreborn.common.blocks.entities.cleaner;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
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
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNACombinatorHybridizer.DNACombinatorHybridizerBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class CleanerBlock extends BaseEntityBlock {

    public static DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static VoxelShape MODEL_SHAPE_SOUTH = Stream.of(
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

    public CleanerBlock(Properties p_49224_) {
        super(p_49224_);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CleanerBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, instance) -> {
            if (instance instanceof CleanerBlockEntity) {
                ((CleanerBlockEntity) instance).tick(world1, pos, state1, (CleanerBlockEntity) instance);
            } else {
                super.getTicker(world, state, type).tick(world1, pos, state1, instance);
            }
        };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
//            MenuProvider menuprovider = this.getMenuProvider(pState, pLevel, pPos);
            if (pLevel.getBlockEntity(pPos) instanceof CleanerBlockEntity e ) {
                pPlayer.openMenu(e);
            }

            return InteractionResult.CONSUME;
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        switch (pState.getValue(FACING)){
            case EAST:
                return MODEL_SHAPE_EAST;
            case SOUTH:
                return MODEL_SHAPE_SOUTH;
            case NORTH:
                return MODEL_SHAPE_NORTH;
            case WEST:
                return MODEL_SHAPE_WEST;
            default:
                return Block.box(0, 0, 0, 16, 16, 16);
        }
    }


    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!oldState.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CleanerBlockEntity cleanerBlock) {
                Containers.dropContents(level, pos, new RecipeWrapper(cleanerBlock.getItemHandler()));
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(oldState, level, pos, newState, isMoving);
        }
    }
}
