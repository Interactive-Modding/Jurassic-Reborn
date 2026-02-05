package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MegalodonEntity;

@OnlyIn(Dist.CLIENT)
public class MegalodonAnimator extends EntityAnimator<MegalodonEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, MegalodonEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
        {

            AdvancedModelBox body    = model.getCube("Body");
            AdvancedModelBox head    = model.getCube("Head");
            AdvancedModelBox pecL    = model.getCube("LeftPectoral");
            AdvancedModelBox pecR    = model.getCube("RightPectoral");
            AdvancedModelBox analFin = model.getCube("AnalFin");
            AdvancedModelBox pelvicL = model.getCube("PelvicL");
            AdvancedModelBox pelvicR = model.getCube("PelvicR");

            AdvancedModelBox tail1 = model.getCube("Tail1");
            AdvancedModelBox tail2 = model.getCube("Tail2");
            AdvancedModelBox tail3 = model.getCube("Tail3");
            AdvancedModelBox tail4 = model.getCube("Tail4");
            AdvancedModelBox tail5 = model.getCube("Tail5");
            AdvancedModelBox tail6 = model.getCube("Tail6");

            // tip -> base for nice propagation
            AdvancedModelBox[] tail = new AdvancedModelBox[]{tail6, tail5, tail4, tail3, tail2, tail1};
            // IMPORTANT: do NOT include head here (prevents double-driving)
            AdvancedModelBox[] spine = body != null ? new AdvancedModelBox[]{body} : new AdvancedModelBox[0];

            final boolean inWater = entity.isInWater();

            // Calm, constant swim
            float swimSpeed   = 0.20F;
            float swimDegree  = 0.09F;
            float waveFactor  = 0.05F;

            float bobSpeed    = 0.16F;
            float bobDegree   = 0.025F;

            float flopSpeed   = 0.30F;
            float flopDegree  = 0.12F;

            if (body != null) {
                model.bob(body, inWater ? bobSpeed : 0.45F, inWater ? bobDegree : 0.10F, false, ticks, 1.0F);
            }

            if (inWater) {
                // Tail only
                model.chainSwing(tail, swimSpeed,            swimDegree,               2, ticks, 1.0F);
                model.chainWave (tail, swimSpeed * 0.55F,    swimDegree * waveFactor,  2, ticks, 1.0F);

                // Subtle body sway (no head in this chain!)
                if (spine.length > 0) {
                    model.chainSwing(spine, swimSpeed * 0.70F, -swimDegree * 0.18F, 2, ticks + 0.5F, 0.9F);
                }

                if (pecL != null && pecR != null) {
                    float pF = 0.40F;
                    float pA = 0.07F;
                    float s = (float)Math.sin(ticks * pF);
                    float c = (float)Math.cos(ticks * pF);
                    pecL.rotateAngleZ +=  s * (pA * 0.65F) - 0.18F;
                    pecR.rotateAngleZ += -s * (pA * 0.65F) + 0.18F;
                    pecL.rotateAngleX +=  c * (pA * 0.20F);
                    pecR.rotateAngleX += -c * (pA * 0.20F);
                }

                if (pelvicL != null && pelvicR != null) {
                    float s = 0.02F;
                    float sw = (float)Math.sin(ticks * 0.55F);
                    pelvicL.rotateAngleZ +=  s * sw;
                    pelvicR.rotateAngleZ += -s * sw;
                }

                if (analFin != null) {
                    analFin.rotateAngleY += 0.012F * (float)Math.sin(ticks * 0.60F);
                }

            } else {
                // Out of water: tail + body flop only (head left to faceTarget)
                model.chainSwing(tail,      flopSpeed,         flopDegree,          2, ticks, 1.0F);
                if (spine.length > 0) {
                    model.chainSwing(spine, flopSpeed * 0.7F, -flopDegree * 0.18F, 2, ticks, 0.9F);
                }
                if (pecL != null) { pecL.rotateAngleZ -= 0.22F; pecL.rotateAngleX += 0.15F; }
                if (pecR != null) { pecR.rotateAngleZ += 0.22F; pecR.rotateAngleX += 0.15F; }
            }

            // HEAD FIX: head is only driven by faceTarget, softly and with clamps
            if (head != null) {
                // gentle damping multiplier so we don't snap
                float damp = 0.30F; // lower = softer response
                model.faceTarget(rotationYaw * damp, rotationPitch * damp, 0.35F, head);

                // HARD CLAMPS (about ±10° yaw, ±8° pitch)
                float maxYaw   = (float)Math.toRadians(10.0);
                float maxPitch = (float)Math.toRadians(8.0);
                if (head.rotateAngleY >  maxYaw)   head.rotateAngleY =  maxYaw;
                if (head.rotateAngleY < -maxYaw)   head.rotateAngleY = -maxYaw;
                if (head.rotateAngleX >  maxPitch) head.rotateAngleX =  maxPitch;
                if (head.rotateAngleX < -maxPitch) head.rotateAngleX = -maxPitch;
            }

        }
    }
}
