package net.vit.jurassicreborn.common.recipes;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.recipes.cleaner.CleaningRecipe;

public class ModRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, JurassicReborn.MODID);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CleaningRecipe>> CLEANING =
            SERIALIZERS.register("cleaning", CleaningRecipe.Serializer::new);

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PotionDartRecipe>> POTION_DART =
            SERIALIZERS.register("crafting_special_potion_dart", () -> PotionDartRecipe.SERIALIZER);
}
