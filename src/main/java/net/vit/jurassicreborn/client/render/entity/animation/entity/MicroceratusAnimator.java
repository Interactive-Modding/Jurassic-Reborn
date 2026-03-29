package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MicroceratusEntity;

@OnlyIn(Dist.CLIENT)
public class MicroceratusAnimator extends EntityAnimator<MicroceratusEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, MicroceratusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
        {
            AdvancedModelBox body = model.getCube("Body MAIN");

            AdvancedModelBox tail1 = model.getCube("Tail #1");
            AdvancedModelBox tail2 = model.getCube("Tail #2");
            AdvancedModelBox tail3 = model.getCube("Tail #3");
            AdvancedModelBox tail4 = model.getCube("Tail #4");
            AdvancedModelBox tail5 = model.getCube("Tail #5");
            AdvancedModelBox tail6 = model.getCube("Tail #6");

            AdvancedModelBox neck1 = model.getCube("Neck #1");

            AdvancedModelBox head = model.getCube("Head");

            AdvancedModelBox thighLeft = model.getCube("Leg Top LEFT");
            AdvancedModelBox thighRight = model.getCube("Leg Top RIGHT");

            AdvancedModelBox thighMidLeft = model.getCube("Leg Mid LEFT");
            AdvancedModelBox thighMidRight = model.getCube("Leg Mid RIGHT");

            AdvancedModelBox upperFootLeft = model.getCube("Leg Bot LEFT");
            AdvancedModelBox upperFootRight = model.getCube("Leg Bot RIGHT");

            AdvancedModelBox footLeft = model.getCube("Leg Foot LEFT");
            AdvancedModelBox footRight = model.getCube("Leg Foot RIGHT");

            AdvancedModelBox armTopLeft = model.getCube("Arm Top LEFT");
            AdvancedModelBox armTopRight = model.getCube("Arm Top RIGHT");

            AdvancedModelBox armMidLeft = model.getCube("Arm Mid LEFT");
            AdvancedModelBox armMidRight = model.getCube("Arm Mid RIGHT");

            AdvancedModelBox handLeft = model.getCube("Arm Hand LEFT");
            AdvancedModelBox handRight = model.getCube("Arm Hand RIGHT");

            AdvancedModelBox[] tail = new AdvancedModelBox[]{tail6, tail5, tail4, tail3, tail2, tail1};
            AdvancedModelBox[] neck = new AdvancedModelBox[]{head, neck1, body};

            AdvancedModelBox[] armLeft = new AdvancedModelBox[]{handLeft, armMidLeft, armTopLeft};
            AdvancedModelBox[] armRight = new AdvancedModelBox[]{handRight, armMidRight, armTopRight};


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
}
