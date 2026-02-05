package net.vit.jurassicreborn.common.worldgen.tree;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * A specialized generator for Magnolia trees with a wide, spreading canopy
 * and visible branching structure like classic magnolia trees.
 */
public class MagnoliaTreeGenerator extends Feature<NoneFeatureConfiguration> {

    private final Supplier<BlockState> trunk;
    private final Supplier<BlockState> leaves;

    public MagnoliaTreeGenerator(Codec<NoneFeatureConfiguration> codec, Supplier<BlockState> trunk, Supplier<BlockState> leaves) {
        super(codec);
        this.trunk = trunk;
        this.leaves = leaves;
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        RandomSource random = context.random();
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();

        int trunkHeight = 6 + random.nextInt(3);

        if (!this.canPlace(context, trunkHeight + 6)) {
            return false;
        }

        BlockState log = trunk.get();
        BlockState leaves = this.leaves.get();


        for (int y = 0; y <= trunkHeight; y++) {
            this.setBlockState(level, origin.above(y), log);
        }


        List<BlockPos> branchTips = new ArrayList<>();


        int branchStartHeight = 4;
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            int branchY = branchStartHeight + random.nextInt(2);
            List<BlockPos> tips = this.createSpreadingBranch(level, origin.above(branchY), dir, random, log, 3 + random.nextInt(3));
            branchTips.addAll(tips);
        }


        for (int i = 0; i < 3; i++) {
            Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            int branchY = trunkHeight - 1 + random.nextInt(2);
            List<BlockPos> tips = this.createSpreadingBranch(level, origin.above(branchY), dir, random, log, 3 + random.nextInt(2));
            branchTips.addAll(tips);
        }


        for (int i = 0; i < 3; i++) {
            Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            List<BlockPos> tips = this.createUpwardBranch(level, origin.above(trunkHeight), dir, random, log);
            branchTips.addAll(tips);
        }


        for (BlockPos tip : branchTips) {
            this.addFoliageCluster(level, tip, random, leaves, 2 + random.nextInt(2));
        }


        this.addScatteredCanopy(level, origin.above(trunkHeight - 1), random, leaves);

        return true;
    }

    private List<BlockPos> createSpreadingBranch(WorldGenLevel level, BlockPos start, Direction dir,
                                                 RandomSource random, BlockState log, int length) {
        List<BlockPos> tips = new ArrayList<>();
        BlockPos current = start;


        for (int i = 0; i < length; i++) {
            current = current.relative(dir);
            BlockState horizontalLog = log.setValue(RotatedPillarBlock.AXIS, dir.getAxis());
            this.setBlockState(level, current, horizontalLog);


            if (i > 0 && i % 2 == 0) {
                current = current.above();
                this.setBlockState(level, current, log);
            }


            if (i >= 2 && random.nextFloat() < 0.5) {
                Direction subDir = random.nextBoolean() ? dir.getClockWise() : dir.getCounterClockWise();
                BlockPos subBranch = this.createSubBranch(level, current, subDir, random, log, 2);
                tips.add(subBranch);
            }
        }

        tips.add(current);
        return tips;
    }

    private List<BlockPos> createUpwardBranch(WorldGenLevel level, BlockPos start, Direction dir,
                                              RandomSource random, BlockState log) {
        List<BlockPos> tips = new ArrayList<>();
        BlockPos current = start;
        int length = 2 + random.nextInt(2);

        for (int i = 0; i < length; i++) {
            current = current.above();
            this.setBlockState(level, current, log);

            if (i > 0) {
                current = current.relative(dir);
                BlockState horizontalLog = log.setValue(RotatedPillarBlock.AXIS, dir.getAxis());
                this.setBlockState(level, current, horizontalLog);
            }
        }

        tips.add(current);
        return tips;
    }

    private BlockPos createSubBranch(WorldGenLevel level, BlockPos start, Direction dir,
                                     RandomSource random, BlockState log, int length) {
        BlockPos current = start;

        for (int i = 0; i < length; i++) {
            current = current.relative(dir);
            BlockState horizontalLog = log.setValue(RotatedPillarBlock.AXIS, dir.getAxis());
            this.setBlockState(level, current, horizontalLog);
        }

        return current;
    }

    private void addFoliageCluster(WorldGenLevel level, BlockPos center, RandomSource random,
                                   BlockState leaves, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    double distance = Math.sqrt(x * x + y * y + z * z);


                    if (distance <= radius + random.nextDouble() * 0.6) {
                        BlockPos leafPos = center.offset(x, y, z);
                        if (!level.getBlockState(leafPos).is(trunk.get().getBlock())) {
                            this.setBlockState(level, leafPos, leaves);
                        }
                    }
                }
            }
        }
    }

    private void addScatteredCanopy(WorldGenLevel level, BlockPos center, RandomSource random, BlockState leaves) {

        for (int y = -2; y <= 3; y++) {
            int radius = 5 - Math.abs(y - 1);

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    double distance = Math.sqrt(x * x + z * z);


                    double probability = 1.0 - (distance / (radius + 1)) * 0.7;

                    if (distance <= radius && random.nextDouble() < probability) {
                        BlockPos leafPos = center.offset(x, y, z);
                        BlockState currentState = level.getBlockState(leafPos);


                        if (currentState.isAir()) {
                            this.setBlockState(level, leafPos, leaves);
                        }
                    }
                }
            }
        }
    }

    private boolean canPlace(FeaturePlaceContext<NoneFeatureConfiguration> context, int height) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();


        for (int y = 0; y <= height; y++) {
            if (!TreePlaceUtil.validTreePos(level, origin.above(y))) {
                return false;
            }
        }


        int canopyRadius = 7;
        int minY = origin.getY() + 3;
        int maxY = origin.getY() + height;

        for (int y = minY; y <= maxY; y++) {
            for (int x = -canopyRadius; x <= canopyRadius; x++) {
                for (int z = -canopyRadius; z <= canopyRadius; z++) {
                    if (x * x + z * z > canopyRadius * canopyRadius) {
                        continue;
                    }

                    BlockPos checkPos = origin.offset(x, y - origin.getY(), z);
                    if (!TreePlaceUtil.validTreePos(level, checkPos)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void setBlockState(WorldGenLevel world, BlockPos pos, BlockState state) {
        BaseTreeGenerator.setBlockState(world, pos, state);
    }
}
