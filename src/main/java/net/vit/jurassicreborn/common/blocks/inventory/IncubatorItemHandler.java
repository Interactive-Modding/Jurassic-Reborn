package net.vit.jurassicreborn.common.blocks.inventory;

import com.google.common.primitives.Ints;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.entities.incubator.IncubatorBlockEntity;
import net.vit.jurassicreborn.common.items.IncubatorEnvironmentItem;
import net.vit.jurassicreborn.common.items.genetics.DinosaurEggItem;
import org.jetbrains.annotations.NotNull;

public class IncubatorItemHandler extends MachineItemStackHandler {

    public IncubatorItemHandler(int slots, int[] inputs, int[] outputs) {
        super(slots, inputs, outputs);
    }

    public static boolean isEnvironment(int slotID, Item item) {
        if(Ints.asList(IncubatorBlockEntity.ENVIRONMENT).contains(slotID)) {
            return item instanceof IncubatorEnvironmentItem;
        }
        return false;
    }

    @Override
    public boolean isItemValid(int slotID, @NotNull ItemStack stack) {
        if (Ints.asList(IncubatorBlockEntity.INPUTS).contains(slotID)) {
            return stack.getItem() instanceof DinosaurEggItem;
        }else
            return isEnvironment(slotID, stack.getItem());
    }

    @Override
    public int getSlotLimit(int slot) {
        if (Ints.asList(IncubatorBlockEntity.INPUTS).contains(slot)) {
            return 1;
        }
        return super.getSlotLimit(slot);
    }
}
