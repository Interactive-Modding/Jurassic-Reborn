package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.LeaellynasauraEntity;

@OnlyIn(Dist.CLIENT)
public class LeaellynasauraAnimator extends EntityAnimator<LeaellynasauraEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, LeaellynasauraEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        float globalSpeed = 0.4F;
        float globalHeight = 0.8F;
        float globalDegree = 0.8F;

        AdvancedModelBox head = model.getCube("Head ");

        AdvancedModelBox neck1 = model.getCube("Neck BASE");
        AdvancedModelBox neck2 = model.getCube("Neck 2");
        AdvancedModelBox neck3 = model.getCube("Neck 3");
        AdvancedModelBox neck4 = model.getCube("Neck 4");
        AdvancedModelBox neck5 = model.getCube("Neck 5");

        AdvancedModelBox bodyFront = model.getCube("Body FRONT");
        AdvancedModelBox bodyRear = model.getCube("Body REAR");

        AdvancedModelBox tail1 = model.getCube("Tail BASE");
        AdvancedModelBox tail2 = model.getCube("Tail 2");
        AdvancedModelBox tail3 = model.getCube("Tail 3");
        AdvancedModelBox tail4 = model.getCube("Tail 4");
        AdvancedModelBox tail5 = model.getCube("Tail 5");
        AdvancedModelBox tail6 = model.getCube("Tail 6");

        AdvancedModelBox thighLeft = model.getCube("Leg UPPER LEFT");
        AdvancedModelBox thighRight = model.getCube("Leg UPPER RIGHT");

        AdvancedModelBox calf1Left = model.getCube("Leg MIDDLE LEFT");
        AdvancedModelBox calf1Right = model.getCube("Leg MIDDLE RIGHT");

        AdvancedModelBox calf2Left = model.getCube("Leg LOWER LEFT");
        AdvancedModelBox calf2Right = model.getCube("Leg LOWER RIGHT");

        AdvancedModelBox footLeft = model.getCube("Foot LEFT");
        AdvancedModelBox footRight = model.getCube("Foot RIGHT");

        AdvancedModelBox armUpperLeft = model.getCube("Arm UPPER LEFT");
        AdvancedModelBox armUpperRight = model.getCube("Arm UPPER RIGHT");

        AdvancedModelBox armLowerLeft = model.getCube("Arm MIDDLE LEFT");
        AdvancedModelBox armLowerRight = model.getCube("Arm MIDDLE RIGHT");

        AdvancedModelBox handLeft = model.getCube("Arm HAND LEFT");
        AdvancedModelBox handRight = model.getCube("Arm HAND RIGHT");

        AdvancedModelBox[] tail = new AdvancedModelBox[] { tail6, tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelBox[] neck = new AdvancedModelBox[] { head, neck5, neck4, neck3, neck2, neck1, bodyFront };

        AdvancedModelBox[] armLeft = new AdvancedModelBox[] { handLeft, armLowerLeft, armUpperLeft };
        AdvancedModelBox[] armRight = new AdvancedModelBox[] { handRight, armLowerRight, armUpperRight };

        model.bob(bodyRear, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);
        model.bob(thighLeft, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);
        model.bob(thighRight, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);

        model.chainWave(tail, globalSpeed * 1.0F, globalHeight * 0.2F, 3, f, f1);
        model.chainSwing(tail, globalSpeed * 0.5F, globalHeight * 0.4F, 3, f, f1);

      //  model.chainWave(neck, globalSpeed * 1.0F, globalHeight * 0.1F, -3, f, f1);

        model.chainWave(armLeft, globalSpeed * 1.0F, globalHeight * 0.4F, -2, f, f1);
        model.chainWave(armRight, globalSpeed * 1.0F, globalHeight * 0.4F, -2, f, f1);

       // model.walk(thighLeft, 1.0F * globalSpeed, globalDegree * 0.8F, false, 0F, 0.4F, f, f1);
       // model.walk(calf1Left, 1.0F * globalSpeed, globalDegree * 0.5F, true, 1F, 0F, f, f1);
       // model.walk(calf2Left, 1.0F * globalSpeed, globalDegree * 0.5F, false, 0F, 0F, f, f1);
       // model.walk(footLeft, 1.0F * globalSpeed, globalDegree * 1.5F, true, 0.5F, 1F, f, f1);

       // model.walk(thighRight, 1.0F * globalSpeed, globalDegree * 0.8F, true, 0F, 0.4F, f, f1);
       // model.walk(calf1Right, 1.0F * globalSpeed, globalDegree * 0.5F, false, 1F, 0F, f, f1);
       // model.walk(calf2Right, 1.0F * globalSpeed, globalDegree * 0.5F, true, 0F, 0F, f, f1);
       // model.walk(footRight, 1.0F * globalSpeed, globalDegree * 1.5F, false, 0.5F, 1F, f, f1);

        model.chainWave(tail, globalSpeed * 0.5F, globalHeight * 0.8F, 3, ticks, 0.025F);
        model.chainWave(neck, globalSpeed * 0.5F, globalHeight * 0.4F, -3, ticks, 0.025F);
    }
}
