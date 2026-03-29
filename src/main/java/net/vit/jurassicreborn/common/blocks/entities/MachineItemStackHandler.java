package net.vit.jurassicreborn.common.blocks.entities;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class MachineItemStackHandler extends ItemStackHandler {

    private final int[] inputs;
    private final int[] outputs;
    private Runnable changeListener = () -> {};

    public MachineItemStackHandler(int slots, int[] inputs, int[] outputs) {
        super(slots);
        this.inputs = inputs;
        this.outputs = outputs;
    }

    protected boolean isInputSlot(int slot) {
        for (int input : inputs) {
            if (input == slot) {
                return true;
            }
        }
        return false;
    }

    protected boolean isOutputSlot(int slot) {
        for (int output : outputs) {
            if (output == slot) {
                return true;
            }
        }
        return false;
    }

    public int[] getAllInputSlots() {
        return inputs;
    }

    public int[] getAllOutputSlots() {
        return outputs;
    }
    /**
     * Set a listener that will run whenever any slot in this handler is
     * modified.  Used by block entities to push inventory changes to the
     * client so that renderers can display the correct items.
     */
    public void setChangeListener(Runnable listener) {
        this.changeListener = listener != null ? listener : () -> {};
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        changeListener.run();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return isInputSlot(slot) && super.isItemValid(slot, stack);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (!isInputSlot(slot) || !isItemValid(slot, stack)) {
            return stack;
        }

        return super.insertItem(slot, stack, simulate);
    }
}
