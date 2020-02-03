package mod.reborn.server.block.plant;

import mod.reborn.server.item.ItemHandler;
import net.minecraft.item.Item;

public class WildOnionBlock extends RBBlockCrops7 {
    public WildOnionBlock() {
        this.seedDropMin = 0;
        this.seedDropMax = 0;
        this.cropDropMin = 1;
        this.cropDropMax = 4;
    }

    @Override
    protected Item getSeed() {
        return ItemHandler.WILD_ONION;
    }

    @Override
    protected Item getCrop() {
        return ItemHandler.WILD_ONION;
    }
}
