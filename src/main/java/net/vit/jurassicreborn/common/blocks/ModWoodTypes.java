package net.vit.jurassicreborn.common.blocks;

import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.vit.jurassicreborn.JurassicReborn;

import java.util.ArrayList;
import java.util.List;

public class ModWoodTypes {
    //create the wood type for the ancient wood, there are a few.........
    public static WoodType araucaria = WoodType.register(new WoodType(JurassicReborn.MODID + ":araucaria", BlockSetType.OAK));
    public static WoodType calamites = WoodType.register(new WoodType(JurassicReborn.MODID + ":calamites", BlockSetType.OAK));
    public static WoodType ginkgo = WoodType.register(new WoodType(JurassicReborn.MODID + ":ginkgo", BlockSetType.OAK));
    public static WoodType phoenix = WoodType.register(new WoodType(JurassicReborn.MODID + ":phoenix", BlockSetType.OAK));
    public static WoodType psaronius = WoodType.register(new WoodType(JurassicReborn.MODID + ":psaronius", BlockSetType.OAK));
    public static WoodType magnolia = WoodType.register(new WoodType(JurassicReborn.MODID + ":magnolia", BlockSetType.OAK));
    public static ArrayList<WoodType> modWoodTypes = new ArrayList<>(List.of(araucaria, calamites, ginkgo, phoenix, psaronius, magnolia));
}
