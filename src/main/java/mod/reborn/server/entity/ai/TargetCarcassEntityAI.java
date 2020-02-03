package mod.reborn.server.entity.ai;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.ai.EntityAIBase;

import java.util.List;

public class TargetCarcassEntityAI extends EntityAIBase {
    private DinosaurEntity entity;
    private DinosaurEntity targetEntity;

    public TargetCarcassEntityAI(DinosaurEntity entity) {
        this.entity = entity;
        this.setMutexBits(Mutex.MOVEMENT | Mutex.METABOLISM);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.entity.resetAttackCooldown();
    }

    @Override
    public void startExecuting() {
        this.entity.setAttackTarget(this.targetEntity);
    }

    @Override
    public boolean shouldExecute() {
        if (!this.entity.getMetabolism().isHungry()) {
            return false;
        }

        if (this.entity.getRNG().nextInt(10) != 0) {
            return false;
        }

        if (!this.entity.isBusy()) {
            List<DinosaurEntity> entities = this.entity.world.getEntitiesWithinAABB(DinosaurEntity.class, this.entity.getEntityBoundingBox().expand(16, 16, 16));

            if (entities.size() > 0) {
                this.targetEntity = null;
                int bestScore = Integer.MAX_VALUE;

                for (DinosaurEntity entity : entities) {
                    if (entity.isCarcass()) {
                        int score = (int) this.entity.getDistanceSq(entity);

                        if (score < bestScore) {
                            bestScore = score;
                            this.targetEntity = entity;
                        }
                    }
                }

                return this.targetEntity != null;
            }
        }

        return false;
    }
}
