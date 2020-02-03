package mod.reborn.server.entity.ai.animations;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.ai.Mutex;
import net.minecraft.entity.ai.EntityAIBase;

public class PeckGroundAnimationAI extends EntityAIBase {
    protected DinosaurEntity entity;

    public PeckGroundAnimationAI(DinosaurEntity entity) {
        super();
        this.entity = entity;
        this.setMutexBits(Mutex.ANIMATION);
    }

    @Override
    public boolean shouldExecute() {
        return !this.entity.isBusy() && this.entity.getRNG().nextDouble() < 0.01;
    }

    @Override
    public void startExecuting() {
        this.entity.setAnimation(EntityAnimation.PECKING.get());
    }

    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }
}
