package net.vit.jurassicreborn.common.util.block;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class ModdedModel<I extends GeoAnimatable> extends GeoModel<I> {

    private ResourceLocation animationLocation;
    private ResourceLocation modelLocation;
    private ResourceLocation textureLocation;

    public ModdedModel(ResourceLocation model, ResourceLocation texture, ResourceLocation animation){
        this.modelLocation = model;
        this.textureLocation = texture;
        this.animationLocation = animation;
    }

    @Override
    public ResourceLocation getModelResource(I i) {
        return this.modelLocation;
    }

    @Override
    public ResourceLocation getTextureResource(I i) {
        return this.textureLocation;
    }

    @Override
    public ResourceLocation getAnimationResource(I i) {
        return this.animationLocation;
    }
}
