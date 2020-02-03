package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.HerrerasaurusEntity;

@SideOnly(Side.CLIENT)
public class HerrerasaurusAnimator extends EntityAnimator<HerrerasaurusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, HerrerasaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        float scaleFactor = 0.77F;
        float height = 2F * f1;

        AdvancedModelRenderer head = model.getCube("Head");

        AdvancedModelRenderer neck1 = model.getCube("Neck 1");
        AdvancedModelRenderer neck2 = model.getCube("Neck 2");
        AdvancedModelRenderer neck3 = model.getCube("Neck 3");
        AdvancedModelRenderer neck4 = model.getCube("Neck 4");
        AdvancedModelRenderer neck5 = model.getCube("Neck 5");
        AdvancedModelRenderer neck6 = model.getCube("Neck 6");

        AdvancedModelRenderer tail1 = model.getCube("Tail 1");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
        AdvancedModelRenderer tail6 = model.getCube("Tail 6");

        AdvancedModelRenderer upperArmLeft = model.getCube("Upper Arm LEFT");
        AdvancedModelRenderer upperArmRight = model.getCube("Upper Arm Right");

        AdvancedModelRenderer lowerArmLeft = model.getCube("Lower Arm LEFT");
        AdvancedModelRenderer lowerArmRight = model.getCube("Lower Arm Right");

        AdvancedModelRenderer handLeft = model.getCube("hand left");
        AdvancedModelRenderer handRight = model.getCube("hand right");

        AdvancedModelRenderer[] leftArmParts = new AdvancedModelRenderer[] { handLeft, lowerArmLeft, upperArmLeft };
        AdvancedModelRenderer[] rightArmParts = new AdvancedModelRenderer[] { handRight, lowerArmRight, upperArmRight };

        AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };

        AdvancedModelRenderer body1 = model.getCube("Body 1");
        AdvancedModelRenderer body2 = model.getCube("Body 2");
        AdvancedModelRenderer body3 = model.getCube("Body 3");

        AdvancedModelRenderer leftThigh = model.getCube("Left Thigh");
        AdvancedModelRenderer rightThigh = model.getCube("Right Thigh");

        AdvancedModelRenderer leftCalf1 = model.getCube("Left Calf 1");
        AdvancedModelRenderer rightCalf1 = model.getCube("Right Calf 1");

        AdvancedModelRenderer leftCalf2 = model.getCube("Left Calf 2");
        AdvancedModelRenderer rightCalf2 = model.getCube("Right Calf 2");

        AdvancedModelRenderer footLeft = model.getCube("Foot Left");
        AdvancedModelRenderer footRight = model.getCube("Foot Right");

        model.bob(body1, 1F * scaleFactor, height, false, f, f1);
        model.bob(leftThigh, 1F * scaleFactor, height, false, f, f1);
        model.bob(rightThigh, 1F * scaleFactor, height, false, f, f1);
        model.bob(neck1, 1F * scaleFactor, height / 2, false, f, f1);

        model.walk(neck1, 1F * scaleFactor, 0.25F, false, 1F, 0.1F, f, f1);
        model.walk(head, 1F * scaleFactor, 0.25F, true, 1F, -0.1F, f, f1);
        model.walk(body1, 1F * scaleFactor, 0.1F, true, 0F, 0.05F, f, f1);

        model.walk(leftThigh, 0.5F * scaleFactor, 0.8F, false, 0F, 0.4F, f, f1);
        model.walk(leftCalf1, 0.5F * scaleFactor, 0.5F, true, 1F, 0F, f, f1);
        model.walk(leftCalf2, 0.5F * scaleFactor, 0.5F, false, 0F, 0F, f, f1);
        model.walk(footLeft, 0.5F * scaleFactor, 1.5F, true, 0.5F, 1F, f, f1);

        model.walk(rightThigh, 0.5F * scaleFactor, 0.8F, true, 0F, 0.4F, f, f1);
        model.walk(rightCalf1, 0.5F * scaleFactor, 0.5F, false, 1F, 0F, f, f1);
        model.walk(rightCalf2, 0.5F * scaleFactor, 0.5F, true, 0F, 0F, f, f1);
        model.walk(footRight, 0.5F * scaleFactor, 1.5F, false, 0.5F, 1F, f, f1);

        model.chainSwing(tailParts, 0.5F * scaleFactor, -0.1F, 2, f, f1);
        model.chainWave(tailParts, 1F * scaleFactor, -0.03F, 2, f, f1);
        model.chainWave(rightArmParts, 1F * scaleFactor, -0.3F, 4, f, f1);
        model.chainWave(leftArmParts, 1F * scaleFactor, -0.3F, 4, f, f1);

        model.chainWave(tailParts, 0.1F, -0.05F, 2, ticks, 0.25F);
        model.walk(neck1, 0.1F, 0.07F, false, -1F, 0F, ticks, 0.25F);
        model.walk(head, 0.1F, 0.07F, true, 0F, 0F, ticks, 0.25F);
        model.walk(body1, 0.1F, 0.05F, false, 0F, 0F, ticks, 0.25F);
        model.chainWave(rightArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);
        model.chainWave(leftArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);
        model.chainSwing(tailParts, 0.1F, -0.1F, 3, ticks, 0.25F);

        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
