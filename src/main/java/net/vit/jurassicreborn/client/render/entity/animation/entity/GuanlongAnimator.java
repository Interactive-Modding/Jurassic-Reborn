package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.GuanlongEntity;


public class GuanlongAnimator extends EntityAnimator<GuanlongEntity> {

    protected void performAnimations(AnimatableModel model, GuanlongEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
        float scaleFactor = 0.77F;
        float height = 2F * f1;

        AdvancedModelBox head = model.getCube("Head");

        AdvancedModelBox neck1 = model.getCube("Neck BASE");

        AdvancedModelBox tail1 = model.getCube("Tail BASE");
        AdvancedModelBox tail2 = model.getCube("Tail 2");
        AdvancedModelBox tail3 = model.getCube("Tail 3");
        AdvancedModelBox tail4 = model.getCube("Tail 4");
        AdvancedModelBox tail5 = model.getCube("Tail 5");
        AdvancedModelBox tail6 = model.getCube("Tail 6");

        AdvancedModelBox upperArmLeft = model.getCube("Upper Arm LEFT");
        AdvancedModelBox upperArmRight = model.getCube("Upper Arm Right");

        AdvancedModelBox lowerArmLeft = model.getCube("Lower Arm Left");
        AdvancedModelBox lowerArmRight = model.getCube("Lower Arm Right");

        AdvancedModelBox handLeft = model.getCube("hand left");
        AdvancedModelBox handRight = model.getCube("hand right");

        AdvancedModelBox[] leftArmParts = new AdvancedModelBox[] { handLeft, lowerArmLeft, upperArmLeft };
        AdvancedModelBox[] rightArmParts = new AdvancedModelBox[] { handRight, lowerArmRight, upperArmRight };

        AdvancedModelBox[] tailParts = new AdvancedModelBox[] { tail6, tail5, tail4, tail3, tail2, tail1 };

        AdvancedModelBox body1 = model.getCube("Body REAR");

        AdvancedModelBox leftThigh = model.getCube("Leg TOP LEFT");
        AdvancedModelBox rightThigh = model.getCube("Leg TOP RIGHT");

        model.bob(body1, 0.4F * scaleFactor, height, false, f, f1);
        model.bob(neck1, 0.4F * scaleFactor, height / 2, false, f, f1);

        model.chainSwing(tailParts, 0.5F * scaleFactor, -0.1F, 2, f, f1);
        model.chainWave(tailParts, 1F * scaleFactor, -0.03F, 2, f, f1);
        model.chainWave(rightArmParts, 0.4F * scaleFactor, -0.3F, 4, f, f1);
        model.chainWave(leftArmParts, 0.4F * scaleFactor, -0.3F, 4, f, f1);

        model.chainWave(tailParts, 0.1F, -0.05F, 2, ticks, 0.25F);
        model.walk(neck1, 0.1F, 0.07F, false, -1F, 0F, ticks, 0.25F);
        model.walk(head, 0.1F, 0.07F, true, 0F, 0F, ticks, 0.25F);
        model.walk(body1, 0.1F, 0.05F, false, 0F, 0F, ticks, 0.25F);
        model.chainWave(rightArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);
        model.chainWave(leftArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);
        model.chainSwing(tailParts, 0.1F, -0.1F, 3, ticks, 0.25F);

        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
