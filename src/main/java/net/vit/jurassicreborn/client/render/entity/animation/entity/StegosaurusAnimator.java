package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.StegosaurusEntity;

@OnlyIn(Dist.CLIENT)
public class StegosaurusAnimator extends EntityAnimator<StegosaurusEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, StegosaurusEntity entity,
                                     float f, float f1, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        AdvancedModelBox body  = model.getCube("Body");
        AdvancedModelBox body2 = model.getCube("Body2");

        AdvancedModelBox neck1 = model.getCube("Neck1");
        AdvancedModelBox neck2 = model.getCube("Neck2");
        AdvancedModelBox neck3 = model.getCube("Neck3");
        AdvancedModelBox head  = model.getCube("Head");
        AdvancedModelBox jaws  = model.getCube("Jaws");

        AdvancedModelBox tail1 = model.getCube("Tail1");
        AdvancedModelBox tail2 = model.getCube("Tail2");
        AdvancedModelBox tail3 = model.getCube("Tail3");
        AdvancedModelBox tail4 = model.getCube("Tail4");
        AdvancedModelBox tail5 = model.getCube("Tail5");
        AdvancedModelBox tail6 = model.getCube("Tail6");
        AdvancedModelBox tail7 = model.getCube("Tail7");
        AdvancedModelBox tail8 = model.getCube("Tail8");

        AdvancedModelBox lFront = model.getCube("LeftFrontLeg");
        AdvancedModelBox rFront = model.getCube("RightFrontLeg");
        AdvancedModelBox lBack  = model.getCube("LeftBackLeg");
        AdvancedModelBox rBack  = model.getCube("RightBackLeg");

        AdvancedModelBox[] tail = new AdvancedModelBox[]{tail8, tail7, tail6, tail5, tail4, tail3, tail2, tail1};

        // === SLOWER GAIT KNOB ===
        final float GAIT_RATE = 0.72F;     // <— lower = slower (e.g., 0.65F). 1.0F = original speed
        final float fG = f * GAIT_RATE;    // use this in ALL gait-driven calls

        // Legacy driver constants
        float scaleFactor = 0.5F;
        float height      = 0.8F;
        float frontOffset = -2.0F;

        // COM bob & trunk (mapped hips→body2)
        model.bob(body2,   2 * scaleFactor, height, false, fG, f1);
        model.bob(lBack,   2 * scaleFactor, height, false, fG, f1);
        model.bob(rBack,   2 * scaleFactor, height, false, fG, f1);

        // hips pitch nod
        model.walk(body2,  2 * scaleFactor, 0.1F * height, true,  -1.5F, 0F, fG, f1);

        // neck/head pitch offsets across gait
        model.walk(neck1,  2 * scaleFactor, 0.07F * height, false, -0.5F, 0F, fG, f1);
        model.walk(neck3,  2 * scaleFactor, 0.07F * height, false,  0.5F, 0F, fG, f1);
        model.walk(head,   2 * scaleFactor, 0.07F * height, true,   1.5F, 0F, fG, f1);

        // Idle micro-motion (unchanged; stays ticks-driven)
        model.walk(neck1, 0.10F, 0.04F, false, -1F, 0F, ticks, 1F);
        model.walk(head,  0.10F, 0.07F, true,   0F, 0F, ticks, 1F);
        model.walk(neck3, 0.10F, 0.03F, false,  0F, 0F, ticks, 1F);
        model.walk(body2, 0.10F, 0.025F, false, 0F, 0F, ticks, 1F);


        // Tail (ticks-only, like legacy)
        model.chainSwing(tail, 0.10F,  0.20F, 3, ticks, 1.0F);
        model.chainWave (tail, 0.10F, -0.05F, 1, ticks, 1.0F);

        float jawOpen = 0.03F + f1 * 0.05F;
        if (entity.isAggressive() || entity.getTarget() != null) {
            jawOpen += 0.12F;
        }
        if (jaws != null) {
            jaws.rotateAngleX += jawOpen;
        }

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
