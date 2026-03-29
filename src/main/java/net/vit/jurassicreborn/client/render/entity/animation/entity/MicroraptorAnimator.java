package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.client.model.AnimatableModel;
//import mod.reborn.client.model.animation.EntityAnimation;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;

//import net.minecraft.client.renderer.GlStateManager;import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MicroraptorEntity;

@OnlyIn(Dist.CLIENT)
public class MicroraptorAnimator extends EntityAnimator<MicroraptorEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, MicroraptorEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
//        if (entity.getAnimation() == EntityAnimation.GLIDING.get()) {
//            GlStateManager.rotate(rotationPitch, 1.0F, 0.0F, 0.0F);
//        }//mf what - gamma

        AdvancedModelBox upperArmRight = model.getCube("RightArm1");
        AdvancedModelBox lowerArmRight = model.getCube("RightArm2");
        AdvancedModelBox rightHand = model.getCube("RightArm3");
        AdvancedModelBox upperArmLeft = model.getCube("LeftArm1");
        AdvancedModelBox lowerArmLeft = model.getCube("LeftArm2");
        AdvancedModelBox leftHand = model.getCube("LeftArm3");

        AdvancedModelBox rightThigh = model.getCube("RightLeg1");
        AdvancedModelBox leftThigh = model.getCube("LeftLeg1");

        AdvancedModelBox tail1 = model.getCube("Tail1");
        AdvancedModelBox tail2 = model.getCube("Tail1");
        AdvancedModelBox tail3 = model.getCube("Tail1");
        AdvancedModelBox tail4 = model.getCube("Tail1");

        AdvancedModelBox shoulders = model.getCube("Body1");
        AdvancedModelBox waist = model.getCube("Body2");

        AdvancedModelBox neck1 = model.getCube("Neck1");
        AdvancedModelBox neck2 = model.getCube("Neck2");
        AdvancedModelBox head = model.getCube("Head");

        AdvancedModelBox[] rightArmParts = new AdvancedModelBox[] { rightHand, lowerArmRight, upperArmRight };
        AdvancedModelBox[] leftArmParts = new AdvancedModelBox[] { leftHand, lowerArmLeft, upperArmLeft };
        AdvancedModelBox[] tailParts = new AdvancedModelBox[] { tail4, tail3, tail2, tail1 };
        AdvancedModelBox[] bodyParts = new AdvancedModelBox[] { waist, shoulders, neck1, neck2, head };

        float globalSpeed = 1.0F;
        float globalDegree = 3.0F;

        model.bob(shoulders, globalSpeed * 1.0F, globalDegree * 1.0F, false, limbSwing, limbSwingAmount);
        model.bob(rightThigh, globalSpeed * 1.0F, globalDegree * 1.0F, false, limbSwing, limbSwingAmount);
        model.bob(leftThigh, globalSpeed * 1.0F, globalDegree * 1.0F, false, limbSwing, limbSwingAmount);

        model.chainWave(tailParts, 0.1F, 0.05F, 2, ticks, 0.5F);
        model.chainWave(bodyParts, 0.1F, -0.03F, 5, ticks, 0.5F);
        model.chainWave(rightArmParts, 0.1F, -0.1F, 4, ticks, 0.5F);
        model.chainWave(leftArmParts, 0.1F, -0.1F, 4, ticks, 0.5F);

        model.faceTarget(rotationYaw, rotationPitch, 2.0F, neck1, head);

        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
