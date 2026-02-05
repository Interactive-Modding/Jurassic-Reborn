package net.vit.jurassicreborn.common.jei.dnasynthesizer;

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
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.minecraft.world.item.ItemStack;

/** JEI category for the DNA Synthesizer. */
public class DNASynthesizerCategory implements IRecipeCategory<DNASynthesizerRecipeExtension> {

    public static final RecipeType<DNASynthesizerRecipeExtension> TYPE =
            RecipeType.create(JurassicReborn.MODID, "dna_synthesizer", DNASynthesizerRecipeExtension.class);

    private static final ResourceLocation TEX =
            JurassicReborn.resource("textures/gui/dna_synthesizer.png");

    private final IDrawable bg;
    private final Component title;
    private final IDrawableAnimated arrow;
    private final IDrawable icon;

    public DNASynthesizerCategory(IGuiHelper gui) {
        this.bg = gui.createDrawable(TEX, 22, 21, 132, 45);
        this.title = ModBlocks.DNA_SYNTHESIZER.get().getName();
        this.icon = gui.createDrawableItemStack(new ItemStack(ModBlocks.DNA_SYNTHESIZER.get()));

        IDrawableStatic arrowDrawable = gui.createDrawable(TEX, 176, 14, 24, 16);
        this.arrow = gui.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override public RecipeType<DNASynthesizerRecipeExtension> getRecipeType() { return TYPE; }
    @Override public Component getTitle()              { return title; }
    @Override public IDrawable getBackground()          { return bg; }
    @Override public IDrawable getIcon()                { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DNASynthesizerRecipeExtension recipe, IFocusGroup focuses) {
        recipe.setRecipe(builder, focuses);
    }

    @Override
    public void draw(DNASynthesizerRecipeExtension recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        arrow.draw(graphics, 57, 13);
    }
}