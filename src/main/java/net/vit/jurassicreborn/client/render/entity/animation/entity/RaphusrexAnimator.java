package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.RaphusrexEntity;

public class RaphusrexAnimator extends EntityAnimator<RaphusrexEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, RaphusrexEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        // --- torso ---
        AdvancedModelBox body1 = model.getCube("Body 1");
        AdvancedModelBox body2 = model.getCube("Body 2");
        AdvancedModelBox body3 = model.getCube("Body 3");
        AdvancedModelBox[] torso = new AdvancedModelBox[]{ body1, body2, body3 };

        // --- neck/head (head last for strongest influence) ---
        AdvancedModelBox neck1 = model.getCube("Neck1");
        AdvancedModelBox neck2 = model.getCube("Neck2");
        AdvancedModelBox neck3 = model.getCube("Neck3");
        AdvancedModelBox neck4 = model.getCube("Neck4");
        AdvancedModelBox neck5 = model.getCube("Neck5");
        AdvancedModelBox neck6 = model.getCube("Neck6");         // present in file
        AdvancedModelBox head  = model.getCube("Head");

        AdvancedModelBox[] neck = new AdvancedModelBox[]{
                 neck1
        };

        // --- tail chain (tip → root) ---
        AdvancedModelBox tail1  = model.getCube("Tail 1");
        AdvancedModelBox tail2  = model.getCube("Tail 2");
        AdvancedModelBox tail3  = model.getCube("Tail 3");
        AdvancedModelBox tail4  = model.getCube("Tail 4");
        AdvancedModelBox tail5  = model.getCube("Tail 5");
        AdvancedModelBox tail6  = model.getCube("Tail 6");
        AdvancedModelBox tail7  = model.getCube("Tail 7");
        AdvancedModelBox tail8  = model.getCube("Tail 8");
        AdvancedModelBox tail9  = model.getCube("Tail 9");
        AdvancedModelBox tail10 = model.getCube("Tail 10");
        AdvancedModelBox tail11 = model.getCube("Tail 11");

        AdvancedModelBox[] tail = new AdvancedModelBox[]{
                tail11, tail10, tail9, tail8, tail7, tail6, tail5, tail4, tail3, tail2, tail1
        };



        // --- tuning ---
        float idleSpeed  = 0.12F;  // frequency
        float idleDegree = 0.08F;  // amplitude

        // --- breathing (soft bob + gentle torso wave) ---
        if (body1 != null) model.bob(body1, idleSpeed, 0.45F, false, ticks, 1.0F);
        model.chainWave(torso, idleSpeed * 0.7F, idleDegree * 0.45F, 2, ticks, 1.0F);

        // --- neck/head subtle motion ---
        model.chainWave(neck, idleSpeed * 0.85F, idleDegree * 0.6F, -2, ticks, 1.0F);
        if (head != null) {
            model.chainSwing(new AdvancedModelBox[]{ head }, idleSpeed, 0.10F, 0, ticks, 1.0F);
            model.bob(head, idleSpeed, 0.03F, false, ticks, 1.0F);
        }

        // --- tail sway (idle) ---
        model.chainSwing(tail, idleSpeed, 0.10F, -2, ticks, 0.7F);

        // extra flutter on feather tips (very subtle)


        // add locomotion layer when moving
        if (limbSwingAmount > 0.12F) {
            model.chainSwing(tail, 0.05F, 0.14F, -2, limbSwing, limbSwingAmount);
        }

        // smooth the tail after all transforms
        entity.tailBuffer.applyChainSwingBuffer(tail);

        model.faceTarget(rotationYaw, rotationPitch, 0.9F, neck1, neck2, neck3, neck4, neck5, neck6, neck6, head);
    }
}
