package net.vit.jurassicreborn.common.recipes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;
import net.vit.jurassicreborn.common.items.ModItems;

public class PotionDartRecipe extends CustomRecipe {

    public static final SimpleRecipeSerializer<PotionDartRecipe> SERIALIZER =
            new SimpleRecipeSerializer<>(PotionDartRecipe::new);

    public PotionDartRecipe(ResourceLocation id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        ItemStack potion = ItemStack.EMPTY;
        int dartCount = 0;

        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                if (isPotion(stack)) {
                    if (!potion.isEmpty()) {
                        return false; // more than one potion
                    }
                    potion = stack;
                } else if (stack.is(ModItems.DART_TIPPED_POTION.get())) {
                    ++dartCount;
                } else {
                    return false; // unexpected ingredient
                }
            }
        }
        return !potion.isEmpty() && dartCount > 0;
    }

    @Override
    public ItemStack assemble(CraftingContainer container) { // 1.19.2 signature
        ItemStack potion = ItemStack.EMPTY;
        int dartCount = 0;

        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                if (isPotion(stack)) {
                    if (!potion.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                    potion = stack.copy();
                } else if (stack.is(ModItems.DART_TIPPED_POTION.get())) {
                    ++dartCount;
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }

        if (potion.isEmpty() || dartCount == 0) {
            return ItemStack.EMPTY;
        }

        ItemStack result = new ItemStack(ModItems.DART_TIPPED_POTION.get(), dartCount);
        PotionUtils.setPotion(result, PotionUtils.getPotion(potion));
        PotionUtils.setCustomEffects(result, PotionUtils.getCustomEffects(potion));

        CompoundTag tag = potion.getTag();
        if (tag != null && tag.contains("CustomPotionColor", 99)) {
            result.getOrCreateTag().putInt("CustomPotionColor", tag.getInt("CustomPotionColor"));
        }

        return result;
    }

    @Override
    public ItemStack getResultItem() { // 1.19.2 signature
        return new ItemStack(ModItems.DART_TIPPED_POTION.get());
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    private boolean isPotion(ItemStack stack) {
        return stack.is(Items.POTION) || stack.is(Items.SPLASH_POTION) || stack.is(Items.LINGERING_POTION);
    }
}
