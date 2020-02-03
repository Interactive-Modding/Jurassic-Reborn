package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.food.FoodHelper;
import net.minecraft.block.Block;
import net.minecraft.init.PotionTypes;

public class WestIndianLilacPlant extends Plant {
    @Override
    public String getName() {
        return "West Indian Lilac";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.WEST_INDIAN_LILAC;
    }

    @Override
    public int getHealAmount() {
        return 4000;
    }

    @Override
    public FoodHelper.FoodEffect[] getEffects() {
        return new FoodHelper.FoodEffect[] { new FoodHelper.FoodEffect(PotionTypes.STRONG_POISON.getEffects().get(0), 100) };
    }

    @Override
    public boolean isPrehistoric() {
        return false;
    }
}
