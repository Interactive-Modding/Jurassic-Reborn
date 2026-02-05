package net.vit.jurassicreborn.common.items.Fossils;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import net.vit.jurassicreborn.common.plants.Plant;
import net.vit.jurassicreborn.common.plants.PlantHandler;
import net.vit.jurassicreborn.common.util.api.GrindableItem;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class PlantFossilItem extends Item implements GrindableItem {

    public PlantFossilItem() {
        super(new Properties());
    }

    @Override
    public boolean isGrindable(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getGroundItem(ItemStack stack, Random random) {
        CompoundTag tag = stack.getTag();
        int outputType = random.nextInt(4);

        if (outputType == 3) {
            List<Plant> prehistoricPlants = PlantHandler.getPrehistoricPlants();
            Plant plant = prehistoricPlants.get(random.nextInt(prehistoricPlants.size()));

            String id = plant.getFormattedName().toLowerCase(Locale.ROOT).replaceAll(" ", "_");
            ResourceLocation tissueId = new ResourceLocation("jurassicreborn", "soft_tissue/plants/soft_tissue_" + id);

            Item item = ForgeRegistries.ITEMS.getValue(tissueId);
            if (item == null || item == Items.AIR) {
                return new ItemStack(Items.FLINT);
            }

            ItemStack output = new ItemStack(item);
            output.setTag(tag);
            return output;
        } else if (outputType < 2) {
            return new ItemStack(Items.BONE_MEAL);
        }

        return new ItemStack(Items.FLINT);
    }


    @Override
    public List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem) {
        List<Pair<Float, ItemStack>> list = Lists.newArrayList();
        List<Plant> prehistoricPlants = PlantHandler.getPrehistoricPlants();
        CompoundTag tag = inputItem.getTag();
        float single = 100F / 4F;
        float plantSingle = single / prehistoricPlants.size();

        for (Plant plant : prehistoricPlants) {
            String id = plant.getFormattedName().toLowerCase(Locale.ROOT).replaceAll(" ", "_");
            ResourceLocation tissueId = new ResourceLocation("jurassicreborn", "soft_tissue/plants/soft_tissue_" + id);
            Item item = ForgeRegistries.ITEMS.getValue(tissueId);

            if (item == null || item == Items.AIR) continue;

            ItemStack output = new ItemStack(item);
            output.setTag(tag);
            list.add(Pair.of(plantSingle, output));
        }

        list.add(Pair.of(50f, new ItemStack(Items.BONE_MEAL)));
        list.add(Pair.of(single, new ItemStack(Items.FLINT)));

        return list;
    }
}
