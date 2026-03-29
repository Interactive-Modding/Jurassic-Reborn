package net.vit.jurassicreborn.client.render.entity;

import com.github.alexthe666.citadel.client.model.TabulaModel;
import com.github.alexthe666.citadel.client.model.container.TabulaModelContainer;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.SkullDisplayBlock;
import net.vit.jurassicreborn.common.blocks.entities.SkullDisplayBlockEntity;
import net.vit.jurassicreborn.common.legacy.tabula.TabulaModelHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@OnlyIn(Dist.CLIENT)
public class SkullDisplayRenderer implements BlockEntityRenderer<SkullDisplayBlockEntity> {

    public SkullDisplayRenderer(BlockEntityRendererProvider.Context ctx) {}

    private static final int MAX_TEXTURE_SIZE = 2048;
    private static final Map<ResourceLocation, ResourceLocation> TEXTURE_CACHE = new ConcurrentHashMap<>();

    @Override
    public void render(SkullDisplayBlockEntity tile, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer,
                       int packedLight, int packedOverlay) {

        BlockState state = tile.getBlockState();
        if (state.getBlock() != ModBlocks.SKULL_DISPLAY.get()) return;


        boolean horizontal = state.getValue(SkullDisplayBlock.FACING).getAxis() == Direction.Axis.Y;

        // Load Tabula model & texture if needed
        if (tile.model == null && tile.hasData()) {
            try {
                String dino = tile.getDinosaur().getName()
                        .toLowerCase(Locale.ENGLISH)
                        .replace(' ', '_');
                String modelOrient   = horizontal ? "horizontal" : "vertical";
                String textureOrient = horizontal ? "vertical"   : "horizontal";

                TabulaModelContainer container = TabulaModelHelper.loadTabulaModel(
                        ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID,
                                "models/block/skull_display/" + dino + '_' + modelOrient));
                tile.model = new TabulaModel(container);
                if (container != null && container.getScale() != null && container.getScale().length >= 3) {
                    tile.modelScale = new float[] {
                            (float) container.getScale()[0],
                            (float) container.getScale()[1],
                            (float) container.getScale()[2]
                    };
                }

                // NOTE: textures/**block**/ (singular) is correct for 1.19+
                ResourceLocation textureLocation = ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID,
                        "textures/block/skull_display/" + dino + '_' +
                                (tile.isFossilized() ? "fossilized" : "fresh") + '_' +
                                textureOrient + ".png");
                tile.texture = ensureTextureSize(textureLocation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 0.5, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(tile.getAngle()));

        float sx = tile.modelScale[0];
        float sy = tile.modelScale[1];
        float sz = tile.modelScale[2];
        poseStack.scale(1.6F * sx, -1.6F * sy, 1.6F * sz);

        if (!horizontal) {
            poseStack.translate(0, 0.15F * sy, -0.18F * sz); // scale aware!
        }
        poseStack.translate(0.0F, -1.194F * sy, 0.0F); // scale aware!

        if (tile.model != null) {
            VertexConsumer vc = buffer.getBuffer(RenderType.entityCutoutNoCull(tile.texture));
            tile.model.renderToBuffer(poseStack, vc,
                    packedLight, packedOverlay, 0xFFFFFFFF
            );
        }
        poseStack.popPose();
    }


    private static ResourceLocation ensureTextureSize(ResourceLocation requested) {
        ResourceLocation resolved = resolveExistingTexture(requested);
        return TEXTURE_CACHE.computeIfAbsent(resolved, SkullDisplayRenderer::loadOrScaleTexture);
    }

    private static ResourceLocation resolveExistingTexture(ResourceLocation requested) {
        Minecraft minecraft = Minecraft.getInstance();

        if (minecraft.getResourceManager().getResource(requested).isPresent()) {
            return requested;
        }

        String path = requested.getPath();
        if (path.startsWith("textures/block/")) {
            String fallbackPath = path.replace("textures/block/", "textures/");
            ResourceLocation fallback = ResourceLocation.fromNamespaceAndPath(requested.getNamespace(), fallbackPath);
            if (minecraft.getResourceManager().getResource(fallback).isPresent()) {
                return fallback;
            }
        }

        return requested;
    }

    private static ResourceLocation loadOrScaleTexture(ResourceLocation original) {
        Minecraft minecraft = Minecraft.getInstance();
        TextureManager textureManager = minecraft.getTextureManager();

        Optional<Resource> optionalResource = minecraft.getResourceManager().getResource(original);
        if (optionalResource.isEmpty()) {
            return original;
        }

        Resource resource = optionalResource.get();
        InputStream stream = null;

        try {
            stream = resource.open();
            NativeImage image = NativeImage.read(stream);

            int width = image.getWidth();
            int height = image.getHeight();

            if (width <= MAX_TEXTURE_SIZE && height <= MAX_TEXTURE_SIZE) {
                image.close();
                return original;
            }

            float scaleFactor = Math.max((float) width / MAX_TEXTURE_SIZE, (float) height / MAX_TEXTURE_SIZE);
            int scaledWidth = Math.max(1, Math.round(width / scaleFactor));
            int scaledHeight = Math.max(1, Math.round(height / scaleFactor));

            NativeImage scaledImage = new NativeImage(scaledWidth, scaledHeight, false);

            float ratioX = (float) width / scaledWidth;
            float ratioY = (float) height / scaledHeight;

            for (int y = 0; y < scaledHeight; y++) {
                int srcY = Math.min(height - 1, (int)(y * ratioY));
                for (int x = 0; x < scaledWidth; x++) {
                    int srcX = Math.min(width - 1, (int)(x * ratioX));
                    scaledImage.setPixelRGBA(x, y, image.getPixelRGBA(srcX, srcY));
                }
            }

            image.close();

            DynamicTexture dynamicTexture = new DynamicTexture(scaledImage);

            String path = original.getPath();
            int dot = path.lastIndexOf('.');
            String base = dot >= 0 ? path.substring(0, dot) : path;
            String ext = dot >= 0 ? path.substring(dot) : "";

            ResourceLocation scaledLocation =
                    ResourceLocation.fromNamespaceAndPath(original.getNamespace(), base + "_scaled" + ext);

            textureManager.register(scaledLocation, dynamicTexture);

            return scaledLocation;
        }
        catch (IOException e) {
            e.printStackTrace();
            return original;
        }
        finally {
            if (stream != null) {
                try { stream.close(); } catch (IOException ignored) {}
            }
        }
    }
}
