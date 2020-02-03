package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

public class WildOnionPlant extends Plant {
    @Override
    public String getName() {
        return "Wild Onion";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.WILD_ONION;
    }

    @Override
    public int getHealAmount() {
        return 3000;
    }
}
