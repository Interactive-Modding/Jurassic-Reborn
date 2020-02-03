package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.tree.TreeType;
import net.minecraft.block.Block;

public class GinkgoPlant extends Plant {
    @Override
    public String getName() {
        return "Ginkgo";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.ANCIENT_SAPLINGS.get(TreeType.GINKGO);
    }

    @Override
    public int getHealAmount() {
        return 1000;
    }

    @Override
    public boolean isTree() {
        return true;
    }
}
