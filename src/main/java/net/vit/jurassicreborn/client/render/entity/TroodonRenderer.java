package net.vit.jurassicreborn.client.render.entity;

import com.github.alexthe666.citadel.client.model.basic.BasicEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.render.entity.DinosaurRenderInfo;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.GrowthStage;

import java.util.Locale;

public class TroodonRenderer extends DinosaurRenderer {
    public TroodonRenderer(EntityRendererProvider.Context context,
                           BasicEntityModel<DinosaurEntity> model,
                           float shadowSize,
                           DinosaurRenderInfo info) {
        super(context, model, shadowSize, info);
        this.addLayer(new TroodonGlowLayer(this));
    }

    private static class TroodonGlowLayer extends RenderLayer<DinosaurEntity, BasicEntityModel<DinosaurEntity>> {
        TroodonGlowLayer(RenderLayerParent<DinosaurEntity, BasicEntityModel<DinosaurEntity>> renderer) {
            super(renderer);
        }

        @Override
        public void render(PoseStack poseStack,
                           MultiBufferSource buffer,
                           int packedLight,
                           DinosaurEntity entity,
                           float limbSwing,
                           float limbSwingAmount,
                           float partialTicks,
                           float ageInTicks,
                           float netHeadYaw,
                           float headPitch) {
            if (entity.isInvisible() || entity.isSkeleton() || entity.areEyelidsClosed()) {
                return;
            }

            ResourceLocation eyesTexture = resolveEyesTexture(entity);
            if (eyesTexture == null) {
                return;
            }

            VertexConsumer consumer = buffer.getBuffer(RenderType.eyes(eyesTexture));
            this.getParentModel().renderToBuffer(poseStack,
                    consumer,
                    LightTexture.FULL_BRIGHT,
                    LivingEntityRenderer.getOverlayCoords(entity, 0.0F),
                    0xFFFFFFFF
            );
        }

        private static ResourceLocation resolveEyesTexture(DinosaurEntity entity) {
            Dinosaur dinosaur = entity.getDinosaur();
            String formattedName = dinosaur.getFormattedName();
            GrowthStage stage = entity.getGrowthStage();
            if (!dinosaur.doesSupportGrowthStage(stage)) {
                stage = GrowthStage.ADULT;
            }

            String stageName = stage.name().toLowerCase(Locale.ROOT);
            String basePath = "textures/entities/" + formattedName + "/";
            String gender = entity.isMale() ? "male" : "female";

            ResourceLocation gendered = ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID,
                    basePath + formattedName + "_" + gender + "_" + stageName + "_eyes.png");
            if (textureExists(gendered)) {
                return gendered;
            }

            ResourceLocation stageOnly = ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID,
                    basePath + formattedName + "_" + stageName + "_eyes.png");
            if (textureExists(stageOnly)) {
                return stageOnly;
            }

            ResourceLocation fallback = ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID,
                    basePath + formattedName + "_eyes.png");
            if (textureExists(fallback)) {
                return fallback;
            }

            return null;
        }

        private static boolean textureExists(ResourceLocation location) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft == null) {
                return false;
            }
            return minecraft.getResourceManager().getResource(location).isPresent();
        }
    }
}
