package mod.reborn.server.entity.ai;

import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.food.FoodHelper;
import mod.reborn.server.food.FoodType;
import net.minecraft.entity.ai.EntityAITempt;

public class TemptNonAdultEntityAI extends EntityAITempt {
    private DinosaurEntity dinosaur;

    public TemptNonAdultEntityAI(DinosaurEntity dinosaur, double speed) {
        super(dinosaur, speed, !dinosaur.getDinosaur().getDiet().canEat(dinosaur, FoodType.MEAT), FoodHelper.getEdibleFoodItems(dinosaur, dinosaur.getDinosaur().getDiet()));
        this.dinosaur = dinosaur;
        this.setMutexBits(Mutex.METABOLISM | Mutex.MOVEMENT);
    }

    @Override
    public boolean shouldExecute() {
        return super.shouldExecute() && !this.dinosaur.isBusy() && this.dinosaur.getAgePercentage() < 50;
    }
}
