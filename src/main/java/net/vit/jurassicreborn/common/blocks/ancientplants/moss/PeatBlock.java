package net.vit.jurassicreborn.common.blocks.ancientplants.moss;

import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class PeatBlock extends Block {
    public static IntegerProperty MOISTURE = IntegerProperty.create("moisture", 0, 7);
    public static int peatGenerationSpeed = 4; //todo:config

    public PeatBlock(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.getStateDefinition().any().setValue(MOISTURE, 0));
    }



    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {
        int moisture = state.getValue(MOISTURE);

        if (!this.hasWater(world, pos) && !world.isRainingAt(pos.above()) && !(world.getBiome(pos).is(Biomes.SWAMP))) {
            if (moisture > 0) {
                world.setBlock(pos, state.setValue(MOISTURE, moisture - 1), 2);
            } else {
                world.setBlock(pos, Blocks.DIRT.defaultBlockState(), 0);
            }
        } else if (moisture < 7) {
            world.setBlock(pos, state.setValue(MOISTURE, 7), 2);
        }
        if ((world.getBiome(pos).is(Biomes.SWAMP))) {
            for (int i = 0; i < 4; ++i) {
                BlockPos blockpos = pos.offset(rand.nextInt(peatGenerationSpeed + 1), rand.nextInt(peatGenerationSpeed + 1), rand.nextInt(peatGenerationSpeed + 1));

                if (blockpos.getY() >= 0 && blockpos.getY() < 256 && !world.isLoaded(blockpos)) {
                    return;
                }

                BlockState iblockstate = world.getBlockState(blockpos.above());
                BlockState iblockstate1 = world.getBlockState(blockpos);
                BlockState iBlockState2 = world.getBlockState(blockpos.above(2));
                BlockState iBlockState3 = world.getBlockState(blockpos.above(3));


                if (iblockstate1.getBlock() == Blocks.DIRT && (iblockstate1.is(Blocks.DIRT) || iblockstate1.is(Blocks.GRASS_BLOCK))) {
                    if (iblockstate.getBlock() == Blocks.GRASS || iBlockState2.getBlock() == Blocks.GRASS || iBlockState3.getBlock() == Blocks.GRASS) {
                        world.setBlock(blockpos, ModBlocks.PEAT.get().defaultBlockState(), 1);
                    }
                }
            }
        }
    }

    private boolean hasWater(Level world, BlockPos pos) {
        for (BlockPos nearPos : BlockPos.betweenClosed(-4, 0, -4, 4, 1, 4)) {
            if (world.getBlockState(nearPos).getFluidState().is(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(MOISTURE);
    }
}
