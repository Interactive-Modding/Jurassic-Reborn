package net.vit.jurassicreborn.common.blocks.entities.grinder;

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
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNACombinatorHybridizer.DNACombinatorHybridizerBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class FossilGrinderBlock extends BaseMachineBlock {
    //todo: block entities and recipies

    public static DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public FossilGrinderBlock(Properties p_49795_) {
        super(p_49795_);
//        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));

//        JurassicReborn.setRenderType(this, RenderType.cutoutMipped());
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Block.box(2.0, 0.0, 2.0, 14.0, 6.0, 14.0 );
    }

    //    @Override
//    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
//        pBuilder.add(FACING);
//    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
//            MenuProvider menuprovider = this.getMenuProvider(pState, pLevel, pPos);
            if (pLevel.getBlockEntity(pPos) instanceof FossilGrinderBlockEntity e ) {
                pPlayer.openMenu(e);
            }

            return InteractionResult.CONSUME;
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new FossilGrinderBlockEntity(pPos, pState);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (pLevel1, pPos, pState1, pBlockEntity) -> {
            if(pBlockEntity instanceof FossilGrinderBlockEntity fossilGrinderBlockEntity){
                fossilGrinderBlockEntity.tick(pLevel1, pPos, pState1, fossilGrinderBlockEntity);
            }else{
                Objects.requireNonNull(super.getTicker(pLevel, pState, pBlockEntityType)).tick(pLevel1, pPos, pState1, pBlockEntity);
            }
        };
    }
    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!oldState.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof FossilGrinderBlockEntity fossilGrinderBlock) {
                Containers.dropContents(level, pos, new RecipeWrapper(fossilGrinderBlock.getItemHandler()));
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(oldState, level, pos, newState, isMoving);
        }
    }
}
