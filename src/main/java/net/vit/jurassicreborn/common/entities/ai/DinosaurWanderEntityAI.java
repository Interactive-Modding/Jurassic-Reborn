package net.vit.jurassicreborn.common.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.EntityUtils.ai.Herd;

import java.util.EnumSet;

public class DinosaurWanderEntityAI extends Goal {
    protected final DinosaurEntity entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private final double speed;
    protected int executionChance;
    private boolean mustUpdate;
    private final int walkradius;
    private final Herd herd;

    public DinosaurWanderEntityAI(DinosaurEntity creatureIn, double speedIn, int chance, int walkradius) {
        this.entity = creatureIn;
        this.herd = creatureIn.herd;
        this.speed = speedIn;
        this.executionChance = chance;
        this.walkradius = walkradius;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!this.mustUpdate) {
            if (innerShouldStopExecuting()) return false;
        }

        if (this.outerShouldExecute()) {
            // Try up to 100 picks to get a valid, open spot on land
            for (int i = 0; i < 100; i++) {
                Vec3 vec = getWanderPosition();
                if (vec == null) continue;

                // Make sure the 2 blocks above the ground spot are air (headroom).
                // (Using betweenClosed avoids stream allocations.)
                BlockPos base = BlockPos.containing(vec.x, vec.y, vec.z);
                boolean spaceClear = true;
                for (BlockPos pos : BlockPos.betweenClosed(base.above(), base.above(2))) {
                    if (!this.entity.level.getBlockState(pos).isAir()) {
                        spaceClear = false;
                        break;
                    }
                }
                if (!spaceClear) continue;

                this.xPosition = vec.x;
                this.yPosition = vec.y;
                this.zPosition = vec.z;
                this.mustUpdate = false;
                return true;
            }
        }
        return false;
    }

    /**
     * Skip wandering unless the dino is explicitly set to WANDER,
     * and apply the random chance gate.
     */
    protected boolean innerShouldStopExecuting() {
        if (this.entity.getOrder() != DinosaurEntity.Order.WANDER) return true;
        return this.entity.getRandom().nextInt(this.executionChance) != 0;
    }

    protected boolean outerShouldExecute() {
        return this.entity.getNavigation().isDone()
                && this.entity.getAttackTarget() == null
                && !this.entity.isInWater();
    }

    /**
     * Find a nearby land position that isn’t in/over liquid.
     */
    protected Vec3 getWanderPosition() {
        for (int i = 0; i < 20; i++) {
            Vec3 pos = LandRandomPos.getPos(this.entity, this.walkradius + 5, this.walkradius);
            if (pos == null) continue;

            BlockPos bp = BlockPos.containing(pos.x, pos.y, pos.z);
            // Reject positions where either the target block or the block below contains water/lava
            if (this.entity.level.getFluidState(bp).isEmpty()
                    && this.entity.level.getFluidState(bp.below()).isEmpty()
                    && !this.entity.level.getFluidState(bp).is(FluidTags.WATER)
                    && !this.entity.level.getFluidState(bp.below()).is(FluidTags.WATER)
                    && !this.entity.level.getFluidState(bp).is(FluidTags.LAVA)
                    && !this.entity.level.getFluidState(bp.below()).is(FluidTags.LAVA)) {
                return pos;
            }
        }
        // Last-chance attempt
        return LandRandomPos.getPos(this.entity, this.walkradius + 5, this.walkradius);
    }

    @Override
    public boolean canContinueToUse() {
        return this.entity.getOrder() == DinosaurEntity.Order.WANDER
                && !this.entity.getNavigation().isDone()
                && !this.entity.isInWater();
    }

    @Override
    public void start() {
        if (herd != null) {
            for (DinosaurEntity member : herd.members) {
                member.getNavigation().moveTo(
                        this.xPosition + (member.getRandom().nextDouble() * 2.0),
                        this.yPosition,
                        this.zPosition + (member.getRandom().nextDouble() * 2.0),
                        this.speed
                );
            }
        } else {
            this.entity.getNavigation().moveTo(this.xPosition, this.yPosition, this.zPosition, this.speed);
        }
    }

    public void makeUpdate() {
        this.mustUpdate = true;
    }

    public void setExecutionChance(int chance) {
        this.executionChance = chance;
    }
}
