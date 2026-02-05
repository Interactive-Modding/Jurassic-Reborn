package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.CamarasaurusEntity;

public class CamarasaurusAnimator extends EntityAnimator<CamarasaurusEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, CamarasaurusEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        AdvancedModelBox hips  = model.getCube("hips");
        AdvancedModelBox body  = model.getCube("body");
        AdvancedModelBox head  = model.getCube("head");

        // neck chain
        AdvancedModelBox neck1 = model.getCube("neck1");
        AdvancedModelBox neck2 = model.getCube("neck2");
        AdvancedModelBox neck3 = model.getCube("neck3");
        AdvancedModelBox neck4 = model.getCube("neck4");
        AdvancedModelBox neck5 = model.getCube("neck5");
        AdvancedModelBox neck6 = model.getCube("neck6");
        AdvancedModelBox neck7 = model.getCube("neck7");
        AdvancedModelBox neck8 = model.getCube("neck8");

        // tail chain
        AdvancedModelBox tail1 = model.getCube("tail1");
        AdvancedModelBox tail2 = model.getCube("tail2");
        AdvancedModelBox tail3 = model.getCube("tail3");
        AdvancedModelBox tail4 = model.getCube("tail4");
        AdvancedModelBox tail5 = model.getCube("tail5");
        AdvancedModelBox tail6 = model.getCube("tail6");
        AdvancedModelBox tail7 = model.getCube("tail7");

        AdvancedModelBox[] neckParts = new AdvancedModelBox[] { head, neck8, neck7, neck6, neck5, neck4, neck3, neck2, neck1 };
        AdvancedModelBox[] tailParts = new AdvancedModelBox[] { tail7, tail6, tail5, tail4, tail3, tail2, tail1 };

        // --- idle breathing ---
        float idleSpeed  = 0.10F;
        float idleDegree = 0.08F;

        // hips/body breathing bob
        model.bob(hips, idleSpeed, 0.6F, false, ticks, 1.0F);

        // subtle neck wave (up/down)
        model.chainWave(neckParts, idleSpeed * 0.6F, idleDegree * 0.4F, -2, ticks, 1.0F);

        // --- idle tail sway (left/right) ---
        model.chainSwing(tailParts, idleSpeed, 0.16F, -2, ticks, 1.0F);

        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
