package mod.reborn.server.plugin.jei.category.calcification;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mod.reborn.RebornMod;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import java.util.List;

public class CalcificationRecipeCategory implements IRecipeCategory<CalcificationRecipeWrapper> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(RebornMod.MODID, "textures/gui/embryo_calcification_machine.png");

    private final IDrawable background;
    private final String title;

    private final IDrawableAnimated arrow;

    public CalcificationRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 33, 13, 81, 54);
        this.title = I18n.translateToLocal("tile.embryo_calcification_machine.name");

        IDrawableStatic arrowDrawable = guiHelper.createDrawable(TEXTURE, 176, 14, 24, 16);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        this.arrow.draw(minecraft, 34, 19);
    }

    @Override
    public String getUid() {
        return "rebornmod.embryo_calcification_machine";
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
    public void setRecipe(IRecipeLayout recipeLayout, CalcificationRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup stackGroup = recipeLayout.getItemStacks();
        List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
        List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);

        stackGroup.init(0, true, 0, 0);
        stackGroup.set(0, inputs.get(0));
        stackGroup.init(1, true, 0, 36);
        stackGroup.set(1, new ItemStack(Items.EGG));

        stackGroup.init(2, false, 63, 18);
        stackGroup.set(2, outputs.get(0));
    }

    @Override
    public String getModName() {
	return RebornMod.NAME;
    }
}
