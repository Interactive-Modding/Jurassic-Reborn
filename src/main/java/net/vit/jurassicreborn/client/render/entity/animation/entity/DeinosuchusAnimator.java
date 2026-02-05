package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.DeinosuchusEntity;

public class DeinosuchusAnimator extends EntityAnimator<DeinosuchusEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, DeinosuchusEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        AdvancedModelBox body  = model.getCube("Body");
        AdvancedModelBox neck1 = model.getCube("Neck1");
        AdvancedModelBox head  = model.getCube("Head");
        AdvancedModelBox jaw   = model.getCube("Jaw");

        AdvancedModelBox tail1 = model.getCube("Tail1");
        AdvancedModelBox tail2 = model.getCube("Tail2");
        AdvancedModelBox tail3 = model.getCube("Tail3");
        AdvancedModelBox tail4 = model.getCube("Tail4");
        AdvancedModelBox tail5 = model.getCube("Tail5");

        AdvancedModelBox fl = model.getCube("FrontLeftLeg");
        AdvancedModelBox fr = model.getCube("FrontRightLeg");
        AdvancedModelBox bl = model.getCube("BackLeftLeg");
        AdvancedModelBox br = model.getCube("BackRightLeg");

        AdvancedModelBox[] neckChain = new AdvancedModelBox[] {neck1, head};
        AdvancedModelBox[] tailChain = new AdvancedModelBox[] {tail5, tail4, tail3, tail2, tail1};

        // ===== Tunables =====
        float idleSpeed  = 0.10F;
        float idleDeg    = 0.10F;

        float walkSpeed  = 0.70F;
        float walkDeg    = 0.45F;

        float runSpeed   = 1.20F;
        float runDeg     = 0.70F;

        float swimSpeed  = 0.90F;
        float swimDeg    = 0.45F;

        // Tail was 75% too fast → use 1/4 frequency
        float tailIdleFreq = idleSpeed  / 4.0F;
        float tailWalkFreq = walkSpeed  / 4.0F;
        float tailRunFreq  = runSpeed   / 4.0F;
        float tailSwimFreq = swimSpeed  / 8.0F;

        boolean swimming = entity.isInWater();
        boolean running  = !swimming && (entity.isSprinting() || limbSwingAmount > 0.55F);
        boolean walking  = !swimming && !running && limbSwingAmount > 0.08F;

        // ===== Idle (breathing & life) =====
        model.bob(body, idleSpeed, 0.05F, false, ticks, 1.0F);
        model.chainWave(neckChain, idleSpeed * 0.9F, idleDeg * 0.35F, -2, ticks, 1.0F);
        model.chainSwing(neckChain, idleSpeed * 0.8F, idleDeg * 0.25F, -2, ticks, 1.0F);
        if ( !entity.isCarcass()) {
            jaw.rotateAngleX += (float) Math.sin(ticks * 0.07F) * 0.02F;
        }
        // face target via neck
//        model.faceTarget(rotationYaw, rotationPitch, 1.0F, neck1);

        // Tail idle sway (slowed)
        model.chainSwing(tailChain, tailIdleFreq, 0.18F, -2, ticks, 1.0F);
        model.chainWave (tailChain, tailIdleFreq * 0.8F, 0.05F, -2, ticks, 1.0F);

        // ===== LAND: Walk (FL+BR vs FR+BL) =====
        if (walking) {
            model.chainSwing(tailChain, tailWalkFreq, 0.22F, -2, limbSwing, limbSwingAmount);
            model.chainWave (tailChain, tailWalkFreq * 0.9F, 0.06F, -2, limbSwing, limbSwingAmount);
        }

        // ===== LAND: Run =====
        if (running) {
            model.chainSwing(tailChain, tailRunFreq, 0.24F, -2, limbSwing, limbSwingAmount);
            model.chainWave (tailChain, tailRunFreq * 0.9F, 0.07F, -2, limbSwing, limbSwingAmount);
        }

        // ===== WATER: Swim =====
        if (swimming) {
            model.chainSwing(tailChain, tailSwimFreq, 0.45F, -2, ticks, 1.0F);
            model.chainWave (tailChain, tailSwimFreq * 0.5F, 0.15F, -2, ticks, 1.0F);
        }


    }
}
