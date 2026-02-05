package net.vit.jurassicreborn.common.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.*;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.CommonRegistries;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.worldgen.tree.petrified.PetrifiedTreeConfig;
import net.vit.jurassicreborn.common.plants.WestIndianLilacBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ConfiguredFeatureRegistries {

    private static final List<FossilOreDefinition> DINO_FOSSILS = List.of(
            new FossilOreDefinition("achillobator", ModBlocks.ACHILLOBATOR_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("alligator_gar", ModBlocks.ALLIGATOR_GAR_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("allosaurus", ModBlocks.ALLOSAURUS_FOSSIL, 6, 1, 16, 30, true, true),
            new FossilOreDefinition("alvarezsaurus", ModBlocks.ALVAREZSAURUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("ankylosaurus", ModBlocks.ANKYLOSAURUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("apatosaurus", ModBlocks.APATOSAURUS_FOSSIL, 6, 1, 16, 30, true, true),
            new FossilOreDefinition("arsinoitherium", ModBlocks.ARSINOITHERIUM_FOSSIL, 6, 1, 48, 58, true, true),
            new FossilOreDefinition("asteroceras", ModBlocks.ASTEROCERAS_FOSSIL, 6, 1, 4, 16, true, true),
            new FossilOreDefinition("baryonyx", ModBlocks.BARYONYX_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("beelzebufo", ModBlocks.BEELZEBUFO_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("brachiosaurus", ModBlocks.BRACHIOSAURUS_FOSSIL, 6, 1, 16, 30, true, true),
            new FossilOreDefinition("calymene", ModBlocks.CALYMENE_FOSSIL, 6, 1, -50, -36, false, true),
            new FossilOreDefinition("camarasaurus", ModBlocks.CAMARASAURUS_FOSSIL, 6, 1, 16, 30, true, true),
            new FossilOreDefinition("cameroceras", ModBlocks.CAMEROCERAS_FOSSIL, 6, 1, -50, -40, false, true),
            new FossilOreDefinition("carcharodontosaurus", ModBlocks.CARCHARODONTOSAURUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("carnotaurus", ModBlocks.CARNOTAURUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("cearadactylus", ModBlocks.CEARADACTYLUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("ceratosaurus", ModBlocks.CERATOSAURUS_FOSSIL, 6, 1, 16, 30, true, true),
            new FossilOreDefinition("chasmosaurus", ModBlocks.CHASMOSAURUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("chilesaurus", ModBlocks.CHILESAURUS_FOSSIL, 6, 1, 16, 30, true, true),
            new FossilOreDefinition("coelacanth", ModBlocks.COELACANTH_FOSSIL, 6, 1, -36, 72, true, true),
            new FossilOreDefinition("coelurus", ModBlocks.COELURUS_FOSSIL, 6, 1, 16, 30, true, true),
            new FossilOreDefinition("compsognathus", ModBlocks.COMPSOGNATHUS_FOSSIL, 6, 1, 16, 30, true, true),
            new FossilOreDefinition("corythosaurus", ModBlocks.CORYTHOSAURUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("crassigyrinus", ModBlocks.CRASSIGYRINUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("deinosuchus", ModBlocks.DEINOSUCHUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("deinotherium", ModBlocks.DEINOTHERIUM_FOSSIL, 6, 1, 48, 58, true, true),
            new FossilOreDefinition("dilophosaurus", ModBlocks.DILOPHOSAURUS_FOSSIL, 6, 1, 16, 30, true, true),
            new FossilOreDefinition("dimetrodon", ModBlocks.DIMETRODON_FOSSIL, 6, 1, -6, 4, false, true),
            new FossilOreDefinition("dimorphodon", ModBlocks.DIMORPHODON_FOSSIL, 6, 1, 16, 30, true, true),
            new FossilOreDefinition("diplocaulus", ModBlocks.DIPLOCAULUS_FOSSIL, 6, 1, -6, 4, false, true),
            new FossilOreDefinition("diplodocus", ModBlocks.DIPLODOCUS_FOSSIL, 6, 1, 16, 30, true, true),
            new FossilOreDefinition("dodo", ModBlocks.DODO_FOSSIL, 6, 1, 58, 72, true, true),
            new FossilOreDefinition("dreadnoughtus", ModBlocks.DREADNOUGHTUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("dunkleosteus", ModBlocks.DUNKLEOSTEUS_FOSSIL, 6, 1, -36, -20, false, true),
            new FossilOreDefinition("edmontosaurus", ModBlocks.EDMONTOSAURUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("elasmotherium", ModBlocks.ELASMOTHERIUM_FOSSIL, 6, 1, 58, 72, true, true),
            new FossilOreDefinition("endoceras", ModBlocks.ENDOCERAS_FOSSIL, 6, 1, -50, -40, false, true),
            new FossilOreDefinition("gallimimus", ModBlocks.GALLIMIMUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("giganotosaurus", ModBlocks.GIGANOTOSAURUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("guanlong", ModBlocks.GUANLONG_FOSSIL, 6, 1, 16, 30, true, true),
            new FossilOreDefinition("herrerasaurus", ModBlocks.HERRERASAURUS_FOSSIL, 6, 1, 4, 16, true, true),
            new FossilOreDefinition("hyaenodon", ModBlocks.HYAENODON_FOSSIL, 6, 1, 48, 58, true, true),
            new FossilOreDefinition("hypsilophodon", ModBlocks.HYPSILOPHODON_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("kairuku", ModBlocks.KAIRUKU_FOSSIL, 6, 1, 48, 58, true, true),
            new FossilOreDefinition("lambeosaurus", ModBlocks.LAMBEOSAURUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("leaellynasaura", ModBlocks.LEAELLYNASAURA_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("leptictidium", ModBlocks.LEPTICTIDIUM_FOSSIL, 6, 1, 48, 58, true, true),
            new FossilOreDefinition("livyatan", ModBlocks.LIVYATAN_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("ludodactylus", ModBlocks.LUDODACTYLUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("maiasaura", ModBlocks.MAIASAURA_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("majungasaurus", ModBlocks.MAJUNGASAURUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("mamenchisaurus", ModBlocks.MAMENCHISAURUS_FOSSIL, 6, 1, 16, 30, true, true),
            new FossilOreDefinition("mammoth", ModBlocks.MAMMOTH_FOSSIL, 6, 1, 58, 72, true, true),
            new FossilOreDefinition("mawsonia", ModBlocks.MAWSONIA_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("megalodon", ModBlocks.MEGALODON_FOSSIL, 6, 1, 58, 72, true, true),
            new FossilOreDefinition("megapiranha", ModBlocks.MEGAPIRANHA_FOSSIL, 6, 1, 58, 72, true, true),
            new FossilOreDefinition("megatherium", ModBlocks.MEGATHERIUM_FOSSIL, 6, 1, 58, 72, true, true),
            new FossilOreDefinition("metriacanthosaurus", ModBlocks.METRIACANTHOSAURUS_FOSSIL, 6, 1, 16, 30, true, true),
            new FossilOreDefinition("microceratus", ModBlocks.MICROCERATUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("microraptor", ModBlocks.MICRORAPTOR_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("moganopterus", ModBlocks.MOGANOPTERUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("mosasaurus", ModBlocks.MOSASAURUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("mussaurus", ModBlocks.MUSSAURUS_FOSSIL, 6, 1, 4, 16, true, true),
            new FossilOreDefinition("nigersaurus", ModBlocks.NIGERSAURUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("ornithomimus", ModBlocks.ORNITHOMIMUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("orthoceras", ModBlocks.ORTHOCERAS_FOSSIL, 6, 1, -50, -40, false, true),
            new FossilOreDefinition("othnielia", ModBlocks.OTHNIELIA_FOSSIL, 6, 1, 16, 30, true, true),
            new FossilOreDefinition("oviraptor", ModBlocks.OVIRAPTOR_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("pachycephalosaurus", ModBlocks.PACHYCEPHALOSAURUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("paraceratherium", ModBlocks.PARACERATHERIUM_FOSSIL, 6, 1, 48, 58, true, true),
            new FossilOreDefinition("parapuzosia", ModBlocks.PARAPUZOSIA_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("parasaurolophus", ModBlocks.PARASAUROLOPHUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("patagotitan", ModBlocks.PATAGOTITAN_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("perisphinctes", ModBlocks.PERISPHINCTES_FOSSIL, 6, 1, 16, 30, true, true),
            new FossilOreDefinition("postosuchus", ModBlocks.POSTOSUCHUS_FOSSIL, 6, 1, 4, 16, true, true),
            new FossilOreDefinition("proceratosaurus", ModBlocks.PROCERATOSAURUS_FOSSIL, 6, 1, 16, 30, true, true),
            new FossilOreDefinition("protoceratops", ModBlocks.PROTOCERATOPS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("pteranodon", ModBlocks.PTERANODON_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("quetzal", ModBlocks.QUETZAL_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("rugops", ModBlocks.RUGOPS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("segisaurus", ModBlocks.SEGISAURUS_FOSSIL, 6, 1, 16, 30, true, true),
            new FossilOreDefinition("sinoceratops", ModBlocks.SINOCERATOPS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("smilodon", ModBlocks.SMILODON_FOSSIL, 6, 1, 48, 64, true, true),
            new FossilOreDefinition("spinosaurus", ModBlocks.SPINOSAURUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("stegosaurus", ModBlocks.STEGOSAURUS_FOSSIL, 6, 1, 16, 30, true, true),
            new FossilOreDefinition("styracosaurus", ModBlocks.STYRACOSAURUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("suchomimus", ModBlocks.SUCHOMIMUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("therizinosaurus", ModBlocks.THERIZINOSAURUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("titanis", ModBlocks.TITANIS_FOSSIL, 6, 1, 48, 58, true, true),
            new FossilOreDefinition("titanites", ModBlocks.TITANITES_FOSSIL, 6, 1, 16, 30, true, true),
            new FossilOreDefinition("triceratops", ModBlocks.TRICERATOPS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("troodon", ModBlocks.TROODON_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("tropeognathus", ModBlocks.TROPEOGNATHUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("tylosaurus", ModBlocks.TYLOSAURUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("tyrannosaurus", ModBlocks.TYRANNOSAURUS_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("vectipelta", ModBlocks.VECTIPELTA_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("velociraptor", ModBlocks.VELOCIRAPTOR_FOSSIL, 6, 1, 30, 48, true, true),
            new FossilOreDefinition("zhenyuanopterus", ModBlocks.ZHENYUANOPTERUS_FOSSIL, 6, 1, 30, 48, true, true)
    );


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
        ModConfiguredFeatures.CONFIGURED_PEAT_DISK = FeatureUtils.register(JurassicReborn.MODID + ":configured_peat_disk", Feature.DISK, new DiskConfiguration(ModBlocks.PEAT.get().defaultBlockState(), UniformInt.of(2, 3),2,List.of(Blocks.DIRT.defaultBlockState(), Blocks.GRASS_BLOCK.defaultBlockState())));
        ModPlacements.PEAT_DISK_PLACEMENT = PlacementUtils.register(JurassicReborn.MODID + ":placed_peat_disk", ModConfiguredFeatures.CONFIGURED_PEAT_DISK, List.of(RarityFilter.onAverageOnceEvery(5), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));
        ModConfiguredFeatures.CONFIGURED_NEST_FOSSIL = FeatureUtils.register(JurassicReborn.MODID + ":configured_nest_fossil", ModFeatures.NEST_FOSSIL_FEATURE.get(), new NoneFeatureConfiguration());
        ModPlacements.NEST_FOSSIL_PLACEMENT = PlacementUtils.register(JurassicReborn.MODID + ":placed_nest_fossil", ModConfiguredFeatures.CONFIGURED_NEST_FOSSIL, chancedOrePlacment(HeightRangePlacement.uniform(VerticalAnchor.absolute(30), VerticalAnchor.absolute(50))));
        ModConfiguredFeatures.CONFIGURED_SMALL_PETRIFIED_TREE = FeatureUtils.register(JurassicReborn.MODID + ":configured_small_petrified_tree", ModFeatures.PETRIFIED_TREE_GENERATOR.get(), new PetrifiedTreeConfig(10, 0.2F));
        ModConfiguredFeatures.CONFIGURED_LARGE_PETRIFIED_TREE = FeatureUtils.register(JurassicReborn.MODID + ":configured_large_petrified_tree", ModFeatures.PETRIFIED_TREE_GENERATOR.get(), new PetrifiedTreeConfig(15, 0.1F));
        ModPlacements.PLACED_LARGE_PETRIFIED_TREE = PlacementUtils.register(JurassicReborn.MODID + ":placed_large_petrified_tree", ModConfiguredFeatures.CONFIGURED_LARGE_PETRIFIED_TREE, chancedOrePlacment(HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));
        ModPlacements.PLACED_SMALL_PETRIFIED_TREE = PlacementUtils.register(JurassicReborn.MODID + ":placed_small_petrified_tree", ModConfiguredFeatures.CONFIGURED_SMALL_PETRIFIED_TREE, chancedOrePlacment(HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));

        CommonRegistries.ORE_GYPSUM_STONE_LIST = List.of(
                OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, ModBlocks.GYPSUM_STONE.get().defaultBlockState()),
                OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.GYPSUM_STONE.get().defaultBlockState())
        );
        ModConfiguredFeatures.CONFIGURED_GYPSUM_STONE = FeatureUtils.register(
                JurassicReborn.MODID + ":gypsum_stone",
                Feature.ORE,
                new OreConfiguration(CommonRegistries.ORE_GYPSUM_STONE_LIST, 17)
        );
        ModPlacements.GYPSUM_STONE_PLACEMENT = PlacementUtils.register(
                JurassicReborn.MODID + ":gypsum_stone_placed",
                ModConfiguredFeatures.CONFIGURED_GYPSUM_STONE,
                commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.absolute(-80), VerticalAnchor.absolute(192)))
        );

        ModConfiguredFeatures.CONFIGURED_GRACILARIA_PATCH = FeatureUtils.register(
                JurassicReborn.MODID + ":gracilaria_patch",
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        64,
                        1,
                        0,
                        PlacementUtils.inlinePlaced(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(SimpleStateProvider.simple(ModBlocks.GRACILARIA.get().defaultBlockState()))
                        )
                )
        );
        ModPlacements.GRACILARIA_PATCH_PLACEMENT = PlacementUtils.register(
                JurassicReborn.MODID + ":gracilaria_patch_placed",
                ModConfiguredFeatures.CONFIGURED_GRACILARIA_PATCH,
                List.of(
                        CountPlacement.of(1),
                        RarityFilter.onAverageOnceEvery(10),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                        BiomeFilter.biome()
                )
        );

        ModConfiguredFeatures.CONFIGURED_HELICONIA_PATCH = FeatureUtils.register(
                JurassicReborn.MODID + ":heliconia_patch",
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        96,
                        2,
                        3,
                        PlacementUtils.inlinePlaced(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        SimpleStateProvider.simple(
                                                ModBlocks.HELICONIA.get().defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
                                        )
                                )
                        )
                )
        );
        ModPlacements.HELICONIA_PATCH_PLACEMENT = PlacementUtils.register(
                JurassicReborn.MODID + ":heliconia_patch_placed",
                ModConfiguredFeatures.CONFIGURED_HELICONIA_PATCH,
                List.of(
                        RarityFilter.onAverageOnceEvery(5),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()
                )
        );

        ModConfiguredFeatures.CONFIGURED_WEST_INDIAN_LILAC_PATCH = FeatureUtils.register(
                JurassicReborn.MODID + ":west_indian_lilac_patch",
                Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(
                        96,
                        3,
                        3,
                        PlacementUtils.inlinePlaced(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockConfiguration(
                                        SimpleStateProvider.simple(
                                                ModBlocks.WEST_INDIAN_LILAC.get().defaultBlockState()
                                                        .setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
                                                        .setValue(WestIndianLilacBlock.AGE, 0)
                                        )
                                )
                        )
                )
        );
        ModPlacements.WEST_INDIAN_LILAC_PATCH_PLACEMENT = PlacementUtils.register(
                JurassicReborn.MODID + ":west_indian_lilac_patch_placed",
                ModConfiguredFeatures.CONFIGURED_WEST_INDIAN_LILAC_PATCH,
                List.of(
                        RarityFilter.onAverageOnceEvery(5),
                        InSquarePlacement.spread(),
                        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                        BiomeFilter.biome()
                )
        );

        registerDinoFossils();

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

    private static void registerDinoFossils() {
        ModPlacements.DINO_FOSSIL_PLACEMENTS.clear();
        for (FossilOreDefinition fossil : DINO_FOSSILS) {
            List<OreConfiguration.TargetBlockState> targets = new ArrayList<>();
            if (fossil.generateInStone()) {
                targets.add(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, fossil.block().get().defaultBlockState()));
            }
            if (fossil.generateInDeepslate()) {
                targets.add(OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, fossil.block().get().defaultBlockState()));
            }
            if (targets.isEmpty()) {
                continue;
            }

            Holder<ConfiguredFeature<OreConfiguration, ?>> configured = FeatureUtils.register(
                    JurassicReborn.MODID + ":" + fossil.name() + "_fossil",
                    ModFeatures.FOSSIL_ORE.get(),
                    new OreConfiguration(targets, fossil.size())
            );
            Holder<PlacedFeature> placed = PlacementUtils.register(
                    JurassicReborn.MODID + ":" + fossil.name() + "_fossil_placed",
                    configured,
                    commonOrePlacement(
                            fossil.count(),
                            HeightRangePlacement.uniform(
                                    VerticalAnchor.absolute(fossil.minY()),
                                    VerticalAnchor.absolute(fossil.maxY())
                            )
                    )
            );
            ModPlacements.DINO_FOSSIL_PLACEMENTS.add(placed);
        }
    }

    private record FossilOreDefinition(String name,
                                       Supplier<? extends Block> block,
                                       int size,
                                       int count,
                                       int minY,
                                       int maxY,
                                       boolean generateInStone,
                                       boolean generateInDeepslate) {
    }
}
