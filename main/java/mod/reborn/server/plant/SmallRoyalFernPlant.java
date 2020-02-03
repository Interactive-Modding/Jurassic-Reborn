package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

public class SmallRoyalFernPlant extends Plant {
    @Override
    public String getName() {
        return "Small Royal Fern";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.SMALL_ROYAL_FERN;
    }

    @Override
    public int getHealAmount() {
        return 2000;
    }
}
