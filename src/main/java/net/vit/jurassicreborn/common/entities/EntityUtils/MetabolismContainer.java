package net.vit.jurassicreborn.common.entities.EntityUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

public class MetabolismContainer {
    public static final int MAX_DIGESTION_AMOUNT = 3000;

    private final int maxEnergy;
    private final int maxWater;

    private int energy;
    private int digestingFood;
    private int water;

    private final DinosaurEntity dinosaur;

    public MetabolismContainer(DinosaurEntity dinosaur) {
        this.dinosaur = dinosaur;

        double health = Math.max(1.0, dinosaur.getDinosaur().getAdultHealth()); // avoid zero/neg
        this.maxEnergy = (int) Math.max(1, (health / 10.0) * 24000.0);
        this.maxWater  = (int) Math.max(1, (health / 10.0) * 24000.0);

        this.energy = this.maxEnergy;
        this.water  = this.maxWater;
    }

    public void update() {
        if (!this.dinosaur.isDeadOrDying()
                && !this.dinosaur.isCarcass()
                && this.dinosaur.level.getGameRules().getBoolean(
                net.vit.jurassicreborn.common.util.GameRuleHandler.DINO_METABOLISM)) {

            decreaseEnergy(1);
            decreaseWater(1);

            if (this.dinosaur.isInWaterRainOrBubble()) {
                if (isThirsty()) {
                    this.dinosaur.setAnimation(EntityAnimation.DRINKING.get());
                }
                this.water = this.maxWater;
            }

            if (this.digestingFood > 0) {
                increaseEnergy(25);
                this.digestingFood--;
            }
        }
    }

    public int getWater() { return this.water; }
    public int getEnergy() { return this.energy; }
    public int getDigestingFood() { return this.digestingFood; }
    public int getMaxEnergy() { return this.maxEnergy; }
    public int getMaxWater()  { return this.maxWater; }

    public void setWater(int water) {
        this.water = Math.max(0, Math.min(water, this.maxWater));
    }

    public void setEnergy(int energy) {
        this.energy = Math.max(0, Math.min(energy, this.maxEnergy));
    }

    public void decreaseEnergy(int amount) {
        setEnergy(this.energy - Math.max(0, amount));
        if (isStarving() && this.dinosaur.tickCount % 40 == 0) {
            this.dinosaur.hurt(DamageSource.STARVE, 1.0F);
        }
    }

    public void decreaseWater(int amount) {
        setWater(this.water - Math.max(0, amount));
        if (isDehydrated() && this.dinosaur.tickCount % 40 == 0) {
            this.dinosaur.hurt(DamageSource.STARVE, 1.0F);
        }
    }

    public void setDigestingFoodAmount(int digesting) {
        this.digestingFood = Math.min(Math.max(0, digesting), MAX_DIGESTION_AMOUNT);
    }

    public void increaseEnergy(int amount) { setEnergy(this.energy + Math.max(0, amount)); }
    public void increaseWater(int amount)  { setWater(this.water + Math.max(0, amount)); }

    public void eat(int amount) {
        amount = Math.max(0, amount);
        increaseEnergy(amount / 10);
        setDigestingFoodAmount(this.digestingFood + amount);
    }

    public boolean isStarving()   { return this.energy < 200 && this.digestingFood <= 50; }
    public boolean isDehydrated() { return this.water  < 50; }

    public boolean isHungry() {
        // FIX: do this in double, not int, so digesting food contributes properly.
        double energyRatio = (double) this.energy / (double) this.maxEnergy; // 0..1
        // Only count a fraction of digesting food based on current energy (prevents over-feeding loops).
        double effectiveDigesting = this.digestingFood * Math.max(0.1, energyRatio) * 10.0;

        boolean belowTarget = (this.energy + effectiveDigesting) < (this.maxEnergy * 0.80);
        boolean lowAbsolute = this.energy < 100;

        // ensure we don't overfill digestion buffer
        boolean roomToDigest = (this.digestingFood + 500) < MAX_DIGESTION_AMOUNT;

        return (belowTarget || lowAbsolute) && roomToDigest;
    }

    public boolean isThirsty() {
        return this.water < (this.maxWater * 0.5);
    }

    public void readFromNBT(CompoundTag nbt) {
        setWater(nbt.getInt("Water"));
        setEnergy(nbt.getInt("Energy"));
        setDigestingFoodAmount(nbt.getInt("DigestingFood"));
    }

    public void writeToNBT(CompoundTag nbt) {
        nbt.putInt("Water", this.water);
        nbt.putInt("Energy", this.energy);
        nbt.putInt("DigestingFood", this.digestingFood);
    }
}
