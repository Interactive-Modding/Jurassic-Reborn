package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.CarnotaurusEntity;

@OnlyIn(Dist.CLIENT)
public class CarnotaurusAnimator extends EntityAnimator<CarnotaurusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, CarnotaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        float globalSpeed = 0.8F;
        float globalDegree = 0.6F;
        float globalHeight = 1.0F;

        AdvancedModelBox neck1 = model.getCube("Neck 1");
        AdvancedModelBox neck2 = model.getCube("Neck 2");
        AdvancedModelBox neck3 = model.getCube("Neck 3");
        AdvancedModelBox neck4 = model.getCube("Neck 4");

        AdvancedModelBox head = model.getCube("Head");

        AdvancedModelBox shoulders = model.getCube("Body shoulders");
        AdvancedModelBox hips = model.getCube("Body hips");
        AdvancedModelBox waist = model.getCube("Body waist");

        AdvancedModelBox tail1 = model.getCube("Tail 1");
        AdvancedModelBox tail2 = model.getCube("Tail 2");
        AdvancedModelBox tail3 = model.getCube("Tail 3");
        AdvancedModelBox tail4 = model.getCube("Tail 4");
        AdvancedModelBox tail5 = model.getCube("Tail 5");
        AdvancedModelBox tail6 = model.getCube("Tail 6");
        AdvancedModelBox tail7 = model.getCube("Tail 7");


        AdvancedModelBox leftThigh = model.getCube("Left Thigh");
        AdvancedModelBox rightThigh = model.getCube("Right Thigh");

        AdvancedModelBox leftCalf1 = model.getCube("Left Calf 1");
        AdvancedModelBox rightCalf1 = model.getCube("Right Calf 1");

        AdvancedModelBox leftCalf2 = model.getCube("Left Calf 2");
        AdvancedModelBox rightCalf2 = model.getCube("Right Calf 2");

        AdvancedModelBox leftFoot = model.getCube("Foot Left");
        AdvancedModelBox rightFoot = model.getCube("Foot Right");

        AdvancedModelBox upperArmLeft = model.getCube("Upper Arm LEFT");
        AdvancedModelBox upperArmRight = model.getCube("Upper Arm Right");

        AdvancedModelBox lowerArmLeft = model.getCube("Lower Arm LEFT");
        AdvancedModelBox lowerArmRight = model.getCube("Lower Arm Right");

        AdvancedModelBox handLeft = model.getCube("Hand LEFT");
        AdvancedModelBox handRight = model.getCube("Hand Right");

        AdvancedModelBox lowerJaw = model.getCube("Lower Jaw");
        AdvancedModelBox upperJaw = model.getCube("Upper Jaw");

        AdvancedModelBox[] body = new AdvancedModelBox[] { head, neck4, neck3, neck2, neck1, shoulders, waist, hips };
        AdvancedModelBox[] tail = new AdvancedModelBox[] { tail7, tail7, tail6, tail5, tail4, tail3, tail2, tail1 };

        AdvancedModelBox[] armRight = new AdvancedModelBox[] { handRight, lowerArmRight, upperArmRight };
        AdvancedModelBox[] armLeft = new AdvancedModelBox[] { handLeft, lowerArmLeft, upperArmLeft };

        model.bob(hips, globalSpeed * 1.0F, globalHeight * 0.0F, false, f, f1);


        model.chainWave(body, globalSpeed * 1.0F, globalHeight * 0.00F, 3, f, f1);
        model.chainWave(tail, globalSpeed * 1.0F, globalHeight * -0.00F, 2, f, f1);
        model.chainSwing(tail, globalSpeed * 0.5F, globalHeight * 0.00F, 2, f, f1);


        model.chainWave(armRight, globalSpeed * 1F, globalHeight * 0.25F, 3, f, f1);
        model.chainWave(armLeft, globalSpeed * 1F, globalHeight * 0.25F, 3, f, f1);


        model.chainWave(tail, 0.1F, -0.05F, 2, ticks, 0.25F);
        model.chainWave(body, 0.1F, 0.00F, 5, ticks, 0.25F);
        model.chainWave(armRight, 0.1F, 0.1F, 4, ticks, 0.25F);
        model.chainWave(armLeft, 0.1F, 0.1F, 4, ticks, 0.25F);
        model.chainSwing(tail, 0.1F, 0.20F, -2, ticks, 1.0F);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}