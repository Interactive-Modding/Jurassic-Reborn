package net.vit.jurassicreborn.common.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.vit.jurassicreborn.common.datagen.data.*;

import java.util.List;

public class JRDatagen {

    public static void gather(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
            generator.addProvider(event.includeServer(), new JRLootTableProvider(generator));
            generator.addProvider(event.includeServer(),new JRRecipeProvider(generator));
            generator.addProvider(event.includeClient(),new JRBlockstateProvider(generator,existingFileHelper));
            generator.addProvider(event.includeClient(),new JRItemModelProvider(generator,existingFileHelper));
            BlockTagsProvider blockTagsProvider = new JRBlockTagsProvider(generator,existingFileHelper);
            generator.addProvider(event.includeServer(),blockTagsProvider);
            generator.addProvider(event.includeServer(),new JRItemTagsProvider(generator,blockTagsProvider,existingFileHelper));
            generator.addProvider(event.includeServer(), new AdvancementHolder(generator, existingFileHelper));
    }
}
