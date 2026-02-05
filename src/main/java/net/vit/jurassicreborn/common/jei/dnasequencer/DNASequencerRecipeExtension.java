package net.vit.jurassicreborn.common.jei.dnasequencer;

import com.mojang.datafixers.util.Pair;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.items.ModItems;

import java.util.ArrayList;
import java.util.List;

/** JEI recipe extension for the DNA Sequencer. */
public class DNASequencerRecipeExtension implements IRecipeCategoryExtension {
    private static final int SLOT_SIZE = 18;
    private final SequencerInput input;
    private final List<Pair<Float, ItemStack>> outputs = new ArrayList<>();

    public DNASequencerRecipeExtension(SequencerInput input) {
        this.input = input;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
        // input tissue / soft tissue
        // slots align with the top row of the machine GUI
        builder.addSlot(RecipeIngredientRole.INPUT, 11, 13).addItemStack(input.stack);
        // empty storage disc
        builder.addSlot(RecipeIngredientRole.INPUT, 33, 13).addItemStack(new ItemStack(ModItems.STORAGE_DISC.get()));

        outputs.clear();
        outputs.addAll(input.item.getChancedOutputs(input.stack));

        IRecipeSlotBuilder out = builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 13);
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < outputs.size(); i++) {
            stacks.add(outputs.get(i).getSecond());
        }
        if (!stacks.isEmpty()) {
            out.addItemStacks(stacks);
        }
    }
}