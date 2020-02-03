package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

public class RaphaeliaPlant extends Plant {
    @Override
    public String getName() {
        return "Raphaelia";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.RAPHAELIA;
    }

    @Override
    public int getHealAmount() {
        return 2000;
    }
}
