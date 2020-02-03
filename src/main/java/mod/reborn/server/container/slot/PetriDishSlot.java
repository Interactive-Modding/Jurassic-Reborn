package mod.reborn.server.container.slot;

import mod.reborn.server.item.ItemHandler;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class PetriDishSlot extends Slot {
    public PetriDishSlot(IInventory inventory, int slotIndex, int xPosition, int yPosition) {
        super(inventory, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() == ItemHandler.PLANT_CELLS_PETRI_DISH || stack.getItem() == ItemHandler.PETRI_DISH;
    }
}
