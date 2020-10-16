package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.CarnotaurusEntity;

@SideOnly(Side.CLIENT)
public class CarnotaurusAnimator extends EntityAnimator<CarnotaurusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, CarnotaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        float globalSpeed = 0.8F;
        float globalDegree = 0.6F;
        float globalHeight = 1.0F;

        AdvancedModelRenderer neck1 = model.getCube("Neck 1");
        AdvancedModelRenderer neck2 = model.getCube("Neck 2");
        AdvancedModelRenderer neck3 = model.getCube("Neck 3");
        AdvancedModelRenderer neck4 = model.getCube("Neck 4");

        AdvancedModelRenderer head = model.getCube("Head");

        AdvancedModelRenderer shoulders = model.getCube("Body shoulders");
        AdvancedModelRenderer hips = model.getCube("Body hips");
        AdvancedModelRenderer waist = model.getCube("Body waist");

        AdvancedModelRenderer tail1 = model.getCube("Tail 1");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
        AdvancedModelRenderer tail6 = model.getCube("Tail 6");
        AdvancedModelRenderer tail7 = model.getCube("Tail 7");


        AdvancedModelRenderer leftThigh = model.getCube("Left Thigh");
        AdvancedModelRenderer rightThigh = model.getCube("Right Thigh");

        AdvancedModelRenderer leftCalf1 = model.getCube("Left Calf 1");
        AdvancedModelRenderer rightCalf1 = model.getCube("Right Calf 1");

        AdvancedModelRenderer leftCalf2 = model.getCube("Left Calf 2");
        AdvancedModelRenderer rightCalf2 = model.getCube("Right Calf 2");

        AdvancedModelRenderer leftFoot = model.getCube("Foot Left");
        AdvancedModelRenderer rightFoot = model.getCube("Foot Right");

        AdvancedModelRenderer upperArmLeft = model.getCube("Upper Arm LEFT");
        AdvancedModelRenderer upperArmRight = model.getCube("Upper Arm Right");

        AdvancedModelRenderer lowerArmLeft = model.getCube("Lower Arm LEFT");
        AdvancedModelRenderer lowerArmRight = model.getCube("Lower Arm Right");

        AdvancedModelRenderer handLeft = model.getCube("Hand LEFT");
        AdvancedModelRenderer handRight = model.getCube("Hand Right");

        AdvancedModelRenderer lowerJaw = model.getCube("Lower Jaw");
        AdvancedModelRenderer upperJaw = model.getCube("Upper Jaw");

        AdvancedModelRenderer[] body = new AdvancedModelRenderer[] { head, neck4, neck3, neck2, neck1, shoulders, waist, hips };
        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail7, tail7, tail6, tail5, tail4, tail3, tail2, tail1 };

        AdvancedModelRenderer[] armRight = new AdvancedModelRenderer[] { handRight, lowerArmRight, upperArmRight };
        AdvancedModelRenderer[] armLeft = new AdvancedModelRenderer[] { handLeft, lowerArmLeft, upperArmLeft };

        model.bob(hips, globalSpeed * 1.0F, globalHeight * 0.0F, false, f, f1);

        model.bob(leftThigh, globalSpeed * 1.0F, globalHeight * 0.0F, false, f, f1);
        model.bob(rightThigh, globalSpeed * 1.0F, globalHeight * 0.0F, false, f, f1);

        model.chainWave(body, globalSpeed * 1.0F, globalHeight * 0.00F, 3, f, f1);
        model.chainWave(tail, globalSpeed * 1.0F, globalHeight * -0.00F, 2, f, f1);
        model.chainSwing(tail, globalSpeed * 0.5F, globalHeight * 0.00F, 2, f, f1);


        model.chainWave(armRight, globalSpeed * 1F, globalHeight * 0.25F, 3, f, f1);
        model.chainWave(armLeft, globalSpeed * 1F, globalHeight * 0.25F, 3, f, f1);


        model.chainWave(tail, 0.1F, -0.05F, 2, ticks, 0.25F);
        model.chainWave(body, 0.1F, 0.00F, 5, ticks, 0.25F);
        model.chainWave(armRight, 0.1F, 0.1F, 4, ticks, 0.25F);
        model.chainWave(armLeft, 0.1F, 0.1F, 4, ticks, 0.25F);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}