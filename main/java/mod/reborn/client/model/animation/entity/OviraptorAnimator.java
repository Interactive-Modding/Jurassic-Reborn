package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.OviraptorEntity;

@SideOnly(Side.CLIENT)
public class OviraptorAnimator extends EntityAnimator<OviraptorEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, OviraptorEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer body = model.getCube("Body ALL");
        AdvancedModelRenderer tail1 = model.getCube("Tail Base");
        AdvancedModelRenderer tail2 = model.getCube("Tail Mid 1");
        AdvancedModelRenderer tail3 = model.getCube("Tail Mid 2");
        AdvancedModelRenderer tail4 = model.getCube("Tail Tip");
        AdvancedModelRenderer leftArm1 = model.getCube("Arm Top Left");
        AdvancedModelRenderer leftArm2 = model.getCube("Arm Mid Left");
        AdvancedModelRenderer leftArm3 = model.getCube("Arm Feather Left");
        AdvancedModelRenderer rightArm1 = model.getCube("Arm Top Right");
        AdvancedModelRenderer rightArm2 = model.getCube("Arm Mid Right");
        AdvancedModelRenderer rightArm3 = model.getCube("Arm Feather Right");
        AdvancedModelRenderer leftLeg1 = model.getCube("Leg Top Left");
        AdvancedModelRenderer rightLeg1 = model.getCube("Leg Top Right");
        AdvancedModelRenderer neck1 = model.getCube("Neck Base");
        AdvancedModelRenderer head = model.getCube("Upper Head");

        AdvancedModelRenderer[] tail = {tail4, tail3, tail2, tail1};
        AdvancedModelRenderer[] leftArm = {leftArm3, leftArm2, leftArm1};
        AdvancedModelRenderer[] rightArm = {rightArm3, rightArm2, rightArm1};
        AdvancedModelRenderer[] mainBody = {body, neck1, head};

        float globalSpeed = 1.0F;
        float globalDegree = 1.0F;

        model.bob(body,  globalSpeed * 0.1F,  globalDegree * 0.3F, false, f,f1);
        model.walk(rightLeg1, globalSpeed * 0.5F, globalDegree * 1.0F, false, (float)Math.PI, 1, f, f1);
        model.walk(leftLeg1, globalSpeed * 0.5F, globalDegree * 1.0F, true, (float)Math.PI,1, f, f1);

        model.chainWave(tail, 0.13F,0.45F,Math.PI - 1, f + ticks, f1 + 0.25F);
        model.chainWave(mainBody, 0.1F, -0.1F, Math.PI + 2, ticks, 0.25F);
        model.chainWave(rightArm, 0.1F, -0.1F, Math.PI + 1, ticks, 0.25F);
        model.chainWave(leftArm, 0.1F, -0.1F, Math.PI + 1, ticks, 0.25F);

        model.faceTarget(rotationYaw, rotationPitch, 2.0F, neck1, head);
        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
