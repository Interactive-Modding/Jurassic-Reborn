package net.vit.jurassicreborn.common.datagen;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.vit.jurassicreborn.common.recipes.cleaner.CleaningRecipe;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class CleaningRecipeBuilder implements RecipeBuilder {
    private final Item result;
    private final Ingredient ingredient;
    private final int count;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    @javax.annotation.Nullable
    private String group;
    private final RecipeSerializer<?> type;

    public CleaningRecipeBuilder(RecipeSerializer<?> pType, Ingredient pIngredient, ItemLike pResult, int pCount) {
        this.type = pType;
        this.result = pResult.asItem();
        this.ingredient = pIngredient;
        this.count = pCount;
    }

    public static CleaningRecipeBuilder cleaning(ItemLike like, ItemLike pResult) {
        return cleaning(Ingredient.of(like), pResult);
    }


    public static CleaningRecipeBuilder cleaning(Ingredient pIngredient, ItemLike pResult) {
        return new CleaningRecipeBuilder(CleaningRecipe.INSTANCE, pIngredient, pResult, 1);
    }

    public CleaningRecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        this.advancement.addCriterion(pCriterionName, pCriterionTrigger);
        return this;
    }

    public CleaningRecipeBuilder group(@javax.annotation.Nullable String pGroupName) {
        this.group = pGroupName;
        return this;
    }

    public Item getResult() {
        return this.result;
    }

    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
       // this.ensureValid(pRecipeId);
      //  this.advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId)).rewards(AdvancementRewards.Builder.recipe(pRecipeId)).requirements(RequirementsStrategy.OR);
        CreativeModeTab category = this.result.getItemCategory();
        String folder = category != null ? category.getRecipeFolderName() : "misc";
        ResourceLocation advancementId = new ResourceLocation(pRecipeId.getNamespace(), "recipes/" + folder + "/" + pRecipeId.getPath());
        pFinishedRecipeConsumer.accept(new CleaningRecipeBuilder.Result(pRecipeId, this.type, this.group == null ? "" : this.group, this.ingredient, this.result, this.count, this.advancement, advancementId));
    }

    private void ensureValid(ResourceLocation pId) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + pId);
        }
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final String group;
        private final Ingredient ingredient;
        private final Item result;
        private final int count;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final RecipeSerializer<?> type;

        public Result(ResourceLocation pId, RecipeSerializer<?> pType, String pGroup, Ingredient pIngredient, Item pResult, int pCount, Advancement.Builder pAdvancement, ResourceLocation pAdvancementId) {
            this.id = pId;
            this.type = pType;
            this.group = pGroup;
            this.ingredient = pIngredient;
            this.result = pResult;
            this.count = pCount;
            this.advancement = pAdvancement;
            this.advancementId = pAdvancementId;
        }

        public void serializeRecipeData(JsonObject pJson) {
            if (!this.group.isEmpty()) {
                pJson.addProperty("group", this.group);
            }

            pJson.add("input", this.ingredient.toJson());

            JsonObject resultObject = new JsonObject();

            resultObject.addProperty("item", Registry.ITEM.getKey(this.result).toString());
            resultObject.addProperty("count",count);

            pJson.add("output",resultObject);

            pJson.addProperty("count", this.count);
        }

        /**
         * Gets the ID for the recipe.
         */
        public ResourceLocation getId() {
            return this.id;
        }

        public RecipeSerializer<?> getType() {
            return this.type;
        }

        /**
         * Gets the JSON for the advancement that unlocks this recipe. Null if there is no advancement.
         */
        @javax.annotation.Nullable
        public JsonObject serializeAdvancement() {
            return null;//this.advancement.serializeToJson();
        }

        /**
         * Gets the ID for the advancement associated with this recipe. Should not be null if {@link #serializeAdvancement()}
         * is non-null.
         */
        @Nullable
        public ResourceLocation getAdvancementId() {
            return null;
            //this.advancementId;
        }
    }
}
