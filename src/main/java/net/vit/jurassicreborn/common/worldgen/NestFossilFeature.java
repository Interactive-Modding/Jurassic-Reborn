package net.vit.jurassicreborn.common.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.common.Tags;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.fossil.NestFossilBlock;

import java.util.List;

public class NestFossilFeature extends Feature<NoneFeatureConfiguration> {

    public NestFossilFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level  = context.level();
        RandomSource   rand  = context.random();
        BlockPos       origin = context.origin();

        /* ---------- biome-based spawn chance ---------- */
        int nestChance = 100;                            // default (rarer)
        Holder<Biome> biome = level.getBiome(origin);

        if (biome.is(BiomeTags.IS_MOUNTAIN)              // mountains & peaks
                || biome.is(BiomeTags.IS_HILL)           // windswept hills, etc.
                || biome.is(BiomeTags.IS_BADLANDS)       // mesa / badlands
                || biome.is(Tags.Biomes.IS_SANDY)) {     // deserts & beaches
            nestChance = 30;                             // ~3× more common
        }

        if (rand.nextInt(nestChance) != 0) {             // RNG roll failed
            return false;
        }
        /* --------------------------------------------- */

        int size = rand.nextInt(3) + 6;   // 6-8 blocks square

        // pick a nearby position (x/z within chunk, y ≈ 30-50)
        BlockPos pos = origin.offset(
                rand.nextInt(16 - size),
                rand.nextInt(20) + 30 - origin.getY(),
                rand.nextInt(16 - size));
        /* ----------- blocks to generate --------------- */
        BlockState nest = ModBlocks.NEST_FOSSIL.get()
                .defaultBlockState()
                .setValue(NestFossilBlock.VARIANT, rand.nextInt(3));

        List<BlockState> trackways = List.of(
                ModBlocks.FOSSILIZED_TRACKWAY_RAPTOR.get().defaultBlockState(),
                ModBlocks.FOSSILIZED_TRACKWAY_BIPED_SMALL.get().defaultBlockState(),
                ModBlocks.FOSSILIZED_TRACKWAY_BIPED_MEDIUM.get().defaultBlockState());

        BlockState trackway = trackways.get(rand.nextInt(trackways.size()));
        if (trackway.hasProperty(DirectionalBlock.FACING)) {
            trackway = trackway.setValue(DirectionalBlock.FACING,
                    Direction.Plane.HORIZONTAL.getRandomDirection(rand));
        }
        /* --------------------------------------------- */

        /* ----- lay down the gravel/terracotta pad ----- */
        for (int dx = 0; dx < size; dx++) {
            for (int dz = 0; dz < size; dz++) {
                BlockPos p = pos.offset(dx, 0, dz);
                BlockState ground = level.getBlockState(p);

                if (!ground.isAir() && ground.getFluidState().isEmpty()) {
                    BlockState replace = null;

                    if (rand.nextFloat() < 0.8F) {                // 80 % chance to replace
                        if (rand.nextFloat() < 0.1F) {            // 10 %   → trackway
                            replace = trackway;
                        } else if (rand.nextFloat() < 0.6F) {     // 54 %   → gravel
                            replace = Blocks.GRAVEL.defaultBlockState();
                        } else {                                  // 16 %   → terracotta
                            replace = rand.nextBoolean()
                                    ? Blocks.WHITE_TERRACOTTA.defaultBlockState()
                                    : Blocks.LIGHT_GRAY_TERRACOTTA.defaultBlockState();
                        }
                    }
                    if (replace != null) {
                        level.setBlock(p, replace, 2);
                    }
                }
            }
        }
        /* --------------------------------------------- */

        /* ------------ place the actual nests ---------- */
        for (int i = 0; i < rand.nextInt(2) + 1; i++) {
            BlockPos p = pos.offset(rand.nextInt(size), 0, rand.nextInt(size));
            BlockState ground = level.getBlockState(p);

            if (!ground.isAir() && ground.getFluidState().isEmpty()) {
                level.setBlock(p, nest, 2);
            }
        }
        /* --------------------------------------------- */

        return true;
    }
}
