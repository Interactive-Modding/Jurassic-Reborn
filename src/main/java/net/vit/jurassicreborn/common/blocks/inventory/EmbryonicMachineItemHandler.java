package net.vit.jurassicreborn.common.blocks.inventory;

import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.blocks.entities.Embryoncis.EmbryonicMachine.EmbryonicMachineBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.genetics.DNAItem;
import net.vit.jurassicreborn.common.items.genetics.PlantDNAItem;
import org.jetbrains.annotations.NotNull;

public class EmbryonicMachineItemHandler extends MachineItemStackHandler {

    public static EmbryonicMachineItemHandler instance() {
        return new EmbryonicMachineItemHandler(EmbryonicMachineBlockEntity.SLOTS,
                EmbryonicMachineBlockEntity.INPUTS,EmbryonicMachineBlockEntity.OUTPUTS);
    }

    public EmbryonicMachineItemHandler(int slots, int[] inputs, int[] outputs) {
        super(slots, inputs, outputs);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        switch (slot) {
            case 0 -> {
                return stack.getItem() instanceof DNAItem || stack.getItem() instanceof PlantDNAItem;
            }
            case 1-> {
                return stack.getItem() == ModItems.PLANT_CELLS_PETRI_DISH.get() || stack.getItem() == ModItems.PETRI_DISH.get();
            }
             case 2 -> {
                 return stack.getItem() == ModItems.EMPTY_SYRINGE.get();
             }
            default -> {
                return false;
            }
        }
    }
}
