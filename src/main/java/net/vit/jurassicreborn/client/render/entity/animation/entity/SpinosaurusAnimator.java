package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.SpinosaurusEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SpinosaurusAnimator extends EntityAnimator<SpinosaurusEntity> {

    // ---- helpers: filter null parts so chain* calls never NPE ----
    private static AdvancedModelBox[] nn(AdvancedModelBox... parts) {
        List<AdvancedModelBox> ok = new ArrayList<>();
        for (AdvancedModelBox p : parts) if (p != null) ok.add(p);
        return ok.toArray(new AdvancedModelBox[0]);
    }

    // small wrappers that no-op on empty arrays
    private static void swing(AnimatableModel m, float s, float d, int off, float t, float w, AdvancedModelBox... parts) {
        AdvancedModelBox[] arr = nn(parts);
        if (arr.length > 0) m.chainSwing(arr, s, d, off, t, w);
    }
    private static void wave(AnimatableModel m, float s, float d, int off, float t, float w, AdvancedModelBox... parts) {
        AdvancedModelBox[] arr = nn(parts);
        if (arr.length > 0) m.chainWave(arr, s, d, off, t, w);
    }
    private static void bob(AnimatableModel m, AdvancedModelBox part, float s, float d, boolean bounce, float f, float f1) {
        if (part != null) m.bob(part, s, d, bounce, f, f1);
    }

    @Override
    protected void performAnimations(AnimatableModel model, SpinosaurusEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {


        AdvancedModelBox hips   = model.getCube("bodyhips");
        AdvancedModelBox body   = model.getCube("body");
        AdvancedModelBox chest  = model.getCube("chest");     // optional in some exports

        AdvancedModelBox neck   = model.getCube("neck");      // base segment
        AdvancedModelBox neckthroat   = model.getCube("neckthroat");      // base segment
        AdvancedModelBox neck2  = model.getCube("neck2");
        AdvancedModelBox neck3  = model.getCube("neck3");
        AdvancedModelBox neck4  = model.getCube("neck4");
        AdvancedModelBox neck5  = model.getCube("neck5");
        AdvancedModelBox neck6  = model.getCube("neck6");
        AdvancedModelBox neck7  = model.getCube("neck7");
        AdvancedModelBox neck8  = model.getCube("neck8");
        AdvancedModelBox head   = model.getCube("Head");

        AdvancedModelBox tail   = model.getCube("tail");      // first tail piece is literally "tail"
        AdvancedModelBox tail2  = model.getCube("tail2");
        AdvancedModelBox tail3  = model.getCube("tail3");
        AdvancedModelBox tail4  = model.getCube("tail4");
        AdvancedModelBox tail5  = model.getCube("tail5");
        AdvancedModelBox tail6  = model.getCube("tail6");
        AdvancedModelBox tail7  = model.getCube("tail7");
        AdvancedModelBox tail8  = model.getCube("tail8");
        AdvancedModelBox tail9  = model.getCube("tail9");
        AdvancedModelBox tail10 = model.getCube("tail10");
        AdvancedModelBox tail11 = model.getCube("tail11");
        AdvancedModelBox tail12 = model.getCube("tail12");
        AdvancedModelBox tail13 = model.getCube("tail13");
        AdvancedModelBox tail14 = model.getCube("tail14");

        AdvancedModelBox leftLeg   = model.getCube("leftleg");
        AdvancedModelBox rightLeg  = model.getCube("rightleg");
        AdvancedModelBox leftAnkle = model.getCube("leftankle");
        AdvancedModelBox rightAnkle= model.getCube("rightankle");
        AdvancedModelBox leftFoot  = model.getCube("leftfoot");
        AdvancedModelBox rightFoot = model.getCube("rightfoot");

        AdvancedModelBox leftBicep   = model.getCube("leftbicep");
        AdvancedModelBox rightBicep  = model.getCube("rightbicep");
        AdvancedModelBox leftArm   = model.getCube("leftarm");
        AdvancedModelBox rightArm  = model.getCube("rightarm");

        // build chains SAFELY (nulls get filtered inside helpers)
        AdvancedModelBox[] spine = nn(hips, body, chest);
        AdvancedModelBox[] neckChain = nn(neckthroat,neck, neck2, neck3, neck4, neck5, neck6, neck7, neck8, head);
        AdvancedModelBox[] tailChain = nn(tail14, tail13, tail12, tail11, tail10, tail9, tail8, tail7, tail6, tail5, tail4, tail3, tail2, tail);

        // ===== idle (gentle, heavy creature) =====
        float idleSpeed = 0.35F, idleDeg = 0.6F;
        bob(model, hips, idleSpeed * 0.25F, idleDeg * 1.2F, false, limbSwing, limbSwingAmount);
        wave(model, idleSpeed * 0.25F, idleDeg * 0.05F, 2, ticks, 0.5F, spine);
        wave(model, idleSpeed * 0.25F, -idleDeg * 0.05F, 3, ticks, 0.5F, neckChain);
        wave(model, 0.10F, 0.05F, 2, ticks, 0.25F, tailChain);

        boolean swimming = entity != null && entity.isUnderWater();

        if (swimming) {
            // ===== exact leg tuck (copied pose) =====
            if (leftLeg  != null)  leftLeg.rotateAngleX  = 1.045897F;
            if (rightLeg != null)  rightLeg.rotateAngleX = 1.083065F;
            if (leftBicep  != null)  leftBicep.rotateAngleX  = 1.045897F;
            if (rightBicep != null)  rightBicep.rotateAngleX = 1.083065F;
            if (leftArm  != null)  leftArm.rotateAngleX  = 0.175897F;
            if (rightArm != null)  rightArm.rotateAngleX = 0.143065F;
            float ankleTuck = -0.84F;
            if (leftAnkle  != null) leftAnkle.rotateAngleX  = ankleTuck;
            if (rightAnkle != null) rightAnkle.rotateAngleX = ankleTuck;
            if (leftFoot   != null) leftFoot.rotateAngleX   = 1.0920526F;
            if (rightFoot  != null) rightFoot.rotateAngleX  = 1.2805481F;

            // ===== swimming undulation (Baryonyx feel, stretched to 14 links) =====
            float swimSpeed = 0.25F, swimDeg = 0.40F, phase = 0.25F;
            swing(model, swimSpeed, swimDeg, 2, ticks, phase, tailChain);
            wave (model, swimSpeed * 0.05F, swimDeg * 0.25F, 2, ticks, phase, tailChain);

            swing(model, swimSpeed * 0.8F, swimDeg * 0.12F, 2, ticks, phase, spine);
            wave (model, swimSpeed * 0.6F, swimDeg * 0.06F, 2, ticks, phase, spine);

            swing(model, swimSpeed * 0.8F, -swimDeg * 0.08F, 2, ticks, phase, neckChain);
            wave (model, swimSpeed * 0.6F, -swimDeg * 0.05F, 2, ticks, phase, neckChain);

            if (hips != null) hips.rotateAngleZ += (float)Math.sin(ticks * swimSpeed) * (swimDeg * 0.10F);


            if (leftArm  != null)  leftArm .rotateAngleX += (float)Math.sin(ticks * 0.2F) * 0.05F;
            if (rightArm != null)  rightArm.rotateAngleX += (float)Math.cos(ticks * 0.2F) * 0.05F;

            model.faceTarget(rotationYaw * 0.5F, rotationPitch * 0.5F, 1.0F, nn(neckthroat,neck, neck2, neck3, head));
        } else {
            model.faceTarget(rotationYaw, rotationPitch, 1.0F, nn(neckthroat,neck, neck2, neck3, head));
        }
    }
}
