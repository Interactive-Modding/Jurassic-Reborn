package net.vit.jurassicreborn.common.blocks.entities.Embryoncis.EmbryoCalcificationMachine;

import net.vit.jurassicreborn.common.util.InventoryUtil;
import com.mojang.serialization.MapCodec;

import net.minecraft.world.Containers;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import net.vit.jurassicreborn.common.blocks.base.BaseMachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
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
import net.vit.jurassicreborn.common.blocks.entities.Embryoncis.EmbryonicMachine.EmbryonicMachineBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EmbryoCalcificationMachineBlock extends BaseMachineBlock {

    public static final MapCodec<EmbryoCalcificationMachineBlock> CODEC =
            Block.simpleCodec(EmbryoCalcificationMachineBlock::new);

//    public static DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static BooleanProperty EGG = BooleanProperty.create("egg");

    public static final VoxelShape NORTH_SHAPE = Shapes.join(Block.box(1.5, 0, 4, 14.5, 7, 16), Block.box(0.5, 0, 5, 15.5, 8, 15), BooleanOp.OR);
    public static final VoxelShape WEST_SHAPE = Shapes.join(Block.box(4, 0, 1.5, 16, 7, 14.5), Block.box(5, 0, 0.5, 15, 8, 15.5), BooleanOp.OR);
    public static final VoxelShape SOUTH_SHAPE = Shapes.join(Block.box(1.5, 0, 0, 14.5, 7, 12), Block.box(0.5, 0, 1, 15.5, 8, 11), BooleanOp.OR);
    public static final VoxelShape EAST_SHAPE = Shapes.join(Block.box(0, 0, 1.5, 12, 7, 14.5), Block.box(1, 0, 0.5, 11, 8, 15.5), BooleanOp.OR);


    public EmbryoCalcificationMachineBlock(Properties p_52591_) {
        super(p_52591_);
        this.registerDefaultState(this.getSetDefaultValues().setValue(EGG, false));
//        JurassicReborn.setRenderType(this, RenderType.cutoutMipped());
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(EGG);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new EmbryoCalcificationMachineBlockEntity(pPos, pState);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide) {
            if (level.getBlockEntity(pos) instanceof EmbryoCalcificationMachineBlockEntity e) {
                player.openMenu(e);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (pLevel1, pPos, pState1, pBlockEntity) -> {
            if(pLevel1.getBlockEntity(pPos) instanceof EmbryoCalcificationMachineBlockEntity dnaSequencer){
                dnaSequencer.tick(pLevel1, pPos, pState1, dnaSequencer);
            }else{
                EmbryoCalcificationMachineBlock.super.getTicker(pLevel, pState, pBlockEntityType);
            }
        };
    }

    @Override
    @NotNull
    public  VoxelShape getShape(BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return switch (pState.getValue(FACING)) {
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case NORTH -> NORTH_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> Block.box(1, 0, 1, 15, 14, 15);
        };
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }
    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!oldState.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof EmbryoCalcificationMachineBlockEntity calcificationMachineBlock) {
                InventoryUtil.dropContents(level, pos, calcificationMachineBlock.getItemHandler());
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(oldState, level, pos, newState, isMoving);
        }
    }
}
