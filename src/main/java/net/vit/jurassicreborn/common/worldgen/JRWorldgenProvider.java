package net.vit.jurassicreborn.common.worldgen;

import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.worldgen.ModConfiguredFeatures;
import net.vit.jurassicreborn.common.worldgen.ModPlacedFeatures;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;

public class JRWorldgenProvider extends DatapackBuiltinEntriesProvider {
    public JRWorldgenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup,
                new RegistrySetBuilder()
                        .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
                        .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap),
                Set.of(JurassicReborn.MODID));
    }
}
