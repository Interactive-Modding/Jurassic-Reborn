package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.DiplodocusEntity;import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DiplodocusAnimator extends EntityAnimator<DiplodocusEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, DiplodocusEntity entity,
                                     float limbSwing, float limbSwingAmount, float ticks,
                                     float rotationYaw, float rotationPitch, float scale) {

        AdvancedModelBox hips = model.getCube("hips");
        AdvancedModelBox body = model.getCube("body");
        AdvancedModelBox head = model.getCube("Head");

        // neck chain (head first for chain helpers)
        AdvancedModelBox neck1 = model.getCube("neck1");
        AdvancedModelBox neck2 = model.getCube("neck2");
        AdvancedModelBox neck3 = model.getCube("neck3");
        AdvancedModelBox neck4 = model.getCube("neck4");
        AdvancedModelBox neck5 = model.getCube("neck5");
        AdvancedModelBox neck6 = model.getCube("neck6");
        AdvancedModelBox neck7 = model.getCube("neck7");
        AdvancedModelBox neck8 = model.getCube("neck8");
        AdvancedModelBox neck9 = model.getCube("neck9");
        AdvancedModelBox neck10 = model.getCube("neck10");

        AdvancedModelBox[] neck = new AdvancedModelBox[]{
                head, neck10, neck9, neck8, neck7, neck6, neck5, neck4, neck3, neck2, neck1
        };

        // Tail segments in this export aren’t named plainly (lots of backcubes);

        AdvancedModelBox tail1 = model.getCube("tail1");
        AdvancedModelBox tail2 = model.getCube("tail2");
        AdvancedModelBox tail3 = model.getCube("tail3");
        AdvancedModelBox tail4 = model.getCube("tail4");
        AdvancedModelBox tail5 = model.getCube("tail5");
        AdvancedModelBox[] tail = new AdvancedModelBox[]{ tail5, tail4, tail3, tail2 };

        float idleSpeed  = 0.08F;
        float idleDegree = 0.08F;

        // breathing on hips/body
        model.bob(hips, idleSpeed, 0.60F, false, ticks, 1.0F);
        model.chainWave(new AdvancedModelBox[]{ hips, body }, idleSpeed * 0.6F, idleDegree * 0.4F, 2, ticks, 1.0F);

        // neck undulation and head subtle sway/bob
        model.chainWave(neck, idleSpeed * 0.6F, idleDegree * 0.6F, -2, ticks, 1.0F);

        model.chainSwing(new AdvancedModelBox[]{ head }, idleSpeed * 0.9F, 0.10F, 0, ticks, 1.0F);
        model.bob(head, idleSpeed, 0.04F, false, ticks, 1.0F);


        // tail sway (if tail cubes are present)

            model.chainSwing(tail, idleSpeed, 0.17F, -2, ticks, 1.0F);
            if (limbSwingAmount > 0.12F) {
                model.chainSwing(tail, 0.55F, 0.12F, -2, limbSwing, limbSwingAmount);
            }
            entity.tailBuffer.applyChainSwingBuffer(tail);


        model.faceTarget(rotationYaw, rotationPitch, 0.9F, neck1, neck2, neck3, neck4, neck5, neck6, neck7, neck8, neck9, neck10, head
        );
    }
}
