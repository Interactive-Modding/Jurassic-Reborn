package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.CalymeneEntity;

@OnlyIn(Dist.CLIENT)
public class CalymeneAnimator extends EntityAnimator<CalymeneEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, CalymeneEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        // --- CUBES ---
        AdvancedModelBox root      = model.getCube("Trilobite");
        AdvancedModelBox head      = model.getCube("Head");
        AdvancedModelBox body1     = model.getCube("Bodyseg1");
        AdvancedModelBox body2     = model.getCube("Bodyseg2");
        AdvancedModelBox body3     = model.getCube("Bodyseg3");
        AdvancedModelBox body4     = model.getCube("Bodyseg4");
        AdvancedModelBox body5     = model.getCube("Bodyseg5");
        AdvancedModelBox eyes      = model.getCube("Eyes");

        AdvancedModelBox antennaR  = model.getCube("AntennaR");
        AdvancedModelBox antennaL  = model.getCube("AntennaL");

        AdvancedModelBox legsL     = model.getCube("LegsL");
        AdvancedModelBox legsR     = model.getCube("LegsR");
        AdvancedModelBox legsL2    = model.getCube("LegsL2");
        AdvancedModelBox legsR2    = model.getCube("LegsR2");
        AdvancedModelBox legsL3    = model.getCube("LegsL3");
        AdvancedModelBox legsR3    = model.getCube("LegsR3");
        AdvancedModelBox legsL4    = model.getCube("LegsL4");
        AdvancedModelBox legsR4    = model.getCube("LegsR4");

        AdvancedModelBox[] bodyChain  = new AdvancedModelBox[]{head, body1, body2, body3, body4, body5};
        AdvancedModelBox[] leftLegs   = new AdvancedModelBox[]{legsL,  legsL2, legsL3, legsL4};
        AdvancedModelBox[] rightLegs  = new AdvancedModelBox[]{legsR,  legsR2, legsR3, legsR4};
        if (entity.isCarcass() || !entity.isAlive()) {
            return;
        }
        // --- STATE ---
        final boolean onBottom = entity.isInWater() && entity.isOnBottom();
        final boolean swimming = entity.isInWater() && !onBottom;

        // --- QUICK EXIT: Defensive roll overrides everything ---
        if (entity.isRolled()) {
            float curlSign = -1F;
            float curlBase = 0.85F;
            if (head  != null) head.rotateAngleX  += curlSign * curlBase * 1.10F;
            if (body1 != null) body1.rotateAngleX += curlSign * curlBase * 0.95F;
            if (body2 != null) body2.rotateAngleX += curlSign * curlBase * 0.80F;
            if (body3 != null) body3.rotateAngleX += curlSign * curlBase * 0.80F;
            if (body4 != null) body4.rotateAngleX += curlSign * curlBase * 0.95F;
            if (body5 != null) body5.rotateAngleX += curlSign * curlBase * 1.15F;
            if (antennaR != null) antennaR.rotateAngleX += curlSign * 1.00F;
            if (antennaL != null) antennaL.rotateAngleX += curlSign * 1.00F;
            for (AdvancedModelBox l : leftLegs)  if (l != null) l.rotateAngleZ += 0.45F;
            for (AdvancedModelBox r : rightLegs) if (r != null) r.rotateAngleZ -= 0.45F;
            return;
        }

        // --- BASELINE IDLE BOB ---
        // Slightly different feel for crawl vs swim
        float bobSpeed  = swimming ? 0.12F : 0.08F;
        float bobDegree = swimming ? 0.20F : 0.10F;
        if (root != null) model.bob(root, bobSpeed, bobDegree, false, ticks, 0.25F);

        if (onBottom) {
            // =========================
            // BENTHIC CRAWL (metachronal)
            // =========================
            // Paleontology note: trilobites used metachronal leg waves that travel posteriorly.
            // We simulate that with chainSwing across leg rows, with a positive phase offset.

            // Body is mostly stable; tiny undulation to suggest shell flex
            model.chainWave(bodyChain, 0.12F, 0.05F, 1, ticks, 0.25F);
            model.chainSwing(bodyChain, 0.10F, 0.04F, 2, ticks, 0.25F);

            // Antennae probe forward with subtle anti-phase yaw and nod
            float antSway = (float)Math.sin(ticks * 0.25F) * 0.18F;
            float antNod  = (float)Math.sin(ticks * 0.17F) * 0.08F;
            if (antennaR != null) { antennaR.rotateAngleY +=  antSway; antennaR.rotateAngleX += antNod; }
            if (antennaL != null) { antennaL.rotateAngleY += -antSway; antennaL.rotateAngleX += antNod * 0.9F; }

            // Leg gait: metachronal wave from front (legsL/legsR) to back (legsL4/legsR4)
            // Left and right are slightly out of phase so they alternate smoothly.
            float crawlSpeed   = 0.9F;    // cycle speed
            float crawlDegree  = 0.45F;   // swing amplitude
            float waveOffset   = 0.6F;    // phase offset per segment (creates the traveling wave)
            float liftFactor   = 0.25F;   // tiny vertical "lift" to suggest stepping

            model.chainSwing(leftLegs,  crawlSpeed, crawlDegree,  waveOffset, ticks, 0.5F);
            model.chainSwing(rightLegs, crawlSpeed, crawlDegree,  waveOffset, ticks + 3.0F, 0.5F);

            // Add small "lift" using chainWave so tips raise as they swing forward
            model.chainWave(leftLegs,   crawlSpeed, crawlDegree * liftFactor, waveOffset, ticks, 0.5F);
            model.chainWave(rightLegs,  crawlSpeed, crawlDegree * liftFactor, waveOffset, ticks + 3.0F, 0.5F);

            // Keep head slightly pitched down while crawling
            if (head != null) head.rotateAngleX += 0.06F;

        } else if (swimming) {
            // ============
            // SWIM (half strength)
            // ============
            float swimScale  = 0.50F;
            float swimSpeed  = 0.45F * swimScale;
            float swimDegree = 0.60F * swimScale;

            model.chainWave(bodyChain, swimSpeed, swimDegree * 0.35F, 2, ticks, 0.5F);
            model.chainSwing(bodyChain, swimSpeed, swimDegree * 0.55F, 3, ticks, 0.5F);

            if (head != null) head.rotateAngleX += (float)Math.sin(ticks * 0.17F) * (0.10F * swimScale);

            float antSway = (float)Math.sin(ticks * 0.30F) * (0.30F * swimScale);
            float antNod  = (float)Math.sin(ticks * 0.22F) * (0.10F * swimScale);
            if (antennaR != null) { antennaR.rotateAngleY +=  antSway; antennaR.rotateAngleX += antNod; }
            if (antennaL != null) { antennaL.rotateAngleY += -antSway; antennaL.rotateAngleX += antNod * 0.9F; }

            float rowSpeed  = 0.55F * swimScale;
            float rowDegree = 0.55F * swimScale;
            model.chainSwing(leftLegs,  rowSpeed, rowDegree * 0.55F, 2, ticks, 0.6F);
            model.chainWave (leftLegs,  rowSpeed, rowDegree * 0.25F, 1, ticks, 0.6F);
            model.chainSwing(rightLegs, rowSpeed, rowDegree * 0.55F, 2, ticks + 4.0F, 0.6F);
            model.chainWave (rightLegs, rowSpeed, rowDegree * 0.25F, 1, ticks + 4.0F, 0.6F);

        } else {
            // ============
            // LAND/AIR IDLE (fallback)
            // ============
            float idleSpeed  = 0.12F, idleDegree = 0.15F;
            float legSpd     = 0.18F, legDeg     = 0.20F;

            model.chainWave(bodyChain, idleSpeed,  idleDegree * 0.7F, 2, ticks, 0.25F);
            model.chainSwing(bodyChain, idleSpeed, idleDegree * 0.4F, 3, ticks, 0.25F);

            if (head != null) head.rotateAngleX += (float)Math.sin(ticks * 0.07F) * 0.02F;
            if (eyes != null) eyes.rotateAngleY += (float)Math.sin(ticks * 0.11F) * 0.015F;

            float antSway = (float)Math.sin(ticks * 0.16F) * 0.25F;
            float antNod  = (float)Math.sin(ticks * 0.12F) * 0.08F;
            if (antennaR != null) { antennaR.rotateAngleY +=  antSway; antennaR.rotateAngleX += antNod; }
            if (antennaL != null) { antennaL.rotateAngleY += -antSway; antennaL.rotateAngleX += antNod * 0.9F; }

            model.chainWave(leftLegs,   legSpd, legDeg * 0.15F, 1, ticks, 0.15F);
            model.chainSwing(leftLegs,  legSpd, legDeg * 0.20F, 2, ticks, 0.15F);
            model.chainWave(rightLegs,  legSpd, legDeg * 0.15F, 1, ticks + 5.5F, 0.15F);
            model.chainSwing(rightLegs, legSpd, legDeg * 0.20F, 2, ticks + 5.5F, 0.15F);
        }

        // Optional tiny look turns (kept subtle)
        // model.faceTarget(rotationYaw * 0.15F, rotationPitch * 0.15F, 1.0F, head);
    }
}
