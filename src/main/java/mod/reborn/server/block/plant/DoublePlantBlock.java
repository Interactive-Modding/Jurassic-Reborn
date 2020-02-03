package mod.reborn.server.block.plant;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class DoublePlantBlock extends AncientPlantBlock {
    public static final PropertyEnum<BlockHalf> HALF = PropertyEnum.create("half", BlockHalf.class);

    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.1F, 0.0F, 0.1F, 0.9F, 1.0F, 0.9F);

    public DoublePlantBlock(Material material) {
        super(material);
        this.setHardness(0.0F);
    }

    public DoublePlantBlock() {
        this(Material.VINE);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDS;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return super.canPlaceBlockAt(world, pos) && world.isAirBlock(pos.up());
    }

    @Override
    public boolean isReplaceable(IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);

        return state.getBlock() != this;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(HALF, BlockHalf.values()[meta]);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((BlockHalf) state.getValue(HALF)).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HALF);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, this.getDefaultState().withProperty(HALF, BlockHalf.LOWER), 2);
        worldIn.setBlockState(pos.up(), this.getDefaultState().withProperty(HALF, BlockHalf.UPPER), 2);
    }

    @Override
    protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state) {
        if (!this.canBlockStay(world, pos, state)) {
            boolean upperPart = state.getValue(HALF) == BlockHalf.UPPER;
            BlockPos top = upperPart ? pos : pos.up();
            BlockPos bottom = upperPart ? pos.down() : pos;
            Block topBlock = upperPart ? this : world.getBlockState(top).getBlock();
            Block bottomBlock = upperPart ? world.getBlockState(bottom).getBlock() : this;

            if (!upperPart) {
                this.dropBlockAsItem(world, pos, state, 0);
            }

            if (topBlock == this) {
                world.setBlockState(top, Blocks.AIR.getDefaultState(), 3);
            }

            if (bottomBlock == this) {
                world.setBlockState(bottom, Blocks.AIR.getDefaultState(), 3);
            }
        }
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        if (state.getBlock() != this) {
            return super.canBlockStay(world, pos, state);
        }
        if (state.getValue(HALF) == BlockHalf.UPPER) {
            return world.getBlockState(pos.down()).getBlock() == this;
        } else {
            IBlockState up = world.getBlockState(pos.up());
            return up.getBlock() == this && super.canBlockStay(world, pos, up);
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        if (state.getValue(HALF) == BlockHalf.UPPER) {
            return null;
        } else {
            return super.getItemDropped(state, rand, fortune);
        }
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (state.getValue(HALF) == BlockHalf.UPPER) {
            if (world.getBlockState(pos.down()).getBlock() == this) {
                if (!player.capabilities.isCreativeMode) {
                    IBlockState lowerBlock = world.getBlockState(pos.down());

                    if (lowerBlock.getBlock() == this) {
                        world.destroyBlock(pos.down(), true);
                    }
                } else {
                    world.setBlockToAir(pos.down());
                }
            }
        } else if (player.capabilities.isCreativeMode && world.getBlockState(pos.up()).getBlock() == this) {
            world.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 2);
        }

        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if (state.getBlock() == this && state.getValue(HALF) == BlockHalf.LOWER && world.getBlockState(pos.up()).getBlock() == this) {
            world.setBlockToAir(pos.up());
        }

        return world.setBlockToAir(pos);
    }

    @Override
    protected void spread(World world, BlockPos position) {
        world.setBlockState(position, this.getDefaultState().withProperty(HALF, BlockHalf.LOWER));
        world.setBlockState(position.up(), this.getDefaultState().withProperty(HALF, BlockHalf.UPPER));
    }

    @Override
    protected boolean canPlace(IBlockState down, IBlockState here, IBlockState up) {
        return super.canPlace(down, here, up) && up.getBlock() == Blocks.AIR;
    }

    public enum BlockHalf implements IStringSerializable {
        UPPER, LOWER;

        @Override
        public String toString() {
            return this.getName();
        }

        @Override
        public String getName() {
            return this == UPPER ? "upper" : "lower";
        }
    }
}
