package mod.reborn.server.block.entity;

import com.google.common.primitives.Ints;
import mod.reborn.RebornMod;
import mod.reborn.server.api.SynthesizableItem;
import mod.reborn.server.container.DNASynthesizerContainer;
import mod.reborn.server.item.ItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

import java.util.Random;

public class DNASynthesizerBlockEntity extends MachineBaseBlockEntity {
    private static final int[] INPUTS = new int[] { 0, 1, 2 };
    private static final int[] OUTPUTS = new int[] { 3, 4, 5, 6 };

    private NonNullList<ItemStack> slots = NonNullList.withSize(7, ItemStack.EMPTY);

    @Override
    protected int getProcess(int slot) {
        return 0;
    }

    @Override
    protected boolean canProcess(int process) {
        ItemStack storage = this.slots.get(0);
        ItemStack testTube = this.slots.get(1);
        ItemStack baseMaterial = this.slots.get(2);

        SynthesizableItem synthesizableItem = SynthesizableItem.getSynthesizableItem(storage);

        if (synthesizableItem != null && synthesizableItem.isSynthesizable(storage) && testTube.getItem() == ItemHandler.EMPTY_TEST_TUBE && baseMaterial.getItem() == ItemHandler.DNA_NUCLEOTIDES && (storage.getTagCompound() != null && storage.getTagCompound().hasKey("DNAQuality"))) {
            ItemStack output = synthesizableItem.getSynthesizedItem(storage, new Random());

            return output != null && this.hasOutputSlot(output);
        }

        return false;
    }

    @Override
    protected void processItem(int process) {
        ItemStack storageDisc = this.slots.get(0);

        ItemStack output = SynthesizableItem.getSynthesizableItem(storageDisc).getSynthesizedItem(storageDisc, new Random());

        int emptySlot = this.getOutputSlot(output);

        if (emptySlot != -1) {
            this.mergeStack(emptySlot, output);

            this.decreaseStackSize(1);
            this.decreaseStackSize(2);
        }
    }

    @Override
    protected int getMainOutput(int process) {
        return 1;
    }

    @Override
    protected int getStackProcessTime(ItemStack stack) {
        return 1000;
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
        return new DNASynthesizerContainer(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return RebornMod.MODID + ":dna_synthesizer";
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.dna_synthesizer";
    }

    @Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side) {
		return Ints.asList(OUTPUTS).contains(slotID);
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {
		if (Ints.asList(INPUTS).contains(slotID)) {
			if ((slotID == 0 && itemstack != null && SynthesizableItem.getSynthesizableItem(itemstack) != null && SynthesizableItem.getSynthesizableItem(itemstack).isSynthesizable(itemstack))
					|| slotID == 1 && itemstack != null && itemstack.getItem() == ItemHandler.EMPTY_TEST_TUBE
					|| slotID == 2 && itemstack != null && itemstack.getItem() == ItemHandler.DNA_NUCLEOTIDES) {

				return true;
			}
		}

		return false;
	}
    
	@Override
	public boolean isEmpty() {
		return false;
	}
}
