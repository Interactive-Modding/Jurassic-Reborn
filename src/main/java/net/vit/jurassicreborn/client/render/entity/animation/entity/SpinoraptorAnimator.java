package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.SpinoraptorEntity;

@OnlyIn(Dist.CLIENT)
public class SpinoraptorAnimator extends EntityAnimator<SpinoraptorEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, SpinoraptorEntity entity,
                                     float f, float f1, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        // --- body cores ---
        AdvancedModelBox bodyRear   = model.getCube("Body Rear");
        AdvancedModelBox bodyMiddle = model.getCube("Body Middle");

        // --- tail chain (tip -> base) ---
        AdvancedModelBox tailBase = model.getCube("Tail Base");
        AdvancedModelBox tail2    = model.getCube("Tail 2");
        AdvancedModelBox tail3    = model.getCube("Tail 3");
        AdvancedModelBox tail4    = model.getCube("Tail 4");
        AdvancedModelBox tail6    = model.getCube("Tail 6");
        AdvancedModelBox tail7    = model.getCube("Tail 7");
        AdvancedModelBox tail8    = model.getCube("Tail 8");
        AdvancedModelBox tail9    = model.getCube("Tail 9");
        AdvancedModelBox tail10   = model.getCube("Tail 10");

        AdvancedModelBox[] tail = new AdvancedModelBox[]{
                tail10, tail9, tail8, tail7, tail6, tail4, tail3, tail2, tailBase
        };

        // --- neck/head chain ---
        AdvancedModelBox neckBase = model.getCube("Neck Base");
        AdvancedModelBox neck2    = model.getCube("Neck 2");
        AdvancedModelBox neck3    = model.getCube("Neck 3");
        AdvancedModelBox neck4    = model.getCube("Neck 4");
        AdvancedModelBox neck5    = model.getCube("Neck 5");
        AdvancedModelBox neck6    = model.getCube("Neck 6");
        AdvancedModelBox neck7    = model.getCube("Neck 7");
        AdvancedModelBox neck8    = model.getCube("Neck 8");
        AdvancedModelBox neck9    = model.getCube("Neck 9");
        AdvancedModelBox head     = model.getCube("Head");

        AdvancedModelBox[] bodyParts = new AdvancedModelBox[]{bodyRear, bodyMiddle, neckBase, head};

        // --- jaws (kept for possible blends/poses) ---
        AdvancedModelBox upperJaw = model.getCube("Upper Jaw");
        AdvancedModelBox lowerJaw = model.getCube("lower jaw");

        // --- tail buffer like Alvarezsaurus ---
        entity.tailBuffer.applyChainSwingBuffer(tail);

        float globalSpeed  = 0.6F;
        float globalDegree = 1.0F;

        // --- breathing / locomotion coupling (walk-parameter driven) ---
//        model.bob(bodyMiddle, globalSpeed * 0.25F, globalDegree * 1.5F, false, f, f1);

        model.chainWave(tail,      globalSpeed * 0.25F, globalDegree * 0.10F, 1, f, f1);
        model.chainSwing(tail,     globalSpeed * 0.25F, globalDegree * 0.20F, 2, f, f1);
        model.chainWave(bodyParts, globalSpeed * 0.25F, globalDegree * 0.025F, 3, f, f1);

        // --- idle breathing (tick-based, always on) ---
        model.chainWave(tail,      0.10F, 0.05F, 1, ticks, 0.25F);
        model.chainWave(bodyParts, 0.10F, -0.05F, 4, ticks, 0.25F);

        // --- head/neck target tracking ---
        model.faceTarget(rotationYaw, rotationPitch, 1.0F,
                neckBase, neck9, head);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
