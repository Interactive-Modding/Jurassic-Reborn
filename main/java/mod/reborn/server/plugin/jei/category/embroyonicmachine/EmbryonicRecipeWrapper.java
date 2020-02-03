package mod.reborn.server.plugin.jei.category.embroyonicmachine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class EmbryonicRecipeWrapper implements IRecipeWrapper {
    private final EmbryoInput input;

    public EmbryonicRecipeWrapper(EmbryoInput input) {
        this.input = input;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        List<ItemStack> inputs = new ArrayList<>();

        int metadata = this.input.getMetadata();
        NBTTagCompound tag = this.input.getTag();

        ItemStack inputStack = new ItemStack(this.input.getInputItem(), 1, metadata);
        inputStack.setTagCompound(tag);

        inputs.add(inputStack);
        inputs.add(new ItemStack(this.input.getPetriDishItem()));
        ingredients.setInputs(ItemStack.class, inputs);

        ItemStack outputStack = new ItemStack(this.input.getOutputItem(), 1, metadata);
        outputStack.setTagCompound(tag);
        ingredients.setOutput(ItemStack.class, outputStack);
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
