package net.vit.jurassicreborn.common.jei.fossilgrinder;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;

public class FossilGrinderCategory implements IRecipeCategory<FossilGrinderRecipeExtension> {

    public static final RecipeType<FossilGrinderRecipeExtension> TYPE =
            RecipeType.create(JurassicReborn.MODID, "fossil_grinder", FossilGrinderRecipeExtension.class);

    private static final ResourceLocation TEX =
            JurassicReborn.resource("textures/gui/fossil_grinder.png");

    private final IDrawable bg;
    private final Component title;
    private final IDrawableAnimated arrow;
    private final IDrawable icon;

    public FossilGrinderCategory(IGuiHelper gui) {
        this.bg = gui.createDrawable(TEX, 18, 21, 147, 43);
        this.title = ModBlocks.FOSSIL_GRINDER.get().getName();
        this.icon = gui.createDrawableItemStack(new ItemStack(ModBlocks.FOSSIL_GRINDER.get()));
        IDrawableStatic arrowDrawable = gui.createDrawable(TEX, 176, 14, 24, 16);
        this.arrow = gui.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override public RecipeType<FossilGrinderRecipeExtension> getRecipeType() { return TYPE; }
    @Override public Component getTitle()              { return title; }
    @Override public IDrawable getBackground()          { return bg; }
    @Override public IDrawable getIcon()                { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FossilGrinderRecipeExtension recipe, IFocusGroup focuses) {
        recipe.setRecipe(builder, focuses);
    }

    @Override
    public void draw(FossilGrinderRecipeExtension recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        arrow.draw(graphics, 61, 14);
    }
}
