package net.vit.jurassicreborn.common.blocks.inventory;

import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNAExtractor.DNAExtractorBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.items.Food.DinosaurMeatItem;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.util.slot.CustomSlot;
import org.jetbrains.annotations.NotNull;

public class DNAExtractorHandler extends MachineItemStackHandler {

    public static DNAExtractorHandler instance() {
        return new DNAExtractorHandler(DNAExtractorBlockEntity.SLOTS,DNAExtractorBlockEntity.INPUTS,DNAExtractorBlockEntity.OUTPUTS);
    }

    public DNAExtractorHandler(int slots, int[] inputs, int[] outputs) {
        super(slots, inputs, outputs);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        switch (slot) {
            case 0 -> {
                return stack.getItem() == ModItems.MOSQUITO_AMBER.get() || stack.getItem() == ModItems.APHID_AMBER.get() || stack.getItem() == ModItems.SEA_LAMPREY.get() ||stack.getItem() == ModItems.FROZEN_LEECH_ITEM.get() || (stack.getItem() instanceof DinosaurMeatItem);
            }
            case 1-> {
                return stack.getItem() == ModItems.STORAGE_DISC.get() && (stack.getTag() == null || !stack.getTag().contains("DNA"));
            }
            default -> {
                return false;
            }
        }
    }
}
