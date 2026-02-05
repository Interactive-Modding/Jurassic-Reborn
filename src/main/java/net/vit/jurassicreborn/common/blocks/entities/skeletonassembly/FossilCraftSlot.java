package net.vit.jurassicreborn.common.blocks.entities.skeletonassembly;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.items.Fossils.FossilItem;

/** A 1-item slot that only accepts fossils (or stays empty). */
public class FossilCraftSlot extends Slot {

    public FossilCraftSlot(Container inv, int index, int x, int y) {
        super(inv, index, x, y);
    }

    /** Limit each fossil slot to a single bone. */
    @Override
    public int getMaxStackSize() {
        return 1;
    }

    /** Optional: restrict to FossilItem only. */
    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof FossilItem
                || stack.isEmpty();
    }
}
