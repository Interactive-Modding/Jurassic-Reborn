package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.TitanisEntity;

public class TitanisAnimator extends EntityAnimator<TitanisEntity> {

    private static AdvancedModelBox[] arr(AdvancedModelBox... parts) {
        java.util.ArrayList<AdvancedModelBox> out = new java.util.ArrayList<>();
        for (AdvancedModelBox p : parts) if (p != null) out.add(p);
        return out.toArray(new AdvancedModelBox[0]);
    }

    @Override
    protected void performAnimations(AnimatableModel model, TitanisEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        // ----- torso / core -----
        AdvancedModelBox abdomen  = model.getCube("abdomen");
        AdvancedModelBox hips     = model.getCube("hips");
        AdvancedModelBox shoulder = model.getCube("shoulder");
        AdvancedModelBox back     = model.getCube("back"); // thin dorsal plate

        AdvancedModelBox[] torso = arr(abdomen, hips, shoulder, back);

        // ----- neck → head chain (head last gets strongest faceTarget) -----



        // ----- tail base + feather fan (tip → root for chain* helpers) -----
        AdvancedModelBox tail_feather_top      = model.getCube("tail_feather_top");
        AdvancedModelBox tail_feather_top_1_l  = model.getCube("tail_feather_top_1_l");
        AdvancedModelBox tail_feather_top_1_r  = model.getCube("tail_feather_top_1_r");
        AdvancedModelBox tail_feather_top_2_l  = model.getCube("tail_feather_top_2_l");
        AdvancedModelBox tail_feather_top_2_r  = model.getCube("tail_feather_top_2_r");

        AdvancedModelBox tail_feather_bottom_1_l = model.getCube("tail_feather_bottom_1_l");
        AdvancedModelBox tail_feather_bottom_1_r = model.getCube("tail_feather_bottom_1_r");
        AdvancedModelBox tail_feather_bottom_2_l = model.getCube("tail_feather_bottom_2_l");
        AdvancedModelBox tail_feather_bottom_2_r = model.getCube("tail_feather_bottom_2_r");
        AdvancedModelBox tail_feather_bottom_3_l = model.getCube("tail_feather_bottom_3_l");
        AdvancedModelBox tail_feather_bottom_3_r = model.getCube("tail_feather_bottom_3_r");
        AdvancedModelBox tail_feather_bottom_4_l = model.getCube("tail_feather_bottom_4_l");
        AdvancedModelBox tail_feather_bottom_4_r = model.getCube("tail_feather_bottom_4_r");

        AdvancedModelBox tail_base      = model.getCube("tail_base");
        AdvancedModelBox tail_base_base = model.getCube("tail_base_base");

        // short bird tail: put feathers first (tip), then base segments
        AdvancedModelBox[] tail = arr(
                // top fan tips
                tail_feather_top_2_l, tail_feather_top_2_r,
                tail_feather_top_1_l, tail_feather_top_1_r,
                tail_feather_top,
                // lower fan rows
                tail_feather_bottom_4_l, tail_feather_bottom_4_r,
                tail_feather_bottom_3_l, tail_feather_bottom_3_r,
                tail_feather_bottom_2_l, tail_feather_bottom_2_r,
                tail_feather_bottom_1_l, tail_feather_bottom_1_r,
                // fleshy base (root-most last)
                tail_base, tail_base_base
        );

        // ----- wings (optional flutter) -----
        AdvancedModelBox wing_upper_arm_r   = model.getCube("wing_upper_arm_r");
        AdvancedModelBox wing_forearm_r     = model.getCube("wing_forearm_r");
        AdvancedModelBox wing_hand_r        = model.getCube("wing_hand_r");

        AdvancedModelBox wing_upper_arm_r_1 = model.getCube("wing_upper_arm_r_1"); // left
        AdvancedModelBox wing_forearm_r_1   = model.getCube("wing_forearm_r_1");
        AdvancedModelBox wing_hand_r_1      = model.getCube("wing_hand_r_1");

        AdvancedModelBox[] wingR = arr(wing_hand_r, wing_forearm_r, wing_upper_arm_r);
        AdvancedModelBox[] wingL = arr(wing_hand_r_1, wing_forearm_r_1, wing_upper_arm_r_1);

        // ===== idle tuning =====
        float idleSpeed  = 0.12F; // frequency
        float idleDegree = 0.08F; // amplitude baseline

        // ----- breathing (torso) -----
        if (abdomen != null) model.bob(abdomen, idleSpeed, 0.35F, false, ticks, 1.0F);
        if (hips    != null) model.bob(hips,    idleSpeed, 0.25F, false, ticks, 1.0F);
        if (torso.length > 1) {
            model.chainWave(torso, idleSpeed * 0.75F, idleDegree * 0.40F, 2, ticks, 1.0F);
        }

        // ----- neck undulation + micro head motion -----

        // ----- tail sway (fan + base) -----
        if (tail.length > 0) {
            // lateral sway
            model.chainSwing(tail, idleSpeed, 0.18F, -2, ticks, 1.0F);
            // subtle up/down ripple on the very tips
            AdvancedModelBox[] fanTips = arr(
                    tail_feather_top, tail_feather_top_1_l, tail_feather_top_1_r,
                    tail_feather_top_2_l, tail_feather_top_2_r
            );
            if (fanTips.length > 0) {
                model.chainWave(fanTips, idleSpeed * 1.15F, 0.06F, 2, ticks, 1.0F);
            }
            // movement overlay
            if (limbSwingAmount > 0.12F) {
                model.chainSwing(tail, 0.65F, 0.13F, -2, limbSwing, limbSwingAmount);
            }
            entity.tailBuffer.applyChainSwingBuffer(tail);
        }

        // ----- wing/primary feather flutter (very subtle) -----
        if (wingR.length > 0) {
            model.chainSwing(wingR, idleSpeed * 1.2F, 0.08F, 2, ticks, 1.0F);
            model.chainWave (wingR, idleSpeed * 0.9F, 0.05F, 2, ticks, 1.0F);
        }
        if (wingL.length > 0) {
            model.chainSwing(wingL, idleSpeed * 1.2F, 0.08F, 2, ticks, 1.0F);
            model.chainWave (wingL, idleSpeed * 0.9F, 0.05F, 2, ticks, 1.0F);
        }

    }
}
