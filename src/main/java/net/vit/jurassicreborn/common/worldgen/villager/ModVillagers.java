package net.vit.jurassicreborn.common.worldgen.villager;

import com.google.common.collect.ImmutableSet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, JurassicReborn.MODID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(Registries.VILLAGER_PROFESSION, JurassicReborn.MODID);

    public static final DeferredHolder<PoiType, PoiType> CLEANING_STATION_POI = POI_TYPES.register("cleaning_station_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.CLEANING_STATION.get().getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final DeferredHolder<VillagerProfession, VillagerProfession> PALEONTOLOGIST = VILLAGER_PROFESSIONS.register("paleontologist",
            () -> new VillagerProfession("paleontologist", x -> x.is(CLEANING_STATION_POI.getKey()),
                    x -> x.is(CLEANING_STATION_POI.getKey()), ImmutableSet.of(), ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_MASON));

    public static final DeferredHolder<PoiType, PoiType> DNA_SEQUENCER_POI = POI_TYPES.register("dna_sequencer_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.DNA_SEQUENCER.get().getStateDefinition().getPossibleStates()),
                    1, 1));

    public static final DeferredHolder<VillagerProfession, VillagerProfession> GENETICIST = VILLAGER_PROFESSIONS.register("geneticist",
            () -> new VillagerProfession("geneticist", x -> x.is(DNA_SEQUENCER_POI.getKey()),
                    x -> x.is(DNA_SEQUENCER_POI.getKey()), ImmutableSet.of(), ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_LIBRARIAN));

    public static void registerPOIs() {
        try {
            Method method = ObfuscationReflectionHelper.findMethod(PoiType.class, "registerBlockStates", PoiType.class);
            method.invoke(null, CLEANING_STATION_POI.get());
            method.invoke(null, DNA_SEQUENCER_POI.get());
        } catch (InvocationTargetException | IllegalAccessException exception) {
            exception.printStackTrace();
        }
    }

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
