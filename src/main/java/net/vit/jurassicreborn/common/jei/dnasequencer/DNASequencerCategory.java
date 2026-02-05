package net.vit.jurassicreborn.common.jei.dnasequencer;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.minecraft.world.item.ItemStack;

/** JEI category for the DNA Sequencer. */
public class DNASequencerCategory implements IRecipeCategory<DNASequencerRecipeExtension> {

    public static final RecipeType<DNASequencerRecipeExtension> TYPE =
            RecipeType.create(JurassicReborn.MODID, "dna_sequencer", DNASequencerRecipeExtension.class);

    private static final ResourceLocation TEX =
            JurassicReborn.resource("textures/gui/dna_sequencer.png");

    private final IDrawable bg;
    private final Component title;
    private final IDrawableAnimated arrow;
    private final IDrawable icon;

    public DNASequencerCategory(IGuiHelper gui) {
        this.bg = gui.createDrawable(TEX, 33, 3, 107, 32);
        this.title = ModBlocks.DNA_SEQUENCER.get().getName();
        this.icon = gui.createDrawableItemStack(new ItemStack(ModBlocks.DNA_SEQUENCER.get()));

        IDrawableStatic arrowDrawable = gui.createDrawable(TEX, 176, 0, 22, 6);
        this.arrow = gui.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override public RecipeType<DNASequencerRecipeExtension> getRecipeType() { return TYPE; }
    @Override public ResourceLocation getUid()               { return TYPE.getUid(); }
    @Override public Component getTitle()              { return title; }
    @Override public IDrawable getBackground()          { return bg; }
    @Override public IDrawable getIcon()                { return icon; }

    @Override
    @SuppressWarnings("removal")
    public Class<? extends DNASequencerRecipeExtension> getRecipeClass() {
        return DNASequencerRecipeExtension.class;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DNASequencerRecipeExtension recipe, IFocusGroup focuses) {
        recipe.setRecipe(builder, focuses);
    }

    public void draw(DNASequencerRecipeExtension recipe, PoseStack gfx, double mouseX, double mouseY) {
        arrow.draw(gfx, 54, 18);
    }
}
