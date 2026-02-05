package net.vit.jurassicreborn.common.blocks.inventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.function.Predicate;

public class SerializableSingleFluidTank extends FluidTank implements INBTSerializable<CompoundTag> {
    public SerializableSingleFluidTank(int capacity) {
        super(capacity);
    }

    public SerializableSingleFluidTank(int capacity, Predicate<FluidStack> validator) {
        super(capacity, validator);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("Fluid", fluid.writeToNBT(new CompoundTag()));
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        fluid = FluidStack.loadFluidStackFromNBT(nbt.getCompound("Fluid"));
        this.onLoad();
    }

    protected void onLoad() {
    }
}
