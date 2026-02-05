package net.vit.jurassicreborn.common.jei.cleaningstation;


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

public class CleaningStationCategory implements IRecipeCategory<CleaningStationRecipeExtension> {

    public static final RecipeType<CleaningStationRecipeExtension> TYPE =
            RecipeType.create(JurassicReborn.MODID, "cleaning_station", CleaningStationRecipeExtension.class);

    private static final ResourceLocation TEX =
            JurassicReborn.resource("textures/gui/cleaning_station.png");

    private final IDrawable bg;
    private final Component title;
    private final IDrawableAnimated arrow;
    private final IDrawableAnimated water;
    private final IDrawable icon;


    public CleaningStationCategory(IGuiHelper gui) {
        this.bg = gui.createDrawable(TEX, 46, 16, 115, 54);
        this.title = ModBlocks.CLEANING_STATION.get().getName();
        this.icon = gui.createDrawableItemStack(new ItemStack(ModBlocks.CLEANING_STATION.get()));
        IDrawableStatic arrowDrawable = gui.createDrawable(TEX, 176, 14, 24, 16);
        this.arrow = gui.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);

        IDrawableStatic waterDrawable = gui.createDrawable(TEX, 176, 31, 6, 51);
        this.water = gui.createAnimatedDrawable(waterDrawable, 400, IDrawableAnimated.StartDirection.TOP, true);
    }

    @Override public RecipeType<CleaningStationRecipeExtension> getRecipeType() { return TYPE; }
    @Override public ResourceLocation getUid()               { return TYPE.getUid(); }
    @Override public Component getTitle()              { return title; }
    @Override public IDrawable getBackground()          { return bg; }
    @Override public IDrawable getIcon()                { return icon; }

    @Override
    @SuppressWarnings("removal")
    public Class<? extends CleaningStationRecipeExtension> getRecipeClass() {
        return CleaningStationRecipeExtension.class;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CleaningStationRecipeExtension recipe, IFocusGroup focuses) {
        recipe.setRecipe(builder, focuses);
    }

    public void draw(CleaningStationRecipeExtension recipe, PoseStack stack, double mouseX, double mouseY) {
        arrow.draw(stack, 33, 18);
        water.draw(stack, 0, 2);
    }
}