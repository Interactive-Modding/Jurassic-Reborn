package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

/**
 * Created by Codyr on 10/11/2016.
 */
public class CladochonusPlant extends Plant {
    @Override
    public String getName() { return "Cladochonus"; }

    @Override
    public Block getBlock() {
        return BlockHandler.CLADOCHONUS;
    }

    @Override
    public int getHealAmount() {
        return 2000;
    }
}

