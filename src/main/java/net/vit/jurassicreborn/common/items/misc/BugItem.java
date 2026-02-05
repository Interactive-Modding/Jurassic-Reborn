package net.vit.jurassicreborn.common.items.misc;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Items;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.TabHandler;
import net.vit.jurassicreborn.common.util.BreedableBug;


import java.util.function.Function;

public class BugItem extends Item implements BreedableBug {
    private final Function<ItemStack, Integer> breedings;

    public BugItem(Function<ItemStack, Integer> breedings) {
        super(new Item.Properties().tab(TabHandler.ITEMS));
        this.breedings = breedings;
    }

    @Override
    public int getBreedings(ItemStack stack) {
        return this.breedings.apply(stack);
    }

}