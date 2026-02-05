package net.vit.jurassicreborn.common.jei.bugcrate;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;

/** JEI category for the Bug Crate. */
public class BugCrateCategory implements IRecipeCategory<BugCrateRecipeExtension> {

    public static final RecipeType<BugCrateRecipeExtension> TYPE =
            RecipeType.create(JurassicReborn.MODID, "bug_crate", BugCrateRecipeExtension.class);

    private static final ResourceLocation TEX =
            JurassicReborn.resource("textures/gui/bug_crate.png");

    private final IDrawable bg;
    private final Component title;
    private final IDrawable icon;

    public BugCrateCategory(IGuiHelper gui) {
        this.bg = gui.createDrawable(TEX, 0, 0, 176, 80);
        this.title = new TranslatableComponent("container.bug_crate");
        this.icon = gui.createDrawableItemStack(new ItemStack(ModBlocks.BUG_CRATE.get()));
    }

    @Override public RecipeType<BugCrateRecipeExtension> getRecipeType() { return TYPE; }
    @Override public ResourceLocation getUid()               { return TYPE.getUid(); }
    @Override public Component getTitle()              { return title; }
    @Override public IDrawable getBackground()          { return bg; }
    @Override public IDrawable getIcon()                { return icon; }

    @Override
    @SuppressWarnings("removal")
    public Class<? extends BugCrateRecipeExtension> getRecipeClass() {
        return BugCrateRecipeExtension.class;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BugCrateRecipeExtension recipe, IFocusGroup focuses) {
        recipe.setRecipe(builder, focuses);
    }

    public void draw(BugCrateRecipeExtension recipe, PoseStack poseStack, double mouseX, double mouseY) {
        // nothing extra
    }
}
