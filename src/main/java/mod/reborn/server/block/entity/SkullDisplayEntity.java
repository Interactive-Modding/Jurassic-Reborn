package mod.reborn.server.block.entity;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

import mod.reborn.client.model.FixedTabulaModel;
import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.EntityHandler;

import com.mojang.authlib.GameProfile;

public class SkullDisplayEntity extends TileEntity {
	
	private short angle = 0;
	private int dinosaur = -1;
    private boolean isFossilized;
    private boolean hasStand;

	public ResourceLocation texture = null;

	public TabulaModel model = null;
	
	public void setModel(int dinosaurID, boolean isFossilized, boolean hasStand) {
        this.dinosaur = dinosaurID;
        this.isFossilized = isFossilized;
        this.hasStand = hasStand;
        this.markDirty();
    }
	
	@SideOnly(Side.CLIENT)
    public short getAngle()
    {
        return this.angle;
    }
	
    public boolean hasStand()
    {
        return this.hasStand;
    }
	
	public Dinosaur getDinosaur() {
		return EntityHandler.getDinosaurById(this.dinosaur);
	}
	
	public boolean hasData() {
		return this.dinosaur != -1 ? true : false;
	}
	
	public boolean isFossilized() {
		return this.isFossilized;
	}
	
	public void setAngle(short angle)
    {
        this.angle = angle;
    }
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound = super.writeToNBT(compound);
        compound.setInteger("dinosaur", this.dinosaur);
        compound.setBoolean("isFossilized", this.isFossilized);
        compound.setShort("angle", (short) this.angle);
        compound.setBoolean("type", this.hasStand);
        return compound;
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
	@SideOnly(Side.CLIENT)
	public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {
		this.readFromNBT(packet.getNbtCompound());
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().grow(2);
	}

	@Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.dinosaur = compound.getInteger("dinosaur");
        this.isFossilized = compound.getBoolean("isFossilized");
        this.angle = compound.getShort("angle");
        this.hasStand = compound.getBoolean("type");
        
    }

}
