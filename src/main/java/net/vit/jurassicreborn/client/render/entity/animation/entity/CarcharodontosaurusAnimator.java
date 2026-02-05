package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.CarcharodontosaurusEntity;

public class CarcharodontosaurusAnimator extends EntityAnimator<CarcharodontosaurusEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, CarcharodontosaurusEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        // torso / root
        AdvancedModelBox body1 = model.getCube("Body 1");

        // neck chain (head-first order works best for chain helpers)
        AdvancedModelBox head   = model.getCube("Head");
        AdvancedModelBox neck6  = model.getCube("Neck6");
        AdvancedModelBox neck5  = model.getCube("Neck5");
        AdvancedModelBox neck4  = model.getCube("Neck4");
        AdvancedModelBox neck3  = model.getCube("Neck3");
        AdvancedModelBox neck2  = model.getCube("Neck2");
        AdvancedModelBox neck1  = model.getCube("Neck1");

        AdvancedModelBox[] neck = new AdvancedModelBox[] {
                head, neck6, neck5, neck4, neck3, neck2, neck1
        };

        // tail chain (tip-first order looks smoother)
        AdvancedModelBox tail1 = model.getCube("Tail 1");
        AdvancedModelBox tail2 = model.getCube("Tail 2");
        AdvancedModelBox tail3 = model.getCube("Tail 3");
        AdvancedModelBox tail4 = model.getCube("Tail 4");
        AdvancedModelBox tail5 = model.getCube("Tail 5");
        AdvancedModelBox tail6 = model.getCube("Tail 6");
        AdvancedModelBox tail7 = model.getCube("Tail 7");

        AdvancedModelBox[] tail = new AdvancedModelBox[] {
                tail7, tail6, tail5, tail4, tail3, tail2, tail1
        };

        // --- idle breathing ---
        float idleSpeed  = 0.10F;  // frequency
        float idleDegree = 0.08F;  // amplitude

        if (body1 != null) {
            // gentle body bob
            model.bob(body1, idleSpeed, 0.6F, false, ticks, 1.0F);
        }
        // subtle neck undulation (up/down)
        model.chainWave(neck, idleSpeed * 0.6F, idleDegree * 0.6F, -2, ticks, 1.0F);

        // --- tail sway (left/right) ---
        model.chainSwing(tail, idleSpeed, 0.18F, -2, ticks, 1.0F);
        model.faceTarget(rotationYaw, rotationPitch, 0.6F, neck1, neck3, neck4, neck5, neck6);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
