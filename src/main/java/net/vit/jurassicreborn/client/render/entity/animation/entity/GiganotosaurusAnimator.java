package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.GiganotosaurusEntity;

public class GiganotosaurusAnimator extends EntityAnimator<GiganotosaurusEntity> {


    @Override
    protected void performAnimations(AnimatableModel model, GiganotosaurusEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        AdvancedModelBox body1 = model.getCube( "Body");
        AdvancedModelBox neck1 = model.getCube( "Neck");
        AdvancedModelBox jaw   = model.getCube( "Jaw");
        AdvancedModelBox tail1 = model.getCube( "Tail");
        AdvancedModelBox tail2 = model.getCube( "Tail2");
        AdvancedModelBox tail3 = model.getCube( "Tail3");
        AdvancedModelBox tail4 = model.getCube( "Tail4");
        AdvancedModelBox tail5 = model.getCube( "Tail5");
        AdvancedModelBox tail6 = model.getCube( "Tail6");
        AdvancedModelBox tail7 = model.getCube( "Tail7");
        AdvancedModelBox tail8 = model.getCube( "Tail8");

        AdvancedModelBox[] torso = new AdvancedModelBox[] {body1};
        AdvancedModelBox[] neckChain = new AdvancedModelBox[] {neck1};
        AdvancedModelBox[] tailChain = new AdvancedModelBox[] {tail8, tail7, tail6, tail5, tail4, tail3, tail2, tail1};
        // ===== Idle tuning =====
        float idleSpeed  = 0.1F;
        float idleDeg    = 0.1F;
        float walkSpeed  = 0.6F;
        float walkDeg    = 0.12F;

        // Tail slowed to 1/3 speed
        float tailIdleSpeed = idleSpeed / 2.0F;
        float tailWalkSpeed = walkSpeed / 2.0F;

        // ----- Breathing -----
        if (body1 != null) model.bob(body1, idleSpeed, 0.35F, false, ticks, 1.0F);
        if (torso.length > 0) {
            model.chainWave(torso, idleSpeed, idleDeg * 0.45F, 2, ticks, 1.0F);
        }

        // ----- Neck + head idle motion -----
        if (neckChain.length > 0) {
            model.chainWave(neckChain, idleSpeed * 0.9F, idleDeg * 0.3F, -2, ticks, 1.0F);
            model.chainSwing(neckChain, idleSpeed * 0.8F, idleDeg * 0.25F, -2, ticks, 1.0F);
        }

        // ----- Face target -----
        model.faceTarget(rotationYaw, rotationPitch, 1.0F, neck1);

        // ----- Jaw subtle idle -----
        if (jaw != null && !entity.isCarcass()) {
            jaw.rotateAngleX += Math.sin(ticks * 0.07F) * 0.02F;
        }

        // ----- Tail sway (slowed to 1/3 speed) -----
        if (tailChain.length > 0) {
            model.chainSwing(tailChain, tailIdleSpeed, 0.12F, -2, ticks, 1.0F);
            model.chainWave (tailChain, tailIdleSpeed * 0.8F, 0.03F, -2, ticks, 1.0F);

            if (limbSwingAmount > 0.1F) {
                model.chainSwing(tailChain, tailWalkSpeed, walkDeg, -2, limbSwing, limbSwingAmount);
            }
            entity.tailBuffer.applyChainSwingBuffer(tailChain);
        }
    }
}
