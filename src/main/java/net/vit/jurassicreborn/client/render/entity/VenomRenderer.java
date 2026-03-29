package net.vit.jurassicreborn.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.vit.jurassicreborn.common.entities.VenomEntity;

public class VenomRenderer extends EntityRenderer<VenomEntity> {
    public VenomRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(VenomEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
    }

    @Override
    public ResourceLocation getTextureLocation(VenomEntity entity) {
        return MissingTextureAtlasSprite.getLocation();
    }
}