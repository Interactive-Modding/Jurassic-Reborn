package net.vit.jurassicreborn.client.render.entity.animation.entity;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.PerisphinctesEntity;

public class PerisphinctesAnimator extends EntityAnimator<PerisphinctesEntity> {

    @Override
    protected void performAnimations(AnimatableModel model, PerisphinctesEntity entity,
                                     float limbSwing, float limbSwingAmount,
                                     float ticks, float rotationYaw,
                                     float rotationPitch, float scale) {

        AdvancedModelBox tentacle1 = model.getCube("Tentacle 1 base");
        AdvancedModelBox tentacle2 = model.getCube("Tentacle 2 base");
        AdvancedModelBox tentacle3 = model.getCube("Tentacle 3 base");
        AdvancedModelBox tentacle4 = model.getCube("Tentacle 4 base");
        AdvancedModelBox tentacle5 = model.getCube("Tentacle 5 base");
        AdvancedModelBox tentacle6 = model.getCube("Tentacle 6 base");
        AdvancedModelBox tentacle7 = model.getCube("Tentacle 7 base");
        AdvancedModelBox tentacle8 = model.getCube("Tentacle 8 base");

        // Put them in an array from tip to base or vice versa,
        AdvancedModelBox[] tentacles = {
                tentacle8, tentacle7, tentacle6, tentacle5,
                tentacle4, tentacle3, tentacle2, tentacle1
        };

        // Here we do a chainWave motion for the tentacles. The .chainWave()
        // method is from Citadel/LLibrary style code in AnimatableModel.
        // Adjust the frequency/amplitude as desired.
        // e.g. chainWave(parts, speed, degree, offset, ticks, distance)
        // entity.inWater() check can let us do bigger, smoother waves in water.
        if (entity.inWater()) {
            model.chainWave(tentacles, 0.15F, 0.6F, 2, ticks, limbSwingAmount + 0.25F * 1.5F);
        } else {
            // On land, do a more subtle wave
            model.chainWave(tentacles, 0.1F, 0.5F, 2, ticks, limbSwingAmount + 0.25F);
        }

    /*
      If you want to do something more advanced – for example,
      distinct logic for each type of animation (like your "ON_LAND",
      "INJURED", or "EATING" from perisphinctes_adult.json) – you can check
      the entity's current animation as follows:

        EntityAnimation anim = EntityAnimation.getAnimation(entity.getAnimation());
        if (anim == EntityAnimation.ON_LAND) {
            // Possibly chainSwing() or do smaller wave or
            // anything specific for "ON_LAND" pose
        } else if (anim == EntityAnimation.SWIMMING) {
            // Keep big wave, etc.
        }

      Remember that these pose transitions (e.g. from "IDLE" to
      "ON_LAND") are already being handled by the PoseHandler/JabelarAnimationHandler.
      Here, in performAnimations(), you can layer additional motion
      on top of the pose-based transformations.
    */
    }
}