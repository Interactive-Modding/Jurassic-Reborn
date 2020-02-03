package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import mod.reborn.server.entity.dinosaur.SuchomimusEntity;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;

public class SuchomimusAnimator extends EntityAnimator<SuchomimusEntity> {

    protected void performAnimations(AnimatableModel parModel, SuchomimusEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {

        AdvancedModelRenderer tail1 = parModel.getCube("Tail Base");
        AdvancedModelRenderer tail2 = parModel.getCube("Tail 2");
        AdvancedModelRenderer tail3 = parModel.getCube("Tail 3");
        AdvancedModelRenderer tail4 = parModel.getCube("Tail 4");
        AdvancedModelRenderer tail5 = parModel.getCube("Tail 6");
        AdvancedModelRenderer neck1 = parModel.getCube("Neck Base");
        AdvancedModelRenderer head = parModel.getCube("Head");

        AdvancedModelRenderer[] tail = {tail5, tail4, tail3, tail2, tail1};

        float globalSpeed = 0.5F;
        float globalDegree = 0.5F;

        parModel.chainWave(tail, globalSpeed * 0.5F, globalDegree * 0.05F, 1, limbSwing, limbSwingAmount);

        parModel.faceTarget(rotationYaw, rotationPitch, 2.0F, neck1, head);
        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
