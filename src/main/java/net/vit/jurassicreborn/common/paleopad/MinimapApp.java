package net.vit.jurassicreborn.common.paleopad;

import net.minecraft.nbt.CompoundTag;

public class MinimapApp extends App {
    @Override
    public String getName() {
        return "Minimap";
    }

    @Override
    public void update() {
        // App logic here
    }

    @Override
    public void writeToNBT(CompoundTag nbt) {
        // Save any state here if needed
    }

    @Override
    public void readFromNBT(CompoundTag nbt) {
        // Load any state here if needed
    }

    @Override
    public void init() {
        // Initialization logic if needed
    }
}
