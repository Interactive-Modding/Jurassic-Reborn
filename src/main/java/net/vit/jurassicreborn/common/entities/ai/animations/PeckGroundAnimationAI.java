package net.vit.jurassicreborn.common.entities.ai.animations;

import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class PeckGroundAnimationAI extends Goal {
    protected DinosaurEntity entity;

    public PeckGroundAnimationAI(DinosaurEntity entity) {
        super();
        this.entity = entity;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        return !this.entity.isBusy() && this.entity.getRandom().nextDouble() < 0.01;
    }

    @Override
    public void start() {
        this.entity.setAnimation(EntityAnimation.PECKING.get());
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }
}