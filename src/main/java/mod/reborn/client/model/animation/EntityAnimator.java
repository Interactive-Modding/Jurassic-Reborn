package mod.reborn.client.model.animation;

import mod.reborn.client.model.AnimatableModel;
import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.api.Animatable;
import mod.reborn.server.entity.GrowthStage;

import java.util.EnumMap;
import java.util.Map;
import java.util.WeakHashMap;

@SideOnly(Side.CLIENT)
public abstract class EntityAnimator<ENTITY extends EntityLivingBase & Animatable> implements ITabulaModelAnimator<ENTITY> {
    protected EnumMap<GrowthStage, Map<ENTITY, JabelarAnimationHandler<ENTITY>>> animationHandlers = new EnumMap<>(GrowthStage.class);

    private JabelarAnimationHandler<ENTITY> getAnimationHelper(ENTITY entity, AnimatableModel model, boolean useInertialTweens) {
        GrowthStage growth = entity.getGrowthStage();
        return animationHandlers
                .computeIfAbsent(growth, k -> new WeakHashMap<>())
                .computeIfAbsent(entity, k -> entity.<ENTITY>getPoseHandler().createAnimationHandler(entity, model, growth, useInertialTweens));
    }

    @Override
    public final void setRotationAngles(TabulaModel model, ENTITY entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
        JabelarAnimationHandler<ENTITY> animationHandler = getAnimationHelper(entity, (AnimatableModel) model, entity.shouldUseInertia());
        animationHandler.performAnimations(entity, limbSwing, limbSwingAmount, ticks);

        // Iterate cubes to scale them
        for (int i = 0; ; i++) {
            AdvancedModelRenderer cube = model.getCube("neck" + i);
            if (cube == null) {
                cube = model.getCube("throat" + i);
            }
            if (cube == null) {
                break;
            }
            float j = 1 - (i * 0.00001F);
            cube.scaleX *= j;
            cube.scaleY *= j;
            cube.scaleZ *= j;
        }

        performAnimations((AnimatableModel) model, entity, limbSwing, limbSwingAmount, ticks, rotationYaw, rotationPitch, scale);
    }

    protected void performAnimations(AnimatableModel parModel, ENTITY entity, float limbSwing, float limbSwingAmount, float ticks, float rotationYaw, float rotationPitch, float scale) {
    }
}
