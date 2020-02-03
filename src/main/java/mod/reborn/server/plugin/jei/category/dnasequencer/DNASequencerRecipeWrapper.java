package mod.reborn.server.plugin.jei.category.dnasequencer;

import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mod.reborn.server.item.ItemHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DNASequencerRecipeWrapper implements IRecipeWrapper {
    private final SequencerInput input;

    public DNASequencerRecipeWrapper(SequencerInput input) {
        this.input = input;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, Lists.newArrayList(input.stack, new ItemStack(ItemHandler.STORAGE_DISC)));
        List<ItemStack> list = Lists.newArrayList();
        input.item.getChancedOutputs(input.stack).forEach(pair -> {
            ItemStack stack = pair.getRight();
            stack.getOrCreateSubCompound("jei_rendering_info").setFloat("Chance", Math.round(pair.getLeft() * 10F) / 10F);
            list.add(stack);
        });

        List<List<ItemStack>> outputs = new ArrayList<>();
        outputs.add(list);
        ingredients.setOutputLists(ItemStack.class, outputs);
    }

    @Override
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    }

    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return Collections.emptyList();
    }

    @Override
    public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }
}
