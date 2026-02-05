package net.vit.jurassicreborn.common.jei.embryoniccalcification;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.items.ModItems;

/** JEI recipe extension for the embryo calcification machine. */
public class CalcificationRecipeExtension implements IRecipeCategoryExtension {
    private final CalcificationInput input;

    public CalcificationRecipeExtension(CalcificationInput input) { this.input = input; }

    public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
        // 1px adjustments so the ghost slots line up
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1)
                .addItemStack(new ItemStack(ModItems.SYRINGES.get(input.dinosaur).get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 37)
                .addItemStack(new ItemStack(Items.EGG));
        ItemStack out = new ItemStack(ModItems.dinoEggs.get(input.dinosaur).get());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 64, 19).addItemStack(out);
    }
}