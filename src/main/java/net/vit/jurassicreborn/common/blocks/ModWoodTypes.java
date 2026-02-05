package net.vit.jurassicreborn.common.blocks;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

import java.util.ArrayList;
import java.util.List;

public class ModWoodTypes {
    //create the wood type for the ancient wood, there are a few.........
    public static WoodType araucaria = WoodType.register(new WoodType("araucaria", BlockSetType.OAK));
    public static WoodType calamites = WoodType.register(new WoodType("calamites", BlockSetType.OAK));
    public static WoodType ginkgo = WoodType.register(new WoodType("ginkgo", BlockSetType.OAK));
    public static WoodType phoenix = WoodType.register(new WoodType("phoenix", BlockSetType.OAK));
    public static WoodType psaronius = WoodType.register(new WoodType("psaronius", BlockSetType.OAK));
    public static WoodType magnolia = WoodType.register(new WoodType("magnolia", BlockSetType.OAK));
    public static ArrayList<WoodType> modWoodTypes = new ArrayList<>(List.of(araucaria, calamites, ginkgo, phoenix, psaronius, magnolia));
}
