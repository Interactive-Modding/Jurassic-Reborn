package mod.reborn.server.item;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class SwarmItem extends Item {
    private Supplier<IBlockState> block;

    public SwarmItem(Supplier<IBlockState> block) {
        super();
        this.block = block;
        this.setCreativeTab(TabHandler.ITEMS);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
    	ItemStack stack = player.getHeldItem(hand);
        RayTraceResult result = this.rayTrace(world, player, true);
        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos pos = result.getBlockPos();
            IBlockState state = world.getBlockState(pos);
            if (state.getMaterial() == Material.WATER) {
                if (player.canPlayerEdit(pos, EnumFacing.UP, stack)) {
                    world.setBlockState(pos.up(), this.block.get());
                    stack.shrink(1);
                    return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
                }
            }
        }
        return ActionResult.newResult(EnumActionResult.PASS, stack);
    }
}
