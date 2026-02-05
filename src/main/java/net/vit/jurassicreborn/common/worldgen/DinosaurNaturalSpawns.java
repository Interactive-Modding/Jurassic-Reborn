package net.vit.jurassicreborn.common.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds a cached view of dinosaur spawn preferences so that biome modifiers can
 * later wire those spawns into the world. This does not actually register any
 * spawns yet; it just prepares the data in one place.
 */
public final class DinosaurNaturalSpawns {
    private static final Map<ResourceKey<Biome>, List<SpawnEntry>> BY_BIOME = new HashMap<>();
    private static boolean initialized;

    private DinosaurNaturalSpawns() {}

    private static void rebuildCache() {
        BY_BIOME.clear();
        for (Dinosaur dinosaur : Dinosaur.DINOS) {
            if (dinosaur == Dinosaur.EMPTY) {
                continue;
            }
            int weight = dinosaur.getSpawnChance();
            List<ResourceKey<Biome>> spawnBiomes = dinosaur.getSpawnBiomes();
            if (weight <= 0 || spawnBiomes == null || spawnBiomes.isEmpty()) {
                continue;
            }

            SpawnEntry entry = new SpawnEntry(dinosaur, weight);
            for (ResourceKey<Biome> biomeKey : spawnBiomes) {
                BY_BIOME.computeIfAbsent(biomeKey, ignored -> new ArrayList<>()).add(entry);
            }
        }
        initialized = true;
    }

    // ADD THIS METHOD
    public static void invalidate() {
        initialized = false;
        BY_BIOME.clear();
    }

    public static List<SpawnEntry> getSpawnsForBiome(Holder<Biome> biome) {
        if (!initialized) {
            rebuildCache();
        }
        return biome.unwrapKey()
                .map(key -> BY_BIOME.getOrDefault(key, List.of()))
                .orElse(List.of());
    }

    public record SpawnEntry(Dinosaur dinosaur, int weight) {}
}