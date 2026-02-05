package net.vit.jurassicreborn.common.plants;

import net.minecraft.world.level.block.SaplingBlock;
import net.vit.jurassicreborn.common.items.Food.FoodHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;

import java.util.Locale;
import java.util.function.Supplier;

public class Plant implements Comparable<Plant> {

    private final String name;
    private final Supplier<? extends Block> block;
    private final boolean shouldRegister;
    private final int healAmount;

    public Plant(String name,Supplier<? extends Block> block,int healAmount) {
        this(name,block,true,healAmount);
    }

    public Plant(String name,Supplier<? extends Block> block,boolean shouldRegister,int healAmount) {
        this.name = name;
        this.block = block;
        this.shouldRegister = shouldRegister;
        this.healAmount = healAmount;
    }

    public final String getName() {
        return name;
    }

    public Block getBlock() {
        return block.get();
    }

    public ResourceLocation getId(){
        return PlantHandler.getPlantId(this);
    }


    public final boolean shouldRegister() {
        return shouldRegister;
    }

    @Override
    public int compareTo(Plant plant) {
        return this.getName().compareTo(plant.getName());
    }

    public final int getHealAmount() {
        return healAmount;
    }

    public FoodHelper.FoodEffect[] getEffects() {
        return new FoodHelper.FoodEffect[0];
    }

    public boolean isPrehistoric() {
        return true;
    }

    public final boolean isTree() {
        return getBlock() instanceof SaplingBlock;
    }

    public String getFormattedName(){
        return this.getName().toLowerCase(Locale.ROOT).replace(' ', '_');
    }


    /**
     * This is the default implimentation of Plant -- The instance of this in {@link PlantHandler}({@link PlantHandler#EMPTY}
     * will be returned to prevent nulls from being returned in refrence to plants.
     *
     * @see Dinosaur#EMPTY
     * @see Dinosaur.EmptyDinosaur
     *
      */
}