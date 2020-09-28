package mod.reborn.server.container.slot;

import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.item.SyringeItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class CultivatorSyringeSlot extends Slot {
    public CultivatorSyringeSlot(IInventory inventory, int slotIndex, int xPosition, int yPosition) {
        super(inventory, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof SyringeItem && SyringeItem.getDinosaur(stack).getBirthType() == Dinosaur.BirthType.LIVE_BIRTH;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }

    @Override
    public int getSlotStackLimit() {
        return 1;
    }
}
