package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.TyrannosaurusEntity;

public class TyrannosaurusAnimator extends EntityAnimator<TyrannosaurusEntity> {
    

    @Override
    protected void performAnimations(AnimatableModel model, TyrannosaurusEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        // ----- Grab parts -----
        AdvancedModelBox body1 = model.getCube( "Body 1");
        AdvancedModelBox body2 = model.getCube( "Body 2");
        AdvancedModelBox body3 = model.getCube( "Body 3");

        AdvancedModelBox neck1 = model.getCube( "Neck1");
        AdvancedModelBox neck2 = model.getCube( "Neck2");
        AdvancedModelBox neck3 = model.getCube( "Neck3");
        AdvancedModelBox neck4 = model.getCube( "Neck4");
        AdvancedModelBox neck5 = model.getCube( "Neck5");
        AdvancedModelBox neck6 = model.getCube( "Neck6");
        AdvancedModelBox head  = model.getCube( "Head");
        AdvancedModelBox jaw   = model.getCube( "Lower Jaw");

        AdvancedModelBox tail1 = model.getCube( "Tail 1");
        AdvancedModelBox tail2 = model.getCube( "Tail 2");
        AdvancedModelBox tail3 = model.getCube( "Tail 3");
        AdvancedModelBox tail4 = model.getCube( "Tail 4");
        AdvancedModelBox tail5 = model.getCube( "Tail 5");
        AdvancedModelBox tail6 = model.getCube( "Tail 6");
        AdvancedModelBox tail7 = model.getCube( "Tail 7");

        AdvancedModelBox[] torso = new AdvancedModelBox[] {body1, body2, body3};
        AdvancedModelBox[] neckChain = new AdvancedModelBox[] {neck1};
        AdvancedModelBox[] tailChain = new AdvancedModelBox[] {tail7, tail6, tail5, tail4, tail3, tail2, tail1};

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
            model.chainSwing(tailChain, tailIdleSpeed, 0.17F, -2, ticks, 1.0F);
            model.chainWave (tailChain, tailIdleSpeed * 0.8F, 0.05F, -2, ticks, 1.0F);

            if (limbSwingAmount > 0.1F) {
                model.chainSwing(tailChain, tailWalkSpeed, walkDeg, -2, limbSwing, limbSwingAmount);
            }
            entity.tailBuffer.applyChainSwingBuffer(tailChain);
        }
    }
}
