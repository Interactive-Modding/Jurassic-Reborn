package net.vit.jurassicreborn.common.datagen.data;

import net.minecraft.advancements.*;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.vit.jurassicreborn.JurassicReborn;
import java.util.function.Consumer;

import static net.vit.jurassicreborn.JurassicReborn.MODID;

public class JRAdvancements implements AdvancementSubProvider {

    private static ItemLike item(String id) {
        Item it = BuiltInRegistries.ITEM.get(ResourceLocation.parse(id));
        if (it == null) throw new IllegalStateException("Missing item: " + id);
        return it;
    }

    private static Component title(String key) {
        return Component.translatable("advancements.jurassicreborn." + key + ".title");
    }

    private static Component description(String key) {
        return Component.translatable("advancements.jurassicreborn." + key + ".description");
    }

    @Override
    public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> consumer) {
// root
        AdvancementHolder root = Advancement.Builder.advancement()
                .display(item("jurassicreborn:bones/mamenchisaurus_skull"),
                        title("root"), description("root"),
                        JurassicReborn.location("textures/block/gypsum_bricks.png"), AdvancementType.TASK, true, true, false)
                .addCriterion("requirement", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.CRAFTING_TABLE))
                .save(consumer, MODID + ":jurassicreborn/root");

        // plaster
        AdvancementHolder plaster = Advancement.Builder.advancement()
                .display(item("jurassicreborn:plaster_and_bandage"),
                        title("plaster"), description("plaster"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(root)
                .addCriterion("plaster_and_bandage",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:plaster_and_bandage")))
                .save(consumer, MODID + ":jurassicreborn/plaster");

        // encased_fossil
        AdvancementHolder encased_fossil = Advancement.Builder.advancement()
                .display(item("jurassicreborn:encased_fauna_fossil"),
                        title("encased_fossil"), description("encased_fossil"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(plaster)
                .addCriterion("encased_achillobator_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_achillobator_fossil")))
                .addCriterion("encased_alligator_gar_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_alligator_gar_fossil")))
                .addCriterion("encased_allosaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_allosaurus_fossil")))
                .addCriterion("encased_alvarezsaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_alvarezsaurus_fossil")))
                .addCriterion("encased_perisphinctes_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_perisphinctes_fossil")))
                .addCriterion("encased_ankylodocus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_ankylodocus_fossil")))
                .addCriterion("encased_ankylosaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_ankylosaurus_fossil")))
                .addCriterion("encased_apatosaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_apatosaurus_fossil")))
                .addCriterion("encased_arsinoitherium_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_arsinoitherium_fossil")))
                .addCriterion("encased_asteroceras_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_asteroceras_fossil")))
                .addCriterion("encased_cameroceras_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_cameroceras_fossil")))
                .addCriterion("encased_endoceras_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_endoceras_fossil")))
                .addCriterion("encased_orthoceras_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_orthoceras_fossil")))
                .addCriterion("encased_megalodon_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_megalodon_fossil")))
                .addCriterion("encased_kairuku_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_kairuku_fossil")))
                .addCriterion("encased_nigersaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_nigersaurus_fossil")))
                .addCriterion("encased_deinosuchus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_deinosuchus_fossil")))
                .addCriterion("encased_baryonyx_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_baryonyx_fossil")))
                .addCriterion("encased_beelzebufo_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_beelzebufo_fossil")))
                .addCriterion("encased_blue_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_blue_fossil")))
                .addCriterion("encased_brachiosaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_brachiosaurus_fossil")))
                .addCriterion("encased_calymene_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_calymene_fossil")))
                .addCriterion("encased_camarasaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_camarasaurus_fossil")))
                .addCriterion("encased_carcharodontosaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_carcharodontosaurus_fossil")))
                .addCriterion("encased_carnotaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_carnotaurus_fossil")))
                .addCriterion("encased_cearadactylus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_cearadactylus_fossil")))
                .addCriterion("encased_ceratosaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_ceratosaurus_fossil")))
                .addCriterion("encased_charlie_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_charlie_fossil")))
                .addCriterion("encased_chasmosaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_chasmosaurus_fossil")))
                .addCriterion("encased_chilesaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_chilesaurus_fossil")))
                .addCriterion("encased_coelacanth_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_coelacanth_fossil")))
                .addCriterion("encased_coelurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_coelurus_fossil")))
                .addCriterion("encased_compsognathus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_compsognathus_fossil")))
                .addCriterion("encased_corythosaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_corythosaurus_fossil")))
                .addCriterion("encased_crassigyrinus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_crassigyrinus_fossil")))
                .addCriterion("encased_deinotherium_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_deinotherium_fossil")))
                .addCriterion("encased_delta_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_delta_fossil")))
                .addCriterion("encased_dilophosaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_dilophosaurus_fossil")))
                .addCriterion("encased_dimetrodon_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_dimetrodon_fossil")))
                .addCriterion("encased_dimorphodon_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_dimorphodon_fossil")))
                .addCriterion("encased_diplocaulus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_diplocaulus_fossil")))
                .addCriterion("encased_diplodocus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_diplodocus_fossil")))
                .addCriterion("encased_dodo_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_dodo_fossil")))
                .addCriterion("encased_dreadnoughtus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_dreadnoughtus_fossil")))
                .addCriterion("encased_dunkleosteus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_dunkleosteus_fossil")))
                .addCriterion("encased_echo_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_echo_fossil")))
                .addCriterion("encased_edmontosaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_edmontosaurus_fossil")))
                .addCriterion("encased_elasmotherium_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_elasmotherium_fossil")))
                .addCriterion("encased_gallimimus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_gallimimus_fossil")))
                .addCriterion("encased_giganotosaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_giganotosaurus_fossil")))
                .addCriterion("encased_guanlong_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_guanlong_fossil")))
                .addCriterion("encased_herrerasaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_herrerasaurus_fossil")))
                .addCriterion("encased_hyaenodon_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_hyaenodon_fossil")))
                .addCriterion("encased_hypsilophodon_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_hypsilophodon_fossil")))
                .addCriterion("encased_indominus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_indominus_fossil")))
                .addCriterion("encased_indoraptor_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_indoraptor_fossil")))
                .addCriterion("encased_lambeosaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_lambeosaurus_fossil")))
                .addCriterion("encased_leaellynasaura_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_leaellynasaura_fossil")))
                .addCriterion("encased_leptictidium_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_leptictidium_fossil")))
                .addCriterion("encased_livyatan_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_livyatan_fossil")))
                .addCriterion("encased_ludodactylus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_ludodactylus_fossil")))
                .addCriterion("encased_majungasaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_majungasaurus_fossil")))
                .addCriterion("encased_mamenchisaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_mamenchisaurus_fossil")))
                .addCriterion("encased_mammoth_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_mammoth_fossil")))
                .addCriterion("encased_mawsonia_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_mawsonia_fossil")))
                .addCriterion("encased_megapiranha_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_megapiranha_fossil")))
                .addCriterion("encased_megatherium_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_megatherium_fossil")))
                .addCriterion("encased_metriacanthosaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_metriacanthosaurus_fossil")))
                .addCriterion("encased_microceratus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_microceratus_fossil")))
                .addCriterion("encased_microraptor_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_microraptor_fossil")))
                .addCriterion("encased_moganopterus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_moganopterus_fossil")))
                .addCriterion("encased_mosasaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_mosasaurus_fossil")))
                .addCriterion("encased_mussaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_mussaurus_fossil")))
                .addCriterion("encased_ornithomimus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_ornithomimus_fossil")))
                .addCriterion("encased_othnielia_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_othnielia_fossil")))
                .addCriterion("encased_oviraptor_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_oviraptor_fossil")))
                .addCriterion("encased_pachycephalosaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_pachycephalosaurus_fossil")))
                .addCriterion("encased_paraceratherium_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_paraceratherium_fossil")))
                .addCriterion("encased_parapuzosia_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_parapuzosia_fossil")))
                .addCriterion("encased_parasaurolophus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_parasaurolophus_fossil")))
                .addCriterion("encased_postosuchus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_postosuchus_fossil")))
                .addCriterion("encased_proceratosaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_proceratosaurus_fossil")))
                .addCriterion("encased_protoceratops_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_protoceratops_fossil")))
                .addCriterion("encased_pteranodon_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_pteranodon_fossil")))
                .addCriterion("encased_quetzalcoatlus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_quetzalcoatlus_fossil")))
                .addCriterion("encased_raphusrex_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_raphusrex_fossil")))
                .addCriterion("encased_rugops_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_rugops_fossil")))
                .addCriterion("encased_segisaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_segisaurus_fossil")))
                .addCriterion("encased_sinoceratops_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_sinoceratops_fossil")))
                .addCriterion("encased_smilodon_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_smilodon_fossil")))
                .addCriterion("encased_spinoraptor_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_spinoraptor_fossil")))
                .addCriterion("encased_spinosaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_spinosaurus_fossil")))
                .addCriterion("encased_stegosaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_stegosaurus_fossil")))
                .addCriterion("encased_styracosaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_styracosaurus_fossil")))
                .addCriterion("encased_suchomimus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_suchomimus_fossil")))
                .addCriterion("encased_therizinosaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_therizinosaurus_fossil")))
                .addCriterion("encased_titanis_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_titanis_fossil")))
                .addCriterion("encased_titanites_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_titanites_fossil")))
                .addCriterion("encased_triceratops_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_triceratops_fossil")))
                .addCriterion("encased_troodon_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_troodon_fossil")))
                .addCriterion("encased_tropeognathus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_tropeognathus_fossil")))
                .addCriterion("encased_tylosaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_tylosaurus_fossil")))
                .addCriterion("encased_tyrannosaurus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_tyrannosaurus_fossil")))
                .addCriterion("encased_vectipelta_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_vectipelta_fossil")))
                .addCriterion("encased_velociraptor_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_velociraptor_fossil")))
                .addCriterion("encased_zhenyuanopterus_fossil", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:encased_zhenyuanopterus_fossil")))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(consumer, MODID + ":jurassicreborn/encased_fossil");

        // cleaning_station
        AdvancementHolder cleaning_station = Advancement.Builder.advancement()
                .display(item("jurassicreborn:cleaning_station"),
                        title("cleaning_station"), description("cleaning_station"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(encased_fossil)
                .addCriterion("cleaning_station",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:cleaning_station")))
                .save(consumer, MODID + ":jurassicreborn/cleaning_station");

        // fossil_grinder
        AdvancementHolder fossil_grinder = Advancement.Builder.advancement()
                .display(item("jurassicreborn:fossil_grinder"),
                        title("fossil_grinder"), description("fossil_grinder"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(cleaning_station)
                .addCriterion("fossil_grinder",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:fossil_grinder")))
                .save(consumer, MODID + ":jurassicreborn/fossil_grinder");

        // skeleton_assembly
        AdvancementHolder skeleton_assembly = Advancement.Builder.advancement()
                .display(item("jurassicreborn:skeleton_assembly"),
                        title("skeleton_assembly"), description("skeleton_assembly"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(fossil_grinder)
                .addCriterion("skeleton_assembly",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:skeleton_assembly")))
                .save(consumer, MODID + ":jurassicreborn/skeleton_assembly");

        // dna_sequencer
        AdvancementHolder dna_sequencer = Advancement.Builder.advancement()
                .display(item("jurassicreborn:dna_sequencer"),
                        title("dna_sequencer"), description("dna_sequencer"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(fossil_grinder)
                .addCriterion("dna_sequencer",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna_sequencer")))
                .save(consumer, MODID + ":jurassicreborn/dna_sequencer");

        // dna_combinator_hybridizer
        AdvancementHolder dna_combinator_hybridizer = Advancement.Builder.advancement()
                .display(item("jurassicreborn:dna_combinator_hybridizer"),
                        title("dna_combinator_hybridizer"), description("dna_combinator_hybridizer"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(dna_sequencer)
                .addCriterion("dna_combinator_hybridizer",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna_combinator_hybridizer")))
                .save(consumer, MODID + ":jurassicreborn/dna_combinator_hybridizer");

        // dna_synthesizer
        AdvancementHolder dna_synthesizer = Advancement.Builder.advancement()
                .display(item("jurassicreborn:dna_synthesizer"),
                        title("dna_synthesizer"), description("dna_synthesizer"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(dna_combinator_hybridizer)
                .addCriterion("dna_synthesizer",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna_synthesizer")))
                .save(consumer, MODID + ":jurassicreborn/dna_synthesizer");

        // embryonic_machine
        AdvancementHolder embryonic_machine = Advancement.Builder.advancement()
                .display(item("jurassicreborn:embryonic_machine"),
                        title("embryonic_machine"), description("embryonic_machine"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(dna_synthesizer)
                .addCriterion("embryonic_machine",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:embryonic_machine")))
                .save(consumer, MODID + ":jurassicreborn/embryonic_machine");

        // embryo_calcification_machine
        AdvancementHolder embryo_calcification_machine = Advancement.Builder.advancement()
                .display(item("jurassicreborn:embryo_calcification_machine"),
                        title("embryo_calcification_machine"), description("embryo_calcification_machine"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(embryonic_machine)
                .addCriterion("embryo_calcification_machine",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:embryo_calcification_machine")))
                .save(consumer, MODID + ":jurassicreborn/embryo_calcification_machine");

        // dino_egg (challenge)
        AdvancementHolder dino_egg = Advancement.Builder.advancement()
                .display(item("jurassicreborn:egg/egg_mamenchisaurus"),
                        title("dino_egg"), description("dino_egg"),
                        null, AdvancementType.CHALLENGE, true, true, false)
                .parent(embryo_calcification_machine)
                .addCriterion("egg_achillobator", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_achillobator")))
                .addCriterion("egg_alligator_gar", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_alligator_gar")))
                .addCriterion("egg_allosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_allosaurus")))
                .addCriterion("egg_alvarezsaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_alvarezsaurus")))
                .addCriterion("egg_perisphinctes", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_perisphinctes")))
                .addCriterion("egg_ankylodocus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_ankylodocus")))
                .addCriterion("egg_ankylosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_ankylosaurus")))
                .addCriterion("egg_apatosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_apatosaurus")))
                .addCriterion("egg_arsinoitherium", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_arsinoitherium")))
                .addCriterion("egg_asteroceras", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_asteroceras")))
                .addCriterion("egg_cameroceras", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_cameroceras")))
                .addCriterion("egg_endoceras", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_endoceras")))
                .addCriterion("egg_orthoceras", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_orthoceras")))
                .addCriterion("egg_megalodon", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_megalodon")))
                .addCriterion("egg_deinosuchus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_deinosuchus")))
                .addCriterion("egg_kairuku", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_kairuku")))
                .addCriterion("egg_nigersaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_nigersaurus")))
                .addCriterion("egg_baryonyx", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_baryonyx")))
                .addCriterion("egg_beelzebufo", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_beelzebufo")))
                .addCriterion("egg_blue", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_blue")))
                .addCriterion("egg_brachiosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_brachiosaurus")))
                .addCriterion("egg_camarasaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_camarasaurus")))
                .addCriterion("egg_carcharodontosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_carcharodontosaurus")))
                .addCriterion("egg_carnotaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_carnotaurus")))
                .addCriterion("egg_cearadactylus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_cearadactylus")))
                .addCriterion("egg_ceratosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_ceratosaurus")))
                .addCriterion("egg_charlie", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_charlie")))
                .addCriterion("egg_chasmosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_chasmosaurus")))
                .addCriterion("egg_chilesaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_chilesaurus")))
                .addCriterion("egg_coelacanth", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_coelacanth")))
                .addCriterion("egg_coelurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_coelurus")))
                .addCriterion("egg_compsognathus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_compsognathus")))
                .addCriterion("egg_corythosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_corythosaurus")))
                .addCriterion("egg_crassigyrinus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_crassigyrinus")))
                .addCriterion("egg_deinotherium", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_deinotherium")))
                .addCriterion("egg_delta", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_delta")))
                .addCriterion("egg_dilophosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_dilophosaurus")))
                .addCriterion("egg_dimetrodon", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_dimetrodon")))
                .addCriterion("egg_dimorphodon", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_dimorphodon")))
                .addCriterion("egg_diplocaulus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_diplocaulus")))
                .addCriterion("egg_diplodocus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_diplodocus")))
                .addCriterion("egg_dodo", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_dodo")))
                .addCriterion("egg_dreadnoughtus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_dreadnoughtus")))
                .addCriterion("egg_dunkleosteus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_dunkleosteus")))
                .addCriterion("egg_echo", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_echo")))
                .addCriterion("egg_edmontosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_edmontosaurus")))
                .addCriterion("egg_elasmotherium", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_elasmotherium")))
                .addCriterion("egg_gallimimus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_gallimimus")))
                .addCriterion("egg_giganotosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_giganotosaurus")))
                .addCriterion("egg_guanlong", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_guanlong")))
                .addCriterion("egg_herrerasaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_herrerasaurus")))
                .addCriterion("egg_hyaenodon", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_hyaenodon")))
                .addCriterion("egg_hypsilophodon", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_hypsilophodon")))
                .addCriterion("egg_indominus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_indominus")))
                .addCriterion("egg_indoraptor", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_indoraptor")))
                .addCriterion("egg_lambeosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_lambeosaurus")))
                .addCriterion("egg_leaellynasaura", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_leaellynasaura")))
                .addCriterion("egg_leptictidium", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_leptictidium")))
                .addCriterion("egg_ludodactylus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_ludodactylus")))
                .addCriterion("egg_majungasaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_majungasaurus")))
                .addCriterion("egg_mamenchisaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_mamenchisaurus")))
                .addCriterion("egg_mammoth", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_mammoth")))
                .addCriterion("egg_mawsonia", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_mawsonia")))
                .addCriterion("egg_megapiranha", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_megapiranha")))
                .addCriterion("egg_megatherium", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_megatherium")))
                .addCriterion("egg_metriacanthosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_metriacanthosaurus")))
                .addCriterion("egg_microceratus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_microceratus")))
                .addCriterion("egg_microraptor", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_microraptor")))
                .addCriterion("egg_moganopterus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_moganopterus")))
                .addCriterion("egg_mosasaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_mosasaurus")))
                .addCriterion("egg_mussaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_mussaurus")))
                .addCriterion("egg_ornithomimus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_ornithomimus")))
                .addCriterion("egg_othnielia", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_othnielia")))
                .addCriterion("egg_oviraptor", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_oviraptor")))
                .addCriterion("egg_pachycephalosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_pachycephalosaurus")))
                .addCriterion("egg_paraceratherium", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_paraceratherium")))
                .addCriterion("egg_parapuzosia", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_parapuzosia")))
                .addCriterion("egg_parasaurolophus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_parasaurolophus")))
                .addCriterion("egg_postosuchus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_postosuchus")))
                .addCriterion("egg_proceratosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_proceratosaurus")))
                .addCriterion("egg_protoceratops", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_protoceratops")))
                .addCriterion("egg_pteranodon", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_pteranodon")))
                .addCriterion("egg_quetzalcoatlus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_quetzalcoatlus")))
                .addCriterion("egg_raphusrex", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_raphusrex")))
                .addCriterion("egg_rugops", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_rugops")))
                .addCriterion("egg_segisaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_segisaurus")))
                .addCriterion("egg_sinoceratops", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_sinoceratops")))
                .addCriterion("egg_smilodon", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_smilodon")))
                .addCriterion("egg_spinoraptor", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_spinoraptor")))
                .addCriterion("egg_spinosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_spinosaurus")))
                .addCriterion("egg_stegosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_stegosaurus")))
                .addCriterion("egg_styracosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_styracosaurus")))
                .addCriterion("egg_suchomimus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_suchomimus")))
                .addCriterion("egg_therizinosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_therizinosaurus")))
                .addCriterion("egg_titanis", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_titanis")))
                .addCriterion("egg_titanites", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_titanites")))
                .addCriterion("egg_triceratops", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_triceratops")))
                .addCriterion("egg_troodon", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_troodon")))
                .addCriterion("egg_tropeognathus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_tropeognathus")))
                .addCriterion("egg_tylosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_tylosaurus")))
                .addCriterion("egg_tyrannosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_tyrannosaurus")))
                .addCriterion("egg_vectipelta", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_vectipelta")))
                .addCriterion("egg_velociraptor", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_velociraptor")))
                .addCriterion("egg_zhenyuanopterus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:egg/egg_zhenyuanopterus")))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(consumer, MODID + ":jurassicreborn/dino_egg");

        // incubator
        AdvancementHolder incubator = Advancement.Builder.advancement()
                .display(item("jurassicreborn:incubator"),
                        title("incubator"), description("incubator"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(dino_egg)
                .addCriterion("incubator",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:incubator")))
                .save(consumer, MODID + ":jurassicreborn/incubator");

        // Hatched egg line (all parented to incubator)
        AdvancementHolder tylosaurus_hatched_egg = Advancement.Builder.advancement()
                .display(item("jurassicreborn:hatched_egg/egg_tylosaurus"),
                        title("tylosaurus_hatched_egg"), description("tylosaurus_hatched_egg"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(incubator)
                .addCriterion("hatched_egg",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_tylosaurus")))
                .save(consumer, MODID + ":jurassicreborn/tylosaurus_hatched_egg");
        AdvancementHolder compsognathus_hatched_egg = Advancement.Builder.advancement()
                .display(item("jurassicreborn:hatched_egg/egg_compsognathus"),
                        title("compsognathus_hatched_egg"), description("compsognathus_hatched_egg"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(incubator)
                .addCriterion("hatched_egg",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_compsognathus")))
                .save(consumer, MODID + ":jurassicreborn/compsognathus_hatched_egg");

        AdvancementHolder spinosaurus_hatched_egg = Advancement.Builder.advancement()
                .display(item("jurassicreborn:hatched_egg/egg_spinosaurus"),
                        title("spinosaurus_hatched_egg"), description("spinosaurus_hatched_egg"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(incubator)
                .addCriterion("hatched_egg",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_spinosaurus")))
                .save(consumer, MODID + ":jurassicreborn/spinosaurus_hatched_egg");

        AdvancementHolder mosasaurus_hatched_egg = Advancement.Builder.advancement()
                .display(item("jurassicreborn:hatched_egg/egg_mosasaurus"),
                        title("mosasaurus_hatched_egg"), description("mosasaurus_hatched_egg"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(incubator)
                .addCriterion("hatched_egg",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_mosasaurus")))
                .save(consumer, MODID + ":jurassicreborn/mosasaurus_hatched_egg");

        AdvancementHolder giganotosaurus_hatched_egg = Advancement.Builder.advancement()
                .display(item("jurassicreborn:hatched_egg/egg_giganotosaurus"),
                        title("giganotosaurus_hatched_egg"), description("giganotosaurus_hatched_egg"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(incubator)
                .addCriterion("hatched_egg",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_giganotosaurus")))
                .save(consumer, MODID + ":jurassicreborn/giganotosaurus_hatched_egg");

        AdvancementHolder velociraptor_hatched_egg = Advancement.Builder.advancement()
                .display(item("jurassicreborn:hatched_egg/egg_velociraptor"),
                        title("velociraptor_hatched_egg"), description("velociraptor_hatched_egg"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(incubator)
                .addCriterion("hatched_egg",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_velociraptor")))
                .save(consumer, MODID + ":jurassicreborn/velociraptor_hatched_egg");

        AdvancementHolder brachiosaurus_hatched_egg = Advancement.Builder.advancement()
                .display(item("jurassicreborn:hatched_egg/egg_brachiosaurus"),
                        title("brachiosaurus_hatched_egg"), description("brachiosaurus_hatched_egg"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(incubator)
                .addCriterion("hatched_egg",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_brachiosaurus")))
                .save(consumer, MODID + ":jurassicreborn/brachiosaurus_hatched_egg");

        AdvancementHolder mamenchisaurus_hatched_egg = Advancement.Builder.advancement()
                .display(item("jurassicreborn:hatched_egg/egg_mamenchisaurus"),
                        title("mamenchisaurus_hatched_egg"), description("mamenchisaurus_hatched_egg"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(incubator)
                .addCriterion("hatched_egg",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_mamenchisaurus")))
                .save(consumer, MODID + ":jurassicreborn/mamenchisaurus_hatched_egg");

        AdvancementHolder dilophosaurus_hatched_egg = Advancement.Builder.advancement()
                .display(item("jurassicreborn:hatched_egg/egg_dilophosaurus"),
                        title("dilophosaurus_hatched_egg"), description("dilophosaurus_hatched_egg"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(incubator)
                .addCriterion("hatched_egg",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_dilophosaurus")))
                .save(consumer, MODID + ":jurassicreborn/dilophosaurus_hatched_egg");

        AdvancementHolder arsinoitherium_hatched_egg = Advancement.Builder.advancement()
                .display(item("jurassicreborn:hatched_egg/egg_arsinoitherium"),
                        title("arsinoitherium_hatched_egg"), description("arsinoitherium_hatched_egg"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(incubator)
                .addCriterion("hatched_egg",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_arsinoitherium")))
                .save(consumer, MODID + ":jurassicreborn/arsinoitherium_hatched_egg");

        AdvancementHolder troodon_hatched_egg = Advancement.Builder.advancement()
                .display(item("jurassicreborn:hatched_egg/egg_troodon"),
                        title("troodon_hatched_egg"), description("troodon_hatched_egg"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(incubator)
                .addCriterion("hatched_egg",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_troodon")))
                .save(consumer, MODID + ":jurassicreborn/troodon_hatched_egg");

        AdvancementHolder parasaurolophus_hatched_egg = Advancement.Builder.advancement()
                .display(item("jurassicreborn:hatched_egg/egg_parasaurolophus"),
                        title("parasaurolophus_hatched_egg"), description("parasaurolophus_hatched_egg"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(incubator)
                .addCriterion("hatched_egg",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_parasaurolophus")))
                .save(consumer, MODID + ":jurassicreborn/parasaurolophus_hatched_egg");

        AdvancementHolder tyrannosaurus_hatched_egg = Advancement.Builder.advancement()
                .display(item("jurassicreborn:hatched_egg/egg_tyrannosaurus"),
                        title("tyrannosaurus_hatched_egg"), description("tyrannosaurus_hatched_egg"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(incubator)
                .addCriterion("hatched_egg",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_tyrannosaurus")))
                .save(consumer, MODID + ":jurassicreborn/tyrannosaurus_hatched_egg");

        AdvancementHolder coelacanth_hatched_egg = Advancement.Builder.advancement()
                .display(item("jurassicreborn:hatched_egg/egg_coelacanth"),
                        title("coelacanth_hatched_egg"), description("coelacanth_hatched_egg"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(incubator)
                .addCriterion("hatched_egg",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_coelacanth")))
                .save(consumer, MODID + ":jurassicreborn/coelacanth_hatched_egg");

        AdvancementHolder beelzebufo_hatched_egg = Advancement.Builder.advancement()
                .display(item("jurassicreborn:hatched_egg/egg_beelzebufo"),
                        title("beelzebufo_hatched_egg"), description("beelzebufo_hatched_egg"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(incubator)
                .addCriterion("hatched_egg",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_beelzebufo")))
                .save(consumer, MODID + ":jurassicreborn/beelzebufo_hatched_egg");

        // dna_extractor
        AdvancementHolder dna_extractor = Advancement.Builder.advancement()
                .display(item("jurassicreborn:dna_extractor"),
                        title("dna_extractor"), description("dna_extractor"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(dna_combinator_hybridizer)
                .addCriterion("dna_extractor",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna_extractor")))
                .save(consumer, MODID + ":jurassicreborn/dna_extractor");

        // amber (challenge)
        AdvancementHolder amber = Advancement.Builder.advancement()
                .display(item("jurassicreborn:amber_mosquito"),
                        title("amber"), description("amber"),
                        null, AdvancementType.CHALLENGE, true, true, false)
                .parent(dna_extractor)
                .addCriterion("sea_lamprey",   InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:sea_lamprey")))
                .addCriterion("amber",         InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:amber_mosquito")))
                .addCriterion("frozen_leech",  InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:frozen_leech")))
                .addCriterion("amber_1",       InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:amber_aphid")))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(consumer, MODID + ":jurassicreborn/amber");

        // dna (challenge)
        AdvancementHolder dna = Advancement.Builder.advancement()
                .display(item("jurassicreborn:mr_dna_keychain"),
                        title("dna"), description("dna"),
                        null, AdvancementType.CHALLENGE, true, true, false)
                .parent(dna_synthesizer)
                .addCriterion("dna_achillobator", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_achillobator")))
                .addCriterion("dna_alligator_gar", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_alligator_gar")))
                .addCriterion("dna_allosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_allosaurus")))
                .addCriterion("dna_alvarezsaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_alvarezsaurus")))
                .addCriterion("dna_perisphinctes", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_perisphinctes")))
                .addCriterion("dna_ankylodocus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_ankylodocus")))
                .addCriterion("dna_ankylosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_ankylosaurus")))
                .addCriterion("dna_apatosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_apatosaurus")))
                .addCriterion("dna_arsinoitherium", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_arsinoitherium")))
                .addCriterion("dna_asteroceras", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_asteroceras")))
                .addCriterion("dna_cameroceras", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_cameroceras")))
                .addCriterion("dna_endoceras", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_endoceras")))
                .addCriterion("dna_orthoceras", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_orthoceras")))
                .addCriterion("dna_megalodon", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_megalodon")))
                .addCriterion("dna_nigersaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_nigersaurus")))
                .addCriterion("dna_kairuku", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_kairuku")))
                .addCriterion("dna_deinosuchus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_deinosuchus")))
                .addCriterion("dna_baryonyx", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_baryonyx")))
                .addCriterion("dna_beelzebufo", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_beelzebufo")))
                .addCriterion("dna_blue", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_blue")))
                .addCriterion("dna_brachiosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_brachiosaurus")))
                .addCriterion("dna_camarasaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_camarasaurus")))
                .addCriterion("dna_carcharodontosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_carcharodontosaurus")))
                .addCriterion("dna_carnotaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_carnotaurus")))
                .addCriterion("dna_cearadactylus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_cearadactylus")))
                .addCriterion("dna_ceratosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_ceratosaurus")))
                .addCriterion("dna_charlie", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_charlie")))
                .addCriterion("dna_chasmosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_chasmosaurus")))
                .addCriterion("dna_chilesaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_chilesaurus")))
                .addCriterion("dna_coelacanth", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_coelacanth")))
                .addCriterion("dna_coelurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_coelurus")))
                .addCriterion("dna_compsognathus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_compsognathus")))
                .addCriterion("dna_corythosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_corythosaurus")))
                .addCriterion("dna_crassigyrinus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_crassigyrinus")))
                .addCriterion("dna_deinotherium", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_deinotherium")))
                .addCriterion("dna_delta", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_delta")))
                .addCriterion("dna_dilophosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_dilophosaurus")))
                .addCriterion("dna_dimetrodon", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_dimetrodon")))
                .addCriterion("dna_dimorphodon", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_dimorphodon")))
                .addCriterion("dna_diplocaulus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_diplocaulus")))
                .addCriterion("dna_diplodocus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_diplodocus")))
                .addCriterion("dna_dodo", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_dodo")))
                .addCriterion("dna_dreadnoughtus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_dreadnoughtus")))
                .addCriterion("dna_dunkleosteus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_dunkleosteus")))
                .addCriterion("dna_echo", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_echo")))
                .addCriterion("dna_edmontosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_edmontosaurus")))
                .addCriterion("dna_elasmotherium", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_elasmotherium")))
                .addCriterion("dna_gallimimus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_gallimimus")))
                .addCriterion("dna_giganotosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_giganotosaurus")))
                .addCriterion("dna_guanlong", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_guanlong")))
                .addCriterion("dna_herrerasaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_herrerasaurus")))
                .addCriterion("dna_hyaenodon", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_hyaenodon")))
                .addCriterion("dna_hypsilophodon", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_hypsilophodon")))
                .addCriterion("dna_indominus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_indominus")))
                .addCriterion("dna_indoraptor", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_indoraptor")))
                .addCriterion("dna_lambeosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_lambeosaurus")))
                .addCriterion("dna_leaellynasaura", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_leaellynasaura")))
                .addCriterion("dna_leptictidium", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_leptictidium")))
                .addCriterion("dna_ludodactylus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_ludodactylus")))
                .addCriterion("dna_majungasaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_majungasaurus")))
                .addCriterion("dna_mamenchisaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_mamenchisaurus")))
                .addCriterion("dna_mammoth", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_mammoth")))
                .addCriterion("dna_mawsonia", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_mawsonia")))
                .addCriterion("dna_megapiranha", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_megapiranha")))
                .addCriterion("dna_megatherium", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_megatherium")))
                .addCriterion("dna_metriacanthosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_metriacanthosaurus")))
                .addCriterion("dna_microceratus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_microceratus")))
                .addCriterion("dna_microraptor", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_microraptor")))
                .addCriterion("dna_moganopterus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_moganopterus")))
                .addCriterion("dna_mosasaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_mosasaurus")))
                .addCriterion("dna_mussaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_mussaurus")))
                .addCriterion("dna_ornithomimus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_ornithomimus")))
                .addCriterion("dna_othnielia", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_othnielia")))
                .addCriterion("dna_oviraptor", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_oviraptor")))
                .addCriterion("dna_pachycephalosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_pachycephalosaurus")))
                .addCriterion("dna_paraceratherium", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_paraceratherium")))
                .addCriterion("dna_parapuzosia", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_parapuzosia")))
                .addCriterion("dna_parasaurolophus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_parasaurolophus")))
                .addCriterion("dna_postosuchus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_postosuchus")))
                .addCriterion("dna_proceratosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_proceratosaurus")))
                .addCriterion("dna_protoceratops", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_protoceratops")))
                .addCriterion("dna_pteranodon", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_pteranodon")))
                .addCriterion("dna_quetzalcoatlus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_quetzalcoatlus")))
                .addCriterion("dna_raphusrex", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_raphusrex")))
                .addCriterion("dna_rugops", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_rugops")))
                .addCriterion("dna_segisaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_segisaurus")))
                .addCriterion("dna_sinoceratops", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_sinoceratops")))
                .addCriterion("dna_smilodon", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_smilodon")))
                .addCriterion("dna_spinoraptor", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_spinoraptor")))
                .addCriterion("dna_spinosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_spinosaurus")))
                .addCriterion("dna_stegosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_stegosaurus")))
                .addCriterion("dna_styracosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_styracosaurus")))
                .addCriterion("dna_suchomimus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_suchomimus")))
                .addCriterion("dna_therizinosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_therizinosaurus")))
                .addCriterion("dna_titanis", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_titanis")))
                .addCriterion("dna_titanites", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_titanites")))
                .addCriterion("dna_triceratops", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_triceratops")))
                .addCriterion("dna_troodon", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_troodon")))
                .addCriterion("dna_tropeognathus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_tropeognathus")))
                .addCriterion("dna_tylosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_tylosaurus")))
                .addCriterion("dna_tyrannosaurus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_tyrannosaurus")))
                .addCriterion("dna_vectipelta", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_vectipelta")))
                .addCriterion("dna_velociraptor", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_velociraptor")))
                .addCriterion("dna_zhenyuanopterus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:dna/dna_zhenyuanopterus")))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(consumer, MODID + ":jurassicreborn/dna");

        // indominus_hatched_egg (challenge)
        AdvancementHolder indominus_hatched_egg = Advancement.Builder.advancement()
                .display(item("jurassicreborn:hatched_egg/egg_indominus"),
                        title("indominus_hatched_egg"), description("indominus_hatched_egg"),
                        null, AdvancementType.CHALLENGE, true, true, false)
                .parent(dna_combinator_hybridizer)
                .addCriterion("hatched_egg",
                        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_indominus")))
                .save(consumer, MODID + ":jurassicreborn/indominus_hatched_egg");

        // hybrids_dino_egg (challenge, multiple)
        AdvancementHolder hybrids_dino_egg = Advancement.Builder.advancement()
                .display(item("jurassicreborn:mr_dna_keychain"),
                        title("hybrids_dino_egg"), description("hybrids_dino_egg"),
                        null, AdvancementType.CHALLENGE, true, true, false)
                .parent(dna_combinator_hybridizer)
                .addCriterion("hatched_egg_0", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_ankylodocus")))
                .addCriterion("hatched_egg_1", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_spinoraptor")))
                .addCriterion("hatched_egg_2", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_raphusrex")))
                .addCriterion("hatched_egg_3", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_blue")))
                .addCriterion("hatched_egg_4", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_charlie")))
                .addCriterion("hatched_egg_5", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_echo")))
                .addCriterion("hatched_egg_6", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_delta")))
                .addCriterion("hatched_egg_7", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_indoraptor")))
                .addCriterion("hatched_egg_8", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:hatched_egg/egg_indoraptor")))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(consumer, MODID + ":jurassicreborn/hybrids_dino_egg");

        // plant branch
        AdvancementHolder plant_fossils = Advancement.Builder.advancement()
                .display(item("jurassicreborn:flora_fossil"),
                        title("plant_fossils"), description("plant_fossils"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(root)
                .addCriterion("plant_fossil",       InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:plant_fossil")))
                .addCriterion("plant_fossil_0",       InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:plant_fossil_0")))
                .addCriterion("plant_fossil_1",       InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:plant_fossil_1")))
                .addCriterion("plant_fossil_2",       InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:plant_fossil_2")))
                .addCriterion("plant_fossil_3",       InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:plant_fossil_3")))
                .addCriterion("twig_fossil",        InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:twig_fossil")))
                .addCriterion("petrified_ginkgo_log",   InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:petrified_ginkgo_log")))
                .addCriterion("petrified_psaronius_log",InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:petrified_psaronius_log")))
                .addCriterion("petrified_calamites_log",InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:petrified_calamites_log")))
                .addCriterion("petrified_araucaria_log",InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:petrified_araucaria_log")))
                .addCriterion("petrified_magnolia_log",InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:petrified_magnolia_log")))
                .addCriterion("petrified_phoenix_log",  InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:petrified_phoenix_log")))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(consumer, MODID + ":jurassicreborn/plant_fossils");
        AdvancementHolder vehicles = Advancement.Builder.advancement()
                .display(item("jurassicreborn:jeep_wrangler"),
                        title("jeep_wrangler"), description("jeep_wrangler"),
                        null, AdvancementType.TASK, true, true, true)
                .parent(root)
                .addCriterion("jeep_wrangler",       InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:jeep_wrangler")))
                .addCriterion("black_jeep_wrangler",       InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:black_jeep_wrangler")))
                .addCriterion("blue_jeep_wrangler",       InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:blue_jeep_wrangler")))
                .addCriterion("green_jeep_wrangler",       InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:green_jeep_wrangler")))
                .addCriterion("lime_jeep_wrangler",       InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:lime_jeep_wrangler")))
                .addCriterion("pink_jeep_wrangler",       InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:pink_jeep_wrangler")))
                .addCriterion("purple_jeep_wrangler",       InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:purple_jeep_wrangler")))
                .addCriterion("sorna_jeep_wrangler",       InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:sorna_jeep_wrangler")))
                .addCriterion("ford_explorer",       InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:ford_explorer")))
                .addCriterion("ford_explorer_snow",       InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:ford_explorer_snow")))
                .addCriterion("helicopter",       InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:helicopter")))
                .addCriterion("monorail",       InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:monorail")))
                .addCriterion("gyrosphere",       InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:gyrosphere")))
                .requirements(AdvancementRequirements.Strategy.OR)
                .save(consumer, MODID + ":jurassicreborn/vehicles");

        AdvancementHolder plant_dna = Advancement.Builder.advancement()
                .display(item("jurassicreborn:mr_dna_keychain"),
                        title("plant_dna"), description("plant_dna"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(plant_fossils)
                .addCriterion("plant_dna", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:plant_dna")))
                .save(consumer, MODID + ":jurassicreborn/plant_dna");

        AdvancementHolder plant_callus = Advancement.Builder.advancement()
                .display(item("jurassicreborn:enallhelia"),
                        title("plant_callus"), description("plant_callus"),
                        null, AdvancementType.TASK, true, true, false)
                .parent(plant_dna)
                .addCriterion("plant_callus", InventoryChangeTrigger.TriggerInstance.hasItems(item("jurassicreborn:plant_callus")))
                .save(consumer, MODID + ":jurassicreborn/plant_callus");
    }
}
