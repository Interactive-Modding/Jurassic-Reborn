package net.vit.jurassicreborn.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
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
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class TourRailBlockEntityRenderer extends GeoBlockRenderer<TourRailBlockEntity> {

    public TourRailBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(new TourRailModel());
        this.addRenderLayer(new StripeLayer(this));
    }

    // Base texture based on rail direction/model name
    @Override
    public ResourceLocation getTextureLocation(TourRailBlockEntity tile) {
        return JurassicReborn.resource("textures/block/" + tile.getDirection().modelName + ".png");
    }

    // Stripe alpha mask
    public ResourceLocation getStripeTexture(TourRailBlockEntity tile) {
        return JurassicReborn.resource("textures/block/" + tile.getDirection().modelName + "_stripe.png");
    }

    /**
     * Centering/rotation call still happens inside GeoBlockRenderer#actuallyRender,
     * but yaw is now handled by our rotateBlock override below.
     */
    @Override
    public void preRender(PoseStack poseStack,
                          TourRailBlockEntity animatable,
                          BakedGeoModel model,
                          MultiBufferSource bufferSource,
                          VertexConsumer buffer,
                          boolean isReRender,
                          float partialTick,
                          int packedLight,
                          int packedOverlay,
                          float red,
                          float green,
                          float blue,
                          float alpha) {

        // Let GeckoLib do its usual bookkeeping
        super.preRender(poseStack, animatable, model, bufferSource, buffer,
                isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        var railDir = animatable.getDirection();

        // Slope-only scale/offset so stripe geometry lines up
        if (railDir.isAscending()) {
            float sx = 1.0F;
            float sz = 1.0F;

            switch (railDir.getFacing()) {
                case EAST, WEST -> sx = 0.7F;
                case NORTH, SOUTH -> sz = 0.7F;
                default -> { }
            }

            poseStack.scale(sx, 1.0F, sz);

            double corr = 0.15;
            double neg = 0.125;

            switch (railDir.getFacing()) {
                case WEST -> poseStack.translate(corr + neg, 0.0, 0.0);
                case NORTH -> poseStack.translate(0.0, 0.0, corr + neg);
                case EAST -> poseStack.translate(corr, 0.0, 0.0);
                case SOUTH -> poseStack.translate(0.0, 0.0, corr);
                default -> { }
            }
        }

        // tiny lift to avoid z-fighting with the block
        poseStack.translate(0.0, 0.01, 0.0);
    }

    /**
     * **MAIN FIX**:
     * Ignore the passed-in facing for yaw and just use EnumRailDirection.rotation.
     *   poseStack.mulPose(Vector3f.YP.rotationDegrees(railDir.rotation));
     */
    @Override
    protected void rotateBlock(Direction facing, PoseStack poseStack) {
        if (this.animatable != null) {
            int rot = this.animatable.getDirection().rotation;
            poseStack.mulPose(Axis.YP.rotationDegrees(rot));
        } else {
            // Fallback if somehow called without an animatable
            super.rotateBlock(facing, poseStack);
        }
    }

    /**
     * Stripe overlay layer – draws the stripe texture tinted by the rail speed color.
     */
    private static class StripeLayer extends GeoRenderLayer<TourRailBlockEntity> {

        public StripeLayer(GeoRenderer<TourRailBlockEntity> renderer) {
            super(renderer);
        }

        @Override
        public void render(PoseStack poseStack,
                           TourRailBlockEntity animatable,
                           BakedGeoModel bakedModel,
                           RenderType baseRenderType,
                           MultiBufferSource bufferSource,
                           VertexConsumer buffer,
                           float partialTick,
                           int packedLight,
                           int packedOverlay) {

            BlockState state = animatable.getBlockState();
            if (!(state.getBlock() instanceof TourRailBlock railBlock)) {
                return;
            }

            int rgb = railBlock.getSpeedType().getColor();
            float r = (rgb >> 16 & 0xFF) / 255.0F;
            float g = (rgb >> 8  & 0xFF) / 255.0F;
            float b = (rgb       & 0xFF) / 255.0F;

            ResourceLocation stripeTex =
                    JurassicReborn.resource("textures/block/" + animatable.getDirection().modelName + "_stripe.png");

            RenderType stripeType = RenderType.entityCutoutNoCull(stripeTex);
            VertexConsumer stripeBuffer = bufferSource.getBuffer(stripeType);

            this.getRenderer().reRender(
                    bakedModel,
                    poseStack,
                    bufferSource,
                    animatable,
                    stripeType,
                    stripeBuffer,
                    partialTick,
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    r, g, b, 1.0F
            );
        }
    }
}