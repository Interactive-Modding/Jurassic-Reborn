package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.AlvarezsaurusEntity;import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AlvarezsaurusAnimator extends EntityAnimator<AlvarezsaurusEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, AlvarezsaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
        {
            AdvancedModelBox body1 = model.getCube("Body ALL");
            AdvancedModelBox tail1 = model.getCube("Tail Base");
            AdvancedModelBox tail2 = model.getCube("Tail Mid 1");
            AdvancedModelBox tail3 = model.getCube("Tail Mid 2");
            AdvancedModelBox tail4 = model.getCube("Tail Tip");
            AdvancedModelBox leftArm1 = model.getCube("Arm Top Left");
            AdvancedModelBox leftArm2 = model.getCube("Arm left 2");
            AdvancedModelBox rightArm1 = model.getCube("Arm Top Right");
            AdvancedModelBox rightArm2 = model.getCube("Arm right 2");
            AdvancedModelBox leftLeg1 = model.getCube("Leg Top Left");
            AdvancedModelBox rightLeg1 = model.getCube("Leg Top Right");
            AdvancedModelBox leftLeg2 = model.getCube("Leg Mid Left");
            AdvancedModelBox rightLeg2 = model.getCube("Leg Mid Right");
            AdvancedModelBox leftLeg3 = model.getCube("Leg Bot Left");
            AdvancedModelBox rightLeg3 = model.getCube("Leg Bot Right");
            AdvancedModelBox leftLeg4 = model.getCube("Foot Left");
            AdvancedModelBox rightLeg4 = model.getCube("Foot Right");
            AdvancedModelBox neck1 = model.getCube("Neck Base");
            AdvancedModelBox neck2 = model.getCube("Neck Mid 1");
            AdvancedModelBox neck3 = model.getCube("Neck Mid 2");
            AdvancedModelBox neck4 = model.getCube("Throat Base");
            AdvancedModelBox head = model.getCube("Upper Head");
            AdvancedModelBox upperJaw = model.getCube("Snout");
            AdvancedModelBox lowerJaw = model.getCube("Mouthpiece");

            AdvancedModelBox[] tail = new AdvancedModelBox[]{tail4, tail3, tail2, tail1};
            AdvancedModelBox[] leftArmParts = new AdvancedModelBox[]{leftArm2, leftArm1};
            AdvancedModelBox[] rightArmParts = new AdvancedModelBox[]{rightArm2, rightArm1};
            AdvancedModelBox[] bodyParts = new AdvancedModelBox[]{body1, neck1, head};
            AdvancedModelBox[] headparts = new AdvancedModelBox[]{neck1, neck2, neck3, neck4};


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
}