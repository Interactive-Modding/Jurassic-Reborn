package net.vit.jurassicreborn.common.worldgen.tree;

import com.mojang.serialization.Codec;
import net.vit.jurassicreborn.common.blocks.ModWoodTypes;
import net.vit.jurassicreborn.common.blocks.wood.AncientLeavesBlock;
import net.vit.jurassicreborn.common.blocks.wood.WoodBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Material;

public class PsaroniusTreeGenerator extends Feature<NoneFeatureConfiguration> {

    public PsaroniusTreeGenerator(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockState log = WoodBlocks.PSARONIUS_LOG.get().defaultBlockState();
        BlockState leaves = WoodBlocks.PSARONIUS_LEAVES.get().defaultBlockState().setValue(AncientLeavesBlock.DISTANCE, 1);

        WorldGenLevel world = context.level();
        RandomSource rand = context.random();
        BlockPos position = context.origin();


        
        
        
        int scale = rand.nextInt(1) + 1;
        int height = scale + 6 + rand.nextInt(2);
        BlockPos topPosition = position.above(height);

        if(!this.canPlace(context, height, scale))
            return false;

        world.setBlock(position, log, 19);


        for (int y = 0; y < height; y++) {
            BlockPos logPosition = position.above(y);
            BaseTreeGenerator.setBlockState(world, logPosition, log);
        }
        int leafScale = scale + 2;
        for (int x = -leafScale; x <= leafScale; x++) {
            BaseTreeGenerator.setBlockState(world, topPosition.offset(x, 0, 0), leaves);
        }
        for (int z = -leafScale; z <= leafScale; z++) {
            BaseTreeGenerator.setBlockState(world, topPosition.offset(0, 0, z), leaves);
        }
        BaseTreeGenerator.setBlockState(world, topPosition.offset(-leafScale - 1, -1, 0), leaves);
        BaseTreeGenerator.setBlockState(world, topPosition.offset(leafScale + 1, -1, 0), leaves);
        BaseTreeGenerator.setBlockState(world, topPosition.offset(0, -1, -leafScale - 1), leaves);
        BaseTreeGenerator.setBlockState(world, topPosition.offset(0, -1, leafScale + 1), leaves);
        BaseTreeGenerator.setBlockState(world, topPosition.offset(-2, -1, -2), leaves);
        BaseTreeGenerator.setBlockState(world, topPosition.offset(-2, -1, 2), leaves);
        BaseTreeGenerator.setBlockState(world, topPosition.offset(2, -1, 2), leaves);
        BaseTreeGenerator.setBlockState(world, topPosition.offset(2, -1, -2), leaves);
        this.generateClump(world, topPosition, 2.5, leaves);
        return true;
    }

    private void generateClump(WorldGenLevel world, BlockPos pos, double size, BlockState state) {
        int blockRadius = (int) Math.ceil(size);
        for (int x = -blockRadius; x < blockRadius; x++) {
            for (int y = -blockRadius; y < blockRadius; y++) {
                for (int z = -blockRadius; z < blockRadius; z++) {
                    if (Math.abs(x * x + y * y + z * z) <= size) {
                        BlockPos leafPos = pos.offset(x, y, z);
                        if (world.getBlockState(leafPos).isAir()) {
                            BaseTreeGenerator.setBlockState(world, leafPos, state);
                        }
                    }
                }
            }
        }
    }

    private boolean canPlace(FeaturePlaceContext<NoneFeatureConfiguration> pContext, int height, int branchHeight) {

        BlockPos.MutableBlockPos min = pContext.origin().mutable();

        min.move(-3, branchHeight, -3);

        BlockPos.MutableBlockPos max = pContext.origin().mutable();

        max.move(3, height+4, 3);

        for(int y = 0; y < branchHeight+5; y++){
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
}
