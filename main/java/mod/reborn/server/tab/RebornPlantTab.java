package mod.reborn.server.tab;

import com.google.common.collect.Ordering;
import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.item.FossilItem;
import mod.reborn.server.item.ItemHandler;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RebornPlantTab extends CreativeTabs {
    private int[] metas;
    static Comparator<ItemStack> tabSorter;

    public RebornPlantTab(String label) {
        super(label);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ItemHandler.PLANT_CALLUS);
    }
}
