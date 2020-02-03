package mod.reborn.server.plugin.jei.category.dnasequencer;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mod.reborn.RebornMod;
import mod.reborn.server.plugin.jei.RebornJEIPlugin;
import mod.reborn.server.plugin.jei.util.RecipeLayoutOutputSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import java.awt.*;
import java.util.List;

public class DNASequencerRecipeCategory implements IRecipeCategory<DNASequencerRecipeWrapper> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(RebornMod.MODID, "textures/gui/dna_sequencer.png");

    private final IDrawable background;
    private final String title;

    private final IDrawableAnimated arrow;

    private RecipeLayoutOutputSupplier outPutSupplier = null;

    public DNASequencerRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 33, 3, 107, 32, 10, 10, 10, 10);
        this.title = I18n.translateToLocal("tile.dna_sequencer.name");
        IDrawableStatic arrowDrawable = guiHelper.createDrawable(TEXTURE, 176, 0, 22, 6);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        this.arrow.draw(minecraft, 64, 28);

        ItemStack stack = outPutSupplier.get();
        if(stack != null && !stack.isEmpty()) {
            float value = stack.getOrCreateSubCompound("jei_rendering_info").getFloat("Chance");
            String text = value + "%";
            if(value != 100) {
                int width = minecraft.fontRenderer.getStringWidth(text);
                minecraft.fontRenderer.drawString(text, 72 - width / 2, 47, Color.GRAY.getRGB());
            }
        }
    }

    @Override
    public String getUid() {
        return RebornJEIPlugin.DNA_SEQUENCER;
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
    public void setRecipe(IRecipeLayout recipeLayout, DNASequencerRecipeWrapper recipeWrapper, IIngredients ingredients) {
        outPutSupplier = new RecipeLayoutOutputSupplier(recipeLayout, 0, false);

        IGuiItemStackGroup stackGroup = recipeLayout.getItemStacks();
        List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
        List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);
        stackGroup.init(0, true, 20, 22);
        stackGroup.set(0, inputs.get(0));
        stackGroup.init(1, true, 42, 22);
        stackGroup.set(1, inputs.get(1));
        stackGroup.init(2, false, 89, 22);
        stackGroup.set(2, outputs.get(0));
    }

	@Override
	public String getModName() {
		return RebornMod.NAME;
	}
}
