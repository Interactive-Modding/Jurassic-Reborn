package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.ApatosaurusEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ApatosaurusAnimator extends EntityAnimator<ApatosaurusEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, ApatosaurusEntity entity,
                                     float limbSwing, float limbSwingAmount,
                                     float ticks, float rotationYaw,
                                     float rotationPitch, float scale) {

        AdvancedModelBox body   = model.getCube("body");
        AdvancedModelBox neck1  = model.getCube("neck1");
        AdvancedModelBox neck2  = model.getCube("neck2");
        AdvancedModelBox neck3  = model.getCube("neck3");
        AdvancedModelBox neck4  = model.getCube("neck4");
        AdvancedModelBox tail1  = model.getCube("tail1");
        AdvancedModelBox tail2  = model.getCube("tail2");
        AdvancedModelBox tail3  = model.getCube("tail3");
        AdvancedModelBox tail4  = model.getCube("tail4");
        AdvancedModelBox tail5  = model.getCube("tail5");
        AdvancedModelBox tail6  = model.getCube("tail6");
        AdvancedModelBox tail7  = model.getCube("tail7");
        AdvancedModelBox tail8  = model.getCube("tail8");
        AdvancedModelBox tail9  = model.getCube("tail9");

        AdvancedModelBox topLegLeft  = model.getCube("toplegleft");
        AdvancedModelBox topLegRight = model.getCube("toplegright");

        // --- Create arrays for chain-based animations. ---
        AdvancedModelBox[] tailArray = {
                tail9, tail8, tail7, tail6, tail5, tail4, tail3, tail2, tail1
        };

        AdvancedModelBox[] neckArray = {
                neck4, neck3, neck2, neck1
        };

        // --- Example: gentle tail swing while walking. ---
        // chainSwing(parts, speed, degree, offset, ticks, distance)
        model.chainSwing(tailArray, 0.4F, 0.05F, 3.0D, limbSwing, limbSwingAmount);

        // --- Example: wave the neck slightly up and down. ---
        // chainWave(parts, speed, degree, offset, ticks, distance)
        model.chainWave(neckArray, 0.1F, 0.05F, 2.0D, ticks, 1.0F);

        // --- Example: animate the legs "walking." ---
        // walk(part, speed, degree, invert, offset, weight, ticks, limbSwingAmount)
        if (topLegLeft != null && topLegRight != null) {
            // Move left leg and right leg in opposite phase
            model.walk(topLegLeft,  0.5F, 0.3F, false,  0.0F, 0.0F, limbSwing, limbSwingAmount);
            model.walk(topLegRight, 0.5F, 0.3F, true,   0.0F, 0.0F, limbSwing, limbSwingAmount);
        }

        // bob(part, speed, degree, ignoreFacing, ticks, distance)
        if (body != null) {
            model.bob(body, 0.5F, 2.0F, false, limbSwing, limbSwingAmount);
        }

        // entity.tailBuffer.applyChainSwingBuffer(tailArray);
    }
}
