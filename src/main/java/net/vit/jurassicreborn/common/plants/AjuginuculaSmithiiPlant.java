package net.vit.jurassicreborn.common.plants;

import net.vit.jurassicreborn.common.items.Food.FoodHelper;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class AjuginuculaSmithiiPlant extends Plant {


    public AjuginuculaSmithiiPlant(String name, Supplier<? extends Block> block, int healAmount) {
        super(name, block, healAmount);
    }

    @Override
    public FoodHelper.FoodEffect[] getEffects() {
        return new FoodHelper.FoodEffect[] {
                new FoodHelper.FoodEffect(MobEffects.MOVEMENT_SPEED, 100)
        };
    }
}
