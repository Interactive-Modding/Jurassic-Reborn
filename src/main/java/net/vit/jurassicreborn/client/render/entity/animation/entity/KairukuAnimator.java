package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.KairukuEntity;

@OnlyIn(Dist.CLIENT)
public class KairukuAnimator extends EntityAnimator<KairukuEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, KairukuEntity entity,
                                     float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {

        // --- parts (match Tabula names) ---
        AdvancedModelBox body      = model.getCube("Body");
        AdvancedModelBox leftFoot  = model.getCube("LeftFoot");
        AdvancedModelBox rightFoot = model.getCube("RightFoot");
        AdvancedModelBox leftWing  = model.getCube("LeftWing");
        AdvancedModelBox rightWing = model.getCube("RightWing");
        AdvancedModelBox neck      = model.getCube("Neck");
        AdvancedModelBox head      = model.getCube("Head");
        AdvancedModelBox beak      = model.getCube("Beak");

        AdvancedModelBox[] bodyChain = new AdvancedModelBox[] { body, neck, head };

        // --- motion tuning ---
        float speed  = 0.9F;   // walk cycle speed
        float deg    = 1.0F;   // overall amplitude

        // Cute vertical bob while walking
            model.bob(body, speed * 0.50F, deg * 0.60F, false, f, f1);


        // Penguin waddle: gentle side sway of the torso
        // (flap = rotation around Z, swing = around Y)
        model.flap(body, speed * 0.50F, deg * 0.20F, true, 0.0F, 0.0F, f, f1);     // side tilt
        model.swing(body, speed * 0.50F, deg * 0.15F, true, 0.0F, 0.0F, f, f1);    // tiny yaw

        model.faceTarget(rotationYaw, rotationPitch, 1.0F, neck, head);
    }
}
