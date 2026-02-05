package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.ChilesaurusEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChilesaurusAnimator extends EntityAnimator<ChilesaurusEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, ChilesaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
        {
            AdvancedModelBox neck1 = model.getCube("Neck BASE");
            AdvancedModelBox neck2 = model.getCube("Neck 2");
            AdvancedModelBox neck3 = model.getCube("Neck 3");

            AdvancedModelBox body1 = model.getCube("Body REAR");
            AdvancedModelBox body2 = model.getCube("Body MIDDLE");
            AdvancedModelBox body3 = model.getCube("Body FRONT");

            AdvancedModelBox head = model.getCube("Head ");

            AdvancedModelBox tail1 = model.getCube("Tail BASE");
            AdvancedModelBox tail2 = model.getCube("Tail 2");
            AdvancedModelBox tail3 = model.getCube("Tail 3");
            AdvancedModelBox tail4 = model.getCube("Tail 4");
            AdvancedModelBox tail5 = model.getCube("Tail 5");
            AdvancedModelBox tail6 = model.getCube("Tail 6");

            AdvancedModelBox[] body = new AdvancedModelBox[]{head, neck3, neck2, neck1, body1, body2, body3};

            AdvancedModelBox[] tail = new AdvancedModelBox[]{tail6, tail5, tail4, tail3, tail2, tail1};

            float globalSpeed = 1.5F;
            float globalDegree = 1.0F;


            model.bob(body1, globalSpeed * 0.25F, globalDegree * 1.5F, false, f, f1);

            model.chainWave(tail, globalSpeed * 0.25F, globalDegree * 0.1F, 1, f, f1);
            model.chainSwing(tail, globalSpeed * 0.25F, globalDegree * 0.4F, 2, f, f1);
            model.chainWave(body, globalSpeed * 0.25F, globalDegree * 0.025F, 3, f, f1);

            model.chainWave(tail, 0.1F, 0.05F, 1, ticks, 0.25F);
            model.chainWave(body, 0.1F, -0.05F, 4, ticks, 0.25F);
            model.faceTarget(rotationYaw, rotationPitch, 1.0F, neck1, neck2, neck3, head);
        }
    }
}