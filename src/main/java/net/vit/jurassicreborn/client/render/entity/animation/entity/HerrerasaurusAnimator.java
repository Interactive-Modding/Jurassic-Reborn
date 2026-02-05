package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.HerrerasaurusEntity;

public class HerrerasaurusAnimator extends EntityAnimator<HerrerasaurusEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, HerrerasaurusEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        // torso
        AdvancedModelBox body1 = model.getCube("Body 1");
        AdvancedModelBox body2 = model.getCube("Body 2");
        AdvancedModelBox body3 = model.getCube("Body 3");

        // neck & head (head last for strongest influence)
        AdvancedModelBox neck1 = model.getCube("Neck 1");
        AdvancedModelBox neck2 = model.getCube("Neck 2");
        AdvancedModelBox neck3 = model.getCube("Neck 3");
        AdvancedModelBox neck4 = model.getCube("Neck 4");
        AdvancedModelBox neck5 = model.getCube("Neck 5");
        AdvancedModelBox neck6 = model.getCube("Neck 6");
        AdvancedModelBox neck7 = model.getCube("Neck 7");
        AdvancedModelBox head  = model.getCube("Head");

        AdvancedModelBox[] neckChain = new AdvancedModelBox[] {
                head, neck7, neck6, neck5, neck4, neck3, neck2, neck1
        };

        // tail (tip-first → root)
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

        // ---- idle tuning ----
        float idleSpeed  = 0.12F;
        float idleDegree = 0.08F;

        // breathing through torso
        if (body1 != null) model.bob(body1, idleSpeed, 0.45F, false, ticks, 1.0F);
        model.chainWave(new AdvancedModelBox[]{ body1, body2, body3 }, idleSpeed * 0.7F, idleDegree * 0.4F, 2, ticks, 1.0F);

        // neck/head subtle motion
        model.chainWave(neckChain, idleSpeed * 0.8F, idleDegree * 0.6F, -2, ticks, 1.0F);
        if (head != null) {
            model.chainSwing(new AdvancedModelBox[]{ head }, idleSpeed, 0.10F, 0, ticks, 1.0F);
            model.bob(head, idleSpeed, 0.03F, false, ticks, 1.0F);
        }

        // tail sway (idle), plus locomotion overlay when moving
        model.chainSwing(tail, idleSpeed, 0.10F, -2, ticks, 1.0F);
        if (limbSwingAmount > 0.12F) {
            model.chainSwing(tail, 0.7F, 0.12F, -2, limbSwing, limbSwingAmount);
        }

        // look-at: distribute yaw/pitch down the neck; head last
        model.faceTarget(rotationYaw, rotationPitch, 0.9F, neck1);

        // smoothing (if entity provides it)
        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
