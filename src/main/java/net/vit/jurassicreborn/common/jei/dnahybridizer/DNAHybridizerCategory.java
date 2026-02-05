package net.vit.jurassicreborn.common.jei.dnahybridizer;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;

/** JEI category for the DNA Hybridizer. */
public class DNAHybridizerCategory implements IRecipeCategory<DNAHybridizerRecipeExtension> {

    public static final RecipeType<DNAHybridizerRecipeExtension> TYPE =
            RecipeType.create(JurassicReborn.MODID, "dna_hybridizer", DNAHybridizerRecipeExtension.class);

    private static final ResourceLocation TEX =
            JurassicReborn.resource("textures/gui/dna_hybridizer.png");

    private final IDrawable bg;
    private final Component title;
    private final IDrawable icon;

    public DNAHybridizerCategory(IGuiHelper gui) {
        this.bg = gui.createDrawable(TEX, 0, 0, 176, 80);
        this.title = Component.translatable("container.dna_hybridizer");
        this.icon = gui.createDrawableItemStack(new ItemStack(ModBlocks.DNA_COMBINER_HYBRIDIZER.get()));
    }

    @Override public RecipeType<DNAHybridizerRecipeExtension> getRecipeType() { return TYPE; }
    @Override public Component getTitle()              { return title; }
    @Override public IDrawable getBackground()          { return bg; }
    @Override public IDrawable getIcon()                { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DNAHybridizerRecipeExtension recipe, IFocusGroup focuses) {
        recipe.setRecipe(builder, focuses);
    }

    @Override
    public void draw(DNAHybridizerRecipeExtension recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        // nothing extra
    }
}
