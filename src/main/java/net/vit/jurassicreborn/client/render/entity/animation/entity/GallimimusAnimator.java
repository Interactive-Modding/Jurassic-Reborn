package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;

import net.minecraft.client.Minecraft;import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.GallimimusEntity;

@OnlyIn(Dist.CLIENT)
public class GallimimusAnimator extends EntityAnimator<GallimimusEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, GallimimusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelBox neck1 = model.getCube("Neck part 1");
        AdvancedModelBox neck2 = model.getCube("Neck part 2");
        AdvancedModelBox neck3 = model.getCube("Neck part 3");
        AdvancedModelBox neck4 = model.getCube("Neck part 4");
        AdvancedModelBox neck5 = model.getCube("Neck part 5");

        AdvancedModelBox tail1 = model.getCube("Tail part 1");
        AdvancedModelBox tail2 = model.getCube("Tail part 2");
        AdvancedModelBox tail3 = model.getCube("Tail part 3");
        AdvancedModelBox tail4 = model.getCube("Tail part 4");
        AdvancedModelBox tail5 = model.getCube("Tail part 5");
        AdvancedModelBox tail6 = model.getCube("Tail part 6");

        AdvancedModelBox body1 = model.getCube("Body 1");
        AdvancedModelBox body2 = model.getCube("Body 2");
        AdvancedModelBox body3 = model.getCube("Body 3");

        AdvancedModelBox head = model.getCube("Head");

        AdvancedModelBox upperArmLeft = model.getCube("Left upper arm");
        AdvancedModelBox upperArmRight = model.getCube("Right upper arm");

        AdvancedModelBox lowerArmRight = model.getCube("Right lower arm");
        AdvancedModelBox lowerArmLeft = model.getCube("Left lower arm");

        AdvancedModelBox handRight = model.getCube("Right wrist");
        AdvancedModelBox handLeft = model.getCube("Left wrist");

        AdvancedModelBox leftThigh = model.getCube("Left thigh");
        AdvancedModelBox rightThigh = model.getCube("Right thigh");

        AdvancedModelBox leftCalf = model.getCube("Left shin");
        AdvancedModelBox rightCalf = model.getCube("Right shin");

        AdvancedModelBox[] body = new AdvancedModelBox[] { head, neck5, neck4, neck3, neck2, neck1, body1, body2, body3 };

        AdvancedModelBox[] tail = new AdvancedModelBox[] { tail6, tail5, tail4, tail3, tail2, tail1 };

        AdvancedModelBox[] armLeft = new AdvancedModelBox[] { handLeft, lowerArmLeft, upperArmLeft };
        AdvancedModelBox[] armRight = new AdvancedModelBox[] { handRight, lowerArmRight, upperArmRight };

Minecraft mc = Minecraft.getInstance();

float delta = mc.isPaused()
        ? 0.0f
        : mc.getTimer().getGameTimeDeltaPartialTick(false);

        LegArticulator.articulateBiped(entity, entity.legSolver, body1, leftThigh, leftCalf, rightThigh, rightCalf, 1.0F, 1.4F, delta);

        float globalSpeed = 1.5F;
        float globalDegree = 1.0F;

        model.bob(body1, globalSpeed * 0.5F, globalDegree * 1.5F, false, f, f1);
        model.bob(rightThigh, globalSpeed * 0.5F, globalDegree * 1.5F, false, f, f1);
        model.bob(leftThigh, globalSpeed * 0.5F, globalDegree * 1.5F, false, f, f1);

        model.chainWave(tail, globalSpeed * 0.25F, globalDegree * 0.05F, 1, f, f1);
        model.chainSwing(tail, globalSpeed * 0.25F, globalDegree * 0.2F, 2, f, f1);
        model.chainWave(body, globalSpeed * 0.25F, globalDegree * 0.025F, 3, f, f1);

        model.chainWave(tail, 0.1F, 0.05F, 1, ticks, 0.25F);
        model.chainWave(body, 0.1F, -0.05F, 4, ticks, 0.25F);
        model.chainWave(armRight, 0.1F, -0.15F, 4, ticks, 0.25F);
        model.chainWave(armLeft, 0.1F, -0.15F, 4, ticks, 0.25F);

        model.faceTarget(rotationYaw, rotationPitch, 1.0F, neck1, neck2, neck3, neck4, head);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
