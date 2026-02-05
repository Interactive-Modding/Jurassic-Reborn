package net.vit.jurassicreborn.common.jei.dnasynthesizer;

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

/** JEI recipe extension for the DNA Synthesizer. */
public class DNASynthesizerRecipeExtension implements IRecipeCategoryExtension {
    private static final int SLOT_SIZE = 18;
    private final SynthesizerInput input;
    private final List<Pair<Float, ItemStack>> outputs = new ArrayList<>();

    public DNASynthesizerRecipeExtension(SynthesizerInput input) {
        this.input = input;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
        // storage disc with DNA
        // coordinates mirror the machine screen
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 1).addItemStack(input.stack);
        builder.addSlot(RecipeIngredientRole.INPUT, 2, 28).addItemStack(new ItemStack(ModItems.EMPTY_TEST_TUBE.get()));
        builder.addSlot(RecipeIngredientRole.INPUT, 28, 28).addItemStack(new ItemStack(ModItems.DNA_NUCLEOTIDES.get()));

        outputs.clear();
        outputs.addAll(input.item.getChancedOutputs(input.stack));

        IRecipeSlotBuilder out = builder.addSlot(RecipeIngredientRole.OUTPUT, 97, 5);
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < outputs.size(); i++) {
            stacks.add(outputs.get(i).getSecond());
        }
        if (!stacks.isEmpty()) {
            out.addItemStacks(stacks);
        }
    }
}
