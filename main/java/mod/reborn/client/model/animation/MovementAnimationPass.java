package mod.reborn.client.model.animation;

import net.ilexiconn.llibrary.server.animation.Animation;
import mod.reborn.server.api.Animatable;

import java.util.Map;

public class MovementAnimationPass extends AnimationPass {
    public MovementAnimationPass(Map<Animation, float[][]> poseSequences, PosedCuboid[][] poses, boolean useInertialTweens) {
        super(poseSequences, poses, useInertialTweens);
    }

    @Override
    protected boolean isEntityAnimationDependent() {
        return false;
    }

    @Override
    protected float getAnimationSpeed(Animatable entity) {
        return this.isMoving(entity) ? this.getAnimationDegree(entity) : 3.0F;
    }

    @Override
    protected float getAnimationDegree(Animatable entity) {
        float degree;

        if (this.animation == EntityAnimation.WALKING.get() || this.animation == EntityAnimation.RUNNING.get() || this.animation == EntityAnimation.SWIMMING.get() || this.animation == EntityAnimation.CLIMBING.get()) {
            if (entity.inWater() || entity.inLava()) {
                degree = this.limbSwingAmount * 4.0F;
            } else {
                degree = this.limbSwingAmount;
            }
        } else {
            return super.getAnimationDegree(entity);
        }

        return Math.max(this.isMoving(entity) ? 0.5F : 0.0F, Math.min(1.0F, degree));
    }

    @Override
    protected Animation getRequestedAnimation(Animatable entity) {
        if (entity.isCarcass()) {
            return EntityAnimation.IDLE.get();
        } else {
            if (entity.isClimbing()) {
                return EntityAnimation.CLIMBING.get();
            } else if (this.isMoving(entity)) {
                if (entity.isSwimming()) {
                    return this.animations.containsKey(EntityAnimation.SWIMMING.get()) ? EntityAnimation.SWIMMING.get() : EntityAnimation.WALKING.get();
                } else {
                    if (entity.isRunning()) {
                        return EntityAnimation.RUNNING.get();
                    } else {
                        return EntityAnimation.WALKING.get();
                    }
                }
            } else {
                return EntityAnimation.IDLE.get();
            }
        }
    }

    private boolean isMoving(Animatable entity) {
        return entity.isMoving();
    }

    @Override
    public boolean isLooping() {
        return true;
    }
}