package mod.reborn.server.block.entity;

import com.google.common.primitives.Ints;
import io.netty.buffer.ByteBuf;
import mod.reborn.RebornMod;
import mod.reborn.client.particle.WashingParticle;
import mod.reborn.client.proxy.ClientProxy;
import mod.reborn.server.api.CleanableItem;
import mod.reborn.server.block.OrientedBlock;
import mod.reborn.server.container.CleaningStationContainer;
import mod.reborn.server.message.TileEntityFieldsMessage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
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
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CleaningStationBlockEntity extends TileEntityLockable implements ITickable, ISidedInventory, ISyncable {
	private static final int[] SLOTS_TOP = new int[] { 0 };
	private static final int[] SLOTS_BOTTOM = new int[] { 7, 6, 5, 4, 3, 2 };
	private static final int[] SLOTS_SIDES = new int[] { 1 }; // 0 = cleaning 1 = fuel 2 = output
	private NonNullList<ItemStack> slots = NonNullList.withSize(8, ItemStack.EMPTY);
	private int cleaningStationWaterTime;

	private int currentItemWaterTime;

	private int cleanTime;
	private int totalCleanTime;

	private ItemStack cleanedItemResult = ItemStack.EMPTY;

	private String customName;
    private boolean isCleaning = false;
	private boolean prevIsCleaning = false;
	public float cleaningRotation = 0;
	public float rotationAmount = 0;
	private float prevCleaningRotation;

	@SideOnly(Side.CLIENT)
	public static boolean isCleaning(IInventory inventory) {
		return inventory.getField(0) > 0;
	}

	public static boolean isItemFuel(ItemStack stack) {
		return !stack.isEmpty() && stack.getItem() == Items.WATER_BUCKET;
	}

	@Override
	public int getSizeInventory() {
		return this.slots.size();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.slots.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack itemstack = ItemStackHelper.getAndSplit(this.slots, index, count);

		if (!itemstack.isEmpty()) {
			this.markDirty();
		}

		return itemstack;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack itemstack = this.slots.get(index);

		if (itemstack.isEmpty()) {
			return ItemStack.EMPTY;
		} else {
			this.slots.set(index, ItemStack.EMPTY);
			return itemstack;
		}
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		boolean send = false;
		if(!this.world.isRemote && index == 0 && ItemStack.EMPTY == stack)
			send = true;
		
		boolean flag = !stack.isEmpty() && stack.isItemEqual(this.slots.get(index)) && ItemStack.areItemStackTagsEqual(stack, this.slots.get(index));
		this.slots.set(index, stack);

		if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

		if (index == 0 && !flag) {
			this.totalCleanTime = this.getStackWashTime(stack);
			this.cleanTime = 0;
			this.markDirty();
		}
		if(send)
			RebornMod.NETWORK_WRAPPER.sendToAllTracking(new TileEntityFieldsMessage(getSyncFields(NonNullList.create()), this), new TargetPoint(this.world.provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 5));
	}

	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : "container.cleaning_station";
	}

	@Override
	public boolean hasCustomName() {
		return this.customName != null && this.customName.length() > 0;
	}

	public void setCustomInventoryName(String customName) {
		this.customName = customName;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		NBTTagList itemList = compound.getTagList("Items", 10);

		for (int i = 0; i < itemList.tagCount(); ++i) {
			NBTTagCompound item = itemList.getCompoundTagAt(i);
			ItemStack stack = new ItemStack(item);

			byte slot = item.getByte("Slot");

			if (slot >= 0 && slot < this.slots.size()) {
				this.slots.set(slot, stack);
			}
		}

		this.cleaningStationWaterTime = compound.getShort("WaterTime");
		this.cleanTime = compound.getShort("CleanTime");
		this.prevIsCleaning = this.cleanTime > 0;
		this.totalCleanTime = compound.getShort("CleanTimeTotal");
		this.currentItemWaterTime = this.getItemCleanTime(this.slots.get(1));

		if (compound.hasKey("CustomName", 8)) {
			this.customName = compound.getString("CustomName");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setShort("WaterTime", (short) this.cleaningStationWaterTime);
		compound.setShort("CleanTime", (short) this.cleanTime);
		compound.setShort("CleanTimeTotal", (short) this.totalCleanTime);
		NBTTagList itemList = new NBTTagList();

		for (int slot = 0; slot < this.slots.size(); ++slot) {
			if (!this.slots.get(slot).isEmpty()) {
				NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setByte("Slot", (byte) slot);

				this.slots.get(slot).writeToNBT(itemTag);
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
	public int getInventoryStackLimit() {
		return 64;
	}

	public boolean hasWater() {
		return this.cleaningStationWaterTime > 0;
	}

	public boolean isCleaning() {
		return this.cleanTime > 0;
	}

	@Override
	public void update() {
		boolean isCleaning = this.hasWater();
		boolean dirty = false;

		if (!world.isRemote) {

			if (this.hasWater() && this.canClean()) {
				--this.cleaningStationWaterTime;
				dirty = true;
			}

			if (!this.hasWater() && (this.slots.get(1).isEmpty() || this.slots.get(0).isEmpty())) {
				if (!this.hasWater() && this.cleanTime > 0) {
					this.cleanTime = MathHelper.clamp(this.cleanTime - 2, 0, this.totalCleanTime);
					dirty = true;
				}
			} else {
				if (!this.hasWater() && this.canClean() && isItemFuel(this.slots.get(1))) {
					this.currentItemWaterTime = this.cleaningStationWaterTime = this.getItemCleanTime(this.slots.get(1));

					if (this.hasWater()) {
						dirty = true;

						if (!this.slots.get(1).isEmpty()) {
							if (this.slots.get(1).getCount() == 1) {
								this.slots.set(1, this.slots.get(1).getItem().getContainerItem(this.slots.get(1)));
							} else {
								this.slots.get(1).shrink(1);
							}
						}
					}
				}

				if (this.hasWater() && this.canClean() && this.cleaningStationWaterTime > 0) {
					++this.cleanTime;

					if (this.cleanTime == this.totalCleanTime) {
						this.cleanTime = 0;
						this.totalCleanTime = this.getStackWashTime(this.slots.get(0));
						this.cleanItem();
						dirty = true;
					}
				} else {
					this.cleanTime = 0;
					dirty = true;
				}
			}

			if (isCleaning != this.hasWater()) {
				dirty = true;
			}

			if (this.cleaningStationWaterTime == 0) {
				this.cleanTime = 0;
			}
			
			this.isCleaning = this.isCleaning();

			if (this.isCleaning != prevIsCleaning) {
				prevIsCleaning = this.isCleaning;
				RebornMod.NETWORK_WRAPPER.sendToAllTracking(new TileEntityFieldsMessage(getSyncFields(NonNullList.create()), this), new TargetPoint(this.world.provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 5));
			}
		}else {
			updateRotation();
		}

		if (this.world.isRemote && this.isCleaning) {
			this.spawnClientWaterParticles();
		}

		if (dirty && !world.isRemote) {
			this.markDirty();
		}
	}

	@SideOnly(Side.CLIENT)
	private void spawnClientWaterParticles() {
		IBlockState state = this.world.getBlockState(this.getPos());
		EnumFacing value = state.getValue(OrientedBlock.FACING);
		int rot = value.getHorizontalIndex();
		double firstAchsis = 0.5;
		double secondAchsis = 0.34;
		double secondAchsis2 = 1.0 - secondAchsis;
		double velX = 0.0;
		double velZ = 0.07;
		boolean isInvertedRotated = (rot == 1 || rot == 3);
		if (!isInvertedRotated) {
			double tmp = firstAchsis;
			firstAchsis = secondAchsis;
			secondAchsis = tmp;
			velX = velZ;
			velZ = 0.0;
		}

		for (int i = 0; i < 3; i++) {
			
			final Minecraft mc = ClientProxy.MC;
			
			mc.effectRenderer.addEffect(new WashingParticle(this.world, this.pos.getX() + firstAchsis, this.pos.getY() + 0.64, this.pos.getZ() + secondAchsis, velX, 0.0, velZ, rot));
			mc.effectRenderer.addEffect(new WashingParticle(this.world, this.pos.getX() + ((isInvertedRotated) ? firstAchsis : secondAchsis2), this.pos.getY() + 0.64, this.pos.getZ() + ((isInvertedRotated) ? secondAchsis2 : secondAchsis), -velX, 0.0, -velZ, rot));
			mc.effectRenderer.addEffect(new WashingParticle(this.world, this.pos.getX() + firstAchsis, this.pos.getY() + 0.6, this.pos.getZ() + secondAchsis, velX, -0.07, velZ, rot));
			mc.effectRenderer.addEffect(new WashingParticle(this.world, this.pos.getX() + ((isInvertedRotated) ? firstAchsis : secondAchsis2), this.pos.getY() + 0.6, this.pos.getZ() + ((isInvertedRotated) ? secondAchsis2 : secondAchsis), -velX, -0.07, -velZ, rot));
		}
	}

	private int getItemCleanTime(ItemStack stack) {
		return 1600;
	}

	public int getStackWashTime(ItemStack stack) {
		return 200;
	}

	private boolean canClean() {
		CleanableItem cleanableItem = CleanableItem.getCleanableItem(this.slots.get(0));
		if (cleanableItem != null && cleanableItem.isCleanable(this.slots.get(0))) {
			if (this.cleanedItemResult.isEmpty()) {
				this.cleanedItemResult = cleanableItem.getCleanedItem(this.slots.get(0), this.world.rand);
			}

			for (int i = 2; i < 8; i++) {
				if (isStackable(this.slots.get(i), this.cleanedItemResult)) {
					return true;
				}
			}
		}

		return false;
	}

	public void cleanItem() {
		if (this.canClean()) {
			for (int i = 2; i < 8; i++) {
				ItemStack slot = this.slots.get(i);

				if (isStackable(slot, this.cleanedItemResult)) {
					if (slot.isEmpty()) {
						this.slots.set(i, this.cleanedItemResult.copy());
					} else {
						slot.grow(this.cleanedItemResult.getCount());
					}

					ItemStack shrinked = this.slots.get(0).copy();
					shrinked.shrink(1);
					this.setInventorySlotContents(0, shrinked);
					this.cleanedItemResult = ItemStack.EMPTY;

					if (this.slots.get(0).getCount() <= 0) {
						this.setInventorySlotContents(0, ItemStack.EMPTY);
					}

					this.world.markChunkDirty(this.pos, this);

					break;
				}
			}
		}
	}

	private static boolean isStackable(ItemStack slotStack, ItemStack insertingStack) {
		return slotStack.isEmpty() || (ItemStack.areItemsEqual(slotStack, insertingStack) && ItemStack.areItemStackTagsEqual(slotStack, insertingStack) && slotStack.getItemDamage() == insertingStack.getItemDamage() && slotStack.getMaxStackSize() - slotStack.getCount() >= insertingStack.getCount());
	}

	@Override
	public NonNullList getSyncFields(NonNullList fields) {
		fields.add(this.isCleaning);
		fields.add(this.slots.get(0));
		return fields;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return ArrayUtils.addAll(SLOTS_TOP, ArrayUtils.addAll(SLOTS_SIDES, SLOTS_BOTTOM));
	}

	@Override
	public boolean canInsertItem(int index, ItemStack stack, EnumFacing direction) {
		return this.isItemValidForSlot(index, stack);
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {
		if (Ints.asList(SLOTS_TOP).contains(slotID)) {
			if (itemstack != null && CleanableItem.getCleanableItem(itemstack) != null && CleanableItem.getCleanableItem(itemstack).isCleanable(itemstack)) {
				return true;
			}
		} else if (Ints.asList(SLOTS_SIDES).contains(slotID)) {

			if (itemstack != null && CleaningStationBlockEntity.isItemFuel(itemstack)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return true;
	}

	@Override
	@Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (facing != null && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) handlerPull;
		return super.getCapability(capability, facing);
	}

	IItemHandler handlerPull = new SidedInvWrapper(this, null) {

		@Override
		@Nonnull
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if (slot == 1 && inv.getStackInSlot(slot) != null && inv.getStackInSlot(slot).getItem() == Items.BUCKET) {
				return super.extractItem(slot, amount, simulate);

			} else if (Ints.asList(SLOTS_BOTTOM).contains(slot)) {
				return super.extractItem(slot, amount, simulate);

			}
			return ItemStack.EMPTY;

		}
	};

	@Override
	public String getGuiID() {
		return RebornMod.MODID + ":cleaning_station";
	}

	@Override
	public Container createContainer(InventoryPlayer inventory, EntityPlayer playerIn) {
		return new CleaningStationContainer(inventory, this);
	}

	@Override
	public int getField(int id) {
		switch (id) {
		case 0:
			return this.cleaningStationWaterTime;
		case 1:
			return this.currentItemWaterTime;
		case 2:
			return this.cleanTime;
		case 3:
			return this.totalCleanTime;
		default:
			return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		switch (id) {
		case 0:
			this.cleaningStationWaterTime = value;
			break;
		case 1:
			this.currentItemWaterTime = value;
			break;
		case 2:
			this.cleanTime = value;
			break;
		case 3:
			this.totalCleanTime = value;
		}
	}

	@Override
	public int getFieldCount() {
		return 4;
	}

	@Override
	public void clear() {
		this.slots.clear();
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {
		this.readFromNBT(packet.getNbtCompound());
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return this.world.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public void packetDataHandler(ByteBuf fields) {
		if (FMLCommonHandler.instance().getSide().isClient()) {
			this.isCleaning = fields.readBoolean();
			this.setInventorySlotContents(0, ByteBufUtils.readItemStack(fields));
		}
	}
	
	@SideOnly(Side.CLIENT)
	public void updateRotation() {
		this.prevCleaningRotation = this.cleaningRotation;
		if(!ClientProxy.MC.isGamePaused()) {

			if(this.isCleaning) {
				this.rotationAmount += 0.008f;
				this.rotationAmount *= 1.01f;
			}else {
				this.rotationAmount -= 0.01f;
				this.rotationAmount *= 0.95f;
			}
			
			if (this.rotationAmount < 0f) {
				this.rotationAmount = 0f;
			}
			if (this.rotationAmount > 1.2f) {
				this.rotationAmount = 1.2f;
			}
			this.cleaningRotation += this.rotationAmount;
			
		}
	}

	@SideOnly(Side.CLIENT)
	public float getRenderCleaningRotation(float particialTicks) {
		return this.prevCleaningRotation + (this.cleaningRotation - this.prevCleaningRotation) * particialTicks;
	}

}
