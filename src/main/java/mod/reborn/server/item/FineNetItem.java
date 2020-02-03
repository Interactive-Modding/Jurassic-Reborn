package mod.reborn.server.item;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class FineNetItem extends Item {
    public FineNetItem() {
        super();
        this.setCreativeTab(TabHandler.ITEMS);
        this.setMaxDamage(50);
        this.setMaxStackSize(1);
        }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    	ItemStack stack = player.getHeldItem(hand);
        RayTraceResult result = this.rayTrace(world, player, true);
        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos pos = result.getBlockPos();
            IBlockState state = world.getBlockState(pos);
            if (state.getMaterial() == Material.WATER) {
                if (!world.isRemote && player.getRNG().nextInt(20) == 0) {
                    player.inventory.addItemStackToInventory(new ItemStack(player.getRNG().nextBoolean() ? ItemHandler.KRILL : ItemHandler.PLANKTON));
                }
                stack.damageItem(1, player);
                return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
            }
        }
        return ActionResult.newResult(EnumActionResult.PASS, stack);
    }
}
