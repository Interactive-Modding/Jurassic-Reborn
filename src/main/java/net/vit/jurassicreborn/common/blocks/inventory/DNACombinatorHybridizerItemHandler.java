package net.vit.jurassicreborn.common.blocks.inventory;

import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.items.ModItems;
import org.jetbrains.annotations.NotNull;

public class DNACombinatorHybridizerItemHandler extends MachineItemStackHandler {
    public DNACombinatorHybridizerItemHandler(int slots, int[] inputs, int[] outputs) {
        super(slots, inputs, outputs);
    }



    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        switch(slot) {
            case 0,1,2,3,4,5,6,7,8,9 -> {
                return stack.getItem() == ModItems.STORAGE_DISC.get() && (stack.getTag() != null && stack.getTag().contains("DNA"));
            }
            case 10,11 -> {
                return false;//stack.getItem() == ModItems.STORAGE_DISC.get() && (stack.getTag() == null || !stack.getTag().contains("DNA"));
            }
            default -> {
                return false;
            }
        }
    }
}
