package net.vit.jurassicreborn.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.vit.jurassicreborn.common.blocks.entities.fence.ElectricFenceBaseBlock;
import net.vit.jurassicreborn.common.blocks.entities.fence.ElectricFencePoleBlock;
import net.vit.jurassicreborn.common.blocks.entities.fence.ElectricFenceWireBlock;
import net.vit.jurassicreborn.common.entities.ai.util.AIUtils;

import java.util.EnumSet;

public class EscapeWireEntityAI extends Goal {

    private static final int START_RADIUS        = 4;
    private static final int MAX_RADIUS          = 24;
    private static final int RADIUS_STEP         = 4;
    private static final int SAFE_CLEARANCE      = 2;
    private static final int RECALC_TICKS        = 20;
    private static final int STUCK_CHECK_TICKS   = 20;
    private static final double MIN_PROGRESS_SQ  = 0.4;
    private static final int HARD_TIMEOUT_TICKS  = 200;

    private final DinosaurEntity dino;

    private BlockPos target;
    private int       nextRecalc;
    private int       startedAtTick;
    private double    lastX, lastZ;
    private int       nextStuckCheck;

    public EscapeWireEntityAI(DinosaurEntity dino) {
        this.dino = dino;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return !dino.level().isClientSide && dino.wireTicks > 0 && !dino.isPassenger() && dino.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        if (dino.level().isClientSide) return false;
        if (dino.tickCount - startedAtTick > HARD_TIMEOUT_TICKS) return false;
        return target != null && !dino.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.startedAtTick   = dino.tickCount;
        this.nextRecalc      = 0;
        this.nextStuckCheck  = dino.tickCount + STUCK_CHECK_TICKS;
        this.lastX           = dino.getX();
        this.lastZ           = dino.getZ();

        dino.getNavigation().stop();
        this.target = findSafeSpot();

        if (this.target != null) {
            dino.getNavigation().moveTo(target.getX() + 0.5, target.getY(), target.getZ() + 0.5, 1.4);
        }
    }

    @Override
    public void stop() {
        target = null;
    }

    @Override
    public void tick() {
        if (target == null) {
            target = findSafeSpot();
            if (target == null) return;
            dino.getNavigation().moveTo(target.getX() + 0.5, target.getY(), target.getZ() + 0.5, 1.4);
        }

        if (--nextRecalc <= 0) {
            nextRecalc = RECALC_TICKS;
            dino.getNavigation().moveTo(target.getX() + 0.5, target.getY(), target.getZ() + 0.5, 1.4);
        }

        if (dino.tickCount >= nextStuckCheck) {
            nextStuckCheck = dino.tickCount + STUCK_CHECK_TICKS;

            double dx = dino.getX() - lastX;
            double dz = dino.getZ() - lastZ;
            if (dx * dx + dz * dz < MIN_PROGRESS_SQ) {
                BlockPos newer = findSafeSpot();
                if (newer != null && !newer.equals(target)) {
                    target = newer;
                    dino.getNavigation().moveTo(target.getX() + 0.5, target.getY(), target.getZ() + 0.5, 1.4);
                }
            }
            lastX = dino.getX();
            lastZ = dino.getZ();
        }
    }

    /* ----------------------------- helpers ----------------------------- */

    private BlockPos findSafeSpot() {
        final Level level = dino.level();
        final RandomSource rng  = dino.getRandom();
        BlockPos origin   = dino.blockPosition();

        for (int radius = START_RADIUS; radius <= MAX_RADIUS; radius += RADIUS_STEP) {
            for (int i = 0; i < 20; i++) {
                int offX = rng.nextInt(radius * 2 + 1) - radius;
                int offZ = rng.nextInt(radius * 2 + 1) - radius;

                BlockPos base = origin.offset(offX, 0, offZ);

                // vanilla API: hasChunksAt(min, max)  not AABB
                BlockPos min = base.offset(-2, -2, -2);
                BlockPos max = base.offset( 2,  2,  2);
                if (!level.hasChunksAt(min, max)) continue;

                BlockPos surface = AIUtils.findSurface(level, base);
                if (!isGoodFooting(level, surface)) continue;
                if (hasFenceNearby(level, surface, SAFE_CLEARANCE)) continue;

                return surface;
            }
        }
        return null;
    }

    private static boolean isGoodFooting(Level level, BlockPos surface) {
        if (!level.isLoaded(surface)) return false;
        if (!level.isEmptyBlock(surface)) return false;
        BlockPos feet = surface.below();
        if (!level.isLoaded(feet)) return false;
        if (!level.getFluidState(feet).isEmpty()) return false;
        return !level.getBlockState(feet).getCollisionShape(level, feet).isEmpty();
    }

    private static boolean hasFenceNearby(Level level, BlockPos center, int r) {
        for (int dx = -r; dx <= r; dx++) {
            for (int dy = -r; dy <= r; dy++) {
                for (int dz = -r; dz <= r; dz++) {
                    BlockPos p = center.offset(dx, dy, dz);
                    if (!level.isLoaded(p)) continue;
                    if (isFenceBlock(level.getBlockState(p))) return true;
                }
            }
        }
        // a small extra vertical check for overhead wires
        for (int up = 1; up <= r + 2; up++) {
            BlockPos p = center.above(up);
            if (!level.isLoaded(p)) break;
            if (isFenceBlock(level.getBlockState(p))) return true;
        }
        return false;
    }

    private static boolean isFenceBlock(BlockState state) {
        return state.getBlock() instanceof ElectricFenceWireBlock
                || state.getBlock() instanceof ElectricFenceBaseBlock
                || state.getBlock() instanceof ElectricFencePoleBlock;
    }
}
