package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import mod.reborn.server.entity.dinosaur.VelociraptorBlueEntity;

public class VelociraptorBlueAnimator extends EntityAnimator<VelociraptorBlueEntity>
{
    @Override
    protected void performAnimations(AnimatableModel parModel, VelociraptorBlueEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer waist = parModel.getCube("body3");
        AdvancedModelRenderer chest = parModel.getCube("body2");
        AdvancedModelRenderer shoulders = parModel.getCube("body1");
        AdvancedModelRenderer leftThigh = parModel.getCube("Left thigh");
        AdvancedModelRenderer rightThigh = parModel.getCube("Right thigh");
        AdvancedModelRenderer neck1 = parModel.getCube("neck1");
        AdvancedModelRenderer neck2 = parModel.getCube("neck2");
        AdvancedModelRenderer neck3 = parModel.getCube("neck3");
        AdvancedModelRenderer neck4 = parModel.getCube("neck4");
        AdvancedModelRenderer head = parModel.getCube("Head");
        AdvancedModelRenderer jaw = parModel.getCube("down_jaw");
        AdvancedModelRenderer leftShin = parModel.getCube("Left shin");
        AdvancedModelRenderer rightShin = parModel.getCube("Right shin");
        AdvancedModelRenderer leftUpperFoot = parModel.getCube("Left upper foot");
        AdvancedModelRenderer leftFoot = parModel.getCube("Left foot");
        AdvancedModelRenderer rightUpperFoot = parModel.getCube("Right upper foot");
        AdvancedModelRenderer rightFoot = parModel.getCube("Right foot");
        AdvancedModelRenderer upperArmRight = parModel.getCube("Right arm");
        AdvancedModelRenderer upperArmLeft = parModel.getCube("Left arm");
        AdvancedModelRenderer tail1 = parModel.getCube("tail1");
        AdvancedModelRenderer tail2 = parModel.getCube("tail2");
        AdvancedModelRenderer tail3 = parModel.getCube("tail3");
        AdvancedModelRenderer tail4 = parModel.getCube("tail4");
        AdvancedModelRenderer tail5 = parModel.getCube("tail5");
        AdvancedModelRenderer tail6 = parModel.getCube("tail6");
        AdvancedModelRenderer rightToe = parModel.getCube("Right toe");
        AdvancedModelRenderer leftToe = parModel.getCube("Left toe");

        AdvancedModelRenderer lowerArmRight = parModel.getCube("Right forearm");
        AdvancedModelRenderer lowerArmLeft = parModel.getCube("Left forearm");
        AdvancedModelRenderer Hand_Right = parModel.getCube("Right hand");
        AdvancedModelRenderer Hand_Left = parModel.getCube("Left hand");

        AdvancedModelRenderer[] rightArmParts = new AdvancedModelRenderer[] { Hand_Right, lowerArmRight, upperArmRight };
        AdvancedModelRenderer[] leftArmParts = new AdvancedModelRenderer[] { Hand_Left, lowerArmLeft, upperArmLeft };
        AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelRenderer[] bodyParts = new AdvancedModelRenderer[] { waist, chest, shoulders, neck4, neck3, neck2, neck1, head };

        float globalSpeed = 1.0F;
        float globalDegree = 1.0F;

        parModel.bob(waist, globalSpeed * 0.5F, globalDegree * 1.0F, false, f, f1);
        parModel.bob(rightThigh, globalSpeed * 0.5F, globalDegree * 1.0F, false, f, f1);
        parModel.bob(leftThigh, globalSpeed * 0.5F, globalDegree * 1.0F, false, f, f1);
        parModel.walk(shoulders, 1F * globalSpeed, 0.2F, true, 1, 0, f, f1);
        parModel.walk(chest, 1F * globalSpeed,  0.2F, false, 0.5F, 0, f, f1);

        parModel.walk(leftThigh, 0.5F * globalSpeed, 0.7F, false, 3.14F, 0.2F, f, f1);
        parModel.walk(leftShin, 0.5F * globalSpeed, 0.6F, false, 1.5F, 0.3F, f, f1);
        parModel.walk(leftUpperFoot, 0.5F * globalSpeed, 0.8F, false, -1F, -0.1F, f, f1);
        parModel.walk(leftFoot, 0.5F * globalSpeed, 1.5F, true, -1F, 1F, f, f1);

        parModel.walk(rightThigh, 0.5F * globalSpeed, 0.7F, true, 3.14F, 0.2F, f, f1);
        parModel.walk(rightShin, 0.5F * globalSpeed, 0.6F, true, 1.5F, 0.3F, f, f1);
        parModel.walk(rightUpperFoot, 0.5F * globalSpeed, 0.8F, true, -1F, -0.1F, f, f1);
        parModel.walk(rightFoot, 0.5F * globalSpeed, 1.5F, false, -1F, 1F, f, f1);

        parModel.chainWave(tailParts, globalSpeed * 0.5F, globalDegree * 0.05F, 1, f, f1);
        parModel.chainSwing(tailParts, globalSpeed * 0.5F, globalDegree * 0.1F, 2, f, f1);
        parModel.chainWave(bodyParts, globalSpeed * 0.5F, globalDegree * 0.025F, 3, f, f1);

        parModel.chainWave(rightArmParts, 1F * globalSpeed, -0.3F, 4, f, f1);
        parModel.chainWave(leftArmParts, 1F * globalSpeed, -0.3F, 4, f, f1);

        parModel.chainWave(tailParts, 0.1F, 0.05F, 2, ticks, 0.25F);
        parModel.chainWave(bodyParts, 0.1F, -0.03F, 5, ticks, 0.25F);
        parModel.chainWave(rightArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);
        parModel.chainWave(leftArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);

        parModel.faceTarget(rotationYaw, rotationPitch, 2.0F, neck1, head);

        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
