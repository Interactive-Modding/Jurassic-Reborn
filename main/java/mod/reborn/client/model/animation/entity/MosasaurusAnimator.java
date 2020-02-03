package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;

import mod.reborn.server.entity.dinosaur.MosasaurusEntity;
import net.minecraft.client.renderer.GlStateManager;

public class MosasaurusAnimator extends EntityAnimator<MosasaurusEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, MosasaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer body = model.getCube("Body");
        AdvancedModelRenderer head = model.getCube("Head");
        AdvancedModelRenderer neck1 = model.getCube("Neck1");
        AdvancedModelRenderer neck2 = model.getCube("Neck2");
        AdvancedModelRenderer neck3 = model.getCube("Neck3");
        AdvancedModelRenderer rightFin1 = model.getCube("Fin Right 1");
        AdvancedModelRenderer rightFin2 = model.getCube("Fin Right 2");
        AdvancedModelRenderer leftFin1 = model.getCube("Fin Left 1");
        AdvancedModelRenderer leftFin2 = model.getCube("Fin Left 2");
        AdvancedModelRenderer fin1 = model.getCube("Fin 1");
        AdvancedModelRenderer fin2 = model.getCube("Fin 2");
        AdvancedModelRenderer membraneFin = model.getCube("Fin membrane");
        AdvancedModelRenderer tail1 = model.getCube("Tail1");
        AdvancedModelRenderer tail2 = model.getCube("Tail2");
        AdvancedModelRenderer tail3 = model.getCube("Tail3");
        AdvancedModelRenderer tail4 = model.getCube("Tail4");
        AdvancedModelRenderer tail5 = model.getCube("Tail5");
        AdvancedModelRenderer tail6 = model.getCube("Tail6");
        AdvancedModelRenderer tail7 = model.getCube("Tail7");
        AdvancedModelRenderer tail8 = model.getCube("Tail8");
        AdvancedModelRenderer upperJaw = model.getCube("Upper_Jaw1");
        AdvancedModelRenderer lowerJaw = model.getCube("Lower Jaw1");
        AdvancedModelRenderer waist = model.getCube("Waist");
        AdvancedModelRenderer teeth = model.getCube("Teeth 1");
        AdvancedModelRenderer[] bodyParts = {body, neck3, neck2, neck1, head};
        AdvancedModelRenderer[] tail = {tail8, tail7, tail6, tail5, tail4, tail3, tail2, tail1};


        float defaultUpperJawRotationX = upperJaw.rotateAngleX;
        float defaultLowerJawRotationX = lowerJaw.rotateAngleX;
        float defaultTail1RotationX = tail1.rotateAngleX;
        float defaultTail2RotationX = tail2.rotateAngleX;
        float defaultTail3RotationX = tail3.rotateAngleX;
        float defaultTail4RotationX = tail4.rotateAngleX;
        float defaultTail5RotationX = tail5.rotateAngleX;
        float defaultTail6RotationX = tail6.rotateAngleX;
        float defaultTail1OffsetZ = tail1.offsetZ;
        float defaultTail1OffsetY = tail1.offsetY;
        float defaultTeethRotationX = teeth.rotateAngleX;

        if(entity.inWater() || entity.getAnimation() != EntityAnimation.DYING.get()) {
            model.chainWave(bodyParts, 0.03F, 0.03F, 0.05, f, f1);
        }
        if(entity.getAnimation() == EntityAnimation.CALLING.get()) {
            upperJaw.rotateAngleX -= 0.24;
            teeth.rotateAngleX -= 0.24;
            lowerJaw.rotateAngleX += 0.24;
        } else {
            upperJaw.rotateAngleX = defaultUpperJawRotationX;
            teeth.rotateAngleX = defaultTeethRotationX;
            lowerJaw.rotateAngleX = defaultLowerJawRotationX;
        }
        if(entity.getAnimation() == EntityAnimation.EATING.get() || entity.getAnimation() == EntityAnimation.ATTACKING.get()){
            model.walk(upperJaw, 0.1F, 0.2F, true, 0.05F, 0.2F, f * 2, f1 * 2);
            model.walk(teeth, 0.1F, 0.2F, true, 0.05F, 0.2F, f * 2, f1 * 2);
            model.walk(lowerJaw, 0.1F, 0.2F, false, 0.05F, 0.2F, f * 2, f1 * 2);
        }
        if(entity.getAnimation() == EntityAnimation.LAYING_EGG.get()){
            tail1.rotateAngleX += 0.3f;
            tail1.offsetZ -= 0.15f;
            tail1.offsetY += 0.06f;
            tail2.rotateAngleX += 0.1f;
            tail3.rotateAngleX += 0.1f;
            tail4.rotateAngleX += 0.1f;
            tail5.rotateAngleX += 0.1f;
            tail6.rotateAngleX += 0.1f;
        } else {
            tail1.rotateAngleX = defaultTail1RotationX;
            tail1.offsetZ = defaultTail1OffsetZ;
            tail1.offsetY = defaultTail1OffsetY;
            tail2.rotateAngleX = defaultTail2RotationX;
            tail3.rotateAngleX = defaultTail3RotationX;
            tail4.rotateAngleX = defaultTail4RotationX;
            tail5.rotateAngleX = defaultTail5RotationX;
            tail6.rotateAngleX = defaultTail6RotationX;
        }if(entity.isInWater()) {
            model.flap(rightFin1, 0.1F, 0.2F, false, 0.05F, 0.2F, f, f1);
            model.flap(leftFin1, 0.1F, 0.2F, true, 0.05F, 0.2F, f, f1);
            model.flap(rightFin2, 0.1F, 0.2F, false, 0.05F, 0.2F, f, f1);
            model.flap(leftFin2, 0.1F, 0.2F, true, 0.05F, 0.2F, f, f1);
            model.flap(fin1, 0.1F, 0.2F, false, 0.05F, 0.2F, f, f1);
            model.flap(fin2, 0.1F, 0.2F, false, 0.05F, 0.2F, f, f1);
            model.flap(membraneFin, 0.1F, 0.2F, false, 0.05F, 0.2F, f, f1);
            model.chainSwing(tail, 0.08F, 0.08F, 0.05, f, f1);
    }}
}
