package mod.reborn.server.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.Random;

public interface SequencableItem extends RebornIngredientItem {
    static SequencableItem getSequencableItem(ItemStack stack) {
        if (stack != null) {
            Item item = stack.getItem();

            if (item instanceof ItemBlock) {
                Block block = ((ItemBlock) item).getBlock();

                if (block instanceof SequencableItem) {
                    return (SequencableItem) block;
                }
            } else if (item instanceof SequencableItem) {
                return (SequencableItem) item;
            }
        }

        return null;
    }

    static boolean isSequencableItem(ItemStack stack) {
        return getSequencableItem(stack) != null;
    }

    static int randomQuality(Random rand) {
        return (rand.nextInt(20) + 1) * 5;
    }

    boolean isSequencable(ItemStack stack);

    ItemStack getSequenceOutput(ItemStack stack, Random random);
}
