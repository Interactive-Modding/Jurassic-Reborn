package net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNACombinatorHybridizer;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraft.world.inventory.Slot;

/**
 * A {@link SlotItemHandler} that can be toggled active/inactive.
 * Used for menus that need to switch between different slot layouts
 * without recreating the menu.
 */
public class ToggleableSlotItemHandler extends SlotItemHandler {
    private boolean active = true;

    public ToggleableSlotItemHandler(IItemHandler handler, int index, int x, int y) {
        super(handler, index, x, y);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Convenience builder to set the initial active state fluently.
     */
    public Slot activeBuilder(boolean isActive) {
        this.setActive(isActive);
        return this;
    }
}