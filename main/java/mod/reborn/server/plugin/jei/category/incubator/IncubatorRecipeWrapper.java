package mod.reborn.server.plugin.jei.category.incubator;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.item.ItemHandler;
import net.minecraft.item.ItemStack;
import mod.reborn.server.dinosaur.Dinosaur;

public class IncubatorRecipeWrapper implements IRecipeWrapper {

    private final Dinosaur dinosaur;

    public IncubatorRecipeWrapper(IncubatorInput input) {
        this.dinosaur = input.getDinosaur();
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        int meta = EntityHandler.getDinosaurId(this.dinosaur);
        ingredients.setInput(ItemStack.class, new ItemStack(ItemHandler.EGG, 1, meta));
        ingredients.setOutput(ItemStack.class, new ItemStack(ItemHandler.HATCHED_EGG, 1, meta));
    }

    public Dinosaur getDinosaur() {
        return dinosaur;
    }
}