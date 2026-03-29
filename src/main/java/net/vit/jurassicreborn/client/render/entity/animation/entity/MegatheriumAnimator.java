package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MegatheriumEntity;
import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MegatheriumAnimator extends EntityAnimator<MegatheriumEntity> {

    private static AdvancedModelBox[] nn(AdvancedModelBox... parts) {
        java.util.ArrayList<AdvancedModelBox> out = new java.util.ArrayList<>();
        for (AdvancedModelBox p : parts) if (p != null) out.add(p);
        return out.toArray(new AdvancedModelBox[0]);
    }

    @Override
    protected void performAnimations(AnimatableModel model, MegatheriumEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        AdvancedModelBox hips   = model.getCube("hips");
        AdvancedModelBox body   = model.getCube("body");
        AdvancedModelBox belly  = model.getCube("belly");
        AdvancedModelBox chest  = model.getCube("chest");

        AdvancedModelBox neck1  = model.getCube("neck1");
        AdvancedModelBox neck2  = model.getCube("neck2");
        AdvancedModelBox neck3  = model.getCube("neck3");
        AdvancedModelBox neck4  = model.getCube("neck4");
        AdvancedModelBox throat3= model.getCube("throat3");
        AdvancedModelBox throat4= model.getCube("throat4");
        AdvancedModelBox head   = model.getCube("head");

        AdvancedModelBox tail1  = model.getCube("tail1");
        AdvancedModelBox tail2  = model.getCube("tail2");
        AdvancedModelBox tail3  = model.getCube("tail3");
        AdvancedModelBox tail4  = model.getCube("tail4");
        AdvancedModelBox tail5  = model.getCube("tail5");

        AdvancedModelBox[] torso = nn(hips, belly, body, chest);
        AdvancedModelBox[] neck  = nn(head, neck4, neck3, neck2, neck1, throat4, throat3);
        AdvancedModelBox[] tail  = nn(tail5, tail4, tail3, tail2, tail1);

        float idleSpeed  = 0.10F;
        float idleDegree = 0.10F;

        // breathing across torso
        if (belly != null) model.bob(belly, idleSpeed, 0.50F, false, ticks, 1.0F);
        if (hips  != null) model.bob(hips,  idleSpeed, 0.35F, false, ticks, 1.0F);
        if (torso.length > 1) model.chainWave(torso, idleSpeed * 0.65F, idleDegree * 0.45F, 2, ticks, 1.0F);

        // neck/head
        if (neck.length > 0) model.chainWave(neck, idleSpeed * 0.6F, idleDegree * 0.5F, -2, ticks, 1.0F);
        if (head != null) {
            model.chainSwing(new AdvancedModelBox[]{ head }, idleSpeed * 0.9F, 0.08F, 0, ticks, 1.0F);
            model.bob(head, idleSpeed, 0.04F, false, ticks, 1.0F);
        }

        // tail sway
        if (tail.length > 0) {
            model.chainSwing(tail, idleSpeed, 0.14F, -2, ticks, 1.0F);
            if (limbSwingAmount > 0.12F)
                model.chainSwing(tail, 0.55F, 0.10F, -2, limbSwing, limbSwingAmount);
            entity.tailBuffer.applyChainSwingBuffer(tail);
        }

        // look-at
        model.faceTarget(rotationYaw, rotationPitch, 0.85F,
                nn(neck1, neck2, neck3, neck4, head));
    }
}
