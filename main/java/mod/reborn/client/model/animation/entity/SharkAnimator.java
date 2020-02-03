package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import mod.reborn.server.entity.animal.EntityCrab;
import mod.reborn.server.entity.animal.EntityShark;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SharkAnimator extends EntityAnimator<EntityShark> {
    @Override
    protected void performAnimations(AnimatableModel model, EntityShark entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelRenderer leftFlipper = model.getCube("LeftFin1");
        AdvancedModelRenderer rightFlipper = model.getCube("RightFin1");
        AdvancedModelRenderer tail5 = model.getCube("Tail5");
        AdvancedModelRenderer tail4 = model.getCube("Tail4");
        AdvancedModelRenderer tail3 = model.getCube("Tail3");
        AdvancedModelRenderer tail2 = model.getCube("Tail2");
        AdvancedModelRenderer tail1 = model.getCube("Tail1");
        AdvancedModelRenderer body1 = model.getCube("Body1");

        AdvancedModelRenderer[] body = new AdvancedModelRenderer[] { tail5, tail4, tail3, tail2, tail1, body1 };

        model.chainSwing(body, 1F, 0.3514F, -3, f, f1);

        model.walk(rightFlipper, 0.25F, 0.2F, false, 0F, 0F, ticks, 0.005F);
        model.walk(leftFlipper, 0.25F, 0.2F, false, 0F, 0F, ticks, 0.005F);
    }
}
