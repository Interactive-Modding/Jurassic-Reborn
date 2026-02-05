package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.TherizinosaurusEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TherizinosaurusAnimator extends EntityAnimator<TherizinosaurusEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, TherizinosaurusEntity entity,
                                     float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {

        AdvancedModelBox body  = model.getCube("Body");
        AdvancedModelBox neck1 = model.getCube("Neck1");
        AdvancedModelBox neck2 = model.getCube("Neck2");
        AdvancedModelBox neck3 = model.getCube("Neck3");
        AdvancedModelBox neck4 = model.getCube("Neck4");
        AdvancedModelBox neck5 = model.getCube("Neck5");
        AdvancedModelBox neck6 = model.getCube("Neck6");
        AdvancedModelBox head  = model.getCube("Head");

        AdvancedModelBox tail1 = model.getCube("Tail1");
        AdvancedModelBox tail2 = model.getCube("Tail2");
        AdvancedModelBox tail3 = model.getCube("Tail3");
        AdvancedModelBox tail4 = model.getCube("Tail4");
        AdvancedModelBox tail5 = model.getCube("Tail5");
        AdvancedModelBox tail6 = model.getCube("Tail6");

        AdvancedModelBox[] tail      = new AdvancedModelBox[]{ tail6, tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelBox[] bodyParts = new AdvancedModelBox[]{ body, neck1, head };
        AdvancedModelBox[] headChain = new AdvancedModelBox[]{ neck1, neck2, neck3, neck4, neck5, neck6 };

        entity.tailBuffer.applyChainSwingBuffer(tail);

        // --- tuning ---
        float globalSpeed  = 0.6F;
        float globalDegree = 1.0F;

        // gentle body bob while moving
        if (body != null) {
            model.bob(body, globalSpeed * 0.25F, globalDegree * 1.5F, false, f, f1);
        }

        // locomotion-driven tail & body motion
        model.chainWave(tail,      globalSpeed * 0.25F, globalDegree * 0.10F, 1, f, f1);
        model.chainSwing(tail,     globalSpeed * 0.25F, globalDegree * 0.40F, 2, f, f1);
        model.chainWave(bodyParts, globalSpeed * 0.25F, globalDegree * 0.025F, 3, f, f1);

        // idle micro-motion
        model.chainWave(tail,      0.10F, 0.05F, 1, ticks, 0.25F);
        model.chainWave(bodyParts, 0.10F, -0.05F, 4, ticks, 0.25F);

        // look where we’re facing (distributes across neck to head)
        model.faceTarget(rotationYaw, rotationPitch, 1.0F, neck1, neck2, neck3, neck4, neck5, neck6, head);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
