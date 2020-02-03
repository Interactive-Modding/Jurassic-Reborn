package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

public class LiriodendritesPlant extends Plant {
    @Override
    public String getName() {
        return "Liriodendrites";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.LIRIODENDRITES;
    }

    @Override
    public int getHealAmount() {
        return 4000;
    }
}
