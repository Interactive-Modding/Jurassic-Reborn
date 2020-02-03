package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.GallimimusEntity;

@SideOnly(Side.CLIENT)
public class GallimimusAnimator extends EntityAnimator<GallimimusEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, GallimimusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelRenderer neck1 = model.getCube("Neck part 1");
        AdvancedModelRenderer neck2 = model.getCube("Neck part 2");
        AdvancedModelRenderer neck3 = model.getCube("Neck part 3");
        AdvancedModelRenderer neck4 = model.getCube("Neck part 4");
        AdvancedModelRenderer neck5 = model.getCube("Neck part 5");

        AdvancedModelRenderer tail1 = model.getCube("Tail part 1");
        AdvancedModelRenderer tail2 = model.getCube("Tail part 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail part 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail part 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail part 5");
        AdvancedModelRenderer tail6 = model.getCube("Tail part 6");

        AdvancedModelRenderer body1 = model.getCube("Body 1");
        AdvancedModelRenderer body2 = model.getCube("Body 2");
        AdvancedModelRenderer body3 = model.getCube("Body 3");

        AdvancedModelRenderer head = model.getCube("Head");

        AdvancedModelRenderer upperArmLeft = model.getCube("Left upper arm");
        AdvancedModelRenderer upperArmRight = model.getCube("Right upper arm");

        AdvancedModelRenderer lowerArmRight = model.getCube("Right lower arm");
        AdvancedModelRenderer lowerArmLeft = model.getCube("Left lower arm");

        AdvancedModelRenderer handRight = model.getCube("Right wrist");
        AdvancedModelRenderer handLeft = model.getCube("Left wrist");

        AdvancedModelRenderer leftThigh = model.getCube("Left thigh");
        AdvancedModelRenderer rightThigh = model.getCube("Right thigh");

        AdvancedModelRenderer leftCalf = model.getCube("Left shin");
        AdvancedModelRenderer rightCalf = model.getCube("Right shin");

        AdvancedModelRenderer[] body = new AdvancedModelRenderer[] { head, neck5, neck4, neck3, neck2, neck1, body1, body2, body3 };

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };

        AdvancedModelRenderer[] armLeft = new AdvancedModelRenderer[] { handLeft, lowerArmLeft, upperArmLeft };
        AdvancedModelRenderer[] armRight = new AdvancedModelRenderer[] { handRight, lowerArmRight, upperArmRight };

        float delta = Minecraft.getMinecraft().getRenderPartialTicks();
        LegArticulator.articulateBiped(entity, entity.legSolver, body1, leftThigh, leftCalf, rightThigh, rightCalf, 1.0F, 1.4F, delta);

        float globalSpeed = 1.5F;
        float globalDegree = 1.0F;

        model.bob(body1, globalSpeed * 0.5F, globalDegree * 1.5F, false, f, f1);
        model.bob(rightThigh, globalSpeed * 0.5F, globalDegree * 1.5F, false, f, f1);
        model.bob(leftThigh, globalSpeed * 0.5F, globalDegree * 1.5F, false, f, f1);

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
