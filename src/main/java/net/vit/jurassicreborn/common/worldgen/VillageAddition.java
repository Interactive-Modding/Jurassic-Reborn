package net.vit.jurassicreborn.common.worldgen;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.vit.jurassicreborn.JurassicReborn;

@EventBusSubscriber(modid = JurassicReborn.MODID)
public final class VillageAddition {

    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        inject(event.getServer());
    }

    private static void inject(MinecraftServer server) {
        Registry<StructureTemplatePool> pools =
                server.registryAccess().registry(Registries.TEMPLATE_POOL).orElseThrow();

        Registry<StructureProcessorList> processors =
                server.registryAccess().registry(Registries.PROCESSOR_LIST).orElseThrow();

        add(pools, processors, "minecraft:village/plains/houses", "jurassicreborn:plains_geneticist_house", 3);
        add(pools, processors, "minecraft:village/plains/houses", "jurassicreborn:plains_paleontologist_house", 1);
        add(pools, processors, "minecraft:village/desert/houses", "jurassicreborn:desert_geneticist_house", 3);
        add(pools, processors, "minecraft:village/desert/houses", "jurassicreborn:desert_paleontologist_house", 3);
        add(pools, processors, "minecraft:village/savanna/houses", "jurassicreborn:savanna_geneticist_house", 3);
        add(pools, processors, "minecraft:village/savanna/houses", "jurassicreborn:savanna_paleontologist_house", 3);
        add(pools, processors, "minecraft:village/snowy/houses", "jurassicreborn:snowy_geneticist_house", 3);
        add(pools, processors, "minecraft:village/snowy/houses", "jurassicreborn:snowy_paleontologist_house", 3);
        add(pools, processors, "minecraft:village/taiga/houses", "jurassicreborn:taiga_geneticist_house", 3);
        add(pools, processors, "minecraft:village/taiga/houses", "jurassicreborn:taiga_paleontologist_house", 3);
    }

    private static void add(
            Registry<StructureTemplatePool> pools,
            Registry<StructureProcessorList> processors,
            String poolId,
            String structureId,
            int weight
    ) {
        VillagePoolUtil.addBuildingToPool(
                pools,
                processors,
                ResourceLocation.parse(poolId),
                ResourceLocation.parse(structureId),
                weight
        );
    }
}
