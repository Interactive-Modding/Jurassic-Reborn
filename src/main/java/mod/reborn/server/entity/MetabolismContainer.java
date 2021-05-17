package mod.reborn.server.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;

public class MetabolismContainer {
    public static final int MAX_DIGESTION_AMOUNT = 3000;

    private final int maxEnergy;
    private final int maxWater;

    private int energy;
    private int digestingFood;
    private int water;

    private DinosaurEntity dinosaur;

    public MetabolismContainer(DinosaurEntity dinosaur) {
        this.dinosaur = dinosaur;

        double health = dinosaur.getDinosaur().getAdultHealth();

        this.maxEnergy = (int) (health / 10.0 * 24000);
        this.maxWater = (int) (health / 10.0 * 24000);

        this.energy = this.maxEnergy;
        this.water = this.maxWater;
    }

    public void update() {
        if (!this.dinosaur.getShouldBeDead() && !this.dinosaur.isCarcass() && GameRuleHandler.DINO_METABOLISM.getBoolean(this.dinosaur.world)) {
            this.decreaseEnergy(1);
            this.decreaseWater(1);

            if (this.dinosaur.isWet()) {
                if (this.isThirsty()) {
                    this.dinosaur.setAnimation(EntityAnimation.DRINKING.get());
                }
                this.water = this.maxWater;
            }

            if (this.digestingFood > 0) {
                this.increaseEnergy(25);
                this.digestingFood--;
            }
        }
    }

    public int getWater() {
        return this.water;
    }

    public void setWater(int water) {
        this.water = Math.min(water, this.maxWater);
    }

    public int getEnergy() {
        return this.energy;
    }

    public void setEnergy(int energy) {
        this.energy = Math.min(energy, this.maxEnergy);
    }

    public int getDigestingFood() {
        return this.digestingFood;
    }

    public void decreaseEnergy(int amount) {
        this.energy -= amount;
        this.energy = Math.max(0, this.energy);

        if (this.isStarving() && this.dinosaur.ticksExisted % 40 == 0) {
            this.dinosaur.attackEntityFrom(DamageSource.STARVE, 1.0F);
        }
    }

    public void decreaseWater(int amount) {
        this.water -= amount;
        this.water = Math.max(0, this.water);

        if (this.isDehydrated() && this.dinosaur.ticksExisted % 40 == 0) {
            this.dinosaur.attackEntityFrom(DamageSource.STARVE, 1.0F);
        }
    }

    public void setDigestingFoodAmount(int digesting) {
        this.digestingFood = Math.min(digesting, MAX_DIGESTION_AMOUNT);
    }

    public void readFromNBT(CompoundNBT nbt) {
        this.water = nbt.getInt("Water");
        this.energy = nbt.getInt("Energy");
        this.digestingFood = nbt.getInt("DigestingFood");
    }

    public void writeToNBT(CompoundNBT nbt) {
        nbt.putInt("Water", this.water);
        nbt.putInt("Energy", this.energy);
        nbt.putInt("DigestingFood", this.digestingFood);
    }

    public int getMaxEnergy() {
        return this.maxEnergy;
    }

    public int getMaxWater() {
        return this.maxWater;
    }

    public void increaseEnergy(int amount) {
        this.setEnergy(this.energy + amount);
    }

    public void eat(int amount) {
        this.increaseEnergy(amount / 10);
        this.setDigestingFoodAmount(this.digestingFood + amount);
    }

    public void increaseWater(int amount) {
        this.setWater(this.water + amount);
    }

    public boolean isStarving() {
        return (double) this.energy < 200 && this.digestingFood <= 50;
    }

    public boolean isDehydrated() {
        return (double) this.water < 50;
    }

    public boolean isHungry() {
        return (this.energy + (this.digestingFood * (this.energy / this.maxEnergy) * 10) < this.maxEnergy * 0.8 || this.energy < 100) && this.digestingFood + 500 < MAX_DIGESTION_AMOUNT;
    }

    public boolean isThirsty() {
        return this.water < this.maxWater * 0.5;
    }
}
