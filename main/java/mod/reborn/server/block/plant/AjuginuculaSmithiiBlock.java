package mod.reborn.server.block.plant;

import mod.reborn.server.item.ItemHandler;
import net.minecraft.item.Item;

public class AjuginuculaSmithiiBlock extends RBBlockCrops8 {
    public AjuginuculaSmithiiBlock() {
        this.seedDropMin = 1;
        this.seedDropMax = 4;
        this.cropDropMin = 2;
        this.cropDropMax = 5;
    }

    @Override
    protected Item getSeed() {
        return ItemHandler.AJUGINUCULA_SMITHII_SEEDS;
    }

    @Override
    protected Item getCrop() {
        return ItemHandler.AJUGINUCULA_SMITHII_LEAVES;
    }
}
