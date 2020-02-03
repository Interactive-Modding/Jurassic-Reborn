package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.BrachiosaurusEntity;

@SideOnly(Side.CLIENT)
public class BrachiosaurusAnimator extends EntityAnimator<BrachiosaurusEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, BrachiosaurusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelRenderer head = model.getCube("head");

        AdvancedModelRenderer neck1 = model.getCube("Neck 1");
        AdvancedModelRenderer neck2 = model.getCube("neck2");
        AdvancedModelRenderer neck3 = model.getCube("neck3");
        AdvancedModelRenderer neck4 = model.getCube("neck4");
        AdvancedModelRenderer neck5 = model.getCube("neck5");
        AdvancedModelRenderer neck6 = model.getCube("neck6");

        AdvancedModelRenderer neck7 = model.getCube("neck7");
        AdvancedModelRenderer tail1 = model.getCube("tail1");
        AdvancedModelRenderer tail2 = model.getCube("tail2");
        AdvancedModelRenderer tail3 = model.getCube("tail3");
        AdvancedModelRenderer tail4 = model.getCube("tail4");
        AdvancedModelRenderer tail5 = model.getCube("tail5");

        AdvancedModelRenderer[] neckParts = new AdvancedModelRenderer[] { head, neck7, neck6, neck5, neck4, neck3, neck2, neck1 };
        AdvancedModelRenderer[] tailParts = new AdvancedModelRenderer[] { tail5, tail4, tail3 };
        AdvancedModelRenderer[] tailParts2 = new AdvancedModelRenderer[] { tail5, tail4, tail3, tail2, tail1 };

        float delta = Minecraft.getMinecraft().getRenderPartialTicks();
        AdvancedModelRenderer root = model.getCube("hips");
        AdvancedModelRenderer backLeftThigh = model.getCube("top leg left");
        AdvancedModelRenderer backLeftCalf = model.getCube("bottom leg left");
        AdvancedModelRenderer backRightThigh = model.getCube("top leg right");
        AdvancedModelRenderer backRightCalf = model.getCube("bottom leg right");
        AdvancedModelRenderer frontLeftThigh = model.getCube("front left top leg");
        AdvancedModelRenderer frontLeftCalf = model.getCube("bottom front left leg");
        AdvancedModelRenderer frontRightThigh = model.getCube("front right top leg");
        AdvancedModelRenderer frontRightCalf = model.getCube("bottom front right leg");
        LegArticulator.articulateQuadruped(entity, entity.legSolver, root, neck1,
            backLeftThigh, backLeftCalf, backRightThigh, backRightCalf, frontLeftThigh, frontLeftCalf, frontRightThigh, frontRightCalf, 
            0.25F, 0.4F, -0.2F, -0.3F,
            delta
        );

        float globalSpeed = 0.4F;
        float globalHeight = 0.5F;

        model.chainWave(tailParts, globalSpeed * 0.25F, globalHeight * 1.0F, 3, ticks, 0.025F);
        model.chainWave(neckParts, globalSpeed * 0.25F, globalHeight * 0.25F, -3, ticks, 0.025F);

        entity.tailBuffer.applyChainSwingBuffer(tailParts2);
    }
}
