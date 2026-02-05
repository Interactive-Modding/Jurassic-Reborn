package net.vit.jurassicreborn.client.render.entity;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.vit.jurassicreborn.common.entities.item.AttractionSignEntity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class AttractionSignRenderer extends EntityRenderer<AttractionSignEntity> {

    public AttractionSignRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public ResourceLocation getTextureLocation(AttractionSignEntity sign) {
        return sign.getFaceTexture();
    }

    @Override
    public void render(AttractionSignEntity sign, float yaw, float pt,
                       PoseStack ms, MultiBufferSource buffers, int light) {
        float w = sign.getWidth()  / 2f;
        float h = sign.getHeight() / 2f;
        float z = -0.04f;
        ms.pushPose();
        ms.mulPose(Axis.YP.rotationDegrees(180f - yaw));
        ms.translate(-w/2f, -h/2f, 0f);

        // Main face (the ONLY visible solid part)
        VertexConsumer face = buffers.getBuffer(RenderType.entityCutoutNoCull(sign.getFaceTexture()));
        drawQuad(ms, face, 0, -2, w, h, 0f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.001f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.002f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.003f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.004f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.005f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.006f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.007f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.008f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.009f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.01f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.011f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.012f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.013f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.014f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.015f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.016f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.017f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.018f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.019f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.02f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.021f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.022f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.023f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.024f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.025f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.026f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.027f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.028f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.029f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.03f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.031f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.032f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.033f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.034f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.035f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.036f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.037f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.038f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.039f, light);    // (flat on wall)
        drawQuad(ms, face, 0, -2, w, h, -0.04f, light);    // (flat on wall)

        // Overlay face (popout)
        VertexConsumer pop = buffers.getBuffer(RenderType.entityCutoutNoCull(sign.getPopoutTexture()));
        drawQuad(ms, pop, 0, -2, w, h, z, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.041f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.042f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.043f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.044f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.045f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.046f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.047f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.048f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.049f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.05f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.051f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.052f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.053f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.054f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.055f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.056f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.057f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.058f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.059f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.06f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.061f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.062f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.063f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.064f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.065f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.066f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.067f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.068f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.069f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.07f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.071f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.072f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.073f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.074f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.075f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.076f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.077f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.078f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.079f, light);      // slightly in front
        drawQuad(ms, pop, 0, -2, w, h, -0.08f, light);      // slightly in front

        ms.popPose();
        super.render(sign, yaw, pt, ms, buffers, light);
    }


    // Draws a quad at z
    private static void drawQuad(PoseStack ms, VertexConsumer vb,
                                 float x, float y, float w, float h, float z, int light) {
        float x1 = x + w, y1 = y + h;
        PoseStack.Pose entry = ms.last();
        Matrix4f m = entry.pose();
        Matrix3f n = entry.normal();

        vb.vertex(m, x,  y1, z).color(255,255,255,255).uv(1, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                .normal(n,0,0,-1).endVertex();
        vb.vertex(m, x1, y1, z).color(255,255,255,255).uv(0, 0)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                .normal(n,0,0,-1).endVertex();
        vb.vertex(m, x1, y,  z).color(255,255,255,255).uv(0, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                .normal(n,0,0,-1).endVertex();
        vb.vertex(m, x,  y,  z).color(255,255,255,255).uv(1, 1)
                .overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light)
                .normal(n,0,0,-1).endVertex();
    }

    // Draws a side quad (from (x, y) along (dx, dy)), between z0 and z1
    private static void drawSide(PoseStack ms, VertexConsumer vb,
                                 float x, float y, float dx, float dy, float z0, float z1, int light) {
        PoseStack.Pose entry = ms.last();
        Matrix4f m = entry.pose();
        Matrix3f n = entry.normal();

        vb.vertex(m, x,     y,     z0).color(200,200,200,255).uv(0,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(n, 0,0,1).endVertex();
        vb.vertex(m, x+dx,  y+dy,  z0).color(200,200,200,255).uv(1,0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(n, 0,0,1).endVertex();
        vb.vertex(m, x+dx,  y+dy,  z1).color(200,200,200,255).uv(1,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(n, 0,0,1).endVertex();
        vb.vertex(m, x,     y,     z1).color(200,200,200,255).uv(0,1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(n, 0,0,1).endVertex();
    }

}
