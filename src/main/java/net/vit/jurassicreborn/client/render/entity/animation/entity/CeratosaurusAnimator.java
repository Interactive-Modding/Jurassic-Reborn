package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.CeratosaurusEntity;

public class CeratosaurusAnimator extends EntityAnimator<CeratosaurusEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, CeratosaurusEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        // hips/torso root
        AdvancedModelBox bodyHips = model.getCube("bodyhips");

        // neck chain
        AdvancedModelBox head = model.getCube("head");
        AdvancedModelBox neck11 = model.getCube("neck11");
        AdvancedModelBox neck10 = model.getCube("neck10");
        AdvancedModelBox neck9  = model.getCube("neck9");
        AdvancedModelBox neck8  = model.getCube("neck8");
        AdvancedModelBox neck7  = model.getCube("neck7");
        AdvancedModelBox neck6  = model.getCube("neck6");
        AdvancedModelBox neck5  = model.getCube("neck5");
        AdvancedModelBox neck4  = model.getCube("neck4");
        AdvancedModelBox neck3  = model.getCube("neck3");
        AdvancedModelBox neck2  = model.getCube("neck2");
        AdvancedModelBox neck   = model.getCube("neck");

        AdvancedModelBox[] neckChain = new AdvancedModelBox[] {
                head, neck11, neck10, neck9, neck8, neck7, neck6, neck5, neck4, neck3, neck2, neck
        };

        // tail chain
        AdvancedModelBox tail  = model.getCube("tail");
        AdvancedModelBox tail2 = model.getCube("tail2");
        AdvancedModelBox tail3 = model.getCube("tail3");
        AdvancedModelBox tail4 = model.getCube("tail4");
        AdvancedModelBox tail5 = model.getCube("tail5");
        AdvancedModelBox tail6 = model.getCube("tail6");
        AdvancedModelBox tail7 = model.getCube("tail7");
        AdvancedModelBox tail8 = model.getCube("tail8");
        AdvancedModelBox tail9 = model.getCube("tail9");

        AdvancedModelBox[] tailChain = new AdvancedModelBox[] {
                tail9, tail8, tail7, tail6, tail5, tail4, tail3, tail2, tail
        };

        // --- idle breathing ---
        float idleSpeed  = 0.12F;
        float idleDegree = 0.08F;


        model.bob(bodyHips, idleSpeed, 0.55F, false, ticks, 1.0F);

        model.chainWave(neckChain, idleSpeed * 0.6F, idleDegree * 0.3F, -2, ticks, 1.0F);

        // --- tail sway ---
        model.chainSwing(tailChain, idleSpeed, 0.20F, -2, ticks, 1.0F);
        model.faceTarget(rotationYaw, rotationPitch, 0.6F, neck, neck3, neck4, neck5, neck6, neck7, neck8, neck9, neck10,neck11);
        entity.tailBuffer.applyChainSwingBuffer(tailChain);
    }
}
