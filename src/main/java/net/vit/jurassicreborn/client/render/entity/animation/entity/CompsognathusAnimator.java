package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.CompsognathusEntity;

public class CompsognathusAnimator extends EntityAnimator<CompsognathusEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, CompsognathusEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        // cores
        AdvancedModelBox abdomen    = model.getCube("abdomen");
        AdvancedModelBox upperBody  = model.getCube("Upper body");
        AdvancedModelBox head       = model.getCube("Head");

        // neck chain (head last for biggest influence)
        AdvancedModelBox neck1 = model.getCube("Neck 1");
        AdvancedModelBox neck2 = model.getCube("Neck 2");
        AdvancedModelBox neck3 = model.getCube("Neck 3");
        AdvancedModelBox neck4 = model.getCube("Neck 4");
        AdvancedModelBox[] neck = new AdvancedModelBox[]{ head, neck4, neck3, neck2, neck1 };

        // tail chain (tip-first works nicely for chain helpers)
        AdvancedModelBox tail1 = model.getCube("Tail 1");
        AdvancedModelBox tail2 = model.getCube("Tail 2");
        AdvancedModelBox tail3 = model.getCube("Tail 3");
        AdvancedModelBox tail4 = model.getCube("Tail 4");
        AdvancedModelBox tail5 = model.getCube("Tail 5");
        AdvancedModelBox[] tail = new AdvancedModelBox[]{ tail5, tail4, tail3, tail2, tail1 };

        // --- idle tuning ---
        float idleSpeed  = 0.12F;
        float idleDegree = 0.08F;

        // breathing bob on abdomen + slight torso wave
        model.bob(abdomen, idleSpeed, 0.35F, false, ticks, 1.0F);
        model.chainWave(new AdvancedModelBox[]{ abdomen, upperBody }, idleSpeed * 0.8F, idleDegree * 0.4F, 2, ticks, 1.0F);

        // subtle neck undulation + tiny head sway/bob
        model.chainWave(neck, idleSpeed * 0.9F, idleDegree * 0.6F, -2, ticks, 1.0F);

            model.chainSwing(new AdvancedModelBox[]{ head }, idleSpeed, 0.08F, 0, ticks, 1.0F);
            model.bob(head, idleSpeed, 0.03F, false, ticks, 1.0F);


        // tail sway (idle) + a little extra when moving
        model.chainSwing(tail, idleSpeed, 0.15F, -2, ticks, 1.0F);
        if (limbSwingAmount > 0.12F) {
            model.chainSwing(tail, 0.7F, 0.12F, -2, limbSwing, limbSwingAmount);
        }

        // look-at: distribute yaw/pitch down the neck chain, head last
        model.faceTarget(rotationYaw, rotationPitch, 0.95F,
                neck1, neck2, neck3, neck4, head
        );

        // dynamic smoothing if available
        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
