package net.vit.jurassicreborn.common.plants;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.ancientplants.DoublePlantBlock;
import net.vit.jurassicreborn.common.items.Food.FoodHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class EncephalartosPlant extends Plant {
    public EncephalartosPlant(String name, Supplier<? extends Block> block, int healAmount) {
        super(name, block, healAmount);
    }

    @Override
    public FoodHelper.FoodEffect[] getEffects() {
        return new FoodHelper.FoodEffect[] { new FoodHelper.FoodEffect(MobEffects.POISON, 35) };
    }
}