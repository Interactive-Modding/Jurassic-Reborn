package mod.reborn.server.block.machine;

import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.entity.CultivatorBlockEntity;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import mod.reborn.RebornMod;
import mod.reborn.server.proxy.ServerProxy;

public class CultivatorBottomBlock extends CultivatorBlock {
    public CultivatorBottomBlock() {
        super("bottom");
        this.setCreativeTab(TabHandler.BLOCKS);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return super.canPlaceBlockAt(worldIn, pos) && worldIn.isAirBlock(pos.up());
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        if (stack.hasDisplayName()) {
            TileEntity tile = worldIn.getTileEntity(pos);

            if (tile instanceof CultivatorBlockEntity) {
                ((CultivatorBlockEntity) tile).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        } else if (!player.isSneaking()) {
            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof CultivatorBlockEntity) {
                CultivatorBlockEntity cultivator = (CultivatorBlockEntity) tile;

                if (cultivator.isUsableByPlayer(player)) {
                    player.openGui(RebornMod.INSTANCE, ServerProxy.GUI_CULTIVATOR_ID, world, pos.getX(), pos.getY(), pos.getZ());
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        BlockPos topBlock = pos.add(0, 1, 0);

        Block block = world.getBlockState(topBlock).getBlock();

        if (block == Blocks.AIR) {
            world.setBlockState(topBlock, BlockHandler.CULTIVATOR_TOP.getDefaultState().withProperty(COLOR, state.getValue(COLOR)));
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        worldIn.setBlockState(pos.add(0, 1, 0), Blocks.AIR.getDefaultState());
        super.breakBlock(worldIn, pos, state);
    }
}
