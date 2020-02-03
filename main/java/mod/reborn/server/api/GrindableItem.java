package mod.reborn.server.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.Random;

public interface GrindableItem extends RebornIngredientItem {
    static GrindableItem getGrindableItem(ItemStack stack) {
        if (!stack.isEmpty()) {
            Item item = stack.getItem();

            if (item instanceof ItemBlock) {
                Block block = ((ItemBlock) item).getBlock();

                if (block instanceof GrindableItem) {
                    return (GrindableItem) block;
                }
            } else if (item instanceof GrindableItem) {
                return (GrindableItem) item;
            }
        }

        return null;
    }

    static boolean isGrindableItem(ItemStack stack) {
        return getGrindableItem(stack) != null;
    }

    boolean isGrindable(ItemStack stack);

    ItemStack getGroundItem(ItemStack stack, Random random);
}
