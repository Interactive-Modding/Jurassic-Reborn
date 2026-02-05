package net.vit.jurassicreborn.common.jei.fossilgrinder;

import net.vit.jurassicreborn.common.util.api.GrindableItem;
import net.minecraft.world.item.ItemStack;

/** Simple wrapper storing the stack and its GrindableItem handler. */
public class GrinderInput {
    public final ItemStack stack;
    public final GrindableItem grind;

    public GrinderInput(ItemStack stack) {
        this.stack = stack;
        this.grind = GrindableItem.getGrindableItem(stack);
    }
}