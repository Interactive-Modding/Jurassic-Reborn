package mod.reborn.server.block;

import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;

import java.util.Random;

public class GypsumStoneBlock extends Block {
    public GypsumStoneBlock() {
        super(Material.ROCK);
        this.setCreativeTab(TabHandler.BLOCKS);
        this.setHardness(1.5F);
        this.setResistance(1.5F);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ItemHandler.GYPSUM_POWDER;
    }

    @Override
    public int quantityDropped(Random random) {
        return random.nextInt(5) + 4;
    }
}
