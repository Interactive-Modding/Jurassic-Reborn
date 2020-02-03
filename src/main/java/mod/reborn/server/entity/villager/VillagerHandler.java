package mod.reborn.server.entity.villager;

import mod.reborn.RebornMod;
import mod.reborn.server.world.structure.GeneticistVillagerHouse;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

@Mod.EventBusSubscriber(modid = RebornMod.MODID)
public class VillagerHandler {
    public static final GeneticistProfession GENETICIST = new GeneticistProfession();

    public static void init() {
        VillagerRegistry.instance().registerVillageCreationHandler(new GeneticistVillagerHouse.CreationHandler());
        MapGenStructureIO.registerStructureComponent(GeneticistVillagerHouse.class, "GeneticistHouse");
    }

    @SubscribeEvent
    public static void onReigster(RegistryEvent.Register<VillagerRegistry.VillagerProfession> event) {
        event.getRegistry().register(GENETICIST);
    }
}