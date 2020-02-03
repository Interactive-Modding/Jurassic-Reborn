package mod.reborn.server.block.fence;

import java.util.List;

import mod.reborn.server.tab.TabHandler;
import mod.reborn.server.block.entity.ElectricFenceBaseBlockEntity;
import mod.reborn.server.block.entity.ElectricFenceWireBlockEntity;
import mod.reborn.server.entity.DinosaurEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ElectricFenceBaseBlock extends BlockContainer {
    public static final PropertyDirection FACING_BIAS = BlockHorizontal.FACING;
    public static final PropertyBool POLE = PropertyBool.create("pole");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyInteger CONNECTIONS = PropertyInteger.create("connections", 0, 4);

    private static final AxisAlignedBB EXTENDED_BOUNDS = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 1.5, 1.0);

    private FenceType type;

    public ElectricFenceBaseBlock(FenceType type) {
        super(Material.IRON);
        this.setHardness(3.5F);
        this.setCreativeTab(TabHandler.BLOCKS);
        this.setSoundType(SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING_BIAS, EnumFacing.NORTH).withProperty(NORTH, false).withProperty(SOUTH, false).withProperty(WEST, false).withProperty(EAST, false));
        this.type = type;
    }

    
    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox,
    		List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185477_7_) {
        if (entityIn instanceof DinosaurEntity && !world.isAirBlock(pos.up())) {
            addCollisionBoxToList(pos, entityBox, collidingBoxes, EXTENDED_BOUNDS);
        } else {
            super.addCollisionBoxToList(state, world, pos, entityBox, collidingBoxes, entityIn, p_185477_7_);
        }
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
        TileEntity wireEntity = world.getTileEntity(pos.up());
        if (wireEntity instanceof ElectricFenceWireBlockEntity && ((ElectricFenceWireBlockEntity) wireEntity).isPowered()) {
            if (entity instanceof DinosaurEntity) {
                DinosaurEntity dinosaur = (DinosaurEntity) entity;
                if (dinosaur.getDinosaur().canClimb()) {
                    return true;
                }
            }
        }
        return false;
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
    public TileEntity createNewTileEntity(World world, int meta) {
        return new ElectricFenceBaseBlockEntity();
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY,
    		float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING_BIAS, placer.getHorizontalFacing().rotateY());
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        boolean north = this.canConnect(world.getBlockState(pos.offset(EnumFacing.NORTH)));
        boolean south = this.canConnect(world.getBlockState(pos.offset(EnumFacing.SOUTH)));
        boolean west = this.canConnect(world.getBlockState(pos.offset(EnumFacing.EAST)));
        boolean east = this.canConnect(world.getBlockState(pos.offset(EnumFacing.WEST)));
        int connections = 0;
        if (north) {
            connections++;
        }
        if (south) {
            connections++;
        }
        if (west) {
            connections++;
        }
        if (east) {
            connections++;
        }
        boolean pole = world.getBlockState(pos.up()).getBlock() instanceof ElectricFencePoleBlock;
        if (!pole && connections == 0) {
            connections = 1;
            switch (state.getValue(FACING_BIAS)) {
                case NORTH:
                    north = true;
                    break;
                case SOUTH:
                    south = true;
                    break;
                case EAST:
                    east = true;
                    break;
                case WEST:
                    west = true;
                    break;
			default:
				break;
            }
        }
        return state.withProperty(NORTH, north).withProperty(SOUTH, south).withProperty(WEST, west).withProperty(EAST, east).withProperty(POLE, pole).withProperty(CONNECTIONS, connections);
    }

    protected boolean canConnect(IBlockState state) {
        return state.getBlock() instanceof ElectricFenceBaseBlock && ((ElectricFenceBaseBlock) state.getBlock()).getType().equals(type);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getHorizontal(meta);
        if (facing.getAxis() == EnumFacing.Axis.Y) {
            facing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(FACING_BIAS, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING_BIAS).getHorizontalIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING_BIAS, NORTH, SOUTH, EAST, WEST, POLE, CONNECTIONS);
    }

    @Override
    public int getLightOpacity(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getActualState(world, pos).getValue(POLE) ? 3 : super.getLightOpacity(state, world, pos);
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!world.isRemote) {
            IBlockState poleState = world.getBlockState(pos.up());
            if (poleState.getBlock() instanceof ElectricFencePoleBlock) {
                ((ElectricFencePoleBlock) poleState.getBlock()).updateConnectedWires(world, pos.up());
            }
        }
    }

    public FenceType getType()
    {
        return type;
    }
}
