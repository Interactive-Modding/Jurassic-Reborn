package mod.reborn.server.plugin.jei.category.incubator;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mod.reborn.RebornMod;
import mod.reborn.server.plugin.jei.RebornJEIPlugin;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class IncubatorRecipeCategory implements IRecipeCategory<IncubatorRecipeWrapper> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(RebornMod.MODID, "textures/gui/incubator.png");

    private final IDrawable background;
    private final String title;

    public IncubatorRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 31, 13, 112, 53);
        this.title = I18n.translateToLocal("container.incubator");

    }

    @Override
    public String getUid() {
        return RebornJEIPlugin.INCUBATOR;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IncubatorRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup stackGroup = recipeLayout.getItemStacks();
        stackGroup.init(0, true, 47, 0);
        stackGroup.set(0, ingredients.getInputs(ItemStack.class).get(0));
        stackGroup.init(1, false, 47, 35);
        stackGroup.set(1, ingredients.getOutputs(ItemStack.class).get(0));
    }

    @Override
    public String getModName() {
        return RebornMod.NAME;
    }

}