package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.dinosaur.ProtoceratopsEntity;

@SideOnly(Side.CLIENT)
public class ProtoceratopsAnimator extends EntityAnimator<ProtoceratopsEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, ProtoceratopsEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        float globalSpeed = 0.5F;
        float globalHeight = 0.5F;

        AdvancedModelRenderer head = model.getCube("Head1");
        AdvancedModelRenderer neck1 = model.getCube("Neck");
        AdvancedModelRenderer body = model.getCube("Hips");

        AdvancedModelRenderer tail1 = model.getCube("Tail1");
        AdvancedModelRenderer tail2 = model.getCube("Tail2");
        AdvancedModelRenderer tail3 = model.getCube("Tail3");
        AdvancedModelRenderer tail4 = model.getCube("Tail4");
        AdvancedModelRenderer tail5 = model.getCube("Tail5");

        AdvancedModelRenderer thighLeft = model.getCube("legThighLeft");
        AdvancedModelRenderer thighRight = model.getCube("legThighRight");

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail5, tail4, tail3, tail2, tail1 };
        AdvancedModelRenderer[] neck = new AdvancedModelRenderer[] { head,  neck1 };

        model.bob(body, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);
        model.bob(thighLeft, globalSpeed * 1.0F, globalHeight * 0.9F, false, f, f1);
        model.bob(thighRight, globalSpeed * 1.0F, globalHeight * 1.0F, false, f, f1);

        model.chainWave(tail, globalSpeed * 1.0F, globalHeight * 0.25F, 3, f, f1);
        model.chainSwing(tail, globalSpeed * 0.5F, globalHeight * 0.25F, 3, f, f1);
        model.chainWave(neck, globalSpeed * 1.0F, globalHeight * 0.25F, -3, f, f1);

        model.chainWave(tail, globalSpeed * 0.25F, globalHeight * 1.0F, 3, ticks, 0.025F);
        model.chainWave(neck, globalSpeed * 0.25F, globalHeight * 1.0F, -3, ticks, 0.025F);

        entity.tailBuffer.applyChainSwingBuffer(tail);
    }
}
