package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MamenchisaurusEntity;

@OnlyIn(Dist.CLIENT)
public class MamenchisaurusAnimator extends EntityAnimator<MamenchisaurusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, MamenchisaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelBox head = model.getCube("Head");

        AdvancedModelBox neck1 = model.getCube("neck1");
        AdvancedModelBox neck2 = model.getCube("neck2");
        AdvancedModelBox neck3 = model.getCube("neck3");
        AdvancedModelBox neck4 = model.getCube("neck4");
        AdvancedModelBox neck5 = model.getCube("neck5");
        AdvancedModelBox neck6 = model.getCube("neck6");
        AdvancedModelBox neck7 = model.getCube("neck7");
        AdvancedModelBox neck8 = model.getCube("neck8");
        AdvancedModelBox neck9 = model.getCube("neck9");
        AdvancedModelBox neck10 = model.getCube("neck10");
        AdvancedModelBox neck11 = model.getCube("neck11");

        AdvancedModelBox waist = model.getCube("hips");
        AdvancedModelBox tail1 = model.getCube("tail1");
        AdvancedModelBox tail2 = model.getCube("tail2");
        AdvancedModelBox tail3 = model.getCube("tail3");
        AdvancedModelBox tail4 = model.getCube("tail4");
        AdvancedModelBox tail5 = model.getCube("tail5");
        AdvancedModelBox tail6 = model.getCube("tail6");
        AdvancedModelBox tail7 = model.getCube("tail7");
        AdvancedModelBox tail8 = model.getCube("tail8");
        AdvancedModelBox tail9 = model.getCube("tail9");
        AdvancedModelBox tail10 = model.getCube("tail10");


        AdvancedModelBox lowerThighLeft = model.getCube("bottom front left leg");
        AdvancedModelBox lowerThighRight = model.getCube("bottom front right leg");

        AdvancedModelBox footLeft = model.getCube("left back foot");
        AdvancedModelBox footRight = model.getCube("left right foot");

        AdvancedModelBox armRight = model.getCube("front right top leg");
        AdvancedModelBox armLeft = model.getCube("front left top leg");

        AdvancedModelBox lowerArmRight = model.getCube("bottom front right leg");
        AdvancedModelBox lowerArmLeft = model.getCube("bottom front left leg");

        AdvancedModelBox handRight = model.getCube("front right foot");
        AdvancedModelBox handLeft = model.getCube("front left foot");

        AdvancedModelBox backLeftCalf = model.getCube("bottom leg left");
        AdvancedModelBox backLeftThigh = model.getCube("top leg left");

        AdvancedModelBox backRightThigh = model.getCube("top leg right");

        AdvancedModelBox backRightCalf = model.getCube("bottom leg right");

        AdvancedModelBox stomach = model.getCube("Stomach");
        AdvancedModelBox body = model.getCube("body");

        AdvancedModelBox[] neckParts = new AdvancedModelBox[] { head,  neck11, neck10, neck9, neck8, neck7,neck6, neck5, neck4, neck3, neck2, neck1, body };
        AdvancedModelBox[] tailParts = new AdvancedModelBox[] { tail10, tail9, tail8, tail7, tail6,tail5, tail4, tail3, tail2, tail1 };
        float delta = Minecraft.getInstance().getDeltaFrameTime();

        LegArticulator.articulateQuadruped(entity, entity.legSolver, waist, neck1,
                backLeftThigh, backLeftCalf, backRightThigh, backRightCalf, armLeft, lowerArmLeft, armRight, lowerArmRight,
                0.25F, 0.4F, -0.2F, -0.3F,
                delta
        );


        float globalSpeed = 0.5F;
        float globalHeight = 0.5F;
        float globalDegree = 0.5F;
        float frontOffset = 1.0F;
        float idleSpeed  = 0.08F;
        float idleDegree = 0.08F;
        model.bob(waist, globalSpeed * 1.0F, globalHeight * 4.0F, false, f, f1);
        model.bob(backLeftThigh, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);
        model.bob(backRightThigh, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);

        model.chainWave(neckParts, globalSpeed * 0.25F, globalHeight * 0.25F, -4, ticks, 0.025F);

        if (entity.isSleeping()) {
            model.chainSwing(tailParts, 0.15F, 0.015F, -2, ticks, 1.0F);
        } else {
            model.chainSwing(tailParts, idleSpeed, 0.17F, -2, ticks, 1.0F);
            if (f1 > 0.12F) {
                model.chainSwing(tailParts, 0.55F, 0.12F, -2, f, f1);
            }
        }
        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
