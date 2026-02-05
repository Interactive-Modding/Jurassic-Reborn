//package net.vit.jurassicreborn.common.blocks.entities.skeletonassembly;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//
//
//import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
//import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
//import mezz.jei.api.recipe.IFocusGroup;
//import mezz.jei.api.recipe.RecipeIngredientRole;
//import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.vit.jurassicreborn.common.items.Fossils.FossilItem;
//import net.vit.jurassicreborn.common.items.ModItems;
//
///**
// * 1.19.2 port of the old {@code SkeletonAssemblyRecipeWrapper}.
// * JEI 11 no longer uses IRecipeWrapper – implement {@link IRecipeCategoryExtension} instead.
// */
//public class SkeletonAssemblyRecipeExtension implements IRecipeCategoryExtension {
//
//    private static final int SLOT_SIZE = 18;        // JEI’s standard slot = 18 px
//    private final SkeletonInput input;
//
//    public SkeletonAssemblyRecipeExtension(SkeletonInput input) {
//        this.input = input;
//    }
//
//    /* ------------------------------------------------------------------------ */
//    /*  IRecipeCategoryExtension                                                */
//    /* ------------------------------------------------------------------------ */
//
//    @Override
//    public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
//        String[][] pattern = input.dinosaur.getRecipe();
//        Map<String, FossilItem> fossils = input.fresh ? ModItems.FRESH_BONES : ModItems.BONES;
//        int dinosaurId = EntityHandler.getDinosaurId(input.dinosaur);
//
//        /* ---- inputs: 5-wide grid, any height -------------------------------- */
//        for (int row = 0; row < pattern.length; row++) {
//            String[] line = pattern[row];
//
//            for (int col = 0; col < 5; col++) {
//                IRecipeSlotBuilder slot = builder
//                        .addSlot(RecipeIngredientRole.INPUT, col * SLOT_SIZE, row * SLOT_SIZE);
//
//                if (col < line.length && !line[col].isEmpty()) {
//                    slot.addItemStack(new ItemStack(fossils.get(line[col])));
//                }
//            }
//        }
//
//        /* ---- output block (stick it on the right, vertically centered) ------ */
//        int outY = (pattern.length * SLOT_SIZE - SLOT_SIZE) / 2;
//        builder.addSlot(RecipeIngredientRole.OUTPUT, 5 * SLOT_SIZE + 4, outY)    // +4 px for a little gap
//                .addItemStack(DisplayBlockItem.getStackWithVariant(
//                        dinosaurId,
//                        input.fresh ? 2 : 1,
//                        true));
//    }
//
//    // public List<Component> getTooltipStrings(double mouseX, double mouseY) { … }
//    // public void drawInfo(GuiGraphics gfx, int width, int height, int mouseX, int mouseY) { … }
//}
