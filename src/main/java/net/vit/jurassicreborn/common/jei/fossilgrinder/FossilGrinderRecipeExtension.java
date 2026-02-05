package net.vit.jurassicreborn.common.jei.fossilgrinder;

import com.mojang.datafixers.util.Pair;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FossilGrinderRecipeExtension implements IRecipeCategoryExtension {
    private static final int SLOT_SIZE = 18;
    private final GrinderInput input;
    private final List<Pair<Float, ItemStack>> outputs = new ArrayList<>();

    public FossilGrinderRecipeExtension(GrinderInput input) {
        this.input = input;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
        // 6 input slots in a 3x2 grid
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 3; col++) {
                // Align with the actual menu slot positions
                builder.addSlot(RecipeIngredientRole.INPUT, 5 + col * SLOT_SIZE, 5 + row * SLOT_SIZE)
                        .addItemStack(input.stack);
            }
        }

        outputs.clear();
        outputs.addAll(input.grind.getChancedOutputs(input.stack));

        int slotCount = 6;
        for (int slot = 0; slot < slotCount; slot++) {
            int row = slot / 3;
            int col = slot % 3;
            // Output slots share the same off-by-one offset
            IRecipeSlotBuilder slotBuilder = builder.addSlot(RecipeIngredientRole.OUTPUT, 90 + col * SLOT_SIZE, 5 + row * SLOT_SIZE);
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