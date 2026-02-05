package net.vit.jurassicreborn.client.render.entity.animation;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.common.entities.EntityUtils.Animatable;

import java.util.Map;

public class OnLandAnimationPass extends AnimationPass {

    public OnLandAnimationPass(Map<Animation, float[][]> poseSequences,
                               PosedCuboid[][] poses,
                               boolean useInertialTweens) {
        super(poseSequences, poses, useInertialTweens);
    }

    @Override
    protected boolean isEntityAnimationDependent() {
        return false;
    }

    @Override
    protected Animation getRequestedAnimation(Animatable entity) {
        if (!entity.isCarcass() && !entity.inWater()) {
            return EntityAnimation.ON_LAND.get();
        }
        return EntityAnimation.IDLE.get();
    }

    @Override
    public boolean isLooping() {
        return true;
    }
}
