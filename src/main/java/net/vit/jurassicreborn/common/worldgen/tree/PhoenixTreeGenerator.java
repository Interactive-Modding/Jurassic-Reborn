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

public class PhoenixTreeGenerator extends Feature<NoneFeatureConfiguration> {
    public PhoenixTreeGenerator(Codec<NoneFeatureConfiguration> p_65786_) {
        super(p_65786_);

    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        BlockState log = WoodBlocks.PHOENIX_LOG.get().defaultBlockState();
        BlockState leaves = WoodBlocks.PHOENIX_LEAVES.get().defaultBlockState().setValue(AncientLeavesBlock.DISTANCE, 1);
        
        WorldGenLevel world = ctx.level();
        RandomSource rand = ctx.random();
        BlockPos position = ctx.origin();


        int height = rand.nextInt(6) + 7;

        if(!this.canPlace(ctx, height, height))
            return false;

        world.setBlock(position, log, 19);

        for (int y = 0; y < height; y++) {
            BaseTreeGenerator.setBlockState(world, position.above(y), log);
        }

        for (int palmX = -1; palmX < 2; palmX++) {
            for (int palmY = -1; palmY < 1; palmY++) {
                for (int palmZ = -1; palmZ < 2; palmZ++) {
                    BaseTreeGenerator.setBlockState(world, position.offset(palmX, height + 1 + palmY, palmZ), leaves);
                }
            }
        }

        BaseTreeGenerator.setBlockState(world, position.above(height + 2), leaves);
        BaseTreeGenerator.setBlockState(world, position.above(height + 3), leaves);
        BaseTreeGenerator.setBlockState(world, position.above(height + 4), leaves);

        BaseTreeGenerator.setBlockState(world, position.offset(1, height + 2, 0), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(-1, height + 2, 0), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(0, height + 2, 1), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(0, height + 2, -1), leaves);

        BaseTreeGenerator.setBlockState(world, position.offset(2, height + 2, 0), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(-2, height + 2, 0), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(0, height + 2, 2), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(0, height + 2, -2), leaves);

        BaseTreeGenerator.setBlockState(world, position.offset(-2, height + 3, 0), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(-3, height + 3, 0), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(-4, height + 2, 0), leaves);

        BaseTreeGenerator.setBlockState(world, position.offset(2, height + 3, 0), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(3, height + 3, 0), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(4, height + 2, 0), leaves);

        BaseTreeGenerator.setBlockState(world, position.offset(0, height + 3, 2), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(0, height + 3, 3), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(0, height + 2, 4), leaves);

        BaseTreeGenerator.setBlockState(world, position.offset(0, height + 3, -2), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(0, height + 3, -3), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(0, height + 2, -4), leaves);

        BaseTreeGenerator.setBlockState(world, position.offset(2, height, 0), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(-2, height, 0), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(0, height, 2), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(0, height, -2), leaves);

        BaseTreeGenerator.setBlockState(world, position.offset(3, height - 1, 0), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(-3, height - 1, 0), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(0, height - 1, 3), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(0, height - 1, -3), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(3, height - 2, 0), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(-3, height - 2, 0), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(0, height - 2, 3), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(0, height - 2, -3), leaves);

        if (rand.nextBoolean()) {
            BaseTreeGenerator.setBlockState(world, position.offset(3, height - 3, 0), leaves);
            BaseTreeGenerator.setBlockState(world, position.offset(-3, height - 3, 0), leaves);
            BaseTreeGenerator.setBlockState(world, position.offset(0, height - 3, 3), leaves);
            BaseTreeGenerator.setBlockState(world, position.offset(0, height - 3, -3), leaves);
        }

        BaseTreeGenerator.setBlockState(world, position.offset(2, height + 1, 2), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(2, height + 2, 2), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(-2, height + 1, 2), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(-2, height + 2, 2), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(2, height + 1, -2), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(2, height + 2, -2), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(-2, height + 1, -2), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(-2, height + 2, -2), leaves);

        BaseTreeGenerator.setBlockState(world, position.offset(3, height - 1, 3), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(3, height, 3), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(3, height + 1, 3), leaves);

        BaseTreeGenerator.setBlockState(world, position.offset(-3, height - 1, 3), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(-3, height, 3), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(-3, height + 1, 3), leaves);

        BaseTreeGenerator.setBlockState(world, position.offset(3, height - 1, -3), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(3, height, -3), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(3, height + 1, -3), leaves);

        BaseTreeGenerator.setBlockState(world, position.offset(-3, height - 1, -3), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(-3, height, -3), leaves);
        BaseTreeGenerator.setBlockState(world, position.offset(-3, height + 1, -3), leaves);

        return true;
    }

    private boolean canPlace(FeaturePlaceContext<NoneFeatureConfiguration> pContext, int height, int branchHeight) {

        BlockPos.MutableBlockPos min = pContext.origin().mutable();

        min.move(-4, branchHeight, -4);

        BlockPos.MutableBlockPos max = pContext.origin().mutable();

        max.move(4, height+4, 4);

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
}
