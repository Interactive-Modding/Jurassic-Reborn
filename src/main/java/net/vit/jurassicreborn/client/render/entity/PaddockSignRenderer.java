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
import org.joml.Matrix3f;
import org.joml.Matrix4f;

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
        float w = sign.getWidth()  / 16f;
        float h = sign.getHeight() / 16f;

        pose.pushPose();
        pose.mulPose(Axis.YP.rotationDegrees(180F - yaw));
        pose.translate(-w/2F, -h/2F, 0);

        VertexConsumer vb = buf.getBuffer(RenderType.entityCutoutNoCull(tex));

        // draw front face at z=0

        // draw back face at z=-δ with flipped normals
        float s = 0.01f;
        pose.pushPose();
        pose.translate(0, 0, -s);

        float zStart   = 0.03f;   // first slice: almost flush to the wall
        float zEnd     = 0.000f;
        int   slices   = 30;

        float step = (zEnd - zStart) / (slices - 1);   // negative value

        for (int i = 0; i < slices; i++) {
            float z = zStart + i * step;               // 0 → -0.03 f (inclusive)
            drawQuad(pose, vb, 0, 0, w, h, z, light);
        }

        pose.popPose();

        pose.popPose();
    }
    // Draws a black quad for the back of the sign
    private static void drawQuadBlack(PoseStack pose, VertexConsumer vb,
                                      float x0, float y0, float w, float h,
                                      float z, int light, boolean flipNormal) {
        float x1 = x0 + w, y1 = y0 + h;
        PoseStack.Pose last = pose.last();
        Matrix4f m = last.pose();
        Matrix3f n = last.normal();
        float nz = flipNormal ? 1f : -1f;

        float u0 = 1f;
        float u1 = 0f;
        float v0 = 0f;
        float v1 = 1f;

        // All BLACK
        int r = 0, g = 0, b = 0, a = 255;

        // bottom-left
        vb.vertex(m, x0, y1, z).color(r, g, b, a).uv(u0, v0)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                .normal(n, 0f, 0f, nz).endVertex();
        // bottom-right
        vb.vertex(m, x1, y1, z).color(r, g, b, a).uv(u1, v0)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                .normal(n, 0f, 0f, nz).endVertex();
        // top-right
        vb.vertex(m, x1, y0, z).color(r, g, b, a).uv(u1, v1)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                .normal(n, 0f, 0f, nz).endVertex();
        // top-left
        vb.vertex(m,  0, 0, z).color(r, g, b, a).uv(u0, v1)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                .normal(n, 0f, 0f, nz).endVertex();
    }

    private static void drawQuad(PoseStack pose, VertexConsumer vb,
                                 float x0, float y0, float w, float h,
                                 float z, int light) {
        drawQuad(pose, vb,  0, 0, w, h, z, light, false);
    }
    private static void drawQuad(PoseStack pose, VertexConsumer vb,
                                 float x0, float y0, float w, float h,
                                 float z, int light, boolean flipNormal) {
        float x1 = x0 + w, y1 = y0 + h;
        PoseStack.Pose last = pose.last();
        Matrix4f m = last.pose();
        Matrix3f n = last.normal();
        float nz = flipNormal ? 1f : -1f;

        // flipped U coordinates
        float u0 = 1f;
        float u1 = 0f;
        float v0 = 0f;
        float v1 = 1f;

        // bottom-left
        vb.vertex(m, x0, y1, z)
                .color(255, 255, 255, 255)
                .uv(u0, v0)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(n, 0f, 0f, nz)
                .endVertex();
        // bottom-right
        vb.vertex(m, x1, y1, z)
                .color(255, 255, 255, 255)
                .uv(u1, v0)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(n, 0f, 0f, nz)
                .endVertex();
        // top-right
        vb.vertex(m, x1, y0, z)
                .color(255, 255, 255, 255)
                .uv(u1, v1)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(n, 0f, 0f, nz)
                .endVertex();
        // top-left
        vb.vertex(m,  0, 0, z)
                .color(255, 255, 255, 255)
                .uv(u0, v1)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(n, 0f, 0f, nz)
                .endVertex();
    }

}
