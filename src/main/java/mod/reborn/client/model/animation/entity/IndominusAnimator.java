package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.IndominusEntity;

@SideOnly(Side.CLIENT)
public class IndominusAnimator extends EntityAnimator<IndominusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, IndominusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        float globalSpeed = 0.5F;
        float globalDegree = 0.4F;
        float globalHeight = 1.0F;

        AdvancedModelRenderer head = model.getCube("Head");

        AdvancedModelRenderer lowerJaw = model.getCube("Lower Jaw");
        AdvancedModelRenderer upperJaw = model.getCube("Upper Jaw");

        AdvancedModelRenderer neck1 = model.getCube("Neck BASE");
        AdvancedModelRenderer neck2 = model.getCube("Neck 2");
        AdvancedModelRenderer neck3 = model.getCube("Neck 3");
        AdvancedModelRenderer neck4 = model.getCube("Neck 4");

        AdvancedModelRenderer tail1 = model.getCube("Tail Base");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
        AdvancedModelRenderer tail6 = model.getCube("Tail 6");
        AdvancedModelRenderer tail7 = model.getCube("Tail 7");

        AdvancedModelRenderer throat1 = model.getCube("Throat 1");
        AdvancedModelRenderer throat2 = model.getCube("Throat 2");

        AdvancedModelRenderer bodyFront = model.getCube("Body Front");
        AdvancedModelRenderer bodyMid = model.getCube("Body Mid");
        AdvancedModelRenderer bodyRear = model.getCube("Body Rear");

        AdvancedModelRenderer leftThigh = model.getCube("Left Thigh");
        AdvancedModelRenderer rightThigh = model.getCube("Right Thigh");

        AdvancedModelRenderer leftCalf1 = model.getCube("Left Calf 1");
        AdvancedModelRenderer rightCalf1 = model.getCube("Right Calf 1");

        AdvancedModelRenderer leftCalf2 = model.getCube("Left Calf 2");
        AdvancedModelRenderer rightCalf2 = model.getCube("Right Calf 2");

        AdvancedModelRenderer leftFoot = model.getCube("Foot Left");
        AdvancedModelRenderer rightFoot = model.getCube("Foot Right");

        AdvancedModelRenderer upperArmLeft = model.getCube("Arm UPPER LEFT");
        AdvancedModelRenderer upperArmRight = model.getCube("Arm UPPER RIGHT");

        AdvancedModelRenderer lowerArmLeft = model.getCube("Arm MID LEFT");
        AdvancedModelRenderer lowerArmRight = model.getCube("Arm MID RIGHT");

        AdvancedModelRenderer handLeft = model.getCube("Hand LEFT");
        AdvancedModelRenderer handRight = model.getCube("Hand RIGHT");

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail7, tail6, tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelRenderer[] body = new AdvancedModelRenderer[] { head, neck4, neck3, neck2, neck1, bodyFront, bodyMid, bodyRear };

        AdvancedModelRenderer[] armLeft = new AdvancedModelRenderer[] { handLeft, lowerArmLeft, upperArmLeft };
        AdvancedModelRenderer[] armRight = new AdvancedModelRenderer[] { handRight, lowerArmRight, upperArmRight };

        model.bob(bodyRear, globalSpeed * 1F, globalHeight * 1.0F, false, f, f1);

        model.bob(leftThigh, globalSpeed * 1F, globalHeight * 1.0F, false, f, f1);
        model.bob(rightThigh, globalSpeed * 1F, globalHeight * 1.0F, false, f, f1);

  //      model.chainWave(body, globalSpeed * 1F, globalHeight * 0.05F, 3, f, f1);
  //      model.chainWave(tail, globalSpeed * 1F, -globalHeight * 0.05F, 2, f, f1);
  //      model.chainSwing(tail, globalSpeed * 0.5F, globalHeight * 0.025F, 2, f, f1);

   //     model.walk(leftThigh, 0.5F * globalSpeed, 0.8F * globalDegree, false, 0F, 0.4F, f, f1);
    //    model.walk(leftCalf1, 0.5F * globalSpeed, 1F * globalDegree, true, 1F, 0.1F, f, f1);
    //    model.walk(leftCalf2, 0.5F * globalSpeed, 1F * globalDegree, false, 0F, 0F, f, f1);
    //    model.walk(leftFoot, 0.5F * globalSpeed, 1.5F * globalDegree, true, 0.5F, 0.1F, f, f1);

    //    model.walk(rightThigh, 0.5F * globalSpeed, 0.8F * globalDegree, true, 0F, 0.4F, f, f1);
    //    model.walk(rightCalf1, 0.5F * globalSpeed, 1F * globalDegree, false, 1F, 0.1F, f, f1);
    //    model.walk(rightCalf2, 0.5F * globalSpeed, 1F * globalDegree, true, 0F, 0F, f, f1);
    //    model.walk(rightFoot, 0.5F * globalSpeed, 1.5F * globalDegree, false, 0.5F, 0.1F, f, f1);

    //    leftThigh.rotationPointY += 2 * f1 * Math.cos(f * 0.5F * globalSpeed);
    //    rightThigh.rotationPointY -= 2 * f1 * Math.cos(f * 0.5F * globalSpeed);

    //    model.chainWave(armRight, globalSpeed * 1F, globalHeight * 0.2F, 3, f, f1);
    //    model.chainWave(armLeft, globalSpeed * 1F, globalHeight * 0.2F, 3, f, f1);

     //   model.chainWave(tail, 0.1F, -0.025F, 2, ticks, 0.25F);
    //    model.chainWave(body, 0.1F, 0.03F, 5, ticks, 0.25F);
    //    model.chainWave(armRight, -0.1F, 0.1F, 4, ticks, 0.25F);
    //    model.chainWave(armLeft, -0.1F, 0.1F, 4, ticks, 0.25F);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
