package mod.reborn.server.block.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;

public abstract  class MachineBaseBlockEntity extends TileEntityLockable implements ISyncable, ITickable, ISidedInventory {
    protected String customName;

    protected int[] processTime = new int[this.getProcessCount()];
    protected int[] totalProcessTime = new int[this.getProcessCount()];

    @SideOnly(Side.CLIENT)
    public static boolean isProcessing(IInventory inventory, int index) {
        return inventory.getField(index) > 0;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        NBTTagList itemList = compound.getTagList("Items", 10);
        NonNullList<ItemStack> slots = NonNullList.create();
        for(int i = 0; i < this.getSizeInventory(); i++) {
            slots.add(ItemStack.EMPTY);
        }
        for (int i = 0; i < itemList.tagCount(); ++i) {
            NBTTagCompound item = itemList.getCompoundTagAt(i);
            byte slot = item.getByte("Slot");

            if (slot >= 0 && slot < this.getSizeInventory()) {
                slots.set(slot, new ItemStack(item));
            }
        }

        for (int i = 0; i < this.getProcessCount(); i++) {
            this.processTime[i] = compound.getShort("ProcessTime" + i);
            this.totalProcessTime[i] = compound.getShort("ProcessTimeTotal" + i);
        }

        if (compound.hasKey("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
        this.setSlots(slots);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        for (int i = 0; i < this.getProcessCount(); i++) {
            compound.setShort("ProcessTime" + i, (short) this.processTime[i]);
            compound.setShort("ProcessTimeTotal" + i, (short) this.totalProcessTime[i]);
        }

        NonNullList<ItemStack> slots = this.getSlots();

        NBTTagList itemList = new NBTTagList();

        for (int slot = 0; slot < this.getSizeInventory(); ++slot) {
            if (!slots.get(slot).isEmpty()) {
                NBTTagCompound itemTag = new NBTTagCompound();
                itemTag.setByte("Slot", (byte) slot);

                slots.get(slot).writeToNBT(itemTag);
                itemList.appendTag(itemTag);
            }
        }

        compound.setTag("Items", itemList);

        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }
        
        return compound;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.getSlots().get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        NonNullList<ItemStack> slots = this.getSlots();
        return ItemStackHelper.getAndSplit(slots, index, count);
    }

    @Override
	public NonNullList getSyncFields(NonNullList fields)
	{

		return fields;
	}
    
    @Override
    public ItemStack removeStackFromSlot(int index) {
        return removeStackFromSlot(index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        NonNullList<ItemStack> slots = this.getSlots();

        boolean stacksEqual = !stack.isEmpty() && stack.isItemEqual(slots.get(index)) && ItemStack.areItemStackTagsEqual(stack, slots.get(index));
        slots.set(index, stack);

        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (!stacksEqual) {
            int process = this.getProcess(index);
            if (process >= 0 && process < this.getProcessCount()) {
                this.totalProcessTime[process] = this.getStackProcessTime(stack);
                if (!this.canProcess(process)) {
                    this.processTime[process] = 0;
                }
                this.markDirty();
            }
            this.onSlotUpdate();
        }
    }

    private boolean isInput(int slot) {
        int[] inputs = this.getInputs();

        for (int input : inputs) {
            if (input == slot) {
                return true;
            }
        }

        return false;
    }

    private boolean isOutput(int slot) {
        int[] outputs = this.getOutputs();

        for (int output : outputs) {
            if (output == slot) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && this.customName.length() > 0;
    }

    public void setCustomInventoryName(String customName) {
        this.customName = customName;
    }

    @Override
    public int getSizeInventory() {
        return this.getSlots().size();
    }

    public boolean isProcessing(int index) {
        return this.processTime[index] > 0;
    }

    @Override
    public void update() {
    	
        NonNullList<ItemStack> slots = this.getSlots();
        
        if(!world.isRemote) {
        for (int process = 0; process < this.getProcessCount(); process++) {
            boolean flag = this.isProcessing(process);
            boolean dirty = false;

            boolean hasInput = false;

            for (int input : this.getInputs(process)) {
                if (!slots.get(input).isEmpty()) {
                    hasInput = true;
                    break;
                }
            }

            if (hasInput && this.canProcess(process)) {
                this.processTime[process]++;

                if (this.processTime[process] >= this.totalProcessTime[process]) {
                    this.processTime[process] = 0;
                    int total = 0;
                    for (int input : this.getInputs()) {
                        ItemStack stack = slots.get(input);
                        if (!stack.isEmpty()) {
                            total = this.getStackProcessTime(stack);
                            break;
                        }
                    }
                    this.totalProcessTime[process] = total;
                    if (!this.world.isRemote)
                    {
                    this.processItem(process);
                    this.onSlotUpdate();
                    }
                  
                }

                dirty = true;
            } else if (this.isProcessing(process)) {
                if (this.shouldResetProgress()) {
                    this.processTime[process] = 0;
                } else if (this.processTime[process] > 0) {
                    this.processTime[process]--;
                }

                dirty = true;
            }

            if (flag != this.isProcessing(process)) {
                dirty = true;
            }

            if (dirty && !this.world.isRemote) {
                this.markDirty();
            }
        }
        }
    }
    	

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return !this.isOutput(index);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return side == EnumFacing.DOWN ? this.getOutputs() : this.getInputs();
    }

    @Override
    public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
        return this.isItemValidForSlot(index, stack);
    }

    protected int getProcess(int slot) {
		return slot;
	}

    protected boolean canProcess(int process) {
		return true;
	}

    protected void processItem(int process) {
	}

    protected int getMainOutput(int process) {
		return 0;
	}

    protected int getStackProcessTime(ItemStack stack) {
		return 0;
	}

    protected int getProcessCount() {
		return 0;
	}

    protected abstract int[] getInputs();

    protected abstract int[] getInputs(int process);

    protected abstract int[] getOutputs();

    protected abstract NonNullList<ItemStack> getSlots();

    public boolean hasOutputSlot(ItemStack output) {
        return this.getOutputSlot(output) != -1;
    }

    public int getOutputSlot(ItemStack output) {
        NonNullList<ItemStack> slots = this.getSlots();
        int[] outputs = this.getOutputs();
        for (int slot : outputs) {
            ItemStack stack = slots.get(slot);
            if (stack.isEmpty() || ((ItemStack.areItemStackTagsEqual(stack, output) && stack.getCount() + output.getCount() <= stack.getMaxStackSize()) && stack.getItem() == output.getItem() && stack.getItemDamage() == output.getItemDamage())) {
                return slot;
            }
        }
        return -1;
    }

    @Override
    public int getField(int id) {
        int processCount = this.getProcessCount();
        if (id < processCount) {
            return this.processTime[id];
        } else if (id < processCount * 2) {
            return this.totalProcessTime[id - processCount];
        }

        return 0;
    }

    @Override
    public void setField(int id, int value) {
        int processCount = this.getProcessCount();

        if (id < processCount) {
            this.processTime[id] = value;
        } else if (id < processCount * 2) {
            this.totalProcessTime[id - processCount] = value;
        }
    }

    @Override
    public int getFieldCount() {
        return this.getProcessCount() * 2;
    }

    @Override
    public void clear() {
        NonNullList<ItemStack> slots = this.getSlots();

        for (int i = 0; i < slots.size(); ++i) {
            slots.set(i, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return true;
    }

    protected void mergeStack(int slot, ItemStack stack) {
        NonNullList<ItemStack> slots = this.getSlots();

        ItemStack previous = slots.get(slot);
        if (previous.isEmpty()) {
            slots.set(slot, stack);
        } else if (ItemStack.areItemsEqual(previous, stack) && ItemStack.areItemStackTagsEqual(previous, stack)) {
        	previous.setCount(previous.getCount() + stack.getCount());
        }
    }

    protected void decreaseStackSize(int slot) {
        NonNullList<ItemStack> slots = this.getSlots();

        slots.get(slot).shrink(1);

        if (slots.get(slot).getCount() <= 0) {
            slots.set(slot, ItemStack.EMPTY);
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }

    protected boolean shouldResetProgress() {
        return true;
    }

    protected void setSlots(NonNullList<ItemStack> slots) {}

    protected void onSlotUpdate() {}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public String getName() {
		return "rebornmod:machine_base_block";
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return null;
	}

	@Override
	public String getGuiID() {
		return null;
	}

    IItemHandler handler = new SidedInvWrapper(this, EnumFacing.UP);
    IItemHandler handlerBottom = new SidedInvWrapper(this, EnumFacing.DOWN);

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            if (facing == EnumFacing.DOWN) {
                return (T) handlerBottom;
            }
            else {
                return (T) handler;
            }
        return super.getCapability(capability, facing);
    }
    
    @Override
	public void packetDataHandler(ByteBuf dataStream) {}
}
