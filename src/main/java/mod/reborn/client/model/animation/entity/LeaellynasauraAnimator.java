package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.LeaellynasauraEntity;

@SideOnly(Side.CLIENT)
public class LeaellynasauraAnimator extends EntityAnimator<LeaellynasauraEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, LeaellynasauraEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        float globalSpeed = 0.4F;
        float globalHeight = 0.8F;
        float globalDegree = 0.8F;

        AdvancedModelRenderer head = model.getCube("Head ");

        AdvancedModelRenderer neck1 = model.getCube("Neck BASE");
        AdvancedModelRenderer neck2 = model.getCube("Neck 2");
        AdvancedModelRenderer neck3 = model.getCube("Neck 3");
        AdvancedModelRenderer neck4 = model.getCube("Neck 4");
        AdvancedModelRenderer neck5 = model.getCube("Neck 5");

        AdvancedModelRenderer bodyFront = model.getCube("Body FRONT");
        AdvancedModelRenderer bodyRear = model.getCube("Body REAR");

        AdvancedModelRenderer tail1 = model.getCube("Tail BASE");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
        AdvancedModelRenderer tail6 = model.getCube("Tail 6");

        AdvancedModelRenderer thighLeft = model.getCube("Leg UPPER LEFT");
        AdvancedModelRenderer thighRight = model.getCube("Leg UPPER RIGHT");

        AdvancedModelRenderer calf1Left = model.getCube("Leg MIDDLE LEFT");
        AdvancedModelRenderer calf1Right = model.getCube("Leg MIDDLE RIGHT");

        AdvancedModelRenderer calf2Left = model.getCube("Leg LOWER LEFT");
        AdvancedModelRenderer calf2Right = model.getCube("Leg LOWER RIGHT");

        AdvancedModelRenderer footLeft = model.getCube("Foot LEFT");
        AdvancedModelRenderer footRight = model.getCube("Foot RIGHT");

        AdvancedModelRenderer armUpperLeft = model.getCube("Arm UPPER LEFT");
        AdvancedModelRenderer armUpperRight = model.getCube("Arm UPPER RIGHT");

        AdvancedModelRenderer armLowerLeft = model.getCube("Arm MIDDLE LEFT");
        AdvancedModelRenderer armLowerRight = model.getCube("Arm MIDDLE RIGHT");

        AdvancedModelRenderer handLeft = model.getCube("Arm HAND LEFT");
        AdvancedModelRenderer handRight = model.getCube("Arm HAND RIGHT");

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelRenderer[] neck = new AdvancedModelRenderer[] { head, neck5, neck4, neck3, neck2, neck1, bodyFront };

        AdvancedModelRenderer[] armLeft = new AdvancedModelRenderer[] { handLeft, armLowerLeft, armUpperLeft };
        AdvancedModelRenderer[] armRight = new AdvancedModelRenderer[] { handRight, armLowerRight, armUpperRight };

        model.bob(bodyRear, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);
        model.bob(thighLeft, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);
        model.bob(thighRight, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);

        model.chainWave(tail, globalSpeed * 1.0F, globalHeight * 0.2F, 3, f, f1);
        model.chainSwing(tail, globalSpeed * 0.5F, globalHeight * 0.4F, 3, f, f1);

        model.chainWave(neck, globalSpeed * 1.0F, globalHeight * 0.1F, -3, f, f1);

        model.chainWave(armLeft, globalSpeed * 1.0F, globalHeight * 0.4F, -2, f, f1);
        model.chainWave(armRight, globalSpeed * 1.0F, globalHeight * 0.4F, -2, f, f1);

        model.walk(thighLeft, 1.0F * globalSpeed, globalDegree * 0.8F, false, 0F, 0.4F, f, f1);
        model.walk(calf1Left, 1.0F * globalSpeed, globalDegree * 0.5F, true, 1F, 0F, f, f1);
        model.walk(calf2Left, 1.0F * globalSpeed, globalDegree * 0.5F, false, 0F, 0F, f, f1);
        model.walk(footLeft, 1.0F * globalSpeed, globalDegree * 1.5F, true, 0.5F, 1F, f, f1);

        model.walk(thighRight, 1.0F * globalSpeed, globalDegree * 0.8F, true, 0F, 0.4F, f, f1);
        model.walk(calf1Right, 1.0F * globalSpeed, globalDegree * 0.5F, false, 1F, 0F, f, f1);
        model.walk(calf2Right, 1.0F * globalSpeed, globalDegree * 0.5F, true, 0F, 0F, f, f1);
        model.walk(footRight, 1.0F * globalSpeed, globalDegree * 1.5F, false, 0.5F, 1F, f, f1);

        model.chainWave(tail, globalSpeed * 0.5F, globalHeight * 0.8F, 3, ticks, 0.025F);
        model.chainWave(neck, globalSpeed * 0.5F, globalHeight * 0.4F, -3, ticks, 0.025F);
    }
}
