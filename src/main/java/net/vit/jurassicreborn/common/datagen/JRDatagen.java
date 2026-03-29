package net.vit.jurassicreborn.common.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.vit.jurassicreborn.common.datagen.data.*;
import net.vit.jurassicreborn.common.plants.PlantHandler;
import net.vit.jurassicreborn.common.worldgen.JRWorldgenProvider;

public class JRDatagen {

    public static void gather(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput out = gen.getPackOutput();
        var lookup = event.getLookupProvider();
        ExistingFileHelper efh = event.getExistingFileHelper();
        PlantHandler.init();
        gen.addProvider(event.includeServer(), new JRWorldgenProvider(out, lookup));
        gen.addProvider(event.includeServer(), new JRLootTableProvider(out, event.getLookupProvider()));
        gen.addProvider(event.includeServer(), new JRRecipeProvider(out, lookup));
        gen.addProvider(event.includeClient(), new JRBlockstateProvider(out, efh));
        gen.addProvider(event.includeClient(), new JRItemModelProvider(out, efh));
        var blockTagsProvider = new JRBlockTagsProvider(out, lookup, efh);
        gen.addProvider(event.includeServer(), blockTagsProvider);
        gen.addProvider(event.includeServer(), new JRItemTagsProvider(out, lookup, blockTagsProvider, efh));
        gen.addProvider(event.includeServer(), new AdvancementHolder(out, lookup));
    }
}
