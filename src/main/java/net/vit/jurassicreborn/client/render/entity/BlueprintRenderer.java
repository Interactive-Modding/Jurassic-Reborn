package net.vit.jurassicreborn.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.vit.jurassicreborn.common.entities.item.BlueprintEntity;

public class BlueprintRenderer extends EntityRenderer<BlueprintEntity> {

    public BlueprintRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(BlueprintEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.mulPose(com.mojang.math.Vector3f.YP.rotationDegrees(180.0F - entityYaw));

        // Scale from pixels to world units (1/16)
        final float s = 0.0625F;
        poseStack.scale(s, s, s);

        // Use a standard entity cutout render type with the entity’s texture
        ResourceLocation tex = entity.type.texture;
        VertexConsumer vc = buffer.getBuffer(RenderType.entityCutoutNoCull(tex));

        // Draw the extruded textured layer.  The previous port offset the pixels so
        // the blueprint started at the entity origin and only extended upwards,
        // which meant the quad visually intersected the hitbox.  By constructing
        // the geometry around the origin (rather than above it) the mesh now sits
        // flush with the hanging bounds just like vanilla paintings.
        renderLayer(entity, entity.getWidth(), entity.getHeight(),
                entity.type.sizeX, entity.type.sizeY, poseStack, vc);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(BlueprintEntity entity) {
        return entity.type.texture;
    }

    private void renderLayer(BlueprintEntity entity,
                             int width, int height, int textureWidth, int textureHeight,
                             PoseStack poseStack, VertexConsumer vc) {

        float centerWidth = -textureWidth / 2.0F;
        float centerHeight = -textureHeight / 2.0F;
        float pixelSize = 0.0625F;
        float depth = 1.0F;

        // Pull current matrices
        PoseStack.Pose last = poseStack.last();
        Matrix4f mat = last.pose();
        Matrix3f nMat = last.normal();

        // Offset so the front face sits at z= -depth + 0.5 like the original
        poseStack.translate(0.0F, 0.0F, -depth + 0.5F);

        // Re-fetch after translate
        last = poseStack.last();
        mat = last.pose();
        nMat = last.normal();

        // Iterate pixels just like before
        for (int x = 0; x < textureWidth * pixelSize; x++) {
            for (int y = 0; y < textureHeight * pixelSize; y++) {

                float maxX = centerWidth + (x + 1) / pixelSize;
                float minX = centerWidth + x / pixelSize;
                float maxY = (y + 1) / pixelSize - textureHeight * 0.5F;
                float minY = y / pixelSize - textureHeight * 0.5F;

                // Compute UVs (mirror logic preserved from original)
                float maxTextureX = (textureWidth - x / pixelSize) / textureWidth;
                float minTextureX = (textureWidth - (x + 1) / pixelSize) / textureWidth;
                float maxTextureY = (textureHeight - y / pixelSize) / textureHeight;
                float minTextureY = (textureHeight - (y + 1) / pixelSize) / textureHeight;

                // Per-pixel lighting similar to setLightmap (use packed light on vertices)
                int lightFront = computePackedLightForPixel(entity, (maxX + minX) / 2.0F, (maxY + minY) / 2.0F);

                addQuad(vc, mat, nMat,
                        maxX, minY, 0.0F,  minTextureX, maxTextureY,  0, 0, -1, lightFront);
                addQuad(vc, mat, nMat,
                        minX, minY, 0.0F,  maxTextureX, maxTextureY,  0, 0, -1, lightFront);
                addQuad(vc, mat, nMat,
                        minX, maxY, 0.0F,  maxTextureX, minTextureY,  0, 0, -1, lightFront);
                addQuad(vc, mat, nMat,
                        maxX, maxY, 0.0F,  minTextureX, minTextureY,  0, 0, -1, lightFront);

                // BACK quad (at z = depth)
                int lightBack = lightFront; // could compute separately, but usually fine
                addQuad(vc, mat, nMat,
                        maxX, minY, depth,  minTextureX, maxTextureY,  0, 0, 1, lightBack);
                addQuad(vc, mat, nMat,
                        minX, minY, depth,  maxTextureX, maxTextureY,  0, 0, 1, lightBack);
                addQuad(vc, mat, nMat,
                        minX, maxY, depth,  maxTextureX, minTextureY,  0, 0, 1, lightBack);
                addQuad(vc, mat, nMat,
                        maxX, maxY, depth,  minTextureX, minTextureY,  0, 0, 1, lightBack);

                for (float i = minX; i < maxX; i += 1.0F) {
                    float uMax = (centerWidth - i) / textureWidth;
                    float uMin = (centerWidth - (i + pixelSize)) / textureWidth;
                    int lightSide = computePackedLightForPixel(entity, i, (minY + maxY) / 2.0F);

                    // LEFT strip (normal -X)
                    addQuad(vc, mat, nMat,
                            i, minY, 0.0F,   uMax, maxTextureY,  -1, 0, 0, lightSide);
                    addQuad(vc, mat, nMat,
                            i, minY, depth,  uMax, maxTextureY,  -1, 0, 0, lightSide);
                    addQuad(vc, mat, nMat,
                            i, maxY, depth,  uMin, minTextureY,  -1, 0, 0, lightSide);
                    addQuad(vc, mat, nMat,
                            i, maxY, 0.0F,   uMin, minTextureY,  -1, 0, 0, lightSide);

                    // RIGHT strip (normal +X)
                    int lightSide2 = lightSide;
                    addQuad(vc, mat, nMat,
                            i + 1, minY, 0.0F,   uMax, maxTextureY,  1, 0, 0, lightSide2);
                    addQuad(vc, mat, nMat,
                            i + 1, minY, depth,  uMax, maxTextureY,  1, 0, 0, lightSide2);
                    addQuad(vc, mat, nMat,
                            i + 1, maxY, depth,  uMin, minTextureY,  1, 0, 0, lightSide2);
                    addQuad(vc, mat, nMat,
                            i + 1, maxY, 0.0F,   uMin, minTextureY,  1, 0, 0, lightSide2);
                }

                for (float i = minY; i < maxY; i += 1.0F) {
                    float vMax = (centerHeight - i) / textureHeight;
                    float vMin = (centerHeight - (i + pixelSize)) / textureHeight;

                    int lightTopBottom = computePackedLightForPixel(entity, (minX + maxX) / 2.0F, i);

                    // BOTTOM strip (normal -Y)
                    addQuad(vc, mat, nMat,
                            minX, i, 0.0F,    maxTextureX, vMax + 0.5F,   0, -1, 0, lightTopBottom);
                    addQuad(vc, mat, nMat,
                            minX, i, depth,   maxTextureX, vMin + 0.5F,   0, -1, 0, lightTopBottom);
                    addQuad(vc, mat, nMat,
                            maxX, i, depth,   minTextureX, vMin + 0.5F,   0, -1, 0, lightTopBottom);
                    addQuad(vc, mat, nMat,
                            maxX, i, 0.0F,    minTextureX, vMax + 0.5F,   0, -1, 0, lightTopBottom);

                    // TOP strip (normal +Y)
                    int lightTopBottom2 = lightTopBottom;
                    addQuad(vc, mat, nMat,
                            minX, i + 1, 0.0F,    maxTextureX, vMax + 0.5F,  0, 1, 0, lightTopBottom2);
                    addQuad(vc, mat, nMat,
                            minX, i + 1, depth,   maxTextureX, vMin + 0.5F,  0, 1, 0, lightTopBottom2);
                    addQuad(vc, mat, nMat,
                            maxX, i + 1, depth,   minTextureX, vMin + 0.5F,  0, 1, 0, lightTopBottom2);
                    addQuad(vc, mat, nMat,
                            maxX, i + 1, 0.0F,    minTextureX, vMax + 0.5F,  0, 1, 0, lightTopBottom2);
                }
            }
        }
    }

    private void addQuad(VertexConsumer vc, Matrix4f mat, Matrix3f nMat,
                         float x, float y, float z, float u, float v,
                         int nx, int ny, int nz, int light) {
        vc.vertex(mat, x, y, z)
                .color(1f, 1f, 1f, 1f)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(nMat, nx, ny, nz)
                .endVertex();
    }

    /**
     * the entity position, pixel offsets, and facing, then fetch packed light.
     */
    private int computePackedLightForPixel(BlueprintEntity entity, float xzOffset, float yOffset) {
        double baseX = entity.getX();
        double baseY = entity.getY();
        double baseZ = entity.getZ();

        int posX = (int)Math.floor(baseX);
        int posY = (int)Math.floor(baseY + (yOffset / 16.0F));
        int posZ = (int)Math.floor(baseZ);

        Direction dir = entity.getDirection();
        if (dir == Direction.NORTH) {
            posX = (int)Math.floor(baseX + (xzOffset / 16.0F));
        } else if (dir == Direction.WEST) {
            posZ = (int)Math.floor(baseZ - (xzOffset / 16.0F));
        } else if (dir == Direction.SOUTH) {
            posX = (int)Math.floor(baseX - (xzOffset / 16.0F));
        } else if (dir == Direction.EAST) {
            posZ = (int)Math.floor(baseZ + (xzOffset / 16.0F));
        }

        BlockPos bp = new BlockPos(posX, posY, posZ);
        return LevelRenderer.getLightColor(entity.level, bp);
    }
}
