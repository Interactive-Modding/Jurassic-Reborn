package net.vit.jurassicreborn.common.blocks.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BaseMachineBlock extends BaseEntityBlock implements EntityBlock, SimpleWaterloggedBlock {

    public static BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static DirectionProperty FACING = HorizontalDirectionalBlock.FACING;


    //TODO: mechanics of this lol
    public BaseMachineBlock(Properties p_52591_) {
        super(p_52591_);
    }

    public BlockState getSetDefaultValues(){
        return this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, WATERLOGGED);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
//        return (world1, pos, state1, instance) -> {
//            if(instance instanceof MachineBlockEntity<?> machineBlockEntity){
//                machineBlockEntity.tick(world1, pos, state1, machineBlockEntity);
//            }else{
//                super.getTicker(pLevel, pState, pBlockEntityType).tick(world1, pos, state1, instance);
//            }
//        };
        return super.getTicker(pLevel, pState, pBlockEntityType);
    }

    public @NotNull FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite()).setValue(WATERLOGGED, pContext.getLevel().getFluidState(pContext.getClickedPos()).getType() == Fluids.WATER);
    }
}
