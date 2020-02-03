package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.TylosaurusEntity;

@SideOnly(Side.CLIENT)
public class TylosaurusAnimator extends EntityAnimator<TylosaurusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, TylosaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer head = model.getCube("Main head");
        AdvancedModelRenderer neck = model.getCube("Neck ");

        AdvancedModelRenderer body1 = model.getCube("Body Section 1");
        AdvancedModelRenderer body2 = model.getCube("Body Section 2");
        AdvancedModelRenderer body3 = model.getCube("Body Section 3");

        AdvancedModelRenderer tail1 = model.getCube("Tail Section 1");
        AdvancedModelRenderer tail2 = model.getCube("Tail Section 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail Section 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail Section 4");

        AdvancedModelRenderer leftFrontFlipper = model.getCube("Left Front Flipper");
        AdvancedModelRenderer rightFrontFlipper = model.getCube("Right Front Flipper");

        AdvancedModelRenderer leftBackFlipper = model.getCube("Left Back Flipper");
        AdvancedModelRenderer rightBackFlipper = model.getCube("Right Back Flipper");

        float scaleFactor = 0.3F;

        // f = ticks;
        // f1 = 0.4F;

        AdvancedModelRenderer[] bodyParts = new AdvancedModelRenderer[] { head, neck, body1, body2, body3, tail1, tail2, tail3, tail4 };

        model.chainSwing(bodyParts, 1F * scaleFactor, 0.2F, -3, f, f1);
        head.rotationPointX -= 6 * f1 * Math.sin(f * scaleFactor);
        model.walk(rightFrontFlipper, 1 * scaleFactor, 0.6F, false, 0F, 0F, f, f1);
        model.walk(leftFrontFlipper, 1 * scaleFactor, 0.6F, false, 0F, 0F, f, f1);
        model.walk(leftBackFlipper, 1 * scaleFactor, 0.6F, false, -1F, 0F, f, f1);
        model.walk(rightBackFlipper, 1 * scaleFactor, 0.6F, false, -1F, 0F, f, f1);

        model.bob(head, 0.25F * scaleFactor, 5F, false, ticks, 0.1F);

        model.walk(rightFrontFlipper, 0.25F * scaleFactor, 1.5F, false, 0F, 0F, ticks, 0.025F);
        model.walk(leftFrontFlipper, 0.25F * scaleFactor, 1.5F, false, 0F, 0F, ticks, 0.025F);
        model.walk(leftBackFlipper, 0.25F * scaleFactor, 1.5F, false, -1F, 0F, ticks, 0.025F);
        model.walk(rightBackFlipper, 0.25F * scaleFactor, 1.5F, false, -1F, 0F, ticks, 0.025F);

        entity.tailBuffer.applyChainSwingBuffer(bodyParts);
    }
}
