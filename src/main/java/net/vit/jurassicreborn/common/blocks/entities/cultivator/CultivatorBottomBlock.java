package net.vit.jurassicreborn.common.blocks.entities.cultivator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.vit.jurassicreborn.common.blocks.base.BaseMachineBlock;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

public class CultivatorBottomBlock extends CultivatorBlock {

    public CultivatorBottomBlock(Properties props) { super(props, false); }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        if (level.isClientSide) return;

        BlockEntity bottom = level.getBlockEntity(pos);
        BlockEntity top = level.getBlockEntity(pos.above());
        if (bottom instanceof CultivatorBlockEntity be && top instanceof CultivatorTopBlockEntity te) {
            te.setBottomEntity(be);
        }
    }

    @Override public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new CultivatorBlockEntity(pos, state); }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null
                : BaseMachineBlock.createTickerHelper(
                type,
                ModBlockEntities.CULTIVATOR_BLOCK_ENTITY_TYPE.get(),
                CultivatorBlockEntity::tick
        );
    }
}
