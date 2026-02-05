package net.vit.jurassicreborn.common.jei.embryonic;

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
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;

public class EmbryonicRecipeCategory implements IRecipeCategory<EmbryonicRecipeExtension> {

    public static final RecipeType<EmbryonicRecipeExtension> TYPE =
            RecipeType.create(JurassicReborn.MODID, "embryonic_machine", EmbryonicRecipeExtension.class);

    private static final ResourceLocation TEX =
            JurassicReborn.resource("textures/gui/embryonic_machine.png");

    private final IDrawable bg;
    private final Component title;
    private final IDrawableAnimated arrow;
    private final IDrawable icon;

    public EmbryonicRecipeCategory(IGuiHelper gui) {
        this.bg = gui.createDrawable(TEX, 23, 12, 131, 54);
        this.title = ModBlocks.EMBRYONIC_MACHINE.get().getName();
        this.icon = gui.createDrawableItemStack(new ItemStack(ModBlocks.EMBRYONIC_MACHINE.get()));

        IDrawableStatic arrowDrawable = gui.createDrawable(TEX, 176, 14, 24, 16);
        this.arrow = gui.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override public RecipeType<EmbryonicRecipeExtension> getRecipeType() { return TYPE; }
    @Override public Component getTitle()              { return title; }
    @Override public IDrawable getBackground()          { return bg; }
    @Override public IDrawable getIcon()                { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, EmbryonicRecipeExtension recipe, IFocusGroup focuses) {
        recipe.setRecipe(builder, focuses);
    }

    public void draw(EmbryonicRecipeExtension recipe, PoseStack gfx, double mouseX, double mouseY) {
        arrow.draw(gfx, 56, 23);
    }
}