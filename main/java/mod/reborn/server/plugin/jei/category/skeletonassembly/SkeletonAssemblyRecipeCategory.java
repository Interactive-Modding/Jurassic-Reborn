package mod.reborn.server.plugin.jei.category.skeletonassembly;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mod.reborn.RebornMod;
import mod.reborn.server.block.BlockHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class SkeletonAssemblyRecipeCategory implements IRecipeCategory<SkeletonAssemblyRecipeWrapper> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(RebornMod.MODID, "textures/gui/skeleton_assembler.png");

    private final IDrawable background;
    private final String title;

    public SkeletonAssemblyRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 15, 15, 146, 90);
        this.title = BlockHandler.SKELETON_ASSEMBLY.getLocalizedName();
    }

    @Override
    public String getUid() {
        return "rebornmod.skeleton_assembly";
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
    public void setRecipe(IRecipeLayout recipeLayout, SkeletonAssemblyRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup stackGroup = recipeLayout.getItemStacks();
        List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {
                int index = column + row * 5;
                stackGroup.init(index, true, column * 18, row * 18);
                if (index < inputs.size()) {
                    stackGroup.set(index, inputs.get(index));
                }
            }
        }
        stackGroup.init(24, false, 124, 36);
        stackGroup.set(24, ingredients.getOutputs(ItemStack.class).get(0));
    }

	@Override
	public String getModName() {
	    return RebornMod.NAME;
	}
}
