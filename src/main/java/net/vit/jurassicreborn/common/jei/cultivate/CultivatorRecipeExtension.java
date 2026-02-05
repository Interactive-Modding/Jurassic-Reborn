package net.vit.jurassicreborn.common.jei.cultivate;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.items.ModItems;

/** JEI recipe extension for the cultivator. */
public class CultivatorRecipeExtension implements IRecipeCategoryExtension {
    private final CultivateInput input;

    public CultivatorRecipeExtension(CultivateInput input) {
        this.input = input;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
        Dinosaur dino = input.dino;
        int meta = DinosaurHandler.getId(dino);
        builder.addSlot(RecipeIngredientRole.INPUT, 41, 7)
                .addItemStack(new ItemStack(ModItems.SYRINGES.get(dino).get(), 1));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 106, 7)
                .addItemStack(new ItemStack(ModItems.hatchedDinoEggs.get(dino).get(), 1));
    }

    public Dinosaur getDinosaur() {
        return input.dino;
    }
}