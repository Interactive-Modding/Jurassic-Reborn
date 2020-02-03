package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

/**
 * Created by Codyr on 30/10/2016.
 */
public class EnallheliaPlant extends Plant {
    @Override
    public String getName() { return "Enallhelia"; }

    @Override
    public Block getBlock() {
        return BlockHandler.ENALLHELIA;
    }

    @Override
    public int getHealAmount() {
        return 2000;
    }
}
