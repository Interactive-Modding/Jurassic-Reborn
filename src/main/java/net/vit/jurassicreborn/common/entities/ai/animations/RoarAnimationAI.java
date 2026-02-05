package net.vit.jurassicreborn.common.entities.ai.animations;

import net.minecraft.world.entity.ai.goal.Goal;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

import java.util.EnumSet;

public class RoarAnimationAI extends Goal {

    private final DinosaurEntity dino;

    public RoarAnimationAI(DinosaurEntity dino) {
        this.dino = dino;
        this.setFlags(EnumSet.of(Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return !dino.isBusy()
                && dino.getAgePercentage() > 75
                && dino.getRandom().nextDouble() < 0.002;
    }

    @Override
    public void start() {
        dino.setAnimation(EntityAnimation.ROARING.get());
        dino.playSound(
                dino.getSoundForAnimation(EntityAnimation.ROARING.get()),
                Math.max(0f, dino.getSoundVolume() + 1.25f),
                dino.getVoicePitch());
    }
}
