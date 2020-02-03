package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.food.FoodHelper;
import net.minecraft.block.Block;
import net.minecraft.init.PotionTypes;

public class EncephalartosPlant extends Plant {
    @Override
    public String getName() {
        return "Encephalartos";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.ENCEPHALARTOS;
    }

    @Override
    public int getHealAmount() {
        return 4000;
    }

    @Override
    public FoodHelper.FoodEffect[] getEffects() {
        return new FoodHelper.FoodEffect[] { new FoodHelper.FoodEffect(PotionTypes.POISON.getEffects().get(0), 35) };
    }
}
