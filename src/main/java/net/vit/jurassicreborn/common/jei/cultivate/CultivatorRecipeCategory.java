package net.vit.jurassicreborn.common.jei.cultivate;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.cultivator.CultivatorBlockEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.items.ModItems;

import java.util.List;
import java.util.function.IntSupplier;

/** JEI category for the cultivator. */
public class CultivatorRecipeCategory implements IRecipeCategory<CultivatorRecipeExtension> {
    public static final RecipeType<CultivatorRecipeExtension> TYPE =
            RecipeType.create(JurassicReborn.MODID, "cultivator", CultivatorRecipeExtension.class);

    private static final ResourceLocation TEX =
            JurassicReborn.resource("textures/gui/cultivator_jei.png");

    private final IDrawable bg;
    private final Component title;
    private final IDrawable icon;

    private List<NutrientBar> nutrientBars = List.of();

    public CultivatorRecipeCategory(IGuiHelper gui) {
        this.bg = gui.createDrawable(TEX, 0, 0, 169, 90);
        this.title = ModBlocks.CULTIVATE_BOTTOM.get().getName();
        this.icon = gui.createDrawableItemStack(new ItemStack(ModBlocks.CULTIVATE_BOTTOM.get()));
    }

    @Override public RecipeType<CultivatorRecipeExtension> getRecipeType() { return TYPE; }
    @Override public Component getTitle()              { return title; }
    @Override public IDrawable getBackground()          { return bg; }
    @Override public IDrawable getIcon()                { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CultivatorRecipeExtension recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 41, 7)
                .addItemStack(new ItemStack(ModItems.SYRINGES.get(recipe.getDinosaur()).get(), 1));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 106, 7)
                .addItemStack(new ItemStack(ModItems.hatchedDinoEggs.get(recipe.getDinosaur()).get(), 1));

        Dinosaur dino = recipe.getDinosaur();
        nutrientBars = Lists.newArrayList(
                new NutrientBar(dino::getProximates, 0),
                new NutrientBar(dino::getMinerals, 1),
                new NutrientBar(dino::getVitamins, 2),
                new NutrientBar(dino::getLipids, 3)
        );
    }

    public void draw(CultivatorRecipeExtension recipe, PoseStack poseStack, double mouseX, double mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEX);
        for (NutrientBar bar : nutrientBars) bar.render(poseStack);
    }

    private static class NutrientBar {
        private final IntSupplier supplier;
        private final int id;
        NutrientBar(IntSupplier supplier, int id) {
            this.supplier = supplier;
            this.id = id;
        }
        void render(PoseStack poseStack) {
            int value = supplier.getAsInt();
            // Example: Drawing a portion of the bar texture
            GuiComponent.blit(
                    poseStack,
                    9, 30 + id * 16, // x, y on screen
                    0, 91 + id * 9,  // u, v in texture
                    value * 150 / CultivatorBlockEntity.MAX_NUTRIENTS, // width of bar (portion filled)
                    9,               // height of bar
                    256, 256
            );
        }
    }
}
