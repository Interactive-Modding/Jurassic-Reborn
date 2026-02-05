package net.vit.jurassicreborn.common.util.api;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Random;

public interface GrindableItem extends RebornIngredientItem {

    public static GrindableItem defaultGrindableItem = new GrindableItem() {
        @Override
        public boolean isGrindable(ItemStack stack) {
            return false;
        }

        @Override
        public ItemStack getGroundItem(ItemStack stack, Random random) {
            return ItemStack.EMPTY;
        }

        @Override
        public List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem) {
            return List.of(Pair.of(1.0f, ItemStack.EMPTY));
        }
    };

    static GrindableItem getGrindableItem(ItemStack stack) {
        if (!stack.isEmpty()) {
            Item item = stack.getItem();

            if (item instanceof BlockItem blockItem) {
                Block block = (blockItem).getBlock();

                if (block instanceof GrindableItem) {
                    return (GrindableItem) block;
                }
            } else if (item instanceof GrindableItem) {
                return (GrindableItem) item;
            }
        }

        return defaultGrindableItem;
    }

    static boolean isGrindableItem(ItemStack stack) {
        GrindableItem grindableItem = getGrindableItem(stack);
        return grindableItem != null && grindableItem != defaultGrindableItem;
    }

    boolean isGrindable(ItemStack stack);

    ItemStack getGroundItem(ItemStack stack, Random random);
}
