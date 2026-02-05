package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.NigersaurusEntity;

public class NigersaurusAnimator extends EntityAnimator<NigersaurusEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, NigersaurusEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        AdvancedModelBox hips  = model.getCube("Niger");
        AdvancedModelBox body  = model.getCube("Body");
        AdvancedModelBox head  = model.getCube("Head");

        AdvancedModelBox neck1 = model.getCube("Neck1");
        AdvancedModelBox neck2 = model.getCube("Neck2");
        AdvancedModelBox neck3 = model.getCube("Neck3");
        AdvancedModelBox neck4 = model.getCube("Neck4");

        AdvancedModelBox tail1 = model.getCube("Tail1");
        AdvancedModelBox tail2 = model.getCube("Tail2");
        AdvancedModelBox tail3 = model.getCube("Tail3");
        AdvancedModelBox tail4 = model.getCube("bone");
        AdvancedModelBox tail5 = model.getCube("bone2");
        AdvancedModelBox tail6 = model.getCube("bone12");

        AdvancedModelBox[] neckParts = new AdvancedModelBox[] { head, neck4, neck3, neck2, neck1 };
        AdvancedModelBox[] tailParts = new AdvancedModelBox[] { tail6, tail5, tail4, tail3, tail2, tail1 };

        float idleSpeed  = 0.10F;
        float idleDegree = 0.08F;

        model.bob(hips, idleSpeed, 0.6F, false, ticks, 1.0F);

        model.chainWave(neckParts, idleSpeed * 0.6F, idleDegree * 0.4F, -2, ticks, 1.0F);

        model.chainSwing(tailParts, idleSpeed, 0.16F, -2, ticks, 1.0F);

        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}

