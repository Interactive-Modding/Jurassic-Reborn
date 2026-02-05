package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.DeinotheriumEntity;

public class DeinotheriumAnimator extends EntityAnimator<DeinotheriumEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, DeinotheriumEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        // torso segments
        AdvancedModelBox bodyBack    = model.getCube("Body back");
        AdvancedModelBox bodyMiddle  = model.getCube("Body middle");
        AdvancedModelBox bodyFront   = model.getCube("Body front");

        // neck & head
        AdvancedModelBox neck1 = model.getCube("Neck 1");
        AdvancedModelBox neck2 = model.getCube("Neck 2");
        AdvancedModelBox head  = model.getCube("Head");
        AdvancedModelBox[] neck = new AdvancedModelBox[]{ head, neck2, neck1 };

        // tail
        AdvancedModelBox tail1 = model.getCube("Tail 1");
        AdvancedModelBox tail2 = model.getCube("Tail 2");
        AdvancedModelBox tail3 = model.getCube("Tail 3");
        AdvancedModelBox[] tail = new AdvancedModelBox[]{ tail3, tail2, tail1 };

        // --- idle tuning (large mammal: slower/larger breathing) ---
        float idleSpeed  = 0.08F;
        float idleDegree = 0.10F;

        // body breathing: bob the midsection, wave through torso
        model.bob(bodyMiddle, idleSpeed, 0.05F, false, ticks, 0.3F);
        model.chainWave(new AdvancedModelBox[]{ bodyBack, bodyMiddle, bodyFront }, idleSpeed * 0.2F, idleDegree * 0.1F, 2, ticks, 1.0F);

        model.chainWave(neck, idleSpeed * 0.6F, idleDegree * 0.5F, -2, ticks, 1.0F);
        model.chainSwing(new AdvancedModelBox[]{ head }, idleSpeed * 0.9F, 0.08F, 0, ticks, 1.0F);
        model.bob(head, idleSpeed, 0.04F, false, ticks, 1.0F);


        // tail sway (short tail, keep it subtle)
        model.chainSwing(tail, idleSpeed, 0.12F, -2, ticks, 1.0F);

        // look-at: steer with necks, head last
        model.faceTarget(rotationYaw, rotationPitch, 0.85F,
                neck1, neck2, head
        );

        // smoothing if available on the entity
        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
