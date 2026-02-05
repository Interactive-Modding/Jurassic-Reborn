package net.vit.jurassicreborn.common.entities.ai.metabolism;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.items.Food.FoodHelper;
import net.vit.jurassicreborn.common.util.GameRuleHandler;

import java.util.EnumSet;
import java.util.List;

public class EatFoodItemEntityAI extends Goal {
    private static final double SEARCH_RADIUS = 16.0D;
    private static final double SPEED = 1.0D;

    protected final DinosaurEntity dinosaur;
    protected ItemEntity target;

    public EatFoodItemEntityAI(DinosaurEntity dinosaur) {
        this.dinosaur = dinosaur;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.dinosaur == null || !this.dinosaur.isAlive() || this.dinosaur.isCarcass()) return false;

        // NEW: Never try to eat if we’re in combat
        if (this.dinosaur.getTarget() != null) return false;

        Level level = this.dinosaur.level();
        if (level == null || !level.getGameRules().getRule(GameRuleHandler.DINO_METABOLISM).get()) return false;
        if (!this.dinosaur.getMetabolism().isHungry()) return false;

        // Find nearest edible item
        AABB box = this.dinosaur.getBoundingBox().inflate(SEARCH_RADIUS, SEARCH_RADIUS, SEARCH_RADIUS);
        List<ItemEntity> items = level.getEntitiesOfClass(ItemEntity.class, box);

        double closestDist = Double.MAX_VALUE;
        ItemEntity closest = null;

        for (ItemEntity entity : items) {
            if (!entity.isAlive()) continue;

            ItemStack stack = entity.getItem();
            Item item = stack.getItem();

            if (FoodHelper.isEdible(this.dinosaur, this.dinosaur.getDinosaur().getDiet(), item)) {
                double distSqr = this.dinosaur.distanceToSqr(entity);
                if (distSqr < closestDist) {
                    closestDist = distSqr;
                    closest = entity;
                }
            }
        }

        if (closest != null) {
            this.target = closest;
            return true;
        }
        return false;
    }

    @Override
    public void start() {
        if (this.target != null) {
            this.dinosaur.getNavigation().moveTo(this.target, SPEED);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.dinosaur != null
                && this.dinosaur.isAlive()
                && this.dinosaur.getTarget() == null // NEW: stop if we acquire a combat target mid-run
                && this.target != null
                && this.target.isAlive()
                && !this.dinosaur.getNavigation().isDone();
    }

    @Override
    public void stop() {
        this.dinosaur.getNavigation().stop();
        this.target = null;
    }

    @Override
    public void tick() {
        if (this.target != null && this.target.isAlive() && this.dinosaur.getTarget() == null) {
            this.dinosaur.getNavigation().moveTo(this.target, SPEED);
        }
    }
}
