package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.VectipeltaEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class VectipeltaAnimator extends EntityAnimator<VectipeltaEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, VectipeltaEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
    {
        float globalSpeed = 0.6F;
        float globalDegree = 1.0F;
        float globalHeight = 1.0F;


        AdvancedModelBox body1 = model.getCube("RibsMAIN");
        AdvancedModelBox[] body = new AdvancedModelBox[] { body1 };

        model.bob(body1,  globalSpeed * 0.5F,  globalDegree * 0.01F, false, f, f1);
        model.chainWave(body, globalSpeed * 0.25F, globalDegree * 0.025F, 3, f, f1);
        model.chainWave(body, 0.1F, -0.05F, 4, ticks, 0.25F);

    }
}
}