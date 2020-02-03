package mod.reborn.server.container.slot;

import mod.reborn.server.item.DNAItem;
import mod.reborn.server.item.PlantDNAItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class TestTubeSlot extends Slot {
    public TestTubeSlot(IInventory inventory, int slotIndex, int xPosition, int yPosition) {
        super(inventory, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof DNAItem || stack.getItem() instanceof PlantDNAItem;
    }
}
