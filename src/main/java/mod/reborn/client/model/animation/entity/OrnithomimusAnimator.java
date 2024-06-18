package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.OrnithomimusEntity;

@SideOnly(Side.CLIENT)
public class OrnithomimusAnimator extends EntityAnimator<OrnithomimusEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, OrnithomimusEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        float globalSpeed = 0.6F;
        float globalDegree = 1.0F;
        float globalHeight = 1.0F;

        AdvancedModelRenderer neck1 = model.getCube("neck1");
        AdvancedModelRenderer neck2 = model.getCube("neck2");
        AdvancedModelRenderer neck3 = model.getCube("neck3");
        AdvancedModelRenderer neck4 = model.getCube("neck4");
        AdvancedModelRenderer neck5 = model.getCube("neck5");

        AdvancedModelRenderer throat = model.getCube("Throat");

        AdvancedModelRenderer tail1 = model.getCube("tail1");
        AdvancedModelRenderer tail2 = model.getCube("tail2");
        AdvancedModelRenderer tail3 = model.getCube("tail3");
        AdvancedModelRenderer tail4 = model.getCube("tail4");
        AdvancedModelRenderer tail5 = model.getCube("tail5");
        AdvancedModelRenderer tail6 = model.getCube("tail6");

        AdvancedModelRenderer body1 = model.getCube("body1");
        AdvancedModelRenderer body2 = model.getCube("body2");
        AdvancedModelRenderer body3 = model.getCube("body3");

        AdvancedModelRenderer head = model.getCube("Head Base");

        AdvancedModelRenderer rightThigh = model.getCube("thigh1");
        AdvancedModelRenderer leftThigh = model.getCube("thigh2");

        AdvancedModelRenderer rightCalf1 = model.getCube("leg1");
        AdvancedModelRenderer leftCalf1 = model.getCube("leg2");

        AdvancedModelRenderer rightCalf2 = model.getCube("upperfoot1");
        AdvancedModelRenderer leftCalf2 = model.getCube("upperfoot2");

        AdvancedModelRenderer rightFoot = model.getCube("foot1");
        AdvancedModelRenderer leftFoot = model.getCube("foot2");

        AdvancedModelRenderer upperArmLeft = model.getCube("Arm UPPER Left");
        AdvancedModelRenderer upperArmRight = model.getCube("Arm UPPER Right");

        AdvancedModelRenderer lowerArmRight = model.getCube("Arm Mid Right");
        AdvancedModelRenderer lowerArmLeft = model.getCube("Arm Mid Left");

        AdvancedModelRenderer handRight = model.getCube("Hand RIGHT");
        AdvancedModelRenderer handLeft = model.getCube("Hand LEFT");

        AdvancedModelRenderer[] body = new AdvancedModelRenderer[] { head, neck5, neck4, neck3, neck2, neck1, body1, body2, body3 };

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };

        AdvancedModelRenderer[] armLeft = new AdvancedModelRenderer[] { handLeft, lowerArmLeft, upperArmLeft };
        AdvancedModelRenderer[] armRight = new AdvancedModelRenderer[] { handRight, lowerArmRight, upperArmRight };

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