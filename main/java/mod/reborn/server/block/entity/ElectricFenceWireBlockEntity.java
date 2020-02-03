package mod.reborn.server.block.entity;

import mod.reborn.server.block.fence.ElectricFencePoleBlock;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ElectricFenceWireBlockEntity extends TileEntity implements ITickable {
    private Set<BlockPos> poweringPoles = new HashSet<>();
    private byte ticks;

    @Override
    public void update() {
        if (++this.ticks >= 20) {
            this.removeInvalidPoles();
            this.ticks = 0;
        }
    }

    private void removeInvalidPoles() {
        List<BlockPos> invalid = this.getInvalidPoles();
        for (BlockPos remove : invalid) {
            this.power(remove, false);
        }
    }

    private List<BlockPos> getInvalidPoles() {
        List<BlockPos> invalid = new ArrayList<>(this.poweringPoles.size());
        for (BlockPos pole : this.poweringPoles) {
            IBlockState state = this.world.getBlockState(pole);
            boolean isInvalid = true;
            if (state.getBlock() instanceof ElectricFencePoleBlock && this.world.isBlockPowered(pole.down())) {
                isInvalid = false;
            }
            if (isInvalid) {
                invalid.add(pole);
            }
        }
        return invalid;
    }

    public void checkDisconnect() {
        this.removeInvalidPoles();
        for (BlockPos pole : this.poweringPoles) {
            IBlockState state = this.world.getBlockState(pole);
            Block block = state.getBlock();
            if (block instanceof ElectricFencePoleBlock) {
                ((ElectricFencePoleBlock) block).updateConnectedWires(this.world, pole);
            }
        }
    }

    public void power(BlockPos pole, boolean powered) {
        if (powered) {
            if (!this.poweringPoles.contains(pole)) {
                this.poweringPoles.add(pole);
            }
        } else {
            this.poweringPoles.remove(pole);
        }
    }

    public boolean isPowered() {
        return this.poweringPoles.size() > 0;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        NBTTagList poweringList = new NBTTagList();
        for (BlockPos pole : this.poweringPoles) {
            poweringList.appendTag(new NBTTagLong(pole.toLong()));
        }
        compound.setTag("Powering", poweringList);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.poweringPoles.clear();
        NBTTagList poweringList = compound.getTagList("Powering", Constants.NBT.TAG_LONG);
        for (int i = 0; i < poweringList.tagCount(); i++) {
            this.poweringPoles.add(BlockPos.fromLong(((NBTTagLong) poweringList.get(i)).getLong()));
        }
    }
}
