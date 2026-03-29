package net.vit.jurassicreborn.common.blocks.entities.fence;

import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ElectricFenceWireBlockEntity extends BlockEntity implements BlockEntityTicker {
    private Set<BlockPos> poweringPoles = new HashSet<>();
    private byte ticks;

    public ElectricFenceWireBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ModBlockEntities.WIRE_FENCE_BLOCK_ENTITY.get(), pWorldPosition, pBlockState);
    }

    public void tick() {
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

    @Override
    public void onLoad() {
        if (this.level != null && !this.level.isClientSide) {
            this.removeInvalidPoles();

            BlockState correct = ((ElectricFenceWireBlock) getBlockState().getBlock())
                    .rebuildConnections(this.level, this.worldPosition, this.getBlockState());
            if (correct != this.getBlockState()) {
                this.level.setBlock(this.worldPosition, correct, 3);
            }
        }
    }

    private List<BlockPos> getInvalidPoles() {
        List<BlockPos> invalid = new ArrayList<>(this.poweringPoles.size());
        for (BlockPos pole : this.poweringPoles) {
            BlockState state = this.level.getBlockState(pole);
            boolean isInvalid = true;
            if (state.getBlock() instanceof ElectricFencePoleBlock poleBlock &&
                    ElectricFencePoleBlock.hasPoweredBase(this.level, pole, poleBlock.getType())) {
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
            BlockState state = this.level.getBlockState(pole);
            Block block = state.getBlock();
            if (block instanceof ElectricFencePoleBlock) {
                ((ElectricFencePoleBlock) block).updateConnectedWires(this.level, pole);
            }
        }
    }

    public void power(BlockPos pole, boolean powered) {
        boolean changed = false;

        if (powered) {
            if (!this.poweringPoles.contains(pole)) {
                this.poweringPoles.add(pole);
                changed = true;
            }
        } else {
            if (this.poweringPoles.remove(pole)) {
                changed = true;
            }
        }

        if (changed) {
            this.setChanged();
            // Immediately notify clients of power state change
            if (this.level != null && !this.level.isClientSide) {
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
            }
        }
    }

    public boolean isPowered() {
        return this.poweringPoles.size() > 0;
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);
        ListTag poweringList = new ListTag();
        for (BlockPos pole : this.poweringPoles) {
            poweringList.add(LongTag.valueOf(pole.asLong()));
        }
        compound.put("Powering", poweringList);
    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.loadAdditional(compound, provider);
        this.poweringPoles.clear();
        ListTag poweringList = compound.getList("Powering", CompoundTag.TAG_LONG);
        for (int i = 0; i < poweringList.size(); i++) {
            this.poweringPoles.add(BlockPos.of(((LongTag) poweringList.get(i)).getAsLong()));
        }
    }

    @Override
    public void tick(Level pLevel, BlockPos pPos, BlockState pState, BlockEntity pBlockEntity) {
        ((ElectricFenceWireBlockEntity)pBlockEntity).tick(pLevel, pPos, pState, pBlockEntity);
    }
}
