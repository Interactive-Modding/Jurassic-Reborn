package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.TriceratopsEntity;

@SideOnly(Side.CLIENT)
public class TriceratopsAnimator extends EntityAnimator<TriceratopsEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, TriceratopsEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelRenderer head = model.getCube("Head");
        AdvancedModelRenderer neck3 = model.getCube("Neck 3");
        AdvancedModelRenderer neck2 = model.getCube("Neck 2");
        AdvancedModelRenderer neck1 = model.getCube("Neck 1");
        AdvancedModelRenderer shoulders = model.getCube("Body shoulders");
        AdvancedModelRenderer main = model.getCube("Body MAIN");
        AdvancedModelRenderer waist = model.getCube("Body hips");
        AdvancedModelRenderer tail1 = model.getCube("Tail 1");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
        AdvancedModelRenderer tail6 = model.getCube("Tail 6");

        AdvancedModelRenderer frontLeftThigh = model.getCube("FrontLeg Upper Left");
        AdvancedModelRenderer frontLeftCalf = model.getCube("FrontLeg MID Left");
        AdvancedModelRenderer frontLeftFoot = model.getCube("FrontLeg FOOT Left");
        AdvancedModelRenderer frontRightThigh = model.getCube("FrontLeg Upper Right");
        AdvancedModelRenderer frontRightCalf = model.getCube("FrontLeg MID Right");
        AdvancedModelRenderer frontRightFoot = model.getCube("FrontLeg FOOT Right");
        AdvancedModelRenderer backLeftThigh = model.getCube("RearLeg Upper Left");
        AdvancedModelRenderer backRightThigh = model.getCube("RearLeg Upper Right");
        AdvancedModelRenderer backLeftCalf = model.getCube("RearLeg Middle Left");
        AdvancedModelRenderer backRightCalf = model.getCube("RearLeg Middle Right");

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelRenderer[] body = new AdvancedModelRenderer[] { head, neck3, neck2, neck1, shoulders, main, waist };

        float globalSpeed = 1.0F;
        float globalDegree = 0.8F;

        model.bob(waist, globalSpeed * 0.5F, globalDegree * 1.5F, false, f, f1);
        model.bob(backRightThigh, globalSpeed * 0.5F, globalDegree * 1.5F, false, f, f1);
        model.bob(backLeftThigh, globalSpeed * 0.5F, globalDegree * 1.5F, false, f, f1);

        model.chainWave(tail, globalSpeed * 0.25F, globalDegree * 0.1F, 1, f, f1);
        model.chainSwing(tail, globalSpeed * 0.25F, globalDegree * 0.25F, 2, f, f1);
        model.chainWave(body, globalSpeed * 0.25F, globalDegree * 0.05F, 3, f, f1);

        model.walk(neck1, 0.1F, 0.07F, false, -1F, 0F, ticks, 1F);
        model.walk(head, 0.1F, 0.07F, true, 0F, 0F, ticks, 1F);
        model.walk(waist, 0.1F, 0.025F, false, 0F, 0F, ticks, 1F);

        float inverseKinematicsConstant = 0.3F;
        model.walk(frontRightThigh, 0.1F, 0.1F * inverseKinematicsConstant, false, 0F, 0F, ticks, 0.25F);
        model.walk(frontRightCalf, 0.1F, 0.3F * inverseKinematicsConstant, true, 0F, 0F, ticks, 0.25F);
        model.walk(frontRightFoot, 0.1F, 0.175F * inverseKinematicsConstant, false, 0F, 0F, ticks, 0.25F);
        model.walk(frontLeftThigh, 0.1F, 0.1F * inverseKinematicsConstant, false, 0F, 0F, ticks, 0.25F);
        model.walk(frontLeftCalf, 0.1F, 0.3F * inverseKinematicsConstant, true, 0F, 0F, ticks, 0.25F);
        model.walk(frontLeftFoot, 0.1F, 0.175F * inverseKinematicsConstant, false, 0F, 0F, ticks, 0.25F);
        frontRightThigh.rotationPointZ -= 0.5 * Math.cos(ticks * 0.025F);
        frontLeftThigh.rotationPointZ -= 0.5 * Math.cos(ticks * 0.025F);

        float delta = Minecraft.getMinecraft().getRenderPartialTicks();
        LegArticulator.articulateQuadruped(entity, entity.legSolver, waist, neck1,
                backLeftThigh, backLeftCalf, backRightThigh, backRightCalf, frontLeftThigh, frontLeftCalf, frontRightThigh, frontRightCalf,
                0.5F, 0.8F, -0.6F, -1.1F,
                delta
        );

        model.chainSwing(tail, 0.1F, 0.05F, 2, ticks, 0.25F);
        model.chainWave(tail, 0.1F, -0.05F, 1, ticks, 0.25F);

        model.faceTarget(rotationYaw, rotationPitch, 1.0F, neck1, neck2, neck3, head);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
