package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MajungasaurusEntity;

@OnlyIn(Dist.CLIENT)
public class MajungasaurusAnimator extends EntityAnimator<MajungasaurusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, MajungasaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelBox tail1 = model.getCube("tail");
        AdvancedModelBox tail2 = model.getCube("tail2");
        AdvancedModelBox tail3 = model.getCube("tail3");
        AdvancedModelBox tail4 = model.getCube("tail4");
        AdvancedModelBox tail5 = model.getCube("tail5");
        AdvancedModelBox tail6 = model.getCube("tail6");
        AdvancedModelBox tail7 = model.getCube("tail7");
        AdvancedModelBox tail8 = model.getCube("tail8");

        AdvancedModelBox bodyRear = model.getCube("bodyhips");
        AdvancedModelBox bodyMid = model.getCube("body");
        AdvancedModelBox bodyFront = model.getCube("shoulder");

        AdvancedModelBox neck1 = model.getCube("neck");
        AdvancedModelBox neck2 = model.getCube("neck2");
        AdvancedModelBox neck3 = model.getCube("neck3");
        AdvancedModelBox neck4 = model.getCube("neck4");

        AdvancedModelBox head = model.getCube("head");


        AdvancedModelBox upperArmRight = model.getCube("rbicep");
        AdvancedModelBox upperArmLeft = model.getCube("lbicep");

        AdvancedModelBox lowerArmRight = model.getCube("rarm");
        AdvancedModelBox lowerArmLeft = model.getCube("larm");

        AdvancedModelBox handRight = model.getCube("rlowerfinger");
        AdvancedModelBox handLeft = model.getCube("llowerfinger");

        AdvancedModelBox[] tail = new AdvancedModelBox[] { tail1, tail2, tail3, tail4, tail5, tail6 };

        AdvancedModelBox[] armLeft = new AdvancedModelBox[] { upperArmLeft, lowerArmLeft, handLeft };
        AdvancedModelBox[] armRight = new AdvancedModelBox[] { upperArmRight, lowerArmRight, handRight };

        AdvancedModelBox[] body = new AdvancedModelBox[] { bodyRear, bodyMid, bodyFront, neck1, neck2, neck3, neck4, head };

        float globalSpeed = 0.5F;
        float globalHeight = 1.0F;

        model.bob(bodyRear, globalSpeed * 1F, globalHeight * 0.8F, false, f, f1);

//        model.chainWave(body, globalSpeed * 1F, globalHeight * -0.02F, -3, f, f1);
        model.chainWave(tail, globalSpeed * 1F, globalHeight * 0.05F, -2, f, f1);

        model.chainWave(armRight, globalSpeed * 1F, globalHeight * -0.25F, -3, f, f1);
        model.chainWave(armLeft, globalSpeed * 1F, globalHeight * -0.25F, -3, f, f1);

        model.chainWave(tail, 0.1F, 0.05F, -2, ticks, 0.25F);
        model.chainWave(body, 0.1F, 0.03F, -5, ticks, 0.25F);
        model.chainWave(armRight, 0.1F, 0.1F, -4, ticks, 0.25F);
        model.chainWave(armLeft, 0.1F, 0.1F, -4, ticks, 0.25F);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
