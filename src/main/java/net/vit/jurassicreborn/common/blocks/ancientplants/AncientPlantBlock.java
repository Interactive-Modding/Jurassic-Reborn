package net.vit.jurassicreborn.common.blocks.ancientplants;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.util.GameRuleHandler;

public class AncientPlantBlock extends BushBlock {

    private static final int DENSITY_PER_AREA = 4;
    private static final int SPREAD_RADIUS = 6;
    public static final MapCodec<AncientPlantBlock> CODEC =
            simpleCodec(AncientPlantBlock::new);

    public AncientPlantBlock(BlockBehaviour.Properties properties) {
        super(properties.randomTicks());
    }

    @Override
    protected MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    public AncientPlantBlock() {
        super(ModBlocks.defaultPlant().randomTicks());
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    protected boolean isNearWater(ServerLevel level, BlockPos pos) {
        for (BlockPos p : BlockPos.betweenClosed(pos.offset(-8, -3, -8), pos.offset(8, 3, 8))) {
            if (level.getFluidState(p).is(FluidTags.WATER)) {
                if (pos.distSqr(p) < 9) return true; // within 3 blocks
            }
        }
        return false;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.getGameRules().getBoolean(GameRuleHandler.ANCIENT_PLANT_SPREAD)) return;
        if (!this.isNearWater(level, pos)) return;
        if (random.nextInt(8) != 0) return; // 1-in-8

        int allowedInArea = DENSITY_PER_AREA;
        for (BlockPos p : BlockPos.betweenClosed(
                pos.offset(-SPREAD_RADIUS, -1, -SPREAD_RADIUS),
                pos.offset( SPREAD_RADIUS,  1,  SPREAD_RADIUS))) {

            if (!p.equals(pos) && level.getBlockState(p).is(this)) {
                if (--allowedInArea <= 0) return;
            }
        }

        BlockPos target = null;
        int attempts = 4;
        while (target == null && attempts-- > 0) {
            int x = random.nextInt(SPREAD_RADIUS * 2 + 1) - SPREAD_RADIUS;
            int y = random.nextInt(3) - 1;
            int z = random.nextInt(SPREAD_RADIUS * 2 + 1) - SPREAD_RADIUS;

            BlockPos candidate = pos.offset(x, y, z);
            BlockPos placement = this.findGround(level, candidate);

            if (placement != null && this.canSurvive(this.defaultBlockState(), level, placement)) {
                target = placement;
            }
        }

        if (target != null) {
            spread(level, target);
        }
    }

    protected void spread(ServerLevel level, BlockPos pos) {
        level.setBlockAndUpdate(pos, this.defaultBlockState());
    }

    protected BlockPos findGround(ServerLevel level, BlockPos start) {
        // try down first
        BlockPos pos = start;
        for (int i = 0; i < 8; i++) {
            BlockState below = level.getBlockState(pos.below());
            BlockState here  = level.getBlockState(pos);
            if (canPlace(below, here, pos, level)) return pos;
            pos = pos.below();
        }
        // then up
        pos = start;
        for (int i = 0; i < 8; i++) {
            BlockState below = level.getBlockState(pos.below());
            BlockState here  = level.getBlockState(pos);
            if (canPlace(below, here, pos, level)) return pos;
            pos = pos.above();
        }
        return null;
    }

    protected boolean canPlace(BlockState down, BlockState here, BlockPos pos, LevelReader level) {
        // must have solid top face beneath, target must be replaceable AND not water
        return down.isFaceSturdy(level, pos.below(), Direction.UP)
                && (here.isAir() || here.is(BlockTags.REPLACEABLE) || here.is(BlockTags.REPLACEABLE_BY_TREES))
                && !level.getFluidState(pos).is(FluidTags.WATER);
    }

    @Override
    protected boolean mayPlaceOn(BlockState soil, BlockGetter level, BlockPos pos) {
        // explicit list works on 1.19.2 Mojang mappings
        return soil.is(Blocks.GRASS_BLOCK) ||
                soil.is(Blocks.DIRT) ||
                soil.is(Blocks.COARSE_DIRT) ||
                soil.is(Blocks.ROOTED_DIRT) ||
                soil.is(Blocks.PODZOL) ||
                soil.is(Blocks.MYCELIUM) ||
                soil.is(Blocks.FARMLAND) ||
                soil.is(Blocks.SAND) ||
                soil.is(Blocks.RED_SAND) ||
                soil.is(Blocks.GRAVEL) ||
                soil.is(Blocks.CLAY) ||
                soil.is(Blocks.MUD);
    }
}
