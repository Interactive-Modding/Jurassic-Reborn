package net.vit.jurassicreborn.common.jei.dnahybridizer;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Hybrid;
import net.vit.jurassicreborn.common.genetics.DinoDNA;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.genetics.StorageDiscItem;

import java.util.ArrayList;
import java.util.List;

/** JEI recipe extension for the DNA Hybridizer. */
public class DNAHybridizerRecipeExtension implements IRecipeCategoryExtension {
    private static final int[] X = {10,30,50,70,90,110,130,150};
    private final List<ItemStack> inputs = new ArrayList<>();
    private final ItemStack output;

    public DNAHybridizerRecipeExtension(Dinosaur hybrid) {
        this.output = createDisc(hybrid);
        if (hybrid instanceof Hybrid h) {
            for (Class<?> clazz : h.getDinosaurs()) {
                inputs.add(createDisc(findDinosaur(clazz)));
            }
        }
    }

    private static Dinosaur findDinosaur(Class<?> clazz) {
        for (Dinosaur d : Dinosaur.DINOS) {
            if (clazz.isInstance(d)) return d;
        }
        return Dinosaur.EMPTY;
    }

    private static ItemStack createDisc(Dinosaur dino) {
        ItemStack stack = new ItemStack(ModItems.STORAGE_DISC.get());
        CompoundTag tag = new CompoundTag();
        new DinoDNA(dino, 100, "").writeToNBT(tag);
        stack.setTag(tag);
        StorageDiscItem.applyCustomModelData(stack);
        return stack;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, IFocusGroup focuses) {
        for (int i = 0; i < inputs.size() && i < X.length; i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, X[i], 17).addItemStack(inputs.get(i));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 56).addItemStack(output);
    }
}
