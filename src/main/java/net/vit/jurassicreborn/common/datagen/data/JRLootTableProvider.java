package net.vit.jurassicreborn.common.datagen.data;

import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableProvider.SubProviderEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.vit.jurassicreborn.common.datagen.JRBlockLoot;
import net.vit.jurassicreborn.common.datagen.JREntityLoot;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class JRLootTableProvider extends LootTableProvider {
    public JRLootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(JREntityLoot::new, LootContextParamSets.ENTITY),
                new SubProviderEntry(JRBlockLoot::new, LootContextParamSets.BLOCK)
        ));
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationtracker) {
    }
}
