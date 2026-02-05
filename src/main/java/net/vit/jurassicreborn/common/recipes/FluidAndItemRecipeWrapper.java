package net.vit.jurassicreborn.common.recipes;

import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

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
