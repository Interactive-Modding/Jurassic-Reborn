package mod.reborn.server.block.machine;

import mod.reborn.server.tab.TabHandler;
import mod.reborn.RebornMod;
import mod.reborn.server.proxy.ServerProxy;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SkeletonAssemblyBlock extends Block {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyEnum<BlockHalf> HALF = PropertyEnum.create("half", BlockHalf.class);

    public SkeletonAssemblyBlock() {
        super(Material.WOOD);
        this.setUnlocalizedName("skeleton_assembly");
        this.setHardness(2.0F);
        this.setLightOpacity(0);
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(TabHandler.BLOCKS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(HALF, BlockHalf.MAIN));
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (state.getValue(HALF) == BlockHalf.DUMMY) {
            pos = this.getOpposite(pos, state);
        }
        if (!world.isRemote) {
            player.openGui(RebornMod.INSTANCE, ServerProxy.GUI_SKELETON_ASSEMBLER, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    public BlockPos getOpposite(BlockPos pos, IBlockState state) {
        EnumFacing facing = state.getValue(FACING);
        BlockHalf half = state.getValue(HALF);
        return pos.offset(half == BlockHalf.MAIN ? facing.rotateYCCW() : facing.rotateY());
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState state = this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
        if (!world.isAirBlock(this.getOpposite(pos, state))) {
            return Blocks.AIR.getDefaultState();
        }
        return state;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        EnumFacing facing = placer.getHorizontalFacing().getOpposite();
        state = state.withProperty(FACING, facing);
        world.setBlockState(pos, state, 2);
        world.setBlockState(getOpposite(pos, state), state.withProperty(HALF, BlockHalf.DUMMY), 2);
    }

    @Override
    public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        BlockPos opposite = this.getOpposite(pos, state);
        world.setBlockToAir(opposite);

        super.onBlockHarvested(world, pos, state, player);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        BlockHalf half = (meta & 1) == 1 ? BlockHalf.DUMMY : BlockHalf.MAIN;
        EnumFacing facing = EnumFacing.getFront(meta >> 1);

        if (facing.getAxis() == EnumFacing.Axis.Y) {
            facing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, facing).withProperty(HALF, half);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex() << 1 | state.getValue(HALF).ordinal() & 1;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, HALF);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return state.getValue(HALF) == BlockHalf.MAIN ? EnumBlockRenderType.MODEL : EnumBlockRenderType.INVISIBLE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
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
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }

    public enum BlockHalf implements IStringSerializable {
        MAIN, DUMMY;

        @Override
        public String toString() {
            return this.getName();
        }

        @Override
        public String getName() {
            return this == MAIN ? "main" : "dummy";
        }
    }
}
