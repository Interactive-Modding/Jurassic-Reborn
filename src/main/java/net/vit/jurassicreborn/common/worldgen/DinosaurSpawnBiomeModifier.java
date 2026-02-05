package net.vit.jurassicreborn.common.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.vit.jurassicreborn.common.CommonRegistries;
import net.vit.jurassicreborn.common.RebornConfig;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.ModEntities;

/**
 * Adds dinosaur natural spawns using the data provided by {@link DinosaurNaturalSpawns}.
 * This modifier is driven entirely by a biome modifier JSON entry so that spawn
 * application is data-driven instead of hard coded.
 */
public final class DinosaurSpawnBiomeModifier implements BiomeModifier {
    public static final DinosaurSpawnBiomeModifier INSTANCE = new DinosaurSpawnBiomeModifier();
    public static final Codec<DinosaurSpawnBiomeModifier> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase != Phase.ADD || !RebornConfig.spawnDinosaursNaturally) {
            return;
        }

        if (biome.is(BiomeTags.IS_NETHER) || biome.is(BiomeTags.IS_END) || biome.is(Tags.Biomes.IS_VOID)) {
            return;
        }

        var dinosaurSpawns = DinosaurNaturalSpawns.getSpawnsForBiome(biome);
        if (dinosaurSpawns.isEmpty()) {
            return;
        }

        for (var spawnEntry : dinosaurSpawns) {
            var dinosaur = spawnEntry.dinosaur();
            var entityType = ModEntities.getTypeForDinosaur(dinosaur).orElse(null);

            if (entityType == null) {
                logMissingDinosaurType(dinosaur);
                continue;
            }

            var category = dinosaur.isMarineCreature()
                    ? MobCategory.WATER_CREATURE
                    : MobCategory.CREATURE;

            int herdSize = Math.max(1, Math.min(4, dinosaur.getMaxHerdSize()));
            builder.getMobSpawnSettings().addSpawn(
                    category,
                    new MobSpawnSettings.SpawnerData(entityType, spawnEntry.weight(), 1, herdSize)
            );
            logSpawnAddition(dinosaur.getName(), biome, category, spawnEntry.weight(), 1, herdSize);
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return CommonRegistries.DINOSAUR_SPAWN_BIOME_MODIFIER_CODEC.get();
    }

    private static void logSpawnAddition(String name, Holder<Biome> biome, MobCategory category, int weight, int min, int max) {
//        JurassicReborn.getLogger().info(
//                "Registering natural spawn for {} in biome {} (category={}, weight={}, groupSize={}–{})",
//                name,
//                biome.unwrapKey().map(Object::toString).orElse("<unknown>"),
//                category.getName(),
//                weight,
//                min,
//                max
//        );
    }

    private static void logMissingDinosaurType(Dinosaur dinosaur) {
//        JurassicReborn.getLogger().warn("Skipping natural spawn for {} because its entity type is not registered", dinosaur.getName());
    }
}
