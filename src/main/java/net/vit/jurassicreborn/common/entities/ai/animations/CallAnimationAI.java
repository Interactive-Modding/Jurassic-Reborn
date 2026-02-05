package net.vit.jurassicreborn.common.entities.ai.animations;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

import java.util.EnumSet;
import java.util.List;

public class CallAnimationAI extends Goal {

    private final DinosaurEntity dino;

    public CallAnimationAI(DinosaurEntity dino) {
        this.dino = dino;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (dino.isBusy() || dino.getRandom().nextInt(350) >= 2) return false;

        AABB box = dino.getBoundingBox().inflate(50.0D, 10.0D, 50.0D);
        List<Entity> list = dino.level.getEntities(dino, box);
        return list.stream().anyMatch(e -> e.getClass() == dino.getClass());
    }


    @Override
    public void start() {
        dino.playSound(dino.getSoundForAnimation(EntityAnimation.CALLING.get()),
                Math.max(0f, dino.getSoundVolume() + 1.25f),
                dino.getVoicePitch());

        dino.setAnimation(EntityAnimation.CALLING.get());
    }
}
