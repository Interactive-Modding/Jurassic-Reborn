package net.vit.jurassicreborn.client.render.entity;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.PaintingRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.Painting;
import net.vit.jurassicreborn.common.entities.item.BlueprintPaintingEntity;
import com.mojang.math.Axis;

public class BlueprintRenderer extends PaintingRenderer {

    public BlueprintRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(Painting painting, float yaw, float partialTicks,
                       PoseStack pose, MultiBufferSource buf, int light) {

        ResourceLocation tex = ((BlueprintPaintingEntity) painting).getBlueprintTexture();
        int   wBlocks = painting.getVariant().value().width();
        int   hBlocks = painting.getVariant().value().height();
        float w   = wBlocks;
        float h   = hBlocks;

        pose.pushPose();
        pose.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
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

        PoseStack.Pose entry = pose.last();

        vc.addVertex(entry.pose(), x0, y1, 0)
                .setColor(255, 255, 255, 255)
                .setUv(0, 1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(entry, 0, 0, 1);

        vc.addVertex(entry.pose(), x1, y1, 0)
                .setColor(255, 255, 255, 255)
                .setUv(1, 1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(entry, 0, 0, 1);

        vc.addVertex(entry.pose(), x1, y0, 0)
                .setColor(255, 255, 255, 255)
                .setUv(1, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(entry, 0, 0, 1);

        vc.addVertex(entry.pose(), x0, y0, 0)
                .setColor(255, 255, 255, 255)
                .setUv(0, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(entry, 0, 0, 1);
    }
}
