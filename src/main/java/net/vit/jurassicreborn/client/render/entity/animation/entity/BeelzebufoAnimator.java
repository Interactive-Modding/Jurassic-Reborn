package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.BeelzebufoEntity;

public class BeelzebufoAnimator extends EntityAnimator<BeelzebufoEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, BeelzebufoEntity entity,
                                     float limbSwing, float limbSwingAmount,
                                     float ticks, float rotationYaw, float rotationPitch, float scale) {

        AdvancedModelBox body = model.getCube("Body MAIN");
        AdvancedModelBox head = model.getCube("Main Head");


        model.bob(body, 0.3F, 0.2F, false, limbSwing, limbSwingAmount);

        model.chainWave(new AdvancedModelBox[]{head}, 0.1F, 0.05F, 0, ticks, 1.0F);
    }
}
