package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.TroodonEntity;

@OnlyIn(Dist.CLIENT)
public class TroodonAnimator extends EntityAnimator<TroodonEntity> {

    @Override
    protected void performAnimations(AnimatableModel model,
                                     TroodonEntity entity,
                                     float limbSwing,
                                     float limbSwingAmount,
                                     float ticks,
                                     float rotationYaw,
                                     float rotationPitch,
                                     float scale) {

        AdvancedModelBox head   = model.getCube("Head");

        AdvancedModelBox neck1  = model.getCube("Neck1");
        AdvancedModelBox neck2  = model.getCube("Neck2");
        AdvancedModelBox neck3  = model.getCube("Neck3");
        AdvancedModelBox neck4  = model.getCube("Neck4");
        AdvancedModelBox neck5  = model.getCube("Neck5");

        AdvancedModelBox chest  = model.getCube("Chest");
        AdvancedModelBox rear   = model.getCube("Rear");

        AdvancedModelBox upperArmR = model.getCube("Arm2");
        AdvancedModelBox upperArmL = model.getCube("Arm");

        AdvancedModelBox thighL = model.getCube("Leg");
        AdvancedModelBox kneeL  = model.getCube("Knee");
        AdvancedModelBox ankleL = model.getCube("Ankle");
        AdvancedModelBox footL  = model.getCube("Foot");

        AdvancedModelBox thighR = model.getCube("Leg2");
        AdvancedModelBox kneeR  = model.getCube("Knee2");
        AdvancedModelBox ankleR = model.getCube("Ankle2");
        AdvancedModelBox footR  = model.getCube("Foot2");

        AdvancedModelBox tail2 = model.getCube("Tail2");
        AdvancedModelBox tail3 = model.getCube("Tail3");
        AdvancedModelBox tail4 = model.getCube("Tail4");
        AdvancedModelBox tail5 = model.getCube("Tail5");
        AdvancedModelBox tail6 = model.getCube("Tail6");
        AdvancedModelBox tail7 = model.getCube("Tail7");

        // chains
        AdvancedModelBox[] neckChain = new AdvancedModelBox[]{head, neck5, neck4, neck3, neck2, neck1};
        AdvancedModelBox[] tailChain = new AdvancedModelBox[]{tail7, tail6, tail5, tail4, tail3, tail2};
        AdvancedModelBox[] rightArm  = new AdvancedModelBox[]{upperArmR};
        AdvancedModelBox[] leftArm   = new AdvancedModelBox[]{upperArmL};

        // speeds
        float walkSpeed = 0.38F;
        float idleSpeed = 0.10F;
        float move = limbSwingAmount;

        boolean isMoving = move > 0.08F;

        if (isMoving) {
            model.bob(chest, walkSpeed * 0.5F, 0.25F * move, false, limbSwing, move);
            model.bob(rear,  walkSpeed * 0.5F, 0.20F * move, true,  limbSwing, move);
        } else {
            model.bob(chest, idleSpeed, 0.10F, false, ticks, 1.0F);
        }

        model.chainWave(neckChain, idleSpeed, 0.03F, 2, ticks, 1.0F);

        if (isMoving) {
            model.chainWave(neckChain, walkSpeed * 0.4F, -0.03F * move, 2, limbSwing, move);
        }

        model.chainSwing(leftArm,  walkSpeed * 0.75F, 0.15F * move, 0, limbSwing, move);
        model.chainSwing(rightArm, walkSpeed * 0.75F, 0.15F * move, (float)Math.PI, limbSwing, move);

        if (isMoving) {
            model.chainSwing(tailChain, walkSpeed * 0.55F, 0.35F * move, 2, limbSwing, move);
            model.chainWave (tailChain, walkSpeed * 0.40F, 0.18F * move, 2, limbSwing, move);
        } else {
            model.chainWave(tailChain, idleSpeed * 0.65F, 0.12F, 2, ticks, 1.0F);
        }
    }
}
