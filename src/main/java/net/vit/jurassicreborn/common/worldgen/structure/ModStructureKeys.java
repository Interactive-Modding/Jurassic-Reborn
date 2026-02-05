package net.vit.jurassicreborn.common.worldgen.structure;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.vit.jurassicreborn.JurassicReborn;

/**
 * Centralises the {@link ResourceKey} definitions for JR structures so they can be reused by gameplay systems
 * such as villager trades.
 */
public final class ModStructureKeys {
    public static final ResourceKey<ConfiguredStructureFeature<?, ?>> JP_SAN_DIEGO = key("jp_san_diego");
    public static final ResourceKey<ConfiguredStructureFeature<?, ?>> ISLA_SORNA_LAB = key("isla_sorna_lab");
    public static final ResourceKey<ConfiguredStructureFeature<?, ?>> VISITOR_CENTRE = key("visitor_centre");

    private ModStructureKeys() {}

    private static ResourceKey<ConfiguredStructureFeature<?, ?>> key(String name) {
        return ResourceKey.create(Registry.CONFIGURED_STRUCTURE_FEATURE_REGISTRY, JurassicReborn.resource(name));
    }
}
