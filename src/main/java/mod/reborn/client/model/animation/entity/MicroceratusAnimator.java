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
    protected void performAnimations(AnimatableModel model, MicroceratusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer body = model.getCube("Body MAIN");

        AdvancedModelRenderer tail1 = model.getCube("Tail #1");
        AdvancedModelRenderer tail2 = model.getCube("Tail #2");
        AdvancedModelRenderer tail3 = model.getCube("Tail #3");
        AdvancedModelRenderer tail4 = model.getCube("Tail #4");
        AdvancedModelRenderer tail5 = model.getCube("Tail #5");
        AdvancedModelRenderer tail6 = model.getCube("Tail #6");

        AdvancedModelRenderer neck1 = model.getCube("Neck #1");

        AdvancedModelRenderer head = model.getCube("Head");

        AdvancedModelRenderer thighLeft = model.getCube("Leg Top LEFT");
        AdvancedModelRenderer thighRight = model.getCube("Leg Top RIGHT");

        AdvancedModelRenderer thighMidLeft = model.getCube("Leg Mid LEFT");
        AdvancedModelRenderer thighMidRight = model.getCube("Leg Mid RIGHT");

        AdvancedModelRenderer upperFootLeft = model.getCube("Leg Bot LEFT");
        AdvancedModelRenderer upperFootRight = model.getCube("Leg Bot RIGHT");

        AdvancedModelRenderer footLeft = model.getCube("Leg Foot LEFT");
        AdvancedModelRenderer footRight = model.getCube("Leg Foot RIGHT");

        AdvancedModelRenderer armTopLeft = model.getCube("Arm Top LEFT");
        AdvancedModelRenderer armTopRight = model.getCube("Arm Top RIGHT");

        AdvancedModelRenderer armMidLeft = model.getCube("Arm Mid LEFT");
        AdvancedModelRenderer armMidRight = model.getCube("Arm Mid RIGHT");

        AdvancedModelRenderer handLeft = model.getCube("Arm Hand LEFT");
        AdvancedModelRenderer handRight = model.getCube("Arm Hand RIGHT");

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelRenderer[] neck = new AdvancedModelRenderer[] { head, neck1, body };

        AdvancedModelRenderer[] armLeft = new AdvancedModelRenderer[] { handLeft, armMidLeft, armTopLeft };
        AdvancedModelRenderer[] armRight = new AdvancedModelRenderer[] { handRight, armMidRight, armTopRight };
        

        // f = ticks;
        // f1 = 0.5F;

        float globalSpeed = 0.8F;
        float globalDegree = 0.5F;
        float globalHeight = 1.0F;

        model.bob(body, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);
        model.bob(thighLeft, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);
        model.bob(thighRight, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);

        model.chainWave(tail, globalSpeed * 1.0F, globalHeight * 0.1F, 2, f, f1);
        model.chainWave(neck, globalSpeed * 1.0F, globalHeight * 0.1F, 3, f, f1);

        model.chainWave(armLeft, globalSpeed * 1.0F, globalHeight * 0.2F, 3, f, f1);
        model.chainWave(armRight, globalSpeed * 1.0F, globalHeight * -0.2F, 3, f, f1);

        model.walk(thighLeft, globalSpeed * 1.0F, globalDegree * 1.0F, true, 0.0F, 0.0F, f, f1);
        model.walk(thighMidLeft, globalSpeed * 1.0F, globalDegree * 1.0F, true, 1.0F, 0.2F, f, f1);
        model.walk(footLeft, globalSpeed * 1.0F, globalDegree * 1.0F, false, -0.25F, -0.2F, f, f1);

        model.walk(thighRight, globalSpeed * 1.0F, globalDegree * 1.0F, false, 0.0F, 0.0F, f, f1);
        model.walk(thighMidRight, globalSpeed * 1.0F, globalDegree * 1.0F, false, 1.0F, 0.2F, f, f1);
        model.walk(footRight, globalSpeed * 1.0F, globalDegree * 1.0F, true, -0.25F, -0.2F, f, f1);

        model.chainWave(tail, globalSpeed * 0.2F, globalHeight * 0.05F, 2, ticks, 0.25F);
        model.chainWave(neck, globalSpeed * 0.2F, globalHeight * 0.05F, 3, ticks, 0.25F);
        
        entity.tailBuffer.applyChainSwingBuffer(tail);
        model.faceTarget(rotationYaw, rotationPitch, 1.0F, neck1, head);
    }
}
