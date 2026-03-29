package net.vit.jurassicreborn.common.recipes;

import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

public class FluidAndItemRecipeWrapper extends RecipeWrapper {
    private final FluidTank tank;
    public FluidAndItemRecipeWrapper(IItemHandlerModifiable inv, FluidTank tank) {
        super(inv);
        this.tank = tank;
    }

    public IFluidHandler getTank() {
        return tank;
    }
}
