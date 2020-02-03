package mod.reborn.server.entity.ai;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.ai.EntityAIBase;

public class MateEntityAI extends EntityAIBase {
    protected DinosaurEntity dinosaur;
    protected DinosaurEntity mate;

    public MateEntityAI(DinosaurEntity dinosaur) {
        this.dinosaur = dinosaur;
        this.setMutexBits(Mutex.ATTACK | Mutex.METABOLISM);
    }

    @Override
    public boolean shouldExecute() {
        // int minEnergy = 12000;
        //
        // if (dinosaur.getEnergy() > minEnergy)
        // {
        // World world = dinosaur.world;
        //
        // double posX = dinosaur.posX;
        // double posY = dinosaur.posY;
        // double posZ = dinosaur.posZ;
        //
        // List<DinosaurEntity> entities = world.getEntitiesWithinAABB(dinosaur.getClass(), AxisAlignedBB.fromBounds(posX - 16, posY - 16, posZ - 16, posX + 16, posY + 16, posZ + 16));
        //
        // for (DinosaurEntity entity : entities)
        // {
        // if (entity.isMale() != dinosaur.isMale() && entity.getEnergy() > minEnergy)
        // {
        // dinosaur.getNavigator().tryMoveToEntityLiving(entity, 1.0D);
        // mate = entity;
        //
        // return true;
        // }
        // }
        // }

        return false;
    }

    @Override
    public void updateTask() {
        if (this.dinosaur.getEntityBoundingBox().intersects(this.mate.getEntityBoundingBox().expand(0.5D, 0.5D, 0.5D))) {
            this.dinosaur.setAnimation(EntityAnimation.MATING.get());

            this.dinosaur.getMetabolism().decreaseEnergy(1000);
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean shouldContinueExecuting() {
        return this.dinosaur != null && !this.dinosaur.getNavigator().noPath() && this.mate != null && !this.mate.isDead;
    }
}
