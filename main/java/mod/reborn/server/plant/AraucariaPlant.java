package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.tree.TreeType;
import net.minecraft.block.Block;

public class AraucariaPlant extends Plant {
    @Override
    public String getName() {
        return "Araucaria";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.ANCIENT_SAPLINGS.get(TreeType.ARAUCARIA);
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
