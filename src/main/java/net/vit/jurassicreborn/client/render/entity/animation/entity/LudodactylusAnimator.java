package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.LudodactylusEntity;

@OnlyIn(Dist.CLIENT)
public class LudodactylusAnimator extends EntityAnimator<LudodactylusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, LudodactylusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelBox leftThigh = model.getCube("Left thigh");
        AdvancedModelBox leftCalf = model.getCube("Left calf");
        AdvancedModelBox leftUpperFoot = model.getCube("Left upper foot");
        AdvancedModelBox leftFoot = model.getCube("Left foot");
        AdvancedModelBox rightThigh = model.getCube("Right thigh");
        AdvancedModelBox rightCalf = model.getCube("right calf");
        AdvancedModelBox rightUpperFoot = model.getCube("Right upper foot");
        AdvancedModelBox rightFoot = model.getCube("Right foot");
        AdvancedModelBox jaw = model.getCube("Lower jaw 1");
        AdvancedModelBox head = model.getCube("Head");
        AdvancedModelBox neck3 = model.getCube("Neck 3");
        AdvancedModelBox neck2 = model.getCube("Neck 2");
        AdvancedModelBox neck1 = model.getCube("Neck 1");
        AdvancedModelBox body3 = model.getCube("Body 3");
        AdvancedModelBox body2 = model.getCube("Body 2");
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

        AdvancedModelBox[] neck = new AdvancedModelBox[]{head, neck3, neck2, neck1};
        AdvancedModelBox[] tail = new AdvancedModelBox[]{tail1, tail2, tail3};
        AdvancedModelBox[] wingLeft = new AdvancedModelBox[]{leftArm4, leftArm3, leftArm2, leftArm1};
        AdvancedModelBox[] wingRight = new AdvancedModelBox[]{rightArm4, rightArm3, rightArm2, rightArm1};
        AdvancedModelBox[] legLeft = new AdvancedModelBox[]{leftThigh, leftCalf, leftUpperFoot, leftFoot};
        AdvancedModelBox[] legRight = new AdvancedModelBox[]{rightThigh, rightCalf, rightUpperFoot, rightFoot};

        float globalSpeed = 0.45F;
        float globalDegree = 1F;
        float globalHeight = 1F;
        float frontOffset = -1.35f;

        if (entity.isOnGround() && !entity.isCarcass()) {
            // --- grounded locomotion ---
            model.bob(body1, 1 * globalSpeed, 1 * globalHeight, false, f, f1);
            model.bob(leftThigh, 1 * globalSpeed, 1 * globalHeight, false, f, f1);
            model.bob(rightThigh, 1 * globalSpeed, 1 * globalHeight, false, f, f1);
            model.walk(body1, 1 * globalSpeed, -0.08f * globalHeight, false, 0, 0.1f, f, f1);
            model.walk(leftArm1, 1 * globalSpeed, -0.08f * globalHeight, true, 0, 0, f, f1);
            model.walk(rightArm1, 1 * globalSpeed, -0.08f * globalHeight, true, 0, 0, f, f1);
            model.chainWave(neck, 1 * globalSpeed, -0.15f * globalHeight, 4, f, f1);
            model.chainWave(tail, 1 * globalSpeed, 0.1f * globalHeight, 1, f, f1);

            model.walk(leftThigh, 0.5F * globalSpeed, 0.7F * globalDegree, false, 3.14F, 0.2F, f, f1);
            model.walk(leftCalf, 0.5F * globalSpeed, 0.6F * globalDegree, false, 1.5F, 0.3F, f, f1);
            model.walk(leftUpperFoot, 0.5F * globalSpeed, 0.8F * globalDegree, false, -2F, -0.4F, f, f1);

            model.walk(rightThigh, 0.5F * globalSpeed, 0.7F * globalDegree, true, 3.14F, 0.2F, f, f1);
            model.walk(rightCalf, 0.5F * globalSpeed, 0.6F * globalDegree, true, 1.5F, 0.3F, f, f1);
            model.walk(rightUpperFoot, 0.5F * globalSpeed, 0.8F * globalDegree, true, -2F, -0.4F, f, f1);

            model.walk(leftArm1, 0.5F * globalSpeed, 0.5F * globalDegree, true, -3.14F + frontOffset, 0.5F, f, f1);
            model.walk(leftArm2, 0.5F * globalSpeed, 0.4F * globalDegree, true, -1.5F + frontOffset, -0.3F, f, f1);
            model.walk(leftArm3, 0.5F * globalSpeed, 0.7F * globalDegree, true, 2F + frontOffset, 0.4F, f, f1);

            model.walk(rightArm1, 0.5F * globalSpeed, 0.5F * globalDegree, false, -3.14F + frontOffset, 0.5F, f, f1);
            model.walk(rightArm2, 0.5F * globalSpeed, 0.4F * globalDegree, false, -1.5F + frontOffset, -0.3F, f, f1);
            model.walk(rightArm3, 0.5F * globalSpeed, 0.7F * globalDegree, false, 2F + frontOffset, 0.4F, f, f1);
        } else {
            if(!entity.isCarcass()) {
                // --- base flight pose ---
                body1.rotateAngleX += 0.3;
                neck1.rotateAngleX -= 0.1;
                leftThigh.rotateAngleX += 0.8;
                rightThigh.rotateAngleX += 0.8;
                leftCalf.rotateAngleX += 0.7;
                rightCalf.rotateAngleX += 0.7;
                leftUpperFoot.rotateAngleX -= 0.3;
                rightUpperFoot.rotateAngleX -= 0.3;
                leftFoot.rotateAngleX += 2;
                rightFoot.rotateAngleX += 2;
                leftArm1.rotateAngleZ -= 1;
                leftArm2.rotateAngleZ -= 0.4;
                leftArm3.rotateAngleZ -= 0.1;
                leftArm4.rotateAngleZ += 3.3;
                leftArm4.rotateAngleY += 2.6;
                leftArm4.rotateAngleX += 1.2;
                rightArm1.rotateAngleZ += 1;
                rightArm2.rotateAngleZ += 0.4;
                rightArm3.rotateAngleZ += 0.1;
                rightArm4.rotateAngleZ -= 3.3;
                rightArm4.rotateAngleY -= 2.6;
                rightArm4.rotateAngleX += 1.2;

                // slight shoulder drop for flight
                leftArm1.offsetY += 0.35F;
                rightArm1.offsetY += 0.35F;

                // choose between moving flight and idle hover (use velocity + limb swing)
                boolean movingAnim = f1 > 0.05f || entity.getDeltaMovement().lengthSqr() > 0.0004;

                if (movingAnim) {
                    // --- moving flight (limb-swing driven) ---
                    model.bob(body1, 0.3f, 7, false, f, f1);
                    model.bob(leftThigh, 0.3f, 7, false, f, f1);
                    model.bob(rightThigh, 0.3f, 7, false, f, f1);
                    model.walk(body1, 0.3f, 0.2f, true, 1, 0, f, f1);
                    model.swing(leftArm1, 0.3f, 0.2f, false, 1, 0, f, f1);
                    model.swing(leftArm2, 0.3f, 0.2f, false, 1, 0, f, f1);
                    model.walk(neck1, 0.3f, 0.2f, false, 1, 0.2f, f, f1);
                    model.walk(head, 0.3f, 0.2f, true, 1, -0.4f, f, f1);

                    model.chainFlap(wingLeft, 0.3f, 0.8f, 2, f, f1);
                    model.walk(leftArm1, 0.3f, 0.6f, false, -1f, -0.2f, f, f1);
                    model.walk(leftArm2, 0.3f, 1.2f, true, -1f, 0, f, f1);
                    model.walk(leftArm3, 0.3f, 0.7f, false, -1f, 0.2f, f, f1);

                    model.chainFlap(wingRight, 0.3f, -0.8f, 2, f, f1);
                    model.walk(rightArm1, 0.3f, 0.6f, false, -1f, -0.2f, f, f1);
                    model.walk(rightArm2, 0.3f, 1.2f, true, -1f, 0, f, f1);
                    model.walk(rightArm3, 0.3f, 0.7f, false, -1f, 0.2f, f, f1);

                    model.chainWave(legLeft, 0.3f, 0.2f, -3, f, f1);
                    model.chainWave(legRight, 0.3f, 0.2f, -3, f, f1);
                    model.chainWave(tail, 0.3f, 0.2f, 1, f, f1);
                    model.chainWave(neck, 0.3f, 0.4f, 4, f, f1);
                } else {
                    // --- idle hover flap (ticks-driven, works when f1 == 0) ---
                    float s = 0.20f; // flap speed
                    float a = 0.60f; // flap amplitude

                    // gentle body/leg bob while hovering
                    model.bob(body1, s, 3.0f, false, ticks, 1.0F);
                    model.bob(leftThigh, s, 3.0f, false, ticks, 1.0F);
                    model.bob(rightThigh, s, 3.0f, false, ticks, 1.0F);

                    // continuous wing flap using ticks
                    model.chainFlap(wingLeft,  s,  a, 2, ticks, 1.0F);
                    model.chainFlap(wingRight, s, -a, 2, ticks, 1.0F);
                    model.walk(leftArm1,  s, 0.45f, false, -1f, -0.1f, ticks, 1.0F);
                    model.walk(leftArm2,  s, 0.90f, true,  -1f,  0.0f, ticks, 1.0F);
                    model.walk(leftArm3,  s, 0.55f, false, -1f,  0.1f, ticks, 1.0F);
                    model.walk(rightArm1, s, 0.45f, false, -1f, -0.1f, ticks, 1.0F);
                    model.walk(rightArm2, s, 0.90f, true,  -1f,  0.0f, ticks, 1.0F);
                    model.walk(rightArm3, s, 0.55f, false, -1f,  0.1f, ticks, 1.0F);

                    // subtle tail/neck sway while hovering
                    model.chainWave(tail, s, 0.15f, 1, ticks, 1.0F);
                    model.chainWave(neck, s, 0.20f, 4, ticks, 1.0F);
                }
            }
        }

        // tiny ambient motion (always-on)
        if (!entity.isCarcass()) {
            model.walk(body1, 0.08f, -0.05f, false, 0, 0, ticks, 0.25F);
            model.chainWave(neck, 0.08f, 0.03f, 2, ticks, 0.25F);
            model.walk(leftArm1, 0.08f, 0.1f, false, 0, 0, ticks, 0.25F);
            model.walk(rightArm1, 0.08f, 0.1f, false, 0, 0, ticks, 0.25F);
            model.walk(leftArm2, 0.08f, 0.1f, false, 0, 0, ticks, 0.25F);
            model.walk(rightArm2, 0.08f, 0.1f, false, 0, 0, ticks, 0.25F);
            model.walk(leftArm3, 0.08f, 0.2f, true, 0, 0, ticks, 0.25F);
            model.walk(rightArm3, 0.08f, 0.2f, true, 0, 0, ticks, 0.25F);
            model.flap(leftArm1, 0.08f, 0.03f, false, 0, 0, ticks, 0.25F);
            model.flap(rightArm1, 0.08f, 0.03f, true, 0, 0, ticks, 0.25F);
            leftArm1.rotationPointZ -= 1 * Math.cos(ticks * 0.08);
            rightArm1.rotationPointZ -= 1 * Math.cos(ticks * 0.08);
        }
    }
}