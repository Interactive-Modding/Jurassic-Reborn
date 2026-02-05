package net.vit.jurassicreborn.common.entities.ai.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Assorted helpers used by the dinosaur AI goals. */
public final class AIUtils {

    private static final Logger LOGGER = LogManager.getLogger();

    /* ------------------------------------------------------------------ */
    /*  Geometry helpers                                                   */
    /* ------------------------------------------------------------------ */

    /** Returns the point on the circle of {@code radius} around {@code center}
     *  that lies on the straight line from {@code start → center}. If
     *  {@code start} is already inside the circle, returns {@code start}. */
    public static BlockPos findIntersect(BlockPos center, int radius, BlockPos start) {
        int dx = start.getX() - center.getX();
        int dz = start.getZ() - center.getZ();
        double dist = Math.sqrt(dx * (double) dx + dz * (double) dz);

        if (dist < radius) return start;

        double scale = radius / dist;
        return new BlockPos(
                center.getX() + Mth.floor(dx * scale + 0.5),
                center.getY(),
                center.getZ() + Mth.floor(dz * scale + 0.5)
        );
    }

    /** Step {@code move} blocks from {@code current} toward {@code target}. */
    public static BlockPos computePosToward(BlockPos current, BlockPos target, int move) {
        int dx = target.getX() - current.getX();
        int dz = target.getZ() - current.getZ();
        double dist = Math.sqrt(dx * (double) dx + dz * (double) dz);

        if (dist < move) return target;

        double scale = move / dist;
        return new BlockPos(
                current.getX() + Mth.floor(dx * scale + 0.5),
                current.getY(),
                current.getZ() + Mth.floor(dz * scale + 0.5)
        );
    }

    /* ------------------------------------------------------------------ */
    /*  Surface / water helpers                                            */
    /* ------------------------------------------------------------------ */

    /** Starting *inside* a fluid, walk upward until we hit air (or non-loaded). */
    public static BlockPos findSurface(Level level, BlockPos pos) {
        if (!level.isLoaded(pos)) return pos;
        BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos().set(pos);
        // go up while still inside any fluid
        while (level.getFluidState(m).isSource() || !level.getFluidState(m).isEmpty()) {
            m.move(0, 1, 0);
            if (!level.isLoaded(m)) break;
        }
        return m.immutable();
    }

    /** Convenience version for an entity already in water. */
    public static BlockPos findSurface(LivingEntity entity) {
        if (!entity.isInWater()) return null;
        Level level = entity.level;
        BlockPos start = entity.blockPosition()
                .below(Mth.floor(entity.getBbHeight() * 0.5F));
        return findSurface(level, start);
    }

    /** Counts how many blocks of *source* water are above and below the entity
     *  until air/solid is reached. */
    public static int getWaterDepth(LivingEntity entity) {
        return getWaterDepth(entity, false);
    }

    /** Same as above, but if {@code fromEntity} the count starts at the
     *  entity’s Y level (legacy behaviour). */
    public static int getWaterDepth(LivingEntity entity, boolean fromEntity) {
        if (!entity.isInWater()) return 0;

        Level level = entity.level;
        BlockPos origin = entity.blockPosition();
        int depth = 0;

        // Upward pass
        BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos().set(origin);
        while (level.isLoaded(m) && isSourceWater(level, m)) {
            m.move(0, 1, 0);
            depth++;
        }

        // Downward pass
        m.set(origin).move(0, -1, 0);
        if (fromEntity) depth = 0;
        while (level.isLoaded(m) && isSourceWater(level, m)) {
            m.move(0, -1, 0);
            depth++;
        }

        return depth + 1; // match legacy +1 logic
    }

    /** Approximate bottom of the water column the entity is in. */
    public static BlockPos getBottom(LivingEntity entity) {
        // Using the double-ctor here is fine on 1.19.x; it floors.
        return BlockPos.containing(
                entity.getX(),
                entity.getY() - getWaterDepth(entity, true),
                entity.getZ()
        );
    }

    /* ------------------------------------------------------------------ */
    /*  Shore finding                                                      */
    /* ------------------------------------------------------------------ */

    /**
     * Expands outward from {@code center} up to radius 32 and returns a
     * standable ground BlockPos on the nearest shore (non-water column).
     * <p>
     * The returned position is the top motion-blocking block at that XZ,
     * i.e., a valid path target for {@code Navigation.moveTo(x+0.5, y, z+0.5,...)}.
     */
    public static BlockPos findShore(Level level, BlockPos center) {
        final int MAX_R = 32;

        for (int radius = 1; radius <= MAX_R; radius++) {
            for (BlockPos ringPos : new RingXZTraverser(center, radius)) {
                if (!level.isLoaded(ringPos)) continue;

                // If this column is NOT water, treat it as potential shore.
                if (!isWaterColumn(level, ringPos)) {
                    // find top solid / motion-blocking y
                    int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                            ringPos.getX(), ringPos.getZ());
                    BlockPos ground = new BlockPos(ringPos.getX(), y - 1, ringPos.getZ());

                    // Small sanity check: ground not water
                    if (!isWaterColumn(level, ground)) {
                        return ground;
                    }
                }
            }
        }
        return null;
    }

    private static boolean isWaterColumn(Level level, BlockPos pos) {
        FluidState fs = level.getFluidState(pos);
        return fs.getType() == Fluids.WATER && !fs.isEmpty();
    }

    /* ------------------------------------------------------------------ */
    /*  Fluid helpers                                                      */
    /* ------------------------------------------------------------------ */

    private static boolean isSourceWater(Level level, BlockPos pos) {
        FluidState state = level.getFluidState(pos);
        return state.getType() == Fluids.WATER && state.isSource();
    }

    /* ------------------------------------------------------------------ */
    /*  Debug – draw a circle in log output                                */
    /* ------------------------------------------------------------------ */

    // Bresenham circle algorithm for quick visualisation in logs
    public static void plotCircle(int x0, int y0, int radius) {
        int x = radius;
        int y = 0;
        int decision = 1 - x;

        while (y <= x) {
            logPixel(x0 +  x, y0 +  y);
            logPixel(x0 +  y, y0 +  x);
            logPixel(x0 + -x, y0 +  y);
            logPixel(x0 + -y, y0 +  x);
            logPixel(x0 + -x, y0 + -y);
            logPixel(x0 + -y, y0 + -x);
            logPixel(x0 +  x, y0 + -y);
            logPixel(x0 +  y, y0 + -x);

            y++;
            if (decision <= 0) {
                decision += 2 * y + 1;
            } else {
                x--;
                decision += 2 * (y - x) + 1;
            }
        }
    }

    private static void logPixel(int x, int y) {
        LOGGER.info("x={}  y={}", x, y);
    }

    private AIUtils() {} // utility class; no instantiation
}
