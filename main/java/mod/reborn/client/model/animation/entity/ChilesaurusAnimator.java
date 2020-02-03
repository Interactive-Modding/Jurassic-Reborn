package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import mod.reborn.server.entity.dinosaur.ChilesaurusEntity;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ChilesaurusAnimator extends EntityAnimator<ChilesaurusEntity> {

    protected void performAnimations(AnimatableModel model, ChilesaurusEntity entity,  float f, float f1, float ticks, float rotationYaw, float rotationPitch, float scale) {
        AdvancedModelRenderer neck1 = model.getCube("Neck BASE");
        AdvancedModelRenderer neck2 = model.getCube("Neck 2");
        AdvancedModelRenderer neck3 = model.getCube("Neck 3");

        AdvancedModelRenderer body1 = model.getCube("Body REAR");
        AdvancedModelRenderer body2 = model.getCube("Body MIDDLE");
        AdvancedModelRenderer body3 = model.getCube("Body FRONT");

        AdvancedModelRenderer head = model.getCube("Head ");

        AdvancedModelRenderer tail1 = model.getCube("Tail BASE");
        AdvancedModelRenderer tail2 = model.getCube("Tail 2");
        AdvancedModelRenderer tail3 = model.getCube("Tail 3");
        AdvancedModelRenderer tail4 = model.getCube("Tail 4");
        AdvancedModelRenderer tail5 = model.getCube("Tail 5");
        AdvancedModelRenderer tail6 = model.getCube("Tail 6");

        AdvancedModelRenderer[] body = new AdvancedModelRenderer[] { head, neck3, neck2, neck1, body1, body2, body3 };

        AdvancedModelRenderer[] tail = new AdvancedModelRenderer[] { tail6, tail5, tail4, tail3, tail2, tail1 };

        float globalSpeed = 1.5F;
        float globalDegree = 1.0F;


        model.bob(body1, globalSpeed * 0.5F, globalDegree * 1.5F, false, f, f1);

        model.chainWave(tail, globalSpeed * 0.25F, globalDegree * 0.05F, 1, f, f1);
        model.chainSwing(tail, globalSpeed * 0.25F, globalDegree * 0.2F, 2, f, f1);
        model.chainWave(body, globalSpeed * 0.25F, globalDegree * 0.025F, 3, f, f1);

        model.chainWave(tail, 0.1F, 0.05F, 1, ticks, 0.25F);
        model.chainWave(body, 0.1F, -0.05F, 4, ticks, 0.25F);
    }
}
