package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

public class CinnamonFernPlant extends Plant {
    @Override
    public String getName() {
        return "Cinnamon Fern";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.CINNAMON_FERN;
    }

    @Override
    public int getHealAmount() {
        return 2000;
    }
}
