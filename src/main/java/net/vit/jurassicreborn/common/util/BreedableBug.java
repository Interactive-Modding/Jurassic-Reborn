package net.vit.jurassicreborn.common.util;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public interface BreedableBug {
    static BreedableBug getBug(ItemStack stack) {
        if (!stack.isEmpty()) {
            Item item = stack.getItem();
            if (item instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();
                if (block instanceof BreedableBug bugBlock) {
                    return bugBlock;
                }
            } else if (item instanceof BreedableBug bugItem) {
                return bugItem;
            }
        }
        return null;
    }

    static boolean isBug(ItemStack stack) {
        return getBug(stack) != null;
    }

    int getBreedings(ItemStack stack);
}
