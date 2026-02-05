package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.SuchomimusEntity;

import java.util.ArrayList;
import java.util.List;

public class SuchomimusAnimator extends EntityAnimator<SuchomimusEntity> {

    // ---- helpers (same pattern as Spino final) ----
    private static AdvancedModelBox[] nn(AdvancedModelBox... parts) {
        List<AdvancedModelBox> ok = new ArrayList<>();
        for (AdvancedModelBox p : parts) if (p != null) ok.add(p);
        return ok.toArray(new AdvancedModelBox[0]);
    }
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
    protected void performAnimations(AnimatableModel parModel, SuchomimusEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {


        AdvancedModelBox tail1 = parModel.getCube("Tail Base");
        AdvancedModelBox tail2 = parModel.getCube("Tail 2");
        AdvancedModelBox tail3 = parModel.getCube("Tail 3");
        AdvancedModelBox tail4 = parModel.getCube("Tail 4");
        AdvancedModelBox tail5 = parModel.getCube("Tail 6");
        AdvancedModelBox neck1 = parModel.getCube("Neck Base");
        AdvancedModelBox head  = parModel.getCube("Head");

        AdvancedModelBox[] tail = {tail5, tail4, tail3, tail2, tail1};

        float globalSpeed  = 0.5F;
        float globalDegree = 0.5F;

        // ======= SWIM PARTS (Spino code, adapted names) =======
        // torso (Spino hips/body/chest -> Sucho Body Rear/Middle/Front)
        AdvancedModelBox bodyRear  = parModel.getCube("Body Rear");   if (bodyRear == null)  bodyRear  = parModel.getCube("BodyRear");
        AdvancedModelBox bodyMid   = parModel.getCube("Body Middle"); if (bodyMid == null)   bodyMid   = parModel.getCube("BodyMiddle");
        AdvancedModelBox bodyFront = parModel.getCube("Body Front");  if (bodyFront == null) bodyFront = parModel.getCube("BodyFront");

        // full neck chain (Spino neck..neck8 -> Sucho Neck Base..Neck 9 + Head)
        AdvancedModelBox neck2 = parModel.getCube("Neck 2"); if (neck2 == null) neck2 = parModel.getCube("Neck2");
        AdvancedModelBox neck3 = parModel.getCube("Neck 3"); if (neck3 == null) neck3 = parModel.getCube("Neck3");
        AdvancedModelBox neck4 = parModel.getCube("Neck 4"); if (neck4 == null) neck4 = parModel.getCube("Neck4");
        AdvancedModelBox neck5 = parModel.getCube("Neck 5"); if (neck5 == null) neck5 = parModel.getCube("Neck5");
        AdvancedModelBox neck6 = parModel.getCube("Neck 6"); if (neck6 == null) neck6 = parModel.getCube("Neck6");
        AdvancedModelBox neck7 = parModel.getCube("Neck 7"); if (neck7 == null) neck7 = parModel.getCube("Neck7");
        AdvancedModelBox neck8 = parModel.getCube("Neck 8"); if (neck8 == null) neck8 = parModel.getCube("Neck8");
        AdvancedModelBox neck9 = parModel.getCube("Neck 9"); if (neck9 == null) neck9 = parModel.getCube("Neck9");

        // extend tail chain if those segments exist (tip->base order like Spino)
        AdvancedModelBox t7  = parModel.getCube("Tail 7");
        AdvancedModelBox t8  = parModel.getCube("Tail 8");
        AdvancedModelBox t9  = parModel.getCube("Tail 9");
        AdvancedModelBox t10 = parModel.getCube("Tail 10");

        // arms (Spino left/right bicep + forearm + hand)
        AdvancedModelBox armUpperL = parModel.getCube("Arm UPPER Left");  if (armUpperL == null) armUpperL = parModel.getCube("ArmUPPERLeft");
        AdvancedModelBox armUpperR = parModel.getCube("Arm UPPER Right"); if (armUpperR == null) armUpperR = parModel.getCube("ArmUPPERRight");
        AdvancedModelBox armMidL   = parModel.getCube("Arm MID Left");    if (armMidL == null)   armMidL   = parModel.getCube("ArmMIDLeft");
        AdvancedModelBox armMidR   = parModel.getCube("Arm MID Right");   if (armMidR == null)   armMidR   = parModel.getCube("ArmMIDRight");
        AdvancedModelBox handL     = parModel.getCube("Hand Left");       if (handL == null)     handL     = parModel.getCube("HandLeft");
        AdvancedModelBox handR     = parModel.getCube("Hand Right");      if (handR == null)     handR     = parModel.getCube("HandRight");

        // legs (Spino left/right leg + ankle + foot -> map to Upper/Middle/Lower/Foot)
        AdvancedModelBox legUpperL = parModel.getCube("Left Leg Upper");   if (legUpperL == null) legUpperL = parModel.getCube("LeftLegUpper");
        AdvancedModelBox legUpperR = parModel.getCube("Right Leg Upper");
        AdvancedModelBox legMidL   = parModel.getCube("Left Leg Middle");  if (legMidL == null)   legMidL   = parModel.getCube("LeftLegMiddle");
        AdvancedModelBox legMidR   = parModel.getCube("Right Leg Middle"); if (legMidR == null)   legMidR   = parModel.getCube("RightLegMiddle");
        AdvancedModelBox legLowL   = parModel.getCube("Left Leg Lower");   if (legLowL == null)   legLowL   = parModel.getCube("LeftLegLower");
        AdvancedModelBox legLowR   = parModel.getCube("Right Leg Lower");  if (legLowR == null)   legLowR   = parModel.getCube("RightLegLower");
        AdvancedModelBox footL     = parModel.getCube("Left Leg Foot");    if (footL == null)     footL     = parModel.getCube("LeftLegFoot");
        AdvancedModelBox footR     = parModel.getCube("Right Leg Foot");   if (footR == null)     footR     = parModel.getCube("RightLegFoot");

        // chains for swimming (Spino used hips/body/chest; we use bodyRear/mid/front)
        AdvancedModelBox[] spine     = nn(bodyRear, bodyMid, bodyFront);
        AdvancedModelBox[] neckChain = nn(neck1, neck2, neck3, neck4, neck5, neck6, neck7, neck8, neck9, head);
        AdvancedModelBox[] tailChain = nn(t10, t9, t8, t7, tail5, tail4, tail3, tail2, tail1);


        if (entity == null || !entity.isUnderWater()) {
            parModel.chainWave(tail, globalSpeed * 0.5F, globalDegree * 0.05F, 1, limbSwing, limbSwingAmount);

            parModel.faceTarget(rotationYaw, rotationPitch, 2.0F, neck1, head);
            if (entity != null) entity.tailBuffer.applyChainSwingBuffer(tail);
            return;
        }

        // ======= SWIMMING (copied from Spino final; only names changed) =======
        // exact leg/arm tuck
        if (legUpperL != null) legUpperL.rotateAngleX = 1.245897F;
        if (legUpperR != null) legUpperR.rotateAngleX = 1.283065F;
        if (armUpperL != null) armUpperL.rotateAngleX = 1.045897F;
        if (armUpperR != null) armUpperR.rotateAngleX = 1.083065F;
        if (armMidL   != null) armMidL.rotateAngleX   = 0.175897F;
        if (armMidR   != null) armMidR.rotateAngleX   = 0.173065F;

        if (legMidL != null) legMidL.rotateAngleX = 1.045897F;
        if (legMidR != null) legMidR.rotateAngleX = 1.045897F;
        if (footL != null) footL.rotateAngleX = -0.2920526F;
        if (footR != null) footR.rotateAngleX = -0.2920526F;
        final float ankleTuck = -0.84F; // deepest pose would be ~-0.9154F
        if (legLowL != null) legLowL.rotateAngleX = ankleTuck;
        if (legLowR != null) legLowR.rotateAngleX = ankleTuck;


        // swim undulation (same constants as Spino)
        float swimSpeed = 0.25F, swimDeg = 0.40F, phase = 0.25F;
        swing(parModel, swimSpeed,         swimDeg,         2, ticks, phase, tailChain);
        wave (parModel, swimSpeed * 0.05F, swimDeg * 0.25F, 2, ticks, phase, tailChain);

        swing(parModel, swimSpeed * 0.8F,  swimDeg * 0.12F, 2, ticks, phase, spine);
        wave (parModel, swimSpeed * 0.6F,  swimDeg * 0.06F, 2, ticks, phase, spine);

        swing(parModel, swimSpeed * 0.8F, -swimDeg * 0.08F, 2, ticks, phase, neckChain);
        wave (parModel, swimSpeed * 0.6F, -swimDeg * 0.05F, 2, ticks, phase, neckChain);

        if (bodyRear != null) bodyRear.rotateAngleZ += (float)Math.sin(ticks * swimSpeed) * (swimDeg * 0.10F);

        // tiny hand drift; same axis & magnitude as Spino final
        if (handL != null) handL.rotateAngleX += (float)Math.sin(ticks * 0.2F) * 0.05F;
        if (handR != null) handR.rotateAngleX += (float)Math.cos(ticks * 0.2F) * 0.05F;

        // keep faceTarget disabled during swim (to mirror Spino final)


        if (entity != null) {
            if (tailChain.length > 0) entity.tailBuffer.applyChainSwingBuffer(tailChain);
            else                      entity.tailBuffer.applyChainSwingBuffer(tail);
        }
    }
}
