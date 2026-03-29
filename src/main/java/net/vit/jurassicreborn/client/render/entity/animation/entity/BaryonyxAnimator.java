package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.BaryonyxEntity;import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BaryonyxAnimator extends EntityAnimator<BaryonyxEntity> {

    private static float clamp(float v, float lo, float hi){ return v < lo ? lo : (v > hi ? hi : v); }
    private static float rad(float d){ return (float)Math.toRadians(d); }

    @Override
    protected void performAnimations(AnimatableModel model, BaryonyxEntity entity,
                                     float f, float f1, float ticks, float yaw, float pitch, float scale) {

        // Core / chest
        AdvancedModelBox Body1 = model.getCube("Body 1");
        AdvancedModelBox Body2 = model.getCube("Body 2");
        AdvancedModelBox Body3 = model.getCube("Body 3");
        AdvancedModelBox Throat1 = model.getCube("Throat 1");

        // Neck (base -> tip) and head
        AdvancedModelBox Neck1 = model.getCube("Neck1");
        AdvancedModelBox Neck2 = model.getCube("Neck2");
        AdvancedModelBox Neck3 = model.getCube("Neck3");
        AdvancedModelBox Neck4 = model.getCube("Neck4");
        AdvancedModelBox Neck5 = model.getCube("Neck5");
        AdvancedModelBox Neck6 = model.getCube("Neck6");
        AdvancedModelBox Head = model.getCube("Head");

        // Jaw (optional gentle idle)
        AdvancedModelBox LowerJawmain = model.getCube("Lower Jaw main");

        // chains
        AdvancedModelBox[] bodyChain = new AdvancedModelBox[]{
                Body1, Body2, Body3
        };
        AdvancedModelBox[] neckChain = new AdvancedModelBox[]{
                Neck1, Neck2, Neck3, Neck4, Neck5, Neck6, Head
        };

        // ------------ idle breathing ------------
        // soft wave on torso and neck; keep it subtle
        model.chainWave(bodyChain, 0.10F, 0.05F, 2, ticks, 0.35F);
        model.chainWave(neckChain, 0.12F, 0.04F, -3, ticks, 0.30F);
        if (!entity.isCarcass()) {

            if (Throat1 != null) Throat1.rotateAngleX += (float) Math.sin(ticks * 0.12F) * 0.04F;
            if (LowerJawmain != null) LowerJawmain.rotateAngleX += (float) Math.sin(ticks * 0.10F + 0.6F) * 0.03F;

            // ------------ head / neck look ------------
            float lookYaw = clamp(rad(yaw), rad(-45F), rad(45F));
            float lookPitch = clamp(rad(pitch), rad(-25F), rad(25F));

            // distribute look along the chain (base -> tip)
            float[] w = new float[]{0.08F, 0.10F, 0.13F, 0.17F, 0.20F, 0.20F, 0.12F};
            for (int i = 0; i < neckChain.length && i < w.length; i++) {
                AdvancedModelBox p = neckChain[i];
                if (p == null) continue;
                p.rotateAngleY += lookYaw * w[i];
                p.rotateAngleX += lookPitch * w[i];
            }
        }

    }
}
