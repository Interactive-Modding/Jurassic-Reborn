package mod.reborn.server.block.entity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AncientItemHoldingBlockEntity extends TileEntity {

	/**
	 * The item that should be displayed on the block
	 */
	private ItemStack displayItem;
	/**
	 * The rotation of the displayed item on the Y-Axis
	 */
	private int displayItemRotation = 0;
	/**
	 * The X-Axis offset of the displayed item(offset from the center of the block)
	 */
	private float displayItemXOffset;
	/**
	 * The Y-Axis offset of the displayed item(offset from the center of the block)
	 */
	private float displayItemYOffset;
	/**
	 * The Z-Axis offset of the displayed item(offset from the center of the block)
	 */
	private float displayItemZOffset;

	public AncientItemHoldingBlockEntity() {

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("itemRotation", this.displayItemRotation);
		if (this.displayItem != null) {
			NBTTagCompound itemCompound = new NBTTagCompound();
			itemCompound = this.displayItem.serializeNBT();
			compound.setTag("item", itemCompound);
		}
		compound.setFloat("itemXOffset", this.displayItemXOffset);
		compound.setFloat("itemYOffset", this.displayItemYOffset);
		compound.setFloat("itemZOffset", this.displayItemZOffset);
		return super.writeToNBT(compound);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.displayItemRotation = compound.getInteger("itemRotation");
		if (compound.hasKey("item")) {
			this.displayItem = new ItemStack((NBTTagCompound) compound.getTag("item"));
		}
		this.displayItemXOffset = compound.getFloat("itemXOffset");
		this.displayItemYOffset = compound.getFloat("itemYOffset");
		this.displayItemZOffset = compound.getFloat("itemZOffset");
		super.readFromNBT(compound);
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

	public int getItemRotation() {
		return this.displayItemRotation;
	}

	public void setItemRotation(int rotation) {
		this.displayItemRotation = rotation;
	}

	public ItemStack getDisplayItemStack() {
		return displayItem;
	}

	public void setDisplayItemStack(ItemStack display) {
		this.displayItem = display;
	}

	public float getDisplayItemXOffset() {
		return displayItemXOffset;
	}

	public void setDisplayItemXOffset(float displayItemXOffset) {
		this.displayItemXOffset = displayItemXOffset;
	}

	public float getDisplayItemYOffset() {
		return displayItemYOffset;
	}

	public void setDisplayItemYOffset(float displayItemYOffset) {
		this.displayItemYOffset = displayItemYOffset;
	}

	public float getDisplayItemZOffset() {
		return displayItemZOffset;
	}

	public void setDisplayItemZOffset(float displayItemZOffset) {
		this.displayItemZOffset = displayItemZOffset;
	}

}
