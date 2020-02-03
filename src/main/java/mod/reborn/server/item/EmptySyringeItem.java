package mod.reborn.server.item;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.BlockBush;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EmptySyringeItem extends Item {
    public EmptySyringeItem() {
        super();
        this.setCreativeTab(TabHandler.ITEMS);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    	ItemStack stack = player.getHeldItem(hand);
        if (world.getBlockState(pos).getBlock() instanceof BlockBush) {
            if (stack.getCount() == 1) {
                player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(ItemHandler.PLANT_CELLS));
            } else {
                player.inventory.addItemStackToInventory(new ItemStack(ItemHandler.PLANT_CELLS));
                stack.shrink(1);
            }

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }
}
