package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.RugopsEntity;

@SideOnly(Side.CLIENT)
public class RugopsAnimator extends EntityAnimator<RugopsEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, RugopsEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        float globalSpeed = 0.45F;
        float globalDegree = 0.4F;
        float height = 1.0F;

        AdvancedModelRenderer shoulders = model.getCube("Body shoulders");
        AdvancedModelRenderer waist = model.getCube("Body waist");

        // Right feet
        AdvancedModelRenderer rightThigh = model.getCube("Right Thigh");
        AdvancedModelRenderer rightCalf1 = model.getCube("Right Calf 1");
        AdvancedModelRenderer rightCalf2 = model.getCube("Right Calf 2");
        AdvancedModelRenderer rightFoot = model.getCube("Foot Right");

        // Left feet
        AdvancedModelRenderer leftThigh = model.getCube("Left Thigh");
        AdvancedModelRenderer leftCalf1 = model.getCube("Left Calf 1");
        AdvancedModelRenderer leftCalf2 = model.getCube("Left Calf 2");
        AdvancedModelRenderer leftFoot = model.getCube("Foot Left");

        // neck
        AdvancedModelRenderer neck1 = model.getCube("Neck 1");
        AdvancedModelRenderer neck2 = model.getCube("Neck 2");
        AdvancedModelRenderer neck3 = model.getCube("Neck 3");
        AdvancedModelRenderer neck4 = model.getCube("Neck 4");
        AdvancedModelRenderer throat1 = model.getCube("Throat 1");
        AdvancedModelRenderer throat2 = model.getCube("Throat 2");

        // head
        AdvancedModelRenderer head = model.getCube("Head");

        // arms
        AdvancedModelRenderer upperArmLeft = model.getCube("Upper Arm Left");
        AdvancedModelRenderer lowerArmLeft = model.getCube("Lower Arm Left");
        AdvancedModelRenderer upperArmRight = model.getCube("Upper Arm Right");
        AdvancedModelRenderer lowerArmRight = model.getCube("Lower Arm Right");

        // hands
        AdvancedModelRenderer handRight = model.getCube("Hand Right");
        AdvancedModelRenderer handLeft = model.getCube("Hand Left");

        // tail
        AdvancedModelRenderer tail1 = model.getCube("Tail 1");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
        AdvancedModelRenderer tail6 = model.getCube("Tail 6");

        AdvancedModelRenderer lowerJaw = model.getCube("Lower Jaw");

        AdvancedModelRenderer[] rightArmParts = new AdvancedModelRenderer[] { handRight, upperArmRight, lowerArmRight };
        AdvancedModelRenderer[] leftArmParts = new AdvancedModelRenderer[] { handLeft, upperArmLeft, lowerArmLeft };
        AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelRenderer[] bodyParts = new AdvancedModelRenderer[] { head, neck1, neck2, neck3, neck4, shoulders, waist };

        model.bob(leftThigh, 1F * globalSpeed, height, false, f, f1);
        model.bob(neck4, 1F * globalSpeed, height, false, f, f1);
        model.bob(rightThigh, 1F * globalSpeed, height, false, f, f1);
        leftThigh.rotationPointY -= -2 * f1 * Math.cos(f * 0.5 * globalSpeed);
        rightThigh.rotationPointY -= 2 * f1 * Math.cos(f * 0.5 * globalSpeed);
        model.chainWave(bodyParts, 1F * globalSpeed, 0.05F, 3, f, f1);
        model.chainWave(tailParts, 1F * globalSpeed, height * 0.05F, 3, f, f1);
        model.chainWave(leftArmParts, 1F * globalSpeed, height * 0.05F, 3, f, f1);
        model.chainWave(rightArmParts, 1F * globalSpeed, height * 0.05F, 3, f, f1);

        model.walk(head, 1F * globalSpeed, 0.15F, true, 0F, -0.2F, f, f1);
        model.walk(neck1, 1F * globalSpeed, 0.03F, false, 0F, 0.04F, f, f1);
        model.walk(neck2, 1F * globalSpeed, 0.03F, false, 0F, 0.04F, f, f1);
        model.walk(neck3, 1F * globalSpeed, 0.03F, false, 0F, 0.04F, f, f1);
        model.walk(neck4, 1F * globalSpeed, 0.03F, false, 0F, 0.04F, f, f1);

        model.walk(leftThigh, 0.5F * globalSpeed, 0.8F * globalDegree, false, 0F, 0.2F, f, f1);
        model.walk(leftCalf1, 0.5F * globalSpeed, 1F * globalDegree, true, 1F, 0.4F, f, f1);
        model.walk(leftCalf2, 0.5F * globalSpeed, 1F * globalDegree, false, 0F, 0F, f, f1);
        model.walk(leftFoot, 0.5F * globalSpeed, 1.5F * globalDegree, true, 0.5F, -0.1F, f, f1);

        model.walk(rightThigh, 0.5F * globalSpeed, 0.8F * globalDegree, true, 0F, 0.2F, f, f1);
        model.walk(rightCalf1, 0.5F * globalSpeed, 1F * globalDegree, false, 1F, 0.4F, f, f1);
        model.walk(rightCalf2, 0.5F * globalSpeed, 1F * globalDegree, true, 0F, 0F, f, f1);
        model.walk(rightFoot, 0.5F * globalSpeed, 1.5F * globalDegree, false, 0.5F, -0.1F, f, f1);

        // idling
        model.chainWave(tailParts, 0.1F, 0.05F, 2, ticks, 0.25F);
        model.chainWave(bodyParts, 0.1F, -0.03F, 5, ticks, 0.25F);
        model.chainWave(rightArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);
        model.chainWave(leftArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);

        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
