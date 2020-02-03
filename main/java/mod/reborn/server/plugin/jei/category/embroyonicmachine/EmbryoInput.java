package mod.reborn.server.plugin.jei.category.embroyonicmachine;

import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.plant.Plant;
import mod.reborn.server.plant.PlantHandler;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.genetics.DinoDNA;
import mod.reborn.server.genetics.GeneticsHelper;
import mod.reborn.server.genetics.PlantDNA;

import java.util.Random;

public interface EmbryoInput {
    boolean isValid();

    int getMetadata();

    NBTTagCompound getTag();

    Item getInputItem();

    Item getOutputItem();

    Item getPetriDishItem();

    class DinosaurInput implements EmbryoInput {
        public final Dinosaur dinosaur;

        public DinosaurInput(Dinosaur dinosaur) {
            this.dinosaur = dinosaur;
        }

        @Override
        public boolean isValid() {
            return this.dinosaur.shouldRegister();
        }

        @Override
        public int getMetadata() {
            return EntityHandler.getDinosaurId(this.dinosaur);
        }

        @Override
        public NBTTagCompound getTag() {
            DinoDNA dna = new DinoDNA(this.dinosaur, 100, GeneticsHelper.randomGenetics(new Random()));
            NBTTagCompound tag = new NBTTagCompound();
            dna.writeToNBT(tag);
            return tag;
        }

        @Override
        public Item getInputItem() {
            return ItemHandler.DNA;
        }

        @Override
        public Item getOutputItem() {
            return ItemHandler.SYRINGE;
        }

        @Override
        public Item getPetriDishItem() {
            return ItemHandler.PETRI_DISH;
        }
    }

    class PlantInput implements EmbryoInput {
        public final Plant plant;

        public PlantInput(Plant plant) {
            this.plant = plant;
        }

        @Override
        public boolean isValid() {
            return this.plant.shouldRegister();
        }

        @Override
        public int getMetadata() {
            return PlantHandler.getPlantId(this.plant);
        }

        @Override
        public NBTTagCompound getTag() {
            PlantDNA dna = new PlantDNA(this.getMetadata(), 100);
            NBTTagCompound tag = new NBTTagCompound();
            dna.writeToNBT(tag);
            return tag;
        }

        @Override
        public Item getInputItem() {
            return ItemHandler.PLANT_DNA;
        }

        @Override
        public Item getOutputItem() {
            return ItemHandler.PLANT_CALLUS;
        }

        @Override
        public Item getPetriDishItem() {
            return ItemHandler.PLANT_CELLS_PETRI_DISH;
        }
    }
}
