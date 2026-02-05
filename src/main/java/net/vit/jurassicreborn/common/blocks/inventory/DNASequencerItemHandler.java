package net.vit.jurassicreborn.common.blocks.inventory;

import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASequencer.DNASequencerBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.util.api.SequencableItem;
import org.jetbrains.annotations.NotNull;

public class DNASequencerItemHandler extends MachineItemStackHandler {

    public static DNASequencerItemHandler instance() {
        return new DNASequencerItemHandler(DNASequencerBlockEntity.SLOTS,DNASequencerBlockEntity.INPUTS,
                DNASequencerBlockEntity.OUTPUTS);
    }

    public DNASequencerItemHandler(int slots, int[] inputs, int[] outputs) {
        super(slots, inputs, outputs);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        switch (slot) {
            case 0,2,4 -> {
                SequencableItem sequencableItem = SequencableItem.getSequencableItem(stack);
                return sequencableItem != null && sequencableItem.isSequencable(stack);
            }
            case 1,3,5 -> {
                return stack.getItem() == ModItems.STORAGE_DISC.get() && (stack.getTag() == null || !stack.getTag().contains("DNA"));
            }
            default -> {
                return false;
            }
        }
    }
}
