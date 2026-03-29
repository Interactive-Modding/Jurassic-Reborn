package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.DodoEntity;

@OnlyIn(Dist.CLIENT)
public class DodoAnimator extends EntityAnimator<DodoEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, DodoEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelBox head = model.getCube("Head");

        AdvancedModelBox neck1 = model.getCube("Neck1");
        AdvancedModelBox neck2 = model.getCube("Neck2");
        AdvancedModelBox neck3 = model.getCube("Neck3");
        AdvancedModelBox neck4 = model.getCube("Neck4");
        AdvancedModelBox neck5 = model.getCube("Neck5");
        AdvancedModelBox neck6 = model.getCube("Neck6");
        AdvancedModelBox neck7 = model.getCube("Neck7");

        AdvancedModelBox lowerJaw = model.getCube("LowerJaw1");
        AdvancedModelBox upperJaw = model.getCube("UpperJaw1");

        AdvancedModelBox body = model.getCube("Body1");
        AdvancedModelBox bodyFront = model.getCube("Body2");
        AdvancedModelBox bodyBack = model.getCube("Body3");

        AdvancedModelBox tail = model.getCube("Tail");

        AdvancedModelBox leftWing1 = model.getCube("LeftWing1");
        AdvancedModelBox leftWing2 = model.getCube("LeftWing2");

        AdvancedModelBox rightWing1 = model.getCube("RightWing1");
        AdvancedModelBox rightWing2 = model.getCube("RightWing2");

        AdvancedModelBox leftLegBase = model.getCube("LeftLeg1");
        AdvancedModelBox leftLeg2 = model.getCube("LeftLeg2");
        AdvancedModelBox leftFoot = model.getCube("LeftFeet");

        AdvancedModelBox rightLegBase = model.getCube("RightLeg1");
        AdvancedModelBox rightLeg2 = model.getCube("RightLeg2");
        AdvancedModelBox rightFoot = model.getCube("RightFeet");

        AdvancedModelBox[] neckParts = new AdvancedModelBox[] { head, neck7, neck6, neck5, neck4, neck3, neck2, neck1 };
        AdvancedModelBox[] bodyParts = new AdvancedModelBox[] { bodyFront, body, bodyBack, tail };

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
