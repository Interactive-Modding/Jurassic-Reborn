package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.RugopsEntity;

@OnlyIn(Dist.CLIENT)
public class RugopsAnimator extends EntityAnimator<RugopsEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, RugopsEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        float globalSpeed = 0.45F;
        float globalDegree = 0.4F;
        float height = 1.0F;

        AdvancedModelBox shoulders = model.getCube("Body shoulders");
        AdvancedModelBox waist = model.getCube("Body waist");

        // Right feet
        AdvancedModelBox rightThigh = model.getCube("Right Thigh");
        AdvancedModelBox rightCalf1 = model.getCube("Right Calf 1");
        AdvancedModelBox rightCalf2 = model.getCube("Right Calf 2");
        AdvancedModelBox rightFoot = model.getCube("Foot Right");

        // Left feet
        AdvancedModelBox leftThigh = model.getCube("Left Thigh");
        AdvancedModelBox leftCalf1 = model.getCube("Left Calf 1");
        AdvancedModelBox leftCalf2 = model.getCube("Left Calf 2");
        AdvancedModelBox leftFoot = model.getCube("Foot Left");

        // neck
        AdvancedModelBox neck1 = model.getCube("Neck 1");
        AdvancedModelBox neck2 = model.getCube("Neck 2");
        AdvancedModelBox neck3 = model.getCube("Neck 3");
        AdvancedModelBox neck4 = model.getCube("Neck 4");
        AdvancedModelBox throat1 = model.getCube("Throat 1");
        AdvancedModelBox throat2 = model.getCube("Throat 2");

        // head
        AdvancedModelBox head = model.getCube("Head");

        // arms
        AdvancedModelBox upperArmLeft = model.getCube("Upper Arm Left");
        AdvancedModelBox lowerArmLeft = model.getCube("Lower Arm Left");
        AdvancedModelBox upperArmRight = model.getCube("Upper Arm Right");
        AdvancedModelBox lowerArmRight = model.getCube("Lower Arm Right");

        // hands
        AdvancedModelBox handRight = model.getCube("Hand Right");
        AdvancedModelBox handLeft = model.getCube("Hand Left");

        // tail
        AdvancedModelBox tail1 = model.getCube("Tail 1");
        AdvancedModelBox tail2 = model.getCube("Tail 2");
        AdvancedModelBox tail3 = model.getCube("Tail 3");
        AdvancedModelBox tail4 = model.getCube("Tail 4");
        AdvancedModelBox tail5 = model.getCube("Tail 5");
        AdvancedModelBox tail6 = model.getCube("Tail 6");

        AdvancedModelBox lowerJaw = model.getCube("Lower Jaw");

        AdvancedModelBox[] rightArmParts = new AdvancedModelBox[] { handRight, upperArmRight, lowerArmRight };
        AdvancedModelBox[] leftArmParts = new AdvancedModelBox[] { handLeft, upperArmLeft, lowerArmLeft };
        AdvancedModelBox[] tailParts = new AdvancedModelBox[] { tail6, tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelBox[] bodyParts = new AdvancedModelBox[] { head, neck1, neck2, neck3, neck4, shoulders, waist };

        model.chainWave(tailParts, 1F * globalSpeed, height * 0.05F, 3, f, f1);
        model.chainWave(leftArmParts, 1F * globalSpeed, height * 0.05F, 3, f, f1);
        model.chainWave(rightArmParts, 1F * globalSpeed, height * 0.05F, 3, f, f1);
        model.bob(waist, 0.1F, 0.04F, false, ticks, 1.0F);

        // idling
        model.chainWave(tailParts, 0.1F, 0.05F, 2, ticks, 0.25F);
        model.chainWave(rightArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);
        model.chainWave(leftArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);

        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
