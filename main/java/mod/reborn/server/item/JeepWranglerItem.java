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
import mod.reborn.server.entity.vehicle.JeepWranglerEntity;

public class JeepWranglerItem extends Item {
    public JeepWranglerItem() {
        this.setCreativeTab(TabHandler.ITEMS);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    	ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote) {
            pos = pos.offset(side);

            JeepWranglerEntity entity = new JeepWranglerEntity(world);
            entity.setPositionAndRotation(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, player.rotationYaw, 0.0F);
            world.spawnEntity(entity);

            stack.shrink(1);
        }

        return EnumActionResult.SUCCESS;
    }
}
