package net.vit.jurassicreborn.common.blocks.entities.skeletonassembly;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

/**
 * Item handler for the Skeleton Assembler.
 * <p>
 * Slots {@code 0-24} form the 5×5 fossil grid and slot {@code 25}
 * is used as the finished result slot.
 * Implements both {@link IItemHandlerModifiable} and {@link Container} so the
 * Menu can treat it like a vanilla inventory.
 */
public class SkeletonAssemblerItemHandler
        extends ItemStackHandler           // already gives us IItemHandlerModifiable
        implements Container
{
    /** Total slots: 25 grid + 1 result */
    public static final int SIZE = 26;

    /** Listener invoked whenever the contents change. */
    private Runnable changeListener = () -> {};

    public SkeletonAssemblerItemHandler() {
        super(SIZE);
    }

    /** Register a listener that runs whenever this handler's contents change. */
    public void setChangeListener(@NotNull Runnable listener) {
        this.changeListener = listener;
    }

    /* ---------- Container delegates ---------------------------------- */

    @Override public int getContainerSize()                { return getSlots(); }

    @Override public boolean isEmpty() {
        for (int i = 0; i < getSlots(); i++)
            if (!getStackInSlot(i).isEmpty()) return false;
        return true;
    }

    @Override public @NotNull ItemStack getItem(int slot)  { return getStackInSlot(slot); }

    @Override public @NotNull ItemStack removeItem(int slot, int amount) {
        return extractItem(slot, amount, false);            // marks dirty for us
    }

    @Override public @NotNull ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = getStackInSlot(slot);
        setStackInSlot(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override public void setItem(int slot, @NotNull ItemStack stack) {
        setStackInSlot(slot, stack);                        // built-in bounds check
    }

    @Override
    public void setChanged() {
        changeListener.run();
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        changeListener.run();
    }

    @Override public void clearContent() {
        for (int i = 0; i < getSlots(); i++) setStackInSlot(i, ItemStack.EMPTY);
    }

    @Override public boolean stillValid(@NotNull Player p) { return true; }
}
