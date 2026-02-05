package net.vit.jurassicreborn.common.blocks.entities.Embryoncis.EmbryonicMachine;

import net.minecraft.world.Containers;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.vit.jurassicreborn.common.blocks.base.BaseMachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNACombinatorHybridizer.DNACombinatorHybridizerBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class EmbryonicMachineBlock extends BaseMachineBlock {



    public static BooleanProperty TEST_TUBES = BooleanProperty.create("tubes");
    public static BooleanProperty PETRI_DISH = BooleanProperty.create("dish");

//    public static DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final VoxelShape MODEL_SHAPE_NORTH = Stream.of(
            Block.box(0.5, 0, 3, 15.5, 2, 13),
            Shapes.join(Block.box(0.5, 4.5, 4, 4.5, 5.5, 8), Block.box(1, 1, 4.5, 4, 5, 7.5), BooleanOp.OR),
            Stream.of(
                    Block.box(9.5, 2, 7.5, 12.5, 9, 10.5),
                    Stream.of(
                            Block.box(9, 8.5, 7, 13, 13.5, 11),
                            Stream.of(
                                    Shapes.join(Block.box(7, 7.8, 8.5, 8, 9.8, 9.5), Block.box(7.350000000000002, 6.3, 8.875, 7.650000000000002, 7.8, 9.175), BooleanOp.OR),
                                    Block.box(6, 9.3, 7.5, 9, 11.3, 10.5),
                                    Block.box(6.5, 10.3, 8, 8.5, 13.3, 10)
                            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(8, 12, 8.5, 10, 13, 9.5),
            Block.box(9.5, 12, 6.5, 10.5, 13, 7.5)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(12, 4.5, 8.5, 13, 5.5, 9.5),
            Block.box(8, 6.4, 8.5, 10, 7.4, 9.5)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(13.5, 1.5, 8.5, 14.5, 2.5, 9.5),
            Block.box(6.75, 1.5, 8, 8.75, 2.5, 10)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape MODEL_SHAPE_WEST = Stream.of(
            Block.box(3, 0, 0.5, 13, 2, 15.5),
            Shapes.join(Block.box(4, 4.5, 11.5, 8, 5.5, 15.5), Block.box(4.5, 1, 12, 7.5, 5, 15), BooleanOp.OR),
            Stream.of(
                    Block.box(7.5, 2, 3.5, 10.5, 9, 6.5),
                    Stream.of(
                            Block.box(7, 8.5, 3, 11, 13.5, 7),
                            Stream.of(
                                    Shapes.join(Block.box(8.5, 7.8, 8, 9.5, 9.8, 9), Block.box(8.875, 6.3, 8.349999999999998, 9.175, 7.8, 8.649999999999999), BooleanOp.OR),
                                    Block.box(7.5, 9.3, 7, 10.5, 11.3, 10),
                                    Block.box(8, 10.3, 7.5, 10, 13.3, 9.5)
                            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(8.5, 12, 6, 9.5, 13, 8),
            Block.box(6.5, 12, 5.5, 7.5, 13, 6.5)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(8.5, 4.5, 3, 9.5, 5.5, 4),
            Block.box(8.5, 6.4, 6, 9.5, 7.4, 8)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(8.5, 1.5, 1.5, 9.5, 2.5, 2.5),
            Shapes.join(Block.box(8, 1.5, 7.25, 10, 2.5, 9.25), Shapes.join(Block.box(7.5, 2, 7, 10.5, 4, 10), Block.box(7, 4, 6.5, 11, 5, 10.5), BooleanOp.OR), BooleanOp.OR)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape MODEL_SHAPE_SOUTH = Stream.of(
            Block.box(0.5, 0, 3, 15.5, 2, 13),
            Shapes.join(Block.box(11.5, 4.5, 8, 15.5, 5.5, 12), Block.box(12, 1, 8.5, 15, 5, 11.5), BooleanOp.OR),
            Stream.of(
                    Block.box(3.5, 2, 5.5, 6.5, 9, 8.5),
                    Stream.of(
                            Block.box(3, 8.5, 5, 7, 13.5, 9),
                            Stream.of(
                                    Shapes.join(Block.box(8, 7.8, 6.5, 9, 9.8, 7.5), Block.box(8.349999999999998, 6.3, 6.824999999999999, 8.649999999999999, 7.8, 7.125), BooleanOp.OR),
                                    Block.box(7, 9.3, 5.5, 10, 11.3, 8.5),
                                    Block.box(7.5, 10.3, 6, 9.5, 13.3, 8)
                            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(6, 12, 6.5, 8, 13, 7.5),
            Block.box(5.5, 12, 8.5, 6.5, 13, 9.5)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(3, 4.5, 6.5, 4, 5.5, 7.5),
            Block.box(6, 6.4, 6.5, 8, 7.4, 7.5)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(1.5, 1.5, 6.5, 2.5, 2.5, 7.5),
            Shapes.join(Block.box(7.25, 1.5, 6, 9.25, 2.5, 8), Shapes.join(Block.box(7, 2, 5.5, 10, 4, 8.5), Block.box(6.5, 4, 5, 10.5, 5, 9), BooleanOp.OR), BooleanOp.OR)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape MODEL_SHAPE_EAST = Stream.of(
            Block.box(3, 0, 0.5, 13, 2, 15.5),
            Shapes.join(Block.box(8, 4.5, 0.5, 12, 5.5, 4.5), Block.box(8.5, 1, 1, 11.5, 5, 4), BooleanOp.OR),
            Stream.of(
                    Block.box(5.5, 2, 9.5, 8.5, 9, 12.5),
                    Stream.of(
                            Block.box(5, 8.5, 9, 9, 13.5, 13),
                            Stream.of(
                                    Shapes.join(Block.box(6.5, 7.8, 7, 7.5, 9.8, 8), Block.box(6.824999999999999, 6.3, 7.350000000000001, 7.125, 7.8, 7.650000000000002), BooleanOp.OR),
                                    Block.box(5.5, 9.3, 6, 8.5, 11.3, 9),
                                    Block.box(6, 10.3, 6.5, 8, 13.3, 8.5)
                            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(6.5, 12, 8, 7.5, 13, 10),
            Block.box(8.5, 12, 9.5, 9.5, 13, 10.5)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(6.5, 4.5, 12, 7.5, 5.5, 13),
            Block.box(6.5, 6.4, 8, 7.5, 7.4, 10)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(6.5, 1.5, 13.5, 7.5, 2.5, 14.5),
            Shapes.join(Block.box(6, 1.5, 6.75, 8, 2.5, 8.75), Shapes.join(Block.box(5.5, 2, 6, 8.5, 4, 9), Block.box(5, 4, 5.5, 9, 5, 9.5), BooleanOp.OR), BooleanOp.OR)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();


    //todo: recipies, blocks
    public EmbryonicMachineBlock(Properties p_52591_) {
        super(p_52591_);
        this.registerDefaultState(this.getSetDefaultValues().setValue(TEST_TUBES, true).setValue(PETRI_DISH, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(TEST_TUBES, PETRI_DISH);
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
//                MenuProvider menuprovider = this.getMenuProvider(pState, pLevel, pPos);
            if (pLevel.getBlockEntity(pPos) instanceof EmbryonicMachineBlockEntity e ) {
                pPlayer.openMenu(e);
            }

            return InteractionResult.CONSUME;
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new EmbryonicMachineBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (pLevel1, pPos, pState1, pBlockEntity) -> {
            if(pLevel1.getBlockEntity(pPos) instanceof EmbryonicMachineBlockEntity dnaSequencer){
                EmbryonicMachineBlockEntity.tick(pLevel1, pPos, pState1, dnaSequencer);
            }else{
                EmbryonicMachineBlock.super.getTicker(pLevel, pState, pBlockEntityType);
            }
        };
    }
    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!oldState.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof EmbryonicMachineBlockEntity embryonicMachineBlock) {
                Containers.dropContents(level, pos, new RecipeWrapper(embryonicMachineBlock.getItemHandler()));
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(oldState, level, pos, newState, isMoving);
        }
    }
}
