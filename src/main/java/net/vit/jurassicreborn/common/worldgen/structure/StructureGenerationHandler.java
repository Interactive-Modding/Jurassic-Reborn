package net.vit.jurassicreborn.common.worldgen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.server.level.ServerLevel;
import java.util.*;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Simplified structure generation handler for 1.19.2.
 * Actual biome registration is left to the mod implementation.
 */
public enum StructureGenerationHandler {
    INSTANCE;

    private static final Map<Biome, List<GeneratorEntry>> GENERATORS = new HashMap<>();
    private static final List<GeneratorEntry> UNIVERSAL_GENERATORS = new ArrayList<>();

    public void generate(ServerLevel level, Random random, BlockPos pos, Biome biome, StructureUtils.StructureData data) {
        boolean universalGenerated = false;
        for (GeneratorEntry entry : UNIVERSAL_GENERATORS) {
            if (entry.predicate.test(level, pos, random) && entry.configPredicate.test(data)) {
                entry.generatorFunction.apply(random).generate(level, random, pos);
                universalGenerated = true;
            }
        }
        if (!universalGenerated) {
            List<GeneratorEntry> list = GENERATORS.get(biome);
            if (list != null && !list.isEmpty()) {
                GeneratorEntry entry = list.get(random.nextInt(list.size()));
                if (entry.predicate.test(level, pos, random) && entry.configPredicate.test(data)) {
                    entry.generatorFunction.apply(random).generate(level, random, pos);
                }
            }
        }
    }

    public static void registerGenerator(Function<Random, StructureGenerator> generatorFunction, Predicate<StructureUtils.StructureData> configPredicate, int weight, Biome... validBiomes) {
        registerGenerator(generatorFunction, configPredicate, (level, pos, random) -> random.nextInt(weight) == 0, validBiomes);
    }

    public static void registerGenerator(Function<Random, StructureGenerator> generatorFunction, Predicate<StructureUtils.StructureData> configPredicate, StructurePredicate predicate, Biome... validBiomes) {
        GeneratorEntry entry = new GeneratorEntry(generatorFunction, configPredicate, predicate);
        if (validBiomes.length == 0) {
            UNIVERSAL_GENERATORS.add(entry);
        } else {
            for (Biome biome : validBiomes) {
                GENERATORS.computeIfAbsent(biome, b -> new ArrayList<>()).add(entry);
            }
        }
    }

    private static class GeneratorEntry {
        private final Function<Random, StructureGenerator> generatorFunction;
        private final Predicate<StructureUtils.StructureData> configPredicate;
        private final StructurePredicate predicate;

        GeneratorEntry(Function<Random, StructureGenerator> generatorFunction, Predicate<StructureUtils.StructureData> configPredicate, StructurePredicate predicate) {
            this.generatorFunction = generatorFunction;
            this.configPredicate = configPredicate;
            this.predicate = predicate;
        }
    }

    @FunctionalInterface
    public interface StructurePredicate {
        boolean test(ServerLevel level, BlockPos pos, Random random);
    }
}
