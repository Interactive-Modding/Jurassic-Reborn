package net.vit.jurassicreborn.common.jei.dnasynthesizer;

import net.vit.jurassicreborn.common.util.api.SynthesizableItem;
import net.minecraft.world.item.ItemStack;

/** Simple wrapper storing the stack and its SynthesizableItem handler. */
public class SynthesizerInput {
    public final ItemStack stack;
    public final SynthesizableItem item;

    public SynthesizerInput(ItemStack stack) {
        this.stack = stack;
        this.item = SynthesizableItem.getSynthesizableItem(stack);
    }
}