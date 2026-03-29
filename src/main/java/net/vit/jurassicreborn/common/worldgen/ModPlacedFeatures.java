// ModPlacedFeatures.java
package net.vit.jurassicreborn.common.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.vit.jurassicreborn.JurassicReborn;

import java.util.List;

public final class ModPlacedFeatures {
    private ModPlacedFeatures() {}

    public static final ResourceKey<PlacedFeature> FLORA_FOSSIL     = key("placement_flora_fossil");
    public static final ResourceKey<PlacedFeature> FAUNA_FOSSIL     = key("placement_fauna_fossil");
    public static final ResourceKey<PlacedFeature> AMBER_ORE        = key("placement_amber_ore");
    public static final ResourceKey<PlacedFeature> ICE_SHARD_ORE    = key("placement_ice_shard_ore");
    public static final ResourceKey<PlacedFeature> PETRIFIED_LARGE  = key("placed_large_petrified_tree");
    public static final ResourceKey<PlacedFeature> PETRIFIED_SMALL  = key("placed_small_petrified_tree");
    public static final ResourceKey<PlacedFeature> NEST_FOSSIL      = key("placed_nest_fossil");

    public static void bootstrap(BootstrapContext<PlacedFeature> ctx) {
        HolderGetter<ConfiguredFeature<?, ?>> cf = ctx.lookup(Registries.CONFIGURED_FEATURE);

        Holder<ConfiguredFeature<?, ?>> flora = cf.getOrThrow(ModConfiguredFeatures.FLORA_FOSSIL_ORE);
        Holder<ConfiguredFeature<?, ?>> fauna = cf.getOrThrow(ModConfiguredFeatures.FAUNA_FOSSIL_ORE);
        Holder<ConfiguredFeature<?, ?>> amber = cf.getOrThrow(ModConfiguredFeatures.AMBER_ORE);
        Holder<ConfiguredFeature<?, ?>> ice   = cf.getOrThrow(ModConfiguredFeatures.ICE_SHARD_ORE);
        Holder<ConfiguredFeature<?, ?>> nest  = cf.getOrThrow(ModConfiguredFeatures.NEST_FOSSIL);
        Holder<ConfiguredFeature<?, ?>> petrL = cf.getOrThrow(ModConfiguredFeatures.PETRIFIED_TREE_LARGE);
        Holder<ConfiguredFeature<?, ?>> petrS = cf.getOrThrow(ModConfiguredFeatures.PETRIFIED_TREE_SMALL);

        register(ctx, FLORA_FOSSIL, flora,
                ModOrePlacement.commonOrePlacement(5,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));

        register(ctx, FAUNA_FOSSIL, fauna,
                ModOrePlacement.commonOrePlacement(3,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));

        register(ctx, AMBER_ORE, amber,
                ModOrePlacement.commonOrePlacement(2,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));

        register(ctx, ICE_SHARD_ORE, ice,
                ModOrePlacement.commonOrePlacement(2,
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));

        register(ctx, NEST_FOSSIL, nest,
                ModOrePlacement.chancedOrePlacement(
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(30), VerticalAnchor.absolute(50))));

        register(ctx, PETRIFIED_LARGE, petrL,
                ModOrePlacement.chancedOrePlacement(
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));

        register(ctx, PETRIFIED_SMALL, petrS,
                ModOrePlacement.chancedOrePlacement(
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(64))));
    }

    private static ResourceKey<PlacedFeature> key(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.parse(JurassicReborn.MODID + ":" + name));
    }

    private static void register(BootstrapContext<PlacedFeature> ctx,
                                 ResourceKey<PlacedFeature> key,
                                 Holder<ConfiguredFeature<?, ?>> configured,
                                 List<PlacementModifier> modifiers) {
        ctx.register(key, new PlacedFeature(configured, List.copyOf(modifiers)));
    }
}
