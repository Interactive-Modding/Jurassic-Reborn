package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MussaurusEntity;

@OnlyIn(Dist.CLIENT)
public class MussaurusAnimator extends EntityAnimator<MussaurusEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, MussaurusEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelBox head = model.getCube("Head1");

        AdvancedModelBox neck1 = model.getCube("Neck1");
        AdvancedModelBox neck2 = model.getCube("Neck2");
        AdvancedModelBox neck3 = model.getCube("Neck3");

        AdvancedModelBox body1 = model.getCube("Body1");
        AdvancedModelBox body2 = model.getCube("Body2");

        AdvancedModelBox tail1 = model.getCube("Tail1");
        AdvancedModelBox tail2 = model.getCube("Tail2");
        AdvancedModelBox tail3 = model.getCube("Tail3");
        AdvancedModelBox tail4 = model.getCube("Tail4");
        AdvancedModelBox tail5 = model.getCube("Tail5");
        AdvancedModelBox tail6 = model.getCube("Tail6");
        AdvancedModelBox tail7 = model.getCube("Tail7");
        AdvancedModelBox tail8 = model.getCube("Tail8");

        AdvancedModelBox[] body = new AdvancedModelBox[] { head, neck3, neck2, neck1, body2, body1 };
        AdvancedModelBox[] tail = new AdvancedModelBox[] { tail8, tail7, tail6, tail5, tail4, tail3, tail2, tail1 };

        model.chainWave(body, 0.075F, -0.01F, -2, ticks, 1.0F);
        model.chainWave(tail, 0.075F, -0.01F, 2, ticks, 1.0F);

        model.faceTarget(rotationYaw, rotationPitch, 2.0F, neck1, neck2, neck3);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
