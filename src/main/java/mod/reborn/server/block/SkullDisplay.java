package mod.reborn.server.block;

import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


import mod.reborn.server.block.entity.SkullDisplayEntity;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.item.DisplayBlockItem;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.proxy.ServerProxy;
import mod.reborn.server.tab.TabHandler;
import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SkullDisplay extends BlockContainer {
	
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    
    public SkullDisplay() {
        super(Material.ROCK);
        
        this.setSoundType(SoundType.STONE);
        this.setHardness(0.0F);
        this.setResistance(0.0F);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
    }
    
    @Override
    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    	return false;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, facing);
    }
    
    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
    	final SkullDisplayEntity tile = getTile(world, pos);
        if(tile != null && world.getBlockState(pos).getValue(SkullDisplay.FACING) == EnumFacing.UP) {
        	return new AxisAlignedBB(-0.125, 0, -0.125, 1.125, 1.25, 1.125);
        }else if(tile != null){
        	return new AxisAlignedBB(0, -0.25, 0, 1, 1, 1);
        }
		return Block.FULL_BLOCK_AABB;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new SkullDisplayEntity();
    }
    
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        super.neighborChanged(state, world, pos, block, fromPos);
        this.checkAndDropBlock(world, pos, world.getBlockState(pos));
    }
    
    private void checkAndDropBlock(World world, BlockPos pos, IBlockState state) {
        if (!canBlockStay(world, pos)) {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
        }
    }
    
    private static boolean canBlockStay(World world, BlockPos pos) {
        
    	final EnumFacing facing = world.getBlockState(pos).getValue(SkullDisplay.FACING);
    	final EnumFacing.Axis axis = facing.getAxis();
    	if (axis == EnumFacing.Axis.Y) {
    		return world.getBlockState(pos.down()).isOpaqueCube();
    	}else {
    		return world.getBlockState(pos.offset(mirror(facing))).isOpaqueCube();
    	}
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }
    
    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!player.capabilities.isCreativeMode) {
            this.dropBlockAsItem(world, pos, state, 0);
        }

        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return getItemFromTile(getTile(world, pos));
    }

    private static ItemStack getItemFromTile(SkullDisplayEntity tile) {
    	final ItemStack stack;
    	if(tile.isFossilized()) {
    		stack = new ItemStack(ItemHandler.FOSSILS.get("skull"), 1, EntityHandler.getDinosaurId(tile.getDinosaur()));
    	}else {
    		stack = new ItemStack(ItemHandler.FRESH_FOSSILS.get("skull"), 1, EntityHandler.getDinosaurId(tile.getDinosaur()));
    	}
        return stack;
    }
    
    private static SkullDisplayEntity getTile(IBlockAccess world, BlockPos pos) {
        return (SkullDisplayEntity) world.getTileEntity(pos);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> drops = new ArrayList<>(1);

        final SkullDisplayEntity tile = getTile(world, pos);

        if (tile != null) {
            drops.add(getItemFromTile(tile));
        }

        return drops;
    }
    
    public static EnumFacing mirror(EnumFacing facing)
    {
        switch (facing)
        {
            case NORTH:
                return EnumFacing.SOUTH;
            case EAST:
                return EnumFacing.WEST;
            case SOUTH:
                return EnumFacing.NORTH;
            case WEST:
                return EnumFacing.EAST;
            default:
            	return facing;
        }
    }
}
