package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.PachycephalosaurusEntity;

@SideOnly(Side.CLIENT)
public class PachycephalosaurusAnimator extends EntityAnimator<PachycephalosaurusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, PachycephalosaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer waist = model.getCube("Body Rear");
        AdvancedModelRenderer chest = model.getCube("Body Middle");
        AdvancedModelRenderer shoulders = model.getCube("Body Front");

        AdvancedModelRenderer neck1 = model.getCube("Neck 1");
        AdvancedModelRenderer neck2 = model.getCube("Neck 2");

        AdvancedModelRenderer head = model.getCube("Head");

        AdvancedModelRenderer tail1 = model.getCube("Tail 1");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
        AdvancedModelRenderer tail6 = model.getCube("Tail 6");
        AdvancedModelRenderer tail7 = model.getCube("Tail 7");

        AdvancedModelRenderer upperLegLeft = model.getCube("Upper Leg Left");
        AdvancedModelRenderer upperLegRight = model.getCube("Upper Leg Right");

        AdvancedModelRenderer lowerLegLeft = model.getCube("Middle Leg Left");
        AdvancedModelRenderer lowerLegRight = model.getCube("Middle Leg Right");

        AdvancedModelRenderer upperFootLeft = model.getCube("Lower Leg Left");
        AdvancedModelRenderer upperFootRight = model.getCube("Lower Leg Right");

        AdvancedModelRenderer footLeft = model.getCube("Foot Left");
        AdvancedModelRenderer footRight = model.getCube("Foot Right");

        AdvancedModelRenderer upperArmLeft = model.getCube("Upper Arm Left");
        AdvancedModelRenderer upperArmRight = model.getCube("Upper Arm Right");

        AdvancedModelRenderer lowerArmLeft = model.getCube("Lower Arm Left");
        AdvancedModelRenderer lowerArmRight = model.getCube("Lower Arm Right");

        AdvancedModelRenderer handLeft = model.getCube("Hand Left");
        AdvancedModelRenderer handRight = model.getCube("Hand Right");

        AdvancedModelRenderer[] rightArmParts = new AdvancedModelRenderer[] { handRight, lowerArmRight, upperArmRight };
        AdvancedModelRenderer[] leftArmParts = new AdvancedModelRenderer[] { handLeft, lowerArmLeft, upperArmLeft };
        AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[] { tail7, tail6, tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelRenderer[] bodyParts = new AdvancedModelRenderer[] { waist, chest, shoulders, neck2, neck1, head };

        float globalSpeed = 0.8F;
        float globalDegree = 0.5F;
        float globalHeight = 1.0F;

        model.bob(waist, globalSpeed * 0.8F, globalHeight * 1.0F, false, f, f1);
        model.bob(upperLegLeft, globalSpeed * 0.8F, globalHeight * 1.0F, false, f, f1);
        model.bob(upperLegRight, globalSpeed * 0.8F, globalHeight * 1.0F, false, f, f1);

        model.walk(upperLegLeft, 0.4F * globalSpeed, 0.8F * globalDegree, false, 0F, 0.2F, f, f1);
        model.walk(lowerLegLeft, 0.4F * globalSpeed, 1F * globalDegree, true, 1F, 0.4F, f, f1);
        model.walk(upperFootLeft, 0.4F * globalSpeed, 1F * globalDegree, false, 0F, 0F, f, f1);
        model.walk(footLeft, 0.4F * globalSpeed, 1.5F * globalDegree, true, 0.5F, 0.1F, f, f1);

        model.walk(upperLegRight, 0.4F * globalSpeed, 0.8F * globalDegree, true, 0F, 0.2F, f, f1);
        model.walk(lowerLegRight, 0.4F * globalSpeed, 1F * globalDegree, false, 1F, 0.4F, f, f1);
        model.walk(upperFootRight, 0.4F * globalSpeed, 1F * globalDegree, true, 0F, 0F, f, f1);
        model.walk(footRight, 0.4F * globalSpeed, 1.5F * globalDegree, false, 0.5F, 0.1F, f, f1);

        model.chainWave(tailParts, globalSpeed * 1.0F, globalHeight * 0.05F, 2, f, f1);
   //     model.chainWave(bodyParts, globalSpeed * 1.0F, globalHeight * 0.025F, 1, f, f1);

        model.chainWave(rightArmParts, 1F * globalSpeed, -0.3F, 4, f, f1);
        model.chainWave(leftArmParts, 1F * globalSpeed, -0.3F, 4, f, f1);

        waist.rotateAngleX += f1 * 0.075F;
        shoulders.rotateAngleX += f1 * 0.05F;
        neck1.rotateAngleX += f1 * 0.05F;
        neck2.rotateAngleX += f1 * 0.05F;
        head.rotateAngleX += f1 * 0.075F;

        model.chainWave(tailParts, 0.1F, 0.025F, 2, ticks, 0.25F);
    //    model.chainWave(bodyParts, 0.1F, -0.03F, 4, ticks, 0.25F);
        model.chainWave(rightArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);
        model.chainWave(leftArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);

        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
