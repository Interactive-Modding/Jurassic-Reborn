package net.vit.jurassicreborn.common;


import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.worldgen.BiomeModification;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
//import net.vit.jurassicreborn.common.worldgen.DinosaurSpawnBiomeModifier;

import java.util.*;

public class CommonRegistries {

    public static DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, JurassicReborn.MODID);

    public static DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<BiomeModification>> BIOME_MODIFIER_CODEC =
            BIOME_MODIFIER_SERIALIZERS.register("biome_modifications", () -> RecordCodecBuilder.mapCodec(builder -> {
            return builder.group(
                    // declare fields
                    (Biome.LIST_CODEC.fieldOf("biomes").forGetter(BiomeModification::biomes)),
                    PlacedFeature.CODEC.fieldOf("feature").forGetter(BiomeModification::feature)
                    // declare constructor
            ).apply(builder, BiomeModification::new);
        }));

//    public static DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<DinosaurSpawnBiomeModifier>> DINOSAUR_SPAWN_BIOME_MODIFIER_CODEC =
//            BIOME_MODIFIER_SERIALIZERS.register("dinosaur_spawns", () -> DinosaurSpawnBiomeModifier.CODEC);


    public static List<OreConfiguration.TargetBlockState> ORE_FAUNA_FOSSIL_LIST;

    public static List<OreConfiguration.TargetBlockState> ORE_AMBER_LIST;

    public static List<OreConfiguration.TargetBlockState> ORE_ICE_SHARD_LIST;

    public static List<OreConfiguration.TargetBlockState> ORE_FLORA_FOSSIL_LIST;
    public static void init(){}

}
