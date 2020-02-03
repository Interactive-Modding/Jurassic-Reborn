package mod.reborn.server.block.machine;

import mod.reborn.server.api.SubBlocksBlock;
import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.entity.CultivatorBlockEntity;
import mod.reborn.server.item.block.CultivateItemBlock;
import net.minecraft.world.IBlockAccess;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CultivatorBlock extends BlockContainer implements SubBlocksBlock {
    public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);

    public CultivatorBlock(String position) {
        super(Material.IRON);
        this.setUnlocalizedName("cultivate");
        this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
        this.setHardness(2.0F);
        this.setResistance(5.0F);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(COLOR).getMetadata();
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (world.getBlockState(pos).getBlock() == BlockHandler.CULTIVATOR_TOP) {
        	//TODO Verify Working
        	pos = pos.down();
        }

        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof CultivatorBlockEntity) {
            InventoryHelper.dropInventoryItems(world, pos, (CultivatorBlockEntity) tile);
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> subtypes) {
        EnumDyeColor[] colors = EnumDyeColor.values();

        for (EnumDyeColor color : colors) {
            subtypes.add(new ItemStack(this, 1, color.getMetadata()));
        }
    }

    @Override
    public ItemBlock getItemBlock() {
        return new CultivateItemBlock(this);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new CultivatorBlockEntity();
    }

    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        return state.getMapColor(worldIn, pos);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(COLOR).getMetadata();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, COLOR);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
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
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
