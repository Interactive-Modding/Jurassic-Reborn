package mod.reborn.server.plugin.jei.category.dnaextractor;

import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mod.reborn.RebornMod;
import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.plugin.jei.RebornJEIPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class DNAExtractorRecipeCategory implements IRecipeCategory<DNAExtractorRecipeWrapper>{
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(RebornMod.MODID, "textures/gui/dna_extractor.png");
	private final IDrawable background;
    private final String title;
    private final IDrawableAnimated arrow;
    private IDrawable icon;
    
    public DNAExtractorRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 49, 22, 98, 45, 10, 10, 10, 10);
        this.title = I18n.translateToLocal("tile.dna_extractor.name");
        IDrawableStatic arrowDrawable = guiHelper.createDrawable(TEXTURE, 176, 0, 22, 16);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(BlockHandler.DNA_EXTRACTOR));
    }
    
    @Override
    public void drawExtras(Minecraft minecraft) 
    {
    	this.arrow.draw(minecraft, 38, 25);
    }

	@Override
	public String getUid() 
	{
		return RebornJEIPlugin.DNA_EXTRACTOR;
	}

	@Override
	public String getTitle() 
	{
		return this.title;
	}

	@Override
	public String getModName() 
	{
		return RebornMod.NAME;
	}

	@Override
	public IDrawable getBackground() 
	{
		return this.background;
	}
	
	@Override
	public IDrawable getIcon() 
	{
		return icon;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, DNAExtractorRecipeWrapper recipeWrapper, IIngredients ingredients) 
	{
		IGuiItemStackGroup stackGroup = recipeLayout.getItemStacks();
        List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
        List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);
        stackGroup.init(0, true, 15, 13);
        stackGroup.set(0, inputs.get(0));
        stackGroup.init(1, true, 15, 34);
        stackGroup.set(1, inputs.get(1));
        stackGroup.init(2, false, 68, 15);
        stackGroup.set(2, outputs.get(0));
	}

}
