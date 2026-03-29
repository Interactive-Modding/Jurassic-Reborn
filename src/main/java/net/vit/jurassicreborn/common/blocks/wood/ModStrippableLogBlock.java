package net.vit.jurassicreborn.common.blocks.wood;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;


public class ModStrippableLogBlock extends RotatedPillarBlock {

    public ModStrippableLogBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockState getToolModifiedState(
            BlockState state,
            UseOnContext context,
            ItemAbility itemAbility,
            boolean simulate
    ) {
        if (!itemAbility.equals(ItemAbilities.AXE_STRIP)) {
            return super.getToolModifiedState(state, context, itemAbility, simulate);
        }

        Block stripped = WoodBlocks.getStrippedByBlock().get(state.getBlock());
        if (stripped != null) {
            BlockState result = stripped.defaultBlockState();
            if (state.hasProperty(RotatedPillarBlock.AXIS)) {
                result = result.setValue(
                        RotatedPillarBlock.AXIS,
                        state.getValue(RotatedPillarBlock.AXIS)
                );
            }
            return result;
        }

        return super.getToolModifiedState(state, context, itemAbility, simulate);
    }
}
