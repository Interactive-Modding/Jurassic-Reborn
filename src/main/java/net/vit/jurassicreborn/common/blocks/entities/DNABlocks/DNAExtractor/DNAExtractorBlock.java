package net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNAExtractor;

import net.vit.jurassicreborn.common.util.InventoryUtil;
import com.mojang.serialization.MapCodec;

import net.minecraft.world.Containers;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import net.vit.jurassicreborn.common.blocks.base.BaseMachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNACombinatorHybridizer.DNACombinatorHybridizerBlockEntity;
import org.jetbrains.annotations.Nullable;

public class DNAExtractorBlock extends BaseMachineBlock {

    public static final MapCodec<DNAExtractorBlock> CODEC =
            Block.simpleCodec(DNAExtractorBlock::new);

    public static DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final VoxelShape MODEL_SHAPE_NORTH = Block.box(0, 0, 1.5, 16, 13, 14);
    public static final VoxelShape MODEL_SHAPE_WEST = Block.box(1.5, 0, 0, 14, 13, 16);
    public static final VoxelShape MODEL_SHAPE_SOUTH = Block.box(0, 0, 2, 16, 13, 14.5);
    public static final VoxelShape MODEL_SHAPE_EAST = Block.box(2, 0, 0, 14.5, 13, 16);


    public DNAExtractorBlock(Properties p_52591_) {
        super(p_52591_);
        this.registerDefaultState(this.getSetDefaultValues());
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (pLevel1, pPos, pState1, pBlockEntity) -> {
            if(pLevel1.getBlockEntity(pPos) instanceof DNAExtractorBlockEntity dnaSequencer){
                dnaSequencer.tick(pLevel1, pPos, pState1, dnaSequencer);
            }else{
                DNAExtractorBlock.super.getTicker(pLevel, pState, pBlockEntityType);
            }
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new DNAExtractorBlockEntity(pPos, pState);
    }
    public RenderShape getRenderShape(BlockState state){return RenderShape.ENTITYBLOCK_ANIMATED;}

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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
    }

//    @Nullable
//    @Override
//    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
//        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
//    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof DNAExtractorBlockEntity e) {
                player.openMenu(e);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!oldState.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof DNAExtractorBlockEntity dnaExtractorBlock) {
                InventoryUtil.dropContents(level, pos, dnaExtractorBlock.getItemHandler());
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(oldState, level, pos, newState, isMoving);
        }
    }


}
