package net.vit.jurassicreborn.common;


import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.worldgen.BiomeModification;
import net.vit.jurassicreborn.common.worldgen.DinosaurSpawnBiomeModifier;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.*;

public class CommonRegistries {

    public static DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, JurassicReborn.MODID);

    public static RegistryObject<Codec<BiomeModification>> BIOME_MODIFIER_CODEC = BIOME_MODIFIER_SERIALIZERS.register("biome_modifications", () ->{
        return RecordCodecBuilder.create(builder -> {
            return builder.group(
                    // declare fields
                    (Biome.LIST_CODEC.fieldOf("biomes").forGetter(BiomeModification::biomes)),
                    PlacedFeature.CODEC.fieldOf("feature").forGetter(BiomeModification::feature)
                    // declare constructor
            ).apply(builder, BiomeModification::new);

        });
    });

    public static RegistryObject<Codec<DinosaurSpawnBiomeModifier>> DINOSAUR_SPAWN_BIOME_MODIFIER_CODEC =
            BIOME_MODIFIER_SERIALIZERS.register("dinosaur_spawns", () -> DinosaurSpawnBiomeModifier.CODEC);


    public static List<OreConfiguration.TargetBlockState> ORE_FAUNA_FOSSIL_LIST;

    public static List<OreConfiguration.TargetBlockState> ORE_AMBER_LIST;

    public static List<OreConfiguration.TargetBlockState> ORE_ICE_SHARD_LIST;

    public static List<OreConfiguration.TargetBlockState> ORE_FLORA_FOSSIL_LIST;
    public static void addBlocksToBlockEntity(BlockEntityType<?> type, List<Block> blocks) {
        Set<Block> typeBlocks = type.validBlocks;
        List<Block> mutable = new ArrayList<>(typeBlocks);

        for (Block block : blocks) {
            if (!mutable.contains(block))
                mutable.add(block);
        }

        type.validBlocks = new HashSet<>(mutable);
    }

    public static void addLogsToStrippables(HashMap<Block, Block> logStrippedMap){
        Map<Block, Block> initialMap = AxeItem.STRIPPABLES;
        ImmutableMap.Builder<Block, Block> builder = new ImmutableMap.Builder<>();
        for(Block b : initialMap.keySet()){
            builder.put(b, initialMap.get(b));
        }
        for(Block b : logStrippedMap.keySet()){
            builder.put(b, logStrippedMap.get(b));
        }
        ImmutableMap<Block, Block> map = null;
        try{
            map = builder.build();
        }catch(IllegalArgumentException e){
            System.out.println(e);
        }
        if(map != null) {
            AxeItem.STRIPPABLES = builder.build();
        }

    }

    public static void init(){}

}
