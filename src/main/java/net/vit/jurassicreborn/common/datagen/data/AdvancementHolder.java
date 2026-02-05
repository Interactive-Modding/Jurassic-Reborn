package net.vit.jurassicreborn.common.datagen.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AdvancementHolder extends AdvancementProvider {
        public AdvancementHolder(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries, List.of(new JRAdvancements()));
        }
}
