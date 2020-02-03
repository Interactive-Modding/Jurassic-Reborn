package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.food.FoodHelper;
import net.minecraft.block.Block;
import net.minecraft.init.PotionTypes;

public class DictyophyllumPlant extends Plant {
    @Override
    public String getName() {
        return "Dictyophyllum";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.DICTYOPHYLLUM;
    }

    @Override
    public int getHealAmount() {
        return 2000;
    }

    @Override
    public FoodHelper.FoodEffect[] getEffects() {
        return new FoodHelper.FoodEffect[] { new FoodHelper.FoodEffect(PotionTypes.POISON.getEffects().get(0), 15) };
    }
}
