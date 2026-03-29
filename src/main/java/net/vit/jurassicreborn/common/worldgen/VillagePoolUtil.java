package net.vit.jurassicreborn.common.worldgen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class VillagePoolUtil {

    private static final ResourceKey<StructureProcessorList> EMPTY_PROCESSOR_LIST_KEY =
            ResourceKey.create(
                    net.minecraft.core.registries.Registries.PROCESSOR_LIST,
                    ResourceLocation.fromNamespaceAndPath("minecraft", "empty")
            );

    // Reflection fields (cached)
    private static Field TEMPLATES_FIELD;
    private static Field RAW_TEMPLATES_FIELD;

    static {
        try {
            TEMPLATES_FIELD = StructureTemplatePool.class.getDeclaredField("templates");
            RAW_TEMPLATES_FIELD = StructureTemplatePool.class.getDeclaredField("rawTemplates");

            TEMPLATES_FIELD.setAccessible(true);
            RAW_TEMPLATES_FIELD.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access StructureTemplatePool internals", e);
        }
    }

    private VillagePoolUtil() {}

    @SuppressWarnings("unchecked")
    public static void addBuildingToPool(
            Registry<StructureTemplatePool> poolRegistry,
            Registry<StructureProcessorList> processorRegistry,
            ResourceLocation poolId,
            ResourceLocation structureId,
            int weight
    ) {
        StructureTemplatePool pool = poolRegistry.get(poolId);
        if (pool == null) return;

        Holder<StructureProcessorList> emptyProcessors =
                processorRegistry.getHolderOrThrow(EMPTY_PROCESSOR_LIST_KEY);

        StructurePoolElement element =
                SinglePoolElement
                        .legacy(structureId.toString(), emptyProcessors)
                        .apply(StructureTemplatePool.Projection.RIGID);

        try {
            // templates list
            List<StructurePoolElement> templates =
                    (List<StructurePoolElement>) TEMPLATES_FIELD.get(pool);

            for (int i = 0; i < weight; i++) {
                templates.add(element);
            }

            // rawTemplates list
            List<Pair<StructurePoolElement, Integer>> rawTemplates =
                    (List<Pair<StructurePoolElement, Integer>>) RAW_TEMPLATES_FIELD.get(pool);

            List<Pair<StructurePoolElement, Integer>> newRaw =
                    new ArrayList<>(rawTemplates);
            newRaw.add(Pair.of(element, weight));

            RAW_TEMPLATES_FIELD.set(pool, newRaw);

        } catch (Exception e) {
            throw new RuntimeException("Failed to inject village pool entry", e);
        }
    }
}
