package net.vit.jurassicreborn.common.items.misc;

import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.cultivator.CultivatorBlock;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;

public class CultivatorItem extends ItemNameBlockItem {
    private final DyeColor color;

    public CultivatorItem(Properties pProperties, DyeColor color1) {
        super(ModBlocks.CULTIVATE_BOTTOM.get(), pProperties);
        color = color1;
    }


    @Override
    public void fillItemCategory(CreativeModeTab pGroup, NonNullList<ItemStack> pItems) {
        if (pGroup == this.getItemCategory() || pGroup == CreativeModeTab.TAB_SEARCH) {
            pItems.add(this.getDefaultInstance());
        }
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext pContext, BlockState pState) {
        return pContext.getLevel().setBlock(pContext.getClickedPos(), pState.setValue(CultivatorBlock.COLOR, this.color), 11);
    }
}
