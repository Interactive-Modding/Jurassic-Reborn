package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.QuetzalEntity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class QuetzalAnimator extends EntityAnimator<QuetzalEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, QuetzalEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelBox leftThigh = model.getCube("Left thigh");
        AdvancedModelBox leftCalf = model.getCube("Left calf");
        AdvancedModelBox leftUpperFoot = model.getCube("Left upper foot");
        AdvancedModelBox leftFoot = model.getCube("Left foot");
        AdvancedModelBox rightThigh = model.getCube("Right thigh");
        AdvancedModelBox rightCalf = model.getCube("right calf");
        AdvancedModelBox rightUpperFoot = model.getCube("Right upper foot");
        AdvancedModelBox rightFoot = model.getCube("Right foot");
        AdvancedModelBox jaw = model.getCube("Lower jaw 1");
        AdvancedModelBox head = model.getCube("Head ");
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

        float globalSpeed = 0.5F;
        float globalDegree = 2F;
        float globalHeight = 2F;
        float frontOffset = -1.35f;

        if (entity.isOnGround() && !entity.isCarcass()) {
        } else {
            if(!entity.isCarcass()) {
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
                leftArm1.rotateAngleZ -= 0.8;
                leftArm2.rotateAngleZ -= 0.4;
                leftArm3.rotateAngleZ -= 0.2;
                leftArm4.rotateAngleZ += 3.3;
                leftArm4.rotateAngleY += 2.6;
                leftArm4.rotateAngleX += 1.2;
                rightArm1.rotateAngleZ += 0.8;
                rightArm2.rotateAngleZ += 0.4;
                rightArm3.rotateAngleZ += 0.2;
                rightArm4.rotateAngleZ -= 3.3;
                rightArm4.rotateAngleY -= 2.6;
                rightArm4.rotateAngleX += 1.2;

                model.bob(body1, 0.1f, 7, false, f, f1);
                model.bob(leftThigh, 0.1f, 7, false, f, f1);
                model.bob(rightThigh, 0.1f, 7, false, f, f1);
                model.walk(body1, 0.1f, 0.2f, true, 1, 0, f, f1);
                model.swing(leftArm1, 0.1f, 0.2f, false, 1, 0, f, f1);
                model.swing(leftArm2, 0.1f, 0.2f, false, 1, 0, f, f1);

                model.chainFlap(wingLeft, 0.2f, 0.4f, 2, f, f1);
                model.walk(leftArm2, 0.2f, 0.8f, true, -1f, 0, f, f1);
                model.walk(leftArm3, 0.2f, 0.8f, false, -1f, 0.2f, f, f1);
                model.chainFlap(wingRight, 0.2f, -0.4f, 2, f, f1);
                model.walk(rightArm2, 0.2f, 0.8f, true, -1f, 0, f, f1);
                model.walk(rightArm3, 0.2f, 0.8f, false, -1f, 0.2f, f, f1);
                model.chainWave(legLeft, 0.1f, 0.2f, -3, f, f1);
                model.chainWave(legRight, 0.1f, 0.2f, -3, f, f1);
                model.chainWave(tail, 0.1f, 0.2f, 1, f, f1);
            }
        }
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
