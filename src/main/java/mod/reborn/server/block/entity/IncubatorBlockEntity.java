package mod.reborn.server.block.entity;

import com.google.common.primitives.Ints;
import io.netty.buffer.ByteBuf;
import mcp.mobius.waila.api.SpecialChars;
import mod.reborn.RebornMod;
import mod.reborn.server.api.IncubatorEnvironmentItem;
import mod.reborn.server.container.IncubatorContainer;
import mod.reborn.server.item.DinosaurEggItem;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.message.TileEntityFieldsMessage;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class IncubatorBlockEntity extends MachineBaseBlockEntity implements TemperatureControl {
    private static final int[] INPUTS = new int[] { 0, 1, 2, 3, 4 };
    private static final int[] ENVIRONMENT = new int[] { 5 };

    private int[] temperature = new int[5];
    
    public float lidAngle, prevLidAngle;
    public int ticksSinceSync, numPlayersUsing;

    private NonNullList<ItemStack> slots = NonNullList.withSize(6, ItemStack.EMPTY);

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        for (int i = 0; i < this.getProcessCount(); i++) {
            this.temperature[i] = compound.getShort("Temperature" + i);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        for (int i = 0; i < this.getProcessCount(); i++) {
            compound.setShort("Temperature" + i, (short) this.temperature[i]);
        }

        return compound;
    }

    @Override
    protected int getProcess(int slot) {
        if (slot == 5) {
            return -1;
        } else {
            return slot;
        }
    }

    @Override
    protected boolean canProcess(int process) {
        ItemStack environment = this.slots.get(5);
        boolean hasEnvironment = false;

        if (!environment.isEmpty()) {
            Item item = environment.getItem();

            if (item instanceof IncubatorEnvironmentItem || Block.getBlockFromItem(item) instanceof IncubatorEnvironmentItem) {
                hasEnvironment = true;
            }
        }

        return hasEnvironment && !this.slots.get(process).isEmpty() && this.slots.get(process).getCount() > 0 && this.slots.get(process).getItem() instanceof DinosaurEggItem;
    }

    @Override
    protected void processItem(int process) {
        if (this.canProcess(process) && !this.world.isRemote) {
            ItemStack egg = this.slots.get(process);

            ItemStack incubatedEgg = new ItemStack(ItemHandler.HATCHED_EGG, 1, egg.getItemDamage());
            NBTTagCompound compound = new NBTTagCompound();
            compound.setBoolean("Gender", this.temperature[process] > 50);

            if (egg.getTagCompound() != null) {
                compound.setString("Genetics", egg.getTagCompound().getString("Genetics"));
                compound.setInteger("DNAQuality", egg.getTagCompound().getInteger("DNAQuality"));
            }

            incubatedEgg.setTagCompound(compound);

            this.decreaseStackSize(5);
            this.setInventorySlotContents(process, incubatedEgg);
        }
    }

    @Override
    protected int getMainOutput(int process) {
        return 0;
    }

    @Override
    protected int getStackProcessTime(ItemStack stack) {
        return 8000;
    }

    @Override
    protected int getProcessCount() {
        return 5;
    }

    @Override
    protected int[] getInputs() {
        return INPUTS;
    }

    @Override
    protected int[] getInputs(int process) {
        return new int[] { process };
    }

    @Override
    protected int[] getOutputs() {
        return ENVIRONMENT;
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
        return new IncubatorContainer(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return RebornMod.MODID + ":incubator";
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.incubator";
    }

    @Override
    public int getField(int id) 
    {
        if (id < 5) 
            return this.processTime[id];
        else if (id < 10) 
            return this.totalProcessTime[id - 5];
        else if (id < 15) 
            return this.temperature[id - 10];
        if(id == 15)
        	return this.numPlayersUsing;
        if(id == 16)
        	return this.ticksSinceSync;
        if(id == 17)
        	return (int)(this.lidAngle*100);
        if(id == 18)
        	return (int)(this.prevLidAngle*100);
        
        return 0;
    }

    @Override
    public void setField(int id, int value) 
    {
        if (id < 5)
            this.processTime[id] = value;
        else if (id < 10)
            this.totalProcessTime[id - 5] = value;
        else if (id < 15)
            this.temperature[id - 10] = value;
        if(id == 15)
        	this.numPlayersUsing = value;
        if(id == 16)
        	this.ticksSinceSync = value;
        if(id == 17)
        	this.lidAngle = (float)(value)/100.0f;
        if(id == 18)
        	this.prevLidAngle = (float)(value)/100.0f;
        
    }
    
    @Override
	public int[] getSlotsForFace(EnumFacing side) {
		
		return ArrayUtils.addAll(INPUTS, ENVIRONMENT);
	}
    
    @Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side) {
		return true;
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {
		if (Ints.asList(INPUTS).contains(slotID)) {
			if (itemstack != null && itemstack.getItem() == ItemHandler.EGG) {
				return true;
			}
		}else if(Ints.asList(ENVIRONMENT).contains(slotID)) {
			if (itemstack != null && itemstack.getItem() instanceof IncubatorEnvironmentItem || Block.getBlockFromItem(itemstack.getItem()) instanceof IncubatorEnvironmentItem) {
				return true;
			}
		}

		return false;
	}
	
	@Override
	public void update() 
	{
		updateLid();
		updateTileInventory();
	}
	
	@Override
	public boolean receiveClientEvent(int id, int type)
    {
        if (id == 1)
        {
            this.numPlayersUsing = type;
            return true;
        }
        else
        {
            return super.receiveClientEvent(id, type);
        }
    }
	
	public void updateTileInventory()
	{
		boolean send = false;
		if (!world.isRemote && FMLCommonHandler.instance().getMinecraftServerInstance().getTickCounter() % 100 == 0) {

			for (int i = 0; i < 5; i++) {
				if (this.isProcessing(i)) {
					send = true;
					break;
				}
			}
		}
		super.update();

		if (send && !world.isRemote) {
			final BlockPos pos = this.getPos();
			RebornMod.NETWORK_WRAPPER.sendToAllTracking(new TileEntityFieldsMessage(this.getSyncFields(NonNullList.create()), this), new TargetPoint(this.world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 5));
		}
	}
	
	public void updateLid()
	{
		if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + pos.getX() + pos.getY() + pos.getZ()) % 200 == 0)
    	{
        		this.numPlayersUsing = 0;
        		float f = 5.0F;

        		for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((double)((float)pos.getX() - 5.0F), (double)((float)pos.getY() - 5.0F), (double)((float)pos.getZ() - 5.0F), (double)((float)(pos.getX() + 1) + 5.0F), (double)((float)(pos.getY() + 1) + 5.0F), (double)((float)(pos.getZ() + 1) + 5.0F))))
        		{
            		if (entityplayer.openContainer instanceof IncubatorContainer)
            		{
                			if (((IncubatorContainer)entityplayer.openContainer).getIncubatorBlockEntity().equals(this))
                			{
                    			++this.numPlayersUsing;
                			}
            		}
        		}
    	}

    	this.prevLidAngle = this.lidAngle;
    	float f1 = 0.1F;

    	if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F)
    	{
        		double d1 = (double)pos.getX() + 0.5D;
        		double d2 = (double)pos.getZ() + 0.5D;
        		this.world.playSound((EntityPlayer)null, d1, (double)pos.getY() + 0.5D, d2, SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
    	}

    	if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F)
    	{
        		float f2 = this.lidAngle;

        		if (this.numPlayersUsing > 0)
        		{
        			this.lidAngle += 0.1F;
        		}
        		else
        		{
            		this.lidAngle -= 0.1F;
        		}

        		if (this.lidAngle > 1.0F)
        		{
            		this.lidAngle = 1.0F;
        		}

        		if (this.lidAngle < 0.5F && f2 >= 0.5F)
        		{
            		double d3 = (double)pos.getX() + 0.5D;
            		double d0 = (double)pos.getZ() + 0.5D;
            		this.world.playSound((EntityPlayer)null, d3, (double)pos.getY() + 0.5D, d0, SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        		}

        		if (this.lidAngle < 0.0F)
        		{
            		this.lidAngle = 0.0F;
        		}
    	}
	}
	
	@Override
	public void openInventory(EntityPlayer player) 
	{
		++this.numPlayersUsing;
		this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
		this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
	}
	
	@Override
	public void closeInventory(EntityPlayer player) 
	{
		--this.numPlayersUsing;
		this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
		this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
	}
	
	
	@Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                return (T) handlerPull;
        return super.getCapability(capability, facing);
    }

    @Override
    protected boolean shouldResetProgress() {
        return false;
    }

    @Override
    public void setTemperature(int index, int value) {
        this.temperature[index] = value;
    }

    @Override
    public int getTemperature(int index) {
        return this.temperature[index];
    }

    @Override
    public int getTemperatureCount() {
        return this.temperature.length;
    }

	@Override
	public boolean isEmpty() {
		return false;
	}
	
	IItemHandler handlerPull = new IncubatorWrapper(this, this, null) {

		@Override
		@Nonnull
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if (Ints.asList(INPUTS).contains(slot)) {
				ItemStack stackInSlot = inv.getStackInSlot(slot);
				if (stackInSlot != null && stackInSlot.getItem() == ItemHandler.HATCHED_EGG) {
					ItemStack extract = super.extractItem(slot, amount, simulate);
					final BlockPos pos = this.getTile().getPos();
					RebornMod.NETWORK_WRAPPER.sendToAllTracking(new TileEntityFieldsMessage(this.getTile().getSyncFields(NonNullList.create()), this.getTile()), new TargetPoint(this.getTile().world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 5));
					return extract;
				}

			}
			return ItemStack.EMPTY;

		}
	};
	
	@Override
	public void packetDataHandler(ByteBuf fields) 
	{
		if (FMLCommonHandler.instance().getSide().isClient()) 
		{
			for (int slot = 0; slot < 5; slot++) 
			{
				this.setInventorySlotContents(slot, ByteBufUtils.readItemStack(fields));
			}
			for (int field = 0; field < 10; field++) 
			{
				this.setField(field, fields.readInt());
			}
			for (int field = 15; field <= 18; field++) 
			{
				this.setField(field, fields.readInt());
			}
		}
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		boolean send = false;
		if (!this.world.isRemote && index >= 0 && index <= 4 && this.slots.get(index).getItem() != stack.getItem()) {
			send = true;
		}
		super.setInventorySlotContents(index, stack);

		if (send) {
			final BlockPos pos = this.getPos();
			RebornMod.NETWORK_WRAPPER.sendToAllTracking(new TileEntityFieldsMessage(this.getSyncFields(NonNullList.create()), this), new TargetPoint(this.world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 5));
		}
	}
	
	@Override
	public NonNullList getSyncFields(NonNullList fields)
	{
		for (int slot = 0; slot < 5; slot++) 
		{
			fields.add(this.slots.get(slot));
		}
		
		for (int field = 0; field < 10; field++) 
		{
			fields.add(this.getField(field));
		}
		for (int field = 15; field <= 18; field++) 
		{
			fields.add(this.getField(field));
		}
		return fields;
	}
	
	private class IncubatorWrapper extends SidedInvWrapper {
		
		private final IncubatorBlockEntity tile;

		public IncubatorWrapper(IncubatorBlockEntity tile, ISidedInventory inv, EnumFacing side) {
			super(inv, side);
			this.tile = tile;
		}
		
		public IncubatorBlockEntity getTile() {
			return this.tile;
		}
		
	}
    
	private int getProgress(int slot) {
        int j = this.getField(slot);
        int k = this.getField(slot + 5);
        return k != 0 && j != 0 ? j * 14 / k : 0;
    }
}
