package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.SpinosaurusEntity;

@SideOnly(Side.CLIENT)
public class SpinosaurusAnimator extends EntityAnimator<SpinosaurusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, SpinosaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        float globalSpeed = 0.45F;
        float globalDegree = 0.4F;
        float height = 1.0F;

        // middle
        AdvancedModelRenderer shoulders = model.getCube("Body 3");
        AdvancedModelRenderer chest = model.getCube("Body 2");
        AdvancedModelRenderer waist = model.getCube("Body 1");

        // right feet
        AdvancedModelRenderer rightThigh = model.getCube("Right Thigh");
        AdvancedModelRenderer rightCalf = model.getCube("Right Calf 1");
        AdvancedModelRenderer rightCalf2 = model.getCube("Right Calf 2");
        AdvancedModelRenderer rightFoot = model.getCube("Foot Right");

        // left feet
        AdvancedModelRenderer leftThigh = model.getCube("Left Thigh");
        AdvancedModelRenderer leftCalf = model.getCube("Left Calf 1");
        AdvancedModelRenderer leftCalf2 = model.getCube("Left Calf 2");
        AdvancedModelRenderer leftFoot = model.getCube("Foot Left");

        // neck
        AdvancedModelRenderer neck1 = model.getCube("Neck 1");
        AdvancedModelRenderer neck2 = model.getCube("Neck 2");
        AdvancedModelRenderer neck3 = model.getCube("Neck 3");
        AdvancedModelRenderer neck4 = model.getCube("Neck 4");
        AdvancedModelRenderer neck5 = model.getCube("Neck 5");
        AdvancedModelRenderer neck6 = model.getCube("Neck Under 1");
        AdvancedModelRenderer neck7 = model.getCube("Neck Under 2");

        // head
        AdvancedModelRenderer head = model.getCube("Head");

        // arms
        AdvancedModelRenderer lowerArmLeft = model.getCube("Lower Arm LEFT");
        AdvancedModelRenderer upperArmLeft = model.getCube("Upper Arm LEFT");
        AdvancedModelRenderer upperArmRight = model.getCube("Upper Arm Right");
        AdvancedModelRenderer lowerArmRight = model.getCube("Lower Arm Right");

        // hands
        AdvancedModelRenderer handLeft = model.getCube("hand left");
        AdvancedModelRenderer handRight = model.getCube("hand right");

        // tail
        AdvancedModelRenderer tail1 = model.getCube("Tail 1");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
        AdvancedModelRenderer tail6 = model.getCube("Tail 6");

        // teeth
        AdvancedModelRenderer teeth = model.getCube("Teeth");
        AdvancedModelRenderer teethFront = model.getCube("Teeth front");

        // jaw
        AdvancedModelRenderer upperJaw1 = model.getCube("Upper Jaw1");
        AdvancedModelRenderer upperJaw2 = model.getCube("Upper Jaw2");
        AdvancedModelRenderer upperJaw3 = model.getCube("Upper Jaw3");
        AdvancedModelRenderer upperJawFront = model.getCube("Upper Jaw front");
        AdvancedModelRenderer lowerJaw = model.getCube("Lower jaw");
        AdvancedModelRenderer lowerJawFront = model.getCube("Lower jaw front");

        // throat
        AdvancedModelRenderer throat1 = model.getCube("Neck Under 1");
        AdvancedModelRenderer throat2 = model.getCube("Neck Under 2");

        AdvancedModelRenderer[] rightArmParts = new AdvancedModelRenderer[] { handRight, lowerArmRight, upperArmRight };
        AdvancedModelRenderer[] leftArmParts = new AdvancedModelRenderer[] { handLeft, lowerArmLeft, upperArmLeft };
        AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelRenderer[] bodyParts = new AdvancedModelRenderer[] { head, neck1, neck2, neck3, neck4, neck5, shoulders, chest, waist };
        AdvancedModelRenderer[] bottomJaw = new AdvancedModelRenderer[] { lowerJawFront, lowerJaw };

        // Body animations
        model.bob(waist, 1F * globalSpeed, height, false, f, f1);
        model.bob(leftThigh, 1F * globalSpeed, height, false, f, f1);
        model.bob(rightThigh, 1F * globalSpeed, height, false, f, f1);
        leftThigh.rotationPointY -= -2 * f1 * Math.cos(f * 0.5 * globalSpeed);
        rightThigh.rotationPointY -= 2 * f1 * Math.cos(f * 0.5 * globalSpeed);
        model.chainWave(bodyParts, 1F * globalSpeed, 0.05F, 3, f, f1);
        model.chainWave(tailParts, 1F * globalSpeed, height * 0.05F, 3, f, f1);
        model.chainSwing(tailParts, 0.5F * globalSpeed, height * -0.05F, 2, f, f1);
        model.chainWave(leftArmParts, 1F * globalSpeed, height * 0.05F, 3, f, f1);
        model.chainWave(rightArmParts, 1F * globalSpeed, height * 0.05F, 3, f, f1);

        model.walk(head, 1F * globalSpeed, 0.1F, true, 0F, -0.2F, f, f1);
        model.walk(neck1, 1F * globalSpeed, 0.02F, false, 0F, 0.04F, f, f1);
        model.walk(neck2, 1F * globalSpeed, 0.02F, false, 0F, 0.04F, f, f1);
        model.walk(neck3, 1F * globalSpeed, 0.02F, false, 0F, 0.04F, f, f1);
        model.walk(neck4, 1F * globalSpeed, 0.02F, false, 0F, 0.04F, f, f1);
        model.walk(neck5, 1F * globalSpeed, 0.02F, false, 0F, 0.04F, f, f1);

        model.walk(leftThigh, 0.5F * globalSpeed, 0.8F * globalDegree, false, 0F, 0.2F, f, f1);
        model.walk(leftCalf, 0.5F * globalSpeed, 1F * globalDegree, true, 1F, 0.4F, f, f1);
        model.walk(leftCalf2, 0.5F * globalSpeed, 1F * globalDegree, false, 0F, 0F, f, f1);
        model.walk(leftFoot, 0.5F * globalSpeed, 1.5F * globalDegree, true, 0.5F, 0.1F, f, f1);

        model.walk(rightThigh, 0.5F * globalSpeed, 0.8F * globalDegree, true, 0F, 0.2F, f, f1);
        model.walk(rightCalf, 0.5F * globalSpeed, 1F * globalDegree, false, 1F, 0.4F, f, f1);
        model.walk(rightCalf2, 0.5F * globalSpeed, 1F * globalDegree, true, 0F, 0F, f, f1);
        model.walk(rightFoot, 0.5F * globalSpeed, 1.5F * globalDegree, false, 0.5F, 0.1F, f, f1);

        // idling
        model.chainWave(tailParts, 0.1F, 0.05F, 2, ticks, 0.25F);
        model.chainWave(bodyParts, 0.1F, -0.03F, 4, ticks, 0.25F);
        model.chainWave(rightArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);
        model.chainWave(leftArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);

        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
