package mod.reborn.server.block.fence;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import mod.reborn.server.block.entity.ElectricFencePoleBlockEntity;
import mod.reborn.server.block.entity.ElectricFenceWireBlockEntity;

import java.util.HashSet;

public class ElectricFencePoleBlock extends BlockContainer {
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool ACTIVE = PropertyBool.create("active");

    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.3425, 0.0, 0.3425, 0.6575, 1.0, 0.6575);

    private FenceType type;

    public ElectricFencePoleBlock(FenceType type) {
        super(Material.IRON);
        this.setHardness(3.0F);
        this.setCreativeTab(TabHandler.BLOCKS);
        this.setSoundType(SoundType.METAL);
        this.type = type;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return getBoundingBox(state, world, pos);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        IBlockState actualState = this.getActualState(state, world, pos);

        boolean north = actualState.getValue(NORTH);
        boolean south = actualState.getValue(SOUTH);
        boolean west = actualState.getValue(WEST);
        boolean east = actualState.getValue(EAST);

        double minX = west ? 0.0 : 0.3425;
        double maxX = east ? 1.0 : 0.6575;
        double minZ = north ? 0.0 : 0.3425;
        double maxZ = south ? 1.0 : 0.6575;

        return new AxisAlignedBB(minX, 0.0, minZ, maxX, 1.0, maxZ);
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
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new ElectricFencePoleBlockEntity();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ACTIVE, NORTH, SOUTH, WEST, EAST);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess access, BlockPos pos) {
        BlockPos northPos = pos.offset(EnumFacing.NORTH);
        BlockPos southPos = pos.offset(EnumFacing.SOUTH);
        BlockPos eastPos = pos.offset(EnumFacing.EAST);
        BlockPos westPos = pos.offset(EnumFacing.WEST);
        IBlockState northBlock = access.getBlockState(northPos);
        IBlockState southBlock = access.getBlockState(southPos);
        IBlockState westBlock = access.getBlockState(westPos);
        IBlockState eastBlock = access.getBlockState(eastPos);
        boolean north = this.canConnect(access, northPos, EnumFacing.NORTH, northBlock);
        boolean south = this.canConnect(access, southPos, EnumFacing.SOUTH, southBlock);
        boolean west = this.canConnect(access, westPos, EnumFacing.WEST, westBlock);
        boolean east = this.canConnect(access, eastPos, EnumFacing.EAST, eastBlock);
        boolean powered = false;
        BlockPos downPos = pos.down();
        IBlockState down = access.getBlockState(downPos);
        if ((this.isBlockPowered(access, downPos) && down.getBlock() instanceof ElectricFenceBaseBlock) || (down.getBlock() instanceof ElectricFencePoleBlock && down.getActualState(access, downPos).getValue(ACTIVE))) {
            powered = true;
        }
        return state.withProperty(NORTH, north).withProperty(SOUTH, south).withProperty(WEST, west).withProperty(EAST, east).withProperty(ACTIVE, powered);
    }

    private int getRedstonePower(IBlockAccess access, BlockPos pos, EnumFacing facing) {
        IBlockState state = access.getBlockState(pos);
        if (state.getBlock().shouldCheckWeakPower(state, access, pos, facing)) {
            return this.getStrongPower(access, pos);
        } else {
            return state.getWeakPower(access, pos, facing);
        }
    }

    private int getStrongPower(IBlockAccess access, BlockPos pos) {
        int highest = 0;
        for (EnumFacing facing : EnumFacing.VALUES) {
            highest = Math.max(highest, access.getStrongPower(pos.offset(facing), facing));
            if (highest >= 15) {
                return highest;
            }
        }
        return highest;
    }

    private boolean isBlockPowered(IBlockAccess access, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.VALUES) {
            if (this.getRedstonePower(access, pos.offset(facing), facing) > 0) {
                return true;
            }
        }
        return false;
    }

    protected boolean canConnect(IBlockAccess world, BlockPos pos, EnumFacing direction, IBlockState state) {
        if ((state.getBlock() instanceof ElectricFenceWireBlock && ((ElectricFenceWireBlock) state.getBlock()).getType().equals(type)) || (state.getBlock() instanceof ElectricFencePoleBlock) && ((ElectricFencePoleBlock) state.getBlock()).getType().equals(type)) {
            return true;
        } else {
            IBlockState down = world.getBlockState(pos.down());
            if (down.getBlock() instanceof ElectricFenceWireBlock && down.getActualState(world, pos).getValue(ElectricFenceWireBlock.UP_DIRECTION).getOpposite() == direction) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!world.isRemote) {
            this.updateConnectedWires(world, pos);
        }
    }

    public void updateConnectedWires(World world, BlockPos pos) {
        boolean powered = world.isBlockPowered(pos.down());
        HashSet<BlockPos> wires = new HashSet<>();
        HashSet<BlockPos> bases = new HashSet<>();
        this.processConnectedWires(bases, wires, world, pos.down(), pos.down());
        for (BlockPos wirePos : wires) {
            TileEntity tile = world.getTileEntity(wirePos);
            if (tile instanceof ElectricFenceWireBlockEntity) {
                ElectricFenceWireBlockEntity wire = (ElectricFenceWireBlockEntity) tile;
                wire.power(pos, powered);
            }
        }
    }

    public FenceType getType()
    {
        return type;
    }

    protected void processConnectedWires(HashSet<BlockPos> bases, HashSet<BlockPos> wires, World world, BlockPos origin, BlockPos current) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (!((x == z || x == -z) && y != 0)) {
                        BlockPos offset = current.add(x, y, z);
                        if (!bases.contains(offset)) {
                            bases.add(offset);
                            IBlockState state = world.getBlockState(offset);
                            Block block = state.getBlock();
                            if (block instanceof ElectricFenceBaseBlock) {
                                int wireX = offset.getX();
                                int wireZ = offset.getZ();
                                int deltaX = wireX - origin.getX();
                                int deltaZ = wireZ - origin.getZ();
                                Chunk chunk = world.getChunkFromBlockCoords(offset);
                                double delta = (deltaX * deltaX) + (deltaZ * deltaZ);
                                if (delta <= 64) {
                                    int currentY = offset.getY();
                                    while (chunk.getBlockState(wireX, ++currentY, wireZ).getBlock() instanceof ElectricFenceWireBlock) {
                                        BlockPos wirePos = new BlockPos(wireX, currentY, wireZ);
                                        if (!wires.contains(wirePos)) {
                                            wires.add(wirePos);
                                        }
                                    }
                                    this.processConnectedWires(bases, wires, world, origin, offset);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
