package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

public class RhamnusSalifocifusPlant extends Plant {
    @Override
    public String getName() {
        return "Rhamnus Salifocifus";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.RHAMNUS_SALICIFOLIUS_PLANT;
    }

    @Override
    public int getHealAmount() {
        return 3000;
    }
}
