package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.model.animation.EntityAnimator;
import mod.reborn.server.entity.dinosaur.StyracosaurusEntity;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class StyracosaurusAnimator extends EntityAnimator<StyracosaurusEntity> {

    protected void performAnimations(AnimatableModel parModel, StyracosaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
    	
    	AdvancedModelRenderer body1 = parModel.getCube("Body Hips");
        AdvancedModelRenderer rearLegLeft1 = parModel.getCube("RearLeg Upper Left");
        AdvancedModelRenderer rearLegRight1 = parModel.getCube("RearLeg Upper Right");
        AdvancedModelRenderer frontLegLeft1 = parModel.getCube("FrontLeg Upper Left");
        AdvancedModelRenderer frontLegRight1 = parModel.getCube("FrontLeg Upper Right");
        AdvancedModelRenderer upperJaw = parModel.getCube("Jaw UPPER 1");
        AdvancedModelRenderer lowerJaw = parModel.getCube("Jaw LOWER");
        AdvancedModelRenderer head = parModel.getCube("Head");
        AdvancedModelRenderer tail1 = parModel.getCube("Tail 1");

        boolean isIdle = !(f1 > 0.2F);
        float defaultUpperJawRotationX = upperJaw.rotateAngleX;
        float defaultLowerJawRotationX = lowerJaw.rotateAngleX;
        float defaultHeadRotationX = head.rotateAngleX;
        float defaultTailRotationX = tail1.rotateAngleX;
    	parModel.walk(body1, 0.1F, 0.1F, false, 0.05F, 0.2F, ticks + f, 0.25F + f1);


        if(entity.getAnimation() == EntityAnimation.CALLING.get()) {
            upperJaw.rotateAngleX -= 0.25f;
            lowerJaw.rotateAngleX += 0.25f;
        } else {
            upperJaw.rotateAngleX = defaultUpperJawRotationX;
            lowerJaw.rotateAngleX = defaultLowerJawRotationX;
        }

        if(entity.getAnimation() == EntityAnimation.ATTACKING.get() || entity.getAnimation() == EntityAnimation.EATING.get()) {
            parModel.walk(upperJaw, 0.1F, 0.2F, true, 0.05F, 0.2F, f * 2, f1 * 2);
            parModel.walk(lowerJaw, 0.1F, 0.2F, false, 0.05F, 0.2F, f * 2, f1 * 2);
        }

        if(entity.getAnimation() == EntityAnimation.DRINKING.get()) {
            upperJaw.rotateAngleX += 0.001f;
            lowerJaw.rotateAngleX -= 0.001f;
            head.rotateAngleX -= 0.03f;
        } else {
            upperJaw.rotateAngleX = defaultUpperJawRotationX;
            lowerJaw.rotateAngleX = defaultLowerJawRotationX;
            head.rotateAngleX = defaultHeadRotationX;
        } else {
            if(!isIdle) {
                parModel.walk(rearLegLeft1, 0.32F, 0.5F, false, 0.05F, 0.2F, f, f1);
                parModel.walk(rearLegRight1, 0.32F, 0.5F, true, 0.05F, 0.2F, f, f1);
                parModel.walk(frontLegLeft1, 0.32F, 0.5F, false, 0.05F, 0.2F, f, f1);
                parModel.walk(frontLegRight1, 0.32F, 0.5F, true, 0.05F, 0.2F, f, f1);
            } else {
                parModel.walk(rearLegLeft1, 0.1F, 0.1F, true, 0.05F, 0.2F, ticks + f, 0.25F + f1);
                parModel.walk(rearLegRight1, 0.1F, 0.1F, true, 0.05F, 0.2F, ticks + f, 0.25F + f1);
                parModel.walk(frontLegLeft1, 0.1F, 0.1F, true, 0.05F, 0.2F, ticks + f, 0.25F + f1);
                parModel.walk(frontLegRight1, 0.1F, 0.1F, true, 0.05F, 0.2F, ticks + f, 0.25F + f1);
            }
        }

        if(entity.getAnimation() == EntityAnimation.LAYING_EGG.get()) {
            tail1.rotateAngleX += 0.15f;
        } else {
            tail1.rotateAngleX = defaultTailRotationX;
        }
    }

}
