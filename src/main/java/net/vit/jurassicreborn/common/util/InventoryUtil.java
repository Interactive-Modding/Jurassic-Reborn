package net.vit.jurassicreborn.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;

public final class InventoryUtil {

    private InventoryUtil() {
    }

    public static void dropContents(Level level, BlockPos pos, IItemHandler handler) {
        if (level == null || handler == null) {
            return;
        }

        SimpleContainer container = new SimpleContainer(handler.getSlots());
        for (int slot = 0; slot < handler.getSlots(); slot++) {
            ItemStack stack = handler.getStackInSlot(slot);
            if (!stack.isEmpty()) {
                container.setItem(slot, stack.copy());
            }
        }

        Containers.dropContents(level, pos, container);
    }
}
