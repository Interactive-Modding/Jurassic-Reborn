package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.HypsilophodonEntity;

@SideOnly(Side.CLIENT)
public class HypsilophodonAnimator extends EntityAnimator<HypsilophodonEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, HypsilophodonEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        float scaleFactor = 1F;
        float height = 12F * f1;

        AdvancedModelRenderer upperlegright = model.getCube("Leg UPPER RIGHT");
        AdvancedModelRenderer upperlegleft = model.getCube("Leg UPPER LEFT");

        AdvancedModelRenderer midlegright = model.getCube("Leg MIDDLE RIGHT");
        AdvancedModelRenderer midlegleft = model.getCube("Leg MIDDLE LEFT");

        AdvancedModelRenderer lowerlegright = model.getCube("Leg LOWER RIGHT");
        AdvancedModelRenderer lowerlegleft = model.getCube("Leg LOWER LEFT");

        AdvancedModelRenderer feetright = model.getCube("Foot RIGHT");
        AdvancedModelRenderer feetleft = model.getCube("Foot LEFT");

        AdvancedModelRenderer head = model.getCube("Head ");
        AdvancedModelRenderer neck = model.getCube("Neck BASE");
        AdvancedModelRenderer neck2 = model.getCube("Neck 2");
        AdvancedModelRenderer neck3 = model.getCube("Neck 3");

        AdvancedModelRenderer body2 = model.getCube("Body REAR");
        AdvancedModelRenderer body1 = model.getCube("Body FRONT");

        AdvancedModelRenderer tail1 = model.getCube("Tail BASE");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
        AdvancedModelRenderer tail6 = model.getCube("Tail 6");

        AdvancedModelRenderer shoulderright = model.getCube("Arm UPPER RIGHT");
        AdvancedModelRenderer shoulderleft = model.getCube("Arm UPPER LEFT");

        AdvancedModelRenderer armright = model.getCube("Arm MIDDLE RIGHT");
        AdvancedModelRenderer armleft = model.getCube("Arm MIDDLE LEFT");

        AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };

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
