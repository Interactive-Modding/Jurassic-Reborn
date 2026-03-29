package net.vit.jurassicreborn.common.blocks.entities;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

/**
 * Direction aware item handler wrapper used to expose the correct slots to automation such as hoppers.
 */
public class MachineItemHandlerSideWrapper implements IItemHandler {

    private final MachineItemStackHandler handler;
    private final Direction direction;

    public MachineItemHandlerSideWrapper(MachineItemStackHandler handler, Direction direction) {
        this.handler = handler;
        this.direction = direction;
    }

    @Override
    public int getSlots() {
        return handler.getSlots();
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return handler.getStackInSlot(slot);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (!allowsInsertion(slot, stack)) {
            return stack;
        }
        return handler.insertItem(slot, stack, simulate);
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (!allowsExtraction(slot)) {
            return ItemStack.EMPTY;
        }
        return handler.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return handler.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (!allowsInsertion(slot, stack)) {
            return false;
        }
        return handler.isItemValid(slot, stack);
    }

    private boolean allowsInsertion(int slot, ItemStack stack) {
        if (direction == Direction.DOWN) {
            return false;
        }
        return handler.isInputSlot(slot) && handler.isItemValid(slot, stack);
    }

    private boolean allowsExtraction(int slot) {
        if (direction == Direction.UP) {
            return false;
        }
        return handler.isOutputSlot(slot);
    }
}
