package net.vit.jurassicreborn.common.items.genetics;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.levelgen.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.genetics.PlantDNA;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.plants.Plant;
import net.vit.jurassicreborn.common.plants.PlantHandler;
import net.vit.jurassicreborn.common.util.api.SequencableItem;

import java.util.List;
import java.util.Random;

public class PlantSoftTissueItem extends Item implements SequencableItem {
    private final PlantDNA defaultDNA;

    public PlantSoftTissueItem(Properties properties, Plant plant) {
        super(properties);
        this.defaultDNA = new PlantDNA(plant.getId(), 0);
    }

    @Override
    public Component getName(ItemStack stack) {
        String plantName = PlantHandler.getPlantById(defaultDNA.getPlant()).getName();
        return new TranslatableComponent("item.jurassicreborn.plant_soft_tissue", plantName);
    }

    @Override
    public List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem) {
        List<Pair<Float, ItemStack>> list = Lists.newArrayList();
        CompoundTag nbt = new CompoundTag();

        PlantDNA dna = new PlantDNA(defaultDNA.getPlant(), -1);
        dna.writeToNBT(nbt);

        ItemStack output = new ItemStack(ModItems.STORAGE_DISC.get(), 1);
        output.setTag(nbt);
        StorageDiscItem.applyCustomModelData(output);

        list.add(Pair.of(100F, output));
        return list;
    }

    @Override
    public boolean isSequencable(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getSequenceOutput(ItemStack stack, Random random) {
        CompoundTag nbt = stack.getTag();

        if (nbt == null || !nbt.contains("DNA") || !nbt.getCompound("DNA").contains("Plant")) {
            nbt = new CompoundTag();
            initDnaCompound(random, nbt);
        }

        ItemStack output = new ItemStack(ModItems.STORAGE_DISC.get(), 1);
        output.setTag(nbt);
        StorageDiscItem.applyCustomModelData(output);
        return output;
    }

    private void initDnaCompound(Random random, CompoundTag outTag) {
        int quality = Math.abs(SequencableItem.randomQuality(random)) / 2;
        PlantDNA dna = new PlantDNA(defaultDNA.getPlant(), quality);
        dna.writeToNBT(outTag);
    }
}
