package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.IndominusEntity;

@OnlyIn(Dist.CLIENT)
public class IndominusAnimator extends EntityAnimator<IndominusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, IndominusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        float globalSpeed = 0.5F;
        float globalDegree = 0.4F;
        float globalHeight = 1.0F;

        AdvancedModelBox head = model.getCube("Head");

        AdvancedModelBox lowerJaw = model.getCube("Lower Jaw");
        AdvancedModelBox upperJaw = model.getCube("Upper Jaw");

        AdvancedModelBox neck1 = model.getCube("Neck BASE");
        AdvancedModelBox neck2 = model.getCube("Neck 2");
        AdvancedModelBox neck3 = model.getCube("Neck 3");
        AdvancedModelBox neck4 = model.getCube("Neck 4");

        AdvancedModelBox tail1 = model.getCube("Tail Base");
        AdvancedModelBox tail2 = model.getCube("Tail 2");
        AdvancedModelBox tail3 = model.getCube("Tail 3");
        AdvancedModelBox tail4 = model.getCube("Tail 4");
        AdvancedModelBox tail5 = model.getCube("Tail 5");
        AdvancedModelBox tail6 = model.getCube("Tail 6");
        AdvancedModelBox tail7 = model.getCube("Tail 7");

        AdvancedModelBox throat1 = model.getCube("Throat 1");
        AdvancedModelBox throat2 = model.getCube("Throat 2");

        AdvancedModelBox bodyFront = model.getCube("Body Front");
        AdvancedModelBox bodyMid = model.getCube("Body Mid");
        AdvancedModelBox bodyRear = model.getCube("Body Rear");

        AdvancedModelBox leftThigh = model.getCube("Left Thigh");
        AdvancedModelBox rightThigh = model.getCube("Right Thigh");

        AdvancedModelBox leftCalf1 = model.getCube("Left Calf 1");
        AdvancedModelBox rightCalf1 = model.getCube("Right Calf 1");

        AdvancedModelBox leftCalf2 = model.getCube("Left Calf 2");
        AdvancedModelBox rightCalf2 = model.getCube("Right Calf 2");

        AdvancedModelBox leftFoot = model.getCube("Foot Left");
        AdvancedModelBox rightFoot = model.getCube("Foot Right");

        AdvancedModelBox upperArmLeft = model.getCube("Arm UPPER LEFT");
        AdvancedModelBox upperArmRight = model.getCube("Arm UPPER RIGHT");

        AdvancedModelBox lowerArmLeft = model.getCube("Arm MID LEFT");
        AdvancedModelBox lowerArmRight = model.getCube("Arm MID RIGHT");

        AdvancedModelBox handLeft = model.getCube("Hand LEFT");
        AdvancedModelBox handRight = model.getCube("Hand RIGHT");

        AdvancedModelBox[] tail = new AdvancedModelBox[] { tail7, tail6, tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelBox[] body = new AdvancedModelBox[] { head, neck4, neck3, neck2, neck1, bodyFront, bodyMid, bodyRear };

        AdvancedModelBox[] armLeft = new AdvancedModelBox[] { handLeft, lowerArmLeft, upperArmLeft };
        AdvancedModelBox[] armRight = new AdvancedModelBox[] { handRight, lowerArmRight, upperArmRight };

        model.bob(bodyRear, globalSpeed * 1F, globalDegree * 1.0F, false, f, f1);

        model.bob(leftThigh, globalSpeed * 1F, globalDegree * 1.0F, false, f, f1);
        model.bob(rightThigh, globalSpeed * 1F, globalDegree * 1.0F, false, f, f1);

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
