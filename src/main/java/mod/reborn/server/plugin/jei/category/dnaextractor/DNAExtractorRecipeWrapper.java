package mod.reborn.server.plugin.jei.category.dnaextractor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.genetics.DinoDNA;
import mod.reborn.server.genetics.GeneticsHelper;
import mod.reborn.server.genetics.PlantDNA;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.plant.Plant;
import mod.reborn.server.plant.PlantHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class DNAExtractorRecipeWrapper implements IRecipeWrapper{
	
	private final ItemStack inputStack;

    public DNAExtractorRecipeWrapper(ItemStack input) {
        this.inputStack = input;
    }

	@Override
	public void getIngredients(IIngredients ingredients) 
	{
		ingredients.setInputs(ItemStack.class, Lists.newArrayList(this.inputStack, new ItemStack(ItemHandler.STORAGE_DISC)));
		ingredients.setOutputLists(ItemStack.class, this.getAllPossibleOutputs(this.inputStack));
	}
	
	
	private List<List<ItemStack>> getAllPossibleOutputs(ItemStack input)
	{
		Random rand = new Random();
		List<List<ItemStack>> outputs = new ArrayList<List<ItemStack>>();
		outputs.add(new ArrayList<ItemStack>());
		if (input.getItem() == ItemHandler.AMBER || input.getItem() == ItemHandler.SEA_LAMPREY)
		{
            if (input.getItemDamage() == 0)
            {
                List<Dinosaur> possibleDinos = input.getItem() == ItemHandler.AMBER ? EntityHandler.getDinosaursFromAmber() : EntityHandler.getMarineCreatures();


                for(Dinosaur dino : possibleDinos)

                {
                	ItemStack disc = ItemStack.EMPTY;
                	disc = new ItemStack(ItemHandler.STORAGE_DISC);

                    int quality = 50 + (rand.nextInt(50));

                    DinoDNA dna = new DinoDNA(dino, quality, GeneticsHelper.randomGenetics(rand));

                    NBTTagCompound nbt = new NBTTagCompound();
                    dna.writeToNBT(nbt);

                    disc.setTagCompound(nbt);
                    outputs.get(0).add(disc);
                }
            }
            }
            else if (input.getItemDamage() == 1)
            {
                List<Plant> possiblePlants = PlantHandler.getPrehistoricPlantsAndTrees();
                for(Plant plant : possiblePlants)
                {
                	ItemStack disc = ItemStack.EMPTY;
                	int plantId = PlantHandler.getPlantId(plant);

                    disc = new ItemStack(ItemHandler.STORAGE_DISC);

                    int quality = 50 + (rand.nextInt(50));

                    PlantDNA dna = new PlantDNA(plantId, quality);

                    NBTTagCompound nbt = new NBTTagCompound();
                    dna.writeToNBT(nbt);

                    disc.setTagCompound(nbt);
                    outputs.get(0).add(disc);
                }
            }
        else if (input.getItem() == ItemHandler.AMBER || input.getItem() == ItemHandler.FROZEN_LEECH)
        {
            if (input.getItemDamage() == 0)
            {
                List<Dinosaur> possibleDinos = input.getItem() == ItemHandler.AMBER ? EntityHandler.getDinosaursFromAmber() : EntityHandler.getMammalCreatures();


                for(Dinosaur dino : possibleDinos)

                {
                    ItemStack disc = ItemStack.EMPTY;
                    disc = new ItemStack(ItemHandler.STORAGE_DISC);

                    int quality = 50 + (rand.nextInt(50));

                    DinoDNA dna = new DinoDNA(dino, quality, GeneticsHelper.randomGenetics(rand));

                    NBTTagCompound nbt = new NBTTagCompound();
                    dna.writeToNBT(nbt);

                    disc.setTagCompound(nbt);
                    outputs.get(0).add(disc);
                }
            }
        }
		else if (input.getItem() == ItemHandler.DINOSAUR_MEAT) 
		{
			ItemStack disc = ItemStack.EMPTY;
			Dinosaur dino = EntityHandler.getDinosaurById(input.getMetadata());
            disc = new ItemStack(ItemHandler.STORAGE_DISC);
            DinoDNA dna = new DinoDNA(dino, 100, GeneticsHelper.randomGenetics(rand));

            NBTTagCompound nbt = new NBTTagCompound();
            dna.writeToNBT(nbt);

            disc.setTagCompound(nbt);
            outputs.get(0).add(disc);
        }
		return outputs;

	}

	
	public static List<DNAExtractorRecipeWrapper> getRecipes(IJeiHelpers helpers)
	{
		List<DNAExtractorRecipeWrapper> list = new ArrayList<>();
		list.add(new DNAExtractorRecipeWrapper(new ItemStack(ItemHandler.AMBER)));
		list.add(new DNAExtractorRecipeWrapper(new ItemStack(ItemHandler.SEA_LAMPREY)));
        list.add(new DNAExtractorRecipeWrapper(new ItemStack(ItemHandler.FROZEN_LEECH)));
        list.add(new DNAExtractorRecipeWrapper(new ItemStack(ItemHandler.AMBER, 1, 1)));
		for(int dino = 0; dino < EntityHandler.getDinosaurs().size(); dino++)
			list.add(new DNAExtractorRecipeWrapper(new ItemStack(ItemHandler.DINOSAUR_MEAT, 1, dino)));
		return list;
	}

}
