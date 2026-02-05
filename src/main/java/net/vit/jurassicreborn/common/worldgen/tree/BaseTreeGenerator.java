package net.vit.jurassicreborn.common.worldgen.tree;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.vit.jurassicreborn.common.blocks.wood.AncientLeavesBlock;
import net.vit.jurassicreborn.common.blocks.wood.WoodBlocks;

import java.util.function.Supplier;

public class BaseTreeGenerator extends Feature<NoneFeatureConfiguration> {


    private final Supplier<BlockState> trunk;
    private final Supplier<BlockState> leaves;

    public BaseTreeGenerator(Codec<NoneFeatureConfiguration> pCodec, Supplier<BlockState> trunk, Supplier<BlockState> leaves) {
        super(pCodec);
        this.trunk = trunk;
        this.leaves = leaves;
    }

    public static BaseTreeGenerator ginkgo(Codec<NoneFeatureConfiguration> pCodec) {
        return new BaseTreeGenerator(pCodec,() -> WoodBlocks.GINKGO_LOG.get().defaultBlockState(),() -> WoodBlocks.GINKGO_LEAVES.get().defaultBlockState().setValue(AncientLeavesBlock.DISTANCE, 1));
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockState log = trunk.get();
        BlockState leaves = this.leaves.get();
        RandomSource rand = context.random();
        BlockPos position = context.origin();
        WorldGenLevel world = context.level();


        int height = rand.nextInt(16) + 4;

        if(!this.canPlace(context, height)){
            return false;
        }

        world.setBlock(position, log, 19);

        for (int y = 0; y < height; y++) {
            BlockPos logPos = position.above(y);
            this.setBlockState(world, logPos, log);

            int branchLength = Math.max(1, (height - y) / 3);

            if (y >= 2) {
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x != 0 || z != 0) {
                            this.setBlockState(world, logPos.offset(x, 0, z), leaves);
                        }
                    }
                }

                int bushSize = (int) (branchLength * 1.8);

                for (int x = -bushSize; x <= bushSize; x++) {
                    for (int z = -bushSize; z <= bushSize; z++) {
                        if ((x != 0 || z != 0) && Math.sqrt(x * x + z * z) < bushSize) {
                            this.setBlockState(world, logPos.offset(x, 0, z), leaves);
                        }
                    }
                }
            }

            if (y % 3 == 2) {
                for (Direction facing : Direction.Plane.HORIZONTAL) {
                    BlockPos branchPos = logPos.offset(facing.getNormal());
                    BlockState facingLog = log.setValue(RotatedPillarBlock.AXIS, facing.getAxis());

                    this.setBlockState(world, branchPos, facingLog);

                    this.setBlockState(world, branchPos.above(2), leaves);
                    this.setBlockState(world, branchPos.below(), leaves);
                    this.setBlockState(world, branchPos.relative(facing.getClockWise(), 2), leaves);
                    this.setBlockState(world, branchPos.relative(facing.getCounterClockWise(), 2), leaves);

                    for (int i = 0; i < branchLength; i++) {
                        BlockPos pos = branchPos.relative(facing, i + 1).above(i / 2 + 1);

                        this.setBlockState(world, pos, facingLog);
                        this.setBlockState(world, pos.above(), leaves);
                        this.setBlockState(world, pos.below(), leaves);
                        this.setBlockState(world, pos.relative(facing.getClockWise()), leaves);
                        this.setBlockState(world, pos.relative(facing.getCounterClockWise()), leaves);

                        if (i >= branchLength - 1) {
                            this.setBlockState(world, pos.relative(facing), leaves);
                        }
                    }
                }
            }
        }

        this.setBlockState(world, position.above(height), leaves);
        this.setBlockState(world, position.above(height).north(), leaves);
        this.setBlockState(world, position.above(height).south(), leaves);
        this.setBlockState(world, position.above(height).west(), leaves);
        this.setBlockState(world, position.above(height).east(), leaves);
        this.setBlockState(world, position.above(height + 1), leaves);

        return true;
    }

    private boolean canPlace(FeaturePlaceContext<NoneFeatureConfiguration> pContext, int height) {
        WorldGenLevel level = pContext.level();
        BlockPos origin = pContext.origin();


        for (int y = 0; y <= height + 1; y++) {
            if (!TreePlaceUtil.validTreePos(level, origin.above(y))) {
                return false;
            }
        }


        int canopyRadius = 6;
        int minY = origin.getY() + 2;
        int maxY = origin.getY() + height + 2;

        for (int y = minY; y <= maxY; y++) {
            for (int x = -canopyRadius; x <= canopyRadius; x++) {
                for (int z = -canopyRadius; z <= canopyRadius; z++) {
                    if (x * x + z * z > canopyRadius * canopyRadius) {
                        continue;
                    }

                    BlockPos checkPos = new BlockPos(origin.getX() + x, y, origin.getZ() + z);
                    if (!TreePlaceUtil.validTreePos(level, checkPos)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    static void setBlockState(WorldGenLevel world, BlockPos pos, BlockState state) {
        Block block = world.getBlockState(pos).getBlock();
        if (isReplaceablePlant(world, pos) || block instanceof LeavesBlock || block instanceof SaplingBlock || block instanceof RotatedPillarBlock || block == Blocks.AIR) {
            world.setBlock(pos, state, 19);
        }
    }
    static boolean isReplaceablePlant(WorldGenLevel p_67289_, BlockPos p_67290_) {
        return p_67289_.isStateAtPosition(p_67290_, state -> state.is(BlockTags.REPLACEABLE));
    }
}
