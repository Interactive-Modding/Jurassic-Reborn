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
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
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
    private Long2ObjectOpenHashMap<PathType> cache;

    public DinosaurWalkNodeEvaluator(Supplier<Dinosaur> dinosaurSupplier) {
        this.dinosaurSupplier = Objects.requireNonNull(dinosaurSupplier);
    }

    @Override
    public void prepare(PathNavigationRegion region, Mob mob) {
        super.prepare(region, mob);
        this.cache = new Long2ObjectOpenHashMap<>();
        mob.setPathfindingMalus(PathType.DAMAGE_OTHER, 16.0F);
        mob.setPathfindingMalus(PathType.DANGER_OTHER, 8.0F);
        mob.setPathfindingMalus(PathType.DAMAGE_FIRE, 16.0F);
        mob.setPathfindingMalus(PathType.DANGER_FIRE, 8.0F);
        Dinosaur d = safeDino();
        if (d != null) {}
    }

    @Override
    public void done() {
        super.done();
        this.cache = null;
    }

    @Override
    public PathType getPathType(PathfindingContext context, int x, int y, int z) {
        BlockGetter level = context.level();
        final long key = BlockPos.asLong(x, y, z);
        if (cache != null) {
            PathType cached = cache.get(key);
            if (cached != null) return cached;
        }

        // 1) Ask vanilla first (handles headroom, step height, etc.)
        PathType type = super.getPathType(context, x, y, z);

        // 2) Our block-level hazard override if not hard-blocked
        final BlockPos pos = new BlockPos(x, y, z);
        final BlockState state = level.getBlockState(pos);
        if (type != PathType.BLOCKED) {
            PathType self = classifyBlock(level, pos, state);
            if (self != null) type = self;
        }

        // 3) Support/danger inheritance & adjacent danger
        if ((type == PathType.WALKABLE || type == PathType.OPEN) && y >= 1) {
            PathType below = getBelowType(context, x, y - 1, z);

            // Promote OPEN→WALKABLE when the support block is solid-supporting
            if (below != PathType.WALKABLE
                    && below != PathType.OPEN
                    && below != PathType.WATER
                    && below != PathType.LAVA) {
                type = PathType.WALKABLE;
            }

            if (below == PathType.DAMAGE_FIRE)   type = PathType.DAMAGE_FIRE;
            if (below == PathType.DAMAGE_OTHER) type = PathType.DAMAGE_OTHER;
            if (below == PathType.WATER)         type = PathType.WATER;

            PathType adj = checkAdjacentDanger(level, x, y, z);
            if (adj != null) {
                if (adj == PathType.DAMAGE_OTHER || adj == PathType.DAMAGE_FIRE) {
                    putCache(key, adj);
                    return adj;
                }
                if (type == PathType.WALKABLE || type == PathType.OPEN) {
                    putCache(key, adj);
                    return adj;
                }
            }
        }

        putCache(key, type);
        return type;
    }

    private PathType getBelowType(PathfindingContext context, int x, int y, int z) {
        final long key = BlockPos.asLong(x, y, z);
        if (cache != null) {
            PathType cached = cache.get(key);
            if (cached != null) return cached;
        }
        PathType t = super.getPathType(context, x, y, z);
        putCache(key, t);
        return t;
    }

    private void putCache(long key, PathType t) {
        if (cache != null) cache.put(key, t);
    }

    private PathType classifyBlock(BlockGetter level, BlockPos pos, BlockState state) {
        FluidState fluid = state.getFluidState();
        if (!fluid.isEmpty()) {
            if (fluid.is(FluidTags.WATER)) return PathType.WATER;
            if (fluid.is(FluidTags.LAVA)) return PathType.LAVA;
        }
        if (state.getBlock() instanceof BaseRailBlock) return PathType.RAIL;

        if (state.getBlock() instanceof DoorBlock) {
            boolean open = state.getValue(DoorBlock.OPEN);
            if (open) return PathType.DOOR_OPEN;
            return state.is(BlockTags.WOODEN_DOORS) ? PathType.DOOR_WOOD_CLOSED : PathType.DOOR_IRON_CLOSED;
        }
        if (state.getBlock() instanceof FenceGateBlock) {
            boolean open = state.getValue(FenceGateBlock.OPEN);
            if (!open) return PathType.FENCE;
        }
        if (state.getBlock() instanceof FenceBlock || state.getBlock() instanceof WallBlock) {
            return PathType.FENCE;
        }
        if (state.getBlock() instanceof TrapDoorBlock || state.is(Blocks.LILY_PAD)) {
            return PathType.TRAPDOOR;
        }
        if (state.is(Blocks.FIRE) || state.is(Blocks.MAGMA_BLOCK)) return PathType.DAMAGE_FIRE;
        if (state.is(Blocks.CACTUS)) return PathType.DAMAGE_OTHER;

        if (state.getBlock() instanceof ElectricFenceBaseBlock base) {
            return fenceDanger(level, pos, base.getType(), false);
        }
        if (state.getBlock() instanceof ElectricFencePoleBlock pole) {
            return fenceDanger(level, pos, pole.getType(), state.getValue(ElectricFencePoleBlock.ACTIVE));
        }
        if (state.getBlock() instanceof ElectricFenceWireBlock) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ElectricFenceWireBlockEntity wire && wire.isPowered()) {
                return PathType.BLOCKED;
            }
            return PathType.DANGER_OTHER;
        }
        return null;
    }

    private PathType fenceDanger(BlockGetter level, BlockPos pos, FenceType type, boolean powered) {
        return isFencePowered(level, pos, type, powered) ? PathType.BLOCKED : PathType.DANGER_OTHER;
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

    private PathType checkAdjacentDanger(BlockGetter level, int x, int y, int z) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            int dx = dir.getStepX();
            int dz = dir.getStepZ();

            pos.set(x + dx, y, z + dz);
            PathType t = dangerFrom(level, pos);
            if (t != null) return t;

            pos.set(x + dx, y - 1, z + dz);
            t = dangerFrom(level, pos);
            if (t != null) return t;
        }
        return null;
    }

    private PathType dangerFrom(BlockGetter level, BlockPos pos) {
        PathType t = classifyBlock(level, pos, level.getBlockState(pos));
        if (t == PathType.DAMAGE_OTHER
                || t == PathType.DANGER_OTHER
                || t == PathType.DAMAGE_FIRE
                || t == PathType.DANGER_FIRE) {
            return t;
        }
        return null;
    }

    private Dinosaur safeDino() {
        try { return dinosaurSupplier.get(); } catch (Throwable ignored) { return null; }
    }
}
