package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

/**
 * Created by Codyr on 11/11/2016.
 */
public class LithostrotionPlant extends Plant {
    @Override
    public String getName() { return "Lithostrotion"; }

    @Override
    public Block getBlock() {
        return BlockHandler.LITHOSTROTION;
    }

    @Override
    public int getHealAmount() {
        return 2000;
    }
}

