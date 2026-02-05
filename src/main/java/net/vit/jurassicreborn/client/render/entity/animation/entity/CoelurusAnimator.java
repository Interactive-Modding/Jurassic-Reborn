package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.CoelurusEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CoelurusAnimator extends EntityAnimator<CoelurusEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, CoelurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
        {
            AdvancedModelBox head = model.getCube("head");
            AdvancedModelBox neck1 = model.getCube("neck1");
            AdvancedModelBox neck2 = model.getCube("neck2");
            AdvancedModelBox neck3 = model.getCube("neck3");
            AdvancedModelBox neck4 = model.getCube("neck4");

            AdvancedModelBox lowerJaw = model.getCube("down_jaw");

            AdvancedModelBox waist = model.getCube("Body Rear");
            AdvancedModelBox chest = model.getCube("Body Middle");
            AdvancedModelBox shoulders = model.getCube("Body Front");

            AdvancedModelBox tail1 = model.getCube("Tail Base");
            AdvancedModelBox tail2 = model.getCube("Tail 2");
            AdvancedModelBox tail3 = model.getCube("Tail 3");
            AdvancedModelBox tail4 = model.getCube("Tail 4");
            AdvancedModelBox tail5 = model.getCube("Tail 5");

            AdvancedModelBox upperArmR = model.getCube("Right arm");
            AdvancedModelBox upperArmL = model.getCube("Left arm");

            AdvancedModelBox lowerArmR = model.getCube("Right forearm");
            AdvancedModelBox lowerArmL = model.getCube("Left forearm");

            AdvancedModelBox handR = model.getCube("Right hand");
            AdvancedModelBox handL = model.getCube("Left hand");

            AdvancedModelBox thighR = model.getCube("thigh1");
            AdvancedModelBox thighL = model.getCube("thigh2");

            AdvancedModelBox[] rightArmParts = new AdvancedModelBox[]{handR, lowerArmR, upperArmR};
            AdvancedModelBox[] leftArmParts = new AdvancedModelBox[]{handL, lowerArmL, upperArmL};
            AdvancedModelBox[] tailParts = new AdvancedModelBox[]{tail5, tail4, tail3, tail2, tail1};
            AdvancedModelBox[] bodyParts = new AdvancedModelBox[]{waist, chest, shoulders, neck4, neck3, neck2, neck1, head};

            float globalSpeed = 1.0F;
            float globalHeight = 2F * f1;

            model.bob(thighL, 1F * globalSpeed, 0.3f * globalHeight, false, f, f1);
            model.bob(thighR, 1F * globalSpeed, 0.3f * globalHeight, false, f, f1);

            model.chainSwing(tailParts, 0.5F * globalSpeed, -0.1F, 2, f, f1);
            model.chainWave(tailParts, 1F * globalSpeed, -0.1F, 2.5F, f, f1);
//        model.chainWave(bodyParts, 1F * globalSpeed, -0.1F, 4, f, f1);

            model.chainWave(rightArmParts, 0.5F * globalSpeed, -0.3F, 4, f, f1);
            model.chainWave(leftArmParts, 0.5F * globalSpeed, -0.3F, 4, f, f1);

            model.chainWave(tailParts, 0.2F, 0.05F, 2, ticks, 0.25F);
//        model.chainWave(bodyParts, 0.2F, -0.03F, 5, ticks, 0.25F);
            model.chainWave(rightArmParts, 0.2F, -0.1F, 4, ticks, 0.25F);
            model.chainWave(leftArmParts, 0.2F, -0.1F, 4, ticks, 0.25F);

            model.faceTarget(rotationYaw, rotationPitch, 1.0F, neck1, neck2, neck3, neck4, head);

            entity.tailBuffer.applyChainSwingBuffer(tailParts);
        }
    }
}