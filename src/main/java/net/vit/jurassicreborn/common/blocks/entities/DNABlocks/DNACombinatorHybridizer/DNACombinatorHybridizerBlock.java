package net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNACombinatorHybridizer;

import com.mojang.serialization.MapCodec;

import net.vit.jurassicreborn.common.util.InventoryUtil;
import net.vit.jurassicreborn.common.blocks.base.BaseMachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class DNACombinatorHybridizerBlock extends BaseMachineBlock {

    public static final MapCodec<DNACombinatorHybridizerBlock> CODEC =
            Block.simpleCodec(DNACombinatorHybridizerBlock::new);

    public static final BooleanProperty MODE = BooleanProperty.create("is_hybridizer");

    private static final VoxelShape MODEL_SHAPE_NORTH = Block.box(1, 0, 1, 15, 14, 15);

    public DNACombinatorHybridizerBlock(Properties p_52591_) {
        super(p_52591_);
        this.registerDefaultState(this.getSetDefaultValues().setValue(MODE, false));

    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(MODE);
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new DNACombinatorHybridizerBlockEntity(pPos, pState);
    }


    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, net.minecraft.world.level.block.state.BlockState state, BlockEntityType<T> type) {
        return (world1, pos, state1, instance) -> {
            if (instance instanceof DNACombinatorHybridizerBlockEntity dna) {
                ( dna).tick(world1, pos, state1, dna);
            } else {
                super.getTicker(world, state, type).tick(world1, pos, state1, instance);
            }
        };
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite()).setValue(MODE, false);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof MenuProvider provider) {
                player.openMenu(provider);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (pState.getValue(FACING)) {
            case EAST, WEST, NORTH, SOUTH -> MODEL_SHAPE_NORTH;
            default -> MODEL_SHAPE_NORTH;
        };
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos) {
        if(pLevel.getBlockEntity(pPos) instanceof DNACombinatorHybridizerBlockEntity e)
            return e;
        return null;
    }

    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!oldState.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof DNACombinatorHybridizerBlockEntity dna) {
                InventoryUtil.dropContents(level, pos, dna.getItemHandler());
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(oldState, level, pos, newState, isMoving);
        }
    }
}
