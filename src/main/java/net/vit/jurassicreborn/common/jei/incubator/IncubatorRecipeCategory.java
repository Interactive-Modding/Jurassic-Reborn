package net.vit.jurassicreborn.common.jei.incubator;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;

/** JEI category for the incubator. */
public class IncubatorRecipeCategory implements IRecipeCategory<IncubatorRecipeExtension> {

    public static final RecipeType<IncubatorRecipeExtension> TYPE =
            RecipeType.create(JurassicReborn.MODID, "incubator", IncubatorRecipeExtension.class);

    private static final ResourceLocation TEX =
            JurassicReborn.resource("textures/gui/incubator.png");

    private final IDrawable bg;
    private final Component title;
    private final IDrawable icon;

    public IncubatorRecipeCategory(IGuiHelper gui) {
        this.bg = gui.createDrawable(TEX, 31, 13, 112, 53);
        this.title = new TranslatableComponent("container.incubator");
        this.icon = gui.createDrawableItemStack(new ItemStack(ModBlocks.INCUBATOR.get()));
    }

    @Override public RecipeType<IncubatorRecipeExtension> getRecipeType() { return TYPE; }
    @Override public ResourceLocation getUid()               { return TYPE.getUid(); }
    @Override public Component getTitle()              { return title; }
    @Override public IDrawable getBackground()          { return bg; }
    @Override public IDrawable getIcon()                { return icon; }

    @Override
    @SuppressWarnings("removal")
    public Class<? extends IncubatorRecipeExtension> getRecipeClass() {
        return IncubatorRecipeExtension.class;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IncubatorRecipeExtension recipe, IFocusGroup focuses) {
        recipe.setRecipe(builder, focuses);
    }

    public void draw(IncubatorRecipeExtension recipe, PoseStack gfx, double mouseX, double mouseY) {
    }
}