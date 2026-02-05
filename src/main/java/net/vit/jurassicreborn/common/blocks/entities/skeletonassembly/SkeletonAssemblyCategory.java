package net.vit.jurassicreborn.common.blocks.entities.skeletonassembly;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.jei.skeletonassembly.SkeletonAssemblyRecipeExtension;

public class SkeletonAssemblyCategory implements IRecipeCategory<SkeletonAssemblyRecipeExtension> {

    public static final RecipeType<SkeletonAssemblyRecipeExtension> TYPE =
            RecipeType.create(JurassicReborn.MODID, "skeleton_assembly",
                    SkeletonAssemblyRecipeExtension.class);

    private static final ResourceLocation TEX =
            new ResourceLocation(JurassicReborn.MODID,
                    "textures/gui/skeleton_assembler.png");

    private final IDrawable bg;
    private final Component title;

    public SkeletonAssemblyCategory(IGuiHelper gui) {
        this.bg    = gui.createDrawable(TEX, 15, 15, 146, 90);
        this.title = ModBlocks.SKELETON_ASSEMBLY.get()
                .getName();
    }

    @Override public RecipeType<SkeletonAssemblyRecipeExtension> getRecipeType() { return TYPE; }
    @Override public ResourceLocation getUid()               { return TYPE.getUid(); }
    @Override public Component getTitle()            { return title; }
    @Override public IDrawable getBackground()        { return bg; }
    @Override public IDrawable getIcon()              { return null; }

    @Override
    public Class<? extends SkeletonAssemblyRecipeExtension> getRecipeClass() {
        return SkeletonAssemblyRecipeExtension.class;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder,
                          SkeletonAssemblyRecipeExtension recipe,
                          IFocusGroup focuses) {
        recipe.setRecipe(builder, focuses);
    }
}