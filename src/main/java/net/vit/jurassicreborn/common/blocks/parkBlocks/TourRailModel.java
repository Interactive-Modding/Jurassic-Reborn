package net.vit.jurassicreborn.common.blocks.parkBlocks;

import net.minecraft.resources.ResourceLocation;
import net.vit.jurassicreborn.JurassicReborn;
import software.bernie.geckolib.model.GeoModel;

public class TourRailModel extends GeoModel<TourRailBlockEntity> {

    @Override
    public ResourceLocation getModelResource(TourRailBlockEntity entity) {
        return JurassicReborn.resource(
                "geo/" + entity.getDirection().modelName + ".geo.json"
        );
    }

    @Override
    public ResourceLocation getTextureResource(TourRailBlockEntity entity) {
        return JurassicReborn.resource(
                "textures/block/" + entity.getDirection().modelName + ".png"
        );
    }

    // Optional: custom stripe texture
    public ResourceLocation getStripeTexture(TourRailBlockEntity entity) {
        return JurassicReborn.resource(
                "textures/block/" + entity.getDirection().modelName + "_stripe.png"
        );
    }

    @Override
    public ResourceLocation getAnimationResource(TourRailBlockEntity entity) {
        return JurassicReborn.resource(
                "animations/" + entity.getDirection().modelName + ".animation.json"
        );
    }
}
