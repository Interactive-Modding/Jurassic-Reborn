package mod.reborn.server.block.plant;

import mod.reborn.server.item.ItemHandler;
import net.minecraft.item.Item;

public class WildPotatoBlock extends RBBlockCrops6 {
    public WildPotatoBlock() {
        this.seedDropMin = 1;
        this.seedDropMax = 3;
        this.cropDropMin = 1;
        this.cropDropMax = 2;
    }

    @Override
    protected Item getSeed() {
        return ItemHandler.WILD_POTATO_SEEDS;
    }

    @Override
    protected Item getCrop() {
        return ItemHandler.WILD_POTATO;
    }
}
