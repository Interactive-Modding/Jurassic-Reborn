package net.vit.jurassicreborn.client.render.entity.animation;

import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.github.alexthe666.citadel.client.model.ITabulaModelAnimator;
import com.github.alexthe666.citadel.client.model.TabulaModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.common.entities.EntityUtils.Animatable;
import net.vit.jurassicreborn.common.entities.EntityUtils.GrowthStage;

import java.util.EnumMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Generic Tabula animator for entities.
 * Ported from LLibrary to Citadel (Minecraft 1.19.2).
 */
@OnlyIn(Dist.CLIENT)
public abstract class EntityAnimator<ENTITY extends LivingEntity & Animatable>
        implements ITabulaModelAnimator<ENTITY> {

    /** Animation handlers cached per growth-stage and per entity (weak refs). */
    protected final EnumMap<GrowthStage, Map<ENTITY, JabelarAnimationHandler<ENTITY>>> animationHandlers =
            new EnumMap<>(GrowthStage.class);

    /* ------------------------------------------------------------------ */
    /*  INTERNAL HELPERS                                                  */
    /* ------------------------------------------------------------------ */

    private JabelarAnimationHandler<ENTITY> getAnimationHelper(ENTITY entity,
                                                               AnimatableModel model,
                                                               boolean useInertialTweens) {

        GrowthStage stage = entity.getGrowthStage();
        Map<ENTITY, JabelarAnimationHandler<ENTITY>> map = this.animationHandlers.get(stage);

        if (map == null) {
            map = new WeakHashMap<>();
            this.animationHandlers.put(stage, map);
        }

        return map.computeIfAbsent(entity,
                e -> (JabelarAnimationHandler<ENTITY>) e.getPoseHandler().createAnimationHandler(e, model, stage, useInertialTweens));
    }

    /* ------------------------------------------------------------------ */
    /*  ITabulaModelAnimator                                              */
    /* ------------------------------------------------------------------ */

    @Override
    public final void setRotationAngles(TabulaModel model,
                                        ENTITY entity,
                                        float limbSwing,
                                        float limbSwingAmount,
                                        float ticks,
                                        float rotationYaw,
                                        float rotationPitch,
                                        float scale) {

        /* Always reset to bind-pose first to avoid compounding transforms. */
        model.resetToDefaultPose();

        /* Run core (pose-table) animations. */
        this.getAnimationHelper(entity, (AnimatableModel) model, entity.shouldUseInertia())
                .performAnimations(entity, limbSwing, limbSwingAmount, ticks);

        /* Example post-process: subtle taper along a segmented neck/throat. */
        for (int i = 1; ; i++) {
            AdvancedModelBox n = model.getCube("neck" + i);
            AdvancedModelBox t = model.getCube("throat" + i);

            if (n == null && t == null) break;

            // keep the effect imperceptible; or set j = 1.0F to disable taper entirely
            float j = 1.0F - (i * 0.00001F);

            if (n != null) n.setScale(j, j, j);
            if (t != null) t.setScale(j, j, j);

            if (n != null) n.setShouldScaleChildren(false);
            if (t != null) t.setShouldScaleChildren(false);
        }

        /* Hook for per-species / per-entity custom animation code. */
        this.performAnimations((AnimatableModel) model, entity,
                limbSwing, limbSwingAmount, ticks,
                rotationYaw, rotationPitch, scale);
    }

    /* ------------------------------------------------------------------ */
    /*  EXTENSION POINT                                                   */
    /* ------------------------------------------------------------------ */

    /**
     * Override to add species-specific, hand-written animation code
     * after the pose-table handler runs.
     */
    protected void performAnimations(AnimatableModel model, ENTITY entity,
                                     float limbSwing, float limbSwingAmount,
                                     float ticks, float rotationYaw,
                                     float rotationPitch, float scale) {
        /* default: no extra animation */
    }
}
