package net.vit.jurassicreborn.common.blocks.inventory;

import com.google.common.primitives.Ints;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.entities.grinder.FossilGrinderBlockEntity;
import net.vit.jurassicreborn.common.util.api.GrindableItem;
import org.jetbrains.annotations.NotNull;

public class FossilGrinderItemHandler extends MachineItemStackHandler {

    public static FossilGrinderItemHandler instance() {
        return new FossilGrinderItemHandler(FossilGrinderBlockEntity.SLOTS,FossilGrinderBlockEntity.INPUTS,FossilGrinderBlockEntity.OUTPUTS);
    }

    public FossilGrinderItemHandler(int slots, int[] inputs, int[] outputs) {
        super(slots, inputs, outputs);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (Ints.asList(FossilGrinderBlockEntity.INPUTS).contains(slot)) {
            return GrindableItem.getGrindableItem(stack) != null && GrindableItem.getGrindableItem(stack).isGrindable(stack);
        }
        return false;
    }
}
