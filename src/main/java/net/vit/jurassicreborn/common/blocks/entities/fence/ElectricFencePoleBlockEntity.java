package net.vit.jurassicreborn.common.blocks.entities.fence;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;

import java.util.HashSet;
import java.util.Set;

public class ElectricFencePoleBlockEntity extends BlockEntity {
    private final Set<BlockPos> cachedBases = new HashSet<>();
    private final Set<BlockPos> cachedWires = new HashSet<>();
    private boolean networkDirty = true;

    public ElectricFencePoleBlockEntity(BlockPos pPos, BlockState pState) {
        super(ModBlockEntities.POLE_FENCE_BLOCK_ENTITY.get(), pPos, pState);
    }

    public void markNetworkDirty() {
        networkDirty = true;
    }

    public Iterable<BlockPos> getOrRebuildNetwork(Level level, BlockPos polePos, FenceType type) {
        if (networkDirty) {
            cachedBases.clear();
            cachedWires.clear();
            ElectricFencePoleBlock.collectNetwork(level, polePos, type, cachedBases, cachedWires);
            networkDirty = false;
        }
        return cachedWires;
    }

    @Override
    public void onLoad() {
        if (level == null || level.isClientSide) return;

        BlockState state = level.getBlockState(worldPosition);
        if (!(state.getBlock() instanceof ElectricFencePoleBlock pole)) {
            return;
        }

        // 1) recompute powered flag (redstone signal at the supporting base)
        boolean powered = ElectricFencePoleBlock.hasPoweredBase(level, worldPosition, pole.getType());
        if (powered != state.getValue(ElectricFencePoleBlock.ACTIVE)) {
            level.setBlock(worldPosition,
                    state.setValue(ElectricFencePoleBlock.ACTIVE, powered),
                    Block.UPDATE_CLIENTS);
        }

        // 2) notify every connected wire of the refreshed state
        pole.updateConnectedWires(level, worldPosition, powered);
    }
}
