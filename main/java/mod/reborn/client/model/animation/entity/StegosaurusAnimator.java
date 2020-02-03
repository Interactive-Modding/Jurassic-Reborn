package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.StegosaurusEntity;

@SideOnly(Side.CLIENT)
public class StegosaurusAnimator extends EntityAnimator<StegosaurusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, StegosaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer head = model.getCube("Head");

        AdvancedModelRenderer upperJaw = model.getCube("Upper Jaw");
        AdvancedModelRenderer lowerJaw = model.getCube("Lower Jaw");

        AdvancedModelRenderer neck1 = model.getCube("Neck");
        AdvancedModelRenderer neck2 = model.getCube("Neck 2");
        AdvancedModelRenderer neck3 = model.getCube("Neck 3");

        AdvancedModelRenderer tail1 = model.getCube("Tail 1");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
        AdvancedModelRenderer tail6 = model.getCube("Tail 6");

        AdvancedModelRenderer rearBody = model.getCube("Body REAR");
        AdvancedModelRenderer hips = model.getCube("Body hips");
        AdvancedModelRenderer bodyFrontBase = model.getCube("Body MAIN");
        AdvancedModelRenderer shoulders = model.getCube("Body shoulders");
        AdvancedModelRenderer bodyFront = model.getCube("Body FRONT");

        AdvancedModelRenderer thighRight = model.getCube("RearLeg Upper Right");
        AdvancedModelRenderer thighLeft = model.getCube("RearLeg Upper Left");

        AdvancedModelRenderer calfRight = model.getCube("RearLeg Middle Right");
        AdvancedModelRenderer calfLeft = model.getCube("RearLeg Middle Left");

        AdvancedModelRenderer footRight = model.getCube("RearLeg Foot Right");
        AdvancedModelRenderer footLeft = model.getCube("RearLeg Foot Left");

        AdvancedModelRenderer armUpperRight = model.getCube("FrontLeg Upper Right");
        AdvancedModelRenderer armUpperLeft = model.getCube("FrontLeg Upper Left");

        AdvancedModelRenderer armLowerRight = model.getCube("FrontLeg MID Right");
        AdvancedModelRenderer armLowerLeft = model.getCube("FrontLeg MID Left");

        AdvancedModelRenderer handRight = model.getCube("FrontLeg FOOT Right");
        AdvancedModelRenderer handLeft = model.getCube("FrontLeg FOOT Left");

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };

        float scaleFactor = 0.5F;
        float height = 0.8F;
        float frontOffset = -2F;

        model.bob(hips, 2 * scaleFactor, height, false, f, f1);
        model.bob(thighLeft, 2 * scaleFactor, height, false, f, f1);
        model.bob(thighRight, 2 * scaleFactor, height, false, f, f1);
        model.walk(hips, 2 * scaleFactor, 0.1F * height, true, -1.5F, 0F, f, f1);
        model.walk(neck1, 2 * scaleFactor, 0.07F * height, false, -0.5F, 0F, f, f1);
        model.walk(neck3, 2 * scaleFactor, 0.07F * height, false, 0.5F, 0F, f, f1);
        model.walk(head, 2 * scaleFactor, 0.07F * height, true, 1.5F, 0F, f, f1);

        model.walk(thighLeft, 1F * scaleFactor, 0.8F, false, 0F, 0F, f, f1);
        model.walk(calfLeft, 1F * scaleFactor, 0.8F, true, 1F, 0F, f, f1);
        model.walk(footLeft, 1F * scaleFactor, 0.8F, false, 1.5F, 0F, f, f1);

        model.walk(thighRight, 1F * scaleFactor, 0.8F, true, 0F, 0F, f, f1);
        model.walk(calfRight, 1F * scaleFactor, 0.8F, false, 1F, 0F, f, f1);
        model.walk(footRight, 1F * scaleFactor, 0.8F, true, 1.5F, 0F, f, f1);

        model.walk(armUpperRight, 1F * scaleFactor, 0.8F, true, frontOffset + 0F, -0.1F, f, f1);
        model.walk(armLowerRight, 1F * scaleFactor, 0.6F, true, frontOffset + 1F, -0.2F, f, f1);
        model.walk(handRight, 1F * scaleFactor, 0.8F, false, frontOffset + 1.5F, 0F, f, f1);

        model.walk(armUpperLeft, 1F * scaleFactor, 0.8F, false, frontOffset + 0F, -0.1F, f, f1);
        model.walk(armLowerLeft, 1F * scaleFactor, 0.6F, false, frontOffset + 1F, -0.2F, f, f1);
        model.walk(handLeft, 1F * scaleFactor, 0.8F, true, frontOffset + 1.5F, 0F, f, f1);

        model.walk(neck1, 0.1F, 0.04F, false, -1F, 0F, ticks, 1F);
        model.walk(head, 0.1F, 0.07F, true, 0F, 0F, ticks, 1F);
        model.walk(neck3, 0.1F, 0.03F, false, 0F, 0F, ticks, 1F);
        model.walk(hips, 0.1F, 0.025F, false, 0F, 0F, ticks, 1F);

        float inverseKinematicsConstant = 0.6F;
        model.walk(armUpperRight, 0.1F, 0.1F * inverseKinematicsConstant, false, 0F, 0F, ticks, 0.25F);
        model.walk(armLowerRight, 0.1F, 0.3F * inverseKinematicsConstant, true, 0F, 0F, ticks, 0.25F);
        model.walk(handRight, 0.1F, 0.175F * inverseKinematicsConstant, false, 0F, 0F, ticks, 0.25F);
        model.walk(armUpperLeft, 0.1F, 0.1F * inverseKinematicsConstant, false, 0F, 0F, ticks, 0.25F);
        model.walk(armLowerLeft, 0.1F, 0.3F * inverseKinematicsConstant, true, 0F, 0F, ticks, 0.25F);
        model.walk(handLeft, 0.1F, 0.175F * inverseKinematicsConstant, false, 0F, 0F, ticks, 0.25F);
        armUpperLeft.rotationPointZ -= 0.5 * Math.cos(ticks * 0.1F);
        armUpperRight.rotationPointZ -= 0.5 * Math.cos(ticks * 0.1F);

        model.chainSwing(tail, 0.1F, 0.2F, 3, ticks, 1.0F);
        model.chainWave(tail, 0.1F, -0.05F, 1, ticks, 1.0F);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
