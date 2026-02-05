package net.vit.jurassicreborn.common.items.misc;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.TabHandler;

public class FineNetItem extends Item {

    public FineNetItem() {
        super(new Item.Properties()
                .durability(250)
//                .stacksTo(1)
                .tab(TabHandler.ITEMS));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        BlockHitResult hit = Item.getPlayerPOVHitResult(level, player,
                ClipContext.Fluid.SOURCE_ONLY);

        if (hit.getType() == HitResult.Type.BLOCK &&
                level.getFluidState(hit.getBlockPos()).is(FluidTags.WATER)) {

            if (!level.isClientSide) {
                if (level.random.nextInt(20) == 0) {
                    ItemStack reward = new ItemStack(
                            level.random.nextBoolean()
                                    ? ModItems.KRILL.get()
                                    : ModItems.PLANKTON.get()
                    );
                    if (!player.addItem(reward)) {
                        player.drop(reward, false);
                    }
                }
                stack.hurtAndBreak(1, player,
                        p -> p.broadcastBreakEvent(hand));
            }
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }
}
