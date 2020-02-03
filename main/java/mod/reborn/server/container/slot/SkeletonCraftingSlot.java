package mod.reborn.server.container.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;

public class SkeletonCraftingSlot extends SlotCrafting {
    private final InventoryCrafting craftMatrix;
    public SkeletonCraftingSlot(EntityPlayer player, InventoryCrafting craftingInventory, IInventory inventoryIn,
            int slotIndex, int xPosition, int yPosition) {
        super(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
        this.craftMatrix = craftingInventory;
    }
    
    @Override
    public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
        for(int x = 0;x<craftMatrix.getSizeInventory();x++){
            craftMatrix.setInventorySlotContents(x,ItemStack.EMPTY);
        }
        return super.onTake(thePlayer, stack);
    }
}
