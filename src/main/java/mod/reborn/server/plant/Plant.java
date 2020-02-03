package mod.reborn.server.plant;

import mod.reborn.server.food.FoodHelper;
import net.minecraft.block.Block;

public abstract class Plant implements Comparable<Plant> {
    public abstract String getName();

    public abstract Block getBlock();

    public boolean shouldRegister() {
        return true;
    }

    @Override
    public int compareTo(Plant plant) {
        return this.getName().compareTo(plant.getName());
    }

    public abstract int getHealAmount();

    public FoodHelper.FoodEffect[] getEffects() {
        return new FoodHelper.FoodEffect[0];
    }

    public boolean isPrehistoric() {
        return true;
    }

    public boolean isTree() {
        return false;
    }
}
