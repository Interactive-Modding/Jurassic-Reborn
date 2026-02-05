package net.vit.jurassicreborn.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class AdvancedSwimEntityAI extends Goal {
    private final DinosaurEntity dino;
    private double x, y, z;

    public AdvancedSwimEntityAI(DinosaurEntity dino) {
        this.dino = dino;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!dino.isInWater() || dino.getRandom().nextFloat() < 0.5F) return false;

        // Try a random nearby position within an 8×3×8 box
        Vec3 candidate = DefaultRandomPos.getPos(dino, 8, 3);
        if (candidate == null) return false;

        BlockPos pos = new BlockPos(candidate.x, candidate.y, candidate.z);
        // Require water at target AND surrounding cells (keeps us submerged and not inside air pockets)
        if (isWater(pos) && isFullySurroundedByWater(pos)) {
            this.x = candidate.x;
            this.y = candidate.y;
            this.z = candidate.z;
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        dino.getNavigation().moveTo(x, y, z, 1.0D);
    }

    @Override
    public boolean canContinueToUse() {
        return dino.isInWater() && !dino.getNavigation().isDone();
    }

    /* ------------------------------- helpers ------------------------------- */

    private boolean isWater(BlockPos p) {
        return dino.level.getFluidState(p).is(FluidTags.WATER);
    }

    /** Check all 6 neighbors (up/down + 4 sides) are water so we don't aim for edges or surface. */
    private boolean isFullySurroundedByWater(BlockPos p) {
        for (Direction dir : Direction.values()) {
            if (!isWater(p.relative(dir))) return false;
        }
        return true;
    }
}
