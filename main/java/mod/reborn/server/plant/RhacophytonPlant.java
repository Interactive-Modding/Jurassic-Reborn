package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

/**
 * Created by Codyr on 29/10/2016.
 */
public class RhacophytonPlant extends Plant {
    @Override
    public String getName() {
        return "Rhacophyton";
    }

    @Override
    public Block getBlock() { return BlockHandler.RHACOPHYTON; }
    

    @Override
    public int getHealAmount() {
        return 4000;
    }
}
