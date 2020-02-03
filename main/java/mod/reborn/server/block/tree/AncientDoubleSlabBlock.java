package mod.reborn.server.block.tree;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Locale;
import java.util.Random;

public class AncientDoubleSlabBlock extends AncientSlabBlock {
    private Block singleSlab;

    public AncientDoubleSlabBlock(TreeType treeType, Block slab, IBlockState state) {
        super(treeType, state);
        this.setUnlocalizedName(treeType.name().toLowerCase(Locale.ENGLISH) + "_double_slab");
        this.singleSlab = slab;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this.singleSlab);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Item.getItemFromBlock(this.singleSlab));
    }

    @Override
    public boolean isDouble() {
        return true;
    }
}
