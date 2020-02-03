package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.model.animation.EntityAnimator;
import mod.reborn.server.entity.dinosaur.CeratosaurusEntity;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class CeratosaurusAnimator extends EntityAnimator<CeratosaurusEntity> {

    protected void performAnimations(AnimatableModel parModel, CeratosaurusEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
    	
        AdvancedModelRenderer body1 = parModel.getCube("Body REAR");
        AdvancedModelRenderer body2 = parModel.getCube("Body MID");
        AdvancedModelRenderer tail1 = parModel.getCube("Tail BASE");
        AdvancedModelRenderer tail2 = parModel.getCube("Tail 2");
        AdvancedModelRenderer tail3 = parModel.getCube("Tail 3");
        AdvancedModelRenderer tail4 = parModel.getCube("Tail 4");
        AdvancedModelRenderer tail5 = parModel.getCube("Tail 6");
        AdvancedModelRenderer leftArm1 = parModel.getCube("Left arm");
        AdvancedModelRenderer leftArm2 = parModel.getCube("Left forearm");
        AdvancedModelRenderer rightArm1 = parModel.getCube("Right arm");
        AdvancedModelRenderer rightArm2 = parModel.getCube("Right forearm");
        AdvancedModelRenderer leftLeg1 = parModel.getCube("Leg TOP LEFT");
        AdvancedModelRenderer rightLeg1 = parModel.getCube("Leg TOP RIGHT");
        AdvancedModelRenderer leftLeg2 = parModel.getCube("Leg MID LEFT");
        AdvancedModelRenderer rightLeg2 = parModel.getCube("Leg MID RIGHT");
        AdvancedModelRenderer leftLeg3 = parModel.getCube("Leg BOT LEFT");
        AdvancedModelRenderer rightLeg3 = parModel.getCube("Leg BOT RIGHT");
        AdvancedModelRenderer leftLeg4 = parModel.getCube("Foot LEFT");
        AdvancedModelRenderer rightLeg4 = parModel.getCube("Foot RIGHT");
        AdvancedModelRenderer neck1 = parModel.getCube("Neck 1");
        AdvancedModelRenderer neck2 = parModel.getCube("Neck 2");
        AdvancedModelRenderer neck3 = parModel.getCube("Neck 3");
        AdvancedModelRenderer neck4 = parModel.getCube("Neck 4");
        AdvancedModelRenderer head = parModel.getCube("Head");
        AdvancedModelRenderer upperJaw = parModel.getCube("Jaw UPPER");
        AdvancedModelRenderer lowerJaw = parModel.getCube("Jaw LOWER");

        AdvancedModelRenderer[] tail = {tail5, tail4, tail3, tail2, tail1};
        AdvancedModelRenderer[] leftArm = {leftArm2, leftArm1};
        AdvancedModelRenderer[] rightArm = {rightArm2, rightArm1};
        AdvancedModelRenderer[] body = {body2, body1, neck1, head};
        AdvancedModelRenderer[] neck = {neck1, neck2, neck3, neck4};

        parModel.faceTarget(rotationYaw, rotationPitch, 2.0F, neck1, head);
        entity.tailBuffer.applyChainSwingBuffer(tail);

        float globalSpeed = 0.6F;
        float globalDegree = 1.0F;

        float defaultUpperJawRotationX = upperJaw.rotateAngleX;
        float defaultLowerJawRotationX = lowerJaw.rotateAngleX;
        float defaultHeadRotationX = head.rotateAngleX;
        float defaultTailRotationX = tail1.rotateAngleX;

        if(entity.getAnimation() == EntityAnimation.CALLING.get()) {
            upperJaw.rotateAngleX -= 0.25f;
            lowerJaw.rotateAngleX += 0.25f;
        } else {
            upperJaw.rotateAngleX = defaultUpperJawRotationX;
            lowerJaw.rotateAngleX = defaultLowerJawRotationX;
        }

        if(entity.getAnimation() == EntityAnimation.ATTACKING.get() || entity.getAnimation() == EntityAnimation.EATING.get()) {
            parModel.walk(upperJaw, 0.1F * globalSpeed, 0.2F * globalDegree, true, 0.05F, 0.2F, limbSwing * 2, limbSwingAmount * 2);
            parModel.walk(lowerJaw, 0.1F * globalSpeed, 0.2F * globalDegree, false, 0.05F, 0.2F, limbSwing * 2, limbSwingAmount * 2);
        }

        if(entity.getAnimation() == EntityAnimation.DRINKING.get()) {
            upperJaw.rotateAngleX += 0.001f;
            lowerJaw.rotateAngleX -= 0.001f;
            head.rotateAngleX -= 0.03f;
        } else {
            upperJaw.rotateAngleX = defaultUpperJawRotationX;
            lowerJaw.rotateAngleX = defaultLowerJawRotationX;
            head.rotateAngleX = defaultHeadRotationX;
        }

        if(entity.getAnimation() == EntityAnimation.DYING.get()) {
            upperJaw.rotateAngleX += 0.25f;
            lowerJaw.rotateAngleX -= 0.25f;
            GlStateManager.rotate(45, 0, 0, 1f);
            GlStateManager.translate(0, 1.5f, 0);
        } else {
            parModel.bob(body1,  globalSpeed * 0.15F,  globalDegree * 0.5F, false, limbSwing + 0.03f, limbSwingAmount + 0.03f);
            parModel.walk(rightLeg1, globalSpeed * 0.8F, globalDegree * 0.8F, false, 0, 0.2f, limbSwing, limbSwingAmount);
            parModel.walk(leftLeg1, globalSpeed * 0.8F, globalDegree * 0.8F, true, 0,0.2f, limbSwing, limbSwingAmount);

            parModel.chainWave(tail, 0.13F,0.15F,2, limbSwing + ticks, limbSwingAmount + (entity.getAnimation() == EntityAnimation.LAYING_EGG.get()?0.25F:0));
            parModel.chainWave(body, 0.1F, -0.1F, 5, ticks, 0.25F);
            parModel.chainWave(rightArm, 0.1F, -0.1F, 4, ticks, 0.25F);
            parModel.chainWave(leftArm, 0.1F, -0.1F, 4, ticks, 0.25F);
        }

        if(entity.getAnimation() == EntityAnimation.LAYING_EGG.get()) {
            tail1.rotateAngleX += 0.15f;
        } else {
            tail1.rotateAngleX = defaultTailRotationX;
        }
    }
}
