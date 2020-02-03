package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

public class BennettitaleanCycadeoideaPlant extends Plant {
    @Override
    public String getName() {
        return "Bennettitalean Cycadeoidea";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.CYCADEOIDEA;
    }

    @Override
    public int getHealAmount() {
        return 2000;
    }
}
