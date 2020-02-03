package mod.reborn.server.container.slot;

import mod.reborn.server.food.FoodNutrients;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class NutrientSlot extends Slot {
    public NutrientSlot(IInventory inventory, int slotIndex, int xPosition, int yPosition) {
        super(inventory, slotIndex, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return FoodNutrients.NUTRIENTS.containsKey(stack.getItem());
    }
}
