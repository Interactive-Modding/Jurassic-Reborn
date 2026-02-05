package net.vit.jurassicreborn.common.genetics;

import net.vit.jurassicreborn.common.items.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.plants.Plant;
import net.vit.jurassicreborn.common.plants.PlantHandler;

import java.util.List;

public class PlantDNAStorageType implements StorageType {
    private PlantDNA dna;

    @Override
    public ItemStack createItem() {
        if (dna == null) {
            return ItemStack.EMPTY;
        }

        // Retrieve the actual Plant instance using the plant ID stored in the DNA.
        Plant plant = PlantHandler.getPlantById(dna.getPlant());
        net.minecraftforge.registries.RegistryObject<? extends net.minecraft.world.item.Item> regObj =
                ModItems.PLANT_DNAS.get(plant);
        if (regObj == null) {
            return ItemStack.EMPTY;
        }

        ItemStack output = new ItemStack(regObj.get(), 1);
        CompoundTag compound = new CompoundTag();
        this.dna.writeToNBT(compound);
        output.setTag(compound);
        return output;
    }


    @Override
    public void saveAdditional(CompoundTag nbt) {
        this.dna.writeToNBT(nbt);
//        nbt.putString("DNA", dna.getPlant().toString()); what????
    }

    @Override
    public DNA load(CompoundTag nbt) {
        this.dna = PlantDNA.readFromNBT(nbt);
        return this.dna;
    }

    @Override
    public void addInformation(ItemStack stack, List<Component> tooltip) {
        this.dna.addInformation(stack, tooltip);
    }

//    @Override
//    public int getMetadata() {
//        return this.dna.get();
//    }
}