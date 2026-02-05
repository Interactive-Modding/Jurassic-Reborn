package net.vit.jurassicreborn.common.jei.cleaningstation;

import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.util.api.CleanableItem;

/** Simple wrapper storing the stack and its CleanableItem handler. */
public class CleanableInput {
    public final ItemStack stack;
    public final CleanableItem cleanable;

    public CleanableInput(ItemStack stack) {
        this.stack = stack;
        this.cleanable = CleanableItem.getCleanableItem(stack);
    }
}