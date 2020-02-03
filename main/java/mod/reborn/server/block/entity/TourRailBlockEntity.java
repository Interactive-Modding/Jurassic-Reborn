package mod.reborn.server.block.entity;

import mod.reborn.server.block.TourRailBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TourRailBlockEntity extends TileEntity {
    private TourRailBlock.EnumRailDirection direction;

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        int metadata = getBlockMetadata();
        return new SPacketUpdateTileEntity(this.pos, metadata, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return nbt;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        this.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        checkNonNull();
        compound.setInteger("RailDirection", direction.ordinal());
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        direction = TourRailBlock.EnumRailDirection.values()[compound.getInteger("RailDirection")];
    }

    public TourRailBlock.EnumRailDirection getDirection() {
        checkNonNull();
        return direction;
    }

    private void checkNonNull() {
        if(direction == null) {
            direction = TourRailBlock.EnumRailDirection.NORTH_SOUTH;
        }
    }

    public void setDirection(TourRailBlock.EnumRailDirection direction) {
        this.direction = direction;
        checkNonNull();
    }
}
