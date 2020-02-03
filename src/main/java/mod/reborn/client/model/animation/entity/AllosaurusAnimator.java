package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import mod.reborn.server.entity.dinosaur.AllosaurusEntity;
import net.minecraft.client.renderer.GlStateManager;

public class AllosaurusAnimator extends EntityAnimator<AllosaurusEntity> {

    protected void performAnimations(AnimatableModel parModel, AllosaurusEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelRenderer body1 = parModel.getCube("Body");
        AdvancedModelRenderer body2 = parModel.getCube("Waist");
        AdvancedModelRenderer tail1 = parModel.getCube("Tail1");
        AdvancedModelRenderer tail2 = parModel.getCube("Tail2");
        AdvancedModelRenderer tail3 = parModel.getCube("Tail3");
        AdvancedModelRenderer tail4 = parModel.getCube("Tail4");
        AdvancedModelRenderer tail5 = parModel.getCube("Tail 5");
        AdvancedModelRenderer tail6 = parModel.getCube("Tail 6");
        AdvancedModelRenderer leftArm1 = parModel.getCube("Upper Arm Left");
        AdvancedModelRenderer leftArm2 = parModel.getCube("Lower Arm Left");
        AdvancedModelRenderer rightArm1 = parModel.getCube("Upper Arm Right");
        AdvancedModelRenderer rightArm2 = parModel.getCube("Lower Arm Right");
        AdvancedModelRenderer leftLeg1 = parModel.getCube("Left Tigh");
        AdvancedModelRenderer rightLeg1 = parModel.getCube("Right Tigh");
        AdvancedModelRenderer leftLeg2 = parModel.getCube("Left Calf 1");
        AdvancedModelRenderer rightLeg2 = parModel.getCube("Right Calf 1");
        AdvancedModelRenderer leftLeg3 = parModel.getCube("Left Calf 2");
        AdvancedModelRenderer rightLeg3 = parModel.getCube("Right Calf 2");
        AdvancedModelRenderer leftLeg4 = parModel.getCube("Foot Left");
        AdvancedModelRenderer rightLeg4 = parModel.getCube("Foot Right");
        AdvancedModelRenderer neck1 = parModel.getCube("Neck 1");
        AdvancedModelRenderer neck2 = parModel.getCube("Neck 2");
        AdvancedModelRenderer neck3 = parModel.getCube("Neck 3");
        AdvancedModelRenderer neck4 = parModel.getCube("Neck 4");
        AdvancedModelRenderer head = parModel.getCube("Head");
        AdvancedModelRenderer upperJaw = parModel.getCube("Upper Jaw 1");
        AdvancedModelRenderer lowerJaw = parModel.getCube("Lower Jaw");

        AdvancedModelRenderer[] tail = {tail5, tail4, tail3, tail2, tail1};
        AdvancedModelRenderer[] leftArm = {leftArm2, leftArm1};
        AdvancedModelRenderer[] rightArm = {rightArm2, rightArm1};
        AdvancedModelRenderer[] body = {body2, body1, neck1, head};
        AdvancedModelRenderer[] neck = {neck1, neck2, neck3, neck4};

        float globalSpeed = 0.6F;
        float globalDegree = 1.0F;

        float defaultUpperJawRotationX = upperJaw.rotateAngleX;
        float defaultLowerJawRotationX = lowerJaw.rotateAngleX;
        float defaultHeadRotationX = head.rotateAngleX;
        float defaultTailRotationX = tail1.rotateAngleX;

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
