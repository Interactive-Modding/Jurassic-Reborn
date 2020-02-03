package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.CorythosaurusEntity;

@SideOnly(Side.CLIENT)
public class CorythosaurusAnimator extends EntityAnimator<CorythosaurusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, CorythosaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer head = model.getCube("Head");

        AdvancedModelRenderer neck1 = model.getCube("Neck");
        AdvancedModelRenderer neck2 = model.getCube("Neck 2");

        // body parts
        AdvancedModelRenderer stomach = model.getCube("Body 1");
        AdvancedModelRenderer shoulders = model.getCube("Body 2");
        AdvancedModelRenderer waist = model.getCube("Body 3");

        // tail parts
        AdvancedModelRenderer tail1 = model.getCube("Tail 1");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
        AdvancedModelRenderer tail6 = model.getCube("Tail 6");

        // left foot
        AdvancedModelRenderer leftThigh = model.getCube("Left Thigh");
        AdvancedModelRenderer leftCalf = model.getCube("Left Calf 1");
        AdvancedModelRenderer leftUpperFoot = model.getCube("Left Upper Foot");
        AdvancedModelRenderer leftFoot = model.getCube("Foot Left");

        // right foot
        AdvancedModelRenderer rightThigh = model.getCube("Right Thigh");
        AdvancedModelRenderer rightCalf = model.getCube("Right Calf 1");
        AdvancedModelRenderer rightUpperFoot = model.getCube("Right Upper Foot");
        AdvancedModelRenderer rightFoot = model.getCube("Foot Right");

        // right arm
        AdvancedModelRenderer upperArmRight = model.getCube("Upper Arm Right");
        AdvancedModelRenderer lowerArmRight = model.getCube("Lower Arm Right");
        AdvancedModelRenderer rightHand = model.getCube("Right Hand");
        AdvancedModelRenderer rightFingers = model.getCube("Right Fingers");

        // left arm
        AdvancedModelRenderer upperArmLeft = model.getCube("Upper Arm Left");
        AdvancedModelRenderer lowerArmLeft = model.getCube("Lower Arm Left");
        AdvancedModelRenderer leftHand = model.getCube("Left Hand");
        AdvancedModelRenderer leftFingers = model.getCube("Left Fingers");

        AdvancedModelRenderer jaw = model.getCube("Jaw");

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };

        float scaleFactor = 0.6F;
        model.chainWave(tail, 1F * scaleFactor, -0.1F, 2, f, f1);
        model.chainSwing(tail, 0.5F * scaleFactor, 0.1F, 2, f, f1);
        model.chainWave(tail, 0.1F, -0.02F, 2, ticks, 1F);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
