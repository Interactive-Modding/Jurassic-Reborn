package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

public class TempskyaPlant extends Plant {
    @Override
    public String getName() {
        return "Tempskya";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.TEMPSKYA;
    }

    @Override
    public int getHealAmount() {
        return 4000;
    }
}
