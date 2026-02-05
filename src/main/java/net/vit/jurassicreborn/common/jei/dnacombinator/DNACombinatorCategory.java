package net.vit.jurassicreborn.common.jei.dnacombinator;

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

/** JEI category for the DNA Combinator. */
public class DNACombinatorCategory implements IRecipeCategory<DNACombinatorRecipeExtension> {

    public static final RecipeType<DNACombinatorRecipeExtension> TYPE =
            RecipeType.create(JurassicReborn.MODID, "dna_combinator", DNACombinatorRecipeExtension.class);

    private static final ResourceLocation TEX =
            JurassicReborn.resource("textures/gui/dna_combinator.png");

    private final IDrawable bg;
    private final Component title;
    private final IDrawable icon;

    public DNACombinatorCategory(IGuiHelper gui) {
        this.bg = gui.createDrawable(TEX, 0, 0, 176, 80);
        this.title = Component.translatable("container.dna_combinator");
        this.icon = gui.createDrawableItemStack(new ItemStack(ModBlocks.DNA_COMBINER_HYBRIDIZER.get()));
    }

    @Override public RecipeType<DNACombinatorRecipeExtension> getRecipeType() { return TYPE; }
    @Override public Component getTitle()              { return title; }
    @Override public IDrawable getBackground()          { return bg; }
    @Override public IDrawable getIcon()                { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DNACombinatorRecipeExtension recipe, IFocusGroup focuses) {
        recipe.setRecipe(builder, focuses);
    }

    @Override
    public void draw(DNACombinatorRecipeExtension recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        // no extra rendering
    }
}
