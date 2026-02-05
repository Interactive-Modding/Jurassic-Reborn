package net.vit.jurassicreborn.common.datagen;

import net.vit.jurassicreborn.common.entities.ModEntities;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Collectors;

public class JREntityLoot extends EntityLoot {

    @Override
    protected void addTables() {
        add(ModEntities.CRAB.get(), LootTable.lootTable());
        add(ModEntities.SHARK.get(), LootTable.lootTable());
        add(ModEntities.GOAT.get(), LootTable.lootTable());

        add(ModEntities.OVIRAPTOR.get(), LootTable.lootTable());
        add(ModEntities.DEINOTHERIUM.get(), LootTable.lootTable());
        add(ModEntities.MICRORAPTOR.get(), LootTable.lootTable());
        add(ModEntities.MAMMOTH.get(), LootTable.lootTable());
        add(ModEntities.DODO.get(), LootTable.lootTable());
        add(ModEntities.ZHENYUANOPTERUS.get(), LootTable.lootTable());
        add(ModEntities.POSTOSUCHUS.get(), LootTable.lootTable());
        add(ModEntities.INDORAPTOR.get(), LootTable.lootTable());
        add(ModEntities.OTHNIELIA.get(), LootTable.lootTable());
        add(ModEntities.PTERANODON.get(), LootTable.lootTable());
        add(ModEntities.INDOMINUS.get(), LootTable.lootTable());
        add(ModEntities.ANKYLOSAURUS.get(), LootTable.lootTable());
        add(ModEntities.ARSINOITHERIUM.get(), LootTable.lootTable());
        add(ModEntities.CRASSIGYRINUS.get(), LootTable.lootTable());
        add(ModEntities.PERISPHINCTES.get(), LootTable.lootTable());
        add(ModEntities.PROCERATOSAURUS.get(), LootTable.lootTable());
        add(ModEntities.APATOSAURUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.CARNOTAURUS.get(), LootTable.lootTable());
        add(ModEntities.DUNKLEOSTEUS.get(), LootTable.lootTable());
        add(ModEntities.TYRANNOSAURUS.get(), LootTable.lootTable());
        add(ModEntities.RAPHUSREX.get(), LootTable.lootTable());
        add(ModEntities.CHASMOSAURUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.METRIACANTHOSAURUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.TROODON.get(), LootTable.lootTable());
        add(ModEntities.HERRERASAURUS.get(), LootTable.lootTable());
        add(ModEntities.BARYONYX.get(), LootTable.lootTable());
        add(ModEntities.BEELZEBUFO_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.VELOCIRAPTORBLUE_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.VELOCIRAPTORECHO_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.SINOCERATOPS.get(), LootTable.lootTable());
        add(ModEntities.PARASAUROLOPHUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.MAMENCHISAURUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.DIMORPHODON_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.ALLOSAURUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.MOSASAURUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.MAWSONIA_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.VELOCIRAPTORDELTA.get(), LootTable.lootTable());
        add(ModEntities.ALVAREZSAURUS.get(), LootTable.lootTable());
        add(ModEntities.RUGOPS.get(), LootTable.lootTable());
        add(ModEntities.CEARADACTYLUS.get(), LootTable.lootTable());
        add(ModEntities.CORYTHOSAURUS.get(), LootTable.lootTable());
        add(ModEntities.COMPSOGNATHUS.get(), LootTable.lootTable());
        add(ModEntities.LUDODACTYLUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.LEAELLYNASAURA_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.MOGANOPTERUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.SUCHOMIMUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.MAJUNGASAURUS.get(), LootTable.lootTable());
        add(ModEntities.PROTOCERATOPS.get(), LootTable.lootTable());
        add(ModEntities.TITANIS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.COELACANTH_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.GALLIMIMUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.CERATOSAURUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.VELOCIRAPTORCHARLIE_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.SPINOSAURUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.PACHYCEPHALOSAURUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.QUETZAL.get(), LootTable.lootTable());
        add(ModEntities.CARCHARODONTOSAURUS.get(), LootTable.lootTable());
        add(ModEntities.TYLOSAURUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.ORNITHOMIMUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.MEGAPIRANHA_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.DIPLODOCUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.DILOPHOSAURUS.get(), LootTable.lootTable());
        add(ModEntities.STYRACOSAURUS.get(), LootTable.lootTable());
        add(ModEntities.GUANLONG_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.CAMARASAURUS.get(), LootTable.lootTable());
        add(ModEntities.NIGERSAURUS.get(), LootTable.lootTable());
        add(ModEntities.HYAENODON_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.COELURUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.DIPLOCAULUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.CALYMENE_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.LIVYATAN_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.DREADNOUGHTUS.get(), LootTable.lootTable());
        add(ModEntities.EDMONTOSAURUS.get(), LootTable.lootTable());
        add(ModEntities.PATAGOTITAN.get(), LootTable.lootTable());
        add(ModEntities.MAIASAURA.get(), LootTable.lootTable());
        add(ModEntities.STEGOSAURUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.SPINORAPTOR_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.ACHILLOBATOR_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.CHILESAURUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.MEGATHERIUM_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.SEGISAURUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.ANKYLODOCUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.BRACHIOSAURUS.get(), LootTable.lootTable());
        add(ModEntities.SMILODON_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.MICROCERATUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.LEPTICTIDIUM_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.HYPSILOPHODON.get(), LootTable.lootTable());
        add(ModEntities.THERIZINOSAURUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.VELOCIRAPTOR_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.MUSSAURUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.TRICERATOPS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.GIGANOTOSAURUS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.TROPEOGNATHUS.get(), LootTable.lootTable());
        add(ModEntities.LAMBEOSAURUS.get(), LootTable.lootTable());
        add(ModEntities.ALLIGATOR_GAR.get(), LootTable.lootTable());
        add(ModEntities.ELASMOTHERIUM.get(), LootTable.lootTable());
        add(ModEntities.DIMETRODON_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.KAIRUKU_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.DEINOSUCHUS.get(), LootTable.lootTable());
        add(ModEntities.ASTEROCERAS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.MEGALODON_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.TITANITES_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.PARAPUZOSIA_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.VECTIPELTA.get(), LootTable.lootTable());
        add(ModEntities.PARACERATHERIUM.get(), LootTable.lootTable());
        add(ModEntities.ORTHOCERAS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.CAMEROCERAS_ENTITY_TYPE.get(), LootTable.lootTable());
        add(ModEntities.ENDOCERAS_ENTITY_TYPE.get(), LootTable.lootTable());
    }


    @Override
    protected Iterable<EntityType<?>> getKnownEntities() {
        return ModEntities.MOD_ENTITY_TYPES.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList());
    }
}
