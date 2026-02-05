package net.vit.jurassicreborn.client.render;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ModMaterials {


    public static Material CLEANING_STATION_MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, ModTextures.CLEANING_STATION_TEXTURE);






    private static final Cache<ResourceLocation, Material> CACHED_MATERIALS = CacheBuilder.newBuilder()
            .expireAfterAccess(2, TimeUnit.MINUTES)
            .build();


    public static Material getBlockMaterial(ResourceLocation bockTexture) {
        try {
            return CACHED_MATERIALS.get(bockTexture, () -> new Material(TextureAtlas.LOCATION_BLOCKS, bockTexture));
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
