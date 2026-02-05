package net.vit.jurassicreborn.common.entities.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.AABB;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.EntityUtils.FoodType;

import java.util.EnumSet;
import java.util.List;

/**
 * Finds the nearest carcass (a DinosaurEntity with {@code isCarcass()==true}) when hungry
 * and sets it as the attack target so the dinosaur can feed from it.
 */
public class TargetCarcassAI extends Goal {

    private final DinosaurEntity dino;
    private DinosaurEntity carcass;

    public TargetCarcassAI(DinosaurEntity dino) {
        this.dino = dino;
        // We manipulate both movement and target so carnivores actually eat the carcass.
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        // Basic state checks
        if (dino.isCarcass() || dino.isSleeping() || !dino.isAlive()) return false;
        if (!dino.getMetabolism().isHungry())                         return false;
        if (dino.isBusy())                                            return false;
        // Throttle a bit
        if (dino.getRandom().nextInt(10) != 0)                        return false;

        // Only meat/fish eaters should bother
        var diet = dino.getDinosaur().getDiet();
        boolean carnivore = diet.canEat(dino, FoodType.MEAT) || diet.canEat(dino, FoodType.FISH);
        if (!carnivore) return false;

        // Search nearby for the closest carcass
        AABB search = dino.getBoundingBox().inflate(16.0);
        List<DinosaurEntity> inRange = dino.level.getEntitiesOfClass(DinosaurEntity.class, search);

        double bestDistSq = Double.MAX_VALUE;
        DinosaurEntity best = null;

        for (DinosaurEntity other : inRange) {
            if (other == dino) continue;
            if (!other.isCarcass()) continue;

            double distSq = dino.distanceToSqr(other);
            if (distSq < bestDistSq) {
                bestDistSq = distSq;
                best = other;
            }
        }

        carcass = best;
        return carcass != null;
    }

    @Override
    public void start() {
        // Hand over the carcass to the combat AI so the dinosaur will attack/eat it.
        dino.setTarget(carcass);
        dino.getNavigation().moveTo(carcass, 1.2F);
    }

    @Override
    public void stop() {
        carcass = null;
    }

    @Override
    public boolean canContinueToUse() {
        // one-shot goal; targeting happens in start()
        return false;
    }
}
