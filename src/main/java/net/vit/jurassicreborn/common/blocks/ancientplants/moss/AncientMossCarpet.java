package net.vit.jurassicreborn.common.blocks.ancientplants.moss;

import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AncientMossCarpet extends Block {

    private static final int DENSITY_PER_AREA = 8;
    private static final int SPREAD_RADIUS = 3;

    public AncientMossCarpet() {
        super(ModBlocks.defaultMoss().randomTicks());
    }
    public AncientMossCarpet(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {
        if (world.getBlockState(pos.below()).getBlock() instanceof PeatBlock) {
            if (rand.nextInt(8) <= 3) {
                int allowedInArea = DENSITY_PER_AREA;

                BlockPos nextPos = null;
                int placementAttempts = 3;

                while (nextPos == null && placementAttempts > 0) {
                    int doubleRadius = SPREAD_RADIUS * 2;
                    BlockPos tmp = pos.offset(rand.nextInt(doubleRadius) - SPREAD_RADIUS, -SPREAD_RADIUS, rand.nextInt(doubleRadius) - SPREAD_RADIUS);
                    nextPos = this.findGround(world, tmp);
                    placementAttempts--;
                }

                if (nextPos != null) {
                    for (BlockPos neighbourPos : BlockPos.betweenClosed(nextPos.offset(-2, -3, -2), nextPos.offset(2, 3, 2))) {
                        if (world.getBlockState(neighbourPos).getBlock() == this) {
                            allowedInArea--;

                            if (allowedInArea <= 0) {
                                return;
                            }
                        }
                    }

                    world.setBlock(nextPos, this.defaultBlockState(), 1);
                }
            }
        }
    }

    private BlockPos findGround(Level world, BlockPos start) {
        BlockPos pos = start;

        Block down = world.getBlockState(pos.below()).getBlock();
        Block here = world.getBlockState(pos).getBlock();
        Block up = world.getBlockState(pos.above()).getBlock();

        for (int i = 0; i < 8; ++i) {
            if (down instanceof PeatBlock) {
                return pos;
            }

            down = here;
            here = up;
            pos = pos.above();
            up = world.getBlockState(pos.above()).getBlock();
        }

        return null;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }





    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockState below = world.getBlockState(pos.below());
        return super.canSurvive(state, world, pos) && this.canBlockStay((Level)world, pos);
    }


    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, level, pos, neighbor);
        this.checkForDrop((Level)level, pos, state);
    }

    private boolean checkForDrop(Level world, BlockPos pos, BlockState state) {
        if (!this.canBlockStay(world, pos)) {
//            this.dropBlockAsItem(world, pos, state, 0);todo: block items
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            return false;
        } else {
            return true;
        }
    }

    private boolean canBlockStay(Level world, BlockPos pos) {
        return !world.isEmptyBlock(pos.below());
    }


}

