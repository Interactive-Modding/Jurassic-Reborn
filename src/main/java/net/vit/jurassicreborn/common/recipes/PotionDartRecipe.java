package net.vit.jurassicreborn.common.recipes;

import com.google.gson.JsonObject;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.vit.jurassicreborn.common.items.ModItems;

public class PotionDartRecipe extends CustomRecipe {

    // --- Serializer ----------------------------------------------------------
    public static final RecipeSerializer<PotionDartRecipe> SERIALIZER = new RecipeSerializer<>() {
        @Override
        public PotionDartRecipe fromJson(ResourceLocation id, JsonObject json) {
            return new PotionDartRecipe(id, CraftingBookCategory.MISC);
        }

        @Override
        public PotionDartRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            return new PotionDartRecipe(id, CraftingBookCategory.MISC);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, PotionDartRecipe recipe) {
            // No extra data
        }
    };

    // --- Constructor ---------------------------------------------------------
    public PotionDartRecipe(ResourceLocation id, CraftingBookCategory category) {
        super(id, category);
    }

    // --- Match check ---------------------------------------------------------
    @Override
    public boolean matches(CraftingContainer container, Level level) {
        ItemStack potion = ItemStack.EMPTY;
        int dartCount = 0;

        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                if (isPotion(stack)) {
                    if (!potion.isEmpty()) return false;
                    potion = stack;
                } else if (isBaseDart(stack)) {
                    ++dartCount;
                } else {
                    return false;
                }
            }
        }
        return !potion.isEmpty() && dartCount > 0;
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess registryAccess) {
        ItemStack potion = ItemStack.EMPTY;
        int dartCount = 0;

        for (int i = 0; i < craftingContainer.getContainerSize(); ++i) {
            ItemStack stack = craftingContainer.getItem(i);
            if (!stack.isEmpty()) {
                if (isPotion(stack)) {
                    potion = stack.copy();
                } else if (isBaseDart(stack)) {
                    ++dartCount;
                }
            }
        }

        if (potion.isEmpty() || dartCount == 0) return ItemStack.EMPTY;

        ItemStack result = new ItemStack(ModItems.DART_TIPPED_POTION.get(), dartCount);
        PotionUtils.setPotion(result, PotionUtils.getPotion(potion));
        PotionUtils.setCustomEffects(result, PotionUtils.getCustomEffects(potion));

        CompoundTag tag = potion.getTag();
        if (tag != null && tag.contains("CustomPotionColor", 99)) {
            result.getOrCreateTag().putInt("CustomPotionColor", tag.getInt("CustomPotionColor"));
        }

        return result;
    }


    // --- Utility checks ------------------------------------------------------
    private boolean isPotion(ItemStack stack) {
        return stack.is(Items.POTION) || stack.is(Items.SPLASH_POTION) || stack.is(Items.LINGERING_POTION);
    }

    private boolean isBaseDart(ItemStack stack) {
        return stack.is(ModItems.DART_TRANQUILIZER.get())
                || stack.is(ModItems.DART_POISON_CYCASIN.get())
                || stack.is(ModItems.DART_POISON_EXECUTIONER_CONCOCTION.get())
                || stack.is(ModItems.TRACKER_DART.get());
    }

    // --- Misc recipe behavior ------------------------------------------------
    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return new ItemStack(ModItems.DART_TIPPED_POTION.get());
    }

    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }
}
