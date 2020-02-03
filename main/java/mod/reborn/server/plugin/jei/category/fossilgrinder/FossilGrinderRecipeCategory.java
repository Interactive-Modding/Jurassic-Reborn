package mod.reborn.server.plugin.jei.category.fossilgrinder;

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
import mod.reborn.server.plugin.jei.util.RecipeLayoutOutputSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;
import java.util.List;

@SideOnly(Side.CLIENT)
public class FossilGrinderRecipeCategory implements IRecipeCategory<FossilGrinderRecipeWrapper> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(RebornMod.MODID, "textures/gui/fossil_grinder.png");

    private final IDrawable background;
    private final String title;

    private final IDrawableAnimated arrow;

    private RecipeLayoutOutputSupplier outPutSupplier = null;

    public FossilGrinderRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 18, 21, 147, 43);
        this.title = BlockHandler.FOSSIL_GRINDER.getLocalizedName();

        IDrawableStatic arrowDrawable = guiHelper.createDrawable(TEXTURE, 176, 14, 24, 16);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        this.arrow.draw(minecraft, 61, 14);
        ItemStack stack = outPutSupplier.get();
        if(stack != null && !stack.isEmpty()) {
            float value = stack.getOrCreateSubCompound("jei_rendering_info").getFloat("Chance");
            String text = value + "%";
            if( value >= 100.0F ) {
                int width = minecraft.fontRenderer.getStringWidth(text);
                minecraft.fontRenderer.drawString(text, 100 - width / 2, 42, Color.GRAY.getRGB());
            }
        }
    }

    @Override
    public String getUid() {
        return "rebornmod.fossil_grinder";
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
    public void setRecipe(IRecipeLayout recipeLayout, FossilGrinderRecipeWrapper recipeWrapper, IIngredients ingredients) {
        outPutSupplier = new RecipeLayoutOutputSupplier(recipeLayout, 0, false);

        IGuiItemStackGroup stackGroup = recipeLayout.getItemStacks();
        List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
        List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);
        for (int row = 0; row < 2; row++) {
            for (int column = 0; column < 3; column++) {
                int index = column + row * 2;
                int outputIndex = index + 6;
                stackGroup.init(index, true, 4 + column * 18, 4 + row * 18);
                stackGroup.init(outputIndex, false, 89 + column * 18, 4 + row * 18);
                if (index < inputs.size()) {
                    stackGroup.set(index, inputs.get(index));
                }
                if (index < outputs.size()) {
                    stackGroup.set(outputIndex, outputs.get(index));
                }
            }
        }
    }

	@Override
	public String getModName() {
	    return RebornMod.NAME;
	}
}
