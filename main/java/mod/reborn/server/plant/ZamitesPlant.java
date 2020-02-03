package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

public class ZamitesPlant extends Plant {
    @Override
    public String getName() {
        return "Cycad Zamites";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.ZAMITES;
    }

    @Override
    public int getHealAmount() {
        return 4000;
    }
}
