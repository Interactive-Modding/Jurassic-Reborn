package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import mod.reborn.server.entity.dinosaur.GuanlongEntity;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;

public class GuanlongAnimator extends EntityAnimator<GuanlongEntity> {

    protected void performAnimations(AnimatableModel model, GuanlongEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
        float scaleFactor = 0.77F;
        float height = 2F * f1;

        AdvancedModelRenderer head = model.getCube("Head");

        AdvancedModelRenderer neck1 = model.getCube("Neck BASE");

        AdvancedModelRenderer tail1 = model.getCube("Tail BASE");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
        AdvancedModelRenderer tail6 = model.getCube("Tail 6");

        AdvancedModelRenderer upperArmLeft = model.getCube("Upper Arm LEFT");
        AdvancedModelRenderer upperArmRight = model.getCube("Upper Arm Right");

        AdvancedModelRenderer lowerArmLeft = model.getCube("Lower Arm Left");
        AdvancedModelRenderer lowerArmRight = model.getCube("Lower Arm Right");

        AdvancedModelRenderer handLeft = model.getCube("hand left");
        AdvancedModelRenderer handRight = model.getCube("hand right");

        AdvancedModelRenderer[] leftArmParts = new AdvancedModelRenderer[] { handLeft, lowerArmLeft, upperArmLeft };
        AdvancedModelRenderer[] rightArmParts = new AdvancedModelRenderer[] { handRight, lowerArmRight, upperArmRight };

        AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };

        AdvancedModelRenderer body1 = model.getCube("Body REAR");

        AdvancedModelRenderer leftThigh = model.getCube("Leg TOP LEFT");
        AdvancedModelRenderer rightThigh = model.getCube("Leg TOP RIGHT");

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
