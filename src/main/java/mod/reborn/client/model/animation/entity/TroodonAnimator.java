package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.TroodonEntity;

@SideOnly(Side.CLIENT)
public class TroodonAnimator extends EntityAnimator<TroodonEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, TroodonEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer head = model.getCube("head UPPER");
        AdvancedModelRenderer neck1 = model.getCube("neck1");
        AdvancedModelRenderer neck2 = model.getCube("neck2");
        AdvancedModelRenderer neck3 = model.getCube("neck3");
        AdvancedModelRenderer neck4 = model.getCube("neck4");

        AdvancedModelRenderer lowerJaw = model.getCube("Jaw LOWER");

        AdvancedModelRenderer waist = model.getCube("body3");
        AdvancedModelRenderer chest = model.getCube("body2");
        AdvancedModelRenderer shoulders = model.getCube("body1");

        AdvancedModelRenderer tail1 = model.getCube("tail1");
        AdvancedModelRenderer tail2 = model.getCube("tail2");
        AdvancedModelRenderer tail3 = model.getCube("tail3");
        AdvancedModelRenderer tail4 = model.getCube("tail4");
        AdvancedModelRenderer tail5 = model.getCube("tail5");

        AdvancedModelRenderer upperArmR = model.getCube("Right arm");
        AdvancedModelRenderer upperArmL = model.getCube("Left arm");

        AdvancedModelRenderer lowerArmR = model.getCube("Right forearm");
        AdvancedModelRenderer lowerArmL = model.getCube("Left forearm");

        AdvancedModelRenderer handR = model.getCube("Right hand");
        AdvancedModelRenderer handL = model.getCube("Left hand");

        AdvancedModelRenderer thighR = model.getCube("Right thigh");
        AdvancedModelRenderer thighL = model.getCube("Left thigh");

        AdvancedModelRenderer lowerThighR = model.getCube("Right shin");
        AdvancedModelRenderer lowerThighL = model.getCube("Left shin");

        AdvancedModelRenderer upperFootR = model.getCube("Right upper foot");
        AdvancedModelRenderer upperFootL = model.getCube("Left upper foot");

        AdvancedModelRenderer footR = model.getCube("Right foot");
        AdvancedModelRenderer footL = model.getCube("Left foot");

        AdvancedModelRenderer[] rightArmParts = new AdvancedModelRenderer[] { handR, lowerArmR, upperArmR };
        AdvancedModelRenderer[] leftArmParts = new AdvancedModelRenderer[] { handL, lowerArmL, upperArmL };
        AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[] { tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelRenderer[] bodyParts = new AdvancedModelRenderer[] { waist, chest, shoulders, neck4, neck3, neck2, neck1, head };

        float globalSpeed = 1.0F;
        float globalHeight = 2F * f1;

        model.bob(thighL, 1F * globalSpeed, 0.3f * globalHeight, false, f, f1);
        model.bob(thighR, 1F * globalSpeed, 0.3f * globalHeight, false, f, f1);
        model.walk(shoulders, 1F * globalSpeed, 0.2F, true, 1, 0, f, f1);
        model.walk(chest, 1F * globalSpeed, 0.2F, false, 0.5F, 0, f, f1);

        model.walk(thighL, 0.5F * globalSpeed, 0.7F, false, 3.14F, 0.2F, f, f1);
        model.walk(lowerThighL, 0.5F * globalSpeed, 0.1F, false, 1.5F, 0.3F, f, f1);
        model.walk(upperFootL, 0.5F * globalSpeed, 0.8F, false, -1F, -0.1F, f, f1);
        model.walk(footL, 0.5F * globalSpeed, 1.5F, true, -1F, 1F, f, f1);

        model.walk(thighR, 0.5F * globalSpeed, 0.7F, true, 3.14F, 0.2F, f, f1);
        model.walk(lowerThighR, 0.5F * globalSpeed, 0.1F, true, 1.5F, 0.3F, f, f1);
        model.walk(upperFootR, 0.5F * globalSpeed, 0.8F, true, -1F, -0.1F, f, f1);
        model.walk(footR, 0.5F * globalSpeed, 1.5F, false, -1F, 1F, f, f1);


        model.chainSwing(tailParts, 0.5F * globalSpeed, -0.1F, 2, f, f1);
        model.chainWave(tailParts, 1F * globalSpeed, -0.1F, 2.5F, f, f1);
        model.chainWave(bodyParts, 1F * globalSpeed, -0.1F, 4, f, f1);

        model.chainWave(rightArmParts, 1F * globalSpeed, -0.3F, 4, f, f1);
        model.chainWave(leftArmParts, 1F * globalSpeed, -0.3F, 4, f, f1);

        model.chainWave(tailParts, 0.2F, 0.05F, 2, ticks, 0.25F);
        model.chainWave(bodyParts, 0.2F, -0.03F, 5, ticks, 0.25F);
        model.chainWave(rightArmParts, 0.2F, -0.1F, 4, ticks, 0.25F);
        model.chainWave(leftArmParts, 0.2F, -0.1F, 4, ticks, 0.25F);

        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
