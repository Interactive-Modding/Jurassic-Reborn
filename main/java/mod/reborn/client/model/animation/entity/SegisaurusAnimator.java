package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.SegisaurusEntity;

@SideOnly(Side.CLIENT)
public class SegisaurusAnimator extends EntityAnimator<SegisaurusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, SegisaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer head = model.getCube("head");
        AdvancedModelRenderer neck1 = model.getCube("neck1");
        AdvancedModelRenderer neck2 = model.getCube("neck2");
        AdvancedModelRenderer neck3 = model.getCube("neck3");
        AdvancedModelRenderer neck4 = model.getCube("neck4");

        AdvancedModelRenderer lowerJaw = model.getCube("down_jaw");

        AdvancedModelRenderer waist = model.getCube("body3");
        AdvancedModelRenderer chest = model.getCube("body2");
        AdvancedModelRenderer shoulders = model.getCube("body1");

        AdvancedModelRenderer tail1 = model.getCube("tail1");
        AdvancedModelRenderer tail2 = model.getCube("tail2");
        AdvancedModelRenderer tail3 = model.getCube("tail3");
        AdvancedModelRenderer tail4 = model.getCube("tail4");
        AdvancedModelRenderer tail5 = model.getCube("tail5");

        AdvancedModelRenderer upperArmR = model.getCube("arm1");
        AdvancedModelRenderer upperArmL = model.getCube("arm2");

        AdvancedModelRenderer lowerArmR = model.getCube("forearm1");
        AdvancedModelRenderer lowerArmL = model.getCube("forearm2");

        AdvancedModelRenderer handR = model.getCube("hand1");
        AdvancedModelRenderer handL = model.getCube("hand2");

        AdvancedModelRenderer thighR = model.getCube("thigh1");
        AdvancedModelRenderer thighL = model.getCube("thigh2");

        AdvancedModelRenderer lowerThighR = model.getCube("leg1");
        AdvancedModelRenderer lowerThighL = model.getCube("leg2");

        AdvancedModelRenderer upperFootR = model.getCube("upperfoot1");
        AdvancedModelRenderer upperFootL = model.getCube("upperfoot2");

        AdvancedModelRenderer footR = model.getCube("foot1");
        AdvancedModelRenderer footL = model.getCube("foot2");

        AdvancedModelRenderer[] rightArmParts = new AdvancedModelRenderer[] { handR, lowerArmR, upperArmR };
        AdvancedModelRenderer[] leftArmParts = new AdvancedModelRenderer[] { handL, lowerArmL, upperArmL };
        AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[] { tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelRenderer[] bodyParts = new AdvancedModelRenderer[] { waist, chest, shoulders, neck4, neck3, neck2, neck1, head };

        float globalSpeed = 1.0F;
        float globalHeight = 2F * f1;


        model.bob(waist, 1F * globalSpeed, globalHeight, false, f, f1);
        model.bob(thighL, 1F * globalSpeed, globalHeight, false, f, f1);
        model.bob(thighR, 1F * globalSpeed, globalHeight, false, f, f1);
        model.walk(shoulders, 1F * globalSpeed, 0.2F, true, 1, 0, f, f1);
        model.walk(chest, 1F * globalSpeed, 0.2F, false, 0.5F, 0, f, f1);

        model.walk(thighL, 0.5F * globalSpeed, 0.7F, false, 3.14F, 0.2F, f, f1);
        model.walk(lowerThighL, 0.5F * globalSpeed, 0.6F, false, 1.5F, 0.3F, f, f1);
        model.walk(upperFootL, 0.5F * globalSpeed, 0.8F, false, -1F, -0.1F, f, f1);
        model.walk(footL, 0.5F * globalSpeed, 1.5F, true, -1F, 1F, f, f1);

        model.walk(thighR, 0.5F * globalSpeed, 0.7F, true, 3.14F, 0.2F, f, f1);
        model.walk(lowerThighR, 0.5F * globalSpeed, 0.6F, true, 1.5F, 0.3F, f, f1);
        model.walk(upperFootR, 0.5F * globalSpeed, 0.8F, true, -1F, -0.1F, f, f1);
        model.walk(footR, 0.5F * globalSpeed, 1.5F, false, -1F, 1F, f, f1);


        model.chainSwing(tailParts, 0.5F * globalSpeed, -0.1F, 2, f, f1);
        model.chainWave(tailParts, 1F * globalSpeed, -0.1F, 2.5F, f, f1);
        model.chainWave(bodyParts, 1F * globalSpeed, -0.1F, 4, f, f1);

        model.chainWave(rightArmParts, 1F * globalSpeed, -0.3F, 4, f, f1);
        model.chainWave(leftArmParts, 1F * globalSpeed, -0.3F, 4, f, f1);

        model.chainWave(tailParts, 0.1F, 0.05F, 2, ticks, 0.25F);
        model.chainWave(bodyParts, 0.1F, -0.03F, 5, ticks, 0.25F);
        model.chainWave(rightArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);
        model.chainWave(leftArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);

        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
