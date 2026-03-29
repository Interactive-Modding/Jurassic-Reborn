package net.vit.jurassicreborn.common.recipes.cleaner;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.recipes.FluidAndItemRecipeWrapper;
import net.vit.jurassicreborn.common.recipes.ModRecipeSerializers;

public class CleaningRecipe implements Recipe<FluidAndItemRecipeWrapper> {

    /* ---------------------------------------------------------------------
       TYPE
       --------------------------------------------------------------------- */
    public static final RecipeType<CleaningRecipe> CLEANING =
            RecipeType.simple(JurassicReborn.resource("cleaning"));

    /* ---------------------------------------------------------------------
       DATA
       --------------------------------------------------------------------- */
    private final ResourceLocation id;
    private final Ingredient input;
    private final ItemStack output;

    public CleaningRecipe(ResourceLocation id, Ingredient input, ItemStack output) {
        this.id = id;
        this.input = input;
        this.output = output;
    }

    /* ---------------------------------------------------------------------
       LOGIC
       --------------------------------------------------------------------- */
    @Override
    public boolean matches(FluidAndItemRecipeWrapper container, Level level) {
        return input.test(container.getItem(0))
                && container.getTank().getFluidInTank(0).getAmount() > 0;
    }

    @Override
    public ItemStack assemble(FluidAndItemRecipeWrapper container, HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int w, int h) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output.copy();
    }

    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.CLEANING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CLEANING;
    }

    /* ---------------------------------------------------------------------
       SERIALIZER
       --------------------------------------------------------------------- */
    public static class Serializer implements RecipeSerializer<CleaningRecipe> {

        /* ---------------- JSON / DATAPACK ---------------- */
        private static final MapCodec<CleaningRecipe> CODEC =
                RecordCodecBuilder.mapCodec(instance ->
                        instance.group(
                                ResourceLocation.CODEC.fieldOf("id")
                                        .forGetter(r -> r.id),
                                Ingredient.CODEC.fieldOf("input")
                                        .forGetter(r -> r.input),
                                ItemStack.CODEC.fieldOf("output")
                                        .forGetter(r -> r.output)
                        ).apply(instance, CleaningRecipe::new)
                );

        /* ---------------- NETWORK ---------------- */
        private static final StreamCodec<RegistryFriendlyByteBuf, CleaningRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        ResourceLocation.STREAM_CODEC,
                        r -> r.id,
                        Ingredient.CONTENTS_STREAM_CODEC,
                        r -> r.input,
                        ItemStack.STREAM_CODEC,
                        r -> r.output,
                        CleaningRecipe::new
                );

        @Override
        public MapCodec<CleaningRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CleaningRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
