package mod.reborn.server.plugin.jei.util;

import mezz.jei.api.gui.IRecipeLayout;
import net.minecraft.item.ItemStack;

import java.util.function.Supplier;
import java.util.stream.Collectors;

public class RecipeLayoutOutputSupplier implements Supplier<ItemStack> {

    private final IRecipeLayout layout;
    private final int slot;
    private final boolean input;

    public RecipeLayoutOutputSupplier(IRecipeLayout layout, int slot, boolean input) {
        this.layout = layout;
        this.slot = slot;
        this.input = input;
    }

    @Override
    public ItemStack get() {
        return layout.getItemStacks()
                .getGuiIngredients()
                .values()
                .stream()
                .filter(in -> in.isInput() == input)
                .collect(Collectors.toList())
                .get(slot).getDisplayedIngredient();
    }
}
