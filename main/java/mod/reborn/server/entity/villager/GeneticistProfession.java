package mod.reborn.server.entity.villager;

import mod.reborn.RebornMod;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class GeneticistProfession extends VillagerRegistry.VillagerProfession {
    public GeneticistProfession() {
        super(RebornMod.MODID + ":geneticist", RebornMod.MODID + ":textures/entities/villager/geneticist.png", RebornMod.MODID + ":textures/entities/villager/geneticist_zombie.png");
        new GeneticistCareer(this);
    }
}