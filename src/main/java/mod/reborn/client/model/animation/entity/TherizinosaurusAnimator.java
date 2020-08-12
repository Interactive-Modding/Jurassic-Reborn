package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.TherizinosaurusEntity;

@SideOnly(Side.CLIENT)
public class TherizinosaurusAnimator extends EntityAnimator<TherizinosaurusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, TherizinosaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer rightThigh = model.getCube("Right Thigh");
        AdvancedModelRenderer bodyHips = model.getCube("Body hips");
        AdvancedModelRenderer rightCalf1 = model.getCube("Right Calf 1");
        AdvancedModelRenderer rightCalf2 = model.getCube("Right Calf 2");
        AdvancedModelRenderer footRight = model.getCube("Foot Right");
        AdvancedModelRenderer tail1 = model.getCube("Tail 1");
        AdvancedModelRenderer bodyMain = model.getCube("Body main");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail2Feathers = model.getCube("Tail 2 feathers");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail3Feathers = model.getCube("Tail 3 feathers");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
        AdvancedModelRenderer tail4Feathers = model.getCube("Tail 4 feathers");
        AdvancedModelRenderer tail6 = model.getCube("Tail 6");
        AdvancedModelRenderer tail5Feathers = model.getCube("Tail 5 feathers");
        AdvancedModelRenderer tail6Feathers = model.getCube("Tail 6 feathers");
        AdvancedModelRenderer tail6FeathersR = model.getCube("Tail 6 feathers r");
        AdvancedModelRenderer tail6FeathersL = model.getCube("Tail 6 feathers l");
        AdvancedModelRenderer tail5FeathersR = model.getCube("Tail 5 feathers r");
        AdvancedModelRenderer tail5FeathersL = model.getCube("Tail 5 feathers l");
        AdvancedModelRenderer tail4FeathersR = model.getCube("Tail 4 feathers r");
        AdvancedModelRenderer tail4FeathersL = model.getCube("Tail 4 feathers l");
        AdvancedModelRenderer tail3FeathersR = model.getCube("Tail 3 feathers r");
        AdvancedModelRenderer tail3FeathersL = model.getCube("Tail 3 feathers l");
        AdvancedModelRenderer bodyShoulders = model.getCube("Body shoulders");
        AdvancedModelRenderer bodyMain1 = model.getCube("Body main 1");
        AdvancedModelRenderer neckBase = model.getCube("Neck base");
        AdvancedModelRenderer neck1 = model.getCube("Neck 1");
        AdvancedModelRenderer neck2 = model.getCube("Neck 2");
        AdvancedModelRenderer neck3 = model.getCube("Neck 3");
        AdvancedModelRenderer neck4 = model.getCube("Neck 4");
        AdvancedModelRenderer neck5 = model.getCube("Neck 5");
        AdvancedModelRenderer neck4feathers = model.getCube("Neck 4 feathers");
        AdvancedModelRenderer neck6 = model.getCube("Neck 6");
        AdvancedModelRenderer neck5Feathers = model.getCube("Neck 5 feathers");
        AdvancedModelRenderer neck7 = model.getCube("Neck 7");
        AdvancedModelRenderer neck6Feathers = model.getCube("Neck 6 feathers");
        AdvancedModelRenderer head = model.getCube("Head");
        AdvancedModelRenderer neck7Feathers = model.getCube("Neck 7 feathers");
        AdvancedModelRenderer snout = model.getCube("Snout");
        AdvancedModelRenderer lowerJaw = model.getCube("Lower Jaw");
        AdvancedModelRenderer snoutRoof = model.getCube("Snout roof");
        AdvancedModelRenderer upperJaw = model.getCube("Upper Jaw");
        AdvancedModelRenderer neck7FeathersR = model.getCube("Neck 7 feathers r");
        AdvancedModelRenderer neck7FeathersL = model.getCube("Neck 7 feathers l");
        AdvancedModelRenderer neck6FeathersR = model.getCube("Neck 6 feathers r");
        AdvancedModelRenderer neck6FeathersL = model.getCube("Neck 6 feathers l");
        AdvancedModelRenderer neck5FeathersR = model.getCube("Neck 5 feathers r");
        AdvancedModelRenderer neck5FeathersL = model.getCube("Neck 5 feathers l");
        AdvancedModelRenderer neck4FeathersR = model.getCube("Neck 4 feathers r");
        AdvancedModelRenderer neck4FeathersL = model.getCube("Neck 4 feathers l");
        AdvancedModelRenderer lowerArmRight = model.getCube("Lower Arm Right");
        AdvancedModelRenderer lowerArmRight1 = model.getCube("Lower Arm Right 1");
        AdvancedModelRenderer rightHand = model.getCube("Right hand");
        AdvancedModelRenderer armRightFeathers = model.getCube("Arm right feathers");
        AdvancedModelRenderer rightFinger1 = model.getCube("Right finger 1");
        AdvancedModelRenderer rightFinger2 = model.getCube("Right finger 2");
        AdvancedModelRenderer righFinger3 = model.getCube("Right finger 3");
        AdvancedModelRenderer rF1mid = model.getCube("RF1 mid");
        AdvancedModelRenderer rF1end = model.getCube("RF1 end");
        AdvancedModelRenderer rF2mid = model.getCube("RF2 mid");
        AdvancedModelRenderer rF2end = model.getCube("RF2 end");
        AdvancedModelRenderer rF3mid = model.getCube("RF3 mid");
        AdvancedModelRenderer rF3end = model.getCube("RF3 end");
        AdvancedModelRenderer lowerArmLeft = model.getCube("Lower Arm LEFT");
        AdvancedModelRenderer lowerArmLeft1 = model.getCube("Lower Arm LEFT 1");
        AdvancedModelRenderer leftHand = model.getCube("Left hand");
        AdvancedModelRenderer armLeftFeathers = model.getCube("Arm left feathers");
        AdvancedModelRenderer leftfinger1 = model.getCube("Left finger 1");
        AdvancedModelRenderer leftfinger2 = model.getCube("Left finger 2");
        AdvancedModelRenderer leftfinger3 = model.getCube("Left finger 3");
        AdvancedModelRenderer lF1mid = model.getCube("LF1 mid");
        AdvancedModelRenderer lF1end = model.getCube("LF1 end");
        AdvancedModelRenderer lF2mid = model.getCube("LF1 end");
        AdvancedModelRenderer lF2end = model.getCube("LF2 end");
        AdvancedModelRenderer lF3mid = model.getCube("LF3 mid");
        AdvancedModelRenderer lF3end = model.getCube("LF3 mid");
        AdvancedModelRenderer leftThigh = model.getCube("Left Thigh");
        AdvancedModelRenderer leftCalf1 = model.getCube("Left Calf 1");
        AdvancedModelRenderer leftCalf2 = model.getCube("Left Calf 2");
        AdvancedModelRenderer footLeft = model.getCube("Foot Left");

        AdvancedModelRenderer[] neck = new AdvancedModelRenderer[] { head, neck7, neck6, neck5, neck4, neck3, neck2, neck1, neckBase, bodyShoulders };
        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail1, tail2, tail3, tail4, tail5, tail6 };
        AdvancedModelRenderer[] armLeft = new AdvancedModelRenderer[] { lowerArmLeft, lowerArmLeft1, leftHand };
        AdvancedModelRenderer[] armRight = new AdvancedModelRenderer[] { lowerArmRight, lowerArmRight1, rightHand };

        // The tail must always be up when the neck is down
        float speed = 0.75F;
        float height = 3F;

        model.bob(bodyHips, 1F * speed, height, false, f, f1);
      //  model.flap(bodyHips, 0.5F * speed, 0.5F, false, 0, 0, f, f1);
      //  model.flap(bodyMain, 0.5F * speed, 0.1F, true, 0, 0, f, f1);
      //  model.flap(bodyShoulders, 0.5F * speed, 0.4F, true, 0, 0, f, f1);
      //  model.flap(tail1, 0.5F * speed, 0.2F, true, 0, 0, f, f1);
      //  model.flap(tail3, 0.5F * speed, 0.2F, true, 0, 0, f, f1);
      //  model.flap(tail5, 0.5F * speed, 0.1F, true, 0, 0, f, f1);
        model.bob(leftThigh, 1F * speed, height, false, f, f1);
        model.bob(rightThigh, 1F * speed, height, false, f, f1);
        model.walk(bodyShoulders, 1F * speed, 0.2F, true, 1, 0, f, f1);
        model.walk(bodyMain1, 1F * speed, 0.2F, false, 0.5F, 0, f, f1);

        model.walk(leftThigh, 0.5F * speed, 0.7F, false, 3.14F, 0.2F, f, f1);
        model.walk(leftCalf1, 0.5F * speed, 0.6F, false, 1.5F, 0.3F, f, f1);
        model.walk(leftCalf2, 0.5F * speed, 0.8F, false, -1F, -0.1F, f, f1);
        model.walk(footLeft, 0.5F * speed, 1.5F, true, -1F, 1F, f, f1);

        model.walk(rightThigh, 0.5F * speed, 0.7F, true, 3.14F, 0.2F, f, f1);
        model.walk(rightCalf1, 0.5F * speed, 0.6F, true, 1.5F, 0.3F, f, f1);
        model.walk(rightCalf2, 0.5F * speed, 0.8F, true, -1F, -0.1F, f, f1);
        model.walk(footRight, 0.5F * speed, 1.5F, false, -1F, 1F, f, f1);

        model.chainSwing(tail, 0.5F * speed, -0.02F, 2, f, f1);
        model.chainWave(tail, 1F * speed, -0.02F, 2.5F, f, f1);
        model.chainSwing(neck, 0.5F * speed, 0.02F, 2, f, f1);
        model.chainWave(neck, 1.0F * speed, 0.02F, 0.5F, f, f1);

        model.chainWave(armRight, 1F * speed, -0.3F, 4, f, f1);
        model.chainWave(armLeft, 1F * speed, -0.3F, 4, f, f1);

        model.chainWave(tail, 0.1F, 0.02F, 2, ticks, 0.25F);
        model.chainWave(neck, 0.1F, 0.02F, 2, ticks, 0.25F);
        model.chainWave(armRight, 0.1F, -0.1F, 4, ticks, 0.25F);
        model.chainWave(armLeft, 0.1F, -0.1F, 4, ticks, 0.25F);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
