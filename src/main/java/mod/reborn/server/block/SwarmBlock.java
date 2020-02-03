package mod.reborn.server.block;

import java.util.Random;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SwarmBlock extends Block {
    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.09F, 1.0F);

    private Supplier<Item> item;

    public SwarmBlock(Supplier<Item> item) {
        super(Material.PLANTS);
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
        this.setTickRandomly(true);
        this.setLightOpacity(0);
        this.item = item;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDS;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(world, pos, state, rand);
        this.checkForDrop(world, pos, state);
        if (rand.nextInt(10) == 0) {
            EntityItem item = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, new ItemStack(this.getItem()));
            item.motionX = (rand.nextFloat() - 0.5F) * 0.5F;
            item.motionY = 0.2F;
            item.motionZ = (rand.nextFloat() - 0.5F) * 0.5F;
            world.spawnEntity(item);
        }
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        super.neighborChanged(state, world, pos, block, fromPos);
        this.checkForDrop(world, pos, state);
    }

    private boolean checkForDrop(World world, BlockPos pos, IBlockState state) {
        if (!this.canBlockStay(world, pos)) {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
            return false;
        } else {
            return true;
        }
    }

    private boolean canBlockStay(World world, BlockPos pos) {
        return world.getBlockState(pos.down()).getMaterial() == Material.WATER;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return this.getItem();
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(this.getItem());
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this.getItem());
    }
    
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    public Item getItem() {
        return this.item.get();
    }
}
