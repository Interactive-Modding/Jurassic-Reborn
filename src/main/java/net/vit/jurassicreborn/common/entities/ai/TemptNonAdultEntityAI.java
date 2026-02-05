package net.vit.jurassicreborn.common.entities.ai;

import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.items.Food.FoodHelper;

public class TemptNonAdultEntityAI extends TemptGoal {
    private final DinosaurEntity dinosaur;

    public TemptNonAdultEntityAI(DinosaurEntity dinosaur, double speed) {
        // Uses FoodHelper to build an Ingredient of all foods this dinosaur can eat.
        super(dinosaur, speed, FoodHelper.getEdibleFoodIngredient(dinosaur, dinosaur.getDinosaur().getDiet()), false);
        this.dinosaur = dinosaur;
    }

    @Override
    public boolean canUse() {
        // Only if the base Tempt conditions pass, the dino isn't busy, and it's <50% grown.
        return super.canUse() && !dinosaur.isBusy() && dinosaur.getAgePercentage() < 50;
    }

    @Override
    public boolean canContinueToUse() {
        // Keep tempting only while still valid (not busy, still juvenile).
        return super.canContinueToUse() && !dinosaur.isBusy() && dinosaur.getAgePercentage() < 50;
    }
}
