package net.vit.jurassicreborn.common.jei.cleaningstation;

import com.mojang.datafixers.util.Pair;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class CleaningStationRecipeExtension implements IRecipeCategoryExtension {
    private static final int SLOT_SIZE = 18;
    private final CleanableInput input;
    private final List<Pair<Float, ItemStack>> outputs = new ArrayList<>();

    public CleaningStationRecipeExtension(CleanableInput input) {
        this.input = input;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
        // Offsets corrected to line up with the menu
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 1).addItemStack(input.stack);
        builder.addSlot(RecipeIngredientRole.INPUT, 10, 37).addItemStack(new ItemStack(Items.WATER_BUCKET));

        outputs.clear();
        outputs.addAll(input.cleanable.getChancedOutputs(input.stack));

        int slotCount = 6;
        for (int slot = 0; slot < slotCount; slot++) {
            int row = slot / 3;
            int col = slot % 3;
            IRecipeSlotBuilder slotBuilder = builder.addSlot(RecipeIngredientRole.OUTPUT, 62 + col * SLOT_SIZE, 10 + row * SLOT_SIZE);
            List<ItemStack> stacks = new ArrayList<>();
            for (int i = slot; i < outputs.size(); i += slotCount) {
                stacks.add(outputs.get(i).getSecond());
            }
            if (!stacks.isEmpty()) {
                slotBuilder.addItemStacks(stacks);
            }
        }
    }
}
