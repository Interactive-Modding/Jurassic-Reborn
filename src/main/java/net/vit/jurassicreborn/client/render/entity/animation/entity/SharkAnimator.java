package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.animal.SharkEntity;import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SharkAnimator extends EntityAnimator<SharkEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, SharkEntity entity, float swing, float swingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelBox leftFlipper = model.getCube("LeftFin1");
        AdvancedModelBox rightFlipper = model.getCube("RightFin1");
        AdvancedModelBox tail5 = model.getCube("Tail5");
        AdvancedModelBox tail4 = model.getCube("Tail4");
        AdvancedModelBox tail3 = model.getCube("Tail3");
        AdvancedModelBox tail2 = model.getCube("Tail2");
        AdvancedModelBox tail1 = model.getCube("Tail1");
        AdvancedModelBox body1 = model.getCube("Body1");

        AdvancedModelBox[] body = new AdvancedModelBox[] { tail5, tail4, tail3, tail2, tail1, body1 };

        model.chainSwing(body, 1F, 0.3514F, -3, swing, swingAmount);

        model.walk(rightFlipper, 0.25F, 0.2F, false, 0F, 0F, ticks, 0.005F);
        model.walk(leftFlipper, 0.25F, 0.2F, false, 0F, 0F, ticks, 0.005F);
    }
}
