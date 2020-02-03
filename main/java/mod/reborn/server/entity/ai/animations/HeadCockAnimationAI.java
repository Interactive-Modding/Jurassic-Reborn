package mod.reborn.server.entity.ai.animations;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.ai.Mutex;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.ai.EntityAIBase;

public class HeadCockAnimationAI extends EntityAIBase {
    protected DinosaurEntity dinosaur;

    public HeadCockAnimationAI(IAnimatedEntity entity) {
        super();
        this.dinosaur = (DinosaurEntity) entity;
        this.setMutexBits(Mutex.ANIMATION);
    }

    @Override
    public boolean shouldExecute() {
        return !this.dinosaur.isBusy() && this.dinosaur.getRNG().nextDouble() < 0.003;
    }

    @Override
    public void startExecuting() {
        this.dinosaur.setAnimation(EntityAnimation.HEAD_COCKING.get());
    }

    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }
}
