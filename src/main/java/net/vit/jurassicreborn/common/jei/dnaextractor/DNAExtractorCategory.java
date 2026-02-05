package net.vit.jurassicreborn.common.jei.dnaextractor;

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
import net.vit.jurassicreborn.common.jei.dnaextractor.DNAExtractorRecipeExtension;

/** JEI category for the DNA Extractor. */
public class DNAExtractorCategory implements IRecipeCategory<DNAExtractorRecipeExtension> {

    public static final RecipeType<DNAExtractorRecipeExtension> TYPE =
            RecipeType.create(JurassicReborn.MODID, "dna_extractor", DNAExtractorRecipeExtension.class);

    private static final ResourceLocation TEX =
            JurassicReborn.resource("textures/gui/dna_extractor.png");

    private final IDrawable bg;
    private final Component title;
    private final IDrawableAnimated arrow;
    private final IDrawable icon;

    public DNAExtractorCategory(IGuiHelper gui) {
        this.bg = gui.createDrawable(TEX, 49, 22, 98, 45);
        this.title = ModBlocks.DNA_EXTRACTOR.get().getName();
        this.icon = gui.createDrawableItemStack(new ItemStack(ModBlocks.DNA_EXTRACTOR.get()));

        IDrawableStatic arrowDrawable = gui.createDrawable(TEX, 176, 0, 22, 16);
        this.arrow = gui.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override public RecipeType<DNAExtractorRecipeExtension> getRecipeType() { return TYPE; }
    @Override public Component getTitle()              { return title; }
    @Override public IDrawable getBackground()          { return bg; }
    @Override public IDrawable getIcon()                { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DNAExtractorRecipeExtension recipe, IFocusGroup focuses) {
        recipe.setRecipe(builder, focuses);
    }

    @Override
    public void draw(DNAExtractorRecipeExtension recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        arrow.draw(graphics, 28, 14);
    }
}
