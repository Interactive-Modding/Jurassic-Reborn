package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.ApatosaurusEntity;

@SideOnly(Side.CLIENT)
public class ApatosaurusAnimator extends EntityAnimator<ApatosaurusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, ApatosaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer head = model.getCube("head");

        AdvancedModelRenderer neck1 = model.getCube("neck1");
        AdvancedModelRenderer neck2 = model.getCube("neck2");
        AdvancedModelRenderer neck3 = model.getCube("neck3");
        AdvancedModelRenderer neck4 = model.getCube("neck4");
        AdvancedModelRenderer neck5 = model.getCube("neck5");
        AdvancedModelRenderer neck6 = model.getCube("neck6");

        AdvancedModelRenderer waist = model.getCube("hips");
        AdvancedModelRenderer tail1 = model.getCube("tail1");
        AdvancedModelRenderer tail2 = model.getCube("tail2");
        AdvancedModelRenderer tail3 = model.getCube("tail3");
        AdvancedModelRenderer tail4 = model.getCube("tail4");
        AdvancedModelRenderer tail5 = model.getCube("tail5");

        AdvancedModelRenderer thighLeft = model.getCube("top leg left");
        AdvancedModelRenderer thighRight = model.getCube("top leg right");

        AdvancedModelRenderer lowerThighLeft = model.getCube("bottom front left leg");
        AdvancedModelRenderer lowerThighRight = model.getCube("bottom front right leg");

        AdvancedModelRenderer footLeft = model.getCube("left back foot");
        AdvancedModelRenderer footRight = model.getCube("right back foot");

        AdvancedModelRenderer armRight = model.getCube("front right top leg");
        AdvancedModelRenderer armLeft = model.getCube("front left top leg");

        AdvancedModelRenderer lowerArmRight = model.getCube("bottom front right leg");
        AdvancedModelRenderer lowerArmLeft = model.getCube("bottom front left leg");

        AdvancedModelRenderer handRight = model.getCube("front right foot");
        AdvancedModelRenderer handLeft = model.getCube("front left foot");

        AdvancedModelRenderer stomach = model.getCube("Stomach");
        AdvancedModelRenderer body = model.getCube("body");

        AdvancedModelRenderer[] neckParts = new AdvancedModelRenderer[] { head, neck6, neck5, neck4, neck3, neck2, neck1, body };
        AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[] { tail5, tail4, tail3, tail2, tail1 };

        float globalSpeed = 0.5F;
        float globalHeight = 0.5F;
        float globalDegree = 0.5F;

        float frontOffset = 1.0F;

        // f = ticks;
        // f1 = 0.4F;

        model.bob(body, globalSpeed * 1.0F, globalHeight * 4.0F, false, f, f1);
        model.bob(thighLeft, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);
        model.bob(thighRight, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);

        model.chainWave(tailParts, globalSpeed * 1.0F, globalHeight * 0.25F, 3, f, f1);
        model.chainSwing(tailParts, globalSpeed * 0.5F, globalHeight * 0.5F, 3, f, f1);
        model.chainWave(neckParts, globalSpeed * 1.0F, globalHeight * 0.125F, -4, f, f1);

        model.walk(thighLeft, 1F * globalSpeed, 0.7F * globalDegree, false, 0F, -0.4F, f, f1);
        model.walk(lowerThighLeft, 1F * globalSpeed, 0.6F * globalDegree, true, 1F, 0.5F, f, f1);

        model.walk(thighRight, 1F * globalSpeed, 0.7F * globalDegree, true, 0F, -0.4F, f, f1);
        model.walk(lowerThighRight, 1F * globalSpeed, 0.6F * globalDegree, false, 1F, 0.5F, f, f1);

        model.walk(armLeft, 1F * globalSpeed, 0.7F * globalDegree, true, frontOffset + 0F, -0.2F, f, f1);
        model.walk(lowerArmLeft, 1F * globalSpeed, 0.6F * globalDegree, true, frontOffset + 1F, -0.2F, f, f1);
        model.walk(handLeft, 1F * globalSpeed, 0.6F * globalDegree, false, frontOffset + 2F, 0.8F, f, f1);

        model.walk(armRight, 1F * globalSpeed, 0.7F * globalDegree, false, frontOffset + 0F, -0.2F, f, f1);
        model.walk(lowerArmRight, 1F * globalSpeed, 0.6F * globalDegree, false, frontOffset + 1F, -0.2F, f, f1);
        model.walk(handRight, 1F * globalSpeed, 0.6F * globalDegree, true, frontOffset + 2F, 0.8F, f, f1);

        model.chainWave(tailParts, globalSpeed * 0.25F, globalHeight * 2.0F, 3, ticks, 0.025F);
        model.chainSwing(tailParts, globalSpeed * 0.125F, globalHeight * 2.0F, 3, ticks, 0.025F);
        model.chainWave(neckParts, globalSpeed * 0.25F, globalHeight * 0.25F, -4, ticks, 0.025F);

        entity.tailBuffer.applyChainSwingBuffer(tailParts);
    }
}
