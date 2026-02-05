package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.PachycephalosaurusEntity;

@OnlyIn(Dist.CLIENT)
public class PachycephalosaurusAnimator extends EntityAnimator<PachycephalosaurusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, PachycephalosaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelBox waist = model.getCube("Body Rear");
        AdvancedModelBox chest = model.getCube("Body Middle");
        AdvancedModelBox shoulders = model.getCube("Body Front");

        AdvancedModelBox neck1 = model.getCube("Neck 1");
        AdvancedModelBox neck2 = model.getCube("Neck 2");

        AdvancedModelBox head = model.getCube("Head");

        AdvancedModelBox tail1 = model.getCube("Tail 1");
        AdvancedModelBox tail2 = model.getCube("Tail 2");
        AdvancedModelBox tail3 = model.getCube("Tail 3");
        AdvancedModelBox tail4 = model.getCube("Tail 4");
        AdvancedModelBox tail5 = model.getCube("Tail 5");
        AdvancedModelBox tail6 = model.getCube("Tail 6");
        AdvancedModelBox tail7 = model.getCube("Tail 7");

        AdvancedModelBox upperLegLeft = model.getCube("Upper Leg Left");
        AdvancedModelBox upperLegRight = model.getCube("Upper Leg Right");

        AdvancedModelBox lowerLegLeft = model.getCube("Middle Leg Left");
        AdvancedModelBox lowerLegRight = model.getCube("Middle Leg Right");

        AdvancedModelBox upperFootLeft = model.getCube("Lower Leg Left");
        AdvancedModelBox upperFootRight = model.getCube("Lower Leg Right");

        AdvancedModelBox footLeft = model.getCube("Foot Left");
        AdvancedModelBox footRight = model.getCube("Foot Right");

        AdvancedModelBox upperArmLeft = model.getCube("Upper Arm Left");
        AdvancedModelBox upperArmRight = model.getCube("Upper Arm Right");

        AdvancedModelBox lowerArmLeft = model.getCube("Lower Arm Left");
        AdvancedModelBox lowerArmRight = model.getCube("Lower Arm Right");

        AdvancedModelBox handLeft = model.getCube("Hand Left");
        AdvancedModelBox handRight = model.getCube("Hand Right");

        AdvancedModelBox[] rightArmParts = new AdvancedModelBox[] { handRight, lowerArmRight, upperArmRight };
        AdvancedModelBox[] leftArmParts = new AdvancedModelBox[] { handLeft, lowerArmLeft, upperArmLeft };
        AdvancedModelBox[] tailParts = new AdvancedModelBox[] { tail7, tail6, tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelBox[] bodyParts = new AdvancedModelBox[] { waist, chest, shoulders, neck2, neck1, head };

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
