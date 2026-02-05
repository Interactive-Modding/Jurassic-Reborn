package net.vit.jurassicreborn.common.worldgen.structure;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.vit.jurassicreborn.JurassicReborn;

/**
 * Centralises the {@link ResourceKey} definitions for JR structures so they can be reused by gameplay systems
 * such as villager trades.
 */
public final class ModStructureKeys {
    public static final ResourceKey<Structure> JP_SAN_DIEGO = key("jp_san_diego");
    public static final ResourceKey<Structure> ISLA_SORNA_LAB = key("isla_sorna_lab");
    public static final ResourceKey<Structure> VISITOR_CENTRE = key("visitor_centre");

    private ModStructureKeys() {}

    private static ResourceKey<Structure> key(String name) {
        return ResourceKey.create(Registry.STRUCTURE_REGISTRY, JurassicReborn.resource(name));
    }
}
