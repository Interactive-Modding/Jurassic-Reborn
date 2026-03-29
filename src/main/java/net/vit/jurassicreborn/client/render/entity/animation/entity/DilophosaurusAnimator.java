package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.DilophosaurusEntity;

@OnlyIn(Dist.CLIENT)
public class DilophosaurusAnimator extends EntityAnimator<DilophosaurusEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, DilophosaurusEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelBox RightFrill = model.getCube("RightFrill1");

        AdvancedModelBox LeftFrill = model.getCube("LeftFrill1");

        boolean hasTarget = entity.hasTarget() && !entity.isCarcass();

        LeftFrill.showModel = hasTarget;
        RightFrill.showModel = hasTarget;

        AdvancedModelBox head = model.getCube("Head");

        AdvancedModelBox neck1 = model.getCube("Neck1");
        AdvancedModelBox neck2 = model.getCube("Neck2");
        AdvancedModelBox neck3 = model.getCube("Neck3");
        AdvancedModelBox neck4 = model.getCube("Neck4");

        AdvancedModelBox body1 = model.getCube("Body");

        AdvancedModelBox tail1 = model.getCube("Tail1");
        AdvancedModelBox tail2 = model.getCube("Tail2");
        AdvancedModelBox tail3 = model.getCube("Tail3");
        AdvancedModelBox tail4 = model.getCube("Tail4");
        AdvancedModelBox tail5 = model.getCube("Tail5");
        AdvancedModelBox tail6 = model.getCube("Tail6");

        AdvancedModelBox rightThigh = model.getCube("rightleg");
        AdvancedModelBox leftThigh = model.getCube("leftleg");

        AdvancedModelBox upperArmRight = model.getCube("RightArm");
        AdvancedModelBox upperArmLeft = model.getCube("LeftArm");
        AdvancedModelBox[] bodyParts = new AdvancedModelBox[] { head, neck4, neck3, neck2, neck1, body1 };
        AdvancedModelBox[] tailParts = new AdvancedModelBox[] { tail6, tail5, tail4, tail3, tail2, tail1 };

        AdvancedModelBox[] armRight = new AdvancedModelBox[] { upperArmRight };
        AdvancedModelBox[] armLeft = new AdvancedModelBox[] { upperArmLeft };

        float globalSpeed = 1.0F;
        float globalDegree = 1.0F;


        model.bob(rightThigh, globalSpeed * 0.5F, globalDegree * 1.0F, false, limbSwing, limbSwingAmount);
        model.bob(leftThigh, globalSpeed * 0.5F, globalDegree * 1.0F, false, limbSwing, limbSwingAmount);

        model.chainWave(tailParts, globalSpeed * 0.5F, globalDegree * 0.05F, 1, limbSwing, limbSwingAmount);
        model.chainSwing(tailParts, globalSpeed * 0.5F, globalDegree * 0.1F, 2, limbSwing, limbSwingAmount);
        model.chainWave(bodyParts, globalSpeed * 0.5F, globalDegree * 0.025F, 3, limbSwing, limbSwingAmount);

        model.chainWave(tailParts, 0.15F, -0.03F, 2, ticks, 0.25F);
        model.chainWave(bodyParts, 0.15F, 0.03F, 3.5F, ticks, 0.25F);
        model.chainWave(armRight, 0.15F, -0.1F, 4, ticks, 0.25F);
        model.chainWave(armLeft, 0.15F, -0.1F, 4, ticks, 0.25F);
        model.chainSwing(tailParts, 0.15F, -0.1F, 3, ticks, 0.25F);

        model.faceTarget(rotationYaw, rotationPitch, 1.0F, neck1, head);

        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
