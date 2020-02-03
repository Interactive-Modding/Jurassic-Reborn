package mod.reborn.server.block;

import mod.reborn.server.tab.TabHandler;
import mod.reborn.RebornMod;
import mod.reborn.server.block.entity.BugCrateBlockEntity;
import mod.reborn.server.proxy.ServerProxy;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BugCrateBlock extends OrientedBlock {
    public BugCrateBlock() {
        super(Material.WOOD);
        this.setSoundType(SoundType.WOOD);
        this.setHardness(2.5F);
        this.setCreativeTab(TabHandler.BLOCKS);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        if (stack.hasDisplayName()) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof BugCrateBlockEntity) {
                ((BugCrateBlockEntity) tile).setCustomInventoryName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof BugCrateBlockEntity) {
            InventoryHelper.dropInventoryItems(worldIn, pos, (BugCrateBlockEntity) tile);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        } else if (!player.isSneaking()) {
            TileEntity tile = world.getTileEntity(pos);
            if (tile instanceof BugCrateBlockEntity) {
                BugCrateBlockEntity entity = (BugCrateBlockEntity) tile;
                if (entity.isUsableByPlayer(player)) {
                    player.openGui(RebornMod.INSTANCE, ServerProxy.GUI_BUG_CRATE, world, pos.getX(), pos.getY(), pos.getZ());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new BugCrateBlockEntity();
    }
}
