package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.MajungasaurusEntity;

@SideOnly(Side.CLIENT)
public class MajungasaurusAnimator extends EntityAnimator<MajungasaurusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, MajungasaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer tail1 = model.getCube("Tail Base");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
        AdvancedModelRenderer tail6 = model.getCube("Tail 6");

        AdvancedModelRenderer leftThigh = model.getCube("Left Thigh");
        AdvancedModelRenderer rightThigh = model.getCube("Right Thigh");

        AdvancedModelRenderer leftCalf1 = model.getCube("Left Calf 1");
        AdvancedModelRenderer rightCalf1 = model.getCube("Right Calf 1");

        AdvancedModelRenderer leftCalf2 = model.getCube("Left Calf 2");
        AdvancedModelRenderer rightCalf2 = model.getCube("Right Calf 2");

        AdvancedModelRenderer leftFoot = model.getCube("Foot Left");
        AdvancedModelRenderer rightFoot = model.getCube("Foot Right");

        AdvancedModelRenderer bodyRear = model.getCube("Body Rear");
        AdvancedModelRenderer bodyMid = model.getCube("Body Mid");
        AdvancedModelRenderer bodyFront = model.getCube("Body Front");

        AdvancedModelRenderer neck1 = model.getCube("Neck BASE");
        AdvancedModelRenderer neck2 = model.getCube("Neck 2");
        AdvancedModelRenderer neck3 = model.getCube("Neck 3");
        AdvancedModelRenderer neck4 = model.getCube("Neck 4");

        AdvancedModelRenderer head = model.getCube("Head");

        AdvancedModelRenderer upperJaw = model.getCube("Upper Jaw");
        AdvancedModelRenderer lowerJaw = model.getCube("Lower jaw");

        AdvancedModelRenderer upperArmRight = model.getCube("Upper Arm Right");
        AdvancedModelRenderer upperArmLeft = model.getCube("Upper Arm LEFT");

        AdvancedModelRenderer lowerArmRight = model.getCube("Lower Arm Right");
        AdvancedModelRenderer lowerArmLeft = model.getCube("Lower Arm LEFT");

        AdvancedModelRenderer handRight = model.getCube("Hand Right");
        AdvancedModelRenderer handLeft = model.getCube("Hand LEFT");

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail1, tail2, tail3, tail4, tail5, tail6 };

        AdvancedModelRenderer[] armLeft = new AdvancedModelRenderer[] { upperArmLeft, lowerArmLeft, handLeft };
        AdvancedModelRenderer[] armRight = new AdvancedModelRenderer[] { upperArmRight, lowerArmRight, handRight };

        AdvancedModelRenderer[] body = new AdvancedModelRenderer[] { bodyRear, bodyMid, bodyFront, neck1, neck2, neck3, neck4, head };

        float globalSpeed = 0.5F;
        float globalDegree = 0.4F;
        float globalHeight = 1.0F;

        model.bob(bodyRear, globalSpeed * 1F, globalHeight * 0.8F, false, f, f1);

        model.bob(leftThigh, globalSpeed * 1F, globalHeight * 0.8F, false, f, f1);
        model.bob(rightThigh, globalSpeed * 1F, globalHeight * 0.8F, false, f, f1);

        model.chainWave(body, globalSpeed * 1F, globalHeight * -0.02F, -3, f, f1);
        model.chainWave(tail, globalSpeed * 1F, globalHeight * 0.05F, -2, f, f1);

        model.chainWave(armRight, globalSpeed * 1F, globalHeight * -0.25F, -3, f, f1);
        model.chainWave(armLeft, globalSpeed * 1F, globalHeight * -0.25F, -3, f, f1);

        model.chainWave(tail, 0.1F, 0.05F, -2, ticks, 0.25F);
        model.chainWave(body, 0.1F, 0.03F, -5, ticks, 0.25F);
        model.chainWave(armRight, 0.1F, 0.1F, -4, ticks, 0.25F);
        model.chainWave(armLeft, 0.1F, 0.1F, -4, ticks, 0.25F);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
