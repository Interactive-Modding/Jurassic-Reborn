package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.FlyingDinosaurEntity;
//import net.vit.jurassicreborn.common.entities.ai.FlyAI;

import java.util.Arrays;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractPterosaurAnimator<E extends FlyingDinosaurEntity> extends EntityAnimator<E> {

    protected String headCube() {
        return "Head";
    }

    protected String altHeadCube() {
        return null;
    }

    protected String neck1Cube() {
        return "Neck 1";
    }

    protected String neck2Cube() {
        return "Neck 2";
    }

    protected String neck3Cube() {
        return "Neck 3";
    }

    protected float groundSpeed() {
        return 0.50F;
    }

    protected float groundDegree() {
        return 2.0F;
    }

    protected float groundHeight() {
        return 2.0F;
    }

    protected float frontOffset() {
        return -1.35F;
    }

    protected float flightCadenceMultiplier() {
        return 1.0F;
    }

    protected float flightAmplitudeMultiplier() {
        return 1.0F;
    }

    private AdvancedModelBox cube(AnimatableModel model, String primary, String fallback) {
        if (primary == null) {
            return fallback != null ? model.getCube(fallback) : null;
        }

        AdvancedModelBox box = model.getCube(primary);
        if (box == null && fallback != null) {
            box = model.getCube(fallback);
        }
        return box;
    }

    private AdvancedModelBox[] chain(AdvancedModelBox... parts) {
        return Arrays.stream(parts).filter(Objects::nonNull).toArray(AdvancedModelBox[]::new);
    }

    private boolean missing(AdvancedModelBox... boxes) {
        for (AdvancedModelBox box : boxes) {
            if (box == null) {
                return true;
            }
        }
        return false;
    }

    private void chainWaveIfPresent(AnimatableModel model, AdvancedModelBox[] boxes, float speed, float degree, int offset, float f, float f1) {
        if (boxes.length > 0) {
            model.chainWave(boxes, speed, degree, offset, f, f1);
        }
    }

    private void chainFlapIfPresent(AnimatableModel model, AdvancedModelBox[] boxes, float speed, float degree, int offset, float f, float f1) {
        if (boxes.length > 0) {
            model.chainFlap(boxes, speed, degree, offset, f, f1);
        }
    }

    @Override
    protected void performAnimations(AnimatableModel model, E entity, float f, float f1, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        AdvancedModelBox leftThigh = model.getCube("Left thigh");
        AdvancedModelBox leftCalf = model.getCube("Left calf");
        AdvancedModelBox leftUpperFoot = model.getCube("Left upper foot");
        AdvancedModelBox leftFoot = model.getCube("Left foot");
        AdvancedModelBox rightThigh = model.getCube("Right thigh");
        AdvancedModelBox rightCalf = model.getCube("right calf");
        AdvancedModelBox rightUpperFoot = model.getCube("Right upper foot");
        AdvancedModelBox rightFoot = model.getCube("Right foot");

        AdvancedModelBox head = cube(model, headCube(), altHeadCube());
        AdvancedModelBox neck3 = cube(model, neck3Cube(), null);
        AdvancedModelBox neck2 = cube(model, neck2Cube(), null);
        AdvancedModelBox neck1 = cube(model, neck1Cube(), null);

        AdvancedModelBox body1 = model.getCube("Body 1");
        AdvancedModelBox tail1 = model.getCube("Tail 1");
        AdvancedModelBox tail2 = model.getCube("Tail 2");
        AdvancedModelBox tail3 = model.getCube("Tail 3");

        AdvancedModelBox leftArm1 = model.getCube("Left Arm 1");
        AdvancedModelBox leftArm2 = model.getCube("Left Arm 2");
        AdvancedModelBox leftArm3 = model.getCube("Left Arm 3");
        AdvancedModelBox leftArm4 = model.getCube("Left Arm 4");

        AdvancedModelBox rightArm1 = model.getCube("Right Arm 1");
        AdvancedModelBox rightArm2 = model.getCube("Right Arm 2");
        AdvancedModelBox rightArm3 = model.getCube("Right Arm 3");
        AdvancedModelBox rightArm4 = model.getCube("Right Arm 4");

        if (missing(
                leftThigh, leftCalf, leftUpperFoot, leftFoot,
                rightThigh, rightCalf, rightUpperFoot, rightFoot,
                head, body1, tail1, tail2, tail3,
                leftArm1, leftArm2, leftArm3, leftArm4,
                rightArm1, rightArm2, rightArm3, rightArm4
        )) {
            return;
        }

        AdvancedModelBox[] neck = chain(head, neck3, neck2, neck1);
        AdvancedModelBox[] tail = chain(tail1, tail2, tail3);
        AdvancedModelBox[] wingLeft = chain(leftArm4, leftArm3, leftArm2, leftArm1);
        AdvancedModelBox[] wingRight = chain(rightArm4, rightArm3, rightArm2, rightArm1);
        AdvancedModelBox[] legLeft = chain(leftThigh, leftCalf, leftUpperFoot, leftFoot);
        AdvancedModelBox[] legRight = chain(rightThigh, rightCalf, rightUpperFoot, rightFoot);

        float attrSpeed = (float) entity.getAttributeValue(Attributes.MOVEMENT_SPEED);
        float speedScale = Mth.clamp(attrSpeed / 0.35F, 0.75F, 1.20F);
        float sizeScale = Mth.clamp(1.15F / Math.max(entity.getBbWidth(), 0.60F), 0.82F, 1.30F);

        float gSpeed = groundSpeed() * speedScale * sizeScale;
        float gDegree = groundDegree() * Mth.clamp(sizeScale, 0.90F, 1.18F);
        float gHeight = groundHeight() * Mth.clamp(sizeScale, 0.90F, 1.15F);
        float frontOffset = frontOffset();

        float flapCadenceScale = Mth.clamp(speedScale * sizeScale * flightCadenceMultiplier(), 0.80F, 1.35F);
        float broadWingScale = Mth.clamp(entity.getBbWidth(), 0.70F, 1.60F) * flightAmplitudeMultiplier();


        boolean actualFlight = entity.isFallFlying();
        boolean aquaticFlight = entity.inWater();
        boolean inFlight = actualFlight || aquaticFlight;

        boolean hardFlap = inFlight;

        boolean glide = inFlight;

        boolean groundedPose = entity.onGround();

        float horizSpeed = (float) entity.getDeltaMovement().horizontalDistance();
        float verticalSpeed = (float) Math.abs(entity.getDeltaMovement().y);
        float motion = Mth.clamp(horizSpeed * 7.0F + verticalSpeed * 3.5F, 0.35F, 1.3F);

        if (!inFlight && groundedPose && !entity.isCarcass()) {
            float walkAmount = Mth.clamp(f1, 0.0F, 1.0F);

            model.bob(body1, 1.0F * gSpeed, 1.0F * gHeight, false, f, walkAmount);
            model.bob(leftThigh, 1.0F * gSpeed, 1.0F * gHeight, false, f, walkAmount);
            model.bob(rightThigh, 1.0F * gSpeed, 1.0F * gHeight, false, f, walkAmount);

            model.walk(body1, 1.0F * gSpeed, -0.08F * gHeight, false, 0, 0.1F, f, walkAmount);
            model.walk(leftArm1, 1.0F * gSpeed, -0.08F * gHeight, true, 0, 0, f, walkAmount);
            model.walk(rightArm1, 1.0F * gSpeed, -0.08F * gHeight, true, 0, 0, f, walkAmount);
            chainWaveIfPresent(model, neck, 1.0F * gSpeed, -0.12F * gHeight, 4, f, walkAmount);
            chainWaveIfPresent(model, tail, 1.0F * gSpeed, 0.10F * gHeight, 1, f, walkAmount);

            model.walk(leftThigh, 0.5F * gSpeed, 0.7F * gDegree, false, 3.14F, 0.2F, f, walkAmount);
            model.walk(leftCalf, 0.5F * gSpeed, 0.6F * gDegree, false, 1.5F, 0.3F, f, walkAmount);
            model.walk(leftUpperFoot, 0.5F * gSpeed, 0.8F * gDegree, false, -2F, -0.4F, f, walkAmount);

            model.walk(rightThigh, 0.5F * gSpeed, 0.7F * gDegree, true, 3.14F, 0.2F, f, walkAmount);
            model.walk(rightCalf, 0.5F * gSpeed, 0.6F * gDegree, true, 1.5F, 0.3F, f, walkAmount);
            model.walk(rightUpperFoot, 0.5F * gSpeed, 0.8F * gDegree, true, -2F, -0.4F, f, walkAmount);

            model.walk(leftArm1, 0.5F * gSpeed, 0.5F * gDegree, true, -3.14F + frontOffset, 0.5F, f, walkAmount);
            model.walk(leftArm2, 0.5F * gSpeed, 0.4F * gDegree, true, -1.5F + frontOffset, -0.3F, f, walkAmount);
            model.walk(leftArm3, 0.5F * gSpeed, 0.7F * gDegree, true, 2F + frontOffset, 0.4F, f, walkAmount);

            model.walk(rightArm1, 0.5F * gSpeed, 0.5F * gDegree, false, -3.14F + frontOffset, 0.5F, f, walkAmount);
            model.walk(rightArm2, 0.5F * gSpeed, 0.4F * gDegree, false, -1.5F + frontOffset, -0.3F, f, walkAmount);
            model.walk(rightArm3, 0.5F * gSpeed, 0.7F * gDegree, false, 2F + frontOffset, 0.4F, f, walkAmount);
        } else if (!entity.isCarcass()) {
            body1.rotateAngleX += 0.28F;
            if (neck1 != null) neck1.rotateAngleX -= 0.08F;

            leftThigh.rotateAngleX += 0.80F;
            rightThigh.rotateAngleX += 0.80F;
            leftCalf.rotateAngleX += 0.70F;
            rightCalf.rotateAngleX += 0.70F;
            leftUpperFoot.rotateAngleX -= 0.30F;
            rightUpperFoot.rotateAngleX -= 0.30F;
            leftFoot.rotateAngleX += 2.00F;
            rightFoot.rotateAngleX += 2.00F;

            leftArm1.rotateAngleZ -= 1.00F;
            leftArm2.rotateAngleZ -= 0.40F;
            leftArm3.rotateAngleZ -= 0.10F;
            leftArm4.rotateAngleZ += 3.30F;
            leftArm4.rotateAngleY += 2.60F;
            leftArm4.rotateAngleX += 1.20F;

            rightArm1.rotateAngleZ += 1.00F;
            rightArm2.rotateAngleZ += 0.40F;
            rightArm3.rotateAngleZ += 0.10F;
            rightArm4.rotateAngleZ -= 3.30F;
            rightArm4.rotateAngleY -= 2.60F;
            rightArm4.rotateAngleX += 1.20F;

            leftArm1.offsetY += 0.32F;
            rightArm1.offsetY += 0.32F;

            if (glide) {
                float s = (0.17F + speedScale * 0.03F) * Mth.clamp(sizeScale, 0.96F, 1.10F);
                float a = (0.34F + (broadWingScale - 0.70F) * 0.10F);
                float amount = Mth.clamp(0.72F + motion * 0.18F, 0.72F, 0.95F);

                leftArm4.rotateAngleZ += 0.12F;
                rightArm4.rotateAngleZ -= 0.12F;

                model.bob(body1, s, 2.0F, false, ticks, amount);
                model.bob(leftThigh, s, 1.8F, false, ticks, amount * 0.75F);
                model.bob(rightThigh, s, 1.8F, false, ticks, amount * 0.75F);

                chainFlapIfPresent(model, wingLeft, s, a, 2, ticks, amount);
                chainFlapIfPresent(model, wingRight, s, -a, 2, ticks, amount);

                model.walk(leftArm1, s, 0.26F, false, -1F, -0.05F, ticks, amount);
                model.walk(leftArm2, s, 0.54F, true, -1F, 0.00F, ticks, amount);
                model.walk(leftArm3, s, 0.30F, false, -1F, 0.08F, ticks, amount);

                model.walk(rightArm1, s, 0.26F, false, -1F, -0.05F, ticks, amount);
                model.walk(rightArm2, s, 0.54F, true, -1F, 0.00F, ticks, amount);
                model.walk(rightArm3, s, 0.30F, false, -1F, 0.08F, ticks, amount);

                chainWaveIfPresent(model, tail, 0.11F, 0.09F, 1, ticks, amount * 0.65F);
                chainWaveIfPresent(model, neck, 0.11F, 0.11F, 4, ticks, amount * 0.65F);
            } else {
                float s = (hardFlap ? 0.28F : 0.22F) * flapCadenceScale;
                float a = hardFlap
                        ? (0.82F + (broadWingScale - 0.70F) * 0.08F)
                        : (0.62F + (broadWingScale - 0.70F) * 0.06F);

                float amount = Mth.clamp(Math.max(motion, 0.45F), 0.45F, 1.10F);

                model.bob(body1, s, hardFlap ? 3.8F : 2.8F, false, ticks, amount);
                model.bob(leftThigh, s, hardFlap ? 3.4F : 2.3F, false, ticks, amount);
                model.bob(rightThigh, s, hardFlap ? 3.4F : 2.3F, false, ticks, amount);

                chainFlapIfPresent(model, wingLeft, s, a, 2, ticks, amount);
                chainFlapIfPresent(model, wingRight, s, -a, 2, ticks, amount);

                model.walk(leftArm1, s, 0.45F, false, -1F, -0.10F, ticks, amount);
                model.walk(leftArm2, s, 0.90F, true, -1F, 0.00F, ticks, amount);
                model.walk(leftArm3, s, 0.55F, false, -1F, 0.10F, ticks, amount);

                model.walk(rightArm1, s, 0.45F, false, -1F, -0.10F, ticks, amount);
                model.walk(rightArm2, s, 0.90F, true, -1F, 0.00F, ticks, amount);
                model.walk(rightArm3, s, 0.55F, false, -1F, 0.10F, ticks, amount);

                chainWaveIfPresent(model, legLeft, s, 0.16F, -3, ticks, amount * 0.85F);
                chainWaveIfPresent(model, legRight, s, 0.16F, -3, ticks, amount * 0.85F);
                chainWaveIfPresent(model, tail, s * 0.75F, 0.12F, 1, ticks, amount * 0.85F);
                chainWaveIfPresent(model, neck, s * 0.85F, 0.16F, 4, ticks, amount * 0.85F);
            }
        }

        if (!entity.isCarcass()) {
            model.walk(body1, 0.08F, -0.05F, false, 0, 0, ticks, 0.25F);
            chainWaveIfPresent(model, neck, 0.08F, 0.03F, 2, ticks, 0.25F);

            model.walk(leftArm1, 0.08F, 0.10F, false, 0, 0, ticks, 0.25F);
            model.walk(rightArm1, 0.08F, 0.10F, false, 0, 0, ticks, 0.25F);
            model.walk(leftArm2, 0.08F, 0.10F, false, 0, 0, ticks, 0.25F);
            model.walk(rightArm2, 0.08F, 0.10F, false, 0, 0, ticks, 0.25F);
            model.walk(leftArm3, 0.08F, 0.20F, true, 0, 0, ticks, 0.25F);
            model.walk(rightArm3, 0.08F, 0.20F, true, 0, 0, ticks, 0.25F);

            model.flap(leftArm1, 0.08F, 0.03F, false, 0, 0, ticks, 0.25F);
            model.flap(rightArm1, 0.08F, 0.03F, true, 0, 0, ticks, 0.25F);

            leftArm1.rotationPointZ -= 1.0F * Math.cos(ticks * 0.08F);
            rightArm1.rotationPointZ -= 1.0F * Math.cos(ticks * 0.08F);
        }
    }
}