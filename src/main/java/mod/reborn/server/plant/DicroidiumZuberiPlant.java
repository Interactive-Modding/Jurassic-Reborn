package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

public class DicroidiumZuberiPlant extends Plant {
    @Override
    public String getName() {
        return "Dicroidium Zuberi";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.DICROIDIUM_ZUBERI;
    }

    @Override
    public int getHealAmount() {
        return 4000;
    }
}
