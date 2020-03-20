package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.AchillobatorEntity;

@SideOnly(Side.CLIENT)
public class AchillobatorAnimator extends EntityAnimator<AchillobatorEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, AchillobatorEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        float speed = 0.75F;
        float height = 2F * f1;

        AdvancedModelRenderer waist = model.getCube("body3");
        AdvancedModelRenderer chest = model.getCube("body2");
        AdvancedModelRenderer shoulders = model.getCube("body1");
        AdvancedModelRenderer leftThigh = model.getCube("Left thigh");
        AdvancedModelRenderer rightThigh = model.getCube("Right thigh");
        AdvancedModelRenderer neck1 = model.getCube("neck1");
        AdvancedModelRenderer neck2 = model.getCube("neck2");
        AdvancedModelRenderer neck3 = model.getCube("neck3");
        AdvancedModelRenderer neck4 = model.getCube("neck4");
        AdvancedModelRenderer head = model.getCube("head");
        AdvancedModelRenderer leftShin = model.getCube("Left shin");
        AdvancedModelRenderer rightShin = model.getCube("Right shin");
        AdvancedModelRenderer leftUpperFoot = model.getCube("Left upper foot");
        AdvancedModelRenderer leftFoot = model.getCube("Left foot");
        AdvancedModelRenderer rightUpperFoot = model.getCube("Right upper foot");
        AdvancedModelRenderer rightFoot = model.getCube("Right foot");
        AdvancedModelRenderer upperArmRight = model.getCube("Right arm");
        AdvancedModelRenderer upperArmLeft = model.getCube("Left arm");
        AdvancedModelRenderer tail1 = model.getCube("tail1");
        AdvancedModelRenderer tail2 = model.getCube("tail2");
        AdvancedModelRenderer tail3 = model.getCube("tail3");
        AdvancedModelRenderer tail4 = model.getCube("tail4");
        AdvancedModelRenderer tail5 = model.getCube("tail5");
        AdvancedModelRenderer tail6 = model.getCube("tail6");


        AdvancedModelRenderer Lower_Arm_Right = model.getCube("Right arm");
        AdvancedModelRenderer Lower_Arm_Left = model.getCube("Left arm");
        AdvancedModelRenderer Hand_Right = model.getCube("Right hand");
        AdvancedModelRenderer Hand_Left = model.getCube("Left hand");

        AdvancedModelRenderer[] rightArmParts = new AdvancedModelRenderer[] { Hand_Right, Lower_Arm_Right, upperArmRight };
        AdvancedModelRenderer[] leftArmParts = new AdvancedModelRenderer[] { Hand_Left, Lower_Arm_Left, upperArmLeft };
        AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelRenderer[] bodyParts = new AdvancedModelRenderer[] { waist, chest, shoulders, neck4, neck3, neck2, neck1, head };


        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
