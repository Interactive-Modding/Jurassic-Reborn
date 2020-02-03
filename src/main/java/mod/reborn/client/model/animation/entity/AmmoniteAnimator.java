package mod.reborn.client.model.animation.entity;

import mod.reborn.client.model.AnimatableModel;
import mod.reborn.client.model.animation.EntityAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import mod.reborn.server.entity.dinosaur.AmmoniteEntity;

public class AmmoniteAnimator extends EntityAnimator<AmmoniteEntity> {

    protected void performAnimations(AnimatableModel parModel, AmmoniteEntity entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {

        AdvancedModelRenderer tentacle1 = parModel.getCube("Tentacle 1 base");
        AdvancedModelRenderer tentacle2 = parModel.getCube("Tentacle 2 base");
        AdvancedModelRenderer tentacle3 = parModel.getCube("Tentacle 3 base");
        AdvancedModelRenderer tentacle4 = parModel.getCube("Tentacle 4 base");
        AdvancedModelRenderer tentacle5 = parModel.getCube("Tentacle 5 base");
        AdvancedModelRenderer tentacle6 = parModel.getCube("Tentacle 6 base");
        AdvancedModelRenderer tentacle7 = parModel.getCube("Tentacle 7 base");
        AdvancedModelRenderer tentacle8 = parModel.getCube("Tentacle 8 base");
        AdvancedModelRenderer[] tentacles = { tentacle8, tentacle7, tentacle6, tentacle5, tentacle4, tentacle3, tentacle2, tentacle1};

        parModel.chainWave(tentacles, entity.inWater()?0.15F:0.1F , entity.inWater()?0.6F:0.5F, 2, ticks, limbSwingAmount + 0.25F * (entity.inWater()?1.5F:1.0F));
    }
}
