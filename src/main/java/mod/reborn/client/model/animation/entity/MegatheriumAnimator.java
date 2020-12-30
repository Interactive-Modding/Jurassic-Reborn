package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import mod.reborn.server.entity.dinosaur.ArsinoitheriumEntity;
import mod.reborn.server.entity.dinosaur.MammothEntity;
import mod.reborn.server.entity.dinosaur.MegatheriumEntity;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MegatheriumAnimator extends EntityAnimator<MegatheriumEntity>
{
    @Override
    protected void performAnimations(AnimatableModel model, MegatheriumEntity entity, float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale)
    {
        AdvancedModelRenderer neck1 = model.getCube("neck1");
        AdvancedModelRenderer neck2 = model.getCube("neck2");
        AdvancedModelRenderer neck3 = model.getCube("neck3");
        AdvancedModelRenderer neck4 = model.getCube("neck4");
        AdvancedModelRenderer throat3 = model.getCube("throat3");
        AdvancedModelRenderer throat4 = model.getCube("throat4");

        AdvancedModelRenderer[] bodyParts = new AdvancedModelRenderer[] { throat4,throat3,neck1,neck2,neck3,neck4 };
    }
}
