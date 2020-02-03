package mod.reborn.server.block.machine;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.Random;

public class CultivatorTopBlock extends CultivatorBlock {
    public CultivatorTopBlock() {
        super("top");
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult result, World world, BlockPos pos, EntityPlayer player) {
        Item item = Item.getItemFromBlock(BlockHandler.CULTIVATOR_BOTTOM);

        if (item == null) {
            return null;
        }

        Block block = item instanceof ItemBlock ? getBlockFromItem(item) : this;
        return new ItemStack(item, 1, block.getMetaFromState(world.getBlockState(pos)));
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    	return Item.getItemFromBlock(BlockHandler.CULTIVATOR_BOTTOM);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        BlockPos add = pos.add(0, -1, 0);
        IBlockState blockState = world.getBlockState(add);

        return blockState.getBlock().onBlockActivated(world, add, blockState, player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        BlockPos bottomBlock = pos.add(0, -1, 0);

        if (world.getBlockState(bottomBlock).getBlock() != BlockHandler.CULTIVATOR_BOTTOM) {
            world.setBlockState(bottomBlock, BlockHandler.CULTIVATOR_BOTTOM.getDefaultState().withProperty(COLOR, state.getValue(COLOR)));
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        world.setBlockState(pos.add(0, -1, 0), Blocks.AIR.getDefaultState());
        super.breakBlock(world, pos, state);
    }
}
