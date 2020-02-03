package mod.reborn.server.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class CustomSlot extends Slot {
    private Predicate<ItemStack> item;
    private final int stackLimit;

    public CustomSlot(IInventory inventory, int slotIndex, int xPosition, int yPosition, Predicate<ItemStack> item) {
        this(inventory, slotIndex, xPosition, yPosition, item, 64);
    }
    
    public CustomSlot(IInventory inventory, int slotIndex, int xPosition, int yPosition, Predicate<ItemStack> item, int stackLimit) {
        super(inventory, slotIndex, xPosition, yPosition);
        this.item = item;
        this.stackLimit = stackLimit;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return this.item.test(stack);
    }
    
    @Override
    public int getSlotStackLimit() {
        return stackLimit;
    }
}
