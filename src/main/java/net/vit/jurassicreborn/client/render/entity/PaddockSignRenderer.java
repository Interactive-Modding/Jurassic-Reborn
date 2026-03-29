package net.vit.jurassicreborn.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.vit.jurassicreborn.common.entities.item.PaddockSignEntity;

public class PaddockSignRenderer extends EntityRenderer<PaddockSignEntity> {

    public PaddockSignRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(PaddockSignEntity sign) {
        return sign.getTextureLocation(sign);
    }

    @Override
    public void render(PaddockSignEntity sign, float yaw, float pt,
                       PoseStack pose, MultiBufferSource buf, int light) {

        ResourceLocation tex = getTextureLocation(sign);
        float w = sign.getWidth() / 16f;
        float h = sign.getHeight() / 16f;

        pose.pushPose();

        // 1️⃣ Rotate to face correct direction
        pose.mulPose(Axis.YP.rotationDegrees(180F - yaw));

        // 2️⃣ PUSH STRAIGHT BACK TOWARD THE WALL (LOCAL SPACE)
        // This is the ONLY offset you want
        pose.translate(0.0F, 0.0F, 0.22F);

        // tiny epsilon to avoid z-fighting
        pose.translate(0.0F, 0.0F, 0.001F);

        // 3️⃣ Move origin to top-left for quad drawing
        pose.translate(-w / 2F, -h / 2F, 0.0F);

        VertexConsumer vb = buf.getBuffer(RenderType.entityCutoutNoCull(tex));

        // ── Thickness slices (unchanged) ───────────────────────────
        pose.pushPose();

        float zStart = 0.03f;
        float zEnd   = 0.0f;
        int slices   = 30;

        float step = (zEnd - zStart) / (slices - 1);

        for (int i = 0; i < slices; i++) {
            float z = zStart + i * step;
            drawQuad(pose, vb, 0, 0, w, h, z, light);
        }

        pose.popPose();
        pose.popPose();
    }

    // Draws a textured quad
    private static void drawQuad(PoseStack pose, VertexConsumer vb,
                                 float x0, float y0, float w, float h,
                                 float z, int light) {
        drawQuad(pose, vb, x0, y0, w, h, z, light, false);
    }

    private static void drawQuad(PoseStack pose, VertexConsumer vb,
                                 float x0, float y0, float w, float h,
                                 float z, int light, boolean flipNormal) {

        float x1 = x0 + w;
        float y1 = y0 + h;

        PoseStack.Pose entry = pose.last();
        float nz = flipNormal ? 1f : -1f;

        // flipped U coordinates
        float u0 = 1f, u1 = 0f;
        float v0 = 0f, v1 = 1f;

        // bottom-left
        vb.addVertex(entry.pose(), x0, y1, z)
                .setColor(255, 255, 255, 255)
                .setUv(u0, v0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(entry, 0f, 0f, nz);

        // bottom-right
        vb.addVertex(entry.pose(), x1, y1, z)
                .setColor(255, 255, 255, 255)
                .setUv(u1, v0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(entry, 0f, 0f, nz);

        // top-right
        vb.addVertex(entry.pose(), x1, y0, z)
                .setColor(255, 255, 255, 255)
                .setUv(u1, v1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(entry, 0f, 0f, nz);

        // top-left
        vb.addVertex(entry.pose(), x0, y0, z)
                .setColor(255, 255, 255, 255)
                .setUv(u0, v1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(entry, 0f, 0f, nz);
    }

    // (kept here in case you use it later)
    @SuppressWarnings("unused")
    private static void drawQuadBlack(PoseStack pose, VertexConsumer vb,
                                      float x0, float y0, float w, float h,
                                      float z, int light, boolean flipNormal) {

        float x1 = x0 + w;
        float y1 = y0 + h;

        PoseStack.Pose entry = pose.last();
        float nz = flipNormal ? 1f : -1f;

        // flipped U coordinates
        float u0 = 1f, u1 = 0f;
        float v0 = 0f, v1 = 1f;

        vb.addVertex(entry.pose(), x0, y1, z)
                .setColor(0, 0, 0, 255)
                .setUv(u0, v0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(entry, 0f, 0f, nz);

        vb.addVertex(entry.pose(), x1, y1, z)
                .setColor(0, 0, 0, 255)
                .setUv(u1, v0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(entry, 0f, 0f, nz);

        vb.addVertex(entry.pose(), x1, y0, z)
                .setColor(0, 0, 0, 255)
                .setUv(u1, v1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(entry, 0f, 0f, nz);

        vb.addVertex(entry.pose(), x0, y0, z)
                .setColor(0, 0, 0, 255)
                .setUv(u0, v1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(entry, 0f, 0f, nz);
    }
}
