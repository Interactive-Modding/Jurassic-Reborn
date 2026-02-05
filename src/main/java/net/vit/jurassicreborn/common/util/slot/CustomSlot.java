package net.vit.jurassicreborn.common.util.slot;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class CustomSlot extends ToggleableSlot {
    private Predicate<ItemStack> item;
    private final int stackLimit;

    public CustomSlot(Container inventory, int slotIndex, int xPosition, int yPosition, Predicate<ItemStack> item) {
        this(inventory, slotIndex, xPosition, yPosition, item, 64);
    }

    public CustomSlot(Container inventory, int slotIndex, int xPosition, int yPosition, Predicate<ItemStack> item, int stackLimit) {
        super(inventory, slotIndex, xPosition, yPosition);
        this.item = item;
        this.stackLimit = stackLimit;
    }



    @Override
    public boolean mayPlace(ItemStack stack) {
        return this.item.test(stack);
    }

    @Override
    public int getMaxStackSize() {
        return stackLimit;
    }


}
