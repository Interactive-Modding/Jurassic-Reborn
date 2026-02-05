package net.vit.jurassicreborn.common.blocks.grower;

import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class TreeGrower extends AbstractTreeGrower {

    private final Supplier<Holder<? extends ConfiguredFeature<?, ?>>> feature;
    public TreeGrower(Supplier<Holder<? extends ConfiguredFeature<?, ?>>> feature) {
        this.feature = feature;
    }

    @Nullable
    @Override
    protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource pRandom, boolean pLargeHive) {
        return feature.get();
    }
}
