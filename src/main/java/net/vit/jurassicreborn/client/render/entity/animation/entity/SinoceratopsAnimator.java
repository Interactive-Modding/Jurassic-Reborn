package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.SinoceratopsEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SinoceratopsAnimator extends EntityAnimator<SinoceratopsEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, SinoceratopsEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        // --- core cubes (common Tabula naming) ---
        AdvancedModelBox body1 = model.getCube("body 1");
        AdvancedModelBox body2 = model.getCube("body 2");
        AdvancedModelBox body3 = model.getCube("body 3");
        AdvancedModelBox neck  = model.getCube("neck");
        AdvancedModelBox head  = model.getCube("head");

        AdvancedModelBox tail1 = model.getCube("tail 1");
        AdvancedModelBox tail2 = model.getCube("tail 2");
        AdvancedModelBox tail3 = model.getCube("tail 3");
        AdvancedModelBox tail4 = model.getCube("tail 4");
        AdvancedModelBox tail5 = model.getCube("tail 5");
        AdvancedModelBox tail6 = model.getCube("tail 6");
        AdvancedModelBox tail7 = model.getCube("tail 7");

        // Frill pieces (names may vary per export; these are common)
        AdvancedModelBox frillConn = model.getCube("frill connection");
        AdvancedModelBox frillMidT = model.getCube("frill middle top");
        AdvancedModelBox frillRT   = model.getCube("frill right top");
        AdvancedModelBox frillLT   = model.getCube("frill left top");

        AdvancedModelBox[] torso     = arr(body1, body2, body3);
        AdvancedModelBox[] neckChain = arr(head, neck); // head first for chain helpers
        AdvancedModelBox[] tail      = arr(tail7, tail6, tail5, tail4, tail3, tail2, tail1); // tip→root
        AdvancedModelBox[] frillSet  = arr(frillMidT, frillRT, frillLT);

        // --- tuning ---
        float idleSpeed  = 0.10F; // frequency
        float idleDegree = 0.08F; // amplitude

        // --- breathing: torso bob + gentle torso wave ---
        if (body1 != null) model.bob(body1, idleSpeed, 0.55F, false, ticks, 1.0F);
        if (torso.length > 1) model.chainWave(torso, idleSpeed * 0.65F, idleDegree * 0.45F, 2, ticks, 1.0F);

        // --- neck & head subtle motion ---
        if (neckChain.length > 0) model.chainWave(neckChain, idleSpeed * 0.6F, idleDegree * 0.55F, -2, ticks, 1.0F);
        if (head != null) {
            model.chainSwing(new AdvancedModelBox[]{ head }, idleSpeed * 0.9F, 0.10F, 0, ticks, 1.0F);
            model.bob(head, idleSpeed, 0.035F, false, ticks, 1.0F);
        }

        // --- tail sway (idle) + locomotion overlay when moving ---
        if (tail.length > 0) {
            model.chainSwing(tail, idleSpeed, 0.16F, -2, ticks, 1.0F);
            if (limbSwingAmount > 0.12F) {
                model.chainSwing(tail, 0.55F, 0.12F, -2, limbSwing, limbSwingAmount);
            }
            entity.tailBuffer.applyChainSwingBuffer(tail);
        }

        // --- frill flutter (very subtle, mostly cosmetic) ---
        if (frillSet.length > 0) {
            model.chainWave(frillSet, idleSpeed * 1.2F, 0.04F, 2, ticks, 1.0F);
            model.chainSwing(frillSet, idleSpeed * 1.1F, 0.05F, 2, ticks, 1.0F);
        }
        if (frillConn != null) {
            model.chainWave(new AdvancedModelBox[]{ frillConn }, idleSpeed, 0.02F, 0, ticks, 1.0F);
        }

        // --- face target (head last for strongest influence) ---
        if (neck != null || head != null) {
            model.faceTarget(rotationYaw, rotationPitch, 0.85F,
                    arr(neck, head)
            );
        }
    }

    private static AdvancedModelBox[] arr(AdvancedModelBox... parts) {
        java.util.ArrayList<AdvancedModelBox> out = new java.util.ArrayList<>();
        for (AdvancedModelBox p : parts) if (p != null) out.add(p);
        return out.toArray(new AdvancedModelBox[0]);
    }
}
