package net.vit.jurassicreborn.common.blocks.entities.feeder;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class FilteredSlot extends Slot {
    private final Predicate<ItemStack> filter;

    public FilteredSlot(Container inv, int index, int x, int y, Predicate<ItemStack> filter) {
        super(inv, index, x, y);
        this.filter = filter;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return filter.test(stack);
    }
}
