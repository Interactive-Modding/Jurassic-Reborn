package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.DodoEntity;

@SideOnly(Side.CLIENT)
public class DodoAnimator extends EntityAnimator<DodoEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, DodoEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer head = model.getCube("Head");

        AdvancedModelRenderer neck1 = model.getCube("Neck1");
        AdvancedModelRenderer neck2 = model.getCube("Neck2");
        AdvancedModelRenderer neck3 = model.getCube("Neck3");
        AdvancedModelRenderer neck4 = model.getCube("Neck4");
        AdvancedModelRenderer neck5 = model.getCube("Neck5");
        AdvancedModelRenderer neck6 = model.getCube("Neck6");
        AdvancedModelRenderer neck7 = model.getCube("Neck7");

        AdvancedModelRenderer lowerJaw = model.getCube("LowerJaw1");
        AdvancedModelRenderer upperJaw = model.getCube("UpperJaw1");

        AdvancedModelRenderer body = model.getCube("Body1");
        AdvancedModelRenderer bodyFront = model.getCube("Body2");
        AdvancedModelRenderer bodyBack = model.getCube("Body3");

        AdvancedModelRenderer tail = model.getCube("Tail");

        AdvancedModelRenderer leftWing1 = model.getCube("LeftWing1");
        AdvancedModelRenderer leftWing2 = model.getCube("LeftWing2");

        AdvancedModelRenderer rightWing1 = model.getCube("RightWing1");
        AdvancedModelRenderer rightWing2 = model.getCube("RightWing2");

        AdvancedModelRenderer leftLegBase = model.getCube("LeftLeg1");
        AdvancedModelRenderer leftLeg2 = model.getCube("LeftLeg2");
        AdvancedModelRenderer leftFoot = model.getCube("LeftFeet");

        AdvancedModelRenderer rightLegBase = model.getCube("RightLeg1");
        AdvancedModelRenderer rightLeg2 = model.getCube("RightLeg2");
        AdvancedModelRenderer rightFoot = model.getCube("RightFeet");

        AdvancedModelRenderer[] neckParts = new AdvancedModelRenderer[] { head, neck7, neck6, neck5, neck4, neck3, neck2, neck1 };
        AdvancedModelRenderer[] bodyParts = new AdvancedModelRenderer[] { bodyFront, body, bodyBack, tail };

        // f = ticks;
        // f1 = 0.25F;

        float globalSpeed = 1.0F;
        float globalDegree = 1.0F;
        float globalHeight = 0.5F;

        model.chainWave(neckParts, globalSpeed * 1.0F, globalHeight * 0.1F, 3, f, f1);
        model.chainWave(bodyParts, globalSpeed * 1.0F, globalHeight * 0.1F, 3, f, f1);

        model.swing(tail, globalSpeed * 1.0F, globalHeight * 2.0F, false, 0.0F, 0.0F, f, f1);

        model.bob(body, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);
        model.bob(leftLegBase, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);
        model.bob(rightLegBase, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);

        model.walk(rightLegBase, globalSpeed * 0.5F, globalDegree * 1.0F, false, 0.0F, 0.0F, f, f1);
        model.walk(rightLeg2, globalSpeed * 0.5F, globalDegree * 0.5F, false, 0.0F, 0.0F, f, f1);
        model.walk(rightFoot, globalSpeed * 0.5F, globalDegree * 1.0F, false, 0.0F, 0.0F, f, f1);

        model.walk(leftLegBase, globalSpeed * 0.5F, globalDegree * 1.0F, true, 0.0F, 0.0F, f, f1);
        model.walk(leftLeg2, globalSpeed * 0.5F, globalDegree * 0.5F, true, 0.0F, 0.0F, f, f1);
        model.walk(leftFoot, globalSpeed * 0.5F, globalDegree * 1.0F, true, 0.0F, 0.0F, f, f1);

        leftLegBase.rotationPointZ -= 1 * f1 * Math.cos(f * 1.0F * globalSpeed);
        rightLegBase.rotationPointZ -= -1 * f1 * Math.cos(f * 1.0F * globalSpeed);

        model.chainWave(neckParts, globalSpeed * 0.125F, globalHeight * 0.05F, 3, ticks, 0.25F);
        model.chainWave(bodyParts, globalSpeed * 0.125F, globalHeight * 0.05F, 3, ticks, 0.25F);
    }
}
