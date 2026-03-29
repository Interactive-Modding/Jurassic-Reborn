package net.vit.jurassicreborn.common.blocks.entities.feeder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

public final class FeederRegistry {
    private static final Map<Level, Set<BlockPos>> ACTIVE =
            Collections.synchronizedMap(new WeakHashMap<>());

    private FeederRegistry() {
    }

    private static Set<BlockPos> getSet(Level level) {
        return ACTIVE.computeIfAbsent(level, l -> ConcurrentHashMap.newKeySet());
    }

    public static void register(Level level, BlockPos pos) {
        if (level == null || level.isClientSide || pos == null) return;
        getSet(level).add(pos.immutable());
    }

    public static void unregister(Level level, BlockPos pos) {
        if (level == null || pos == null) return;
        Set<BlockPos> set = ACTIVE.get(level);
        if (set == null) return;

        set.remove(pos);
        if (set.isEmpty()) {
            ACTIVE.remove(level);
        }
    }

    @Nullable
    public static BlockPos findNearest(Level level, BlockPos origin, DinosaurEntity dino, int horizontalRange, int verticalRange) {
        if (level == null || origin == null || dino == null) return null;

        Set<BlockPos> set = ACTIVE.get(level);
        if (set == null || set.isEmpty()) return null;

        BlockPos best = null;
        int bestDistSq = Integer.MAX_VALUE;

        for (BlockPos pos : set) {
            if (pos == null || !level.hasChunkAt(pos)) continue;

            int dx = pos.getX() - origin.getX();
            int dy = pos.getY() - origin.getY();
            int dz = pos.getZ() - origin.getZ();

            if (Math.abs(dx) > horizontalRange || Math.abs(dz) > horizontalRange || Math.abs(dy) > verticalRange) {
                continue;
            }

            int distSq = dx * dx + dy * dy + dz * dz;
            if (distSq >= bestDistSq) {
                continue;
            }

            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof FeederBlockEntity feeder && feeder.canServe(dino)) {
                best = pos.immutable();
                bestDistSq = distSq;
            }
        }

        return best;
    }
}