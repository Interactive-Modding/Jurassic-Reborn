package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.LeptictidiumEntity;

@SideOnly(Side.CLIENT)
public class LeptictidiumAnimator extends EntityAnimator<LeptictidiumEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, LeptictidiumEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer body1 = model.getCube("Body FRONT");
        AdvancedModelRenderer body2 = model.getCube("Body REAR");
        AdvancedModelRenderer tail1 = model.getCube("Tail BASE");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
        AdvancedModelRenderer leftArm1 = model.getCube("Arm UPPER LEFT");
        AdvancedModelRenderer leftArm2 = model.getCube("Arm MID LEFT");
        AdvancedModelRenderer rightArm1 = model.getCube("Arm UPPER RIGHT");
        AdvancedModelRenderer rightArm2 = model.getCube("Arm MID RIGHT");
        AdvancedModelRenderer leftLeg1 = model.getCube("Leg UPPER LEFT");
        AdvancedModelRenderer rightLeg1 = model.getCube("Leg UPPER RIGHT");
        AdvancedModelRenderer neck1 = model.getCube("Neck BASE");
        AdvancedModelRenderer head = model.getCube("Head");

        AdvancedModelRenderer[] tail = { tail1, tail2};
        AdvancedModelRenderer[] leftArm = {leftArm2, leftArm1};
        AdvancedModelRenderer[] rightArm = {rightArm2, rightArm1};
        AdvancedModelRenderer[] body = {body2, body1, neck1, head};

        float globalSpeed = 1.0F;
        float globalDegree = 1.0F;

        model.bob(body1,  globalSpeed * 0.1F,  globalDegree * 0.3F, false, f,f1);
       // model.walk(rightLeg1, globalSpeed * 0.9F, globalDegree * 0.3F, false, (float)Math.PI, 1, f, f1);
      //  model.walk(leftLeg1, globalSpeed * 0.9F, globalDegree * 0.3F, true, (float)Math.PI,1, f, f1);

       // model.chainWave(tail, 0.1F,0.25F,Math.PI -1, f + ticks, f1 + 0.05F);
       // model.chainWave(body, 0.1F, -0.1F, Math.PI + 2, ticks, 0.25F);
       // model.chainWave(rightArm, 0.1F, -0.1F, Math.PI + 1, ticks, 0.25F);
       // model.chainWave(leftArm, 0.1F, -0.1F, Math.PI + 1, ticks, 0.25F);

        model.faceTarget(rotationYaw, rotationPitch, 2.0F, neck1, head);
        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
