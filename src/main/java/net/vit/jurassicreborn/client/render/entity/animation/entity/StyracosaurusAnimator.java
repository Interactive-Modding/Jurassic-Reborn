package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.StyracosaurusEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StyracosaurusAnimator extends EntityAnimator<StyracosaurusEntity> {

    private static AdvancedModelBox[] arr(AdvancedModelBox... parts) {
        java.util.ArrayList<AdvancedModelBox> out = new java.util.ArrayList<>();
        for (AdvancedModelBox p : parts) if (p != null) out.add(p);
        return out.toArray(new AdvancedModelBox[0]);
    }

    @Override
    protected void performAnimations(AnimatableModel model, StyracosaurusEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        // Core torso/neck/head
        AdvancedModelBox body1 = model.getCube("body 1");
        AdvancedModelBox body2 = model.getCube("body 2");
        AdvancedModelBox body3 = model.getCube("body 3");                 // :contentReference[oaicite:4]{index=4}
        AdvancedModelBox neck  = model.getCube("neck");
        AdvancedModelBox head  = model.getCube("head");                  // :contentReference[oaicite:5]{index=5}

        // Tail chain (tip → root)
        AdvancedModelBox tail1 = model.getCube("tail 1");
        AdvancedModelBox tail2 = model.getCube("tail 2");
        AdvancedModelBox tail3 = model.getCube("tail 3");
        AdvancedModelBox tail4 = model.getCube("tail 4");
        AdvancedModelBox tail5 = model.getCube("tail 5");
        AdvancedModelBox tail6 = model.getCube("tail 6");
        AdvancedModelBox tail7 = model.getCube("tail 7");                 // :contentReference[oaicite:6]{index=6}

        AdvancedModelBox frillConn   = model.getCube("frill connection");
        AdvancedModelBox frillTopR   = model.getCube("frill top right");
        AdvancedModelBox frillBotR   = model.getCube("frill bottom right");
        AdvancedModelBox frillBotSideR = model.getCube("frill bottom side right");
        AdvancedModelBox frillTopSideR = model.getCube("frill top side right");  // :contentReference[oaicite:7]{index=7}

        // Arrays
        AdvancedModelBox[] torso    = arr(body1, body2, body3);
        AdvancedModelBox[] neckHead = arr(head, neck); // head last will get strongest faceTarget influence
        AdvancedModelBox[] tail     = arr(tail7, tail6, tail5, tail4, tail3, tail2, tail1);
        AdvancedModelBox[] frillSet = arr(frillTopR, frillBotR, frillBotSideR, frillTopSideR);

        // Tuning
        float idleSpeed  = 0.10F;
        float idleDegree = 0.08F;

        // Breathing: torso bob + gentle torso wave
        model.bob(body1, idleSpeed, 0.55F, false, ticks, 1.0F);
        if (torso.length > 1)
            model.chainWave(torso, idleSpeed * 0.65F, idleDegree * 0.45F, 2, ticks, 1.0F);

        // Neck & head subtle motion
        if (neckHead.length > 0)
            model.chainWave(neckHead, idleSpeed * 0.6F, idleDegree * 0.55F, -2, ticks, 1.0F);

            model.chainSwing(new AdvancedModelBox[]{ head }, idleSpeed * 0.9F, 0.10F, 0, ticks, 1.0F);
            model.bob(head, idleSpeed, 0.035F, false, ticks, 1.0F);


        // Tail sway (idle) + locomotion overlay when moving
        if (tail.length > 0) {
            model.chainSwing(tail, idleSpeed, 0.16F, -2, ticks, 1.0F);
            if (limbSwingAmount > 0.12F) {
                model.chainSwing(tail, 0.55F, 0.12F, -2, limbSwing, limbSwingAmount);
            }
            entity.tailBuffer.applyChainSwingBuffer(tail);
        }

        // Frill flutter (very subtle)
        if (frillSet.length > 0) {
            model.chainWave(frillSet, idleSpeed * 1.2F, 0.04F, 2, ticks, 1.0F);
            model.chainSwing(frillSet, idleSpeed * 1.1F, 0.05F, 2, ticks, 1.0F);
        }
            model.chainWave(new AdvancedModelBox[]{ frillConn }, idleSpeed, 0.02F, 0, ticks, 1.0F);

        // Face target (idle tracking)
        if (neck != null || head != null) {
            model.faceTarget(rotationYaw, rotationPitch, 0.85F, arr(neck, head));
        }
    }
}
