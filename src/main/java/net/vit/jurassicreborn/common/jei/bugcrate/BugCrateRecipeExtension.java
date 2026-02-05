package net.vit.jurassicreborn.common.jei.bugcrate;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.vit.jurassicreborn.common.entities.EntityUtils.FoodType;
import net.vit.jurassicreborn.common.items.Food.FoodHelper;

import java.util.List;
import java.util.stream.Collectors;

/** JEI recipe extension for the Bug Crate. */
public class BugCrateRecipeExtension implements IRecipeCategoryExtension {
    private static final List<ItemStack> PLANTS = ForgeRegistries.ITEMS.getValues().stream()
            .filter(i -> FoodHelper.isFoodType(i, FoodType.PLANT))
            .map(ItemStack::new)
            .collect(Collectors.toList());

    private final ItemStack bug;

    public BugCrateRecipeExtension(ItemStack bug) {
        this.bug = bug;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 17).addItemStacks(PLANTS);
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 51).addItemStack(bug);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 126, 17).addItemStack(new ItemStack(bug.getItem()));
    }
}
