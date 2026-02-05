package net.vit.jurassicreborn.common.blocks.parkBlocks;

import net.vit.jurassicreborn.JurassicReborn;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class TourRailModel extends AnimatedGeoModel<TourRailBlockEntity> {
    public TourRailModel() {
    }

    @Override
    public ResourceLocation getModelResource(TourRailBlockEntity tourRailBlockEntity) {
        return JurassicReborn.resource("geo/" + tourRailBlockEntity.getDirection().modelName + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TourRailBlockEntity tile) {
        return JurassicReborn.resource("textures/block/" + tile.getDirection().modelName + ".png");
    }

    /** the overlay-only stripe texture (alpha mask) */
    public ResourceLocation getStripeTexture(TourRailBlockEntity tile) {
        return JurassicReborn.resource("textures/block/" + tile.getDirection().modelName + "_stripe.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TourRailBlockEntity tourRailBlockEntity) {
        return JurassicReborn.resource("animations/" + tourRailBlockEntity.getDirection().modelName + ".animation.json");
    }
}
