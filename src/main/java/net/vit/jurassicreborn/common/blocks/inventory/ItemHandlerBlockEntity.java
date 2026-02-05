package net.vit.jurassicreborn.common.blocks.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.ApiStatus;

public interface ItemHandlerBlockEntity {

    IItemHandlerModifiable getItemHandler();

    @ApiStatus.NonExtendable
    default ItemStack getItem(int slot) {
        return getItemHandler().getStackInSlot(slot);
    }

    default void setItem(int slot, ItemStack stack) {
        getItemHandler().setStackInSlot(slot, stack);
    }

}
