package net.vit.jurassicreborn.common.util.slot;

import net.vit.jurassicreborn.common.items.ModItems;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class StorageSlot extends ToggleableSlot {

    private final int stackLimit;
    private boolean stored;

    public StorageSlot(Container inventory, int slotIndex, int xPosition, int yPosition, boolean stored) {
        this(inventory, slotIndex, xPosition, yPosition, stored, 64);
    }

    public StorageSlot(Container inventory, int slotIndex, int xPosition, int yPosition, boolean stored, int stackLimit) {
        super(inventory, slotIndex, xPosition, yPosition);
        this.stored = stored;
        this.stackLimit = stackLimit;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (this.stored) {
            return stack.getItem() == ModItems.STORAGE_DISC.get() && (stack.getTag() != null && stack.getTag().contains("DNA"));
        } else {
            return stack.getItem() == ModItems.STORAGE_DISC.get() && (stack.getTag() == null || !stack.getTag().contains("DNA"));
        }
    }





    @Override
    public int getMaxStackSize() {
        return stackLimit;
    }
}