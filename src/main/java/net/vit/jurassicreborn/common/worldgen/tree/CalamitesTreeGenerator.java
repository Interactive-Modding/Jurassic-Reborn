package net.vit.jurassicreborn.common.worldgen.tree;

import com.mojang.serialization.Codec;
import net.vit.jurassicreborn.common.blocks.ModWoodTypes;
import net.vit.jurassicreborn.common.blocks.wood.AncientLeavesBlock;
import net.vit.jurassicreborn.common.blocks.wood.WoodBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import java.util.Random;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CalamitesTreeGenerator extends Feature<NoneFeatureConfiguration> {
    public CalamitesTreeGenerator(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockState log = WoodBlocks.CALAMITES_LOG.get().defaultBlockState();
        BlockState leaves = WoodBlocks.CALAMITES_LEAVES.get().defaultBlockState().setValue(AncientLeavesBlock.DISTANCE, 1);
        WorldGenLevel world = context.level();
        Random rand = context.random();
        BlockPos position = context.origin();



        int height = rand.nextInt(10) + 10;
        int branchIndex = 0;

        int halfDistance = height / 2;

        if(!this.canPlace(context, height, halfDistance))
            return false;

        world.setBlock(position, log, 19);

        for (int y = 0; y < height; y++) {
            BlockPos logPos = position.above(y);
            BaseTreeGenerator.setBlockState(world, logPos, log);

            boolean upperHalf = y > halfDistance;

            branchIndex++;

            if (branchIndex > (upperHalf ? 2 : 3)) {
                branchIndex = 0;
            }

            boolean branch = upperHalf ? branchIndex >= 2 : branchIndex >= 3;

            if (branch) {
                for (Direction facing : Direction.Plane.HORIZONTAL) {

                    BlockPos branchPos = logPos.relative(facing);
                    BlockState facingLog = log.setValue(RotatedPillarBlock.AXIS, facing.getAxis());

                    BaseTreeGenerator.setBlockState(world, branchPos, facingLog);
                    BaseTreeGenerator.setBlockState(world, branchPos.above(), leaves);

                    int leaveOut = Math.max(1, (upperHalf ? -(halfDistance - y) : (halfDistance - y) + halfDistance) / 2) + (rand.nextInt(2) - 1);

                    for (int i = 0; i < leaveOut; i++) {
                        BlockPos leavePos = branchPos.relative(facing, i + 1).above(i / 2 + 1);

                        BaseTreeGenerator.setBlockState(world, leavePos, leaves);

                        if (!upperHalf) {
                            if (i < leaveOut / 4 || height < 12) {
                                BaseTreeGenerator.setBlockState(world, leavePos.above(), leaves);
                            }

                            if (i < leaveOut - 2) {
                                BaseTreeGenerator.setBlockState(world, leavePos.below(), leaves);
                                BaseTreeGenerator.setBlockState(world, leavePos.relative(facing.getCounterClockWise()), leaves);
                                BaseTreeGenerator.setBlockState(world, leavePos.relative(facing.getClockWise()), leaves);
                            } else if (i >= leaveOut - 2) {
                                BaseTreeGenerator.setBlockState(world, leavePos.above(), leaves);
                            }
                        } else if (i >= leaveOut - 1) {
                            BaseTreeGenerator.setBlockState(world, leavePos.above(), leaves);
                            BaseTreeGenerator.setBlockState(world, leavePos.above(1).relative(facing), leaves);
                        }
                    }

                    if (!upperHalf) {
                        BaseTreeGenerator.setBlockState(world, branchPos.relative(facing).above(), facingLog);
                        BaseTreeGenerator.setBlockState(world, branchPos.relative(facing), leaves);
                    } else {
                        BaseTreeGenerator.setBlockState(world, branchPos.relative(facing).above(2), leaves);
                    }
                }
            }
        }

        for (int i = 0; i < height / 4 + 1; i++) {
            BaseTreeGenerator.setBlockState(world, position.above(height + i), leaves);
        }

        return true;
    }

    private boolean canPlace(FeaturePlaceContext<NoneFeatureConfiguration> pContext, int height, int halfDistance) {

        BlockPos.MutableBlockPos min = pContext.origin().mutable();

        min.move(-3, 0, -3);

        BlockPos.MutableBlockPos max = pContext.origin().mutable();

        max.move(3, height, 3);

//        for(int y = pContext.origin().getY(); y < branchHeight; y++){
//            if(!TreePlaceUtil.validTreePos(pContext.level(), pContext.origin().above(y))){
//                return false;
//            }
//        }


        for (int x = min.getX(); x < max.getX(); x++) {
            for (int y = min.getY(); y < max.getY(); y++) {
                for (int z = min.getZ(); z < max.getZ(); z++) {
                    if(!TreePlaceUtil.validTreePos(pContext.level(), new BlockPos(x, y, z))){
                        return false;
                    }
                }
            }
        }

        return true;

    }
}
