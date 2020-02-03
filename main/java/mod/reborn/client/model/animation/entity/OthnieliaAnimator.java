package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.OthnieliaEntity;

@SideOnly(Side.CLIENT)
public class OthnieliaAnimator extends EntityAnimator<OthnieliaEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, OthnieliaEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        float speed = 0.8F;
        float height = 12F * f1;

        AdvancedModelRenderer bodyRear = model.getCube("Body REAR");
        AdvancedModelRenderer bodyFront = model.getCube("Body FRONT");
        AdvancedModelRenderer neck1 = model.getCube("Neck BASE");
        AdvancedModelRenderer neck2 = model.getCube("Neck 2");
        AdvancedModelRenderer neck3 = model.getCube("Neck 3");
        AdvancedModelRenderer head = model.getCube("Head ");

        AdvancedModelRenderer tail1 = model.getCube("Tail BASE");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
        AdvancedModelRenderer tail6 = model.getCube("Tail 6");

        AdvancedModelRenderer thighLeft = model.getCube("Leg UPPER LEFT");
        AdvancedModelRenderer thighRight = model.getCube("Leg UPPER RIGHT");
        AdvancedModelRenderer lowerThighLeft = model.getCube("Leg MIDDLE LEFT");
        AdvancedModelRenderer lowerThighRight = model.getCube("Leg MIDDLE RIGHT");
        AdvancedModelRenderer upperFootLeft = model.getCube("Leg LOWER LEFT");
        AdvancedModelRenderer upperFootRight = model.getCube("Leg LOWER RIGHT");
        AdvancedModelRenderer footLeft = model.getCube("Foot LEFT");
        AdvancedModelRenderer footRight = model.getCube("Foot RIGHT");

        AdvancedModelRenderer upperArmLeft = model.getCube("Arm UPPER LEFT");
        AdvancedModelRenderer upperArmRight = model.getCube("Arm UPPER RIGHT");

        AdvancedModelRenderer lowerArmLeft = model.getCube("Arm MIDDLE LEFT");
        AdvancedModelRenderer lowerArmRight = model.getCube("Arm MIDDLE RIGHT");

        AdvancedModelRenderer handLeft = model.getCube("Arm HAND LEFT");
        AdvancedModelRenderer handRight = model.getCube("Arm HAND RIGHT");

        AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };

        model.bob(bodyRear, 0.5F * speed, height, true, f, f1);
        model.bob(thighRight, 0.5F * speed, height, true, f, f1);
        model.bob(thighLeft, 0.5F * speed, height, true, f, f1);

        model.walk(thighLeft, 1F * speed, 0.75F, true, 1F, 0.25F, f, f1);
        model.walk(thighRight, 1F * speed, 0.75F, true, 0.5F, 0.25F, f, f1);
        model.walk(lowerThighLeft, 1F * speed, 0.75F, false, 1.5F, 0F, f, f1);
        model.walk(lowerThighRight, 1F * speed, 0.75F, false, 1F, 0F, f, f1);
        model.walk(upperFootRight, 1F * speed, 0.75F, true, 0.5F, 0F, f, f1);
        model.walk(upperFootLeft, 1F * speed, 0.75F, true, 1F, 0F, f, f1);
        model.walk(footLeft, 1F * speed, 0.5F, true, 1F, 0.75F, f, f1);
        model.walk(footRight, 1F * speed, 0.5F, true, 0.5F, 0.75F, f, f1);

        model.walk(bodyRear, 1F * speed, 0.3F, false, 0.5F, 0F, f, f1);
        model.walk(bodyFront, 1F * speed, 0.5F, true, 1.0F, 0F, f, f1);
        model.walk(neck1, 1F * speed, 0.3F, true, 0.25F, 0.3F, f, f1);
        model.walk(head, 1F * speed, 0.3F, false, 0.25F, -0.3F, f, f1);

        model.walk(upperArmRight, 1 * speed, 0.3F, true, 1, 0.2F, f, f1);
        model.walk(upperArmLeft, 1 * speed, 0.3F, true, 1, 0.2F, f, f1);
        model.walk(lowerArmRight, 1 * speed, 0.3F, false, 1, -0.2F, f, f1);
        model.walk(lowerArmLeft, 1 * speed, 0.3F, false, 1, -0.2F, f, f1);

        model.chainWave(tailParts, 0.2F, -0.05F, 2, ticks, 0.25F);
        model.walk(neck1, 0.2F, 0.1F, false, -1F, 0F, ticks, 0.25F);
        model.walk(head, 0.2F, 0.1F, true, 0F, 0F, ticks, 0.25F);
        model.walk(bodyFront, 0.2F, 0.1F, true, 0F, 0F, ticks, 0.25F);
        model.walk(bodyRear, 0.2F, 0.1F, false, 0F, 0F, ticks, 0.25F);
        model.walk(upperArmRight, 0.2F, 0.1F, true, 0F, 0F, ticks, 0.25F);
        model.walk(upperArmLeft, 0.2F, 0.1F, true, 0F, 0F, ticks, 0.25F);
        model.walk(lowerArmRight, 0.2F, 0.1F, false, 0F, 0F, ticks, 0.25F);
        model.walk(lowerArmLeft, 0.2F, 0.1F, false, 0F, 0F, ticks, 0.25F);

        model.chainWave(tailParts, 1F * speed, 0.15F, 2, f, f1);

        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
