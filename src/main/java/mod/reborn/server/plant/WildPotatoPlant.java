package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

public class WildPotatoPlant extends Plant {
    @Override
    public String getName() {
        return "Wild Potato";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.WILD_POTATO_PLANT;
    }

    @Override
    public int getHealAmount() {
        return 3000;
    }
}
