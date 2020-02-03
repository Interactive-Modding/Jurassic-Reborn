package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import mod.reborn.server.entity.animal.EntityCrab;
import mod.reborn.server.entity.animal.GoatEntity;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CrabAnimator extends EntityAnimator<EntityCrab> {
    @Override
    protected void performAnimations(AnimatableModel model, EntityCrab entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelRenderer head = model.getCube("bodyTop");
        AdvancedModelRenderer[] neck = new AdvancedModelRenderer[] { head };

        model.chainWave(neck, 0.125F, 1.0F, 3, ticks, 0.025F);
    }
}
