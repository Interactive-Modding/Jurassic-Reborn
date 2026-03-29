package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.DreadnoughtusEntity;import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DreadnoughtusAnimator extends EntityAnimator<DreadnoughtusEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, DreadnoughtusEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        AdvancedModelBox hips    = model.getCube("hips");
        AdvancedModelBox body    = model.getCube("body");     // core torso
        AdvancedModelBox stomach = model.getCube("Stomach");  // extra torso section (present in this model)
        AdvancedModelBox head    = model.getCube("Head");

        // neck chain (head first for chain helpers)
        AdvancedModelBox neck1  = model.getCube("neck1");
        AdvancedModelBox neck2  = model.getCube("neck2");
        AdvancedModelBox neck3  = model.getCube("neck3");
        AdvancedModelBox neck4  = model.getCube("neck4");
        AdvancedModelBox neck5  = model.getCube("neck5");
        AdvancedModelBox neck6  = model.getCube("neck6");
        AdvancedModelBox neck7  = model.getCube("neck7");
        AdvancedModelBox neck8  = model.getCube("neck8");
        AdvancedModelBox neck9  = model.getCube("neck9");
        AdvancedModelBox neck10 = model.getCube("neck10");
        AdvancedModelBox neck11 = model.getCube("neck11");

        AdvancedModelBox[] neck = new AdvancedModelBox[]{
                head, neck11, neck10, neck9, neck8, neck7, neck6, neck5, neck4, neck3, neck2, neck1
        };

        // tail chain (tip-first)
        AdvancedModelBox tail1 = model.getCube("tail1");
        AdvancedModelBox tail2 = model.getCube("tail2");
        AdvancedModelBox tail3 = model.getCube("tail3");
        AdvancedModelBox tail4 = model.getCube("tail4");
        AdvancedModelBox tail5 = model.getCube("tail5");
        AdvancedModelBox tail6 = model.getCube("tail6");
        AdvancedModelBox tail7 = model.getCube("tail7");
        AdvancedModelBox[] tail = new AdvancedModelBox[]{ tail7, tail6, tail5, tail4, tail3, tail2 };

        float idleSpeed  = 0.08F;
        float idleDegree = 0.08F;

        // breathing: bob hips/stomach; wave through torso
        model.bob(hips,    idleSpeed, 0.60F, false, ticks, 1.0F);
        model.bob(stomach, idleSpeed, 0.45F, false, ticks, 1.0F);
        model.chainWave(new AdvancedModelBox[]{ hips, stomach, body }, idleSpeed * 0.6F, idleDegree * 0.45F, 2, ticks, 1.0F);

        // neck undulation + gentle head motion
        model.chainWave(neck, idleSpeed * 0.6F, idleDegree * 0.65F, -2, ticks, 1.0F);
        model.chainSwing(new AdvancedModelBox[]{ head }, idleSpeed * 0.9F, 0.10F, 0, ticks, 1.0F);
        model.bob(head, idleSpeed, 0.04F, false, ticks, 1.0F);


        // tail sway + movement layer
        model.chainSwing(tail, idleSpeed, 0.18F, -2, ticks, 1.0F);
        if (limbSwingAmount > 0.12F) {
            model.chainSwing(tail, 0.55F, 0.12F, -2, limbSwing, limbSwingAmount);
        }
        entity.tailBuffer.applyChainSwingBuffer(tail);

        model.faceTarget(rotationYaw, rotationPitch, 0.9F,
                neck1, neck2, neck3, neck4, neck5, neck6, neck7, neck8, neck9, neck10, neck11, head
        );
    }
}
