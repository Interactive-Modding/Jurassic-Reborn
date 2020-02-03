package mod.reborn.server.item;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import mod.reborn.RebornMod;

public class PaddockSignItem extends Item {
    public PaddockSignItem() {
        this.setCreativeTab(TabHandler.DECORATIONS);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    	ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote && side != EnumFacing.DOWN && side != EnumFacing.UP) {
            BlockPos offset = pos.offset(side);

            if (player.canPlayerEdit(offset, side, stack)) {
                RebornMod.PROXY.openSelectDino(offset, side, hand);
            }
        }

        return EnumActionResult.PASS;
    }
}
