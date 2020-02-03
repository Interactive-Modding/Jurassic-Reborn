package mod.reborn.server.container.slot;

import mod.reborn.server.api.CleanableItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class CleanableItemSlot extends Slot {
    public CleanableItemSlot(IInventory inventory, int slotIndex, int xPosition, int yPosition) {
        super(inventory, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        CleanableItem cleanableItem = CleanableItem.getCleanableItem(stack);
        return cleanableItem != null && cleanableItem.isCleanable(stack);
    }
}
