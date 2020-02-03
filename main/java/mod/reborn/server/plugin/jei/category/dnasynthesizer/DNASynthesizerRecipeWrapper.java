package mod.reborn.server.plugin.jei.category.dnasynthesizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

public class DNASynthesizerRecipeWrapper implements IRecipeWrapper {
    private final SynthesizerInput input;

    public DNASynthesizerRecipeWrapper(SynthesizerInput input) {
        this.input = input;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
//        int metadata = this.input.getMetadata();
//        List<ItemStack> inputs = new ArrayList<>();
//        ItemStack data = new ItemStack(ItemHandler.STORAGE_DISC);
//        data.setTagCompound(this.input.getTag());
//        inputs.add(data);
//        ingredients.setInputs(ItemStack.class, inputs);
//        ItemStack output = new ItemStack(this.input.getItem(), 1, metadata);
//        ingredients.setOutput(ItemStack.class, output);

        ingredients.setInput(ItemStack.class, input.stack);
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
