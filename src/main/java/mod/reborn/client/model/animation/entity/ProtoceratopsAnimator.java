package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.ProtoceratopsEntity;

@SideOnly(Side.CLIENT)
public class ProtoceratopsAnimator extends EntityAnimator<ProtoceratopsEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, ProtoceratopsEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        float globalSpeed = 0.5F;
        float globalHeight = 0.5F;
        float globalDegree = 0.5F;

        float frontOffset = 1.0F;

        AdvancedModelRenderer head = model.getCube("Head");

        AdvancedModelRenderer lowerJaw = model.getCube("Lower jaw");

        AdvancedModelRenderer neck1 = model.getCube("Neck #1");
        AdvancedModelRenderer neck2 = model.getCube("Neck #2");
        AdvancedModelRenderer neck3 = model.getCube("Neck #3");

        AdvancedModelRenderer body = model.getCube("Body MAIN");

        AdvancedModelRenderer tail1 = model.getCube("Tail #1");
        AdvancedModelRenderer tail2 = model.getCube("Tail #2");
        AdvancedModelRenderer tail3 = model.getCube("Tail #3");
        AdvancedModelRenderer tail4 = model.getCube("Tail #4");

        AdvancedModelRenderer armUpperRight = model.getCube("Arm Top RIGHT");
        AdvancedModelRenderer armLowerRight = model.getCube("Arm Mid RIGHT");
        AdvancedModelRenderer handRight = model.getCube("Arm Hand RIGHT");

        AdvancedModelRenderer armUpperLeft = model.getCube("Arm Top LEFT");
        AdvancedModelRenderer armLowerLeft = model.getCube("Arm Mid LEFT");
        AdvancedModelRenderer handLeft = model.getCube("Arm Hand LEFT");

        AdvancedModelRenderer thighLeft = model.getCube("Leg Top LEFT");
        AdvancedModelRenderer thighRight = model.getCube("Leg Top RIGHT");

        AdvancedModelRenderer thighLowerLeft = model.getCube("Leg Mid LEFT");
        AdvancedModelRenderer thighLowerRight = model.getCube("Leg Mid RIGHT");

        AdvancedModelRenderer footUpperLeft = model.getCube("Leg Bot LEFT");
        AdvancedModelRenderer footUpperRight = model.getCube("Leg Bot RIGHT");

        AdvancedModelRenderer footLeft = model.getCube("Leg Foot LEFT");
        AdvancedModelRenderer footRight = model.getCube("Leg Foot RIGHT");

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail4, tail3, tail2, tail1 };
        AdvancedModelRenderer[] neck = new AdvancedModelRenderer[] { head, neck3, neck2, neck1 };

        model.bob(body, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);
        model.bob(thighLeft, globalSpeed * 1.0F, globalHeight * 0.9F, false, f, f1);
        model.bob(thighRight, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);

        model.chainWave(tail, globalSpeed * 1.0F, globalHeight * 0.25F, 3, f, f1);
        model.chainSwing(tail, globalSpeed * 0.5F, globalHeight * 0.25F, 3, f, f1);
        model.chainWave(neck, globalSpeed * 1.0F, globalHeight * 0.25F, -3, f, f1);

      //  model.walk(thighLeft, 1F * globalSpeed, 0.7F * globalDegree, false, 0F, -0.4F, f, f1);
      //  model.walk(thighLowerLeft, 1F * globalSpeed, 0.5F * globalDegree, true, 1F, 0.5F, f, f1);
      //  model.walk(footUpperLeft, 1F * globalSpeed, 0.5F * globalDegree, false, -1.5F, 0.85F, f, f1);

    //   model.walk(thighRight, 1F * globalSpeed, 0.7F * globalDegree, true, 0F, -0.4F, f, f1);
   //     model.walk(thighLowerRight, 1F * globalSpeed, 0.6F * globalDegree, false, 1F, 0.5F, f, f1);
   //    model.walk(footUpperRight, 1F * globalSpeed, 0.6F * globalDegree, true, -1.5F, 0.85F, f, f1);

    //    model.walk(armUpperLeft, 1F * globalSpeed, 0.7F * globalDegree, true, frontOffset + 0F, -0.2F, f, f1);
     //   model.walk(armLowerLeft, 1F * globalSpeed, 0.5F * globalDegree, true, frontOffset + 1F, -0.1F, f, f1);
    //    model.walk(handLeft, 1F * globalSpeed, 0.6F * globalDegree, false, frontOffset + 2F, 0.8F, f, f1);

    //   model.walk(armUpperRight, 1F * globalSpeed, 0.7F * globalDegree, false, frontOffset + 0F, -0.2F, f, f1);
    //    model.walk(armLowerRight, 1F * globalSpeed, 0.5F * globalDegree, false, frontOffset + 1F, -0.1F, f, f1);
    //   model.walk(handRight, 1F * globalSpeed, 0.6F * globalDegree, true, frontOffset + 2F, 0.8F, f, f1);

        model.chainWave(tail, globalSpeed * 0.25F, globalHeight * 1.0F, 3, ticks, 0.025F);
        model.chainWave(neck, globalSpeed * 0.25F, globalHeight * 1.0F, -3, ticks, 0.025F);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
