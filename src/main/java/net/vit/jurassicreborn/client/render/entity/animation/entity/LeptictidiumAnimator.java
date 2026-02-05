package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.LeptictidiumEntity;

public class LeptictidiumAnimator extends EntityAnimator<LeptictidiumEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, LeptictidiumEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        // torso / core
        AdvancedModelBox hips   = model.getCube("bodyhips");
        AdvancedModelBox body   = model.getCube("body");
        AdvancedModelBox belly  = model.getCube("belly");
        AdvancedModelBox head   = model.getCube("head");

        // neck chain (head last for stronger influence)
        AdvancedModelBox neck   = model.getCube("neck");
        AdvancedModelBox neck2  = model.getCube("neck2");
        AdvancedModelBox neck3  = model.getCube("neck3");
        AdvancedModelBox neck4  = model.getCube("neck4");
        AdvancedModelBox neck5  = model.getCube("neck5");
        AdvancedModelBox neck6  = model.getCube("neck6");
        AdvancedModelBox neck7  = model.getCube("neck7");
        AdvancedModelBox neck8  = model.getCube("neck8");
        AdvancedModelBox neck9  = model.getCube("neck9");
        AdvancedModelBox[] neckChain = new AdvancedModelBox[]{
                head, neck9, neck8, neck7, neck6, neck5, neck4, neck3, neck2, neck
        };

        // tail chain (tip → root)
        AdvancedModelBox tail1 = model.getCube("tail");
        AdvancedModelBox tail2 = model.getCube("tail2");
        AdvancedModelBox tail3 = model.getCube("tail3");
        AdvancedModelBox tail4 = model.getCube("tail4");
        AdvancedModelBox tail5 = model.getCube("tail5");
        AdvancedModelBox tail6 = model.getCube("tail6");
        AdvancedModelBox[] tail = new AdvancedModelBox[]{ tail6, tail5, tail4, tail3, tail2, tail1 };

        // --- idle tuning ---
        float idleSpeed  = 0.12F;
        float idleDegree = 0.08F;

        // breathing
        model.bob(belly, idleSpeed, 0.35F, false, ticks, 1.0F);
        model.bob(hips,  idleSpeed, 0.25F, false, ticks, 1.0F);

        model.chainWave(new AdvancedModelBox[]{ hips, body }, idleSpeed * 0.8F, idleDegree * 0.4F, 2, ticks, 1.0F);

        // neck/head subtle motion
        model.chainWave(neckChain, idleSpeed * 0.9F, idleDegree * 0.6F, -2, ticks, 1.0F);

        model.chainSwing(new AdvancedModelBox[]{ head }, idleSpeed, 0.08F, 0, ticks, 1.0F);
        model.bob(head, idleSpeed, 0.03F, false, ticks, 1.0F);


        // tail sway (idle), locomotion overlay when moving
        model.chainSwing(tail, idleSpeed, 0.18F, -2, ticks, 1.0F);
        if (limbSwingAmount > 0.12F) {
            model.chainSwing(tail, 0.7F, 0.12F, -2, limbSwing, limbSwingAmount);
        }

        // look-at (idle tracking)
        model.faceTarget(rotationYaw, rotationPitch, 0.95F,
                neck, neck2, neck3, neck4, neck5, neck6, neck7, neck8, neck9, head
        );

        // smooth tail if buffer exists
        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
