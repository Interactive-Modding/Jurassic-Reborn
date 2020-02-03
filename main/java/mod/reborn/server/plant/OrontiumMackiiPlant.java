package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.food.FoodHelper;
import net.minecraft.block.Block;
import net.minecraft.init.PotionTypes;

public class OrontiumMackiiPlant extends Plant {
    @Override
    public String getName() {
        return "Orontium Mackii";
    }

    @Override
    public Block getBlock() {
        return BlockHandler.ORONTIUM_MACKII;
    }

    @Override
    public int getHealAmount() {
        return 1500;
    }

    @Override
    public FoodHelper.FoodEffect[] getEffects() {
        return new FoodHelper.FoodEffect[] { new FoodHelper.FoodEffect(PotionTypes.WEAKNESS.getEffects().get(0), 50) };
    }
}
