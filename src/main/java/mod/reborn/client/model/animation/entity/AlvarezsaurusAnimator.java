package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.model.animation.EntityAnimator;
import mod.reborn.server.entity.dinosaur.CoelurusEntity;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import mod.reborn.server.entity.dinosaur.AlvarezsaurusEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AlvarezsaurusAnimator extends EntityAnimator<AlvarezsaurusEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, AlvarezsaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer body1 = model.getCube("Body ALL");
        AdvancedModelRenderer tail1 = model.getCube("Tail Base");
        AdvancedModelRenderer tail2 = model.getCube("Tail Mid 1");
        AdvancedModelRenderer tail3 = model.getCube("Tail Mid 2");
        AdvancedModelRenderer tail4 = model.getCube("Tail Tip");
        AdvancedModelRenderer leftArm1 = model.getCube("Arm Top Left");
        AdvancedModelRenderer leftArm2 = model.getCube("Arm left 2");
        AdvancedModelRenderer rightArm1 = model.getCube("Arm Top Right");
        AdvancedModelRenderer rightArm2 = model.getCube("Arm right 2");
        AdvancedModelRenderer leftLeg1 = model.getCube("Leg Top Left");
        AdvancedModelRenderer rightLeg1 = model.getCube("Leg Top Right");
        AdvancedModelRenderer leftLeg2 = model.getCube("Leg Mid Left");
        AdvancedModelRenderer rightLeg2 = model.getCube("Leg Mid Right");
        AdvancedModelRenderer leftLeg3 = model.getCube("Leg Bot Left");
        AdvancedModelRenderer rightLeg3 = model.getCube("Leg Bot Right");
        AdvancedModelRenderer leftLeg4 = model.getCube("Foot Left");
        AdvancedModelRenderer rightLeg4 = model.getCube("Foot Right");
        AdvancedModelRenderer neck1 = model.getCube("Neck Base");
        AdvancedModelRenderer neck2 = model.getCube("Neck Mid 1");
        AdvancedModelRenderer neck3 = model.getCube("Neck Mid 2");
        AdvancedModelRenderer neck4 = model.getCube("Throat Base");
        AdvancedModelRenderer head = model.getCube("Upper Head");
        AdvancedModelRenderer upperJaw = model.getCube("Snout");
        AdvancedModelRenderer lowerJaw = model.getCube("Mouthpiece");

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] {tail4, tail3, tail2, tail1};
        AdvancedModelRenderer[] leftArmParts  = new AdvancedModelRenderer[] {leftArm2, leftArm1};
        AdvancedModelRenderer[] rightArmParts  = new AdvancedModelRenderer[] {rightArm2, rightArm1};
        AdvancedModelRenderer[] bodyParts  = new AdvancedModelRenderer[] {body1, neck1, head};
        AdvancedModelRenderer[] headparts  = new AdvancedModelRenderer[] {neck1, neck2, neck3, neck4};


        entity.tailBuffer.applyChainSwingBuffer(tail);



        float globalSpeed = 0.6F;
        float globalDegree = 1.0F;

        float defaultUpperJawRotationX = upperJaw.rotateAngleX;
        float defaultLowerJawRotationX = lowerJaw.rotateAngleX;
        float defaultHeadRotationX = head.rotateAngleX;
        float defaultTailRotationX = tail1.rotateAngleX;



        model.bob(body1, globalSpeed * 0.25F, globalDegree * 1.5F, false, f, f1);

        model.chainWave(tail, globalSpeed * 0.25F, globalDegree * 0.1F, 1, f, f1);
        model.chainSwing(tail, globalSpeed * 0.25F, globalDegree * 0.4F, 2, f, f1);
        model.chainWave(bodyParts, globalSpeed * 0.25F, globalDegree * 0.025F, 3, f, f1);

        model.chainWave(tail, 0.1F, 0.05F, 1, ticks, 0.25F);
        model.chainWave(bodyParts, 0.1F, -0.05F, 4, ticks, 0.25F);

        model.faceTarget(rotationYaw, rotationPitch, 1.0F, neck1, neck2, neck3, neck4, head);

        entity.tailBuffer.applyChainSwingBuffer(tail);

    }

}