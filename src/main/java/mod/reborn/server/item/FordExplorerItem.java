package mod.reborn.server.item;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.client.util.ITooltipFlag;
import mod.reborn.server.entity.vehicle.FordExplorerEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public final class FordExplorerItem extends Item {
    public FordExplorerItem() {
        this.setCreativeTab(TabHandler.ITEMS);
    }
    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag tooltipFlag) {
        super.addInformation(stack, world, tooltip, tooltipFlag);
        tooltip.add("Right click on a block to spawn the ford");
    }
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            pos = pos.offset(side);

            FordExplorerEntity entity = new FordExplorerEntity(world);
            entity.setPositionAndRotation(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, player.rotationYaw, 0.0F);
            world.spawnEntity(entity);

            stack.shrink(1);
        }

        return EnumActionResult.SUCCESS;
    }
}
