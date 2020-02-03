package mod.reborn.server.block.tree;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;

import java.util.Locale;

public class AncientStairsBlock extends BlockStairs {
    public AncientStairsBlock(TreeType type, IBlockState state) {
        super(state);
        this.setCreativeTab(TabHandler.PLANTS);
        this.setUnlocalizedName(type.name().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_") + "_stairs");
    }
}