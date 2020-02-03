package mod.reborn.server.entity.item;

import io.netty.buffer.ByteBuf;
import mod.reborn.server.item.ItemHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PaddockSignEntity extends EntityHanging implements IEntityAdditionalSpawnData {
    private int dinosaur;

    public PaddockSignEntity(World world) {
        super(world);
    }

    public PaddockSignEntity(World world, BlockPos pos, EnumFacing enumFacing, int dinosaur) {
        super(world, pos);
        this.setType(dinosaur);

        this.updateFacingWithBoundingBox(enumFacing);
    }

    private void setType(int dinosaur) {
        this.dinosaur = dinosaur;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        tagCompound.setInteger("Dinosaur", this.dinosaur);
        super.writeEntityToNBT(tagCompound);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        this.setType(tagCompund.getInteger("Dinosaur"));

        super.readEntityFromNBT(tagCompund);
    }

    @Override
    public int getWidthPixels() {
        return 16;
    }

    @Override
    public int getHeightPixels() {
        return 16;
    }

    @Override
    public void onBroken(Entity entity) {
        if (this.world.getGameRules().getBoolean("doTileDrops")) {
            if (entity instanceof EntityPlayer) {
                EntityPlayer entityplayer = (EntityPlayer) entity;

                if (entityplayer.capabilities.isCreativeMode) {
                    return;
                }
            }

            ItemStack stack = new ItemStack(ItemHandler.PADDOCK_SIGN);
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger("Dinosaur", this.dinosaur);

            this.entityDropItem(stack, 0.0F);
        }
    }

    @Override
    public void playPlaceSound() {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport) {
    }

    @Override
    public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch) {
        BlockPos positionOffset = new BlockPos(x - this.posX, y - this.posY, z - this.posZ);
        BlockPos newPosition = this.hangingPosition.add(positionOffset);
        this.setPosition(newPosition.getX(), newPosition.getY(), newPosition.getZ());
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(this.dinosaur);
        buffer.writeLong(this.hangingPosition.toLong());
        buffer.writeByte(this.facingDirection.getHorizontalIndex());
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        this.setType(buf.readInt());
        this.hangingPosition = BlockPos.fromLong(buf.readLong());
        this.updateFacingWithBoundingBox(EnumFacing.getHorizontal(buf.readByte()));
    }

    public int getDinosaur() {
        return this.dinosaur;
    }
}
