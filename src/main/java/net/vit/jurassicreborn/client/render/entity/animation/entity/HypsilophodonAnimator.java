package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.HypsilophodonEntity;

@OnlyIn(Dist.CLIENT)
public class HypsilophodonAnimator extends EntityAnimator<HypsilophodonEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, HypsilophodonEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        float scaleFactor = 1F;
        float height = 12F * f1;

        AdvancedModelBox upperlegright = model.getCube("Leg UPPER RIGHT");
        AdvancedModelBox upperlegleft = model.getCube("Leg UPPER LEFT");

        AdvancedModelBox midlegright = model.getCube("Leg MIDDLE RIGHT");
        AdvancedModelBox midlegleft = model.getCube("Leg MIDDLE LEFT");

        AdvancedModelBox lowerlegright = model.getCube("Leg LOWER RIGHT");
        AdvancedModelBox lowerlegleft = model.getCube("Leg LOWER LEFT");

        AdvancedModelBox feetright = model.getCube("Foot RIGHT");
        AdvancedModelBox feetleft = model.getCube("Foot LEFT");

        AdvancedModelBox head = model.getCube("Head ");
        AdvancedModelBox neck = model.getCube("Neck BASE");
        AdvancedModelBox neck2 = model.getCube("Neck 2");
        AdvancedModelBox neck3 = model.getCube("Neck 3");

        AdvancedModelBox body2 = model.getCube("Body REAR");
        AdvancedModelBox body1 = model.getCube("Body FRONT");

        AdvancedModelBox tail1 = model.getCube("Tail BASE");
        AdvancedModelBox tail2 = model.getCube("Tail 2");
        AdvancedModelBox tail3 = model.getCube("Tail 3");
        AdvancedModelBox tail4 = model.getCube("Tail 4");
        AdvancedModelBox tail5 = model.getCube("Tail 5");
        AdvancedModelBox tail6 = model.getCube("Tail 6");

        AdvancedModelBox shoulderright = model.getCube("Arm UPPER RIGHT");
        AdvancedModelBox shoulderleft = model.getCube("Arm UPPER LEFT");

        AdvancedModelBox armright = model.getCube("Arm MIDDLE RIGHT");
        AdvancedModelBox armleft = model.getCube("Arm MIDDLE LEFT");

        AdvancedModelBox[] tailParts = new AdvancedModelBox[] { tail6, tail5, tail4, tail3, tail2, tail1 };

      //  model.bob(body2, 0.5F * scaleFactor, height, true, f, f1);
     //   model.bob(upperlegright, 0.5F * scaleFactor, height, true, f, f1);
      //  model.bob(upperlegleft, 0.5F * scaleFactor, height, true, f, f1);

     //   model.walk(upperlegleft, 1F * scaleFactor, 0.75F, true, 1F, 0.25F, f, f1);
     //   model.walk(upperlegright, 1F * scaleFactor, 0.75F, true, 0.5F, 0.25F, f, f1);
      //  model.walk(midlegleft, 1F * scaleFactor, 0.75F, false, 1.5F, 0F, f, f1);
      //  model.walk(midlegright, 1F * scaleFactor, 0.75F, false, 1F, 0F, f, f1);
      //  model.walk(lowerlegright, 1F * scaleFactor, 0.75F, true, 0.5F, 0F, f, f1);
     //   model.walk(lowerlegleft, 1F * scaleFactor, 0.75F, true, 1F, 0F, f, f1);
     //   model.walk(feetleft, 1F * scaleFactor, 0.5F, true, 1F, 0.75F, f, f1);
      //  model.walk(feetright, 1F * scaleFactor, 0.5F, true, 0.5F, 0.75F, f, f1);

     //   model.walk(body2, 1F * scaleFactor, 0.3F, false, 0.5F, 0F, f, f1);
     //   model.walk(body1, 1F * scaleFactor, 0.5F, true, 1.0F, 0F, f, f1);
     //   model.walk(neck, 1F * scaleFactor, 0.3F, true, 0.25F, 0.3F, f, f1);
      //  model.walk(head, 1F * scaleFactor, 0.3F, false, 0.25F, -0.3F, f, f1);

      //  model.walk(shoulderright, 1 * scaleFactor, 0.3F, true, 1, 0.2F, f, f1);
   //     model.walk(shoulderleft, 1 * scaleFactor, 0.3F, true, 1, 0.2F, f, f1);
     //   model.walk(armright, 1 * scaleFactor, 0.3F, false, 1, -0.2F, f, f1);
     //   model.walk(armleft, 1 * scaleFactor, 0.3F, false, 1, -0.2F, f, f1);

    //    model.chainWave(tailParts, 0.2F, -0.05F, 2, ticks, 0.25F);
    //    model.walk(neck, 0.2F, 0.1F, false, -1F, 0F, ticks, 0.25F);
    //    model.walk(head, 0.2F, 0.1F, true, 0F, 0F, ticks, 0.25F);
    //    model.walk(body1, 0.2F, 0.1F, true, 0F, 0F, ticks, 0.25F);
     //   model.walk(body2, 0.2F, 0.1F, false, 0F, 0F, ticks, 0.25F);
    //    model.walk(shoulderright, 0.2F, 0.1F, true, 0F, 0F, ticks, 0.25F);
    //    model.walk(shoulderleft, 0.2F, 0.1F, true, 0F, 0F, ticks, 0.25F);
    //    model.walk(armright, 0.2F, 0.1F, false, 0F, 0F, ticks, 0.25F);
   //     model.walk(armleft, 0.2F, 0.1F, false, 0F, 0F, ticks, 0.25F);

    //    model.chainWave(tailParts, 1F * scaleFactor, 0.15F, 2, f, f1);

        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
