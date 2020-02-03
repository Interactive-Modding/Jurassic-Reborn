package mod.reborn.server.container.slot;

import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.item.SyringeItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SyringeSlot extends Slot {
    public SyringeSlot(IInventory inventory, int slotIndex, int xPosition, int yPosition) {
        super(inventory, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof SyringeItem && SyringeItem.getDinosaur(stack).getBirthType() == Dinosaur.BirthType.EGG_LAYING;
    }
}
