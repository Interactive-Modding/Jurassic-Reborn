package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

public class DicksoniaPlant extends Plant {
    @Override
    public String getName() {
        return "Dicksonia";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.DICKSONIA;
    }

    @Override
    public int getHealAmount() {
        return 4000;
    }
}
