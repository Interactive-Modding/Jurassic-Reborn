package net.vit.jurassicreborn.client.render.entity.vehicle;

import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.model.TabulaModelUV;
import net.vit.jurassicreborn.common.entities.vehicle.HelicopterEntity;
import net.vit.jurassicreborn.common.legacy.tabula.TabulaModelHelper;
import org.jetbrains.annotations.Nullable;

public abstract class HelicopterRenderer<E extends HelicopterEntity> extends EntityRenderer<E> {

    private final String helicopterName;
    protected final ResourceLocation texture;
    protected final ResourceLocation positionLights;

    private static final ResourceLocation[] DESTROY_STAGES =
            java.util.stream.IntStream.range(0, 10)
                    .mapToObj(i -> new ResourceLocation("textures/block/destroy_stage_" + i + ".png"))
                    .toArray(ResourceLocation[]::new);

    protected TabulaModel baseModel;
    protected TabulaModel destroyModel;
    protected HelicopterAnimator animator;
    protected int passedRenderTicks = 0;

    public HelicopterRenderer(EntityRendererProvider.Context context, String helicopterName) {
        super(context);
        this.helicopterName = helicopterName;
        this.animator = createCarAnimator();
        this.texture = new ResourceLocation(JurassicReborn.MODID, "textures/entities/" + helicopterName + "/" + helicopterName + ".png");
        this.positionLights = new ResourceLocation(JurassicReborn.MODID, "textures/entities/" + helicopterName + "/" + helicopterName + "_position_lights.png");

        try {
            var container = TabulaModelHelper.loadTabulaModel(
                    new ResourceLocation(JurassicReborn.MODID, "models/entities/" + helicopterName + "/" + helicopterName)
            );
            this.baseModel = new TabulaModel(container, animator);
            this.destroyModel = new TabulaModel(new TabulaModelUV(container, 16, 16), animator);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load helicopter " + helicopterName, e);
        }
    }

    @Override
    public void render(E entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0, 1.25F, 0);

        // Quaternion-based rotation (replace Axis)
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(entity.pitch(partialTicks)));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.roll(partialTicks)));
        poseStack.scale(-1, -1, 1);

        // Render the main model
        renderModel(entity, poseStack, buffer, packedLight, partialTicks, false);

        // Render position lamp (example logic)
        renderPositionLamp(entity, poseStack, buffer, packedLight, partialTicks);

        // Render destroy texture overlay (example logic)
        renderDestroyTexture(entity, poseStack, buffer, packedLight, partialTicks);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    /**
     * Main helicopter model renderer.
     */
    protected void renderModel(E entity, PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                               float partialTicks, boolean destroy) {
        TabulaModel model = destroy ? destroyModel : baseModel;
        animator.setRotationAngles(model, entity, 0, 0,
                entity.tickCount + partialTicks, entity.getYRot(), entity.getXRot(), 0.0625F);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(getTextureLocation(entity)));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F);    }

    /**
     * Renders the blinking position lamp using the lamp texture.
     */
    protected void renderPositionLamp(E entity, PoseStack poseStack, MultiBufferSource buffer, int packedLight, float partialTicks) {
        // Lamp blinks when occupied or running
        if (entity.getControllingPassenger() != null || entity.getCurrentEngineSpeed() > 1) {
            if (entity.level.getGameTime() - this.passedRenderTicks > entity.getPositionLightFrequency() * 2) {
                this.passedRenderTicks = (int) entity.level.getGameTime();
            }
            if (entity.level.getGameTime() - this.passedRenderTicks <= entity.getPositionLightFrequency()) {
                VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(positionLights));
                baseModel.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY,
                        1.0F, 1.0F, 1.0F, 0.7F);
            }
        }
    }

    /**
     * Renders a damage overlay when the helicopter is damaged.
     */
    protected void renderDestroyTexture(E entity, PoseStack poseStack, MultiBufferSource buffer, int packedLight, float partialTicks) {
        int destroyStage = Math.min(10, (int) (10 - (entity.getHealth() / entity.getHealth()) * 10)) - 1;
        if (destroyStage >= 0) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            VertexConsumer consumer = buffer.getBuffer(RenderType.entityTranslucent(DESTROY_STAGES[destroyStage]));
            destroyModel.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY,
                    1.0F, 1.0F, 1.0F, 0.5F);
            RenderSystem.disableBlend();
        }
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(E entity) {
        return texture;
    }

    protected abstract HelicopterAnimator createCarAnimator();
}
