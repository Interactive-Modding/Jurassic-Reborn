package net.vit.jurassicreborn.common.util.api;

import java.util.Random;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public interface SynthesizableItem extends RebornIngredientItem {
    static SynthesizableItem getSynthesizableItem(ItemStack stack) {
        if (stack != null) {
            Item item = stack.getItem();

            if (item instanceof BlockItem) {
                Block block = ((BlockItem) item).getBlock();

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
