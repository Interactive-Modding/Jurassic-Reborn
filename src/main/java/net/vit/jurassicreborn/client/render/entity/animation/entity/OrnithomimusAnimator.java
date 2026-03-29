package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.OrnithomimusEntity;import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class OrnithomimusAnimator extends EntityAnimator<OrnithomimusEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, OrnithomimusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
        {
            float globalSpeed = 0.6F;
            float globalDegree = 1.0F;
            float globalHeight = 1.0F;

            AdvancedModelBox neck1 = model.getCube("neck1");
            AdvancedModelBox neck2 = model.getCube("neck2");
            AdvancedModelBox neck3 = model.getCube("neck3");
            AdvancedModelBox neck4 = model.getCube("neck4");
            AdvancedModelBox neck5 = model.getCube("neck5");

            AdvancedModelBox throat = model.getCube("Throat");

            AdvancedModelBox tail1 = model.getCube("tail1");
            AdvancedModelBox tail2 = model.getCube("tail2");
            AdvancedModelBox tail3 = model.getCube("tail3");
            AdvancedModelBox tail4 = model.getCube("tail4");
            AdvancedModelBox tail5 = model.getCube("tail5");
            AdvancedModelBox tail6 = model.getCube("tail6");

            AdvancedModelBox body1 = model.getCube("body1");
            AdvancedModelBox body2 = model.getCube("body2");
            AdvancedModelBox body3 = model.getCube("body3");

            AdvancedModelBox head = model.getCube("Head Base");

            AdvancedModelBox rightThigh = model.getCube("thigh1");
            AdvancedModelBox leftThigh = model.getCube("thigh2");

            AdvancedModelBox rightCalf1 = model.getCube("leg1");
            AdvancedModelBox leftCalf1 = model.getCube("leg2");

            AdvancedModelBox rightCalf2 = model.getCube("upperfoot1");
            AdvancedModelBox leftCalf2 = model.getCube("upperfoot2");

            AdvancedModelBox rightFoot = model.getCube("foot1");
            AdvancedModelBox leftFoot = model.getCube("foot2");

            AdvancedModelBox upperArmLeft = model.getCube("Arm UPPER Left");
            AdvancedModelBox upperArmRight = model.getCube("Arm UPPER Right");

            AdvancedModelBox lowerArmRight = model.getCube("Arm Mid Right");
            AdvancedModelBox lowerArmLeft = model.getCube("Arm Mid Left");

            AdvancedModelBox handRight = model.getCube("Hand RIGHT");
            AdvancedModelBox handLeft = model.getCube("Hand LEFT");

            AdvancedModelBox[] body = new AdvancedModelBox[]{head, neck5, neck4, neck3, neck2, neck1, body1, body2, body3};

            AdvancedModelBox[] tail = new AdvancedModelBox[]{tail6, tail5, tail4, tail3, tail2, tail1};

            AdvancedModelBox[] armLeft = new AdvancedModelBox[]{handLeft, lowerArmLeft, upperArmLeft};
            AdvancedModelBox[] armRight = new AdvancedModelBox[]{handRight, lowerArmRight, upperArmRight};

//        float delta = Minecraft.getMinecraft().getRenderPartialTicks();
//        LegArticulator.articulateBiped(entity, entity.legSolver, body1, leftThigh, leftCalf1, rightThigh, rightCalf1,1.0F, 1.4F, delta);

            model.bob(body1, globalSpeed * 0.5F, globalDegree * 0.01F, false, f, f1);
            model.bob(rightThigh, globalSpeed * 0.5F, globalDegree * 0.8F, false, f, f1);
            model.bob(leftThigh, globalSpeed * 0.5F, globalDegree * 0.8F, false, f, f1);

            model.chainWave(tail, globalSpeed * 0.25F, globalDegree * 0.05F, 1, f, f1);
            model.chainSwing(tail, globalSpeed * 0.25F, globalDegree * 0.2F, 2, f, f1);
            model.chainWave(body, globalSpeed * 0.25F, globalDegree * 0.025F, 3, f, f1);

            model.chainWave(tail, 0.1F, 0.05F, 1, ticks, 0.25F);
            model.chainWave(body, 0.1F, -0.05F, 4, ticks, 0.25F);
            model.chainWave(armRight, 0.1F, -0.15F, 4, ticks, 0.25F);
            model.chainWave(armLeft, 0.1F, -0.15F, 4, ticks, 0.25F);

            model.faceTarget(rotationYaw, rotationPitch, 1.0F, neck1, neck2, neck3, neck4, head);

            entity.tailBuffer.applyChainSwingBuffer(tail);
        }
    }
}