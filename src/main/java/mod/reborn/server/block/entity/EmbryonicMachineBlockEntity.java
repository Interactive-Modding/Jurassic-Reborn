package mod.reborn.server.block.entity;

import com.google.common.primitives.Ints;
import mod.reborn.RebornMod;
import mod.reborn.server.container.EmbryonicMachineContainer;
import mod.reborn.server.item.DNAItem;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.item.PlantDNAItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

public class EmbryonicMachineBlockEntity extends MachineBaseBlockEntity {
    private static final int[] INPUTS = new int[] { 0, 1, 2 };
    private static final int[] OUTPUTS = new int[] { 3, 4, 5, 6 };

    private NonNullList<ItemStack> slots = NonNullList.<ItemStack>withSize(7, ItemStack.EMPTY);

    @Override
    protected int getProcess(int slot) {
        return 0;
    }

    @Override
    protected boolean canProcess(int process) {
        ItemStack dna = this.slots.get(0);
        ItemStack petridish = this.slots.get(1);
        ItemStack syringe = this.slots.get(2);

        if (dna != null && petridish != null && syringe != null && syringe.getItem() == ItemHandler.EMPTY_SYRINGE) {
            ItemStack output = null;

            if (petridish.getItem() == ItemHandler.PETRI_DISH && dna.getItem() instanceof DNAItem) {
                output = new ItemStack(ItemHandler.SYRINGE, 1, dna.getItemDamage());
                output.setTagCompound(dna.getTagCompound());
            } else if (petridish.getItem() == ItemHandler.PLANT_CELLS_PETRI_DISH && dna.getItem() instanceof PlantDNAItem) {
                output = new ItemStack(ItemHandler.PLANT_CALLUS, 1, dna.getItemDamage());
                output.setTagCompound(dna.getTagCompound());
            }

            return output != null && this.hasOutputSlot(output);
        }

        return false;
    }

    @Override
    protected void processItem(int process) {
        if (this.canProcess(process)) {
            ItemStack output = null;

            if (this.slots.get(0).getItem() instanceof DNAItem && this.slots.get(1).getItem() == ItemHandler.PETRI_DISH) {
                output = new ItemStack(ItemHandler.SYRINGE, 1, this.slots.get(0).getItemDamage());
            } else if (this.slots.get(0).getItem() instanceof PlantDNAItem && this.slots.get(1).getItem() == ItemHandler.PLANT_CELLS_PETRI_DISH) {
                output = new ItemStack(ItemHandler.PLANT_CALLUS, 1, this.slots.get(0).getItemDamage());
            }

            output.setTagCompound(this.slots.get(0).getTagCompound());

            int emptySlot = this.getOutputSlot(output);

            if (emptySlot != -1) {
                this.mergeStack(emptySlot, output);

                this.decreaseStackSize(0);
                this.decreaseStackSize(1);
                this.decreaseStackSize(2);
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
        return new EmbryonicMachineContainer(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return RebornMod.MODID + ":embryonic_machine";
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.embryonic_machine";
    }
    
    @Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side) {
		return Ints.asList(OUTPUTS).contains(slotID);
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {
		if (Ints.asList(INPUTS).contains(slotID)) {
			if ((slotID == 0 && itemstack != null && (itemstack.getItem() instanceof DNAItem || itemstack.getItem() instanceof PlantDNAItem) && this.getStackInSlot(slotID).getCount() == 0)
					|| (slotID == 1 && itemstack != null && (itemstack.getItem() == ItemHandler.PLANT_CELLS_PETRI_DISH || itemstack.getItem() == ItemHandler.PETRI_DISH))
					|| (slotID == 2 && itemstack != null && itemstack.getItem() == ItemHandler.EMPTY_SYRINGE)) {

				return true;
			}
		}

		return false;
	}

    @Override
    protected void onSlotUpdate() {
        super.onSlotUpdate();
        this.world.markBlockRangeForRenderUpdate(this.pos, this.pos);
    }

	@Override
	public boolean isEmpty() {
		return false;
	}
}
