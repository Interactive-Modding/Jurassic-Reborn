package net.vit.jurassicreborn.common.util.block;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModdedModel<I extends IAnimatable> extends AnimatedGeoModel<I> {

    private ResourceLocation animationLocation;
    private ResourceLocation modelLocation;
    private ResourceLocation textureLocation;

    public ModdedModel(ResourceLocation model, ResourceLocation texture, ResourceLocation animation){
        this.modelLocation = model;
        this.textureLocation = texture;
        this.animationLocation = animation;
    }

    @Override
    public ResourceLocation getModelLocation(I i) {
        return this.modelLocation;
    }

    @Override
    public ResourceLocation getTextureLocation(I i) {
        return this.textureLocation;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(I i) {
        return this.animationLocation;
    }
}
