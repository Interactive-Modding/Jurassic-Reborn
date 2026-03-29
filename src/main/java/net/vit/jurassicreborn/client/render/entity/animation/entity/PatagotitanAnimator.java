
package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.DreadnoughtusEntity;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.PatagotitanEntity;

@OnlyIn(Dist.CLIENT)
public class PatagotitanAnimator extends EntityAnimator<PatagotitanEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, PatagotitanEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        // Core parts
        final AdvancedModelBox body = model.getCube("Body");
        final AdvancedModelBox head = model.getCube("Head");
        final AdvancedModelBox jaw = model.getCube("Jaw");

        final AdvancedModelBox neck1 = model.getCube("Neck1");
        final AdvancedModelBox neck2 = model.getCube("Neck2");
        final AdvancedModelBox neck3 = model.getCube("Neck3");
        final AdvancedModelBox neck4 = model.getCube("Neck4");
        final AdvancedModelBox neck5 = model.getCube("Neck5");
        final AdvancedModelBox neck6 = model.getCube("Neck6");
        final AdvancedModelBox neck7 = model.getCube("Neck7");
        final AdvancedModelBox neck8 = model.getCube("Neck8");

        final AdvancedModelBox[] neck = new AdvancedModelBox[]{neck1, neck2, neck3, neck4, neck5, neck6, neck7, neck8, head};

        final AdvancedModelBox tail1 = model.getCube("Tail1");
        final AdvancedModelBox tail2 = model.getCube("Tail2");
        final AdvancedModelBox tail3 = model.getCube("bone");
        final AdvancedModelBox tail4 = model.getCube("bone3");
        final AdvancedModelBox tail5 = model.getCube("bone4");
        final AdvancedModelBox tail6 = model.getCube("bone5");
        final AdvancedModelBox tail7 = model.getCube("bone6");
        final AdvancedModelBox tail8 = model.getCube("bone7");
        final AdvancedModelBox tail9 = model.getCube("bone8");
        final AdvancedModelBox[] tail = new AdvancedModelBox[]{tail1,tail2,tail3,tail4,tail5,tail6,tail7,tail8,tail9};


        float idleSpeed  = 0.09F;
        float idleDegree = 0.08F;

        model.bob(body, idleSpeed, 0.6F, false, ticks, 1.0F);
        model.chainWave(neck, idleSpeed * 0.6F, idleDegree * 0.4F, -2, ticks, 1.0F);
        model.chainSwing(tail, idleSpeed, 0.16F, -2, ticks, 1.0F);
        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
