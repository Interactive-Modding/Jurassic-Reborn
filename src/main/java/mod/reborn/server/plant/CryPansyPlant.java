package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

public class CryPansyPlant extends Plant {
    @Override
    public String getName() {
        return "Cry Pansy";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.CRY_PANSY;
    }

    @Override
    public int getHealAmount() {
        return 250;
    }
}
