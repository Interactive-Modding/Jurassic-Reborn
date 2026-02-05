package net.vit.jurassicreborn.common.blocks.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.vit.jurassicreborn.common.blocks.entities.Embryoncis.EmbryoCalcificationMachine.EmbryoCalcificationMachineBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.items.genetics.SyringeItem;
import org.jetbrains.annotations.NotNull;

public class EmbryoCalcificationMachineItemHandler extends MachineItemStackHandler {

    public static EmbryoCalcificationMachineItemHandler instance() {
        return new EmbryoCalcificationMachineItemHandler(EmbryoCalcificationMachineBlockEntity.SLOTS,
                EmbryoCalcificationMachineBlockEntity.INPUTS,EmbryoCalcificationMachineBlockEntity.OUTPUTS);
    }

    public EmbryoCalcificationMachineItemHandler(int slots, int[] inputs, int[] outputs) {
        super(slots, inputs, outputs);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        switch (slot) {
            case 0 -> {
                return stack.getItem() instanceof SyringeItem syringe && syringe.getDinosaur(stack).getBirthType() == Dinosaur.BirthType.EGG_LAYING;
            }
            case 1 -> {
                return stack.getItem() == Items.EGG;
            }
            default -> {
                return false;
            }
        }

    }
}
