package mod.reborn.client.model.animation;

import mod.reborn.server.entity.GrowthStage;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.EnumMap;
import java.util.WeakHashMap;

@SideOnly(MixinEnvironment.Side.CLIENT)
public abstract class EntityAnimator<ENTITY extends LivingEntity & Animatable> implements ITabulaModelAnimator<ENTITY> {
    protected EnumMap<GrowthStage, Map<ENTITY, JabelarAnimationHandler<ENTITY>>> animationHandlers = new EnumMap<>(GrowthStage.class);

    private JabelarAnimationHandler<ENTITY> getAnimationHelper(ENTITY entity, AnimatableModel model, boolean useInertialTweens) {
        GrowthStage growth = entity.getGrowthStage();
        Map<ENTITY, JabelarAnimationHandler<ENTITY>> growthToRender = this.animationHandlers.get(growth);

        if (growthToRender == null) {
            growthToRender = new WeakHashMap<>();
            this.animationHandlers.put(growth, growthToRender);
        }

        JabelarAnimationHandler<ENTITY> render = growthToRender.get(entity);

        if (render == null) {
            render = entity.<ENTITY>getPoseHandler().createAnimationHandler(entity, model, growth, useInertialTweens);
            growthToRender.put(entity, render);
        }

        return render;
    }

    @Override
    public final void setRotationAngles(TabulaModel model, ENTITY entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        this.getAnimationHelper(entity, (AnimatableModel) model, entity.shouldUseInertia()).performAnimations(entity, limbSwing, limbSwingAmount, ticks);
        for(int i = 0;true;i++) {
            AdvancedModelRenderer cube = model.getCube("neck" + i++);
            if(cube == null) {
                cube = model.getCube("throat" + i++);
            }
            float j = 1 - (i * 0.00001F);
            if(cube != null ) {
                cube.scaleX *= j;
                cube.scaleY *= j;
                cube.scaleZ *= j;

            }
            break;
        }
        this.performAnimations((AnimatableModel) model, entity, limbSwing, limbSwingAmount, ticks, rotationYaw, rotationPitch, scale);
    }

    protected void performAnimations(AnimatableModel parModel, ENTITY entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
    }
}