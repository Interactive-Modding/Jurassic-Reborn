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
import net.minecraft.world.level.material.Material;

public class AraucariaTreeGenerator extends Feature<NoneFeatureConfiguration> {
    public AraucariaTreeGenerator(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }


    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {



        WorldGenLevel world = pContext.level();
        BlockState log = WoodBlocks.ARAUCARIA_LOG.get().defaultBlockState();
        BlockState leaves = WoodBlocks.ARAUCARIA_LEAVES.get().defaultBlockState().setValue(AncientLeavesBlock.DISTANCE, 1);

        BlockPos position = pContext.origin();
        Random rand = pContext.random();


        int height = rand.nextInt(15) + 10;
        int branchHeight = (int) (height / 1.3);

        if(!this.canPlace(pContext, height, branchHeight)){
            return false;
        }

        world.setBlock(position, log, 19);

        for (int y = 0; y < height; y++) {
            BlockPos logPos = position.above(y);
            world.setBlock(logPos, log, 19);
            if (y > branchHeight) {
                for (Direction facing : Direction.Plane.HORIZONTAL) {
                    Direction.Axis axis = facing.getAxis();
                    boolean zAxis = axis == Direction.Axis.Z;
                    if (y % 2 == (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 0 : 1)) {
                        BlockPos branch = logPos.offset(facing.getNormal());
                        BlockState facingLog = log.setValue(RotatedPillarBlock.AXIS, zAxis ? Direction.Axis.Z : Direction.Axis.X);
                        world.setBlock(branch, facingLog, 19);
                        this.generateClump(world, branch, 1.2, leaves);
                        if (y > branchHeight * 1.2) {
                            world.setBlock(branch.above().offset(facing.getNormal()), facingLog, 19);
                            this.generateClump(world, branch.above().offset(facing.getNormal()), 4, leaves);
                        }
                    }
                }
            }
        }
        if (rand.nextInt(10) == 0) {
            this.generateClump(world, position.above(height), 64, 6, leaves);
            this.generateClump(world, position.above(height), 8, 8, leaves);
        } else {
            this.generateClump(world, position.above(height), 32, 6, leaves);
            this.generateClump(world, position.above(height), 6, 6, leaves);
        }
        this.generateClump(world, position.above(height + 2), 16, 3.5, leaves);
        return true;
    }

    private boolean canPlace(FeaturePlaceContext<NoneFeatureConfiguration> pContext, int height, int branchHeight) {

        BlockPos.MutableBlockPos min = pContext.origin().mutable();

        min.move(-6, branchHeight, -6);

        BlockPos.MutableBlockPos max = pContext.origin().mutable();

        max.move(6, height+4, 6);

        for(int y = 0; y < branchHeight; y++){
            if(!TreePlaceUtil.validTreePos(pContext.level(), pContext.origin().above(y))){
                return false;
            }
        }


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

    private void generateClump(WorldGenLevel world, BlockPos pos, double size, BlockState state) {
        int blockRadius = (int) Math.ceil(size);
        for (int x = -blockRadius; x < blockRadius; x++) {
            for (int y = -blockRadius; y < blockRadius; y++) {
                for (int z = -blockRadius; z < blockRadius; z++) {
                    if (Math.abs(x * x + y * y + z * z) <= size) {
                        BlockPos leafPos = pos.offset(x, y, z);
                        if (world.getBlockState(leafPos) == Blocks.AIR.defaultBlockState()) {
                            this.setBlockState(world, leafPos, state);
                        }
                    }
                }
            }
        }
    }


    private void generateClump(WorldGenLevel world, BlockPos pos, double size, double sizeY, BlockState state) {
        int blockRadius = (int) Math.ceil(size);
        for (int x = -blockRadius; x < blockRadius; x++) {
            for (int y = -blockRadius; y < blockRadius; y++) {
                for (int z = -blockRadius; z < blockRadius; z++) {
                    if (Math.abs(x * x + (y * y * (size / sizeY)) + z * z) <= size) {
                        BlockPos leafPos = pos.offset(x, y, z);
                        if (world.getBlockState(leafPos).isAir()) {
                            this.setBlockState(world, leafPos, state);
                        }
                    }
                }
            }
        }
    }

    private void setBlockState(WorldGenLevel world, BlockPos pos, BlockState state) {
        Block block = world.getBlockState(pos).getBlock();
        if (isReplaceablePlant(world, pos) || block instanceof LeavesBlock || block instanceof SaplingBlock || block instanceof RotatedPillarBlock || block == Blocks.AIR) {
            world.setBlock(pos, state, 19);
        }
    }

    static boolean isReplaceablePlant(WorldGenLevel p_67289_, BlockPos p_67290_) {
        return p_67289_.isStateAtPosition(p_67290_, (p_160551_) -> {
            Material material = p_160551_.getMaterial();
            return material == Material.REPLACEABLE_PLANT;
        });
    }
}
