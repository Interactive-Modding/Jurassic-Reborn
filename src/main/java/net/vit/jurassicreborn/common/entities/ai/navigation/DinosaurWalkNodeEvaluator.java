package net.vit.jurassicreborn.common.entities.ai.navigation;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.vit.jurassicreborn.common.blocks.entities.fence.ElectricFenceBaseBlock;
import net.vit.jurassicreborn.common.blocks.entities.fence.ElectricFencePoleBlock;
import net.vit.jurassicreborn.common.blocks.entities.fence.ElectricFenceWireBlock;
import net.vit.jurassicreborn.common.blocks.entities.fence.ElectricFenceWireBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.fence.FenceType;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;

import java.util.Objects;
import java.util.function.Supplier;

public class DinosaurWalkNodeEvaluator extends WalkNodeEvaluator {
    private final Supplier<Dinosaur> dinosaurSupplier;
    private Long2ObjectOpenHashMap<BlockPathTypes> cache;

    public DinosaurWalkNodeEvaluator(Supplier<Dinosaur> dinosaurSupplier) {
        this.dinosaurSupplier = Objects.requireNonNull(dinosaurSupplier);
    }

    @Override
    public void prepare(PathNavigationRegion region, Mob mob) {
        super.prepare(region, mob);
        this.cache = new Long2ObjectOpenHashMap<>();
        mob.setPathfindingMalus(BlockPathTypes.DAMAGE_OTHER, 16.0F);
        mob.setPathfindingMalus(BlockPathTypes.DANGER_OTHER, 8.0F);
        mob.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 16.0F);
        mob.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 8.0F);
        Dinosaur d = safeDino();
        if (d != null) {}
    }

    @Override
    public void done() {
        super.done();
        this.cache = null;
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter level, int x, int y, int z) {
        final long key = BlockPos.asLong(x, y, z);
        if (cache != null) {
            BlockPathTypes cached = cache.get(key);
            if (cached != null) return cached;
        }

        // 1) Ask vanilla first (handles headroom, step height, etc.)
        BlockPathTypes type = super.getBlockPathType(level, x, y, z);

        // 2) Our block-level hazard override if not hard-blocked
        final BlockPos pos = new BlockPos(x, y, z);
        final BlockState state = level.getBlockState(pos);
        if (type != BlockPathTypes.BLOCKED) {
            BlockPathTypes self = classifyBlock(level, pos, state);
            if (self != null) type = self;
        }

        // 3) Support/danger inheritance & adjacent danger
        if ((type == BlockPathTypes.WALKABLE || type == BlockPathTypes.OPEN) && y >= 1) {
            BlockPathTypes below = getBelowType(level, x, y - 1, z);

            // Promote OPEN→WALKABLE when the support block is solid-supporting
            if (below != BlockPathTypes.WALKABLE
                    && below != BlockPathTypes.OPEN
                    && below != BlockPathTypes.WATER
                    && below != BlockPathTypes.LAVA) {
                type = BlockPathTypes.WALKABLE;
            }

            if (below == BlockPathTypes.DAMAGE_FIRE)   type = BlockPathTypes.DAMAGE_FIRE;
            if (below == BlockPathTypes.DAMAGE_OTHER) type = BlockPathTypes.DAMAGE_OTHER;
            if (below == BlockPathTypes.WATER)         type = BlockPathTypes.WATER;

            BlockPathTypes adj = checkAdjacentDanger(level, x, y, z);
            if (adj != null) {
                if (adj == BlockPathTypes.DAMAGE_OTHER || adj == BlockPathTypes.DAMAGE_FIRE) {
                    putCache(key, adj);
                    return adj;
                }
                if (type == BlockPathTypes.WALKABLE || type == BlockPathTypes.OPEN) {
                    putCache(key, adj);
                    return adj;
                }
            }
        }

        putCache(key, type);
        return type;
    }

    private BlockPathTypes getBelowType(BlockGetter level, int x, int y, int z) {
        final long key = BlockPos.asLong(x, y, z);
        if (cache != null) {
            BlockPathTypes cached = cache.get(key);
            if (cached != null) return cached;
        }
        BlockPathTypes t = super.getBlockPathType(level, x, y, z);
        putCache(key, t);
        return t;
    }

    private void putCache(long key, BlockPathTypes t) {
        if (cache != null) cache.put(key, t);
    }

    private BlockPathTypes classifyBlock(BlockGetter level, BlockPos pos, BlockState state) {
        FluidState fluid = state.getFluidState();
        if (!fluid.isEmpty()) {
            if (fluid.is(FluidTags.WATER)) return BlockPathTypes.WATER;
            if (fluid.is(FluidTags.LAVA)) return BlockPathTypes.LAVA;
        }
        if (state.getBlock() instanceof BaseRailBlock) return BlockPathTypes.RAIL;

        if (state.getBlock() instanceof DoorBlock) {
            boolean open = state.getValue(DoorBlock.OPEN);
            if (open) return BlockPathTypes.DOOR_OPEN;
            return state.is(BlockTags.WOODEN_DOORS) ? BlockPathTypes.DOOR_WOOD_CLOSED : BlockPathTypes.DOOR_IRON_CLOSED;
        }
        if (state.getBlock() instanceof FenceGateBlock) {
            boolean open = state.getValue(FenceGateBlock.OPEN);
            if (!open) return BlockPathTypes.FENCE;
        }
        if (state.getBlock() instanceof FenceBlock || state.getBlock() instanceof WallBlock) {
            return BlockPathTypes.FENCE;
        }
        if (state.getBlock() instanceof TrapDoorBlock || state.is(Blocks.LILY_PAD)) {
            return BlockPathTypes.TRAPDOOR;
        }
        if (state.is(Blocks.FIRE) || state.is(Blocks.MAGMA_BLOCK)) return BlockPathTypes.DAMAGE_FIRE;
        if (state.is(Blocks.CACTUS)) return BlockPathTypes.DAMAGE_OTHER;

        if (state.getBlock() instanceof ElectricFenceBaseBlock base) {
            return fenceDanger(level, pos, base.getType(), false);
        }
        if (state.getBlock() instanceof ElectricFencePoleBlock pole) {
            return fenceDanger(level, pos, pole.getType(), state.getValue(ElectricFencePoleBlock.ACTIVE));
        }
        if (state.getBlock() instanceof ElectricFenceWireBlock) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ElectricFenceWireBlockEntity wire && wire.isPowered()) {
                return BlockPathTypes.BLOCKED;
            }
            return BlockPathTypes.DANGER_OTHER;
        }
        return null;
    }

    private BlockPathTypes fenceDanger(BlockGetter level, BlockPos pos, FenceType type, boolean powered) {
        return isFencePowered(level, pos, type, powered) ? BlockPathTypes.BLOCKED : BlockPathTypes.DANGER_OTHER;
    }

    private boolean isFencePowered(BlockGetter level, BlockPos pos, FenceType type, boolean powered) {
        if (powered) {
            return true;
        }

        BlockPos.MutableBlockPos cursor = pos.mutable().move(Direction.UP);
        BlockState above = level.getBlockState(cursor);

        if (above.getBlock() instanceof ElectricFencePoleBlock pole) {
            return pole.getType() == type && above.getValue(ElectricFencePoleBlock.ACTIVE);
        }

        while (above.getBlock() instanceof ElectricFenceWireBlock wire) {
            if (wire.getType() != type) {
                return false;
            }
            BlockEntity be = level.getBlockEntity(cursor);
            if (be instanceof ElectricFenceWireBlockEntity wireBe && wireBe.isPowered()) {
                return true;
            }
            cursor.move(Direction.UP);
            above = level.getBlockState(cursor);
        }

        return false;
    }

    private BlockPathTypes checkAdjacentDanger(BlockGetter level, int x, int y, int z) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            int dx = dir.getStepX();
            int dz = dir.getStepZ();

            pos.set(x + dx, y, z + dz);
            BlockPathTypes t = dangerFrom(level, pos);
            if (t != null) return t;

            pos.set(x + dx, y - 1, z + dz);
            t = dangerFrom(level, pos);
            if (t != null) return t;
        }
        return null;
    }

    private BlockPathTypes dangerFrom(BlockGetter level, BlockPos pos) {
        BlockPathTypes t = classifyBlock(level, pos, level.getBlockState(pos));
        if (t == BlockPathTypes.DAMAGE_OTHER
                || t == BlockPathTypes.DANGER_OTHER
                || t == BlockPathTypes.DAMAGE_FIRE
                || t == BlockPathTypes.DANGER_FIRE) {
            return t;
        }
        return null;
    }

    private Dinosaur safeDino() {
        try { return dinosaurSupplier.get(); } catch (Throwable ignored) { return null; }
    }
}