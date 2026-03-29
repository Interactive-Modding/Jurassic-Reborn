package net.vit.jurassicreborn.common.blocks.entities;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNACombinatorHybridizer.DNACombinatorHybridizerBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNAExtractor.DNAExtractorBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASequencer.DNASequencerBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASynthesizer.DNASynthesizerBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.Embryoncis.EmbryoCalcificationMachine.EmbryoCalcificationMachineBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.Embryoncis.EmbryonicMachine.EmbryonicMachineBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.bugcrate.BugCrateBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.cleaner.CleanerBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.HologramBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.cultivator.CultivatorBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.cultivator.CultivatorTopBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.feeder.FeederBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.fence.ElectricFenceBaseBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.fence.ElectricFencePoleBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.fence.ElectricFenceWireBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.grinder.FossilGrinderBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.incubator.IncubatorBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.skeletonassembly.SkeletonAssemblerBlockEntity;
import net.vit.jurassicreborn.common.blocks.fossil.FaunaFossilBlockEntity;
import net.vit.jurassicreborn.common.blocks.parkBlocks.TourRailBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vit.jurassicreborn.common.blocks.wood.WoodBlocks;


public class ModBlockEntities<T extends BlockEntity> {

    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, JurassicReborn.MODID);

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<CleanerBlockEntity>> CLEANING_STATION = BLOCK_ENTITY_TYPES.register("cleaning_station", () -> BlockEntityType.Builder.of(CleanerBlockEntity::new, ModBlocks.CLEANING_STATION.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<EncasedFaunaFossilBlockEntity>> ENCASED_FAUNA_FOSSIL =
            BLOCK_ENTITY_TYPES.register("encased_fossil", () -> BlockEntityType.Builder.of(
                    EncasedFaunaFossilBlockEntity::new,
                    ModBlocks.ENCASED_FAUNA_FOSSIL.get(),
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
                    ModBlocks.ENCASED_CAMARASAURUS_FOSSIL.get(),
                    ModBlocks.ENCASED_CALYMENE_FOSSIL.get(),
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
                    ModBlocks.ENCASED_LUDODACTYLUS_FOSSIL.get(),
                    ModBlocks.ENCASED_MAJUNGASAURUS_FOSSIL.get(),
                    ModBlocks.ENCASED_MAMENCHISAURUS_FOSSIL.get(),
                    ModBlocks.ENCASED_MAMMOTH_FOSSIL.get(),
                    ModBlocks.ENCASED_MAWSONIA_FOSSIL.get(),
                    ModBlocks.ENCASED_MEGAPIRANHA_FOSSIL.get(),
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
                    ModBlocks.ENCASED_NIGERSAURUS_FOSSIL.get(),
                    ModBlocks.ENCASED_TROODON_FOSSIL.get(),
                    ModBlocks.ENCASED_TROPEOGNATHUS_FOSSIL.get(),
                    ModBlocks.ENCASED_TYLOSAURUS_FOSSIL.get(),
                    ModBlocks.ENCASED_TYRANNOSAURUS_FOSSIL.get(),
                    ModBlocks.ENCASED_VECTIPELTA_FOSSIL.get(),
                    ModBlocks.ENCASED_VELOCIRAPTOR_FOSSIL.get(),
                    ModBlocks.ENCASED_LIVYATAN_FOSSIL.get(),
                    ModBlocks.ENCASED_MEGALODON_FOSSIL.get(),
                    ModBlocks.ENCASED_ZHENYUANOPTERUS_FOSSIL.get(),
                    ModBlocks.ENCASED_CAMEROCERAS_FOSSIL.get(),
                    ModBlocks.ENCASED_ORTHOCERAS_FOSSIL.get(),
                    ModBlocks.ENCASED_DEINOSUCHUS_FOSSIL.get(),
                    ModBlocks.ENCASED_PATAGOTITAN_FOSSIL.get(),
                    ModBlocks.ENCASED_MAIASAURA_FOSSIL.get(),
                    ModBlocks.ENCASED_KAIRUKU_FOSSIL.get(),
                    ModBlocks.ENCASED_ENDOCERAS_FOSSIL.get()
            ).build(null));

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<DNACombinatorHybridizerBlockEntity>> DNA_COMBINATOR_HYBRIDIZER = BLOCK_ENTITY_TYPES.register("dna_combinator_hybridizer", () -> BlockEntityType.Builder.of(DNACombinatorHybridizerBlockEntity::new, ModBlocks.DNA_COMBINER_HYBRIDIZER.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ModSignBlockEntity>> SIGN =
            BLOCK_ENTITY_TYPES.register("sign",
                    () -> BlockEntityType.Builder.of(
                            ModSignBlockEntity::new,
                            WoodBlocks.GINKGO_SIGN.get(),
                            WoodBlocks.GINKGO_WALL_SIGN.get(),
                            WoodBlocks.CALAMITES_SIGN.get(),
                            WoodBlocks.CALAMITES_WALL_SIGN.get(),
                            WoodBlocks.ARAUCARIA_SIGN.get(),
                            WoodBlocks.ARAUCARIA_WALL_SIGN.get(),
                            WoodBlocks.PHOENIX_SIGN.get(),
                            WoodBlocks.PHOENIX_WALL_SIGN.get(),
                            WoodBlocks.PSARONIUS_SIGN.get(),
                            WoodBlocks.PSARONIUS_WALL_SIGN.get(),
                            WoodBlocks.MAGNOLIA_SIGN.get(),
                            WoodBlocks.MAGNOLIA_WALL_SIGN.get()
                    ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ModHangingSignBlockEntity>> HANGING_SIGN =
            BLOCK_ENTITY_TYPES.register("hanging_sign",
                    () -> BlockEntityType.Builder.of(
                            ModHangingSignBlockEntity::new,
                            WoodBlocks.GINKGO_HANGING_SIGN.get(),
                            WoodBlocks.GINKGO_WALL_HANGING_SIGN.get(),
                            WoodBlocks.CALAMITES_HANGING_SIGN.get(),
                            WoodBlocks.CALAMITES_WALL_HANGING_SIGN.get(),
                            WoodBlocks.ARAUCARIA_HANGING_SIGN.get(),
                            WoodBlocks.ARAUCARIA_WALL_HANGING_SIGN.get(),
                            WoodBlocks.PHOENIX_HANGING_SIGN.get(),
                            WoodBlocks.PHOENIX_WALL_HANGING_SIGN.get(),
                            WoodBlocks.PSARONIUS_HANGING_SIGN.get(),
                            WoodBlocks.PSARONIUS_WALL_HANGING_SIGN.get(),
                            WoodBlocks.MAGNOLIA_HANGING_SIGN.get(),
                            WoodBlocks.MAGNOLIA_WALL_HANGING_SIGN.get()
                    ).build(null));

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TourRailBlockEntity>> TOUR_RAIL_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("tour_rail_block_entity", () -> {
        return BlockEntityType.Builder.of(TourRailBlockEntity::new,
                ModBlocks.TOUR_RAIL.get(),
                ModBlocks.TOUR_RAIL_MEDIUM.get(),
                ModBlocks.TOUR_RAIL_SLOW.get(),
                ModBlocks.TOUR_RAIL_FAST.get()).build(null);
    });

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<FaunaFossilBlockEntity>> FAUNA_FOSSIL_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("fauna_fossil_block_entity", () -> BlockEntityType.Builder.of(FaunaFossilBlockEntity::new, ModBlocks.FAUNA_FOSSIL.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<FeederBlockEntity>> FEEDER =
            BLOCK_ENTITY_TYPES.register("feeder",
                    () -> BlockEntityType.Builder
                            .of(FeederBlockEntity::new, ModBlocks.FEEDER.get())
                            .build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BugCrateBlockEntity>> BUG_CRATE =
            BLOCK_ENTITY_TYPES.register("bug_crate",
                    () -> BlockEntityType.Builder
                            .of(net.vit.jurassicreborn.common.blocks.entities.bugcrate.BugCrateBlockEntity::new, ModBlocks.BUG_CRATE.get())
                            .build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<DNAExtractorBlockEntity>> DNA_EXTRACTOR_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("dna_extractor_block_entity", () -> BlockEntityType.Builder.of(DNAExtractorBlockEntity::new, ModBlocks.DNA_EXTRACTOR.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<ElectricFenceBaseBlockEntity>> BASE_FENCE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("base_fence_block_entity", () -> BlockEntityType.Builder.of(ElectricFenceBaseBlockEntity::new, ModBlocks.LOW_SECURITY_FENCE_BASE.get(), ModBlocks.HIGH_SECURITY_FENCE_BASE.get(), ModBlocks.MED_SECURITY_FENCE_BASE.get()).build(null));

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<ElectricFenceWireBlockEntity>> WIRE_FENCE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("wire_fence_block_entity", () -> BlockEntityType.Builder.of(ElectricFenceWireBlockEntity::new, ModBlocks.LOW_SECURITY_FENCE_WIRE.get(), ModBlocks.HIGH_SECURITY_FENCE_WIRE.get(), ModBlocks.MED_SECURITY_FENCE_WIRE.get()).build(null));

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<ElectricFencePoleBlockEntity>> POLE_FENCE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("pole_fence_block_entity", () -> BlockEntityType.Builder.of(ElectricFencePoleBlockEntity::new, ModBlocks.LOW_SECURITY_FENCE_POLE.get(), ModBlocks.MED_SECURITY_FENCE_POLE.get(), ModBlocks.HIGH_SECURITY_FENCE_POLE.get()).build(null));

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<ActionFigureBlockEntity>> DISPLAY_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("display_block_entity", () -> BlockEntityType.Builder.of(ActionFigureBlockEntity::new, ModBlocks.DISPLAY_BLOCK.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<HologramBlockEntity>> HOLOGRAM_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("hologram_block_entity", () -> BlockEntityType.Builder.of(HologramBlockEntity::new, ModBlocks.HOLOGRAM_BLOCK.get()).build(null));

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<IncubatorBlockEntity>> INCUBATOR_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("incubator_block_entity", () -> BlockEntityType.Builder.of(IncubatorBlockEntity::new, ModBlocks.INCUBATOR.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<SkeletonAssemblerBlockEntity>> SKELETON_ASSEMBLY_ENTITY = BLOCK_ENTITY_TYPES.register("skeleton_assembly_block_entity", () -> BlockEntityType.Builder.of(SkeletonAssemblerBlockEntity::new, ModBlocks.SKELETON_ASSEMBLY.get()).build(null));

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<FossilGrinderBlockEntity>> FOSSIL_GRINDER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("fossil_grinder_block_entity", () -> BlockEntityType.Builder.of(FossilGrinderBlockEntity::new, ModBlocks.FOSSIL_GRINDER.get()).build(null));

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<DNASequencerBlockEntity>> DNA_SEQUENCER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("dna_sequencer_block_entity", () -> BlockEntityType.Builder.of(DNASequencerBlockEntity::new, ModBlocks.DNA_SEQUENCER.get()).build(null));

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<DNASynthesizerBlockEntity>> DNA_SYNTHESIZER_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("dna_synthesizer_block_entity", () -> BlockEntityType.Builder.of(DNASynthesizerBlockEntity::new, ModBlocks.DNA_SYNTHESIZER.get()).build(null));

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<EmbryonicMachineBlockEntity>> EMBRYONIC_MACHINE_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("embryonic_machine_block_entity", () -> BlockEntityType.Builder.of(EmbryonicMachineBlockEntity::new, ModBlocks.EMBRYONIC_MACHINE.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<SkullDisplayBlockEntity>> SKULL_DISPLAY_BLOCK_ENTITY = BLOCK_ENTITY_TYPES.register("skull_display_block_entity", () -> BlockEntityType.Builder.of(SkullDisplayBlockEntity::new, ModBlocks.SKULL_DISPLAY.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<EmbryoCalcificationMachineBlockEntity>> EMBRYO_CALCIFICATION_MACHINE_BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("embryo_calcification_machine_block_entity", () -> BlockEntityType.Builder.of(EmbryoCalcificationMachineBlockEntity::new, ModBlocks.EMBRYO_CALCIFICATION_MACHINE.get()).build(null));

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<CultivatorTopBlockEntity>> CULTIVATOR_TOP_BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("cultivator_top_block_entity_type", () -> BlockEntityType.Builder.of(CultivatorTopBlockEntity::new, ModBlocks.CULTIVATE_TOP.get()).build(null));

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<CultivatorBlockEntity>> CULTIVATOR_BLOCK_ENTITY_TYPE = BLOCK_ENTITY_TYPES.register("cultivator_block_entity_type", () -> BlockEntityType.Builder.of(CultivatorBlockEntity::new, ModBlocks.CULTIVATE_BOTTOM.get()).build(null));

}
