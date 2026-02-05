package net.vit.jurassicreborn.common.blocks.entities.cultivator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CultivatorTopBlock extends CultivatorBlock {
    public CultivatorTopBlock(Properties props) { super(props, true); }
    @Override public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) { return new CultivatorTopBlockEntity(pos, state); }
}
