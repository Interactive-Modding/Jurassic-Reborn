package net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASequencer;

import net.minecraft.world.Containers;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.vit.jurassicreborn.common.blocks.base.BaseMachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNACombinatorHybridizer.DNACombinatorHybridizerBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class DNASequencerBlock extends BaseMachineBlock {
    //todo: block entities, recipies, DNA

//    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final VoxelShape MODEL_SHAPE_NORTH = Stream.of(
            Block.box(-0.5, 0, 5.5, 16.5, 1, 15.5),
            Stream.of(
                    Block.box(1, 0.44999999999999996, 0.5, 7, 1.45, 1.5),
                    Block.box(1, 0.925, 1.5, 7, 1.9250000000000003, 2.5),
                    Block.box(1, 1.3250000000000002, 2.5, 7, 2.3249999999999997, 3.5),
                    Block.box(1, 1.7000000000000002, 3.5, 7, 2.6999999999999993, 4.5),
                    Block.box(1, 0, 4.5, 7, 3.099999999999999, 5.5)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(-0.25, 1, 6.25, 6.75, 10, 15.25),
            Stream.of(
            Block.box(0.75, 9, 7.25, 3.75, 11.5, 8.25),
            Block.box(0.75, 11, 6.25, 3.75, 14, 7.25),
            Stream.of(
            Block.box(-1.25, 12, 5.25, 5.75, 14, 6.25),
            Block.box(-1.25, 14, 6.25, 5.75, 16, 7.25),
            Block.box(-1.25, 16, 6.75, 5.75, 17, 7.75),
            Block.box(-1.25, 10, 4.25, 5.75, 12, 5.25),
            Block.box(-1.25, 8.425, 3.5, 5.75, 10, 4.25)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get())
                    .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Shapes.join(Block.box(4.75, 1, 6.75, 7.75, 15, 14.75), Stream.of(
            Block.box(7.5, 2, 7.25, 14.5, 6, 14.25),
            Block.box(7.5, 10, 7.25, 14.5, 14, 14.25),
            Block.box(7.5, 6, 7.25, 14.5, 10, 14.25)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), BooleanOp.OR)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(14.5, 1, 7, 16.5, 15, 15)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape MODEL_SHAPE_WEST = Stream.of(
            Block.box(5.5, 0, -0.5, 15.5, 1, 16.5),
            Stream.of(
                    Block.box(0.5, 0.44999999999999996, 9, 1.5, 1.45, 15),
                    Block.box(1.5, 0.925, 9, 2.5, 1.9250000000000003, 15),
                    Block.box(2.5, 1.3250000000000002, 9, 3.5, 2.3249999999999997, 15),
                    Block.box(3.5, 1.7000000000000002, 9, 4.5, 2.6999999999999993, 15),
                    Block.box(4.5, 0, 9, 5.5, 3.099999999999999, 15)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(6.25, 1, 9.25, 15.25, 10, 16.25),
            Stream.of(
            Block.box(7.25, 9, 12.25, 8.25, 11.5, 15.25),
            Block.box(6.25, 11, 12.25, 7.25, 14, 15.25),
            Stream.of(
            Block.box(5.25, 12, 10.25, 6.25, 14, 17.25),
            Block.box(6.25, 14, 10.25, 7.25, 16, 17.25),
            Block.box(6.75, 16, 10.25, 7.75, 17, 17.25),
            Block.box(4.25, 10, 10.25, 5.25, 12, 17.25),
            Block.box(3.5, 8.425, 10.25, 4.25, 10, 17.25)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get())
                    .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Shapes.join(Block.box(6.75, 1, 8.25, 14.75, 15, 11.25), Stream.of(
            Block.box(7.25, 2, 1.5, 14.25, 6, 8.5),
            Block.box(7.25, 10, 1.5, 14.25, 14, 8.5),
            Block.box(7.25, 6, 1.5, 14.25, 10, 8.5)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), BooleanOp.OR)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(7, 1, -0.5, 15, 15, 1.5)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape MODEL_SHAPE_SOUTH = Stream.of(
            Block.box(-0.5, 0, 0.5, 16.5, 1, 10.5),
            Stream.of(
                    Block.box(9, 0.44999999999999996, 14.5, 15, 1.45, 15.5),
                    Block.box(9, 0.925, 13.5, 15, 1.9250000000000003, 14.5),
                    Block.box(9, 1.3250000000000002, 12.5, 15, 2.3249999999999997, 13.5),
                    Block.box(9, 1.7000000000000002, 11.5, 15, 2.6999999999999993, 12.5),
                    Block.box(9, 0, 10.5, 15, 3.099999999999999, 11.5)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(9.25, 1, 0.75, 16.25, 10, 9.75),
            Stream.of(
            Block.box(12.25, 9, 7.75, 15.25, 11.5, 8.75),
            Block.box(12.25, 11, 8.75, 15.25, 14, 9.75),
            Stream.of(
            Block.box(10.25, 12, 9.75, 17.25, 14, 10.75),
            Block.box(10.25, 14, 8.75, 17.25, 16, 9.75),
            Block.box(10.25, 16, 8.25, 17.25, 17, 9.25),
            Block.box(10.25, 10, 10.75, 17.25, 12, 11.75),
            Block.box(10.25, 8.425, 11.75, 17.25, 10, 12.5)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get())
                    .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Shapes.join(Block.box(8.25, 1, 1.25, 11.25, 15, 9.25), Stream.of(
            Block.box(1.5, 2, 1.75, 8.5, 6, 8.75),
            Block.box(1.5, 10, 1.75, 8.5, 14, 8.75),
            Block.box(1.5, 6, 1.75, 8.5, 10, 8.75)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), BooleanOp.OR)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(-0.5, 1, 1, 1.5, 15, 9)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape MODEL_SHAPE_EAST = Stream.of(
            Block.box(0.5, 0, -0.5, 10.5, 1, 16.5),
            Stream.of(
                    Block.box(14.5, 0.44999999999999996, 1, 15.5, 1.45, 7),
                    Block.box(13.5, 0.925, 1, 14.5, 1.9250000000000003, 7),
                    Block.box(12.5, 1.3250000000000002, 1, 13.5, 2.3249999999999997, 7),
                    Block.box(11.5, 1.7000000000000002, 1, 12.5, 2.6999999999999993, 7),
                    Block.box(10.5, 0, 1, 11.5, 3.099999999999999, 7)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(0.75, 1, -0.25, 9.75, 10, 6.75),
            Stream.of(
            Block.box(7.75, 9, 0.75, 8.75, 11.5, 3.75),
            Block.box(8.75, 11, 0.75, 9.75, 14, 3.75),
            Stream.of(
            Block.box(9.75, 12, -1.25, 10.75, 14, 5.75),
            Block.box(8.75, 14, -1.25, 9.75, 16, 5.75),
            Block.box(8.25, 16, -1.25, 9.25, 17, 5.75),
            Block.box(10.75, 10, -1.25, 11.75, 12, 5.75),
            Block.box(11.75, 8.425, -1.25, 12.5, 10, 5.75)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get())
                    .reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),

            Shapes.join(Block.box(1.25, 1, 4.75, 9.25, 15, 7.75), Stream.of(
            Block.box(1.75, 2, 7.5, 8.75, 6, 14.5),
            Block.box(1.75, 10, 7.5, 8.75, 14, 14.5),
            Block.box(1.75, 6, 7.5, 8.75, 10, 14.5)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(), BooleanOp.OR)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(1, 1, 14.5, 9, 15, 16.5)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();



    public DNASequencerBlock(Properties p_52591_) {
        super(p_52591_);
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
                return Block.box(1, 0, 1, 15, 14, 15);
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
//            MenuProvider menuprovider = this.getMenuProvider(pState, pLevel, pPos);
            if (pLevel.getBlockEntity(pPos) instanceof DNASequencerBlockEntity e ) {
                pPlayer.openMenu(e);
            }

            return InteractionResult.CONSUME;
        }
    }


    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (pLevel1, pPos, pState1, pBlockEntity) -> {
            if(pLevel1.getBlockEntity(pPos) instanceof DNASequencerBlockEntity dnaSequencer){
                dnaSequencer.tick(pLevel1, pPos, pState1, dnaSequencer);
            }else{
                DNASequencerBlock.super.getTicker(pLevel, pState, pBlockEntityType);
            }
        };
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new DNASequencerBlockEntity(pPos, pState);
    }
    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!oldState.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof DNASequencerBlockEntity dnaSequencerBlock) {
                Containers.dropContents(level, pos, new RecipeWrapper(dnaSequencerBlock.getItemHandler()));
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(oldState, level, pos, newState, isMoving);
        }
    }
}
