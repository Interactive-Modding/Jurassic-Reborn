package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.CompsognathusEntity;

@SideOnly(Side.CLIENT)
public class CompsognathusAnimator extends EntityAnimator<CompsognathusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, CompsognathusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer abdomen = model.getCube("abdomen");
        AdvancedModelRenderer upperBody = model.getCube("Upper body");

        AdvancedModelRenderer head = model.getCube("Head");

        AdvancedModelRenderer neck1 = model.getCube("Neck 1");
        AdvancedModelRenderer neck2 = model.getCube("Neck 2");
        AdvancedModelRenderer neck3 = model.getCube("Neck 3");
        AdvancedModelRenderer neck4 = model.getCube("Neck 4");
        AdvancedModelRenderer neck5 = model.getCube("Neck 5");
        AdvancedModelRenderer neck6 = model.getCube("Neck 6");
        AdvancedModelRenderer neck7 = model.getCube("Neck 7");

        AdvancedModelRenderer lowerJaw = model.getCube("Lower Jaw");

        AdvancedModelRenderer leftThigh = model.getCube("Left thigh");
        AdvancedModelRenderer leftMidLeg = model.getCube("Left mid leg");
        AdvancedModelRenderer leftShin = model.getCube("Left shin");
        AdvancedModelRenderer leftFoot = model.getCube("Left foot");

        AdvancedModelRenderer rightThigh = model.getCube("Right thigh");
        AdvancedModelRenderer rightMidLeg = model.getCube("Right mid leg");
        AdvancedModelRenderer rightShin = model.getCube("Right shin");
        AdvancedModelRenderer rightFoot = model.getCube("Right foot");

        AdvancedModelRenderer tail1 = model.getCube("Tail 1");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");

        AdvancedModelRenderer leftArm = model.getCube("Left arm");
        AdvancedModelRenderer leftForeArm = model.getCube("Left forearm");
        AdvancedModelRenderer leftHand = model.getCube("Left hand");

        AdvancedModelRenderer rightArm = model.getCube("Right arm");
        AdvancedModelRenderer rightForeArm = model.getCube("Right forearm");
        AdvancedModelRenderer rightHand = model.getCube("Right hand");

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelRenderer[] neck = new AdvancedModelRenderer[] { head, neck7, neck6, neck5, neck4, neck3, neck2, neck1, upperBody };

        // f = ticks;
        // f1 = 0.4F;

        float globalSpeed = 1.8F;
        float globalDegree = 1.5F;
        float globalHeight = 0.45F;

        model.bob(abdomen, globalSpeed * 0.5F, globalHeight * 1.0F, false, f, f1);
        model.bob(leftThigh, globalSpeed * 0.5F, globalHeight * 1.0F, false, f, f1);
        model.bob(rightThigh, globalSpeed * 0.5F, globalHeight * 1.0F, false, f, f1);
        model.chainWave(tail, globalSpeed * 0.5F, globalHeight * 0.125F, 2, f, f1);
        model.chainWave(neck, globalSpeed * 0.5F, globalHeight * 0.0625F, -2, f, f1);

        model.walk(leftThigh, 0.5F * globalSpeed, globalDegree * 0.7F, false, 3.14F, 0.2F, f, f1);
        model.walk(leftMidLeg, 0.5F * globalSpeed, globalDegree * 0.6F, false, 1.5F, 0.3F, f, f1);
        model.walk(leftShin, 0.5F * globalSpeed, globalDegree * 0.8F, false, -1F, -0.1F, f, f1);
        model.walk(leftFoot, 0.5F * globalSpeed, globalDegree * 1.5F, true, -1F, 1F, f, f1);

        model.walk(rightThigh, 0.5F * globalSpeed, globalDegree * 0.7F, true, 3.14F, 0.2F, f, f1);
        model.walk(rightMidLeg, 0.5F * globalSpeed, globalDegree * 0.6F, true, 1.5F, 0.3F, f, f1);
        model.walk(rightShin, 0.5F * globalSpeed, globalDegree * 0.8F, true, -1F, -0.1F, f, f1);
        model.walk(rightFoot, 0.5F * globalSpeed, globalDegree * 1.5F, false, -1F, 1F, f, f1);

        model.chainWave(tail, 0.125F * globalSpeed, globalHeight * 0.125F, 2, ticks, 0.0625F);
        model.chainWave(neck, 0.125F * globalSpeed, globalHeight * 0.125F, -2, ticks, 0.0625F);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
