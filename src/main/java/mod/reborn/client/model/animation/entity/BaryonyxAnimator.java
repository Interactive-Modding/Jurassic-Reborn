package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.BaryonyxEntity;

@SideOnly(Side.CLIENT)
public class BaryonyxAnimator extends EntityAnimator<BaryonyxEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, BaryonyxEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        float scaleFactor = 0.62F;
        float height = 2F * f1;

        AdvancedModelRenderer thighRight = model.getCube("Right Thigh");
        AdvancedModelRenderer thighLeft = model.getCube("Left Thigh");

        AdvancedModelRenderer rightCalf1 = model.getCube("Right Calf 1");
        AdvancedModelRenderer rightCalf2 = model.getCube("Right Calf 2");
        AdvancedModelRenderer leftCalf1 = model.getCube("Left Calf 1");
        AdvancedModelRenderer leftCalf2 = model.getCube("Left Calf 2");

        AdvancedModelRenderer rightFoot = model.getCube("Foot Right");
        AdvancedModelRenderer leftFoot = model.getCube("Foot Left");

        AdvancedModelRenderer upperArmRight = model.getCube("Upper Arm Right");
        AdvancedModelRenderer upperArmLeft = model.getCube("Upper Arm LEFT");

        AdvancedModelRenderer lowerArmRight = model.getCube("Lower Arm Right");
        AdvancedModelRenderer lowerArmLeft = model.getCube("Lower Arm LEFT");

        AdvancedModelRenderer handRight = model.getCube("hand right");
        AdvancedModelRenderer handLeft = model.getCube("hand left");

        AdvancedModelRenderer waist = model.getCube("Body 1");
        AdvancedModelRenderer stomach = model.getCube("Body 2");
        AdvancedModelRenderer shoulders = model.getCube("Body 3");

        AdvancedModelRenderer neck1 = model.getCube("Neck 1");
        AdvancedModelRenderer neck2 = model.getCube("Neck 2");
        AdvancedModelRenderer neck3 = model.getCube("Neck 3");
        AdvancedModelRenderer neck4 = model.getCube("Neck 4");
        AdvancedModelRenderer neck5 = model.getCube("Neck 5");

        AdvancedModelRenderer tail1 = model.getCube("Tail 1");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
        AdvancedModelRenderer tail6 = model.getCube("Tail 6");

        AdvancedModelRenderer head = model.getCube("Head");

        AdvancedModelRenderer[] leftArmParts = new AdvancedModelRenderer[] { handLeft, lowerArmLeft, upperArmLeft };
        AdvancedModelRenderer[] rightArmParts = new AdvancedModelRenderer[] { handRight, lowerArmRight, upperArmRight };

        AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };

        model.bob(waist, 1F * scaleFactor, height, false, f, f1);
        model.bob(thighLeft, 1F * scaleFactor, height, false, f, f1);
        model.bob(thighRight, 1F * scaleFactor, height, false, f, f1);
        model.bob(neck1, 1F * scaleFactor, height / 2, false, f, f1);


        model.chainSwing(tailParts, 0.5F * scaleFactor, -0.1F, 2, f, f1);
        model.chainWave(tailParts, 1F * scaleFactor, -0.03F, 2, f, f1);
        model.chainWave(rightArmParts, 1F * scaleFactor, -0.3F, 4, f, f1);
        model.chainWave(leftArmParts, 1F * scaleFactor, -0.3F, 4, f, f1);

        model.chainWave(tailParts, 0.1F, -0.05F, 2, ticks, 0.25F);
        model.chainWave(rightArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);
        model.chainWave(leftArmParts, 0.1F, -0.1F, 4, ticks, 0.25F);
        model.chainSwing(tailParts, 0.1F, -0.1F, 3, ticks, 0.25F); //

        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
