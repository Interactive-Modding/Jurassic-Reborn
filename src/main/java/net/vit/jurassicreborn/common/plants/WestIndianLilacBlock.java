package net.vit.jurassicreborn.common.plants;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.util.FakePlayer;
import net.vit.jurassicreborn.common.items.ModItems;

public class WestIndianLilacBlock extends DoublePlantBlock implements BonemealableBlock {

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 2);

    public WestIndianLilacBlock() {
        super(Properties.copy(net.minecraft.world.level.block.Blocks.FERN).noCollission().sound(SoundType.GRASS));
        this.registerDefaultState(this.defaultBlockState()
                .setValue(AGE, 0)
                .setValue(HALF, DoubleBlockHalf.LOWER));
    }

    public boolean isMaxAge(BlockState state) {
        return state.getValue(AGE) >= 2;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return !isMaxAge(state);
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, boolean b) {
        return !isMaxAge(blockState);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockState downState = level.getBlockState(pos.below());
            if (downState.getBlock() instanceof BonemealableBlock growable) {
                growable.performBonemeal(level, random, pos.below(), downState);
            }
            return;
        }
        int age = state.getValue(AGE);
        if (age < 2) {
            int newAge = Mth.clamp(age + 1, 0, 2);
            BlockState newState = state.setValue(AGE, newAge);
            level.setBlock(pos, newState, 2);
            level.setBlock(pos.above(), newState.setValue(HALF, DoubleBlockHalf.UPPER), 2);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!isMaxAge(state) && random.nextInt(5) == 0) {
            performBonemeal(level, random, pos, state);
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            DoubleBlockHalf half = state.getValue(HALF);
            BlockPos otherPos = half == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
            BlockState otherState = level.getBlockState(otherPos);
            if (otherState.is(this) && otherState.getValue(HALF) != half) {
                level.setBlock(otherPos, Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(2001, otherPos, Block.getId(otherState));
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            BlockState downState = level.getBlockState(pos.below());
            if (downState.getBlock() instanceof WestIndianLilacBlock) {
                return downState.getBlock().use(downState, level, pos.below(), player, hand, hit);
            }
            return InteractionResult.PASS;
        }
        int age = state.getValue(AGE);
        if (age == 2) {
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            }
            int newAge = Mth.clamp(age - 1, 0, 2);
            BlockState newState = state.setValue(AGE, newAge);
            level.setBlock(pos, newState, 2);
            level.setBlock(pos.above(), newState.setValue(HALF, DoubleBlockHalf.UPPER), 2);

            // Drop the berries
            ItemStack itemDrop = new ItemStack(ModItems.WEST_INDIAN_LILAC_BERRIES.get());
            ItemEntity entityItem = new ItemEntity(level, player.getX(), player.getY() - 1.0D, player.getZ(), itemDrop);
            level.addFreshEntity(entityItem);

            if (!(player instanceof FakePlayer)) {
                entityItem.playerTouch(player);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
