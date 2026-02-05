package net.vit.jurassicreborn.common.jei.skeletonassembly;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.blocks.entities.skeletonassembly.SkeletonInput;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

/**
 * 1.19.2 port of the old SkeletonAssemblyRecipeWrapper.
 * JEI 11 no longer uses IRecipeWrapper – implement {@link IRecipeCategoryExtension} instead.
 */
public class SkeletonAssemblyRecipeExtension implements IRecipeCategoryExtension {

    private static final int SLOT_SIZE = 18;        // JEI’s standard slot = 18 px
    private final SkeletonInput input;

    public SkeletonAssemblyRecipeExtension(SkeletonInput input) {
        this.input = input;
    }

    private static final int BACKGROUND_X = 15;
    private static final int BACKGROUND_Y = 15;

    public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
        String[][] pattern = input.dinosaur.getRecipe();
        Map<String, RegistryObject<Item>> fossils = input.fresh
                ? ModItems.FRESH_BONES.get(input.dinosaur)
                : ModItems.BONES.get(input.dinosaur);

        for (int row = 0; row < pattern.length; row++) {
            String[] line = pattern[row];
            for (int col = 0; col < 5; col++) {
                int slotX = 16 + col * SLOT_SIZE - BACKGROUND_X;
                int slotY = 16 + row * SLOT_SIZE - BACKGROUND_Y;
                IRecipeSlotBuilder slot = builder
                        .addSlot(RecipeIngredientRole.INPUT, slotX, slotY);
                if (col < line.length && !line[col].isEmpty() && fossils != null) {
                    var reg = fossils.get(line[col]);
                    if (reg != null)
                        slot.addItemStack(new ItemStack(reg.get()));
                }
            }
        }

        int outY = (pattern.length * SLOT_SIZE - SLOT_SIZE) / 2;
        ItemStack result = input.fresh
                ? new ItemStack(ModItems.FRESH_SKELETONS.get(input.dinosaur).get())
                : new ItemStack(ModItems.FOSSIL_SKELETONS.get(input.dinosaur).get());

        // Corrected output slot:
        builder.addSlot(RecipeIngredientRole.OUTPUT, 140 - BACKGROUND_X, 52 - BACKGROUND_Y)
                .addItemStack(result);
    }
}