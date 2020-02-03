package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

public class UmaltolepisPlant extends Plant {
    @Override
    public String getName() {
        return "Umaltolepis";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.UMALTOLEPIS;
    }

    @Override
    public int getHealAmount() {
        return 4000;
    }
}
