package net.vit.jurassicreborn.common.entities.ai.animations;

import net.minecraft.world.entity.ai.goal.Goal;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

import java.util.EnumSet;

public class HeadCockAnimationAI extends Goal {

    private final DinosaurEntity dino;

    public HeadCockAnimationAI(DinosaurEntity dino) {
        this.dino = dino;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return !dino.isBusy() && dino.getRandom().nextDouble() < 0.003;
    }

    @Override
    public void start() {
        dino.setAnimation(EntityAnimation.HEAD_COCKING.get());
    }
}
