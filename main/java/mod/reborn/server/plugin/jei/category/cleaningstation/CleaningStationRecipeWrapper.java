package mod.reborn.server.plugin.jei.category.cleaningstation;

import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CleaningStationRecipeWrapper implements IRecipeWrapper {
    private final CleanableInput input;

    public CleaningStationRecipeWrapper(CleanableInput input) {
        this.input = input;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
//        int metadata = EntityHandler.getDinosaurId(this.dinosaur);
//        ingredients.setInputs(ItemStack.class, Lists.newArrayList(new ItemStack(BlockHandler.getEncasedFossil(metadata), 1, BlockHandler.getMetadata(metadata))));
//        ItemStack output = new ItemStack(ItemHandler.FOSSILS.get(this.bone), 1, metadata);
//        ingredients.setOutput(ItemStack.class, output);
        ingredients.setInput(ItemStack.class, input.stack);
        List<ItemStack> list = Lists.newArrayList();
        input.grind.getChancedOutputs(input.stack).forEach(pair -> {
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
