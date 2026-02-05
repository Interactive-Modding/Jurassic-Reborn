package net.vit.jurassicreborn.common.entities.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

import javax.annotation.Nullable;

public class PathNavigateClimber extends DinosaurPathNavigate {
    @Nullable
    private BlockPos targetPosition;

    public PathNavigateClimber(DinosaurEntity entity, Level level) {
        super(entity, level);
    }

    @Override
    public Path createPath(BlockPos pos, int distance) {
        this.targetPosition = pos;
        return super.createPath(pos, distance);
    }

    @Override
    public Path createPath(Entity target, int distance) {
        this.targetPosition = target.blockPosition();
        return super.createPath(target, distance);
    }

    @Override
    public boolean moveTo(Entity target, double speed) {
        if (!(target instanceof LivingEntity living)) return false;
        Path path = this.createPath(living, 0);
        if (path != null) {
            return this.moveTo(path, speed);
        } else {
            this.targetPosition = living.blockPosition();
            this.speedModifier = speed; // protected field from PathNavigation
            return true;
        }
    }

    @Override
    public void tick() {
        if (this.isDone()) {
            if (this.targetPosition != null) {
                double size = this.mob.getBbWidth() * this.mob.getBbWidth();
                double dxz = this.mob.distanceToSqr(this.targetPosition.getX() + 0.5D, this.mob.getY(), this.targetPosition.getZ() + 0.5D);
                double d3  = this.mob.distanceToSqr(this.targetPosition.getX() + 0.5D, this.targetPosition.getY(), this.targetPosition.getZ() + 0.5D);

                if (d3 >= size && (this.mob.getY() <= this.targetPosition.getY() || dxz >= size)) {
                    this.mob.getMoveControl().setWantedPosition(
                            this.targetPosition.getX() + 0.5D,
                            this.targetPosition.getY(),
                            this.targetPosition.getZ() + 0.5D,
                            this.speedModifier
                    );
                } else {
                    this.targetPosition = null;
                }
            }
        } else {
            super.tick();
        }
    }
}
