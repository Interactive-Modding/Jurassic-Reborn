package mod.reborn.server.entity.ai.animations;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.ai.Mutex;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class CallAnimationAI extends EntityAIBase {
    protected DinosaurEntity dinosaur;

    public CallAnimationAI(IAnimatedEntity entity) {
        super();
        this.dinosaur = (DinosaurEntity) entity;
        this.setMutexBits(Mutex.ANIMATION);
    }

    public List<Entity> getEntitiesWithinDistance(Entity entity, double width, double height) {
        return entity.world.getEntitiesWithinAABBExcludingEntity(entity, new AxisAlignedBB(entity.posX - width, entity.posY - height, entity.posZ - width, entity.posX + width, entity.posY + height, entity.posZ + width));
    }

    @Override
    public boolean shouldExecute() {
        if (!this.dinosaur.isBusy() && this.dinosaur.getRNG().nextInt(350) < 2) {
            List<Entity> entities = this.getEntitiesWithinDistance(this.dinosaur, 50, 10);

            for (Entity entity : entities) {
                if (this.dinosaur.getClass().isInstance(entity)) {
                    this.dinosaur.playSound(this.dinosaur.getSoundForAnimation(EntityAnimation.CALLING.get()), this.dinosaur.getSoundVolume() > 0.0F ? this.dinosaur.getSoundVolume() + 1.25F : 0.0F, this.dinosaur.getSoundPitch());
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void startExecuting() {
        this.dinosaur.setAnimation(EntityAnimation.CALLING.get());
    }

    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }
}
