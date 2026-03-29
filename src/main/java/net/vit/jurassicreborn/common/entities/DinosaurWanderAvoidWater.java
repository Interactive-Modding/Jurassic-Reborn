package net.vit.jurassicreborn.common.entities;

import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.ai.DinosaurWanderEntityAI;

/**
 * Land wandering that avoids deep water.
 * Extends your already-ported DinosaurWanderEntityAI (speed / interval logic).
 */
public class DinosaurWanderAvoidWater extends DinosaurWanderEntityAI {

    private final int walkRadius;

    public DinosaurWanderAvoidWater(DinosaurEntity dino, double speed, int radius) {
        super(dino, speed, 1, radius);   // (entity, speed, interval, radius)
        this.walkRadius = radius;
    }
    @Override
    public boolean canUse() {
        // outerShouldExecute logic
        return entity.shouldEscapeWaterFast();
    }
    @Override
    public boolean canContinueToUse() {
        // innerShouldStopExecuting logic
        return entity.canDinoSwim() || !entity.isInWater();
    }


    /* ------------------------------------------------------------------ */
    /*  Wander position finder                                             */
    /* ------------------------------------------------------------------ */

    @Override
    protected Vec3 getWanderPosition() {
        Vec3 best = null;

        for (int i = 0; i < 100; i++) {
            Vec3 candidate = DefaultRandomPos.getPos(entity, walkRadius + 5, walkRadius);
            if (candidate == null) continue;

            if (best == null
                    || entity.position().distanceTo(candidate) < entity.position().distanceTo(best)) {
                best = candidate;
            }
        }
        return best == null ? super.getWanderPosition() : best;
    }
}
