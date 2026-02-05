package net.vit.jurassicreborn.client.render.entity.animation.entity;


import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.CorythosaurusEntity;

public class CorythosaurusAnimator extends EntityAnimator<CorythosaurusEntity> {
    
    protected void performAnimations(AnimatableModel model, CorythosaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {

        AdvancedModelBox head = model.getCube("Head");

        AdvancedModelBox neck1 = model.getCube("Neck1");

        // body parts
        AdvancedModelBox stomach = model.getCube("Body");
        // tail parts
        AdvancedModelBox tail1 = model.getCube("Tail1");
        AdvancedModelBox tail2 = model.getCube("Tail2");
        AdvancedModelBox tail3 = model.getCube("Tail3");
        AdvancedModelBox tail4 = model.getCube("Tail4");

        // right arm
        AdvancedModelBox upperArmRight = model.getCube("RightArm");
        AdvancedModelBox lowerArmRight = model.getCube("RightArm2");
        AdvancedModelBox rightHand = model.getCube("RightArm3");

        // left arm
        AdvancedModelBox upperArmLeft = model.getCube("LeftArm");
        AdvancedModelBox lowerArmLeft = model.getCube("LeftArm2");
        AdvancedModelBox leftHand = model.getCube("LeftArm3");
        AdvancedModelBox[] neck  = new AdvancedModelBox[]{ head, neck1 };
        AdvancedModelBox[] torso = new AdvancedModelBox[]{ stomach };
        AdvancedModelBox[] tail = new AdvancedModelBox[] {tail4, tail3, tail2, tail1 };
        float idleSpeed  = 0.10F;
        float idleDegree = 0.08F;

        float scaleFactor = 0.6F;
        float height = 2F;
        model.chainWave(torso, idleSpeed * 0.65F, idleDegree * 0.45F, 2, ticks, 1.0F);
        model.bob(stomach, idleSpeed, 0.55F, false, ticks, 1.0F);
        model.chainWave(neck, idleSpeed * 0.6F, idleDegree * 0.6F, -2, ticks, 1.0F);
        model.chainSwing(new AdvancedModelBox[]{ head }, idleSpeed * 0.9F, 0.10F, 0, ticks, 1.0F);
        model.bob(head, idleSpeed, 0.035F, false, ticks, 1.0F);

        model.chainWave(tail, 1F * scaleFactor, -0.1F, 2, f, f1);
        model.chainSwing(tail, 0.5F * scaleFactor, 0.1F, 2, f, f1);

        model.chainWave(tail, 0.1F, -0.02F, 2, ticks, 1F);
        model.faceTarget(rotationYaw, rotationPitch, 0.95F, neck1, head);
        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}