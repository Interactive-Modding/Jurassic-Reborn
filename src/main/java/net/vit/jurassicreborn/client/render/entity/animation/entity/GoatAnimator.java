package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;

import net.vit.jurassicreborn.common.entities.animal.GoatEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GoatAnimator extends EntityAnimator<GoatEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, GoatEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelBox neck1 = model.getCube("Neck base");
        AdvancedModelBox neck2 = model.getCube("Throat");
        AdvancedModelBox head = model.getCube("Head lower");
        AdvancedModelBox[] neck = new AdvancedModelBox[] { head, neck2, neck1 };

        model.chainWave(neck, 0.125F, 1.0F, 3, ticks, 0.025F);
        model.faceTarget(rotationYaw, rotationPitch, 1.0F, head);
    }
}
