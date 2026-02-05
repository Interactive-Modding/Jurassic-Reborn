package net.vit.jurassicreborn.common.jei.embryonic;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.world.item.ItemStack;

/** JEI recipe extension for the embryonic machine. */
public class EmbryonicRecipeExtension implements IRecipeCategoryExtension {
    private final EmbryoInput input;

    public EmbryonicRecipeExtension(EmbryoInput input) { this.input = input; }

    public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
        ItemStack dna = new ItemStack(input.getInputItem(), 1);
        dna.setTag(input.getTag());
        // Adjust positions by one pixel to match the GUI
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 37).addItemStack(dna);
        builder.addSlot(RecipeIngredientRole.INPUT, 27, 37).addItemStack(new ItemStack(input.getPetriDishItem()));
        builder.addSlot(RecipeIngredientRole.INPUT, 27, 1).addItemStack(new ItemStack(net.vit.jurassicreborn.common.items.ModItems.EMPTY_SYRINGE.get()));

        ItemStack output = new ItemStack(input.getOutputItem(), 1);
        output.setTag(input.getTag());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 96, 14).addItemStack(output);
    }
}