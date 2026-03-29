package net.vit.jurassicreborn.client.render.entity.vehicle;

import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.citadel.client.model.container.TabulaModelContainer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.model.TabulaModelUV;
import net.vit.jurassicreborn.common.entities.vehicle.HelicopterEntity;
import net.vit.jurassicreborn.common.entities.vehicle.VehicleEntity;
import net.vit.jurassicreborn.common.legacy.tabula.TabulaModelHelper;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

public abstract class HelicopterRenderer<E extends HelicopterEntity> extends EntityRenderer<E> {
    private static final ResourceLocation[] DESTROY_STAGES =
            IntStream.range(0, 10)
                    .mapToObj(i -> ResourceLocation.withDefaultNamespace("textures/block/destroy_stage_" + i + ".png"))
                    .toArray(ResourceLocation[]::new);

    protected final String helicopterName;
    protected final ResourceLocation texture;
    protected final ResourceLocation positionLights;

    protected final TabulaModel baseModel;
    protected final TabulaModel destroyModel;
    protected final HelicopterAnimator animator;

    protected int passedRenderTicks = 0;

    protected HelicopterRenderer(EntityRendererProvider.Context context, String helicopterName) {
        super(context);
        this.helicopterName = helicopterName;
        this.animator = createCarAnimator();
        this.texture = ResourceLocation.fromNamespaceAndPath(
                JurassicReborn.MODID,
                "textures/entities/" + helicopterName + "/" + helicopterName + ".png"
        );
        this.positionLights = ResourceLocation.fromNamespaceAndPath(
                JurassicReborn.MODID,
                "textures/entities/" + helicopterName + "/" + helicopterName + "_position_lights.png"
        );

        try {
            TabulaModelContainer container = TabulaModelHelper.loadTabulaModel(
                    ResourceLocation.fromNamespaceAndPath(
                            JurassicReborn.MODID,
                            "models/entities/" + helicopterName + "/" + helicopterName
                    )
            );

            this.baseModel = new TabulaModel(container, this.animator);
            this.destroyModel = new TabulaModel(new TabulaModelUV(container, 16, 16), this.animator);
        } catch (Exception e) {
            throw new RuntimeException("Unable to load helicopter " + helicopterName, e);
        }
    }

    @Override
    public void render(E entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0F, 1.25F, 0.0F);

        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entityYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(entity.pitch(partialTicks)));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.roll(partialTicks)));

        // Tabula handedness
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        // Base helicopter model
        this.renderPass(
                this.baseModel,
                entity,
                partialTicks,
                poseStack,
                buffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(entity))),
                packedLight
        );

        // Blinking position lights
        this.renderPositionLamp(entity, poseStack, buffer, partialTicks);

        // Damage overlay
        this.renderDestroyTexture(entity, poseStack, buffer, partialTicks, packedLight);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    protected void renderPass(TabulaModel model,
                              E entity,
                              float partialTicks,
                              PoseStack poseStack,
                              VertexConsumer consumer,
                              int packedLight) {
        this.animator.setRotationAngles(
                model,
                entity,
                0.0F,
                0.0F,
                entity.tickCount + partialTicks,
                entity.getYRot(),
                entity.getXRot(),
                0.0625F
        );

        model.renderToBuffer(
                poseStack,
                consumer,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                0xFFFFFFFF
        );
    }

    protected void renderPositionLamp(E entity,
                                      PoseStack poseStack,
                                      MultiBufferSource buffer,
                                      float partialTicks) {
        if (entity.getControllingPassenger() == null && entity.getCurrentEngineSpeed() <= 1) {
            return;
        }

        long gameTime = entity.level().getGameTime();
        int frequency = entity.getPositionLightFrequency();

        if (gameTime - this.passedRenderTicks > (long) frequency * 2L) {
            this.passedRenderTicks = (int) gameTime;
        }

        if (gameTime - this.passedRenderTicks <= frequency) {
            this.renderPass(
                    this.baseModel,
                    entity,
                    partialTicks,
                    poseStack,
                    buffer.getBuffer(RenderType.entityTranslucent(this.positionLights)),
                    LightTexture.FULL_BRIGHT
            );
        }
    }

    protected void renderDestroyTexture(E entity,
                                        PoseStack poseStack,
                                        MultiBufferSource buffer,
                                        float partialTicks,
                                        int packedLight) {
        int destroyStage = Math.min(
                10,
                (int) (10.0F - (entity.getHealth() / VehicleEntity.MAX_HEALTH) * 10.0F)
        ) - 1;

        if (destroyStage < 0) {
            return;
        }

        this.renderPass(
                this.destroyModel,
                entity,
                partialTicks,
                poseStack,
                buffer.getBuffer(RenderType.crumbling(DESTROY_STAGES[destroyStage])),
                packedLight
        );
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(E entity) {
        return this.texture;
    }

    protected abstract HelicopterAnimator createCarAnimator();
}