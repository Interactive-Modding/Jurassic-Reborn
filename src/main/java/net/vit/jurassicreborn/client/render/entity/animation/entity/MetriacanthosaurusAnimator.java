package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MetriacanthosaurusEntity;

public class MetriacanthosaurusAnimator extends EntityAnimator<MetriacanthosaurusEntity> {

    private static AdvancedModelBox[] nn(AdvancedModelBox... parts) {
        java.util.ArrayList<AdvancedModelBox> out = new java.util.ArrayList<>();
        for (AdvancedModelBox p : parts) if (p != null) out.add(p);
        return out.toArray(new AdvancedModelBox[0]);
    }

    @Override
    protected void performAnimations(AnimatableModel model, MetriacanthosaurusEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        AdvancedModelBox hips  = model.getCube("Body Rear");
        AdvancedModelBox body1 = model.getCube("Body Mid");
        AdvancedModelBox body2 = model.getCube("Body Front");

        AdvancedModelBox neck1 = model.getCube("Neck BASE");
        AdvancedModelBox neck2 = model.getCube("Neck 2");
        AdvancedModelBox neck3 = model.getCube("Neck 3");
        AdvancedModelBox neck4 = model.getCube("Neck 4");
        AdvancedModelBox head  = model.getCube("Head");

        AdvancedModelBox tail1 = model.getCube("Tail Base");
        AdvancedModelBox tail2 = model.getCube("Tail 2");
        AdvancedModelBox tail3 = model.getCube("Tail 3");
        AdvancedModelBox tail4 = model.getCube("Tail 4");
        AdvancedModelBox tail5 = model.getCube("Tail 5");
        AdvancedModelBox tail6 = model.getCube("Tail 6");
        AdvancedModelBox tail7 = model.getCube("Tail 7");
        AdvancedModelBox tail8 = model.getCube("Tail 8");

        AdvancedModelBox[] torso = nn(hips, body1, body2);
        AdvancedModelBox[] neck  = nn(head, neck4, neck3, neck2, neck1);
        AdvancedModelBox[] tail  = nn(tail8, tail7, tail6, tail5, tail4, tail3, tail2, tail1);

        float idleSpeed  = 0.12F;
        float idleDegree = 0.08F;

        // breathing
        if (body1 != null) model.bob(body1, idleSpeed, 0.45F, false, ticks, 1.0F);
        if (torso.length > 1) model.chainWave(torso, idleSpeed * 0.7F, idleDegree * 0.4F, 2, ticks, 1.0F);

        // neck/head idle motion
        if (neck.length > 0) model.chainWave(neck, idleSpeed * 0.8F, idleDegree * 0.6F, -2, ticks, 1.0F);
        if (head != null) {
            model.chainSwing(new AdvancedModelBox[]{ head }, idleSpeed, 0.10F, 0, ticks, 1.0F);
            model.bob(head, idleSpeed, 0.03F, false, ticks, 1.0F);
        }

        // tail sway + locomotion overlay
        if (tail.length > 0) {
            model.chainSwing(tail, idleSpeed, 0.20F, -2, ticks, 1.0F);
            if (limbSwingAmount > 0.12F)
                model.chainSwing(tail, 0.7F, 0.14F, -2, limbSwing, limbSwingAmount);
            entity.tailBuffer.applyChainSwingBuffer(tail);
        }

        // face target (idle tracking)
        model.faceTarget(rotationYaw, rotationPitch, 0.9F,
                nn(neck1, neck2, neck3, neck4, head));
    }
}
