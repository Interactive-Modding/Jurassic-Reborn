package net.vit.jurassicreborn.common.jei.dnaextractor;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.RandomSource;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.genetics.DinoDNA;
import net.vit.jurassicreborn.common.genetics.GeneticsHelper;
import net.vit.jurassicreborn.common.genetics.PlantDNA;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.genetics.StorageDiscItem;
import net.vit.jurassicreborn.common.plants.Plant;
import net.vit.jurassicreborn.common.plants.PlantHandler;

import java.util.ArrayList;
import java.util.List;
import net.vit.jurassicreborn.common.util.ItemStackNbtUtil;

/** JEI recipe extension for the DNA Extractor. */
public class DNAExtractorRecipeExtension implements IRecipeCategoryExtension {
    private final ItemStack input;
    private final List<ItemStack> outputs = new ArrayList<>();

    public DNAExtractorRecipeExtension(ItemStack input) {
        this.input = input;
        computeOutputs();
    }

    /** Build the preset list of extractor recipes used in JEI. */
    public static List<DNAExtractorRecipeExtension> createRecipes() {
        List<DNAExtractorRecipeExtension> list = new ArrayList<>();
        list.add(new DNAExtractorRecipeExtension(new ItemStack(ModItems.MOSQUITO_AMBER.get())));
        list.add(new DNAExtractorRecipeExtension(new ItemStack(ModItems.SEA_LAMPREY.get())));
        list.add(new DNAExtractorRecipeExtension(new ItemStack(ModItems.FROZEN_LEECH_ITEM.get())));
        list.add(new DNAExtractorRecipeExtension(new ItemStack(ModItems.APHID_AMBER.get())));
        for (var entry : ModItems.MEATS.entrySet()) {
            list.add(new DNAExtractorRecipeExtension(new ItemStack(entry.getValue().get())));
        }
        return list;
    }

    private void computeOutputs() {
        outputs.clear();
        RandomSource rand = RandomSource.create();
        if (input.is(ModItems.MOSQUITO_AMBER.get()) || input.is(ModItems.SEA_LAMPREY.get())) {
            if (input.getDamageValue() == 0) {
                List<Dinosaur> dinos = input.is(ModItems.MOSQUITO_AMBER.get())
                        ? DinosaurHandler.getDinosaursFromAmber()
                        : DinosaurHandler.getMarineCreatures();
                for (Dinosaur dino : dinos) {
                    ItemStack disc = ModItems.STORAGE_DISC.get().getDefaultInstance();
                    int quality = 50 + rand.nextInt(50);
                    DinoDNA dna = new DinoDNA(dino, quality, GeneticsHelper.randomGenetics(rand));
                    CompoundTag nbt = new CompoundTag();
                    dna.writeToNBT(nbt);
                    ItemStackNbtUtil.setTag(disc, nbt);
                    StorageDiscItem.applyCustomModelData(disc);
                    outputs.add(disc);
                }
            } else if (input.getDamageValue() == 1) {
                List<Plant> plants = PlantHandler.getPrehistoricPlantsAndTrees();
                for (Plant plant : plants) {
                    ItemStack disc = ModItems.STORAGE_DISC.get().getDefaultInstance();
                    int quality = 50 + rand.nextInt(50);
                    PlantDNA dna = new PlantDNA(PlantHandler.getPlantId(plant), quality);
                    CompoundTag tag = new CompoundTag();
                    dna.writeToNBT(tag);
                    ItemStackNbtUtil.setTag(disc, tag);
                    StorageDiscItem.applyCustomModelData(disc);
                    outputs.add(disc);
                }
            }
        } else if (input.is(ModItems.MOSQUITO_AMBER.get()) || input.is(ModItems.FROZEN_LEECH_ITEM.get())) {
            if (input.getDamageValue() == 0) {
                List<Dinosaur> dinos = input.is(ModItems.MOSQUITO_AMBER.get())
                        ? DinosaurHandler.getDinosaursFromAmber()
                        : DinosaurHandler.getMammalCreatures();
                for (Dinosaur dino : dinos) {
                    ItemStack disc = ModItems.STORAGE_DISC.get().getDefaultInstance();
                    int quality = 50 + rand.nextInt(50);
                    DinoDNA dna = new DinoDNA(dino, quality, GeneticsHelper.randomGenetics(rand));
                    CompoundTag tag = new CompoundTag();
                    dna.writeToNBT(tag);
                    ItemStackNbtUtil.setTag(disc, tag);
                    StorageDiscItem.applyCustomModelData(disc);
                    outputs.add(disc);
                }
            }
        } else if (input.getItem() instanceof net.vit.jurassicreborn.common.items.Food.DinosaurMeatItem meat) {
            Dinosaur dino = meat.getDinosaur();
            ItemStack disc = ModItems.STORAGE_DISC.get().getDefaultInstance();
            DinoDNA dna = new DinoDNA(dino, 100, GeneticsHelper.randomGenetics(rand));
            CompoundTag tag = new CompoundTag();
            dna.writeToNBT(tag);
            ItemStackNbtUtil.setTag(disc, tag);
            StorageDiscItem.applyCustomModelData(disc);
            outputs.add(disc);
        }
    }

    public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
        // match the actual menu slot positions
        builder.addSlot(RecipeIngredientRole.INPUT, 6, 4).addItemStack(input);
        builder.addSlot(RecipeIngredientRole.INPUT, 6, 25).addItemStack(ModItems.STORAGE_DISC.get().getDefaultInstance());

        IRecipeSlotBuilder slot = builder.addSlot(RecipeIngredientRole.OUTPUT, 59, 6);
        if (!outputs.isEmpty()) {
            slot.addItemStacks(outputs);
        }
    }
}
