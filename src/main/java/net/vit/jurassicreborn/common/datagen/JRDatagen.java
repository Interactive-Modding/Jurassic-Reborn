package net.vit.jurassicreborn.common.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.vit.jurassicreborn.common.datagen.data.*;

import java.util.List;

public class JRDatagen {

    public static void gather(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeServer()) {
            generator.addProvider(new JRLootTableProvider(generator));
            generator.addProvider(new JRRecipeProvider(generator));
            BlockTagsProvider blockTagsProvider = new JRBlockTagsProvider(generator, existingFileHelper);
            generator.addProvider(blockTagsProvider);
            generator.addProvider(new JRItemTagsProvider(generator, blockTagsProvider, existingFileHelper));
            generator.addProvider(new AdvancementHolder(generator, existingFileHelper));
        }

        if (event.includeClient()) {
            generator.addProvider(new JRBlockstateProvider(generator, existingFileHelper));
            generator.addProvider(new JRItemModelProvider(generator, existingFileHelper));
        }
    }
}
