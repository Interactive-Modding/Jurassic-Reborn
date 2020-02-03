package mod.reborn.server.entity.ai.animations;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.ai.Mutex;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.ai.EntityAIBase;

public class RoarAnimationAI extends EntityAIBase {
    protected DinosaurEntity dinosaur;

    public RoarAnimationAI(IAnimatedEntity entity) {
        super();
        this.dinosaur = (DinosaurEntity) entity;
        this.setMutexBits(Mutex.ANIMATION);
    }

    @Override
    public boolean shouldExecute() {
        return !this.dinosaur.isBusy() && this.dinosaur.getAgePercentage() > 75 && this.dinosaur.getRNG().nextDouble() < 0.002;
    }

    @Override
    public void startExecuting() {
        this.dinosaur.setAnimation(EntityAnimation.ROARING.get());
        this.dinosaur.playSound(this.dinosaur.getSoundForAnimation(EntityAnimation.ROARING.get()), this.dinosaur.getSoundVolume() > 0.0F ? this.dinosaur.getSoundVolume() + 1.25F : 0.0F, this.dinosaur.getSoundPitch());
    }

    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }
}
