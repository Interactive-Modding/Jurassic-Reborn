package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.ElasmotheriumEntity;

public class ElasmotheriumAnimator extends EntityAnimator<ElasmotheriumEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, ElasmotheriumEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        // torso segments
        AdvancedModelBox back   = model.getCube("back");
        AdvancedModelBox rear   = model.getCube("rear");
        AdvancedModelBox gut    = model.getCube("gut");
        AdvancedModelBox shoulder = model.getCube("shoulder");

        // neck & head
        AdvancedModelBox neckThicc = model.getCube("neck_thicc"); // base pad
        AdvancedModelBox neck1 = model.getCube("neck1");
        AdvancedModelBox neck2 = model.getCube("neck2");
        AdvancedModelBox neck3 = model.getCube("neck3");
        AdvancedModelBox head  = model.getCube("head");

        AdvancedModelBox[] neckChain = new AdvancedModelBox[] {
                head, neck3, neck2, neck1, neckThicc
        };

        // tail (tip-first)
        AdvancedModelBox tailBase  = model.getCube("tail_base");
        AdvancedModelBox tail1     = model.getCube("tail1");
        AdvancedModelBox tail2     = model.getCube("tail2");
        AdvancedModelBox tail4     = model.getCube("tail4"); // model skips "tail3" in this export
        AdvancedModelBox tailFluff = model.getCube("tail_fluff");

        AdvancedModelBox[] tail = new AdvancedModelBox[] {
                tailFluff, tail4, tail2, tail1, tailBase
        };

        // ---- idle tuning (big mammal) ----
        float idleSpeed  = 0.09F;
        float idleDegree = 0.10F;

        // deep breathing across torso
        if (gut != null) model.bob(gut, idleSpeed, 0.55F, false, ticks, 1.0F);
        model.chainWave(new AdvancedModelBox[]{ back, rear, gut, shoulder }, idleSpeed * 0.6F, idleDegree * 0.45F, 2, ticks, 1.0F);

        // neck & head subtle motion
        model.chainWave(neckChain, idleSpeed * 0.55F, idleDegree * 0.5F, -2, ticks, 1.0F);

        model.chainSwing(new AdvancedModelBox[]{ head }, idleSpeed * 0.9F, 0.08F, 0, ticks, 1.0F);
        model.bob(head, idleSpeed, 0.04F, false, ticks, 1.0F);

        // tail sway (short tail; keep subtle)
        model.chainSwing(tail, idleSpeed, 0.12F, -2, ticks, 1.0F);

        // look-at: steer with necks, head last
        model.faceTarget(rotationYaw, rotationPitch, 0.85F, head);

        // smoothing if available
        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
