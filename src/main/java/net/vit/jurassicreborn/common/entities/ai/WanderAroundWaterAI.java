package net.vit.jurassicreborn.common.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class WanderAroundWaterAI extends Goal {

    protected final Animal entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private final double speed;
    protected int executionChance;
    private boolean mustUpdate;
    private final int walkradius;

    public WanderAroundWaterAI(Animal creatureIn, double speedIn, int chance, int walkradius) {
        this.entity = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.walkradius = walkradius;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.mustUpdate) {
            if (innerShouldStopExecuting()) {
                return false;
            }
        }

        if (this.outerShouldExecute()) {
            overlist:
            for (int i = 0; i < 100; i++) {
                Vec3 vec = getWanderPosition();
                if (vec != null) {
                    // ensure the 1x1x1 space above the target is clear air
                    BlockPos from = new BlockPos(vec.x + 0.0D, vec.y + 1.0D, vec.z + 0.0D);
                    BlockPos to   = new BlockPos(vec.x + 1.0D, vec.y + 1.0D, vec.z + 1.0D);
                    for (BlockPos pos : BlockPos.betweenClosed(from, to)) {
                        if (!this.entity.level.getBlockState(pos).isAir()) {
                            continue overlist;
                        }
                    }

                    this.xPosition = vec.x;
                    this.yPosition = vec.y;
                    this.zPosition = vec.z;
                    this.mustUpdate = false;
                    return true;
                }
            }
        }

        return false;
    }

    // random throttle
    protected boolean innerShouldStopExecuting() {
        return this.entity.getRandom().nextInt(this.executionChance) != 0;
    }

    // only when idle, alive, and with air to breathe (not drowning)
    protected boolean outerShouldExecute() {
        return this.entity.getNavigation().isDone()
                && this.entity.getTarget() == null
                && this.entity.level.isDay()
                && this.entity.getAirSupply() > 0;
    }

    protected Vec3 getWanderPosition() {
        // bias radius: horizontal = walkradius, vertical = walkradius
        return DefaultRandomPos.getPos(this.entity, this.walkradius, this.walkradius);
    }

    @Override
    public boolean canContinueToUse() {
        return !this.entity.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.entity.getNavigation().moveTo(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }

    public void makeUpdate() {
        this.mustUpdate = true;
    }

    public void setExecutionChance(int chance) {
        this.executionChance = chance;
    }
}
