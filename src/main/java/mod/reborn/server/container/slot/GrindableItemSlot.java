package mod.reborn.server.container.slot;

import mod.reborn.server.api.GrindableItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class GrindableItemSlot extends Slot {
    public GrindableItemSlot(IInventory inventory, int slotIndex, int xPosition, int yPosition) {
        super(inventory, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        GrindableItem grindableItem = GrindableItem.getGrindableItem(stack);
        return grindableItem != null && grindableItem.isGrindable(stack);
    }
}
