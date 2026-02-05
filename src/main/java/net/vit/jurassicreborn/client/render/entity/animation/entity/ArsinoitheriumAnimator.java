package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.ArsinoitheriumEntity;

public class ArsinoitheriumAnimator extends EntityAnimator<ArsinoitheriumEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, ArsinoitheriumEntity entity,
                                     float limbSwing, float limbSwingAmount,
                                     float ticks, float rotationYaw, float rotationPitch, float scale) {

        AdvancedModelBox head = model.getCube("Head");
        AdvancedModelBox bodymiddle = model.getCube("Body middle");
        AdvancedModelBox neck = model.getCube("Neck 1");
        AdvancedModelBox tailbase = model.getCube("tail_base");
        model.chainSwing(new AdvancedModelBox[]{tailbase}, 0.1F, 0.15F, 0, ticks, 1.0F);
        model.bob(head, 0.1F, 0.05F, false, ticks, 1.0F);
        model.chainWave(new AdvancedModelBox[]{bodymiddle,neck, head}, 0.05F, 0.03F, -2, ticks, 1.0F);
    }
}
