package net.vit.jurassicreborn.common.blocks.grower;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.function.Supplier;

public class TreeGrower extends AbstractTreeGrower {

    private final Supplier<ResourceKey<ConfiguredFeature<?, ?>>> feature;

    public TreeGrower(Supplier<ResourceKey<ConfiguredFeature<?, ?>>> feature) {
        this.feature = feature;
    }

    @Override
    protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource pRandom, boolean pLargeHive) {
        return feature.get();
    }
}
