package net.vit.jurassicreborn.common.items.genetics;

import net.vit.jurassicreborn.common.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BushBlock;

public class EmptySyringeItem extends Item {
    public EmptySyringeItem(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if(pContext.getLevel().isClientSide)
            return InteractionResult.PASS;

        Player player = pContext.getPlayer();
        InteractionHand hand = pContext.getHand();
        Level world = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        ItemStack stack = player.getItemInHand(hand);
        if (world.getBlockState(pos).getBlock() instanceof BushBlock) {
            if (stack.getCount() == 1) {
                player.getInventory().setItem(player.getInventory().selected, new ItemStack(ModItems.PLANT_CELLS.get()));
            } else {
                player.getInventory().add(new ItemStack(ModItems.PLANT_CELLS.get()));
                stack.shrink(1);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }
}
