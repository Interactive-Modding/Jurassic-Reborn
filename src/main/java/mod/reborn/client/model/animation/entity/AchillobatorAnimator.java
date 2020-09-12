package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.AchillobatorEntity;

@SideOnly(Side.CLIENT)
public class AchillobatorAnimator extends EntityAnimator<AchillobatorEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, AchillobatorEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        float speed = 0.75F;
        float height = 2F * f1;

        AdvancedModelRenderer waist = model.getCube("body3");
        AdvancedModelRenderer chest = model.getCube("body2");
        AdvancedModelRenderer shoulders = model.getCube("body1");
        AdvancedModelRenderer leftThigh = model.getCube("Left thigh");
        AdvancedModelRenderer rightThigh = model.getCube("Right thigh");
        AdvancedModelRenderer neck1 = model.getCube("neck1");
        AdvancedModelRenderer neck2 = model.getCube("neck2");
        AdvancedModelRenderer neck3 = model.getCube("neck3");
        AdvancedModelRenderer neck4 = model.getCube("neck4");
        AdvancedModelRenderer head = model.getCube("head");
        AdvancedModelRenderer leftShin = model.getCube("Left shin");
        AdvancedModelRenderer rightShin = model.getCube("Right shin");
        AdvancedModelRenderer leftUpperFoot = model.getCube("Left upper foot");
        AdvancedModelRenderer leftFoot = model.getCube("Left foot");
        AdvancedModelRenderer rightUpperFoot = model.getCube("Right upper foot");
        AdvancedModelRenderer rightFoot = model.getCube("Right foot");
        AdvancedModelRenderer upperArmRight = model.getCube("Right arm");
        AdvancedModelRenderer upperArmLeft = model.getCube("Left arm");
        AdvancedModelRenderer tail1 = model.getCube("tail1");
        AdvancedModelRenderer tail2 = model.getCube("tail2");
        AdvancedModelRenderer tail3 = model.getCube("tail3");
        AdvancedModelRenderer tail4 = model.getCube("tail4");
        AdvancedModelRenderer tail5 = model.getCube("tail5");
        AdvancedModelRenderer tail6 = model.getCube("tail6");


        AdvancedModelRenderer Lower_Arm_Right = model.getCube("Right arm");
        AdvancedModelRenderer Lower_Arm_Left = model.getCube("Left arm");
        AdvancedModelRenderer Hand_Right = model.getCube("Right hand");
        AdvancedModelRenderer Hand_Left = model.getCube("Left hand");

        AdvancedModelRenderer[] rightArmParts = new AdvancedModelRenderer[] { Hand_Right, Lower_Arm_Right, upperArmRight };
        AdvancedModelRenderer[] leftArmParts = new AdvancedModelRenderer[] { Hand_Left, Lower_Arm_Left, upperArmLeft };
        AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelRenderer[] bodyParts = new AdvancedModelRenderer[] { waist, chest, shoulders, neck4, neck3, neck2, neck1, head };

      //  model.bob(waist, 1F * speed, height, false, f, f1);
    //    model.bob(leftThigh, 1F * speed, height, false, f, f1);
     //   model.bob(rightThigh, 1F * speed, height, false, f, f1);
    //    model.walk(shoulders, 1F * speed, 0.2F, true, 1, 0, f, f1);
     //   model.walk(chest, 1F * speed, 0.2F, false, 0.5F, 0, f, f1);

     //   model.walk(leftThigh, 0.5F * speed, 0.6F, false, 3.14F, 0.2F, f, f1);
      //  model.walk(leftShin, 0.5F * speed, 0.5F, false, 1.5F, 0.3F, f, f1);
     //   model.walk(leftUpperFoot, 0.5F * speed, 0.7F, false, -1F, -0.1F, f, f1);
        model.walk(leftFoot, 0.5F * speed, 1.3F, true, -0.9F, 1F, f, f1);

     //   model.walk(rightThigh, 0.5F * speed, 0.6F, true, 3.14F, 0.2F, f, f1);
     //   model.walk(rightShin, 0.5F * speed, 0.5F, true, 1.5F, 0.3F, f, f1);
     //   model.walk(rightUpperFoot, 0.5F * speed, 0.7F, true, -1F, -0.1F, f, f1);
        model.walk(rightFoot, 0.5F * speed, 1.3F, false, -0.9F, 1F, f, f1);

      //  shoulders.rotationPointY -= 0.5 * f1;
     //   shoulders.rotationPointZ -= 0.5 * f1;
      //  shoulders.rotateAngleX += 0.5 * f1;
      //  chest.rotateAngleX += 0.1 * f1;
      //  neck1.rotateAngleX += 0.1 * f1;
      //  neck2.rotateAngleX += 0.1 * f1;
      //  neck3.rotateAngleX -= 0.3 * f1;
     //   neck4.rotateAngleX -= 0.2 * f1;
      //  head.rotateAngleX -= 0.3 * f1;

        model.chainSwing(tailParts, 0.5F * speed, -0.1F, 2, f, f1);
        model.chainWave(tailParts, 1F * speed, -0.1F, 2.5F, f, f1);
      //  model.chainWave(bodyParts, 1F * speed, -0.1F, 4, f, f1);

        model.chainWave(rightArmParts, 1F * speed, -0.3F, 4, f, f1);
        model.chainWave(leftArmParts, 1F * speed, -0.3F, 4, f, f1);

        // Idling
        model.chainWave(tailParts, 0.1F, 0.05F, 2, ticks, 0.25F);
       // model.chainWave(bodyParts, 0.1F, -0.03F, 5, ticks, 0.25F);
        model.chainWave(rightArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);
        model.chainWave(leftArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);

        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
