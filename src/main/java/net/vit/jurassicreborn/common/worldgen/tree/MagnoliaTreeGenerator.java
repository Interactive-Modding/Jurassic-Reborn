package net.vit.jurassicreborn.common.worldgen.tree;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import java.util.Random;
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
        Random random = context.random();
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();

        int trunkHeight = 6 + random.nextInt(3);

        if (!this.canPlace(context, trunkHeight + 6)) {
            return false;
        }

        BlockState log = trunk.get();
        BlockState leaves = this.leaves.get();

        // Build main trunk (short and stocky)
        for (int y = 0; y <= trunkHeight; y++) {
            this.setBlockState(level, origin.above(y), log);
        }

        // Track all branch endpoints for foliage
        List<BlockPos> branchTips = new ArrayList<>();

        // Create major spreading branches from low on the trunk
        int branchStartHeight = 4;
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            int branchY = branchStartHeight + random.nextInt(2);
            List<BlockPos> tips = this.createSpreadingBranch(level, origin.above(branchY), dir, random, log, 3 + random.nextInt(3));
            branchTips.addAll(tips);
        }

        // Add additional branches at mid-height for fuller canopy
        for (int i = 0; i < 3; i++) {
            Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            int branchY = trunkHeight - 1 + random.nextInt(2);
            List<BlockPos> tips = this.createSpreadingBranch(level, origin.above(branchY), dir, random, log, 3 + random.nextInt(2));
            branchTips.addAll(tips);
        }

        // Create top branches
        for (int i = 0; i < 3; i++) {
            Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
            List<BlockPos> tips = this.createUpwardBranch(level, origin.above(trunkHeight), dir, random, log);
            branchTips.addAll(tips);
        }

        // Add dense foliage clusters at all branch tips
        for (BlockPos tip : branchTips) {
            this.addFoliageCluster(level, tip, random, leaves, 2 + random.nextInt(2));
        }

        // Fill in with additional scattered foliage for density
        this.addScatteredCanopy(level, origin.above(trunkHeight - 1), random, leaves);

        return true;
    }

    private List<BlockPos> createSpreadingBranch(WorldGenLevel level, BlockPos start, Direction dir,
                                                 Random random, BlockState log, int length) {
        List<BlockPos> tips = new ArrayList<>();
        BlockPos current = start;

        // Main branch extends outward and slightly up
        for (int i = 0; i < length; i++) {
            current = current.relative(dir);
            BlockState horizontalLog = log.setValue(RotatedPillarBlock.AXIS, dir.getAxis());
            this.setBlockState(level, current, horizontalLog);

            // Gradually angle upward
            if (i > 0 && i % 2 == 0) {
                current = current.above();
                this.setBlockState(level, current, log);
            }

            // Add sub-branches for more complexity
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
                                              Random random, BlockState log) {
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
                                     Random random, BlockState log, int length) {
        BlockPos current = start;

        for (int i = 0; i < length; i++) {
            current = current.relative(dir);
            BlockState horizontalLog = log.setValue(RotatedPillarBlock.AXIS, dir.getAxis());
            this.setBlockState(level, current, horizontalLog);
        }

        return current;
    }

    private void addFoliageCluster(WorldGenLevel level, BlockPos center, Random random,
                                   BlockState leaves, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    double distance = Math.sqrt(x * x + y * y + z * z);

                    // Dense clusters with some randomness for natural shape
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

    private void addScatteredCanopy(WorldGenLevel level, BlockPos center, Random random, BlockState leaves) {
        // Create a wide, rounded canopy that fills the space between branches
        for (int y = -2; y <= 3; y++) {
            int radius = 5 - Math.abs(y - 1);

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    double distance = Math.sqrt(x * x + z * z);

                    // Scatter leaves with decreasing probability as distance increases
                    double probability = 1.0 - (distance / (radius + 1)) * 0.7;

                    if (distance <= radius && random.nextDouble() < probability) {
                        BlockPos leafPos = center.offset(x, y, z);
                        BlockState currentState = level.getBlockState(leafPos);

                        // Only place if it's air (don't overwrite branches or existing leaves)
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

        BlockPos.MutableBlockPos min = origin.mutable();
        min.move(-8, 0, -8);
        BlockPos.MutableBlockPos max = origin.mutable();
        max.move(8, height + 2, 8);

        for (int x = min.getX(); x <= max.getX(); x++) {
            for (int y = min.getY(); y <= max.getY(); y++) {
                for (int z = min.getZ(); z <= max.getZ(); z++) {
                    if (!TreePlaceUtil.validTreePos(level, new BlockPos(x, y, z))) {
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