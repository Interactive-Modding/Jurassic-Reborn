package net.vit.jurassicreborn.common.jei.incubator;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.items.ModItems;

/** JEI recipe extension for the incubator. */
public class IncubatorRecipeExtension implements IRecipeCategoryExtension {
    private final IncubatorInput input;

    public IncubatorRecipeExtension(IncubatorInput input) {
        this.input = input;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
        var dino = input.getDinosaur();
        int meta = DinosaurHandler.getId(dino);
        builder.addSlot(RecipeIngredientRole.INPUT, 48, 0)
                .addItemStack(new ItemStack(ModItems.dinoEggs.get(dino).get(), 1));
        builder.addSlot(RecipeIngredientRole.INPUT, 48, 36)
                .addItemStack(new ItemStack(ModItems.PEAT_MOSS_BLOCK.get()));
//        builder.addSlot(RecipeIngredientRole.OUTPUT, 47, 0)
//                .addItemStack(new ItemStack(ModItems.hatchedDinoEggs.get(dino).get(), 1));
    }

    public Dinosaur getDinosaur() {
        return input.getDinosaur();
    }
}