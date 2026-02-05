package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.ProceratosaurusEntity;

public class ProceratosaurusAnimator extends EntityAnimator<ProceratosaurusEntity> {

    protected void performAnimations(AnimatableModel parModel, ProceratosaurusEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {

        AdvancedModelBox body1 = parModel.getCube("body1");
        AdvancedModelBox body2 = parModel.getCube("body2");
        AdvancedModelBox tail1 = parModel.getCube("tail1");
        AdvancedModelBox tail2 = parModel.getCube("tail2");
        AdvancedModelBox tail3 = parModel.getCube("tail3");
        AdvancedModelBox tail4 = parModel.getCube("tail4");
        AdvancedModelBox tail5 = parModel.getCube("tail5");
        AdvancedModelBox leftArm1 = parModel.getCube("arm2");
        AdvancedModelBox leftArm2 = parModel.getCube("forearm2");
        AdvancedModelBox rightArm1 = parModel.getCube("arm1");
        AdvancedModelBox rightArm2 = parModel.getCube("forearm1");
        AdvancedModelBox leftLeg1 = parModel.getCube("thigh2");
        AdvancedModelBox rightLeg1 = parModel.getCube("thigh1");
        AdvancedModelBox neck1 = parModel.getCube("neck1");
        AdvancedModelBox head = parModel.getCube("head");

        AdvancedModelBox[] tail = {tail5, tail4, tail3, tail2, tail1};
        AdvancedModelBox[] leftArm = {leftArm2, leftArm1};
        AdvancedModelBox[] rightArm = {rightArm2, rightArm1};
        AdvancedModelBox[] body = {body2, body1, neck1, head};

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
