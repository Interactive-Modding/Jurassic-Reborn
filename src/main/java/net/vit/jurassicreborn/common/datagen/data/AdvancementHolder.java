package net.vit.jurassicreborn.common.datagen.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

public class AdvancementHolder extends AdvancementProvider {
        public AdvancementHolder(DataGenerator generatorIn, ExistingFileHelper fileHelperIn) {
            super(generatorIn, fileHelperIn);
        }
        @Override
        protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
            new JRAdvancements().accept(consumer);
        }
    }
