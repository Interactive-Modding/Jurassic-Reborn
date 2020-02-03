package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.MicroraptorEntity;

@SideOnly(Side.CLIENT)
public class MicroraptorAnimator extends EntityAnimator<MicroraptorEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, MicroraptorEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        if (entity.getAnimation() == EntityAnimation.GLIDING.get()) {
            GlStateManager.rotate(rotationPitch, 1.0F, 0.0F, 0.0F);
        }

        AdvancedModelRenderer upperArmRight = model.getCube("RightArm1");
        AdvancedModelRenderer lowerArmRight = model.getCube("RightArm2");
        AdvancedModelRenderer rightHand = model.getCube("RightArm3");
        AdvancedModelRenderer upperArmLeft = model.getCube("LeftArm1");
        AdvancedModelRenderer lowerArmLeft = model.getCube("LeftArm2");
        AdvancedModelRenderer leftHand = model.getCube("LeftArm3");

        AdvancedModelRenderer rightThigh = model.getCube("RightLeg1");
        AdvancedModelRenderer leftThigh = model.getCube("LeftLeg1");

        AdvancedModelRenderer tail1 = model.getCube("Tail1");
        AdvancedModelRenderer tail2 = model.getCube("Tail1");
        AdvancedModelRenderer tail3 = model.getCube("Tail1");
        AdvancedModelRenderer tail4 = model.getCube("Tail1");

        AdvancedModelRenderer shoulders = model.getCube("Body1");
        AdvancedModelRenderer waist = model.getCube("Body2");

        AdvancedModelRenderer neck1 = model.getCube("Neck1");
        AdvancedModelRenderer neck2 = model.getCube("Neck2");
        AdvancedModelRenderer head = model.getCube("Head");

        AdvancedModelRenderer[] rightArmParts = new AdvancedModelRenderer[] { rightHand, lowerArmRight, upperArmRight };
        AdvancedModelRenderer[] leftArmParts = new AdvancedModelRenderer[] { leftHand, lowerArmLeft, upperArmLeft };
        AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[] { tail4, tail3, tail2, tail1 };
        AdvancedModelRenderer[] bodyParts = new AdvancedModelRenderer[] { waist, shoulders, neck1, neck2, head };

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
