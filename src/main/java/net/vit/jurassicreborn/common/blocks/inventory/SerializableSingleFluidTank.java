package net.vit.jurassicreborn.common.blocks.inventory;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

import java.util.function.Predicate;

public class SerializableSingleFluidTank extends FluidTank {

    public SerializableSingleFluidTank(int capacity) {
        super(capacity);
    }

    public SerializableSingleFluidTank(int capacity, Predicate<FluidStack> validator) {
        super(capacity, validator);
    }

    /**
     * Convenience wrapper (optional)
     */
    public CompoundTag save(HolderLookup.Provider provider) {
        return this.writeToNBT(provider, new CompoundTag());
    }

    /**
     * Convenience wrapper (optional)
     */
    public void load(HolderLookup.Provider provider, CompoundTag tag) {
        this.readFromNBT(provider, tag);
    }
}
