package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.tree.TreeType;
import net.minecraft.block.Block;

public class PsaroniusPlant extends Plant {
    @Override
    public String getName() {
        return "Psaronius";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.ANCIENT_SAPLINGS.get(TreeType.PSARONIUS);
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
