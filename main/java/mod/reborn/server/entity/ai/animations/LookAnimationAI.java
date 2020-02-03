package mod.reborn.server.entity.ai.animations;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.ai.Mutex;
import net.minecraft.entity.ai.EntityAIBase;

public class LookAnimationAI extends EntityAIBase {
    protected DinosaurEntity dinosaur;

    public LookAnimationAI(DinosaurEntity dinosaur) {
        super();
        this.dinosaur = dinosaur;
        this.setMutexBits(Mutex.ANIMATION);
    }

    @Override
    public boolean shouldExecute() {
        return !this.dinosaur.isBusy() && this.dinosaur.getRNG().nextDouble() < 0.003;
    }

    @Override
    public void startExecuting() {
        this.dinosaur.setAnimation(this.dinosaur.getRNG().nextBoolean() ? EntityAnimation.LOOKING_LEFT.get() : EntityAnimation.LOOKING_RIGHT.get());
    }

    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }
}