package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import mod.reborn.server.entity.dinosaur.ParaceratheriumEntity;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParaceratheriumAnimator extends EntityAnimator<ParaceratheriumEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, ParaceratheriumEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        float globalSpeed = 0.6F;
        float globalDegree = 1.0F;
        float globalHeight = 1.0F;


        AdvancedModelRenderer body1 = model.getCube("Body back");
        AdvancedModelRenderer[] body = new AdvancedModelRenderer[] { body1 };

        model.bob(body1, globalSpeed * 0.5F, globalDegree * 0.01F, false, f, f1);
        model.chainWave(body, globalSpeed * 0.25F, globalDegree * 0.025F, 3, f, f1);
        model.chainWave(body, 0.1F, -0.05F, 4, ticks, 0.25F);

    }
}