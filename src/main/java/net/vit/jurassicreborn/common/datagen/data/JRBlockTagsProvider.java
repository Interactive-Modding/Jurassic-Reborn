package net.vit.jurassicreborn.common.datagen.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.BlockFamily;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
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

        tag(BlockTags.MINEABLE_WITH_AXE)
                .addTag(BlockTags.LOGS)
                .addTag(BlockTags.PLANKS)
                .addTag(BlockTags.WOODEN_SLABS)
                .addTag(BlockTags.WOODEN_STAIRS)
                .addTag(BlockTags.WOODEN_FENCES)
                .addTag(BlockTags.FENCE_GATES)
                .addTag(BlockTags.WOODEN_DOORS)
                .addTag(BlockTags.WOODEN_TRAPDOORS)
                .addTag(BlockTags.WOODEN_BUTTONS)
                .addTag(BlockTags.WOODEN_PRESSURE_PLATES)
                .addTag(BlockTags.SIGNS)
                .add(
                        WoodBlocks.ARAUCARIA_HANGING_SIGN.get(),
                        WoodBlocks.ARAUCARIA_WALL_HANGING_SIGN.get(),
                        WoodBlocks.CALAMITES_HANGING_SIGN.get(),
                        WoodBlocks.CALAMITES_WALL_HANGING_SIGN.get(),
                        WoodBlocks.GINKGO_HANGING_SIGN.get(),
                        WoodBlocks.GINKGO_WALL_HANGING_SIGN.get(),
                        WoodBlocks.MAGNOLIA_HANGING_SIGN.get(),
                        WoodBlocks.MAGNOLIA_WALL_HANGING_SIGN.get(),
                        WoodBlocks.PHOENIX_HANGING_SIGN.get(),
                        WoodBlocks.PHOENIX_WALL_HANGING_SIGN.get(),
                        WoodBlocks.PSARONIUS_HANGING_SIGN.get(),
                        WoodBlocks.PSARONIUS_WALL_HANGING_SIGN.get(),
                        ModBlocks.SKELETON_ASSEMBLY.get(),
                        ModBlocks.BUG_CRATE.get(),
                        ModBlocks.PARK_BENCH.get()
                );

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                ModBlocks.LOW_SECURITY_FENCE_BASE.get(),
                ModBlocks.MED_SECURITY_FENCE_BASE.get(),
                ModBlocks.HIGH_SECURITY_FENCE_BASE.get(),
                ModBlocks.LOW_SECURITY_FENCE_POLE.get(),
                ModBlocks.MED_SECURITY_FENCE_POLE.get(),
                ModBlocks.HIGH_SECURITY_FENCE_POLE.get(),
                ModBlocks.LOW_SECURITY_FENCE_WIRE.get(),
                ModBlocks.MED_SECURITY_FENCE_WIRE.get(),
                ModBlocks.HIGH_SECURITY_FENCE_WIRE.get(),
                WoodBlocks.PETRIFIED_ARAUCARIA_LOG.get(),
                WoodBlocks.PETRIFIED_CALAMITES_LOG.get(),
                WoodBlocks.PETRIFIED_GINKGO_LOG.get(),
                WoodBlocks.PETRIFIED_MAGNOLIA_LOG.get(),
                WoodBlocks.PETRIFIED_PHOENIX_LOG.get(),
                WoodBlocks.PETRIFIED_PSARONIUS_LOG.get(),
                ModBlocks.ENCASED_FAUNA_FOSSIL.get(),
                ModBlocks.ICE_SHARD_ORE.get(),
                ModBlocks.AMBER_ORE.get(),
                ModBlocks.AMBER_BLOCK.get(),
                ModBlocks.FEEDER.get(),
                ModBlocks.CULTIVATE_BOTTOM.get(),
                ModBlocks.CULTIVATE_TOP.get(),
                ModBlocks.AMBER_MOSQUITO.get(),
                ModBlocks.AMBER_APHID.get(),
                ModBlocks.SEA_LAMPREY.get(),
                ModBlocks.FROZEN_LEECH.get(),
                ModBlocks.CLEANING_STATION.get(),
                ModBlocks.FOSSIL_GRINDER.get(),
                ModBlocks.DNA_SEQUENCER.get(),
                ModBlocks.DNA_COMBINER_HYBRIDIZER.get(),
                ModBlocks.DNA_SYNTHESIZER.get(),
                ModBlocks.EMBRYO_CALCIFICATION_MACHINE.get(),
                ModBlocks.EMBRYONIC_MACHINE.get(),
                ModBlocks.INCUBATOR.get(),
                ModBlocks.DNA_EXTRACTOR.get(),
                ModBlocks.TOUR_RAIL.get(),
                ModBlocks.TOUR_RAIL_SLOW.get(),
                ModBlocks.TOUR_RAIL_MEDIUM.get(),
                ModBlocks.TOUR_RAIL_FAST.get(),
                ModBlocks.FLORA_FOSSIL.get(),
                ModBlocks.DEEPSLATE_FLORA_FOSSIL.get(),
                ModBlocks.DEEPSLATE_AMBER_ORE.get(),
                ModBlocks.FOSSILIZED_TRACKWAY_RAPTOR.get(),
                ModBlocks.FOSSILIZED_TRACKWAY_BIPED_SMALL.get(),
                ModBlocks.FOSSILIZED_TRACKWAY_BIPED_MEDIUM.get(),
                ModBlocks.NEST_FOSSIL.get(),
                ModBlocks.DEEPSLATE_ICE_SHARD_ORE.get(),
                ModBlocks.REINFORCED_DOOR.get(),
                ModBlocks.SECURITY_DOOR.get(),
                ModBlocks.REINFORCED_STONE.get(),
                ModBlocks.REINFORCED_BRICKS.get(),
                ModBlocks.REINFORCED_STONE_TILES.get(),
                ModBlocks.REINFORCED_STONE_PATHWAY.get(),
                ModBlocks.REINFORCED_STONE_PANEL.get(),
                ModBlocks.REINFORCED_BRICK_STAIRS.get(),
                ModBlocks.REINFORCED_BRICK_SLAB.get(),
                ModBlocks.REINFORCED_BRICK_PRESSURE_PLATE.get(),
                ModBlocks.REINFORCED_BRICK_BUTTON.get(),
                ModBlocks.REINFORCED_BRICK_WALL.get(),
                ModBlocks.GYPSUM_STONE.get(),
                ModBlocks.GYPSUM_COBBLESTONE.get(),
                ModBlocks.GYPSUM_COBBLESTONE_PATHWAY.get(),
                ModBlocks.GYPSUM_BRICKS.get(),
                ModBlocks.GYPSUM_PATHWAY.get(),
                ModBlocks.GYPSUM_MIXED_PATH.get(),
                ModBlocks.GYPSUM_TILES.get(),
                ModBlocks.REFINED_GYPSUM_PANEL.get(),
                ModBlocks.GYPSUM_STONE_PANEL.get(),
                ModBlocks.GYPSUM_BRICK_STAIRS.get(),
                ModBlocks.GYPSUM_BRICK_SLAB.get(),
                ModBlocks.GYPSUM_BRICK_PRESSURE_PLATE.get(),
                ModBlocks.GYPSUM_BRICK_BUTTON.get(),
                ModBlocks.GYPSUM_BRICK_WALL.get(),
                ModBlocks.ENCASED_ACHILLOBATOR_FOSSIL.get(),
                ModBlocks.ENCASED_ALLIGATOR_GAR_FOSSIL.get(),
                ModBlocks.ENCASED_ALLOSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_ALVAREZSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_ANKYLOSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_APATOSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_ARSINOITHERIUM_FOSSIL.get(),
                ModBlocks.ENCASED_ASTEROCERAS_FOSSIL.get(),
                ModBlocks.ENCASED_BARYONYX_FOSSIL.get(),
                ModBlocks.ENCASED_BEELZEBUFO_FOSSIL.get(),
                ModBlocks.ENCASED_BRACHIOSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_CALYMENE_FOSSIL.get(),
                ModBlocks.ENCASED_CAMARASAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_CAMEROCERAS_FOSSIL.get(),
                ModBlocks.ENCASED_ENDOCERAS_FOSSIL.get(),
                ModBlocks.ENCASED_ORTHOCERAS_FOSSIL.get(),
                ModBlocks.ENCASED_CARCHARODONTOSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_CARNOTAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_CEARADACTYLUS_FOSSIL.get(),
                ModBlocks.ENCASED_CERATOSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_CHASMOSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_CHILESAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_COELACANTH_FOSSIL.get(),
                ModBlocks.ENCASED_COELURUS_FOSSIL.get(),
                ModBlocks.ENCASED_COMPSOGNATHUS_FOSSIL.get(),
                ModBlocks.ENCASED_CORYTHOSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_CRASSIGYRINUS_FOSSIL.get(),
                ModBlocks.ENCASED_DEINOTHERIUM_FOSSIL.get(),
                ModBlocks.ENCASED_DILOPHOSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_DIMETRODON_FOSSIL.get(),
                ModBlocks.ENCASED_DIMORPHODON_FOSSIL.get(),
                ModBlocks.ENCASED_DIPLOCAULUS_FOSSIL.get(),
                ModBlocks.ENCASED_DIPLODOCUS_FOSSIL.get(),
                ModBlocks.ENCASED_DODO_FOSSIL.get(),
                ModBlocks.ENCASED_DREADNOUGHTUS_FOSSIL.get(),
                ModBlocks.ENCASED_DUNKLEOSTEUS_FOSSIL.get(),
                ModBlocks.ENCASED_EDMONTOSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_ELASMOTHERIUM_FOSSIL.get(),
                ModBlocks.ENCASED_GALLIMIMUS_FOSSIL.get(),
                ModBlocks.ENCASED_GIGANOTOSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_GUANLONG_FOSSIL.get(),
                ModBlocks.ENCASED_HERRERASAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_HYAENODON_FOSSIL.get(),
                ModBlocks.ENCASED_HYPSILOPHODON_FOSSIL.get(),
                ModBlocks.ENCASED_LAMBEOSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_LEAELLYNASAURA_FOSSIL.get(),
                ModBlocks.ENCASED_LEPTICTIDIUM_FOSSIL.get(),
                ModBlocks.ENCASED_LIVYATAN_FOSSIL.get(),
                ModBlocks.ENCASED_LUDODACTYLUS_FOSSIL.get(),
                ModBlocks.ENCASED_MAJUNGASAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_MAMENCHISAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_MAMMOTH_FOSSIL.get(),
                ModBlocks.ENCASED_MAWSONIA_FOSSIL.get(),
                ModBlocks.ENCASED_NIGERSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_DEINOSUCHUS_FOSSIL.get(),
                ModBlocks.ENCASED_PATAGOTITAN_FOSSIL.get(),
                ModBlocks.ENCASED_MAIASAURA_FOSSIL.get(),
                ModBlocks.ENCASED_KAIRUKU_FOSSIL.get(),
                ModBlocks.TRASH_CAN.get(),
                ModBlocks.ENCASED_MEGAPIRANHA_FOSSIL.get(),
                ModBlocks.ENCASED_MEGALODON_FOSSIL.get(),
                ModBlocks.ENCASED_MEGATHERIUM_FOSSIL.get(),
                ModBlocks.ENCASED_METRIACANTHOSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_MICROCERATUS_FOSSIL.get(),
                ModBlocks.ENCASED_MICRORAPTOR_FOSSIL.get(),
                ModBlocks.ENCASED_MOGANOPTERUS_FOSSIL.get(),
                ModBlocks.ENCASED_MOSASAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_MUSSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_ORNITHOMIMUS_FOSSIL.get(),
                ModBlocks.ENCASED_OTHNIELIA_FOSSIL.get(),
                ModBlocks.ENCASED_OVIRAPTOR_FOSSIL.get(),
                ModBlocks.ENCASED_PACHYCEPHALOSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_PARACERATHERIUM_FOSSIL.get(),
                ModBlocks.ENCASED_PARAPUZOSIA_FOSSIL.get(),
                ModBlocks.ENCASED_PARASAUROLOPHUS_FOSSIL.get(),
                ModBlocks.ENCASED_PERISPHINCTES_FOSSIL.get(),
                ModBlocks.ENCASED_POSTOSUCHUS_FOSSIL.get(),
                ModBlocks.ENCASED_PROCERATOSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_PROTOCERATOPS_FOSSIL.get(),
                ModBlocks.ENCASED_PTERANODON_FOSSIL.get(),
                ModBlocks.ENCASED_QUETZAL_FOSSIL.get(),
                ModBlocks.ENCASED_RUGOPS_FOSSIL.get(),
                ModBlocks.ENCASED_SEGISAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_SINOCERATOPS_FOSSIL.get(),
                ModBlocks.ENCASED_SMILODON_FOSSIL.get(),
                ModBlocks.ENCASED_SPINOSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_STEGOSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_STYRACOSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_SUCHOMIMUS_FOSSIL.get(),
                ModBlocks.ENCASED_THERIZINOSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_TITANIS_FOSSIL.get(),
                ModBlocks.ENCASED_TITANITES_FOSSIL.get(),
                ModBlocks.ENCASED_TRICERATOPS_FOSSIL.get(),
                ModBlocks.ENCASED_TROODON_FOSSIL.get(),
                ModBlocks.ENCASED_TROPEOGNATHUS_FOSSIL.get(),
                ModBlocks.ENCASED_TYLOSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_TYRANNOSAURUS_FOSSIL.get(),
                ModBlocks.ENCASED_VECTIPELTA_FOSSIL.get(),
                ModBlocks.ENCASED_VELOCIRAPTOR_FOSSIL.get(),
                ModBlocks.ENCASED_ZHENYUANOPTERUS_FOSSIL.get(),
                ModBlocks.REINFORCED_STONE_STAIRS.get(),
                ModBlocks.REINFORCED_STONE_SLAB.get(),
                ModBlocks.REINFORCED_STONE_PRESSURE_PLATE.get(),
                ModBlocks.REINFORCED_STONE_BUTTON.get(),
                ModBlocks.REINFORCED_STONE_WALL.get(),
                ModBlocks.REINFORCED_BRICK_STAIRS.get(),
                ModBlocks.REINFORCED_BRICK_SLAB.get(),
                ModBlocks.REINFORCED_BRICK_PRESSURE_PLATE.get(),
                ModBlocks.REINFORCED_BRICK_BUTTON.get(),
                ModBlocks.REINFORCED_BRICK_WALL.get(),
                ModBlocks.REINFORCED_STONE_TILES_STAIRS.get(),
                ModBlocks.REINFORCED_STONE_TILES_SLAB.get(),
                ModBlocks.REINFORCED_STONE_TILES_PRESSURE_PLATE.get(),
                ModBlocks.REINFORCED_STONE_TILES_BUTTON.get(),
                ModBlocks.REINFORCED_STONE_TILES_WALL.get(),
                ModBlocks.REINFORCED_STONE_PATHWAY_STAIRS.get(),
                ModBlocks.REINFORCED_STONE_PATHWAY_SLAB.get(),
                ModBlocks.REINFORCED_STONE_PATHWAY_PRESSURE_PLATE.get(),
                ModBlocks.REINFORCED_STONE_PATHWAY_BUTTON.get(),
                ModBlocks.REINFORCED_STONE_PATHWAY_WALL.get(),
                ModBlocks.REINFORCED_STONE_PANEL_STAIRS.get(),
                ModBlocks.REINFORCED_STONE_PANEL_SLAB.get(),
                ModBlocks.REINFORCED_STONE_PANEL_PRESSURE_PLATE.get(),
                ModBlocks.REINFORCED_STONE_PANEL_BUTTON.get(),
                ModBlocks.REINFORCED_STONE_PANEL_WALL.get(),
                ModBlocks.GYPSUM_STONE_STAIRS.get(),
                ModBlocks.GYPSUM_STONE_SLAB.get(),
                ModBlocks.GYPSUM_STONE_PRESSURE_PLATE.get(),
                ModBlocks.GYPSUM_STONE_BUTTON.get(),
                ModBlocks.GYPSUM_STONE_WALL.get(),
                ModBlocks.GYPSUM_COBBLESTONE_STAIRS.get(),
                ModBlocks.GYPSUM_COBBLESTONE_SLAB.get(),
                ModBlocks.GYPSUM_COBBLESTONE_PRESSURE_PLATE.get(),
                ModBlocks.GYPSUM_COBBLESTONE_BUTTON.get(),
                ModBlocks.GYPSUM_COBBLESTONE_WALL.get(),
                ModBlocks.GYPSUM_COBBLESTONE_PATHWAY_STAIRS.get(),
                ModBlocks.GYPSUM_COBBLESTONE_PATHWAY_SLAB.get(),
                ModBlocks.GYPSUM_COBBLESTONE_PATHWAY_PRESSURE_PLATE.get(),
                ModBlocks.GYPSUM_COBBLESTONE_PATHWAY_BUTTON.get(),
                ModBlocks.GYPSUM_COBBLESTONE_PATHWAY_WALL.get(),
                ModBlocks.GYPSUM_BRICK_STAIRS.get(),
                ModBlocks.GYPSUM_BRICK_SLAB.get(),
                ModBlocks.GYPSUM_BRICK_PRESSURE_PLATE.get(),
                ModBlocks.GYPSUM_BRICK_BUTTON.get(),
                ModBlocks.GYPSUM_BRICK_WALL.get(),
                ModBlocks.GYPSUM_PATHWAY_STAIRS.get(),
                ModBlocks.GYPSUM_PATHWAY_SLAB.get(),
                ModBlocks.GYPSUM_PATHWAY_PRESSURE_PLATE.get(),
                ModBlocks.GYPSUM_PATHWAY_BUTTON.get(),
                ModBlocks.GYPSUM_PATHWAY_WALL.get(),
                ModBlocks.GYPSUM_MIXED_PATH_STAIRS.get(),
                ModBlocks.GYPSUM_MIXED_PATH_SLAB.get(),
                ModBlocks.GYPSUM_MIXED_PATH_PRESSURE_PLATE.get(),
                ModBlocks.GYPSUM_MIXED_PATH_BUTTON.get(),
                ModBlocks.GYPSUM_MIXED_PATH_WALL.get(),
                ModBlocks.GYPSUM_TILES_STAIRS.get(),
                ModBlocks.GYPSUM_TILES_SLAB.get(),
                ModBlocks.GYPSUM_TILES_PRESSURE_PLATE.get(),
                ModBlocks.GYPSUM_TILES_BUTTON.get(),
                ModBlocks.GYPSUM_TILES_WALL.get(),
                ModBlocks.REFINED_GYPSUM_PANEL_STAIRS.get(),
                ModBlocks.REFINED_GYPSUM_PANEL_SLAB.get(),
                ModBlocks.REFINED_GYPSUM_PANEL_PRESSURE_PLATE.get(),
                ModBlocks.REFINED_GYPSUM_PANEL_BUTTON.get(),
                ModBlocks.REFINED_GYPSUM_PANEL_WALL.get(),
                ModBlocks.GYPSUM_STONE_PANEL_STAIRS.get(),
                ModBlocks.GYPSUM_STONE_PANEL_SLAB.get(),
                ModBlocks.GYPSUM_STONE_PANEL_PRESSURE_PLATE.get(),
                ModBlocks.GYPSUM_STONE_PANEL_BUTTON.get(),
                ModBlocks.GYPSUM_STONE_PANEL_WALL.get()
        );
    }

    @Override
    public String getName() {
        return "Jurassic Reborn Block Tags";
    }
}
