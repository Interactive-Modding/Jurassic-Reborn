package mod.reborn.server.block.plant;

import mod.reborn.server.item.ItemHandler;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

import java.util.Random;

public class WestIndianLilacBlock extends DoublePlantBlock implements IGrowable{

    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 2);

    public WestIndianLilacBlock() {
        super(Material.VINE);
    }

    public boolean isMaxAge(IBlockState state)
    {
        return state.getValue(AGE) >= 2;
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return !this.isMaxAge(state);
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(AGE, meta % 3).withProperty(HALF, meta >= 3 ? BlockHalf.UPPER : BlockHalf.LOWER);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(AGE) + (state.getValue(HALF) ==  BlockHalf.UPPER ? 3 : 0);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, AGE, HALF);
    }


    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return true;
    }

    @Override
    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        if(!worldIn.isRemote) {
            if(state.getValue(HALF) == BlockHalf.UPPER) {
                IBlockState downState = worldIn.getBlockState(pos.down());
                if(downState.getBlock() instanceof IGrowable) {
                    ((IGrowable)downState.getBlock()).grow(worldIn, rand, pos.down(), downState);
                }
                return;
            }
            int age = state.getValue(AGE);
            if (age <= 2) {
                IBlockState newState = state.withProperty(AGE, MathHelper.clamp(++age, 0, 2));
                worldIn.setBlockState(pos, newState);
                worldIn.setBlockState(pos.up(), newState.withProperty(HALF, BlockHalf.UPPER));
            }
        }
    }


    @Override
    public int damageDropped(IBlockState state)
    {
        return 0;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if(state.getValue(HALF) == BlockHalf.UPPER) {
            IBlockState downState = worldIn.getBlockState(pos.down());
            if(downState.getBlock() instanceof  WestIndianLilacBlock) {
                return downState.getBlock().onBlockActivated(worldIn, pos.down(), downState, playerIn, hand, side, hitX, hitY, hitZ);
            }
            return false;
        }
        int age = state.getValue(AGE);
        if (age == 2)
        {
            if (worldIn.isRemote) {
                return true;
            }

            IBlockState newState = state.withProperty(AGE, MathHelper.clamp(--age, 0, 2));

            worldIn.setBlockState(pos, newState);
            worldIn.setBlockState(pos.up(), newState.withProperty(HALF, BlockHalf.UPPER));

            ItemStack itemDrop = new ItemStack(ItemHandler.WEST_INDIAN_LILAC_BERRIES);
            EntityItem entityitem = new EntityItem(worldIn, playerIn.posX, playerIn.posY - 1.0D, playerIn.posZ, itemDrop);

            worldIn.spawnEntity(entityitem);

            if (!(playerIn instanceof FakePlayer))
            {
                entityitem.onCollideWithPlayer(playerIn);
            }

            return true;
        }

        return false;
    }

}
