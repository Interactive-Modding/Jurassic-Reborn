package mod.reborn.server.block.machine;

import mod.reborn.server.block.OrientedBlock;
import mod.reborn.server.block.entity.EmbryoCalcificationMachineBlockEntity;
import mod.reborn.server.tab.TabHandler;
import mod.reborn.RebornMod;
import mod.reborn.server.proxy.ServerProxy;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EmbryoCalcificationMachineBlock extends OrientedBlock {
    public static final PropertyBool EGG = PropertyBool.create("egg");

    public EmbryoCalcificationMachineBlock() {
        super(Material.IRON);
        this.setUnlocalizedName("embryo_calcification_machine");
        this.setHardness(2.0F);
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(TabHandler.BLOCKS);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);

        if (stack.hasDisplayName()) {
            TileEntity tileentity = world.getTileEntity(pos);

            if (tileentity instanceof EmbryoCalcificationMachineBlockEntity) {
                ((EmbryoCalcificationMachineBlockEntity) tileentity).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tileentity = world.getTileEntity(pos);

        if (tileentity instanceof EmbryoCalcificationMachineBlockEntity) {
            InventoryHelper.dropInventoryItems(world, pos, (EmbryoCalcificationMachineBlockEntity) tileentity);
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        } else if (!player.isSneaking()) {
            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof EmbryoCalcificationMachineBlockEntity) {
                EmbryoCalcificationMachineBlockEntity machineTile = (EmbryoCalcificationMachineBlockEntity) tile;

                if (machineTile.isUsableByPlayer(player)) {
                    player.openGui(RebornMod.INSTANCE, ServerProxy.GUI_EMBRYO_CALCIFICATION_MACHINE_ID, world, pos.getX(), pos.getY(), pos.getZ());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new EmbryoCalcificationMachineBlockEntity();
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
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, EGG);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess access, BlockPos pos) {
        boolean egg = false;
        TileEntity tile = access.getTileEntity(pos);
        if (tile instanceof EmbryoCalcificationMachineBlockEntity) {
            EmbryoCalcificationMachineBlockEntity machine = (EmbryoCalcificationMachineBlockEntity) tile;
            egg = machine.getStackInSlot(1) != null || machine.getStackInSlot(2) != null;
        }
        return state.withProperty(EGG, egg);
    }
}
