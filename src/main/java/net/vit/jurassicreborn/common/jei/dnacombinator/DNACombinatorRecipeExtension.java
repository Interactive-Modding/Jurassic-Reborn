package net.vit.jurassicreborn.common.jei.dnacombinator;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.world.item.ItemStack;

/** JEI recipe extension for the DNA Combinator. */
public class DNACombinatorRecipeExtension implements IRecipeCategoryExtension {
    private final ItemStack disc;

    public DNACombinatorRecipeExtension(ItemStack disc) {
        this.disc = disc;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 55, 13).addItemStack(disc);
        builder.addSlot(RecipeIngredientRole.INPUT, 105, 13).addItemStack(disc.copy());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 81, 60).addItemStack(disc.copy());
    }
}
