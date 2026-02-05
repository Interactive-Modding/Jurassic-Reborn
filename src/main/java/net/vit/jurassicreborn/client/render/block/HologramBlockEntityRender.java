package net.vit.jurassicreborn.client.render.block;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.blocks.entities.HologramBlockEntity;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import org.jetbrains.annotations.NotNull;

public class HologramBlockEntityRender implements BlockEntityRenderer<HologramBlockEntity> {

    public HologramBlockEntityRender(BlockEntityRendererProvider.Context ctx) {}

    /** Wraps a VertexConsumer and multiplies incoming .color() by a tint without touching defaultColor on the shared delegate. */
    private static class TintingVertexConsumer implements VertexConsumer {
        private final VertexConsumer delegate;
        private final float tr, tg, tb, ta; // [0..1]

        TintingVertexConsumer(VertexConsumer delegate, float tr, float tg, float tb, float ta) {
            this.delegate = delegate;
            this.tr = tr; this.tg = tg; this.tb = tb; this.ta = ta;
        }

        @Override public VertexConsumer color(int r, int g, int b, int a) {
            int nr = Math.min(255, Math.round(r * tr));
            int ng = Math.min(255, Math.round(g * tg));
            int nb = Math.min(255, Math.round(b * tb));
            int na = Math.min(255, Math.round(a * ta));
            return delegate.color(nr, ng, nb, na);
        }

        // Forward everything else unchanged
        @Override public VertexConsumer vertex(double x, double y, double z){ return delegate.vertex(x,y,z); }
        @Override public VertexConsumer uv(float u, float v){ return delegate.uv(u,v); }
        @Override public VertexConsumer overlayCoords(int u, int v){ return delegate.overlayCoords(u,v); }
        @Override public VertexConsumer uv2(int u, int v){ return delegate.uv2(u,v); }
        @Override public VertexConsumer normal(float x, float y, float z){ return delegate.normal(x,y,z); }
        @Override public void endVertex(){ delegate.endVertex(); }

        // NO-OP these to avoid leaking default color onto the shared underlying buffer
        @Override public void defaultColor(int r, int g, int b, int a){ /* swallow */ }
        @Override public void unsetDefaultColor(){ /* swallow */ }
    }

    @Override
    public void render(HologramBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       @NotNull MultiBufferSource _ignoredGlobalBuffers, int packedLight, int packedOverlay) {
        DinosaurEntity entity = blockEntity.getEntity();
        if (entity == null) return;

        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.scale(1.0f, 1.0f, 1.0f);
        poseStack.mulPose(Axis.YP.rotationDegrees(blockEntity.getRot()));

        EntityAnimation anim = blockEntity.getPoseAnimation();
        if (entity.getAnimation() != anim.get()) {
            entity.setAnimation(anim.get());
        }

        EntityRenderer<? super DinosaurEntity> er =
                Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
        final ResourceLocation tex = er.getTextureLocation(entity);

        final float TINT_R = 102f / 255f;
        final float TINT_G = 178f / 255f;
        final float TINT_B = 255f / 255f;
        final float TINT_A = 0.50f;

        final MultiBufferSource.BufferSource privateBuffers =
                MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());

        MultiBufferSource hologramBuffer = new MultiBufferSource() {
            @Override
            public @NotNull VertexConsumer getBuffer(@NotNull RenderType requested) {
                if (requested == RenderType.lines()) {
                    return privateBuffers.getBuffer(RenderType.lines());
                }
                VertexConsumer base = privateBuffers.getBuffer(RenderType.entityTranslucent(tex));
                return new TintingVertexConsumer(base, TINT_R, TINT_G, TINT_B, TINT_A);
            }
        };

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        final int FULL_BRIGHT = LightTexture.pack(15, 15);

        try {
            Minecraft.getInstance().getEntityRenderDispatcher()
                    .render(entity, 0, 0, 0, 0, 0, poseStack, hologramBuffer, FULL_BRIGHT);

            privateBuffers.endBatch();
        } finally {
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        }
        poseStack.popPose();
    }
}
