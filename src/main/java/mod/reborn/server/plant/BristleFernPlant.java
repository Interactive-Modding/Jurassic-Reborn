package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

public class BristleFernPlant extends Plant {
    @Override
    public String getName() {
        return "Bristle Fern";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.BRISTLE_FERN;
    }

    @Override
    public int getHealAmount() {
        return 2000;
    }
}
