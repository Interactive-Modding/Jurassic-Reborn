package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.GiganotosaurusEntity;

@SideOnly(Side.CLIENT)
public class GiganotosaurusAnimator extends EntityAnimator<GiganotosaurusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, GiganotosaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        float globalSpeed = 0.45F;
        float globalDegree = 0.45F;
        float height = 1.0F;

        // f = ticks;
        // f1 = 1.0F;

        AdvancedModelRenderer neck = model.getCube("Neck 1");
        AdvancedModelRenderer neck2 = model.getCube("Neck 2");
        AdvancedModelRenderer neck3 = model.getCube("Neck 3");
        AdvancedModelRenderer neck4 = model.getCube("Neck 4");

        AdvancedModelRenderer throat = model.getCube("Throat 1");
        AdvancedModelRenderer throat2 = model.getCube("Throat 2");

        AdvancedModelRenderer head = model.getCube("Head");

        AdvancedModelRenderer tail1 = model.getCube("Tail 1");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
        AdvancedModelRenderer tail6 = model.getCube("Tail 6");

        AdvancedModelRenderer body1 = model.getCube("Body shoulders");
        AdvancedModelRenderer body2 = model.getCube("Body waist");
        AdvancedModelRenderer body3 = model.getCube("Body hips");

        AdvancedModelRenderer rightThigh = model.getCube("Right Thigh");
        AdvancedModelRenderer leftThigh = model.getCube("Left Thigh");

        AdvancedModelRenderer rightFoot = model.getCube("Foot Right");
        AdvancedModelRenderer leftFoot = model.getCube("Foot Left");

        AdvancedModelRenderer rightCalf = model.getCube("Right Calf 1");
        AdvancedModelRenderer leftCalf = model.getCube("Left Calf 1");

        AdvancedModelRenderer rightCalf2 = model.getCube("Right Calf 2");
        AdvancedModelRenderer leftCalf2 = model.getCube("Left Calf 2");

        AdvancedModelRenderer lowerJaw = model.getCube("Lower jaw");

        AdvancedModelRenderer[] body = new AdvancedModelRenderer[] { head, neck4, neck3, neck2, neck, body1, body2, body3 };

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };

        // TODO:Arms

        // body3.rotateAngleX += f1 * 0.15F;

        model.bob(body3, globalSpeed * 1.0F, height * 1.0F, false, f, f1);

        model.bob(leftThigh, globalSpeed * 1.0F, height * 1.0F, false, f, f1);
        model.bob(rightThigh, globalSpeed * 1.0F, height * 1.0F, false, f, f1);

        model.chainWave(body, globalSpeed * 1.0F, height * 0.02F, 3, f, f1);
        model.chainWave(tail, globalSpeed * 1.0F, height * 0.05F, 2, f, f1);

        model.walk(rightThigh, globalSpeed * 0.5F, globalDegree * 0.8F, true, 0, 0.2F, f, f1);
        model.walk(leftThigh, globalSpeed * 0.5F, globalDegree * 0.8F, false, 0, 0.2F, f, f1);

        model.walk(leftCalf, globalSpeed * 0.5F, globalDegree * 1F, false, -1.3F, 0.4F, f, f1);
        model.walk(rightCalf, globalSpeed * 0.5F, globalDegree * 1F, true, -1.3F, 0.4F, f, f1);

        model.walk(leftCalf2, globalSpeed * 0.5F, globalDegree * 1.1F, true, -2F, 0F, f, f1);
        model.walk(rightCalf2, globalSpeed * 0.5F, globalDegree * 1.1F, false, -2F, 0F, f, f1);


        model.walk(head, 1F * globalSpeed, 0.15F, true, 0F, 0.2F, f, f1);
        model.walk(neck, 1F * globalSpeed, 0.03F, false, 0F, 0.04F, f, f1);
        model.walk(neck2, 1F * globalSpeed, 0.03F, false, 0F, 0.04F, f, f1);
        model.walk(neck3, 1F * globalSpeed, 0.03F, false, 0F, 0.04F, f, f1);
        model.walk(neck4, 1F * globalSpeed, 0.03F, false, 0F, 0.04F, f, f1);

        double rotationPointY = 2 * f1 * Math.cos(f * 0.5F * globalSpeed);
        leftThigh.rotationPointY += rotationPointY;
        rightThigh.rotationPointY -= rotationPointY;

        model.chainWave(tail, 0.1F, -0.05F, 2, ticks, 0.25F);
        model.chainWave(body, 0.1F, 0.03F, 4, ticks, 0.25F);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
