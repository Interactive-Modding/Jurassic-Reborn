package net.vit.jurassicreborn.common.worldgen.tree.petrified;

import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.world.level.block.Block;
import net.vit.jurassicreborn.common.blocks.ModWoodTypes;
import net.vit.jurassicreborn.common.blocks.wood.WoodBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;

public class PetrifiedTreeGenerator extends Feature<PetrifiedTreeConfig> {

    public PetrifiedTreeGenerator(Codec<PetrifiedTreeConfig> p_65786_) {
        super(p_65786_);
    }

    @Override
    public boolean place(FeaturePlaceContext<PetrifiedTreeConfig> context) {
        RandomSource random = context.random();
        float chance = random.nextFloat();
        // Only attempt placement if the random roll is below the configured chance
        if (context.config().chance >= chance) {
            WorldGenLevel world = context.level();

            BlockPos pos = context.origin();



            int chunkX = world.getChunk(pos).getPos().getMinBlockX();
            int chunkZ = world.getChunk(pos).getPos().getMinBlockZ();

            int randPosX = chunkX + random.nextInt(16) + 8;
            int randPosZ = chunkZ + random.nextInt(16) + 8;
            int surfaceY = world.getHeight(Heightmap.Types.WORLD_SURFACE_WG, randPosX, randPosZ);
            int oceanFloorY = world.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, randPosX, randPosZ);

            int minY = 5;
            int maxY = Math.max(minY, surfaceY - 5); // Prevents going above surface
            int randPosY = random.nextInt(maxY - minY + 1) + minY;

            BlockPos targetPos = new BlockPos(randPosX, randPosY, randPosZ);

            if (world.getBlockState(targetPos).isAir() || !world.getBlockState(targetPos).getFluidState().isEmpty()) {
                return false;
            }

            this.generatePetrifiedTree(world, randPosX, randPosY, randPosZ, random, context.config());

            if(world.getBlockState(pos).getBlock() instanceof RotatedPillarBlock) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    private void generatePetrifiedTree(WorldGenLevel world,int x, int y, int z, RandomSource rand, PetrifiedTreeConfig config) {
        Predicate<BlockState> predicate = (state) ->  Feature.isReplaceable(BlockTags.FEATURES_CANNOT_REPLACE).test(state) || state.isAir();
        float rotX = (float) (rand.nextDouble() * 360.0F);
        float rotY = (float) (rand.nextDouble() * 360.0F) - 180.0F;

        Block log = Util.getRandom(WoodBlocks.petrifiedLogs(),rand).get();

        BlockState state = log.defaultBlockState();

        float horizontal = Mth.cos(rotX * (float) Math.PI / 180.0F);
        float vertical = Mth.sin(rotX * (float) Math.PI / 180.0F);

        float xOffset = -Mth.sin(rotY * (float) Math.PI / 180.0F) * horizontal;
        float yOffset = Mth.cos(rotY * (float) Math.PI / 180.0F) * horizontal;

        for (int i = 0; i < rand.nextInt(config.size) + 2; i++) {

            int blockX = x + Math.round(xOffset * i);
            int blockY = y + Math.round(vertical * i);
            int blockZ = z + Math.round(yOffset * i);
            if (blockY > world.getMinBuildHeight() && blockY < world.getMaxBuildHeight()) {
                BlockPos pos = new BlockPos(blockX, blockY, blockZ);

                if(world.isAreaLoaded(pos, 20))
                    this.safeSetBlock(world, pos, state, predicate);

            }
        }

    }

}
