
package net.vit.jurassicreborn.common.worldgen;

import net.minecraft.world.level.levelgen.placement.*;
import java.util.List;

public final class ModOrePlacement {
    private ModOrePlacement() {}

    public static List<PlacementModifier> orePlacement(PlacementModifier countOrRarity, PlacementModifier height) {
        return List.of(countOrRarity, InSquarePlacement.spread(), height, BiomeFilter.biome());
    }
    public static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier height) {
        return orePlacement(CountPlacement.of(count), height);
    }
    public static List<PlacementModifier> rareOrePlacement(int onceEvery, PlacementModifier height) {
        return orePlacement(RarityFilter.onAverageOnceEvery(onceEvery), height);
    }
    public static List<PlacementModifier> chancedOrePlacement(PlacementModifier height) {
        return List.of(InSquarePlacement.spread(), height, BiomeFilter.biome());
    }
}
