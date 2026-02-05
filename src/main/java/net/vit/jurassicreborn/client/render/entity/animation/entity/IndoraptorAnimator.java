package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.IndoraptorEntity;

public class IndoraptorAnimator extends EntityAnimator<IndoraptorEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, IndoraptorEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        // cores
        AdvancedModelBox hips   = model.getCube("bodyhips");
        AdvancedModelBox body   = model.getCube("body");
        AdvancedModelBox body2  = model.getCube("body2");
        AdvancedModelBox belly  = model.getCube("belly");
        AdvancedModelBox head   = model.getCube("Head");

        // neck chain (base → tip, head last for strongest influence in faceTarget)
        AdvancedModelBox neckBase = model.getCube("Neck Base");

        AdvancedModelBox[] neckChain = new AdvancedModelBox[]{
                head, neckBase
        };

        // tail chain (tip → root)
        AdvancedModelBox tail1 = model.getCube("tail");
        AdvancedModelBox tail2 = model.getCube("tail2");
        AdvancedModelBox tail3 = model.getCube("tail3");
        AdvancedModelBox tail4 = model.getCube("tail4");
        AdvancedModelBox tail5 = model.getCube("tail5");
        AdvancedModelBox tail6 = model.getCube("tail6");
        AdvancedModelBox tail7 = model.getCube("tail7");
        AdvancedModelBox tail8 = model.getCube("tail8");
        AdvancedModelBox tail9 = model.getCube("tail9");
        AdvancedModelBox[] tail = new AdvancedModelBox[]{ tail9, tail8, tail7, tail6, tail5, tail4, tail3, tail2, tail1 };

        // --- idle tuning ---
        float idleSpeed  = 0.12F;
        float idleDegree = 0.08F;

        // breathing through torso
        model.bob(belly, idleSpeed, 0.45F, false, ticks, 1.0F);
        model.bob(hips,  idleSpeed, 0.30F, false, ticks, 1.0F);
        model.chainWave(new AdvancedModelBox[]{ hips, body, body2 }, idleSpeed * 0.7F, idleDegree * 0.4F, 2, ticks, 1.0F);

        // neck/head subtle motion
        model.chainWave(neckChain, idleSpeed * 0.8F, idleDegree * 0.6F, -2, ticks, 1.0F);
        model.chainSwing(new AdvancedModelBox[]{ head }, idleSpeed, 0.10F, 0, ticks, 1.0F);
        model.bob(head, idleSpeed, 0.035F, false, ticks, 1.0F);

        // tail sway (idle) + locomotion layer
        model.chainSwing(tail, idleSpeed, 0.20F, -2, ticks, 1.0F);
        if (limbSwingAmount > 0.12F) {
            model.chainSwing(tail, 0.3F, 0.05F, -2, limbSwing, limbSwingAmount);
        }

        model.faceTarget(rotationYaw, rotationPitch, 0.9F, neckBase, head);

        // tail smoothing
        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
