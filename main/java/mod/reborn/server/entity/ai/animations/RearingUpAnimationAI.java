package mod.reborn.server.entity.ai.animations;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.ai.Mutex;
import net.minecraft.entity.ai.EntityAIBase;

public class RearingUpAnimationAI extends EntityAIBase {
    protected DinosaurEntity entity;

    public RearingUpAnimationAI(DinosaurEntity entity) {
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
        this.entity.setAnimation(EntityAnimation.REARING_UP.get());
    }

    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }
}

