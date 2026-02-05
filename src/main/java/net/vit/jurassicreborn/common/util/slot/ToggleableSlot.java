package net.vit.jurassicreborn.common.util.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public abstract class ToggleableSlot extends Slot {
    boolean active = true;
    public ToggleableSlot(Container pContainer, int pSlot, int pX, int pY) {
        super(pContainer, pSlot, pX, pY);
    }
    @Override
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean isActive){
        this.active = isActive;
    }

    public Slot activeBuilder(boolean isActive){
        this.setActive(isActive);
        return this;
    }


}
