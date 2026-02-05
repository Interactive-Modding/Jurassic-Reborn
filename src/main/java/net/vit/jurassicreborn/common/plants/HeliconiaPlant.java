package net.vit.jurassicreborn.common.plants;

import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

/**
 * Created by Codyr on 22/01/2017.
 */
public class HeliconiaPlant extends Plant {
    public HeliconiaPlant(String name, Supplier<? extends Block> block, int healAmount) {
        super(name, block, healAmount);
    }

    @Override
    public boolean isPrehistoric() {
        return false;
    }
}
