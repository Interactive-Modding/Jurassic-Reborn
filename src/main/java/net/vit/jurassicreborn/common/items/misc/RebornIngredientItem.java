package net.vit.jurassicreborn.common.items.misc;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.List;

public interface RebornIngredientItem {

    default List<ItemStack> getJEIRecipeTypes() {
        if(this instanceof Block) {
            return Lists.newArrayList(new ItemStack((Block) this));
        }
        return Lists.newArrayList(new ItemStack((Item) this));
    }
    List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem);

//    default List<ItemStack> getItemSubtypes(Item item) {
//        NonNullList<ItemStack> list = NonNullList.create();
//        item.getSubItems(CreativeTabs.SEARCH, list);
//        return Lists.newArrayList(list);
//    }
//
//    default List<ItemStack> getItemSubtypes(Block block) {
//        return getItemSubtypes(Item.getItemFromBlock(block));
//    }
}