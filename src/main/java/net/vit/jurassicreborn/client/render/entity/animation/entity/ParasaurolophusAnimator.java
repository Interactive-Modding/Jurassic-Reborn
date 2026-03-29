package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.ParasaurolophusEntity;import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParasaurolophusAnimator extends EntityAnimator<ParasaurolophusEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, ParasaurolophusEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        // --- core parts (names from parasaurolophus_adult_idle) ---
        AdvancedModelBox body1 = model.getCube("Body1");
        AdvancedModelBox body2 = model.getCube("Body2");
        AdvancedModelBox body3 = model.getCube("Body3");                              // body chain
        AdvancedModelBox head  = model.getCube("Head");                               // head
        AdvancedModelBox neck1 = model.getCube("Neck1");
        AdvancedModelBox neck2 = model.getCube("Neck2");
        AdvancedModelBox neck3 = model.getCube("Neck3");
        AdvancedModelBox neck4 = model.getCube("Neck4");
        AdvancedModelBox neck5 = model.getCube("Neck5");
        AdvancedModelBox neck6 = model.getCube("Neck6");
        AdvancedModelBox neck7 = model.getCube("Neck7");
        AdvancedModelBox neck8 = model.getCube("Neck8");
        AdvancedModelBox neck9 = model.getCube("Neck9");
        AdvancedModelBox neck10 = model.getCube("Neck10");
        AdvancedModelBox neck11 = model.getCube("Neck11");
        AdvancedModelBox neck12 = model.getCube("Neck12");
        AdvancedModelBox neck13 = model.getCube("Neck13");                             // long neck chain
        AdvancedModelBox tail1 = model.getCube("Tail1");
        AdvancedModelBox tail2 = model.getCube("Tail2");
        AdvancedModelBox tail3 = model.getCube("Tail3");
        AdvancedModelBox tail4 = model.getCube("Tail4");
        AdvancedModelBox tail5 = model.getCube("Tail5");
        AdvancedModelBox tail6 = model.getCube("Tail6");
        AdvancedModelBox tail7 = model.getCube("Tail7");                               // tail chain


        AdvancedModelBox[] torso = new AdvancedModelBox[]{ body1, body2, body3 };
        AdvancedModelBox[] neck  = new AdvancedModelBox[]{ head, neck13, neck12, neck11, neck10, neck9, neck8, neck7, neck6, neck5, neck4, neck3, neck2, neck1 };
        AdvancedModelBox[] tail  = new AdvancedModelBox[]{ tail7, tail6, tail5, tail4, tail3, tail2, tail1 };

        // --- tuning ---
        float idleSpeed  = 0.10F; // frequency
        float idleDegree = 0.08F; // amplitude

        // --- breathing (torso bob + gentle wave) ---
        if (body1 != null) model.bob(body1, idleSpeed, 0.55F, false, ticks, 1.0F);
        model.chainWave(torso, idleSpeed * 0.65F, idleDegree * 0.45F, 2, ticks, 1.0F);

        // --- neck & head subtle motion ---
        model.chainWave(neck, idleSpeed * 0.6F, idleDegree * 0.6F, -2, ticks, 1.0F);
        if (head != null) {
            model.chainSwing(new AdvancedModelBox[]{ head }, idleSpeed * 0.9F, 0.10F, 0, ticks, 1.0F);
            model.bob(head, idleSpeed, 0.035F, false, ticks, 1.0F);
        }

        // --- tail sway (idle) + locomotion layer when moving ---
        model.chainSwing(tail, idleSpeed, 0.18F, -2, ticks, 1.0F);
        if (limbSwingAmount > 0.12F) {
            model.chainSwing(tail, 0.60F, 0.13F, -2, limbSwing, limbSwingAmount);
        }
        entity.tailBuffer.applyChainSwingBuffer(tail);

        // --- look-at (face target) along the long neck; head last for strongest influence ---
        model.faceTarget(rotationYaw, rotationPitch, 0.9F,
                neck1, neck2, neck3, neck4, neck5, neck6, neck7, neck8, neck9, neck10, neck11, neck12, neck13, head
        );
    }
}
