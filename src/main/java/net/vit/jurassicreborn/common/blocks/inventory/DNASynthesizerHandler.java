package net.vit.jurassicreborn.common.blocks.inventory;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.util.api.SynthesizableItem;
import org.jetbrains.annotations.NotNull;

public class DNASynthesizerHandler extends MachineItemStackHandler {
    public DNASynthesizerHandler(int slots, int[] inputs, int[] outputs) {
        super(slots, inputs, outputs);
    }


    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return switch (slot) {
            case 0 -> SynthesizableItem.getSynthesizableItem(stack) != null && SynthesizableItem.getSynthesizableItem(stack).isSynthesizable(stack);
            case 1 -> stack.getItem() == ModItems.EMPTY_TEST_TUBE.get();
            case 2 -> stack.getItem() == ModItems.DNA_NUCLEOTIDES.get();
            case 3,4,5,6 -> false;
            default -> false;
        };
    }
}
