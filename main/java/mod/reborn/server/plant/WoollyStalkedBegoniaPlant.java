package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

public class WoollyStalkedBegoniaPlant extends Plant {
    @Override
    public String getName() { return "Woolly Stalked Begonia"; }

    @Override
    public Block getBlock() {
        return BlockHandler.WOOLLY_STALKED_BEGONIA;
    }

    @Override
    public int getHealAmount() {
        return 2000;
    }
}
