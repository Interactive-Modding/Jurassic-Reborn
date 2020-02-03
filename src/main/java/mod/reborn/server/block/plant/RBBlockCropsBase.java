package mod.reborn.server.block.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A configurable rework of the Minecraft BlockCrops.
 */
public abstract class RBBlockCropsBase extends BlockBush implements IGrowable {
    protected int seedDropMin = 0;
    protected int seedDropMax = 3;
    protected int cropDropMin = 1;
    protected int cropDropMax = 1;

    protected RBBlockCropsBase() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(this.getAgeProperty(), 0));
        this.setTickRandomly(true);
        // NOTE: No tab because the seeds are placed not the plant.
        this.setCreativeTab(null);
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
        this.disableStats();
    }

    protected static float getGrowthChance(Block block, World world, BlockPos pos) {
        float f = 1.0F;
        BlockPos blockpos = pos.down();

        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                float f1 = 0.0F;
                IBlockState state = world.getBlockState(blockpos.add(i, 0, j));

                if (state.getBlock().canSustainPlant(state, world, blockpos.add(i, 0, j), EnumFacing.UP, (IPlantable) block)) {
                    f1 = 1.0F;

                    if (state.getBlock().isFertile(world, blockpos.add(i, 0, j))) {
                        f1 = 3.0F;
                    }
                }

                if (i != 0 || j != 0) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        BlockPos blockpos1 = pos.north();
        BlockPos blockpos2 = pos.south();
        BlockPos blockpos3 = pos.west();
        BlockPos blockpos4 = pos.east();
        boolean flag = block == world.getBlockState(blockpos3).getBlock() || block == world.getBlockState(blockpos4).getBlock();
        boolean flag1 = block == world.getBlockState(blockpos1).getBlock() || block == world.getBlockState(blockpos2).getBlock();

        if (flag && flag1) {
            f /= 2.0F;
        } else {
            boolean flag2 = block == world.getBlockState(blockpos3.north()).getBlock() ||
                    block == world.getBlockState(blockpos4.north()).getBlock() ||
                    block == world.getBlockState(blockpos4.south()).getBlock() ||
                    block == world.getBlockState(blockpos3.south()).getBlock();

            if (flag2) {
                f /= 2.0F;
            }
        }

        return f;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0.1F, 0.0F, 0.1F, 0.9F, (state.getValue(this.getAgeProperty()) + 1) * 0.125F, 0.9F);
    }

    abstract protected PropertyInteger getAgeProperty();

    abstract protected int getMaxAge();

    // NOTE:  This is called on parent object construction.
    @Override
    abstract protected BlockStateContainer createBlockState();

    abstract protected Item getSeed();

    //============================================

    abstract protected Item getCrop();

    @Override
    protected boolean canSustainBush(IBlockState ground) {
        return ground == Blocks.FARMLAND;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(world, pos, state, rand);

        if (world.getLightFromNeighbors(pos.up()) >= 9) {
            int i = state.getValue(this.getAgeProperty());

            if (i < this.getMaxAge()) {
                float f = getGrowthChance(this, world, pos);

                if (rand.nextInt((int) (25.0F / f) + 1) == 0) {
                    world.setBlockState(pos, state.withProperty(this.getAgeProperty(), i + 1), 2);
                }
            }
        }
    }

    public void grow(World world, BlockPos pos, IBlockState state) {
        // TODO:  Pull out these two numbers.
        int i = state.getValue(this.getAgeProperty()) + MathHelper.getInt(world.rand, 2, 5);

        if (i > this.getMaxAge()) {
            i = this.getMaxAge();
        }

        world.setBlockState(pos, state.withProperty(this.getAgeProperty(), i), 2);
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        return (world.getLight(pos) >= 8 || world.canSeeSky(pos)) && world.getBlockState(pos.down()).getBlock().canSustainPlant(state, world, pos.down(), net.minecraft.util.EnumFacing.UP, this);
    }

    @Override
    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
        return state.getValue(this.getAgeProperty()) < this.getMaxAge();
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return true;
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this.getSeed());
    }

    @Override
    public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
        this.grow(world, pos, state);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(this.getAgeProperty(), meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(this.getAgeProperty());
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(this.getAgeProperty()) == this.getMaxAge() ? this.getCrop() : this.getSeed();
    }

    @Override
    public List<ItemStack> getDrops(net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> drops = new ArrayList<>();

        int age = state.getValue(this.getAgeProperty());
        Random rand = world instanceof World ? ((World) world).rand : new Random();

        if (age < this.getMaxAge()) {
            drops.add(new ItemStack(this.getSeed()));
        } else {
            // Drop range of leaves and range of seeds
            if (this.seedDropMin > 0 && this.seedDropMax > 0) {
                drops.add(new ItemStack(this.getSeed(), MathHelper.getInt(rand, this.seedDropMin, this.seedDropMax)));
            }
            if (this.cropDropMin > 0 && this.cropDropMax > 0) {
                drops.add(new ItemStack(this.getCrop(), MathHelper.getInt(rand, this.cropDropMin, this.cropDropMax)));
            }
        }

        return drops;
    }
}
