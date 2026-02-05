package net.vit.jurassicreborn.common.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.wood.WoodBlocks;
import net.vit.jurassicreborn.common.datagen.ModBlockFamilies;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class JRBlockTagsProvider extends BlockTagsProvider {

    public JRBlockTagsProvider(PackOutput output,
                               CompletableFuture<HolderLookup.Provider> lookupProvider,
                               @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, JurassicReborn.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Families → generic tags by variant
        ModBlockFamilies.getAllFamilies()
                .map(family -> family.get(BlockFamily.Variant.TRAPDOOR))
                .filter(Objects::nonNull)
                .forEach(b -> tag(BlockTags.WOODEN_TRAPDOORS).add(b));

        ModBlockFamilies.getAllFamilies()
                .map(family -> family.get(BlockFamily.Variant.BUTTON))
                .filter(Objects::nonNull)
                .forEach(b -> tag(BlockTags.BUTTONS).add(b));

        ModBlockFamilies.getAllFamilies()
                .map(family -> family.get(BlockFamily.Variant.PRESSURE_PLATE))
                .filter(Objects::nonNull)
                .forEach(b -> tag(BlockTags.PRESSURE_PLATES).add(b));

        ModBlockFamilies.getAllFamilies()
                .map(family -> family.get(BlockFamily.Variant.SLAB))
                .filter(Objects::nonNull)
                .forEach(b -> tag(BlockTags.SLABS).add(b));

        ModBlockFamilies.getAllFamilies()
                .map(family -> family.get(BlockFamily.Variant.STAIRS))
                .filter(Objects::nonNull)
                .forEach(b -> tag(BlockTags.STAIRS).add(b));

        ModBlockFamilies.getAllFamilies()
                .map(family -> family.get(BlockFamily.Variant.WALL))
                .filter(Objects::nonNull)
                .forEach(b -> tag(BlockTags.WALLS).add(b));

        ModBlockFamilies.getAllFamilies()
                .map(family -> family.get(BlockFamily.Variant.DOOR))
                .filter(Objects::nonNull)
                .forEach(b -> tag(BlockTags.DOORS).add(b));

        // (Removed duplicate extra loop that added TRAPDOORs again)

        // Explicit wooden/stone/etc. sets
        tag(BlockTags.BUTTONS).add(
                WoodBlocks.ARAUCARIA_BUTTON.get(),
                WoodBlocks.MAGNOLIA_BUTTON.get(),
                WoodBlocks.GINKGO_BUTTON.get(),
                WoodBlocks.CALAMITES_BUTTON.get(),
                WoodBlocks.PHOENIX_BUTTON.get(),
                WoodBlocks.PSARONIUS_BUTTON.get(),
                ModBlocks.GYPSUM_BRICK_BUTTON.get(),
                ModBlocks.REINFORCED_BRICK_BUTTON.get(),
                ModBlocks.GYPSUM_STONE_BUTTON.get(),
                ModBlocks.GYPSUM_COBBLESTONE_BUTTON.get(),
                ModBlocks.GYPSUM_COBBLESTONE_PATHWAY_BUTTON.get(),
                ModBlocks.GYPSUM_PATHWAY_BUTTON.get(),
                ModBlocks.GYPSUM_MIXED_PATH_BUTTON.get(),
                ModBlocks.GYPSUM_TILES_BUTTON.get(),
                ModBlocks.REFINED_GYPSUM_PANEL_BUTTON.get(),
                ModBlocks.GYPSUM_STONE_PANEL_BUTTON.get(),
                ModBlocks.REINFORCED_STONE_BUTTON.get(),
                ModBlocks.REINFORCED_STONE_TILES_BUTTON.get(),
                ModBlocks.REINFORCED_STONE_PATHWAY_BUTTON.get(),
                ModBlocks.REINFORCED_STONE_PANEL_BUTTON.get()
        );

        tag(BlockTags.PRESSURE_PLATES).add(
                WoodBlocks.ARAUCARIA_PRESSURE_PLATE.get(),
                WoodBlocks.MAGNOLIA_PRESSURE_PLATE.get(),
                WoodBlocks.GINKGO_PRESSURE_PLATE.get(),
                WoodBlocks.CALAMITES_PRESSURE_PLATE.get(),
                WoodBlocks.PHOENIX_PRESSURE_PLATE.get(),
                WoodBlocks.PSARONIUS_PRESSURE_PLATE.get(),
                ModBlocks.GYPSUM_BRICK_PRESSURE_PLATE.get(),
                ModBlocks.REINFORCED_BRICK_PRESSURE_PLATE.get(),
                ModBlocks.GYPSUM_STONE_PRESSURE_PLATE.get(),
                ModBlocks.GYPSUM_COBBLESTONE_PRESSURE_PLATE.get(),
                ModBlocks.GYPSUM_COBBLESTONE_PATHWAY_PRESSURE_PLATE.get(),
                ModBlocks.GYPSUM_PATHWAY_PRESSURE_PLATE.get(),
                ModBlocks.GYPSUM_MIXED_PATH_PRESSURE_PLATE.get(),
                ModBlocks.GYPSUM_TILES_PRESSURE_PLATE.get(),
                ModBlocks.REFINED_GYPSUM_PANEL_PRESSURE_PLATE.get(),
                ModBlocks.GYPSUM_STONE_PANEL_PRESSURE_PLATE.get(),
                ModBlocks.REINFORCED_STONE_PRESSURE_PLATE.get(),
                ModBlocks.REINFORCED_STONE_TILES_PRESSURE_PLATE.get(),
                ModBlocks.REINFORCED_STONE_PATHWAY_PRESSURE_PLATE.get(),
                ModBlocks.REINFORCED_STONE_PANEL_PRESSURE_PLATE.get()
        );

        tag(BlockTags.SLABS).add(
                WoodBlocks.ARAUCARIA_SLAB.get(),
                WoodBlocks.MAGNOLIA_SLAB.get(),
                WoodBlocks.GINKGO_SLAB.get(),
                WoodBlocks.CALAMITES_SLAB.get(),
                WoodBlocks.PHOENIX_SLAB.get(),
                WoodBlocks.PSARONIUS_SLAB.get(),
                ModBlocks.GYPSUM_BRICK_SLAB.get(),
                ModBlocks.REINFORCED_BRICK_SLAB.get(),
                ModBlocks.GYPSUM_STONE_SLAB.get(),
                ModBlocks.GYPSUM_COBBLESTONE_SLAB.get(),
                ModBlocks.GYPSUM_COBBLESTONE_PATHWAY_SLAB.get(),
                ModBlocks.GYPSUM_PATHWAY_SLAB.get(),
                ModBlocks.GYPSUM_MIXED_PATH_SLAB.get(),
                ModBlocks.GYPSUM_TILES_SLAB.get(),
                ModBlocks.REFINED_GYPSUM_PANEL_SLAB.get(),
                ModBlocks.GYPSUM_STONE_PANEL_SLAB.get(),
                ModBlocks.REINFORCED_STONE_SLAB.get(),
                ModBlocks.REINFORCED_STONE_TILES_SLAB.get(),
                ModBlocks.REINFORCED_STONE_PATHWAY_SLAB.get(),
                ModBlocks.REINFORCED_STONE_PANEL_SLAB.get()
        );

        tag(BlockTags.STAIRS).add(
                WoodBlocks.ARAUCARIA_STAIRS.get(),
                WoodBlocks.MAGNOLIA_STAIRS.get(),
                WoodBlocks.GINKGO_STAIRS.get(),
                WoodBlocks.CALAMITES_STAIRS.get(),
                WoodBlocks.PHOENIX_STAIRS.get(),
                WoodBlocks.PSARONIUS_STAIRS.get(),
                ModBlocks.GYPSUM_BRICK_STAIRS.get(),
                ModBlocks.REINFORCED_BRICK_STAIRS.get(),
                ModBlocks.GYPSUM_STONE_STAIRS.get(),
                ModBlocks.GYPSUM_COBBLESTONE_STAIRS.get(),
                ModBlocks.GYPSUM_COBBLESTONE_PATHWAY_STAIRS.get(),
                ModBlocks.GYPSUM_PATHWAY_STAIRS.get(),
                ModBlocks.GYPSUM_MIXED_PATH_STAIRS.get(),
                ModBlocks.GYPSUM_TILES_STAIRS.get(),
                ModBlocks.REFINED_GYPSUM_PANEL_STAIRS.get(),
                ModBlocks.GYPSUM_STONE_PANEL_STAIRS.get(),
                ModBlocks.REINFORCED_STONE_STAIRS.get(),
                ModBlocks.REINFORCED_STONE_TILES_STAIRS.get(),
                ModBlocks.REINFORCED_STONE_PATHWAY_STAIRS.get(),
                ModBlocks.REINFORCED_STONE_PANEL_STAIRS.get()
        );

        tag(BlockTags.WALLS).add(
                ModBlocks.GYPSUM_BRICK_WALL.get(),
                ModBlocks.REINFORCED_BRICK_WALL.get(),
                ModBlocks.GYPSUM_STONE_WALL.get(),
                ModBlocks.GYPSUM_COBBLESTONE_WALL.get(),
                ModBlocks.GYPSUM_COBBLESTONE_PATHWAY_WALL.get(),
                ModBlocks.GYPSUM_PATHWAY_WALL.get(),
                ModBlocks.GYPSUM_MIXED_PATH_WALL.get(),
                ModBlocks.GYPSUM_TILES_WALL.get(),
                ModBlocks.REFINED_GYPSUM_PANEL_WALL.get(),
                ModBlocks.GYPSUM_STONE_PANEL_WALL.get(),
                ModBlocks.REINFORCED_STONE_WALL.get(),
                ModBlocks.REINFORCED_STONE_TILES_WALL.get(),
                ModBlocks.REINFORCED_STONE_PATHWAY_WALL.get(),
                ModBlocks.REINFORCED_STONE_PANEL_WALL.get()
        );

        tag(BlockTags.DOORS).add(
                ModBlocks.REINFORCED_DOOR.get(),
                ModBlocks.SECURITY_DOOR.get()
        );
    }

    @Override
    public String getName() {
        return "Jurassic Reborn Block Tags";
    }
}
