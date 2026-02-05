package net.vit.jurassicreborn.common.plants;

import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.items.Food.FoodHelper;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class WestIndianLilacPlant extends Plant {
    public WestIndianLilacPlant(String name, Supplier<? extends Block> block, int healAmount) {
        super(name, block, healAmount);
    }

    @Override
    public FoodHelper.FoodEffect[] getEffects() {
        return new FoodHelper.FoodEffect[] { new FoodHelper.FoodEffect(MobEffects.POISON, 100) };
    }
    @Override
    public Block getBlock() {
        return ModBlocks.WEST_INDIAN_LILAC.get();
    }
    @Override
    public boolean isPrehistoric() {
        return false;
    }

}
