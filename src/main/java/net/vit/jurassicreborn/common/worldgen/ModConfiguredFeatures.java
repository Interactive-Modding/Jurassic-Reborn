package net.vit.jurassicreborn.common.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.worldgen.tree.petrified.PetrifiedTreeConfig;

import java.util.List;

public final class ModConfiguredFeatures {

    private ModConfiguredFeatures() {}

    // ---------- Keys ----------
    public static final ResourceKey<ConfiguredFeature<?, ?>> ARAUCARIA          = key("araucaria_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GINKGO             = key("ginkgo_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CALAMITES          = key("calamites_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PHOENIX            = key("phoenix_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PSARONIUS          = key("psaronius_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MAGNOLIA           = key("magnolia_tree");

    public static final ResourceKey<ConfiguredFeature<?, ?>> FLORA_FOSSIL_ORE   = key("flora_fossil_configured");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FAUNA_FOSSIL_ORE   = key("fauna_fossil_configured");
    public static final ResourceKey<ConfiguredFeature<?, ?>> AMBER_ORE          = key("amber_ore_configured");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_SHARD_ORE      = key("ice_shard_ore_configured");

    public static final ResourceKey<ConfiguredFeature<?, ?>> NEST_FOSSIL        = key("configured_nest_fossil");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PETRIFIED_TREE_SMALL = key("configured_small_petrified_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PETRIFIED_TREE_LARGE = key("configured_large_petrified_tree");

    // ---------- Bootstrap ----------
    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> ctx) {
        // Rule tests (targets)
        var STONE_ORE_TARGET     = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        var DEEPSLATE_ORE_TARGET = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

        // ORE target lists
        var floraTargets = List.of(
                OreConfiguration.target(STONE_ORE_TARGET,     ModBlocks.FLORA_FOSSIL.get().defaultBlockState()),
                OreConfiguration.target(DEEPSLATE_ORE_TARGET, ModBlocks.DEEPSLATE_FLORA_FOSSIL.get().defaultBlockState())
        );

        var faunaTargets = List.of(
                OreConfiguration.target(STONE_ORE_TARGET, ModBlocks.FAUNA_FOSSIL.get().defaultBlockState())
        );

        var amberTargets = List.of(
                OreConfiguration.target(STONE_ORE_TARGET,     ModBlocks.AMBER_ORE.get().defaultBlockState()),
                OreConfiguration.target(DEEPSLATE_ORE_TARGET, ModBlocks.DEEPSLATE_AMBER_ORE.get().defaultBlockState())
        );

        // The original code accidentally pointed at Amber; leaving a sensible placeholder:
        var iceShardTargets = amberTargets;

        register(ctx, ARAUCARIA, FeatureUtils.withConfig(ModFeatures.AraucariaTreePre.get(), NoneFeatureConfiguration.INSTANCE));
        register(ctx, GINKGO,    FeatureUtils.withConfig(ModFeatures.GinkgoTreePre.get(),    NoneFeatureConfiguration.INSTANCE));
        register(ctx, CALAMITES, FeatureUtils.withConfig(ModFeatures.CalamitesTreePre.get(), NoneFeatureConfiguration.INSTANCE));
        register(ctx, PHOENIX,   FeatureUtils.withConfig(ModFeatures.PhoenixTreePre.get(),   NoneFeatureConfiguration.INSTANCE));
        register(ctx, PSARONIUS, FeatureUtils.withConfig(ModFeatures.PsaroniusTreePre.get(), NoneFeatureConfiguration.INSTANCE));
        register(ctx, MAGNOLIA,  FeatureUtils.withConfig(ModFeatures.MagnoliaTreePre.get(),  NoneFeatureConfiguration.INSTANCE));

        // Ores
        register(ctx, FLORA_FOSSIL_ORE, Feature.ORE, new OreConfiguration(floraTargets, 17));
        register(ctx, FAUNA_FOSSIL_ORE, Feature.ORE, new OreConfiguration(faunaTargets, 3));
        register(ctx, AMBER_ORE,        Feature.ORE, new OreConfiguration(amberTargets, 3));
        register(ctx, ICE_SHARD_ORE,    Feature.ORE, new OreConfiguration(iceShardTargets, 3));

        // Special features
        register(ctx, NEST_FOSSIL, FeatureUtils.withConfig(ModFeatures.NEST_FOSSIL_FEATURE.get(), NoneFeatureConfiguration.INSTANCE));
        register(ctx, PETRIFIED_TREE_SMALL, FeatureUtils.withConfig(ModFeatures.PETRIFIED_TREE_GENERATOR.get(), new PetrifiedTreeConfig(10, 0.2F)));
        register(ctx, PETRIFIED_TREE_LARGE, FeatureUtils.withConfig(ModFeatures.PETRIFIED_TREE_GENERATOR.get(), new PetrifiedTreeConfig(15, 0.1F)));
    }

    // ---------- Helpers ----------
    private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(JurassicReborn.MODID, name));
    }

    private static <C extends FeatureConfiguration> void register(
            BootstapContext<ConfiguredFeature<?, ?>> ctx,
            ResourceKey<ConfiguredFeature<?, ?>> key,
            Feature< C > feature,
            C config
    ) {
        ctx.register(key, new ConfiguredFeature<>(feature, config));
    }

    private static void register(
            BootstapContext<ConfiguredFeature<?, ?>> ctx,
            ResourceKey<ConfiguredFeature<?, ?>> key,
            ConfiguredFeature<?, ?> configured
    ) {
        ctx.register(key, configured);
    }

    // Small convenience to keep tree lines tidy
    private static final class FeatureUtils {
        static <C extends FeatureConfiguration> ConfiguredFeature<C, ?> withConfig(Feature<C> f, C c) {
            return new ConfiguredFeature<>(f, c);
        }
    }
}
