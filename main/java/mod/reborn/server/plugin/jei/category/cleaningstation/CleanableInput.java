package mod.reborn.server.plugin.jei.category.cleaningstation;

import mod.reborn.server.api.CleanableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class CleanableInput {
    public final ItemStack stack;
    public final CleanableItem grind;

    public CleanableInput(ItemStack stack) {
        this.stack = stack;
        Item item = stack.getItem();
        if(item instanceof ItemBlock) {
            this.grind = (CleanableItem)((ItemBlock)stack.getItem()).getBlock();
        } else {
            this.grind = (CleanableItem)stack.getItem();
        }
    }
}
