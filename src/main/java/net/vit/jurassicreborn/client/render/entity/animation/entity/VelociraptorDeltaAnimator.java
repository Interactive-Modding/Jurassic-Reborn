package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.VelociraptorDeltaEntity;
@OnlyIn(Dist.CLIENT)
public class VelociraptorDeltaAnimator extends EntityAnimator<VelociraptorDeltaEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, VelociraptorDeltaEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelBox waist = model.getCube("body3");
        AdvancedModelBox chest = model.getCube("body2");
        AdvancedModelBox shoulders = model.getCube("body1");
        AdvancedModelBox neck1 = model.getCube("neck1");
        AdvancedModelBox neck2 = model.getCube("neck2");
        AdvancedModelBox neck3 = model.getCube("neck3");
        AdvancedModelBox neck4 = model.getCube("neck4");
        AdvancedModelBox head = model.getCube("Head");
        AdvancedModelBox tail1 = model.getCube("tail1");
        AdvancedModelBox tail2 = model.getCube("tail2");
        AdvancedModelBox tail3 = model.getCube("tail3");
        AdvancedModelBox tail4 = model.getCube("tail4");
        AdvancedModelBox tail5 = model.getCube("tail5");
        AdvancedModelBox tail6 = model.getCube("tail6");

        AdvancedModelBox upperArmRight = model.getCube("Right arm");
        AdvancedModelBox upperArmLeft = model.getCube("Left arm");
        AdvancedModelBox lowerArmRight = model.getCube("Right forearm");
        AdvancedModelBox lowerArmLeft = model.getCube("Left forearm");
        AdvancedModelBox Hand_Right = model.getCube("Right hand");
        AdvancedModelBox Hand_Left = model.getCube("Left hand");

        AdvancedModelBox leftThigh = model.getCube("Left thigh");
        AdvancedModelBox rightThigh = model.getCube("Right thigh");

        AdvancedModelBox[] rightArmParts = new AdvancedModelBox[] { Hand_Right, lowerArmRight, upperArmRight };
        AdvancedModelBox[] leftArmParts = new AdvancedModelBox[] { Hand_Left, lowerArmLeft, upperArmLeft };
        AdvancedModelBox[] tailParts = new AdvancedModelBox[] { tail6, tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelBox[] bodyParts = new AdvancedModelBox[] { waist, chest, shoulders, neck1, neck2, neck3, neck4, head };

        float globalSpeed = 1.0F;
        float globalDegree = 1.0F;

        model.bob(waist, globalSpeed * 0.5F, globalDegree * 1.0F, false, limbSwing, limbSwingAmount);
        model.bob(rightThigh, globalSpeed * 0.5F, globalDegree * 1.0F, false, limbSwing, limbSwingAmount);
        model.bob(leftThigh, globalSpeed * 0.5F, globalDegree * 1.0F, false, limbSwing, limbSwingAmount);

        model.chainWave(tailParts, globalSpeed * 0.5F, globalDegree * 0.05F, 1, limbSwing, limbSwingAmount);
        model.chainSwing(tailParts, globalSpeed * 0.5F, globalDegree * 0.1F, 2, limbSwing, limbSwingAmount);
        model.chainWave(bodyParts, globalSpeed * 0.5F, globalDegree * 0.025F, 3, limbSwing, limbSwingAmount);

        model.chainWave(tailParts, 0.1F, 0.05F, 2, ticks, 0.25F);
        model.chainWave(bodyParts, 0.1F, -0.03F, 5, ticks, 0.25F);
        model.chainWave(rightArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);
        model.chainWave(leftArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);

        if(!entity.isNoAi())
            model.faceTarget(rotationYaw, rotationPitch, 2.0F, neck1, head);

        entity.tailBuffer.applyChainSwingBuffer(tailParts);

    }
}