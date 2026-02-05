package net.vit.jurassicreborn.common.blocks.parkBlocks;

import net.minecraft.util.StringRepresentable;

public enum BenchPart implements StringRepresentable {
    LEFT("left"),
    RIGHT("right");

    private final String name;
    BenchPart(String name) { this.name = name; }
    @Override public String getSerializedName() { return name; }
    @Override public String toString() { return name; }
}
