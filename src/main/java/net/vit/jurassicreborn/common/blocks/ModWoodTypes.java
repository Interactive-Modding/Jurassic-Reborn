package net.vit.jurassicreborn.common.blocks;

import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.ArrayList;
import java.util.List;

public class ModWoodTypes {
    //create the wood type for the ancient wood, there are a few.........
    public static WoodType araucaria = WoodType.register(WoodType.create("araucaria"));
    public static WoodType calamites = WoodType.register(WoodType.create("calamites"));
    public static WoodType ginkgo = WoodType.register(WoodType.create("ginkgo"));
    public static WoodType phoenix = WoodType.register(WoodType.create("phoenix"));
    public static WoodType psaronius = WoodType.register(WoodType.create("psaronius"));
    public static WoodType magnolia = WoodType.register(WoodType.create("magnolia"));
    public static ArrayList<WoodType> modWoodTypes = new ArrayList<>(List.of(araucaria, calamites, ginkgo, phoenix, psaronius, magnolia));
}
