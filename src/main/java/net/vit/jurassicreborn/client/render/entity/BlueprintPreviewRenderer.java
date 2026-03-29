package net.vit.jurassicreborn.client.render.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.item.BlueprintPaintingEntity;
import net.vit.jurassicreborn.common.items.misc.BlueprintItem;

@EventBusSubscriber(modid = JurassicReborn.MODID, value = Dist.CLIENT)
public class BlueprintPreviewRenderer {

    @SubscribeEvent
    public static void renderPreview(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null || mc.level == null) {
            return;
        }

        ItemStack held = player.getMainHandItem();
        if (!(held.getItem() instanceof BlueprintItem blueprintItem)) {
            return;
        }

        HitResult hit = mc.hitResult;
        if (!(hit instanceof BlockHitResult blockHit)) {
            return;
        }

        BlockPos pos = blockHit.getBlockPos();
        Direction face = blockHit.getDirection();
        if (!face.getAxis().isHorizontal()) {
            return;
        }

        BlockPos spawnPos = pos.relative(face);
        Holder<PaintingVariant> previewVariant = blueprintItem.getPreviewVariant(held).orElse(null);
        ResourceLocation texture = blueprintItem.getPreviewTexture(held);

        if (previewVariant == null || texture == null) {
            return;
        }

        BlueprintPaintingEntity testPainting = new BlueprintPaintingEntity(mc.level, spawnPos, face, previewVariant);
        boolean valid = testPainting.survives();

        renderGhostMural(event, testPainting, texture, valid);
    }

    private static void renderGhostMural(RenderLevelStageEvent event,
                                         BlueprintPaintingEntity painting,
                                         ResourceLocation texture,
                                         boolean valid) {
        Minecraft mc = Minecraft.getInstance();
        PoseStack pose = event.getPoseStack();
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 camPos = camera.getPosition();

        int wBlocks = painting.getVariant().value().width();
        int hBlocks = painting.getVariant().value().height();
        float w = wBlocks;
        float h = hBlocks;

        pose.pushPose();
        pose.translate(
                painting.getX() - camPos.x,
                painting.getY() - camPos.y,
                painting.getZ() - camPos.z
        );
        pose.mulPose(Axis.YP.rotationDegrees(180.0F - painting.getYRot()));
        pose.translate(-w / 2.0F, -h / 2.0F, 0.01F);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        if (valid) {
            RenderSystem.setShaderColor(0.5F, 1.0F, 0.5F, 0.6F);
        } else {
            RenderSystem.setShaderColor(1.0F, 0.3F, 0.3F, 0.6F);
        }

        MultiBufferSource.BufferSource source = mc.renderBuffers().bufferSource();
        VertexConsumer vc = source.getBuffer(RenderType.entityTranslucent(texture));
        addQuad(pose, vc, 0.0F, 0.0F, w, h);
        source.endBatch();

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
        pose.popPose();
    }

    private static void addQuad(PoseStack pose, VertexConsumer vc,
                                float x0, float y0, float w, float h) {
        float x1 = x0 + w;
        float y1 = y0 + h;
        PoseStack.Pose last = pose.last();

        vc.addVertex(last, x0, y1, 0.0F)
                .setColor(255, 255, 255, 255)
                .setUv(1.0F, 0.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setNormal(last, 0.0F, 0.0F, 1.0F);

        vc.addVertex(last, x1, y1, 0.0F)
                .setColor(255, 255, 255, 255)
                .setUv(0.0F, 0.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setNormal(last, 0.0F, 0.0F, 1.0F);

        vc.addVertex(last, x1, y0, 0.0F)
                .setColor(255, 255, 255, 255)
                .setUv(0.0F, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setNormal(last, 0.0F, 0.0F, 1.0F);

        vc.addVertex(last, x0, y0, 0.0F)
                .setColor(255, 255, 255, 255)
                .setUv(1.0F, 1.0F)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setNormal(last, 0.0F, 0.0F, 1.0F);
    }
}
