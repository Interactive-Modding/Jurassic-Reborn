package net.vit.jurassicreborn.common.paleopad;

import net.minecraft.nbt.CompoundTag;

public abstract class App {
    private boolean previouslyOpened;

    public abstract String getName();

    public abstract void update();

    public void readAppFromNBT(CompoundTag nbt) {
        this.readFromNBT(nbt);
        previouslyOpened = nbt.getBoolean("PreviouslyOpened");
    }

    public void writeAppToNBT(CompoundTag nbt) {
        this.writeToNBT(nbt);
        nbt.putBoolean("PreviouslyOpened", previouslyOpened);
    }

    public abstract void writeToNBT(CompoundTag nbt);

    public abstract void readFromNBT(CompoundTag nbt);

    public abstract void init();

    public void open() {
        previouslyOpened = true;
    }

    public boolean hasBeenPreviouslyOpened() {
        return previouslyOpened;
    }
}
