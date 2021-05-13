package mod.reborn.server.tab;

import mod.reborn.server.items.ItemHandler;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import java.util.Comparator;

public class RebornPlantTab extends ItemGroup {
    private int[] metas;
    static Comparator<ItemStack> tabSorter;

    public RebornPlantTab(String label) {
        super(label);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ItemHandler.PLANT_CALLUS);
    }
}
