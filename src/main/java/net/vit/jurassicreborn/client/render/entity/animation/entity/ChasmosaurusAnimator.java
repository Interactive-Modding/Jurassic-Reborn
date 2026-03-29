package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.ChasmosaurusEntity;import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChasmosaurusAnimator extends EntityAnimator<ChasmosaurusEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, ChasmosaurusEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        // --- parts from Tabula model ---
        AdvancedModelBox body1 = model.getCube("body 1");
        AdvancedModelBox body2 = model.getCube("body 2");
        AdvancedModelBox body3 = model.getCube("body 3");
        AdvancedModelBox neck  = model.getCube("neck");
        AdvancedModelBox head  = model.getCube("head");

        AdvancedModelBox tail1 = model.getCube("tail 1");
        AdvancedModelBox tail2 = model.getCube("tail 2");
        AdvancedModelBox tail3 = model.getCube("tail 3");
        AdvancedModelBox tail4 = model.getCube("tail 4");
        AdvancedModelBox tail5 = model.getCube("tail 5");
        AdvancedModelBox tail6 = model.getCube("tail 6");
        AdvancedModelBox tail7 = model.getCube("tail 7");

        AdvancedModelBox[] neckChain = new AdvancedModelBox[] { head, neck };
        AdvancedModelBox[] bodyChain = new AdvancedModelBox[] { body1, body2, body3 };
        AdvancedModelBox[] tailChain = new AdvancedModelBox[] { tail7, tail6, tail5, tail4, tail3, tail2, tail1 };

        // --- tuning ---
        float idleSpeed  = 0.10F;  // frequency
        float idleDegree = 0.08F;  // amplitude

        // --- idle breathing ---
        model.bob(body1, idleSpeed, 0.65F, false, ticks, 1.0F);
        model.chainWave(bodyChain, idleSpeed * 0.6F, idleDegree * 0.35F, 2, ticks, 1.0F);

        // --- neck & head subtle motion ---
        model.chainWave(neckChain, idleSpeed * 0.6F, idleDegree * 0.6F, -2, ticks, 1.0F);
            model.chainSwing(new AdvancedModelBox[]{ head }, idleSpeed * 0.9F, 0.10F, 0, ticks, 1.0F);
            model.bob(head, idleSpeed, 0.04F, false, ticks, 1.0F);


        model.chainSwing(tailChain, idleSpeed, 0.16F, -2, ticks, 1.0F);

        if (limbSwingAmount > 0.15F) {model.chainSwing(tailChain, 0.6F, 0.12F, -2, limbSwing, limbSwingAmount);}

        model.faceTarget(rotationYaw, rotationPitch, 0.9F, neck, head);

        entity.tailBuffer.applyChainSwingBuffer(tailChain);
    }
}
