package net.vit.jurassicreborn.common.recipes;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.vit.jurassicreborn.common.items.ModItems;

public class PotionDartRecipe extends CustomRecipe {

    // ───────────────── Serializer (CORRECT FOR 1.21) ─────────────────

    public static final RecipeSerializer<PotionDartRecipe> SERIALIZER =
            new SimpleCraftingRecipeSerializer<>(PotionDartRecipe::new);

    // ───────────────── Constructor ─────────────────

    public PotionDartRecipe(CraftingBookCategory category) {
        super(category);
    }

    // ───────────────── Assembly ─────────────────

    @Override
    public ItemStack assemble(CraftingInput input, net.minecraft.core.HolderLookup.Provider registries) {
        ItemStack potion = ItemStack.EMPTY;
        int darts = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (isPotion(stack)) {
                potion = stack;
            } else if (isBaseDart(stack)) {
                darts++;
            }
        }

        if (potion.isEmpty() || darts == 0) {
            return ItemStack.EMPTY;
        }

        ItemStack result = new ItemStack(ModItems.DART_TIPPED_POTION.get(), darts);

        PotionContents contents = potion.get(DataComponents.POTION_CONTENTS);
        if (contents != null) {
            result.set(DataComponents.POTION_CONTENTS, contents);
        }

        return result;
    }

    // ───────────────── Matching ─────────────────

    @Override
    public boolean matches(CraftingInput input, Level level) {
        ItemStack potion = ItemStack.EMPTY;
        int darts = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.isEmpty()) continue;

            if (isPotion(stack)) {
                if (!potion.isEmpty()) return false;
                potion = stack;
            } else if (isBaseDart(stack)) {
                darts++;
            } else {
                return false;
            }
        }

        return !potion.isEmpty() && darts > 0;
    }

    // ───────────────── Metadata ─────────────────

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider registries) {
        return new ItemStack(ModItems.DART_TIPPED_POTION.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeType.CRAFTING;
    }

    // ───────────────── Helpers ─────────────────

    private boolean isPotion(ItemStack stack) {
        return stack.is(Items.POTION)
                || stack.is(Items.SPLASH_POTION)
                || stack.is(Items.LINGERING_POTION);
    }

    private boolean isBaseDart(ItemStack stack) {
        return stack.is(ModItems.DART_TRANQUILIZER.get())
                || stack.is(ModItems.DART_POISON_CYCASIN.get())
                || stack.is(ModItems.DART_POISON_EXECUTIONER_CONCOCTION.get())
                || stack.is(ModItems.TRACKER_DART.get());
    }
}
