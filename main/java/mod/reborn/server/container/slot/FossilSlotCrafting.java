package mod.reborn.server.container.slot;

import net.minecraft.inventory.IInventory;

public class FossilSlotCrafting extends FossilSlot {

    public FossilSlotCrafting(IInventory inventory, int slotIndex, int xPosition, int yPosition) {
        super(inventory, slotIndex, xPosition, yPosition);
        // TODO Auto-generated constructor stub
    }


    @Override
    public int getSlotStackLimit() {
        return 1;
    }

}
