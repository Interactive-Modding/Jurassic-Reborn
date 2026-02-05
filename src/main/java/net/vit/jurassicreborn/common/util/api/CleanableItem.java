package net.vit.jurassicreborn.common.util.api;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.vit.jurassicreborn.common.items.misc.RebornIngredientItem;
import java.util.Random;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface CleanableItem extends RebornIngredientItem {

    static CleanableItem getCleanableItem(ItemStack stack) {
        if (stack != null) {
            Item item = stack.getItem();

            if (item instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();

                if (block instanceof CleanableItem) {
                    return (CleanableItem) block;
                }
            }
            if (item instanceof CleanableItem i) {
                return i;
            }
        }

        return null;
    }

    static boolean isCleanableItem(ItemStack stack) {
        return getCleanableItem(stack) != null;
    }

    boolean isCleanable(ItemStack stack);

    ItemStack getCleanedItem(ItemStack stack, Random random);

}
