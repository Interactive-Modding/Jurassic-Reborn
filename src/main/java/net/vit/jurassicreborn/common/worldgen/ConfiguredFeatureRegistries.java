package net.vit.jurassicreborn.common.worldgen;

import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.CommonRegistries;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.worldgen.tree.petrified.PetrifiedTreeConfig;

import java.util.List;

public class ConfiguredFeatureRegistries {


    public static void init() {


        JRTreeFeatures.AraucariaTree = FeatureUtils.register("araucaria_tree", ModFeatures.AraucariaTreePre.get(), new NoneFeatureConfiguration());
        JRTreeFeatures.GinkgoTree = FeatureUtils.register("ginkgo_tree", ModFeatures.GinkgoTreePre.get(), new NoneFeatureConfiguration());
        JRTreeFeatures.CalamitesTreeFeature = FeatureUtils.register("calamites_tree", ModFeatures.CalamitesTreePre.get(), new NoneFeatureConfiguration());
        JRTreeFeatures.PhoenixTreeFeature = FeatureUtils.register("phoenix_tree", ModFeatures.PhoenixTreePre.get(), new NoneFeatureConfiguration());
        JRTreeFeatures.PsaroniusTree = FeatureUtils.register("psaronius_tree", ModFeatures.PsaroniusTreePre.get(), new NoneFeatureConfiguration());
        JRTreeFeatures.MagnoliaTreeFeature = FeatureUtils.register("magnolia_tree", ModFeatures.MagnoliaTreePre.get(), new NoneFeatureConfiguration());


        CommonRegistries.ORE_FAUNA_FOSSIL_LIST = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.FAUNA_FOSSIL.get().defaultBlockState()));
        CommonRegistries.ORE_AMBER_LIST = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.AMBER_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_AMBER_ORE.get().defaultBlockState()));
        CommonRegistries.ORE_ICE_SHARD_LIST = List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.AMBER_ORE.get().defaultBlockState()), OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_AMBER_ORE.get().defaultBlockState()));

        CommonRegistries.ORE_FLORA_FOSSIL_LIST = List.of(
                OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.FLORA_FOSSIL.get().defaultBlockState()),
                OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.DEEPSLATE_FLORA_FOSSIL.get().defaultBlockState())
        );
        ModConfiguredFeatures.CONFIGURED_FLORA_FOSSIL = FeatureUtils.register(
                JurassicReborn.MODID + ":flora_fossil_configured",
                ModFeatures.FLORA_FOSSIL_ORE.get(),
                new OreConfiguration(CommonRegistries.ORE_FLORA_FOSSIL_LIST, 17)
        );
        ModPlacements.FLORA_FOSSIL_PLACEMENT = PlacementUtils.register(JurassicReborn.MODID + ":placement_flora_fossil", ModConfiguredFeatures.CONFIGURED_FLORA_FOSSIL, commonOrePlacement(5, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));
        ModConfiguredFeatures.CONFIGURED_FAUNA_FOSSIL = FeatureUtils.register(JurassicReborn.MODID + ":fauna_fossil_configured", Feature.ORE, new OreConfiguration(CommonRegistries.ORE_FAUNA_FOSSIL_LIST, 3));
        ModPlacements.FAUNA_FOSSIL_PLACEMENT = PlacementUtils.register(JurassicReborn.MODID + ":placement_fauna_fossil", ModConfiguredFeatures.CONFIGURED_FAUNA_FOSSIL, commonOrePlacement(3, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));

        ModConfiguredFeatures.CONFIGURED_AMBER_ORE = FeatureUtils.register(JurassicReborn.MODID + ":amber_ore_configured", Feature.ORE, new OreConfiguration(CommonRegistries.ORE_AMBER_LIST, 3));
        ModConfiguredFeatures.CONFIGURED_ICE_SHARD_ORE = FeatureUtils.register(JurassicReborn.MODID + ":ice_shard_ore_configured", Feature.ORE, new OreConfiguration(CommonRegistries.ORE_ICE_SHARD_LIST, 3));
        ModPlacements.AMBER_ORE_PLACEMENT = PlacementUtils.register(JurassicReborn.MODID + ":placement_amber_ore", ModConfiguredFeatures.CONFIGURED_AMBER_ORE, commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));
        ModPlacements.ICE_SHARD_ORE_PLACEMENT = PlacementUtils.register(JurassicReborn.MODID + ":placement_ice_shard_ore", ModConfiguredFeatures.CONFIGURED_ICE_SHARD_ORE, commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));
        ModConfiguredFeatures.CONFIGURED_PEAT_DISK = FeatureUtils.register(JurassicReborn.MODID + ":configured_peat_disk", Feature.DISK, new DiskConfiguration(RuleBasedBlockStateProvider.simple(ModBlocks.PEAT.get().defaultBlockState().getBlock()), BlockPredicate.matchesBlocks(Blocks.DIRT, Blocks.MUD, Blocks.GRASS_BLOCK), UniformInt.of(1, 2), 2));
        ModPlacements.PEAT_DISK_PLACEMENT = PlacementUtils.register(JurassicReborn.MODID + ":placed_peat_disk", ModConfiguredFeatures.CONFIGURED_PEAT_DISK, List.of(RarityFilter.onAverageOnceEvery(5), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));
        ModConfiguredFeatures.CONFIGURED_NEST_FOSSIL = FeatureUtils.register(JurassicReborn.MODID + ":configured_nest_fossil", ModFeatures.NEST_FOSSIL_FEATURE.get(), new NoneFeatureConfiguration());
        ModPlacements.NEST_FOSSIL_PLACEMENT = PlacementUtils.register(JurassicReborn.MODID + ":placed_nest_fossil", ModConfiguredFeatures.CONFIGURED_NEST_FOSSIL, chancedOrePlacment(HeightRangePlacement.uniform(VerticalAnchor.absolute(30), VerticalAnchor.absolute(50))));
        ModConfiguredFeatures.CONFIGURED_SMALL_PETRIFIED_TREE = FeatureUtils.register(JurassicReborn.MODID + ":configured_small_petrified_tree", ModFeatures.PETRIFIED_TREE_GENERATOR.get(), new PetrifiedTreeConfig(10, 0.2F));
        ModConfiguredFeatures.CONFIGURED_LARGE_PETRIFIED_TREE = FeatureUtils.register(JurassicReborn.MODID + ":configured_large_petrified_tree", ModFeatures.PETRIFIED_TREE_GENERATOR.get(), new PetrifiedTreeConfig(15, 0.1F));
        ModPlacements.PLACED_LARGE_PETRIFIED_TREE = PlacementUtils.register(JurassicReborn.MODID + ":placed_large_petrified_tree", ModConfiguredFeatures.CONFIGURED_LARGE_PETRIFIED_TREE, chancedOrePlacment(HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));
        ModPlacements.PLACED_SMALL_PETRIFIED_TREE = PlacementUtils.register(JurassicReborn.MODID + ":placed_small_petrified_tree", ModConfiguredFeatures.CONFIGURED_SMALL_PETRIFIED_TREE, chancedOrePlacment(HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));

    }

    private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    private static List<PlacementModifier> chancedOrePlacment(PlacementModifier p_195348_) {
        return List.of(InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int p_195344_, PlacementModifier p_195345_) {
        return orePlacement(CountPlacement.of(p_195344_), p_195345_);
    }
}
