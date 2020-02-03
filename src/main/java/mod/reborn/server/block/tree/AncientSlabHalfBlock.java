package mod.reborn.server.block.tree;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.state.IBlockState;

public class AncientSlabHalfBlock extends AncientSlabBlock {
    public AncientSlabHalfBlock(TreeType type, IBlockState state) {
        super(type, state);
        this.setCreativeTab(TabHandler.PLANTS);
    }

    @Override
    public boolean isDouble() {
        return false;
    }
}
