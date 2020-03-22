package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import mod.reborn.server.entity.dinosaur.AlvarezsaurusEntity;
import net.minecraft.client.renderer.GlStateManager;

public class AlvarezsaurusAnimator extends EntityAnimator<AlvarezsaurusEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, AlvarezsaurusEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelRenderer body1 = model.getCube("Body ALL");
        AdvancedModelRenderer tail1 = model.getCube("Tail Base");
        AdvancedModelRenderer tail2 = model.getCube("Tail Mid 1");
        AdvancedModelRenderer tail3 = model.getCube("Tail Mid 2");
        AdvancedModelRenderer tail4 = model.getCube("Tail Tip");
        AdvancedModelRenderer leftArm1 = model.getCube("Arm Top Left");
        AdvancedModelRenderer leftArm2 = model.getCube("Arm left 2");
        AdvancedModelRenderer rightArm1 = model.getCube("Arm Top Right");
        AdvancedModelRenderer rightArm2 = model.getCube("Arm right 2");
        AdvancedModelRenderer leftLeg1 = model.getCube("Leg Top Left");
        AdvancedModelRenderer rightLeg1 = model.getCube("Leg Top Right");
        AdvancedModelRenderer leftLeg2 = model.getCube("Leg Mid Left");
        AdvancedModelRenderer rightLeg2 = model.getCube("Leg Mid Right");
        AdvancedModelRenderer leftLeg3 = model.getCube("Leg Bot Left");
        AdvancedModelRenderer rightLeg3 = model.getCube("Leg Bot Right");
        AdvancedModelRenderer leftLeg4 = model.getCube("Foot Left");
        AdvancedModelRenderer rightLeg4 = model.getCube("Foot Right");
        AdvancedModelRenderer neck1 = model.getCube("Neck Base");
        AdvancedModelRenderer neck2 = model.getCube("Neck Mid 1");
        AdvancedModelRenderer neck3 = model.getCube("Neck Mid 2");
        AdvancedModelRenderer neck4 = model.getCube("Throat Base");
        AdvancedModelRenderer head = model.getCube("Upper Head");
        AdvancedModelRenderer upperJaw = model.getCube("Snout");
        AdvancedModelRenderer lowerJaw = model.getCube("Mouthpiece");

        AdvancedModelRenderer[] tail = {tail4, tail3, tail2, tail1};
        AdvancedModelRenderer[] leftArm = {leftArm2, leftArm1};
        AdvancedModelRenderer[] rightArm = {rightArm2, rightArm1};
        AdvancedModelRenderer[] body = {body1, neck1, head};
        AdvancedModelRenderer[] neck = {neck1, neck2, neck3, neck4};

        model.bob(body1, globalSpeed * 1F, globalHeight * 0.8F, false, f, f1);

        model.bob(leftLeg1, globalSpeed * 1F, globalHeight * 0.8F, false, f, f1);
        model.bob(rightLeg1, globalSpeed * 1F, globalHeight * 0.8F, false, f, f1);

        entity.tailBuffer.applyChainSwingBuffer(tail);


        float globalSpeed = 0.6F;
        float globalHeight = 1F * fl;
        float globalDegree = 1.0F;

        float defaultUpperJawRotationX = upperJaw.rotateAngleX;
        float defaultLowerJawRotationX = lowerJaw.rotateAngleX;
        float defaultHeadRotationX = head.rotateAngleX;
        float defaultTailRotationX = tail1.rotateAngleX;

    }

}
