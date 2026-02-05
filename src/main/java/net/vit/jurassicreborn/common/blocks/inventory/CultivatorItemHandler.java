package net.vit.jurassicreborn.common.blocks.inventory;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.entities.cultivator.CultivatorBlockEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.items.genetics.SyringeItem;
import org.jetbrains.annotations.NotNull;

public class CultivatorItemHandler extends MachineItemStackHandler {
    public CultivatorItemHandler(int slots, int[] inputs, int[] outputs) {
        super(slots, inputs, outputs);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        switch (slot) {
            case 0 -> {
                return stack.getItem() instanceof SyringeItem syringeItem && syringeItem.getDinosaur(stack).getBirthType() == Dinosaur.BirthType.LIVE_BIRTH;
            }
            case 1 -> {
                return CultivatorBlockEntity.FoodNutrients.NUTRIENTS.containsKey(stack.getItem());
            }
            case 2 -> {
                return stack.getItem() instanceof BucketItem;
            }
            case 3 -> {
                return false;
            }
            default -> {
                return false;
            }
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return slot == 0 ? 1 : super.getSlotLimit(slot);
    }
}
