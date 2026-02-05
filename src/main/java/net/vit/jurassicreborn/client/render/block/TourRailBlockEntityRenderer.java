package net.vit.jurassicreborn.client.render.block;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.parkBlocks.TourRailBlock;
import net.vit.jurassicreborn.common.blocks.parkBlocks.TourRailBlockEntity;
import net.vit.jurassicreborn.common.blocks.parkBlocks.TourRailModel;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;
import software.bernie.geckolib3.util.EModelRenderCycle;

public class TourRailBlockEntityRenderer extends GeoBlockRenderer<TourRailBlockEntity> {
    public TourRailBlockEntityRenderer(BlockEntityRendererProvider.Context rendererProvider) {
        super(rendererProvider, new TourRailModel());
    }

    public ResourceLocation getTextureResource(TourRailBlockEntity tile) {
        return JurassicReborn.resource("textures/block/" + tile.getDirection().modelName + ".png");
    }

    /** the overlay-only stripe texture (alpha mask) */
    public ResourceLocation getStripeTexture(TourRailBlockEntity tile) {
        return JurassicReborn.resource("textures/block/" + tile.getDirection().modelName + "_stripe.png");
    }
    @Override
    public void render(TourRailBlockEntity tile, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        var railDir = tile.getDirection();
        GeoModel model = modelProvider.getModel(modelProvider.getModelResource(tile));
        modelProvider.setLivingAnimations(tile, getInstanceId(tile));

        // 1) push
        poseStack.pushPose();

        // 2) apply slope-only scale/offset so stripe geometry lines up
        if (railDir.isAscending()) {
            Vector3f scale = switch (railDir.getFacing()) {
                case EAST, WEST  -> new Vector3f(0.7f, 1f,   1f);
                case SOUTH, NORTH-> new Vector3f(1f,   1f, 0.7f);
                default          -> new Vector3f(1f,   1f,   1f);
            };
            poseStack.scale(scale.x(), scale.y(), scale.z());

            double corr = 0.15, neg = 0.125;
            switch (railDir.getFacing()) {
                case WEST -> poseStack.translate(corr + neg, 0, 0);
                case NORTH-> poseStack.translate(0, 0, corr + neg);
                case EAST -> poseStack.translate(corr,     0, 0);
                case SOUTH-> poseStack.translate(0, 0, corr);
                default   -> {}
            }
        }

        // 3) center & rotate
        poseStack.translate(0, 0.01, 0);
        poseStack.translate(0.5, 0, 0.5);
        poseStack.mulPose(Vector3f.YP.rotationDegrees((float) railDir.rotation));

        // 4) BASE PASS: plain rail
        RenderSystem.setShaderTexture(0, getTextureResource(tile));
        RenderType baseType = getRenderType(tile, partialTick, poseStack, bufferSource, null, packedLight, getTextureResource(tile));
        this.render(model, tile, partialTick, baseType, poseStack, bufferSource, null, packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);

        // 5) STRIPE PASS: **always** draw the stripe mask, tinted by speedColor
        BlockState state = tile.getBlockState();
        int rgb = ((TourRailBlock)state.getBlock()).getSpeedType().getColor();
        float r = (rgb >> 16 & 0xFF) / 255f;
        float g = (rgb >>  8 & 0xFF) / 255f;
        float b = (rgb       & 0xFF) / 255f;

        RenderSystem.setShaderTexture(0, getStripeTexture(tile));
        RenderType stripeType = getRenderType(tile, partialTick, poseStack, bufferSource, null, packedLight, getStripeTexture(tile));
        this.render(model, tile, partialTick, stripeType, poseStack, bufferSource, null, packedLight, OverlayTexture.NO_OVERLAY, r, g, b, 1f);

        // 6) pop
        poseStack.popPose();
    }
}
