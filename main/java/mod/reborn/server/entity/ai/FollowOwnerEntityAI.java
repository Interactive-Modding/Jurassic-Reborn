package mod.reborn.server.entity.ai;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNodeType;

import java.util.UUID;

public class FollowOwnerEntityAI extends EntityAIBase {
    private DinosaurEntity entity;
    private int recalculateTime;
    private float oldWaterCost;
    private EntityPlayer owner;

    public FollowOwnerEntityAI(DinosaurEntity entity) {
        this.entity = entity;
        this.setMutexBits(Mutex.MOVEMENT);
    }

    @Override
    public boolean shouldExecute() {
        if (this.entity.getNavigator().noPath() && this.entity.getOrder() == DinosaurEntity.Order.FOLLOW) {
            UUID ownerId = this.entity.getOwner();
            if (ownerId != null) {
                EntityPlayer owner = this.entity.world.getPlayerEntityByUUID(ownerId);
                return owner != null && this.isOwnerFar(owner) && this.entity.getAttackTarget() == null;
            }
        }

        return false;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !(this.entity.getEntityBoundingBox() != null && this.owner.getEntityBoundingBox() != null && this.entity.getEntityBoundingBox().expand(2, 2, 2).intersects(this.owner.getEntityBoundingBox())) && !this.entity.getNavigator().noPath() && this.owner.isEntityAlive() && this.entity.getOrder() == DinosaurEntity.Order.FOLLOW;
    }

    @Override
    public void startExecuting() {
        this.recalculateTime = 0;
        this.oldWaterCost = this.entity.getPathPriority(PathNodeType.WATER);
        this.entity.setPathPriority(PathNodeType.WATER, 0.0F);
        this.owner = this.entity.world.getPlayerEntityByUUID(this.entity.getOwner());
    }

    @Override
    public void resetTask() {
        this.owner = null;
        this.entity.getNavigator().clearPath();
        this.entity.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }

    @Override
    public void updateTask() {
        this.entity.getLookHelper().setLookPositionWithEntity(this.owner, 10.0F, (float) this.entity.getVerticalFaceSpeed());

        if (!this.entity.isMovementBlocked()) {
            if (this.recalculateTime-- <= 0) {
                this.recalculateTime = 10;

                if (this.isOwnerFar(this.owner)) {
                    this.entity.getNavigator().tryMoveToEntityLiving(this.owner, 0.8F);
                }
            }
        }
    }

    private boolean isOwnerFar(EntityPlayer owner) {
        return this.entity.getDistanceSq(owner) > this.entity.width * 40;
    }
}
