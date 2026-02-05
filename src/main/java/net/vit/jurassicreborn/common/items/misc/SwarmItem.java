package net.vit.jurassicreborn.common.items.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.stats.Stats;

import org.jetbrains.annotations.Nullable;

public class SwarmItem extends BlockItem {

    public SwarmItem(Block block, Item.Properties properties) {
        super(block, properties);
    }
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);

        if (state.getBlock() == Blocks.WATER && state.getFluidState().isSource() && context.getClickedFace() == Direction.UP) {
            if (this.placeAboveWater(level, context.getPlayer(), pos, context.getItemInHand())) {
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
            return InteractionResult.FAIL;
        }

        return super.useOn(context);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        if (hitResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(stack);
        }

        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        }

        BlockPos hitPos = hitResult.getBlockPos();
        if (!level.mayInteract(player, hitPos)) {
            return InteractionResultHolder.fail(stack);
        }

        if (!player.mayUseItemAt(hitPos.above(), Direction.UP, stack)) {
            return InteractionResultHolder.fail(stack);
        }

        BlockState state = level.getBlockState(hitPos);
        if (state.getBlock() == Blocks.WATER && state.getFluidState().isSource()) {
            if (this.placeAboveWater(level, player, hitPos, stack)) {
                return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    private boolean placeAboveWater(Level level, @Nullable Player player, BlockPos waterPos, ItemStack stack) {
        BlockPos placePos = waterPos.above();
        if (!level.getBlockState(placePos).isAir()) {
            return false;
        }

        if (!level.isClientSide) {
            level.setBlock(placePos, this.getBlock().defaultBlockState(), 3);
            level.gameEvent(player, GameEvent.BLOCK_PLACE, placePos);
            if (player != null && !player.getAbilities().instabuild) {
                stack.shrink(1);
            }
            if (player != null) {
                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }

        return true;
    }


}
