package net.vit.jurassicreborn.common.recipes;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.recipes.cleaner.CleaningRecipe;
import net.vit.jurassicreborn.common.recipes.PotionDartRecipe;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, JurassicReborn.MODID);

    public static final RegistryObject<RecipeSerializer<?>> CLEANING =
            SERIALIZERS.register("cleaning", () -> CleaningRecipe.INSTANCE);

    public static final RegistryObject<RecipeSerializer<?>> POTION_DART =
            SERIALIZERS.register("crafting_special_potion_dart", () -> PotionDartRecipe.SERIALIZER);
}
