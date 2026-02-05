package net.vit.jurassicreborn.common.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.vit.jurassicreborn.common.RebornConfig;
import net.vit.jurassicreborn.common.entities.ModEntities;

import static net.vit.jurassicreborn.common.CommonRegistries.*;
import static net.vit.jurassicreborn.common.worldgen.ModPlacements.PLACED_SMALL_PETRIFIED_TREE;

public record BiomeModification(HolderSet<Biome> biomes, Holder<PlacedFeature> feature) implements BiomeModifier {



    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {

        if(phase == Phase.ADD) {
            if (!biome.is(BiomeTags.IS_NETHER) && !biome.is(BiomeTags.IS_END)) {
                builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.ICE_SHARD_ORE_PLACEMENT);
                builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.FAUNA_FOSSIL_PLACEMENT);
                builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.FLORA_FOSSIL_PLACEMENT);
                builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.NEST_FOSSIL_PLACEMENT);
                builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.AMBER_ORE_PLACEMENT);
//                if (biome.is(Tags.Biomes.IS_SWAMP)) {
//                    builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.PEAT_DISK_PLACEMENT);
//                }
                if (!biome.is(Tags.Biomes.IS_VOID)) {
                    builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacements.PLACED_LARGE_PETRIFIED_TREE);
                    builder.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, PLACED_SMALL_PETRIFIED_TREE);
                }
                if (RebornConfig.spawnCrabs &&
                        (biome.is(Tags.Biomes.IS_WET) || biome.is(BiomeTags.IS_OCEAN))) {
                    builder.getMobSpawnSettings().addSpawn(
                            MobCategory.WATER_CREATURE,
                            new MobSpawnSettings.SpawnerData(ModEntities.CRAB.get(), 8, 2, 4)
                    );
                }

                if (RebornConfig.spawnSharks && biome.is(BiomeTags.IS_OCEAN)) {
                    builder.getMobSpawnSettings().addSpawn(
                            MobCategory.WATER_CREATURE,
                            new MobSpawnSettings.SpawnerData(ModEntities.SHARK.get(), 2, 1, 2)
                    );
                }

                if (RebornConfig.spawnGoats &&
                        (biome.is(BiomeTags.IS_MOUNTAIN) || biome.is(Tags.Biomes.IS_MOUNTAIN))) {
                    builder.getMobSpawnSettings().addSpawn(
                            MobCategory.CREATURE,
                            new MobSpawnSettings.SpawnerData(ModEntities.GOAT.get(), 6, 2, 4)
                    );
                }


            }
        }
    }

    public Codec<? extends BiomeModifier> codec()
    {
        return BIOME_MODIFIER_CODEC.get();
    }
}
