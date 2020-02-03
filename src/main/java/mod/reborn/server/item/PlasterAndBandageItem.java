package mod.reborn.server.item;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.EncasedFossilBlock;
import mod.reborn.server.block.FossilBlock;
import mod.reborn.server.block.NestFossilBlock;

public class PlasterAndBandageItem extends Item {
    public PlasterAndBandageItem() {
        super();

        this.setCreativeTab(TabHandler.ITEMS);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    	ItemStack stack = player.getHeldItem(hand);
        if (player.canPlayerEdit(pos.offset(side), side, stack)) {
            IBlockState state = world.getBlockState(pos);

            Block block = state.getBlock();

            if (block instanceof FossilBlock) {
                if (!world.isRemote) {
                    int id = BlockHandler.getDinosaurId((FossilBlock) block, block.getMetaFromState(state));

                    world.setBlockState(pos, BlockHandler.getEncasedFossil(id).getDefaultState().withProperty(EncasedFossilBlock.VARIANT, BlockHandler.getMetadata(id)));

                    if (!player.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                }

                return EnumActionResult.SUCCESS;
            } else if (block instanceof NestFossilBlock && !((NestFossilBlock) block).encased) {
                if (!world.isRemote) {
                    world.setBlockState(pos, BlockHandler.ENCASED_NEST_FOSSIL.getDefaultState().withProperty(NestFossilBlock.VARIANT, state.getValue(NestFossilBlock.VARIANT)));

                    if (!player.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                }

                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.PASS;
    }
}
