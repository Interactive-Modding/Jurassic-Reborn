package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.AnkylodocusEntity;
import net.minecraft.client.Minecraft;import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AnkylodocusAnimator extends EntityAnimator<AnkylodocusEntity> {

    private static float clamp01(float v){ return v < 0 ? 0 : (v > 1 ? 1 : v); }

    @Override
    protected void performAnimations(AnimatableModel model, AnkylodocusEntity entity,
                                     float f, float f1, float ticks, float yaw, float pitch, float scale) {

        // Core
        AdvancedModelBox body = model.getCube("body");
        AdvancedModelBox hips = model.getCube("hips");

        // Neck (base -> tip)
        AdvancedModelBox neck1  = model.getCube("neck1");
        AdvancedModelBox neck2  = model.getCube("neck2");
        AdvancedModelBox neck3  = model.getCube("neck3");
        AdvancedModelBox neck4  = model.getCube("neck4");
        AdvancedModelBox neck5  = model.getCube("neck5");
        AdvancedModelBox neck6  = model.getCube("neck6");
        AdvancedModelBox neck7  = model.getCube("neck7");
        AdvancedModelBox neck8  = model.getCube("neck8");
        AdvancedModelBox neck9  = model.getCube("neck9");
        AdvancedModelBox neck10 = model.getCube("neck10");


        AdvancedModelBox bottomjaw = model.getCube("bottom jaw");
        AdvancedModelBox jawflap   = model.getCube("jaw flap");

        // Tail (base -> tip)
        AdvancedModelBox tail1  = model.getCube("tail1");
        AdvancedModelBox tail2  = model.getCube("tail2");
        AdvancedModelBox tail3  = model.getCube("tail3");
        AdvancedModelBox tail4  = model.getCube("tail4");
        AdvancedModelBox tail5  = model.getCube("tail5");
        AdvancedModelBox tail6  = model.getCube("tail6");
        AdvancedModelBox tail7  = model.getCube("tail7");
        AdvancedModelBox tail8  = model.getCube("tail8");
        AdvancedModelBox tail9  = model.getCube("tail9");
        AdvancedModelBox tail10 = model.getCube("tail10");
        AdvancedModelBox tailclub   = model.getCube("tail club0");
        AdvancedModelBox tailclub_1 = model.getCube("tail club1");
        AdvancedModelBox tailclub_2 = model.getCube("tail club2");

        // Chains (match Allosaurus style: base -> tip)
        AdvancedModelBox[] tailChain = new AdvancedModelBox[] {
                tail1, tail2, tail3, tail4, tail5, tail6, tail7, tail8, tail9, tail10, tailclub, tailclub_1, tailclub_2
        };
        AdvancedModelBox[] bodyChain = new AdvancedModelBox[] {
                hips, body, neck1 // keep it minimal; no distinct chest/shoulder cubes in this model
        };
        AdvancedModelBox[] neckChain = new AdvancedModelBox[] {
                neck1, neck2, neck3, neck4, neck5, neck6, neck7, neck8, neck9, neck10
        };

        Minecraft mc = Minecraft.getInstance();

        float delta = mc.isPaused()
                ? 0.0f
                : mc.getTimer().getGameTimeDeltaPartialTick(false);
        float speed  = 0.40F;
        float degree = 0.80F;

        // action weight like Allosaurus: dampen idle/walk while attacking/keyframing
        float actionW = clamp01(entity.getAttackAnim(ticks));
        float idleW   = 0.20F + 0.80F * (1.0F - actionW);

        // --- idle breathing / body bob ---
        model.bob(hips, speed * 0.25F, degree * 1.60F * idleW, false, f, f1);
        model.bob(body, speed * 0.20F, degree * 0.60F * idleW, false, f, f1);

        // --- tail: small idle curl + gait sway (weighted like Allosaurus) ---
        model.chainWave (tailChain, 0.08F, 0.04F * idleW, 1, ticks, 0.25F);              // idle
        model.chainSwing(tailChain, speed * 0.25F, degree * 0.16F * idleW, 2, f, f1);     // gait

        // when actually moving, add a bit more counterbalance & bob
        if (f1 > 0.01F) {
            model.chainSwing(tailChain, speed * 0.50F, degree * 0.05F * idleW, 1, f, f1);
            model.bob(hips,            speed * 0.80F, degree * 0.10F * idleW, false, f, f1);
        }

        // --- body/neck subtle motion (same flavor as Allosaurus) ---
        model.chainWave(bodyChain, speed * 0.20F, degree * 0.025F * idleW, 2, f, f1);
        model.chainWave(neckChain, 0.12F,         0.05F    * idleW,       -3, ticks, 0.30F);

        // jaw micro-motion so the head doesn’t feel dead
        if (bottomjaw != null) bottomjaw.rotateAngleX += (float)Math.sin(ticks * 0.05F) * 0.03F;
        if (jawflap   != null) jawflap.rotateAngleX   += (float)Math.sin(ticks * 0.05F + 0.6F) * 0.02F;


        entity.tailBuffer.applyChainSwingBuffer(tailChain);
    }
}
