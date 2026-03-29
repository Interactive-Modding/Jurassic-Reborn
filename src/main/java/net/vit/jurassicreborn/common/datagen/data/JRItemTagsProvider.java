package net.vit.jurassicreborn.common.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.vit.jurassicreborn.JurassicReborn;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class JRItemTagsProvider extends ItemTagsProvider {

    public JRItemTagsProvider(PackOutput output,
                              CompletableFuture<HolderLookup.Provider> lookupProvider,
                              BlockTagsProvider blockTagsProvider,
                              @Nullable ExistingFileHelper existingFileHelper) {
        // Forge ItemTagsProvider now requires the block tags lookup future.
        super(output, lookupProvider, blockTagsProvider.contentsGetter(), JurassicReborn.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        copy(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS);
        copy(BlockTags.BUTTONS, ItemTags.BUTTONS);
        copy(BlockTags.PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES);
        copy(BlockTags.SLABS, ItemTags.SLABS);
        copy(BlockTags.STAIRS, ItemTags.STAIRS);
        copy(BlockTags.WALLS, ItemTags.WALLS);
        copy(BlockTags.DOORS, ItemTags.DOORS);
    }

    @Override
    public String getName() {
        return "Jurassic Reborn Item Tags";
    }
}
