package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.VelociraptorDeltaEntity;

@SideOnly(Side.CLIENT)
public class VelociraptorDeltaAnimator extends EntityAnimator<VelociraptorDeltaEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, VelociraptorDeltaEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer waist = model.getCube("body3");
        AdvancedModelRenderer chest = model.getCube("body2");
        AdvancedModelRenderer shoulders = model.getCube("body1");
        AdvancedModelRenderer leftThigh = model.getCube("Left thigh");
        AdvancedModelRenderer rightThigh = model.getCube("Right thigh");
        AdvancedModelRenderer neck1 = model.getCube("neck1");
        AdvancedModelRenderer neck2 = model.getCube("neck2");
        AdvancedModelRenderer neck3 = model.getCube("neck3");
        AdvancedModelRenderer neck4 = model.getCube("neck4");
        AdvancedModelRenderer head = model.getCube("Head");
        AdvancedModelRenderer jaw = model.getCube("down_jaw");
        AdvancedModelRenderer leftShin = model.getCube("Left shin");
        AdvancedModelRenderer rightShin = model.getCube("Right shin");
        AdvancedModelRenderer leftUpperFoot = model.getCube("Left upper foot");
        AdvancedModelRenderer leftFoot = model.getCube("Left foot");
        AdvancedModelRenderer rightUpperFoot = model.getCube("Right upper foot");
        AdvancedModelRenderer rightFoot = model.getCube("Right foot");
        AdvancedModelRenderer tail1 = model.getCube("tail1");
        AdvancedModelRenderer tail2 = model.getCube("tail2");
        AdvancedModelRenderer tail3 = model.getCube("tail3");
        AdvancedModelRenderer tail4 = model.getCube("tail4");
        AdvancedModelRenderer tail5 = model.getCube("tail5");
        AdvancedModelRenderer tail6 = model.getCube("tail6");
        AdvancedModelRenderer rightToe = model.getCube("Right toe");
        AdvancedModelRenderer leftToe = model.getCube("Left toe");

        AdvancedModelRenderer upperArmRight = model.getCube("Right arm");
        AdvancedModelRenderer upperArmLeft = model.getCube("Left arm");
        AdvancedModelRenderer lowerArmRight = model.getCube("Right forearm");
        AdvancedModelRenderer lowerArmLeft = model.getCube("Left forearm");
        AdvancedModelRenderer Hand_Right = model.getCube("Right hand");
        AdvancedModelRenderer Hand_Left = model.getCube("Left hand");

        AdvancedModelRenderer[] rightArmParts = new AdvancedModelRenderer[] { Hand_Right, lowerArmRight, upperArmRight };
        AdvancedModelRenderer[] leftArmParts = new AdvancedModelRenderer[] { Hand_Left, lowerArmLeft, upperArmLeft };
        AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelRenderer[] bodyParts = new AdvancedModelRenderer[] { waist, chest, shoulders, neck4, neck3, neck2, neck1, head };

        float globalSpeed = 1.0F;
        float globalDegree = 1.0F;

        model.bob(waist, globalSpeed * 0.5F, globalDegree * 1.0F, false, limbSwing, limbSwingAmount);
        model.bob(rightThigh, globalSpeed * 0.5F, globalDegree * 1.0F, false, limbSwing, limbSwingAmount);
        model.bob(leftThigh, globalSpeed * 0.5F, globalDegree * 1.0F, false, limbSwing, limbSwingAmount);
        model.walk(shoulders, 1F * globalSpeed, 0.2F, true, 1, 0, limbSwing, limbSwingAmount);
        model.walk(chest, 1F * globalSpeed, 0.2F, false, 0.5F, 0, limbSwing, limbSwingAmount);

        model.walk(leftThigh, 0.5F * globalSpeed, 0.7F, false, 3.14F, 0.2F, limbSwing, limbSwingAmount);
        model.walk(leftShin, 0.5F * globalSpeed, 0.6F, false, 1.5F, 0.3F, limbSwing, limbSwingAmount);
        model.walk(leftUpperFoot, 0.5F * globalSpeed, 0.8F, false, -1F, -0.1F, limbSwing, limbSwingAmount);
        model.walk(leftFoot, 0.5F * globalSpeed, 1.5F, true, -1F, 1F, limbSwing, limbSwingAmount);

        model.walk(rightThigh, 0.5F * globalSpeed, 0.7F, true, 3.14F, 0.2F, limbSwing, limbSwingAmount);
        model.walk(rightShin, 0.5F * globalSpeed, 0.6F, true, 1.5F, 0.3F, limbSwing, limbSwingAmount);
        model.walk(rightUpperFoot, 0.5F * globalSpeed, 0.8F, true, -1F, -0.1F, limbSwing, limbSwingAmount);
        model.walk(rightFoot, 0.5F * globalSpeed, 1.5F, false, -1F, 1F, limbSwing, limbSwingAmount);

        model.chainWave(tailParts, globalSpeed * 0.5F, globalDegree * 0.05F, 1, limbSwing, limbSwingAmount);
        model.chainSwing(tailParts, globalSpeed * 0.5F, globalDegree * 0.1F, 2, limbSwing, limbSwingAmount);
        model.chainWave(bodyParts, globalSpeed * 0.5F, globalDegree * 0.025F, 3, limbSwing, limbSwingAmount);

        model.chainWave(rightArmParts, 1F * globalSpeed, -0.3F, 4, limbSwing, limbSwingAmount);
        model.chainWave(leftArmParts, 1F * globalSpeed, -0.3F, 4, limbSwing, limbSwingAmount);

        model.chainWave(tailParts, 0.1F, 0.05F, 2, ticks, 0.25F);
        model.chainWave(bodyParts, 0.1F, -0.03F, 5, ticks, 0.25F);
        model.chainWave(rightArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);
        model.chainWave(leftArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);

        model.faceTarget(rotationYaw, rotationPitch, 2.0F, neck1, head);

        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
