package net.vit.jurassicreborn.client.render.entity.animation;

import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.EntityUtils.Animatable;

import java.util.Map;

/**
 * Animation pass helper – Citadel 1.19.2 version.
 *
 *   resetToDefaultPose();
 *   animationPass.performAnimations(entity, limbSwing, limbSwingAmount, ageInTicks);
 */
@OnlyIn(Dist.CLIENT)
public class AnimationPass {

    /* ------------------ CONSTANT POSE/ANIMATION DATA ------------------ */

    protected final Map<Animation, float[][]> animations;
    protected final PosedCuboid[][]           poses;

    /* ------------------ PER-ENTITY, PER-PASS STATE -------------------- */

    protected float[][] rotationIncrements;
    protected float[][] positionIncrements;
    protected float[][] prevRotationIncrements;
    protected float[][] prevPositionIncrements;

    protected int   poseCount;
    protected int   poseIndex;
    protected float poseLength;

    protected float animationTick;
    protected float prevTicks;

    protected AdvancedModelBox[] parts;          // Citadel part class
    protected PosedCuboid[]      pose;

    protected Animation animation;

    protected final boolean useInertia;
    protected float inertiaFactor;

    protected float limbSwing;
    protected float limbSwingAmount;

    /* ------------------------------------------------------------------ */

    public AnimationPass(Map<Animation, float[][]> animations,
                         PosedCuboid[][] poses, boolean useInertia) {
        this.animations  = animations;
        this.poses       = poses;
        this.useInertia  = useInertia;
    }

    /* ========================= INITIALISERS ========================== */

    public void init(AdvancedModelBox[] parts, Animatable entity) {
        this.parts = parts;

        int len = parts.length;
        this.prevRotationIncrements = new float[len][3];
        this.prevPositionIncrements = new float[len][3];
        this.rotationIncrements     = new float[len][3];
        this.positionIncrements     = new float[len][3];

        this.animation = this.getRequestedAnimation(entity);
        this.initPoseModel();
        this.initAnimation(entity, this.animation);
        this.initAnimationTicks(entity);

        this.prevTicks = 0.0F;
        this.initIncrements(entity);
        this.performAnimations(entity, 0.0F, 0.0F, 0.0F);
    }

    public void initPoseModel() {
        float[][] pose = this.animations.get(this.animation);
        if (pose != null) {
            this.poseCount = pose.length;
            this.poseIndex = 0;
            this.pose      = this.poses[(int) pose[this.poseIndex][0]];
        }
    }

    protected void initIncrements(Animatable entity) {
        float animationDegree = this.getAnimationDegree(entity);

        for (int i = 0; i < Math.min(this.pose.length, this.parts.length); i++) {
            AdvancedModelBox part = this.parts[i];
            PosedCuboid      next = this.pose[i];

            float[] rotInc = this.rotationIncrements[i];
            float[] posInc = this.positionIncrements[i];

            rotInc[0] = (next.rotationX - (part.defaultRotationX + this.prevRotationIncrements[i][0])) * animationDegree;
            rotInc[1] = (next.rotationY - (part.defaultRotationY + this.prevRotationIncrements[i][1])) * animationDegree;
            rotInc[2] = (next.rotationZ - (part.defaultRotationZ + this.prevRotationIncrements[i][2])) * animationDegree;

            posInc[0] = (next.positionX - (part.defaultPositionX + this.prevPositionIncrements[i][0])) * animationDegree;
            posInc[1] = (next.positionY - (part.defaultPositionY + this.prevPositionIncrements[i][1])) * animationDegree;
            posInc[2] = (next.positionZ - (part.defaultPositionZ + this.prevPositionIncrements[i][2])) * animationDegree;
        }
    }

    public void initAnimation(Animatable entity, Animation animation) {
        this.animation = animation;
        if (this.animations.get(animation) == null) {
            this.animation = EntityAnimation.IDLE.get();
        }
    }

    protected void initAnimationTicks(Animatable entity) {
        this.startAnimation(entity);
        if (EntityAnimation.getAnimation(this.animation).shouldHold()) {
            this.poseIndex     = this.poseCount - 1;
            this.animationTick = this.animations.get(this.animation)[this.poseIndex][1];
        } else {
            this.animationTick = 0;
        }
    }

    protected void startAnimation(Animatable entity) {
        float[][] pose = this.animations.get(this.animation);
        if (pose != null) {
            this.pose          = this.poses[(int) pose[this.poseIndex][0]];
            this.poseLength    = Math.max(1, pose[this.poseIndex][1]);
            this.animationTick = 0;

            this.initIncrements(entity);
        }
    }

    /* ========================== RUNTIME LOOP ========================= */

    public void performAnimations(Animatable entity,
                                  float limbSwing, float limbSwingAmount,
                                  float ticks) {

        this.limbSwing       = limbSwing;
        this.limbSwingAmount = limbSwingAmount;
        Animation requested = this.getRequestedAnimation(entity);
        if (requested != this.animation) {
            this.setAnimation(entity, requested);
        }

        if (this.poseIndex >= this.poseCount) {
            this.poseIndex = this.poseCount - 1;
        }

        this.inertiaFactor = this.calculateInertiaFactor();

        if (this.pose == null) {
            JurassicReborn.getLogger().error("Trying to animate to a null pose array");
        } else {
            for (int i = 0; i < Math.min(this.parts.length, this.pose.length); i++) {
                if (this.pose[i] == null) {
                    JurassicReborn.getLogger().error("Part " + i + " in pose is null");
                } else {
                    this.applyRotations(i);
                    this.applyTranslations(i);
                }
            }
        }

        if (this.updateAnimationTick(entity, ticks)) {
            this.onPoseFinish(entity, ticks);
        }

        this.prevTicks = ticks;
    }

    /* --------------------- INCREMENT / DECAY ------------------------- */

    public boolean updateAnimationTick(Animatable entity, float ticks) {
        float inc = (ticks - this.prevTicks) * this.getAnimationSpeed(entity);
        if (this.animationTick < 0.0F) this.animationTick = 0.0F;

        if (!EntityAnimation.getAnimation(this.animation).shouldHold() || this.poseIndex < this.poseCount) {
            this.animationTick += inc;
            if (this.animationTick >= this.poseLength) {
                this.animationTick = this.poseLength;
                return true;
            }
            return false;
        } else {
            if (this.animationTick < this.poseLength) {
                this.animationTick += inc;
                if (this.animationTick >= this.poseLength) this.animationTick = this.poseLength;
            } else {
                this.animationTick = this.poseLength;
            }
            return false;
        }
    }

    protected float calculateInertiaFactor() {
        float factor = this.animationTick / this.poseLength;
        if (this.useInertia && EntityAnimation.getAnimation(this.animation).useInertia()) {
            factor = (float) (Math.sin(Math.PI * (factor - 0.5D)) * 0.5D + 0.5D);
        }
        return Math.min(1.0F, Math.max(0.0F, factor));
    }

    /* ---------------------- APPLYING POSE STEPS ---------------------- */

    protected void applyRotations(int idx) {
        AdvancedModelBox part = this.parts[idx];
        float[] rotInc = this.rotationIncrements[idx];

        part.rotateAngleX += rotInc[0] * this.inertiaFactor + this.prevRotationIncrements[idx][0];
        part.rotateAngleY += rotInc[1] * this.inertiaFactor + this.prevRotationIncrements[idx][1];
        part.rotateAngleZ += rotInc[2] * this.inertiaFactor + this.prevRotationIncrements[idx][2];
    }

    protected void applyTranslations(int idx) {
        AdvancedModelBox part = this.parts[idx];
        float[] posInc = this.positionIncrements[idx];

        part.rotationPointX += posInc[0] * this.inertiaFactor + this.prevPositionIncrements[idx][0];
        part.rotationPointY += posInc[1] * this.inertiaFactor + this.prevPositionIncrements[idx][1];
        part.rotationPointZ += posInc[2] * this.inertiaFactor + this.prevPositionIncrements[idx][2];
    }

    /* ------------------- POSE / ANIMATION CONTROL -------------------- */

    protected void setPose(int poseIdx) {
        this.poseCount  = this.animations.get(this.animation).length;
        this.poseIndex  = poseIdx;
        this.pose       = this.poses[(int) this.animations.get(this.animation)[this.poseIndex][0]];
    }

    protected void setPose(Animatable entity, float ticks) {
        this.pose          = this.poses[(int) this.animations.get(this.animation)[this.poseIndex][0]];
        this.poseLength    = this.animations.get(this.animation)[this.poseIndex][1];
        this.animationTick = 0;
        this.prevTicks     = ticks;
        this.initIncrements(entity);
    }

    protected void onPoseFinish(Animatable entity, float ticks) {
        if (this.incrementPose()) {
            this.setAnimation(entity, this.isEntityAnimationDependent()
                    ? EntityAnimation.IDLE.get()
                    : this.getRequestedAnimation(entity));
        } else {
            this.updatePreviousPose();
        }
        this.setPose(entity, ticks);
    }

    public boolean incrementPose() {
        this.poseIndex++;
        if (this.poseIndex >= this.poseCount) {
            EntityAnimation anim = EntityAnimation.getAnimation(this.animation);
            if (anim != null && anim.shouldHold()) {
                this.poseIndex = this.poseCount - 1;
            } else {
                this.poseIndex = 0;
                return true;
            }
        }
        return false;
    }

    protected void setAnimation(Animatable entity, Animation requested) {
        this.updatePreviousPose();

        if (this.animations.get(requested) != null
                && !(this.animation != EntityAnimation.IDLE.get()
                && this.animation == requested
                && !this.isLooping())) {
            this.animation = requested;
        } else {
            this.animation = EntityAnimation.IDLE.get();
        }

        this.setPose(0);
        this.startAnimation(entity);
    }

    protected void updatePreviousPose() {
        for (int i = 0; i < this.parts.length; i++) {
            this.prevRotationIncrements[i][0] += this.rotationIncrements[i][0] * this.inertiaFactor;
            this.prevRotationIncrements[i][1] += this.rotationIncrements[i][1] * this.inertiaFactor;
            this.prevRotationIncrements[i][2] += this.rotationIncrements[i][2] * this.inertiaFactor;

            this.prevPositionIncrements[i][0] += this.positionIncrements[i][0] * this.inertiaFactor;
            this.prevPositionIncrements[i][1] += this.positionIncrements[i][1] * this.inertiaFactor;
            this.prevPositionIncrements[i][2] += this.positionIncrements[i][2] * this.inertiaFactor;
        }
    }

    /* -------------------- OVERRIDES / EXTENSION ---------------------- */

    protected float getAnimationSpeed(Animatable entity) {
        return 1.0F;
    }
    protected float getAnimationDegree(Animatable entity) {
        return 1.0F;
    }
    protected Animation getRequestedAnimation(Animatable entity) {
        return entity.getAnimation();
    }
    protected boolean isEntityAnimationDependent() {
        return true;
    }
    public boolean isLooping() {
        return false;
    }
}
