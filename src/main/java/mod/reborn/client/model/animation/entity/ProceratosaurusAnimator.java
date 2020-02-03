package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import mod.reborn.server.entity.dinosaur.ProceratosaurusEntity;

public class ProceratosaurusAnimator extends EntityAnimator<ProceratosaurusEntity> {

    protected void performAnimations(AnimatableModel parModel, ProceratosaurusEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {

        AdvancedModelRenderer body1 = parModel.getCube("body1");
        AdvancedModelRenderer body2 = parModel.getCube("body2");
        AdvancedModelRenderer tail1 = parModel.getCube("tail1");
        AdvancedModelRenderer tail2 = parModel.getCube("tail2");
        AdvancedModelRenderer tail3 = parModel.getCube("tail3");
        AdvancedModelRenderer tail4 = parModel.getCube("tail4");
        AdvancedModelRenderer tail5 = parModel.getCube("tail5");
        AdvancedModelRenderer leftArm1 = parModel.getCube("arm2");
        AdvancedModelRenderer leftArm2 = parModel.getCube("forearm2");
        AdvancedModelRenderer rightArm1 = parModel.getCube("arm1");
        AdvancedModelRenderer rightArm2 = parModel.getCube("forearm1");
        AdvancedModelRenderer leftLeg1 = parModel.getCube("thigh2");
        AdvancedModelRenderer rightLeg1 = parModel.getCube("thigh1");
        AdvancedModelRenderer neck1 = parModel.getCube("neck1");
        AdvancedModelRenderer head = parModel.getCube("head");

        AdvancedModelRenderer[] tail = {tail5, tail4, tail3, tail2, tail1};
        AdvancedModelRenderer[] leftArm = {leftArm2, leftArm1};
        AdvancedModelRenderer[] rightArm = {rightArm2, rightArm1};
        AdvancedModelRenderer[] body = {body2, body1, neck1, head};

        float globalSpeed = 1.0F;
        float globalDegree = 1.0F;

        parModel.bob(body1,  globalSpeed * 0.1F,  globalDegree * 0.3F, false, limbSwing, limbSwingAmount);
        parModel.walk(rightLeg1, globalSpeed * 0.8F, globalDegree * 0.5F, false, 0, 0.2f, limbSwing, limbSwingAmount);
        parModel.walk(leftLeg1, globalSpeed * 0.8F, globalDegree * 0.5F, true, 0,0.2f, limbSwing, limbSwingAmount);

        parModel.chainWave(tail, 0.13F,0.15F,2, limbSwing + ticks, limbSwingAmount + 0.25F);
        parModel.chainWave(body, 0.1F, -0.1F, 5, ticks, 0.25F);
        parModel.chainWave(rightArm, 0.1F, -0.1F, 4, ticks, 0.25F);
        parModel.chainWave(leftArm, 0.1F, -0.1F, 4, ticks, 0.25F);

        parModel.faceTarget(rotationYaw, rotationPitch, 2.0F, neck1, head);
        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
