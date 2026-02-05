package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.AllosaurusEntity;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AllosaurusAnimator extends EntityAnimator<AllosaurusEntity> {

    private static float clamp01(float v){ return v < 0 ? 0 : (v > 1 ? 1 : v); }

    @Override
    protected void performAnimations(AnimatableModel model, AllosaurusEntity entity,
                                     float f, float f1, float ticks, float yaw, float pitch, float scale) {
        AdvancedModelBox bodyhips = model.getCube("bodyhips");
        AdvancedModelBox body     = model.getCube("body");
        AdvancedModelBox belly    = model.getCube("belly");
        AdvancedModelBox chest    = model.getCube("chest");
        AdvancedModelBox shoulder = model.getCube("shoulder");

        AdvancedModelBox tail    = model.getCube("tail");
        AdvancedModelBox tai2    = model.getCube("tai2");
        AdvancedModelBox tai3    = model.getCube("tai3");
        AdvancedModelBox tai4    = model.getCube("tai4");
        AdvancedModelBox tail5   = model.getCube("tail5");
        AdvancedModelBox tail6   = model.getCube("tail6");
        AdvancedModelBox tail7   = model.getCube("tail7");
        AdvancedModelBox tail8   = model.getCube("tail8");
        AdvancedModelBox tail9   = model.getCube("tail9");
        AdvancedModelBox tail10  = model.getCube("tail10");

        AdvancedModelBox neck  = model.getCube("neck");
        AdvancedModelBox neck2 = model.getCube("neck2");
        AdvancedModelBox neck3 = model.getCube("neck3");
        AdvancedModelBox neck4 = model.getCube("neck4");
        AdvancedModelBox neck5 = model.getCube("neck5");
        AdvancedModelBox neck6 = model.getCube("neck6");
        AdvancedModelBox neck7 = model.getCube("neck7");
        AdvancedModelBox neck8 = model.getCube("neck8");
        AdvancedModelBox neck9 = model.getCube("neck9");
        AdvancedModelBox head  = model.getCube("head");

        AdvancedModelBox leftThigh  = model.getCube("leftleg");
        AdvancedModelBox rightThigh = model.getCube("rightleg");
        AdvancedModelBox leftCalf   = model.getCube("leftcalf");
        AdvancedModelBox rightCalf  = model.getCube("rightcalf");

        // chains base->tip
        AdvancedModelBox[] tailChain = new AdvancedModelBox[]{ tail, tai2, tai3, tai4, tail5, tail6, tail7, tail8, tail9, tail10 };
        AdvancedModelBox[] bodyChain = new AdvancedModelBox[]{ bodyhips, body, belly, chest, shoulder, neck, head };
        AdvancedModelBox[] neckChain = new AdvancedModelBox[]{ neck, neck2, neck3, neck4, neck5, neck6, neck7, neck8, neck9, head };

        float delta = Minecraft.getInstance().getDeltaFrameTime();
        // global motion
        float speed  = 0.40F;
        float degree = 0.80F;

        // --- action/keyframe weight ---

        float actionW = clamp01(entity.getAttackAnim(ticks)); // 0..1 while attacking/playing keyframe
        // When actions play, suppress idle/walk (leave 20% so it’s never dead still)
        float idleW   = 0.20F + 0.80F * (1.0F - actionW);

        // --- idle breathing (weighted) ---
        model.bob(bodyhips, speed * 0.25F, degree * 2.00F * idleW, false, f, f1);
        model.bob(belly,    speed * 0.15F, degree * 0.40F * idleW, false, f, f1);
        model.bob(chest,    speed * 0.15F, degree * 0.40F * idleW, false, f, f1);

        // --- tail (weighted & gentle) ---
        // tiny idle curl (ticks)
        model.chainWave (tailChain, 0.08F, 0.04F * idleW, 1, ticks, 0.25F);
        // gait sway
        model.chainSwing(tailChain, speed * 0.25F, degree * 0.18F * idleW, 2, f, f1);

        // extra counterbalance only when moving, still scaled by idleW
        if (f1 > 0.01F) {
            model.chainSwing(tailChain, speed * 0.50F, degree * 0.06F * idleW, 1, f, f1);
            model.bob(bodyhips,         speed * 0.80F, degree * 0.10F * idleW, false, f, f1);
        }

        // --- body/neck subtle motion (weighted) ---
        model.chainWave(bodyChain, speed * 0.20F, degree * 0.025F * idleW, 2, f, f1);
        model.chainWave(neckChain, 0.12F,         0.05F    * idleW,       -3, ticks, 0.30F);

        // tail buffer AFTER all rotations, once
        entity.tailBuffer.applyChainSwingBuffer(tailChain);
    }
}
