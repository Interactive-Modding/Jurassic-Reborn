package net.vit.jurassicreborn.client.render.entity.model;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.vehicle.boat.ModBoatType;

public final class JurassicBoatModelLayers {
    private JurassicBoatModelLayers() {
    }

    public static ModelLayerLocation createBoatModelName(ModBoatType type) {
        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "boat/" + type.getSerializedName()), "main");
    }

    public static ModelLayerLocation createChestBoatModelName(ModBoatType type) {
        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "chest_boat/" + type.getSerializedName()), "main");
    }
}
