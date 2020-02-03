package mod.reborn.server.entity.ai.navigation;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class DinosaurPathNavigateClimber extends DinosaurPathNavigate {
    private BlockPos targetPosition;

    public DinosaurPathNavigateClimber(DinosaurEntity entity, World world) {
        super(entity, world);
    }

    @Override
    public Path getPathToPos(BlockPos pos) {
        this.targetPosition = pos;
        return super.getPathToPos(pos);
    }

    @Override
    public Path getPathToEntityLiving(Entity entity) {
        this.targetPosition = new BlockPos(entity);
        return super.getPathToEntityLiving(entity);
    }

    @Override
    public boolean tryMoveToEntityLiving(Entity entity, double speed) {
        Path path = this.getPathToEntityLiving(entity);
        if (path != null) {
            return this.setPath(path, speed);
        } else {
            this.targetPosition = new BlockPos(entity);
            this.speed = speed;
            return true;
        }
    }

    @Override
    public void onUpdateNavigation() {
        if (this.noPath()) {
            if (this.targetPosition != null) {
                double size = (this.entity.width * this.entity.width);
                if (this.entity.getDistanceSqToCenter(this.targetPosition) >= size && (this.entity.posY <= this.targetPosition.getY() || this.entity.getDistanceSqToCenter(new BlockPos(this.targetPosition.getX(), MathHelper.floor(this.entity.posY), this.targetPosition.getZ())) >= size)) {
                    this.entity.getMoveHelper().setMoveTo(this.targetPosition.getX(), this.targetPosition.getY(), this.targetPosition.getZ(), this.speed);
                } else {
                    this.targetPosition = null;
                }
            }
        } else {
            super.onUpdateNavigation();
        }
    }
}