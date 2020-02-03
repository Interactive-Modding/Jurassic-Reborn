package mod.reborn.server.plugin.jei.category.dnasequencer;

import mod.reborn.server.api.SequencableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class SequencerInput {
    public final ItemStack stack;
    public final SequencableItem item;

    public SequencerInput(ItemStack stack) {
        this.stack = stack;
        Item item = stack.getItem();
        if(item instanceof ItemBlock) {
            this.item = (SequencableItem)((ItemBlock)stack.getItem()).getBlock();
        } else {
            this.item = (SequencableItem)stack.getItem();
        }
    }
}
