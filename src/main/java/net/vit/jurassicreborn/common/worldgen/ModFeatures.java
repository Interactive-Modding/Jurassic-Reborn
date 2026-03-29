package net.vit.jurassicreborn.common.worldgen;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.wood.AncientLeavesBlock;
import net.vit.jurassicreborn.common.blocks.wood.WoodBlocks;
import net.vit.jurassicreborn.common.worldgen.tree.*;
import net.vit.jurassicreborn.common.worldgen.tree.petrified.PetrifiedTreeConfig;
import net.vit.jurassicreborn.common.worldgen.tree.petrified.PetrifiedTreeGenerator;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, JurassicReborn.MODID );
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> MagnoliaTreePre = FEATURES.register("magnolia_tree_pre", () -> new MagnoliaTreeGenerator(NoneFeatureConfiguration.CODEC, () -> WoodBlocks.MAGNOLIA_LOG.get().defaultBlockState(), () -> WoodBlocks.MAGNOLIA_LEAVES.get().defaultBlockState().setValue(AncientLeavesBlock.DISTANCE, 1)));
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> PsaroniusTreePre = FEATURES.register("psaronius_tree_pre", () -> new PsaroniusTreeGenerator(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> PhoenixTreePre = FEATURES.register("phoenix_tree_pre", () -> new PhoenixTreeGenerator(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> CalamitesTreePre = FEATURES.register("calamites_tree_pre", () -> new CalamitesTreeGenerator(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> GinkgoTreePre = FEATURES.register("ginkgo_tree_pre", () -> BaseTreeGenerator.ginkgo(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> AraucariaTreePre = FEATURES.register("araucaria_tree_pre", () -> new AraucariaTreeGenerator(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<OreConfiguration>> FLORA_FOSSIL_ORE = FEATURES.register(
            "flora_fossil_ore",
            VariantOreFeature::new
    );
    public static final DeferredHolder<Feature<?>, Feature<OreConfiguration>> FOSSIL_ORE = FEATURES.register("fossil_ore", FossilOreFeature::new);
    public static final DeferredHolder<Feature<?>, Feature<PetrifiedTreeConfig>> PETRIFIED_TREE_GENERATOR = FEATURES.register("petrified_tree_generation",() ->new PetrifiedTreeGenerator(PetrifiedTreeConfig.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<NoneFeatureConfiguration>> NEST_FOSSIL_FEATURE = FEATURES.register("nest_fossil", () -> new NestFossilFeature(NoneFeatureConfiguration.CODEC));
}
