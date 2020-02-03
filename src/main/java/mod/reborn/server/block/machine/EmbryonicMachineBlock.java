package mod.reborn.server.block.machine;

import mod.reborn.server.block.OrientedBlock;
import mod.reborn.server.block.entity.EmbryonicMachineBlockEntity;
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

public class EmbryonicMachineBlock extends OrientedBlock {
    public static final PropertyBool PETRI_DISH = PropertyBool.create("dish");
    public static final PropertyBool TEST_TUBES = PropertyBool.create("tubes");

    public EmbryonicMachineBlock() {
        super(Material.IRON);
        this.setUnlocalizedName("embryonic_machine");
        this.setHardness(2.0F);
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(TabHandler.BLOCKS);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);

        if (stack.hasDisplayName()) {
            TileEntity tile = world.getTileEntity(pos);

            if (tile instanceof EmbryonicMachineBlockEntity) {
                ((EmbryonicMachineBlockEntity) tile).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tileentity = world.getTileEntity(pos);

        if (tileentity instanceof EmbryonicMachineBlockEntity) {
            InventoryHelper.dropInventoryItems(world, pos, (EmbryonicMachineBlockEntity) tileentity);
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        } else if (!player.isSneaking()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            
            	if (tileEntity instanceof EmbryonicMachineBlockEntity) {
            		EmbryonicMachineBlockEntity embryonicMachine = (EmbryonicMachineBlockEntity) tileEntity;

                if (embryonicMachine.isUsableByPlayer(player)) {
                    player.openGui(RebornMod.INSTANCE, ServerProxy.GUI_EMBRYONIC_MACHINE_ID, world, pos.getX(), pos.getY(), pos.getZ());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new EmbryonicMachineBlockEntity();
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
        return new BlockStateContainer(this, FACING, PETRI_DISH, TEST_TUBES);
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess access, BlockPos pos) {
        boolean dish = false;
        boolean tubes = false;
        TileEntity tile = access.getTileEntity(pos);
        if (tile instanceof EmbryonicMachineBlockEntity) {
            EmbryonicMachineBlockEntity machine = (EmbryonicMachineBlockEntity) tile;
            tubes = machine.getStackInSlot(1) != ItemStack.EMPTY;
            dish = machine.getStackInSlot(0) != ItemStack.EMPTY;
        }
        return state.withProperty(PETRI_DISH, dish).withProperty(TEST_TUBES, tubes);
    }
}
