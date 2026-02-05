package net.vit.jurassicreborn.client.render.entity.animation;


import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.common.entities.EntityUtils.Animatable;
import net.vit.jurassicreborn.common.legacy.tabula.TabulaModelHelper;

import java.util.Map;

/**
 * @author jabelar
 *         This class handles per-entity animations.
 */

@OnlyIn(Dist.CLIENT)
public class JabelarAnimationHandler<ENTITY extends LivingEntity & Animatable> {

    private final AnimationPass DEFAULT_PASS;
    private final AnimationPass MOVEMENT_PASS;
    private final AnimationPass ON_LAND_PASS;

    public JabelarAnimationHandler(ENTITY entity, AnimatableModel model, PosedCuboid[][] poses, Map<Animation, float[][]> poseSequences, boolean useInertialTweens) {
        this.DEFAULT_PASS = new AnimationPass(poseSequences, poses, useInertialTweens);
        this.MOVEMENT_PASS = new MovementAnimationPass(poseSequences, poses, useInertialTweens);
        this.ON_LAND_PASS = new OnLandAnimationPass(poseSequences, poses, useInertialTweens);

        this.init(entity, model);
    }

    public static AnimatableModel loadModel(String model) {
        try {
            return new AnimatableModel(TabulaModelHelper.loadTabulaModel(model), null);
        } catch (Exception e) {
            JurassicReborn.getLogger().error("Could not load Tabula model " + model, e);
        }
        return null;
    }

    private void init(ENTITY entity, AnimatableModel model) {
        AdvancedModelBox[] parts = this.getParts(model);
        this.DEFAULT_PASS.init(parts, entity);
        this.MOVEMENT_PASS.init(parts, entity);
        if (entity.isMarineCreature()) {
            this.ON_LAND_PASS.init(parts, entity);
        }
    }

    public void performAnimations(ENTITY entity, float limbSwing, float limbSwingAmount, float ticks) {
        this.DEFAULT_PASS.performAnimations(entity, limbSwing, limbSwingAmount, ticks);
        if (!entity.isCarcass()) {
            this.MOVEMENT_PASS.performAnimations(entity, limbSwing, limbSwingAmount, ticks);
            if (entity.isMarineCreature()) {
                this.ON_LAND_PASS.performAnimations(entity, limbSwing, limbSwingAmount, ticks);
            }
        }
    }

    private AdvancedModelBox[] getParts(AnimatableModel model) {
        AdvancedModelBox[] parts = new AdvancedModelBox[model.getIdentifierCubes().size()];
        int i = 0;
        for (Map.Entry<String, AdvancedModelBox> part : model.getIdentifierCubes().entrySet()) {
            parts[i++] = part.getValue();
        }
        return parts;
    }
}
