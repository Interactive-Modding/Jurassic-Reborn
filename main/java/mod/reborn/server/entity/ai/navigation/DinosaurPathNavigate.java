package mod.reborn.server.entity.ai.navigation;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DinosaurPathNavigate extends PathNavigateGround {
    private DinosaurEntity dinosaur;

    public DinosaurPathNavigate(DinosaurEntity entity, World world) {
        super(entity, world);
        this.dinosaur = entity;
    }

    @Override
    protected PathFinder getPathFinder() {
        this.nodeProcessor = new DinosaurWalkNodeProcessor(((DinosaurEntity) this.entity).getDinosaur());
        this.nodeProcessor.setCanEnterDoors(true);
        return new PathFinder(this.nodeProcessor);
    }

    @Override
    protected void pathFollow() {
        Vec3d position = this.getEntityPosition();
        int length = this.currentPath.getCurrentPathLength();

        for (int i = this.currentPath.getCurrentPathIndex(); i < this.currentPath.getCurrentPathLength(); ++i) {
            if (this.currentPath.getPathPointFromIndex(i).y != Math.floor(position.y)) {
                length = i;
                break;
            }
        }

        Vec3d target = this.currentPath.getCurrentPos();

        float deltaX = MathHelper.abs((float) (this.entity.posX - (target.x + 0.5)));
        float deltaZ = MathHelper.abs((float) (this.entity.posZ - (target.z + 0.5)));
        float deltaY = MathHelper.abs((float) (this.entity.posY - target.y));

        int width = MathHelper.ceil(this.entity.width);
        int height = MathHelper.ceil(this.entity.height);

        float maxDistance = this.entity.width > 0.75F ? width : 0.75F - this.entity.width / 2.0F;

        if (deltaX < maxDistance && deltaZ < maxDistance && deltaY < 1.0) {
            this.currentPath.incrementPathIndex();
        }

        for (int i = length - 1; i >= this.currentPath.getCurrentPathIndex(); --i) {
            if (this.isDirectPathBetweenPoints(position, this.currentPath.getVectorFromIndex(this.entity, i), width, height, width)) {
                this.currentPath.setCurrentPathIndex(i);
                break;
            }
        }

        this.checkForStuck(position);
    }

    @Override
    protected boolean canNavigate() {
        return !this.dinosaur.isMovementBlocked() && super.canNavigate();
    }
}