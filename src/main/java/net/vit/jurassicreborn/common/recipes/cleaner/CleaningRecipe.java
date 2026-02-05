package net.vit.jurassicreborn.common.recipes.cleaner;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.recipes.FluidAndItemRecipeWrapper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class CleaningRecipe implements Recipe<FluidAndItemRecipeWrapper> {

    public static final RecipeType<CleaningRecipe> CLEANING = RecipeType.simple(JurassicReborn.resource("cleaning"));

    public static Serializer INSTANCE = new Serializer();

    final ResourceLocation id;
    final Ingredient input;

    final ItemStack output;


    public CleaningRecipe(ResourceLocation id, Ingredient input, ItemStack output){
        this.id = id;
        this.input = input;
        this.output = output;
    }

    @Override
    public boolean matches(FluidAndItemRecipeWrapper pContainer, Level pLevel) {
        ItemStack recipeItem = pContainer.getItem(0);
        return input.test(recipeItem) && pContainer.getTank().getFluidInTank(0).getAmount() > 0;
    }

    @Override
    @Nonnull
    public ItemStack assemble(FluidAndItemRecipeWrapper pContainer, RegistryAccess registryAccess) {
//        pContainer.getItem(0).setCount(pContainer.getItem(0).getCount() - this.input.getCount());
//
//        Collection<ArrayList<RegistryObject<BoneItem>>> tempList = DynamicBoneRegistry.BoneMap.values();
//
//        List<ArrayList<RegistryObject<BoneItem>>> listt = new ArrayList<>(tempList.stream().toList());
//
//        Collections.shuffle(listt);
//
//        Collections.shuffle(listt.get(0));
//
//        return listt.get(0).get(0).get().getDefaultInstance();
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return CLEANING;
    }

    private static class Serializer implements RecipeSerializer<CleaningRecipe>{

        Serializer(){
        }


        @Override
        public CleaningRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            Ingredient input = CraftingHelper.getIngredient(pSerializedRecipe.get("input"), false);
            ItemStack output = CraftingHelper.getItemStack(pSerializedRecipe.getAsJsonObject("output"), false);

            return new CleaningRecipe(pRecipeId, input, output);
        }

        @Nullable
        @Override
        public CleaningRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            Ingredient input = Ingredient.fromNetwork(pBuffer);
            ItemStack output = pBuffer.readItem();

            return new CleaningRecipe(pRecipeId, input, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, CleaningRecipe pRecipe) {
            pRecipe.input.toNetwork(pBuffer);
            pBuffer.writeItemStack(pRecipe.output, false);
        }

    }
}
