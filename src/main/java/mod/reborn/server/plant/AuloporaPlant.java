package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

/**
 * Created by Codyr on 10/11/2016.
 */
public class AuloporaPlant extends Plant {
    @Override
    public String getName() { return "Aulopora"; }

    @Override
    public Block getBlock() {
        return BlockHandler.AULOPORA;
    }

    @Override
    public int getHealAmount() {
        return 2000;
    }
}
