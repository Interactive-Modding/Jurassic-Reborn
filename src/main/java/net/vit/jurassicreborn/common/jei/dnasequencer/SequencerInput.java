package net.vit.jurassicreborn.common.jei.dnasequencer;

import net.vit.jurassicreborn.common.util.api.SequencableItem;
import net.minecraft.world.item.ItemStack;

/** Simple wrapper storing the stack and its SequencableItem handler. */
public class SequencerInput {
    public final ItemStack stack;
    public final SequencableItem item;

    public SequencerInput(ItemStack stack) {
        this.stack = stack;
        this.item = SequencableItem.getSequencableItem(stack);
    }
}