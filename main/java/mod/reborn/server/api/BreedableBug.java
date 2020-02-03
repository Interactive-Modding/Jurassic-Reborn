package mod.reborn.server.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public interface BreedableBug {
    static BreedableBug getBug(ItemStack stack) {
        if (stack != null) {
            Item item = stack.getItem();
            if (item instanceof ItemBlock) {
                Block block = ((ItemBlock) item).getBlock();
                if (block instanceof BreedableBug) {
                    return (BreedableBug) block;
                }
            } else if (item instanceof BreedableBug) {
                return (BreedableBug) item;
            }
        }
        return null;
    }

    static boolean isBug(ItemStack stack) {
        return getBug(stack) != null;
    }

    int getBreedings(ItemStack stack);
}
