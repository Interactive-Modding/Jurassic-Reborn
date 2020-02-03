package mod.reborn.server.block.tree;

import mod.reborn.server.item.ItemHandler;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Locale;
import java.util.Random;

public class AncientDoorBlock extends BlockDoor {
    private TreeType treeType;

    public AncientDoorBlock(TreeType treeType) {
        super(Material.WOOD);
        this.treeType = treeType;
        this.setHardness(2.0F);
        this.setResistance(5.0F);
        this.setSoundType(SoundType.WOOD);
        this.setUnlocalizedName(treeType.name().toLowerCase(Locale.ENGLISH) + "_door");
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        if (state.getValue(HALF) == EnumDoorHalf.UPPER) {
            return null;
        } else {
            return this.getItem();
        }
    }

    @Override
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(this.getItem());
    }

    private Item getItem() {
        return ItemHandler.ANCIENT_DOORS.get(this.treeType);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
}
