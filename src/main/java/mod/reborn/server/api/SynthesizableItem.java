package mod.reborn.server.api;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.Random;

public interface SynthesizableItem extends RebornIngredientItem {
    static SynthesizableItem getSynthesizableItem(ItemStack stack) {
        if (stack != null) {
            Item item = stack.getItem();

            if (item instanceof ItemBlock) {
                Block block = ((ItemBlock) item).getBlock();

                if (block instanceof SynthesizableItem) {
                    return (SynthesizableItem) block;
                }
            } else if (item instanceof SynthesizableItem) {
                return (SynthesizableItem) item;
            }
        }

        return null;
    }

    static boolean isSynthesizableItem(ItemStack stack) {
        return getSynthesizableItem(stack) != null;
    }

    boolean isSynthesizable(ItemStack stack);

    ItemStack getSynthesizedItem(ItemStack stack, Random random);
}
