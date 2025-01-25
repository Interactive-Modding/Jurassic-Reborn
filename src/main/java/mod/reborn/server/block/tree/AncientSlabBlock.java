package mod.reborn.server.block.tree;

import mod.reborn.server.api.SubBlocksBlock;
import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.item.block.AncientSlabItemBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Locale;

public abstract class AncientSlabBlock extends BlockSlab implements SubBlocksBlock {
    private TreeType type;

    public AncientSlabBlock(TreeType type, IBlockState referenceState) {
        super(referenceState.getBlock().getMaterial(referenceState));
        this.type = type;
        IBlockState state = this.blockState.getBaseState();
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        if (!this.isDouble()) {
            state = state.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        }

        Block block = state.getBlock();

        this.setHardness(block.getBlockHardness(state, null, null));
        this.setResistance((block.getExplosionResistance(null) * 5.0F) / 3.0F);
        this.setSoundType(SoundType.WOOD);
        this.setHarvestLevel(block.getHarvestTool(state), block.getHarvestLevel(state));
        this.setUnlocalizedName(type.name().toLowerCase(Locale.ENGLISH) + "_slab");

        this.setDefaultState(state);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        if (!this.isDouble()) {
            if ((facing == EnumFacing.UP || (double) hitY <= 0.5D) && facing != EnumFacing.DOWN) {
                return state;
            } else {
                return state.withProperty(HALF, BlockSlab.EnumBlockHalf.TOP);
            }
        }
        return state;
    }

    @Override
    public String getUnlocalizedName(int meta) {
        return super.getUnlocalizedName();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = this.getDefaultState();

        if (!this.isDouble()) {
            state = state.withProperty(HALF, meta == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
        }

        return state;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(HALF) == BlockSlab.EnumBlockHalf.BOTTOM ? 0 : 1;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HALF);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }

    @Override
    public Comparable<?> getTypeForItem(ItemStack stack) {
        return null;
    }

    @Override
    public IProperty<?> getVariantProperty() {
        return null;
    }

    @Override
    public ItemBlock getItemBlock() {
        return new AncientSlabItemBlock(this, BlockHandler.ANCIENT_SLABS.get(this.type), BlockHandler.ANCIENT_DOUBLE_SLABS.get(this.type));
    }
}
