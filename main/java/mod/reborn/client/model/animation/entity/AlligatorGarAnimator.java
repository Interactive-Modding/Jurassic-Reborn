package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import mod.reborn.server.entity.dinosaur.AlligatorGarEntity;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AlligatorGarAnimator extends EntityAnimator<AlligatorGarEntity>
{

@Override
protected void performAnimations(AnimatableModel model, AlligatorGarEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {

    //TODO: needs some work.
        AdvancedModelRenderer neck = model.getCube("Neck");
        AdvancedModelRenderer body1 = model.getCube("Body Section 1");
        AdvancedModelRenderer body2 = model.getCube("Body Section 2");
        AdvancedModelRenderer body3 = model.getCube("Body Section 3");
        AdvancedModelRenderer tail1 = model.getCube("Tail Section 1");
        AdvancedModelRenderer tail2 = model.getCube("Tail Section 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail Section 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail section 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail section 5");
        AdvancedModelRenderer leftFlipper = model.getCube("Left Front Flipper");
        AdvancedModelRenderer rightFlipper = model.getCube("Right Front Flipper");
        AdvancedModelRenderer lowerJawFront = model.getCube("Lower jaw top");

        AdvancedModelRenderer[] body = new AdvancedModelRenderer[] { tail5, tail4, tail3, tail2, tail1, body3, body2, body1 };
        AdvancedModelRenderer[] frontBody = new AdvancedModelRenderer[] { body3, body2, body1 };

        model.chainSwing(frontBody, 0.6F, 0.4F, 3.0D, limbSwing, limbSwingAmount);

        if (entity.isInWater()) {
        model.walk(leftFlipper, 0.2F, 0.25F, false, 1.0F, 0.1F, ticks, 0.25F);
        model.walk(rightFlipper, 0.2F, 0.25F, false, 1.0F, 0.1F, ticks, 0.25F);
        model.chainSwing(body, 0.05F, -0.075F, 1.5D, ticks, 0.25F);
        model.walk(lowerJawFront, 0.1F, 0.7F, false, 0.0F, 0.5F, ticks, 0.25F);

        }

        entity.tailBuffer.applyChainSwingBuffer(body);
        }
}