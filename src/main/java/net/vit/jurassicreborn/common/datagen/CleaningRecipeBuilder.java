package net.vit.jurassicreborn.common.datagen;

import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vit.jurassicreborn.common.recipes.ModRecipeSerializers;
import net.vit.jurassicreborn.common.recipes.cleaner.CleaningRecipe;

import javax.annotation.Nullable;

public class CleaningRecipeBuilder implements RecipeBuilder {

    private final Item result;
    private final Ingredient ingredient;
    private final int count;
    private final DeferredHolder<RecipeSerializer<?>, ? extends RecipeSerializer<?>> serializer;

    @Nullable
    private String group;

    public CleaningRecipeBuilder(DeferredHolder<RecipeSerializer<?>, ? extends RecipeSerializer<?>> serializer, Ingredient ingredient, ItemLike result, int count) {
        this.serializer = serializer;
        this.ingredient = ingredient;
        this.result = result.asItem();
        this.count = count;
    }

    public static CleaningRecipeBuilder cleaning(Ingredient ingredient, ItemLike result) {
        return new CleaningRecipeBuilder(
                ModRecipeSerializers.CLEANING,
                ingredient,
                result,
                1
        );
    }


    public static CleaningRecipeBuilder cleaning(ItemLike input, ItemLike result) {
        return cleaning(Ingredient.of(input), result);
    }

    // REQUIRED by RecipeBuilder in 1.21
    @Override
    public CleaningRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        // recipes no longer auto-generate advancements unless you do it yourself
        return this;
    }

    @Override
    public CleaningRecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return this.result;
    }

    @Override
    public void save(RecipeOutput output, ResourceLocation id) {
        output.accept(
                id,
                new CleaningRecipe(
                        id,
                        this.ingredient,
                        new ItemStack(this.result, this.count)
                ),
                null
        );
    }


}
