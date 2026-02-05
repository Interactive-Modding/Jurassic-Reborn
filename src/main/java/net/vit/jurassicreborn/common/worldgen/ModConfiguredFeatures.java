package net.vit.jurassicreborn.common.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.vit.jurassicreborn.common.worldgen.tree.petrified.PetrifiedTreeConfig;

public class ModConfiguredFeatures {

    public static Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> CONFIGURED_ARAUCARIA;
    public static Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> CONFIGURED_GINKGO;

    public static Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> CONFIGURED_CALAMITES;

    public static Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> CONFIGURED_PHOENIX;

    public static Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> CONFIGURED_PSARONIUS;

    public static Holder<ConfiguredFeature<OreConfiguration, ?>> CONFIGURED_FLORA_FOSSIL;
    public static Holder<ConfiguredFeature<OreConfiguration, ?>> CONFIGURED_FAUNA_FOSSIL;
    public static Holder<ConfiguredFeature<OreConfiguration, ?>> CONFIGURED_AMBER_ORE;
    public static Holder<ConfiguredFeature<OreConfiguration, ?>> CONFIGURED_ICE_SHARD_ORE;
    public static Holder<ConfiguredFeature<PetrifiedTreeConfig, ?>> CONFIGURED_SMALL_PETRIFIED_TREE;
    public static Holder<ConfiguredFeature<PetrifiedTreeConfig, ?>> CONFIGURED_LARGE_PETRIFIED_TREE;
    public static Holder<ConfiguredFeature<NoneFeatureConfiguration, ?>> CONFIGURED_NEST_FOSSIL;
    public static Holder<ConfiguredFeature<DiskConfiguration, ?>> CONFIGURED_PEAT_DISK;
}
