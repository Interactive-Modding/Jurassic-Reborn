package net.vit.jurassicreborn.common.items.genetics;

import net.vit.jurassicreborn.common.genetics.PlantDNA;
import net.vit.jurassicreborn.common.plants.Plant;
import net.vit.jurassicreborn.common.plants.PlantHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import net.vit.jurassicreborn.common.util.ItemStackNbtUtil;

public class PlantDNAItem extends DNAContainerItem {
    public final Plant plant;

    public PlantDNAItem(Plant plant, Properties pProperties) {
        super(pProperties);
        this.plant = plant;
    }

    @Override
    public Component getName(ItemStack stack) {
        // If the stack doesn't have a DNA tag, use the default plant from this item.
        String plantName = PlantHandler.getPlantById(this.plant.getId()).getName();
        return Component.translatable("item.jurassicreborn.plant_dna", plantName);
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        CompoundTag tag = new CompoundTag();
        // Create a default PlantDNA with a valid plant identifier and a default quality (e.g., 100)
        PlantDNA dna = new PlantDNA(plant.getId(), 100);
        dna.writeToNBT(tag);
        ItemStackNbtUtil.setTag(stack, tag);
        return stack;
    }

}
