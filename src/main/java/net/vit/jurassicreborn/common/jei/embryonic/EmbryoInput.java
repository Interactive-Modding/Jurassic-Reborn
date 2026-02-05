package net.vit.jurassicreborn.common.jei.embryonic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.genetics.DinoDNA;
import net.vit.jurassicreborn.common.genetics.GeneticsHelper;
import net.vit.jurassicreborn.common.genetics.PlantDNA;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.plants.Plant;
import net.vit.jurassicreborn.common.plants.PlantHandler;
import java.util.Random;

/** Represents a single recipe input for the embryonic machine. */
public interface EmbryoInput {
    boolean isValid();
    int getMetadata();
    CompoundTag getTag();
    Item getInputItem();
    Item getOutputItem();
    Item getPetriDishItem();

    class DinosaurInput implements EmbryoInput {
        public final Dinosaur dinosaur;
        public DinosaurInput(Dinosaur dinosaur) { this.dinosaur = dinosaur; }

        @Override public boolean isValid() { return this.dinosaur.shouldRegister(); }
        @Override public int getMetadata() { return DinosaurHandler.getId(this.dinosaur); }
        @Override public CompoundTag getTag() {
            DinoDNA dna = new DinoDNA(this.dinosaur, 100, GeneticsHelper.randomGenetics(new Random()));
            CompoundTag tag = new CompoundTag();
            dna.writeToNBT(tag);
            return tag;
        }
        @Override public Item getInputItem() {
            net.minecraftforge.registries.RegistryObject<? extends Item> regObj = ModItems.DINOSAUR_DNA.get(this.dinosaur);
            return regObj != null ? regObj.get() : net.minecraft.world.item.Items.AIR;
        }
        @Override public Item getOutputItem() {
            net.minecraftforge.registries.RegistryObject<? extends Item> regObj = ModItems.SYRINGES.get(this.dinosaur);
            return regObj != null ? regObj.get() : net.minecraft.world.item.Items.AIR;
        }
        @Override public Item getPetriDishItem() { return ModItems.PETRI_DISH.get(); }
    }

    class PlantInput implements EmbryoInput {
        public final Plant plant;
        public PlantInput(Plant plant) { this.plant = plant; }

        @Override public boolean isValid() { return this.plant.shouldRegister(); }
        @Override public int getMetadata() { return PlantHandler.getPlants().indexOf(this.plant); }
        @Override public CompoundTag getTag() {
            PlantDNA dna = new PlantDNA(PlantHandler.getPlantId(this.plant), 100); // getPlantId returns ResourceLocation
            CompoundTag tag = new CompoundTag();
            dna.writeToNBT(tag);
            return tag;
        }
        @Override public Item getInputItem() {
            net.minecraftforge.registries.RegistryObject<? extends Item> regObj = ModItems.PLANT_DNAS.get(this.plant);
            return regObj != null ? regObj.get() : net.minecraft.world.item.Items.AIR;
        }
        @Override public Item getOutputItem() { return ModItems.PLANT_CALLUS.get(); }
        @Override public Item getPetriDishItem() { return ModItems.PLANT_CELLS_PETRI_DISH.get(); }
    }
}
