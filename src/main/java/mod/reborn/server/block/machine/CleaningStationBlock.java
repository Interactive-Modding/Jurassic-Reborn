package mod.reborn.server.block.machine;

import mod.reborn.server.block.OrientedBlock;
import mod.reborn.server.block.entity.CleaningStationBlockEntity;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
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
import mod.reborn.RebornMod;
import mod.reborn.server.proxy.ServerProxy;

public class CleaningStationBlock extends OrientedBlock {
    public CleaningStationBlock() {
        super(Material.IRON);
        this.setUnlocalizedName("cleaning_station");
        this.setHardness(2.0F);
        this.setLightOpacity(0);
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(TabHandler.BLOCKS);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);

        if (stack.hasDisplayName()) {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof CleaningStationBlockEntity) {
                ((CleaningStationBlockEntity) tileentity).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof CleaningStationBlockEntity) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (CleaningStationBlockEntity) tileentity);
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,
    		EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        } else if (!player.isSneaking()) {
            TileEntity tileEntity = world.getTileEntity(pos);

            if (tileEntity instanceof CleaningStationBlockEntity) {
                CleaningStationBlockEntity cleaningStation = (CleaningStationBlockEntity) tileEntity;

                if (cleaningStation.isUsableByPlayer(player)) {
                    player.openGui(RebornMod.INSTANCE, ServerProxy.GUI_CLEANING_STATION_ID, world, pos.getX(), pos.getY(), pos.getZ());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new CleaningStationBlockEntity();
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
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return true;
    }
}
