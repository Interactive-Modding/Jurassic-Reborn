package net.vit.jurassicreborn.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
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

    /** Wraps a VertexConsumer and multiplies incoming color by a tint. */
    private static class TintingVertexConsumer implements VertexConsumer {
        private final VertexConsumer delegate;
        private final float tr, tg, tb, ta; // [0..1]

        TintingVertexConsumer(VertexConsumer delegate, float tr, float tg, float tb, float ta) {
            this.delegate = delegate;
            this.tr = tr; this.tg = tg; this.tb = tb; this.ta = ta;
        }

        private static int clamp255(int value) {
            return Math.min(255, Math.max(0, value));
        }

        private int tintR(int r) {
            return clamp255(Math.round(r * tr));
        }

        private int tintG(int g) {
            return clamp255(Math.round(g * tg));
        }

        private int tintB(int b) {
            return clamp255(Math.round(b * tb));
        }

        private int tintA(int a) {
            return clamp255(Math.round(a * ta));
        }

        @Override public VertexConsumer setColor(int r, int g, int b, int a) {
            delegate.setColor(tintR(r), tintG(g), tintB(b), tintA(a));
            return this;
        }

        @Override public VertexConsumer addVertex(float x, float y, float z) { delegate.addVertex(x, y, z); return this; }
        @Override public VertexConsumer setColor(float r, float g, float b, float a) { delegate.setColor(r * tr, g * tg, b * tb, a * ta); return this; }
        @Override public VertexConsumer setColor(int packedColor) {
            int a = (packedColor >>> 24) & 0xFF;
            int r = (packedColor >>> 16) & 0xFF;
            int g = (packedColor >>> 8) & 0xFF;
            int b = packedColor & 0xFF;
            delegate.setColor(tintR(r), tintG(g), tintB(b), tintA(a));
            return this;
        }
        @Override public VertexConsumer setWhiteAlpha(int alpha) {
            delegate.setColor(tintR(255), tintG(255), tintB(255), tintA(alpha));
            return this;
        }
        @Override public VertexConsumer setUv(float u, float v) { delegate.setUv(u, v); return this; }
        @Override public VertexConsumer setUv1(int u, int v) { delegate.setUv1(u, v); return this; }
        @Override public VertexConsumer setOverlay(int overlay) { delegate.setOverlay(overlay); return this; }
        @Override public VertexConsumer setUv2(int u, int v) { delegate.setUv2(u, v); return this; }
        @Override public VertexConsumer setLight(int light) { delegate.setLight(light); return this; }
        @Override public VertexConsumer setNormal(float x, float y, float z) { delegate.setNormal(x, y, z); return this; }
    }

    @Override
    public void render(HologramBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        DinosaurEntity entity = blockEntity.getEntity();
        if (entity == null) return;

        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
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

        final int FULL_BRIGHT = LightTexture.pack(15, 15);

        MultiBufferSource hologramBuffer = renderType -> {
            // Pass lines through untinted so outlines don't break
            if (renderType == RenderType.lines()) {
                return bufferSource.getBuffer(RenderType.lines());
            }
            // All other geometry: redirect to entityTranslucent and tint it
            VertexConsumer base = bufferSource.getBuffer(RenderType.entityTranslucent(tex));
            return new TintingVertexConsumer(base, TINT_R, TINT_G, TINT_B, TINT_A);
        };

        Minecraft.getInstance().getEntityRenderDispatcher()
                .render(entity, 0, 0, 0, 0, 0, poseStack, hologramBuffer, FULL_BRIGHT);

        poseStack.popPose();
    }
}
