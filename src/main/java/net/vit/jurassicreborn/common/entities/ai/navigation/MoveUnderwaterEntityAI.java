package net.vit.jurassicreborn.common.entities.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.common.entities.SwimmingDinosaurEntity;

import java.util.EnumSet;

public class MoveUnderwaterEntityAI extends Goal {
    private final SwimmingDinosaurEntity mob;
    private double x, y, z;

    // tuning knobs
    private final int horiz = 8;
    private final int tries = 16;
    private final int minDepthFromSurface = 3;  // stay at least this far below surface
    private final int minClearAboveFloor = 2;   // stay at least this far above bottom

    public MoveUnderwaterEntityAI(SwimmingDinosaurEntity entity) {
        this.mob = entity;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!mob.isInWater() || mob.isCarcass()) return false;
        // lower idle chance so we don't drift upward between targets
        if (mob.getRandom().nextFloat() < 0.15F) return false;

        Vec3 target = pickUnderwaterTarget(mob.level(), mob.blockPosition(), mob.getRandom());
        if (target == null) return false;

        this.x = target.x;
        this.y = target.y;
        this.z = target.z;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return mob.isInWater() && !mob.getNavigation().isDone();
    }

    @Override
    public void start() {
        // If we spawned resting on the bottom, nudge up so navigation engages
        if (mob.isInWater() && isBottomedOut(mob)) {
            mob.setDeltaMovement(mob.getDeltaMovement().add(0.0, 0.08, 0.0));
        }
        mob.getNavigation().moveTo(x, y, z, 1.0D);
    }

    /** Pick a water target inside a depth band (not too close to surface or floor). */
    private Vec3 pickUnderwaterTarget(Level level, BlockPos origin, RandomSource rnd) {
        if (level == null) return null;
        final int minYWorld = level.getMinBuildHeight() + 1;
        final int maxYWorld = level.getMaxBuildHeight() - 2;

        for (int i = 0; i < tries; i++) {
            int dx = rnd.nextInt(horiz * 2 + 1) - horiz;
            int dz = rnd.nextInt(horiz * 2 + 1) - horiz;

            BlockPos col = origin.offset(dx, 0, dz);
            if (!level.hasChunkAt(col)) continue;

            int surfaceY = findSurfaceY(level, col);
            if (surfaceY == Integer.MIN_VALUE) continue; // no water column here

            int floorY = findFloorY(level, col, surfaceY);
            if (floorY == Integer.MAX_VALUE) continue; // weird column, skip

            int minY = Math.max(floorY + minClearAboveFloor, minYWorld);
            int maxY = Math.min(surfaceY - minDepthFromSurface, maxYWorld);
            if (minY >= maxY) continue; // too shallow, skip

            int y = minY + rnd.nextInt(maxY - minY + 1);
            BlockPos p = new BlockPos(col.getX(), y, col.getZ());

            // require at least 2 blocks of water clearance
            if (isWater(level, p) && isWater(level, p.above())) {
                return Vec3.atCenterOf(p);
            }
        }
        return null;
    }

    /** Y of first non-water block *above* the column; MIN_VALUE if no water found. */
    private int findSurfaceY(Level level, BlockPos col) {
        if (!level.hasChunkAt(col)) return Integer.MIN_VALUE;

        BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos(col.getX(), col.getY(), col.getZ());
        int y = col.getY();

        // find any water in/under this column
        while (y >= level.getMinBuildHeight() && (!level.hasChunkAt(m.setY(y)) || !isWater(level, m))) y--;
        if (y < level.getMinBuildHeight()) return Integer.MIN_VALUE;

        // climb to top of water column
        while (y + 1 < level.getMaxBuildHeight() && level.hasChunkAt(m.setY(y + 1)) && isWater(level, m)) y++;
        return y + 1; // first non-water above the water column
    }

    /** Y of first solid/non-empty collision block under the water column; MAX_VALUE if none found. */
    private int findFloorY(Level level, BlockPos col, int surfaceY) {
        BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos(col.getX(), surfaceY - 1, col.getZ());
        for (int y = surfaceY - 1; y >= level.getMinBuildHeight(); y--) {
            if (!level.hasChunkAt(m.setY(y))) continue;
            if (!isWater(level, m)) {
                BlockState s = level.getBlockState(m);
                // treat any non-empty collision shape as solid floor
                if (!s.getCollisionShape(level, m).isEmpty()) return y;
            }
        }
        return Integer.MAX_VALUE;
    }

    private boolean isWater(Level level, BlockPos pos) {
        return level.getFluidState(pos).is(FluidTags.WATER);
    }

    /** True if feet are in water and the block below is solid (sitting on the bottom). */
    private boolean isBottomedOut(SwimmingDinosaurEntity e) {
        Level lvl = e.level();
        BlockPos feet = e.blockPosition();
        if (!lvl.hasChunkAt(feet) || !isWater(lvl, feet)) return false;
        BlockPos below = feet.below();
        return lvl.hasChunkAt(below)
                && !lvl.getBlockState(below).getCollisionShape(lvl, below).isEmpty()
                && e.getDeltaMovement().y <= 0.0;
    }
}
