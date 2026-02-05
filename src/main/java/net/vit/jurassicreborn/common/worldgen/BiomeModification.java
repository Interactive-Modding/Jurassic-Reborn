package net.vit.jurassicreborn.common.worldgen;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.vit.jurassicreborn.common.RebornConfig;
import net.vit.jurassicreborn.common.entities.ModEntities;
import net.vit.jurassicreborn.common.worldgen.ModPlacements;

import java.util.Optional;

import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;

public final class BiomeModification {

    private static final TagKey<Biome> IS_END = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation("minecraft", "is_end"));
    private static final TagKey<Biome> FORGE_MOUNTAIN = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation("forge", "is_mountain"));

    private BiomeModification() {
    }

    @SubscribeEvent
    public static void onBiomeLoading(final BiomeLoadingEvent event) {
        ResourceLocation name = event.getName();
        if (name == null) {
            return;
        }

        ResourceKey<Biome> key = ResourceKey.create(Registry.BIOME_REGISTRY, name);
        Optional<Holder<Biome>> optional = ForgeRegistries.BIOMES.getHolder(key);
        if (optional.isEmpty()) {
            return;
        }

        Holder<Biome> biome = optional.get();
        if (biome.is(BiomeTags.IS_NETHER) || biome.is(IS_END)) {
            return;
        }

        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.ICE_SHARD_ORE_PLACEMENT);
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.FAUNA_FOSSIL_PLACEMENT);
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.FLORA_FOSSIL_PLACEMENT);
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.NEST_FOSSIL_PLACEMENT);
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.AMBER_ORE_PLACEMENT);
        event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.GYPSUM_STONE_PLACEMENT);
        for (Holder<PlacedFeature> placement : ModPlacements.DINO_FOSSIL_PLACEMENTS) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, placement);
        }

        if (biome.is(Tags.Biomes.IS_SWAMP)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, ModPlacements.PEAT_DISK_PLACEMENT);
        }

        if (!biome.is(Tags.Biomes.IS_VOID)) {
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.PLACED_LARGE_PETRIFIED_TREE);
            event.getGeneration().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.PLACED_SMALL_PETRIFIED_TREE);
        }

        if (RebornConfig.spawnCrabs && (biome.is(Tags.Biomes.IS_WET) || biome.is(BiomeTags.IS_OCEAN))) {
            event.getSpawns().addSpawn(
                    MobCategory.WATER_CREATURE,
                    new MobSpawnSettings.SpawnerData(ModEntities.CRAB.get(), 8, 2, 4)
            );
        }

        if (RebornConfig.spawnSharks && biome.is(BiomeTags.IS_OCEAN)) {
            event.getSpawns().addSpawn(
                    MobCategory.WATER_CREATURE,
                    new MobSpawnSettings.SpawnerData(ModEntities.SHARK.get(), 2, 1, 2)
            );
        }

        if (RebornConfig.spawnGoats && (biome.is(BiomeTags.IS_MOUNTAIN) || biome.is(FORGE_MOUNTAIN))) {
            event.getSpawns().addSpawn(
                    MobCategory.CREATURE,
                    new MobSpawnSettings.SpawnerData(ModEntities.GOAT.get(), 6, 2, 4)
            );
        }
    }
}
