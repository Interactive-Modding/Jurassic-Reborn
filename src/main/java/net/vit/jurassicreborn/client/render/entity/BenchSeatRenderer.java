package net.vit.jurassicreborn.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.vit.jurassicreborn.common.entities.ParkBenchSeatBaseEntity;

public class BenchSeatRenderer<T extends ParkBenchSeatBaseEntity> extends EntityRenderer<T> {

    public BenchSeatRenderer(EntityRendererProvider.Context context) {
        super(context);

        this.shadowRadius = 0.0F;
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        // Intentionally empty: invisible seat
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        // Safe placeholder (won’t be used since render() is empty)
        return MissingTextureAtlasSprite.getLocation();
    }
}
