package mod.reborn.client.model.animation;

import net.ilexiconn.llibrary.server.animation.Animation;
import mod.reborn.server.api.Animatable;

import java.util.Map;

public class OnLandAnimationPass extends AnimationPass {
    public OnLandAnimationPass(Map<Animation, float[][]> poseSequences, PosedCuboid[][] poses, boolean useInertialTweens) {
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