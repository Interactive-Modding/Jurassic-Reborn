package mod.reborn.server.plugin.jei.category.dnasynthesizer;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.*;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mod.reborn.RebornMod;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.plugin.jei.util.RecipeLayoutOutputSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import java.awt.*;
import java.util.List;

public class DNASynthesizerRecipeCategory implements IRecipeCategory<DNASynthesizerRecipeWrapper> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(RebornMod.MODID, "textures/gui/dna_synthesizer.png");

    private final IDrawable background;
    private final String title;

    private final IDrawableAnimated arrow;

    private RecipeLayoutOutputSupplier outPutSupplier = null;


    public DNASynthesizerRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 22, 21, 132, 45);
        this.title = I18n.translateToLocal("tile.dna_synthesizer.name");

        IDrawableStatic arrowDrawable = guiHelper.createDrawable(TEXTURE, 176, 14, 24, 16);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        this.arrow.draw(minecraft, 57, 14);
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
        return "rebornmod.dna_synthesizer";
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
    public void setRecipe(IRecipeLayout recipeLayout, DNASynthesizerRecipeWrapper recipeWrapper, IIngredients ingredients) {
        outPutSupplier = new RecipeLayoutOutputSupplier(recipeLayout, 0, false);

        IGuiItemStackGroup stackGroup = recipeLayout.getItemStacks();
        List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
        List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);
        stackGroup.init(0, true, 15, 0);
        stackGroup.set(0, inputs.get(0));
        stackGroup.init(1, true, 1, 27);
        stackGroup.set(1, new ItemStack(ItemHandler.EMPTY_TEST_TUBE));
        stackGroup.init(2, true, 27, 27);
        stackGroup.set(2, new ItemStack(ItemHandler.DNA_NUCLEOTIDES));
        stackGroup.init(3, false, 96, 4);
        stackGroup.set(3, outputs.get(0));
    }

	@Override
	public String getModName() {
		return RebornMod.NAME;
	}
}
