package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.animal.CrabEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CrabAnimator extends EntityAnimator<CrabEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, CrabEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelBox head = model.getCube("bodyTop");
        AdvancedModelBox[] neck = new AdvancedModelBox[]{head};
        model.chainWave(neck, 0.125F, 1.0F, 3, ticks, 0.025F);

        AdvancedModelBox thigh1Left = model.getCube("thigh1Left");
        AdvancedModelBox thigh2Left = model.getCube("thigh2Left");
        AdvancedModelBox thigh3Left = model.getCube("thigh3Left");
        AdvancedModelBox thigh4Left = model.getCube("thigh4Left");
        AdvancedModelBox thigh1Right = model.getCube("thigh1Right");
        AdvancedModelBox thigh2Right = model.getCube("thigh2Right");
        AdvancedModelBox thigh3Right = model.getCube("thigh3Right");
        AdvancedModelBox thigh4Right = model.getCube("thigh4Right");

        AdvancedModelBox[] leftLegs = new AdvancedModelBox[]{thigh1Left, thigh2Left, thigh3Left, thigh4Left};
        AdvancedModelBox[] rightLegs = new AdvancedModelBox[]{thigh1Right, thigh2Right, thigh3Right, thigh4Right};

        float speed = 1.0F;
        float degree = 0.6F;

        for (int i = 0; i < leftLegs.length; i++) {
            model.walk(leftLegs[i], speed, degree, true, i * 0.5F, 0.0F, limbSwing, limbSwingAmount);
            model.walk(rightLegs[i], speed, degree, false, i * 0.5F, 0.0F, limbSwing, limbSwingAmount);
        }
    }
}
