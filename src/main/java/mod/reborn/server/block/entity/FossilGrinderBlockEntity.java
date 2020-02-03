package mod.reborn.server.block.entity;

import com.google.common.primitives.Ints;
import mod.reborn.RebornMod;
import mod.reborn.server.api.GrindableItem;
import mod.reborn.server.container.FossilGrinderContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

import java.util.Random;

public class FossilGrinderBlockEntity extends MachineBaseBlockEntity {
    private static final int[] INPUTS = new int[] { 0, 1, 2, 3, 4, 5 };
    private static final int[] OUTPUTS = new int[] { 6, 7, 8, 9, 10, 11 };

    private NonNullList<ItemStack> slots = NonNullList.withSize(12, ItemStack.EMPTY);

    @Override
    protected int getProcess(int slot) {
        return 0;
    }

    @Override
    protected boolean canProcess(int process) {
        for (int inputIndex = 0; inputIndex < 6; inputIndex++) {
            ItemStack input = this.slots.get(inputIndex);

            GrindableItem grindableItem = GrindableItem.getGrindableItem(input);

            if (grindableItem != null && grindableItem.isGrindable(input)) {
                for (int outputIndex = 6; outputIndex < 12; outputIndex++) {
                    if (this.slots.get(outputIndex).isEmpty()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
    
	@Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side) {
		return Ints.asList(OUTPUTS).contains(slotID);
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {
		if (Ints.asList(INPUTS).contains(slotID)) {
			if (itemstack != null && GrindableItem.getGrindableItem(itemstack) != null && GrindableItem.getGrindableItem(itemstack).isGrindable(itemstack)) {
				return true;
			}
		}

		return false;
	}

    @Override
    protected void processItem(int process) {
        Random rand = new Random();

        ItemStack input = ItemStack.EMPTY;
        int index = 0;

        for (int inputIndex = 0; inputIndex < 6; inputIndex++) {
            input = this.slots.get(inputIndex);

            if (!input.isEmpty()) {
                index = inputIndex;
                break;
            }
        }

        if (!input.isEmpty()) {
            GrindableItem grindableItem = GrindableItem.getGrindableItem(input);

            ItemStack output = grindableItem.getGroundItem(input, rand);

            int emptySlot = this.getOutputSlot(output);
            if (emptySlot != -1) {
                this.mergeStack(emptySlot, output);
                this.decreaseStackSize(index);
            }
        }
    }

    @Override
    protected int getMainOutput(int process) {
        return 1;
    }

    @Override
    protected int getStackProcessTime(ItemStack stack) {
        return 200;
    }

    @Override
    protected int getProcessCount() {
        return 1;
    }

    @Override
    protected int[] getInputs() {
        return INPUTS;
    }

    @Override
    protected int[] getInputs(int process) {
        return this.getInputs();
    }

    @Override
    protected int[] getOutputs() {
        return OUTPUTS;
    }

    @Override
    protected NonNullList<ItemStack> getSlots() {
        return this.slots;
    }

    @Override
    protected void setSlots(NonNullList<ItemStack> slots) {
        this.slots = slots;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
        return new FossilGrinderContainer(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return RebornMod.MODID + ":fossil_grinder";
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.fossil_grinder";
    }

	@Override
	public boolean isEmpty() {
		return false;
	}

}
