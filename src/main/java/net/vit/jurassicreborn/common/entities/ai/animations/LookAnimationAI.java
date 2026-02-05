package net.vit.jurassicreborn.common.entities.ai.animations;

import net.minecraft.world.entity.ai.goal.Goal;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

import java.util.EnumSet;

public class LookAnimationAI extends Goal {

    private final DinosaurEntity dino;

    public LookAnimationAI(DinosaurEntity dino) {
        this.dino = dino;
        // prevents other animation / look goals from running concurrently
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return !dino.isBusy() && dino.getRandom().nextDouble() < 0.003;
    }

    @Override
    public void start() {
        boolean left = dino.getRandom().nextBoolean();
        dino.setAnimation(left ? EntityAnimation.LOOKING_LEFT.get()
                : EntityAnimation.LOOKING_RIGHT.get());
    }
}
