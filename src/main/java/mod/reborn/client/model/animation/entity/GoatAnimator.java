package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.entity.animal.GoatEntity;

@SideOnly(Side.CLIENT)
public class GoatAnimator extends EntityAnimator<GoatEntity> {
    @Override
    protected void performAnimations(AnimatableModel model, GoatEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelRenderer neck1 = model.getCube("Neck base");
        AdvancedModelRenderer neck2 = model.getCube("Throat");
        AdvancedModelRenderer head = model.getCube("Head lower");
        AdvancedModelRenderer[] neck = new AdvancedModelRenderer[] { head, neck2, neck1 };

        model.chainWave(neck, 0.125F, 1.0F, 3, ticks, 0.025F);
        model.faceTarget(rotationYaw, rotationPitch, 1.0F, head);
    }
}
