package net.vit.jurassicreborn.client.render.entity;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.PaintingRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.Painting;
import net.vit.jurassicreborn.common.entities.item.BlueprintPaintingEntity;
import com.mojang.math.Vector3f;

public class BlueprintRenderer extends PaintingRenderer {

    public BlueprintRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(Painting painting, float yaw, float partialTicks,
                       PoseStack pose, MultiBufferSource buf, int light) {

        ResourceLocation tex = ((BlueprintPaintingEntity) painting).getBlueprintTexture();
        int   wPx = painting.getVariant().value().getWidth();
        int   hPx = painting.getVariant().value().getHeight();
        float w   = wPx / 16f;   // block units
        float h   = hPx / 16f;

        pose.pushPose();
        pose.mulPose(Vector3f.YP.rotationDegrees(180.0F - yaw));
        pose.translate(-w / 2.0F, -h / 2.0F, 0.0F);

        VertexConsumer vc = buf.getBuffer(RenderType.entityCutout(tex));

        // one big quad
        addQuad(pose, vc, 0, 0, w, h, light);

        pose.popPose();
    }

    /** emits a single textured quad facing +Z */
    private static void addQuad(PoseStack pose, VertexConsumer vc,
                                float x0, float y0, float w, float h, int light) {

        float x1 = x0 + w;
        float y1 = y0 + h;

        vc.vertex(pose.last().pose(), x0, y1, 0)
                .color(255,255,255,255).uv(0, 1).uv2(light).endVertex();
        vc.vertex(pose.last().pose(), x1, y1, 0)
                .color(255,255,255,255).uv(1, 1).uv2(light).endVertex();
        vc.vertex(pose.last().pose(), x1, y0, 0)
                .color(255,255,255,255).uv(1, 0).uv2(light).endVertex();
        vc.vertex(pose.last().pose(), x0, y0, 0)
                .color(255,255,255,255).uv(0, 0).uv2(light).endVertex();
    }
}
