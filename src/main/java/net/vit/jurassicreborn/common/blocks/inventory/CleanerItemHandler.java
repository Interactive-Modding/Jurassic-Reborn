package net.vit.jurassicreborn.common.blocks.inventory;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.entities.cleaner.CleanerBlockEntity;
import net.vit.jurassicreborn.common.util.api.CleanableItem;
import org.jetbrains.annotations.NotNull;

public class CleanerItemHandler extends MachineItemStackHandler {



    public CleanerItemHandler(int slots, int[] inputs, int[] outputs) {
        super(slots, inputs, outputs);
    }

    public static CleanerItemHandler instance() {
        return new CleanerItemHandler(CleanerBlockEntity.SLOTS, CleanerBlockEntity.INPUTS, CleanerBlockEntity.OUTPUTS);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        switch (slot) {
            case 0 -> {
                Item item = stack.getItem();
                if (item instanceof CleanableItem) return true;
                if (item instanceof BlockItem blockItem) {
                    return blockItem.getBlock() instanceof CleanableItem;
                }
                return false;
            }
            case 1 -> {
                return stack.is(Items.WATER_BUCKET);
            }
            case 2,3,4,5,6,7 -> {
                return false;
            }
            default -> {
                return false;
            }
        }

    }
}
