package mod.reborn.server.plugin.jei.category.dnasynthesizer;

import mod.reborn.server.api.SynthesizableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class SynthesizerInput {
    public final ItemStack stack;
    public final SynthesizableItem item;

    public SynthesizerInput(ItemStack stack) {
        this.stack = stack;
        Item item = stack.getItem();
        if(item instanceof ItemBlock) {
            this.item = (SynthesizableItem)((ItemBlock)stack.getItem()).getBlock();
        } else {
            this.item = (SynthesizableItem)stack.getItem();
        }
    }
}
