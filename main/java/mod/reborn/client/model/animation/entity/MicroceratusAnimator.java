package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.MicroceratusEntity;

@SideOnly(Side.CLIENT)
public class MicroceratusAnimator extends EntityAnimator<MicroceratusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel parModel, MicroceratusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer body = parModel.getCube("Body MAIN");

        AdvancedModelRenderer tail1 = parModel.getCube("Tail #1");
        AdvancedModelRenderer tail2 = parModel.getCube("Tail #2");
        AdvancedModelRenderer tail3 = parModel.getCube("Tail #3");
        AdvancedModelRenderer tail4 = parModel.getCube("Tail #4");
        AdvancedModelRenderer tail5 = parModel.getCube("Tail #5");

        AdvancedModelRenderer neck1 = parModel.getCube("Neck #1");
        AdvancedModelRenderer neck2 = parModel.getCube("Neck #2");

        AdvancedModelRenderer head = parModel.getCube("Head");

        AdvancedModelRenderer thighLeft = parModel.getCube("Leg Top LEFT");
        AdvancedModelRenderer thighRight = parModel.getCube("Leg Top RIGHT");

        AdvancedModelRenderer thighMidLeft = parModel.getCube("Leg Mid LEFT");
        AdvancedModelRenderer thighMidRight = parModel.getCube("Leg Mid RIGHT");

        AdvancedModelRenderer upperFootLeft = parModel.getCube("Leg Bot LEFT");
        AdvancedModelRenderer upperFootRight = parModel.getCube("Leg Bot RIGHT");

        AdvancedModelRenderer footLeft = parModel.getCube("Leg Foot LEFT");
        AdvancedModelRenderer footRight = parModel.getCube("Leg Foot RIGHT");

        AdvancedModelRenderer armTopLeft = parModel.getCube("Arm Top LEFT");
        AdvancedModelRenderer armTopRight = parModel.getCube("Arm Top RIGHT");

        AdvancedModelRenderer armMidLeft = parModel.getCube("Arm Mid LEFT");
        AdvancedModelRenderer armMidRight = parModel.getCube("Arm Mid RIGHT");

        AdvancedModelRenderer handLeft = parModel.getCube("Arm Hand LEFT");
        AdvancedModelRenderer handRight = parModel.getCube("Arm Hand RIGHT");

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelRenderer[] neck = new AdvancedModelRenderer[] { head, neck2, neck1, body };

        AdvancedModelRenderer[] armLeft = new AdvancedModelRenderer[] { handLeft, armMidLeft, armTopLeft };
        AdvancedModelRenderer[] armRight = new AdvancedModelRenderer[] { handRight, armMidRight, armTopRight };

        // f = ticks;
        // f1 = 0.5F;

        float globalSpeed = 0.8F;
        float globalDegree = 0.5F;
        float globalHeight = 1.0F;

        parModel.bob(body, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);
        parModel.bob(thighLeft, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);
        parModel.bob(thighRight, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);

        parModel.chainWave(tail, globalSpeed * 1.0F, globalHeight * 0.1F, 2, f, f1);
        parModel.chainWave(neck, globalSpeed * 1.0F, globalHeight * 0.1F, 3, f, f1);

        parModel.chainWave(armLeft, globalSpeed * 1.0F, globalHeight * 0.2F, 3, f, f1);
        parModel.chainWave(armRight, globalSpeed * 1.0F, globalHeight * -0.2F, 3, f, f1);

        parModel.walk(thighLeft, globalSpeed * 1.0F, globalDegree * 1.0F, true, 0.0F, 0.0F, f, f1);
        parModel.walk(thighMidLeft, globalSpeed * 1.0F, globalDegree * 1.0F, true, 1.0F, 0.2F, f, f1);
        parModel.walk(footLeft, globalSpeed * 1.0F, globalDegree * 1.0F, false, -0.25F, -0.2F, f, f1);

        parModel.walk(thighRight, globalSpeed * 1.0F, globalDegree * 1.0F, false, 0.0F, 0.0F, f, f1);
        parModel.walk(thighMidRight, globalSpeed * 1.0F, globalDegree * 1.0F, false, 1.0F, 0.2F, f, f1);
        parModel.walk(footRight, globalSpeed * 1.0F, globalDegree * 1.0F, true, -0.25F, -0.2F, f, f1);

        parModel.chainWave(tail, globalSpeed * 0.2F, globalHeight * 0.05F, 2, ticks, 0.25F);
        parModel.chainWave(neck, globalSpeed * 0.2F, globalHeight * 0.05F, 3, ticks, 0.25F);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
