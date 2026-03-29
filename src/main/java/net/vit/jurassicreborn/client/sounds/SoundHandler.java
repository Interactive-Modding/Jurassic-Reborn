package net.vit.jurassicreborn.client.sounds;

import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vit.jurassicreborn.JurassicReborn;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.ArrayList;
import java.util.List;

public class SoundHandler {
    private static final List<SoundEvent> ALL_SOUNDS = new ArrayList<>();
    public static void init(){};

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, JurassicReborn.MODID);


    public static final SoundEvent TROODONS_AND_RAPTORS = register("troodons_and_raptors");
    public static final SoundEvent JURASSICREBORN_THEME = register("jurassicreborn_theme");
    public static final SoundEvent DONT_MOVE_A_MUSCLE = register("dont_move_a_muscle");

    public static final SoundEvent STOMP = register("stomp");
    public static final SoundEvent FEEDER = register("feeder");
    public static final SoundEvent CAR_MOVE = register("car_move");

    public static final SoundEvent BRACHIOSAURUS_LIVING = register("brachiosaurus_living");
    public static final SoundEvent BRACHIOSAURUS_HURT = register("brachiosaurus_hurt");
    public static final SoundEvent BRACHIOSAURUS_DEATH = register("brachiosaurus_death");

    public static final SoundEvent MAMENCHISAURUS_LIVING = register("mamenchisaurus_living");
    public static final SoundEvent MAMENCHISAURUS_CALLING = register("mamenchisaurus_call");
    public static final SoundEvent MAMENCHISAURUS_DEATH = register("mamenchisaurus_death");
    public static final SoundEvent MAMENCHISAURUS_HURT = register("mamenchisaurus_hurt");
    public static final SoundEvent MAMENCHISAURUS_THREAT = register("mamenchisaurus_threat");
    public static final SoundEvent MAMENCHISAURUS_MATING = register("mamenchisaurus_mate_call");

    public static final SoundEvent DREADNOUGHTUS_LIVING = register("dreadnoughtus_living");
    public static final SoundEvent DREADNOUGHTUS_CALLING = register("dreadnoughtus_call");
    public static final SoundEvent DREADNOUGHTUS_DEATH = register("dreadnoughtus_death");
    public static final SoundEvent DREADNOUGHTUS_HURT = register("dreadnoughtus_hurt");
    public static final SoundEvent DREADNOUGHTUS_THREAT = register("dreadnoughtus_threat");
    public static final SoundEvent DREADNOUGHTUS_MATING = register("dreadnoughtus_mate_call");

    public static final SoundEvent PATAGOTITAN_LIVING = register("patagotitan_living");
    public static final SoundEvent PATAGOTITAN_CALLING = register("patagotitan_call");
    public static final SoundEvent PATAGOTITAN_DEATH = register("patagotitan_death");
    public static final SoundEvent PATAGOTITAN_HURT = register("patagotitan_hurt");
    public static final SoundEvent PATAGOTITAN_THREAT = register("patagotitan_threat");
    public static final SoundEvent PATAGOTITAN_MATING = register("patagotitan_mate_call");

    public static final SoundEvent PARACERATHERIUM_LIVING = register("paraceratherium_living");
    public static final SoundEvent PARACERATHERIUM_DEATH = register("paraceratherium_death");
    public static final SoundEvent PARACERATHERIUM_HURT = register("paraceratherium_hurt");
    public static final SoundEvent PARACERATHERIUM_THREAT = register("paraceratherium_threat");

    public static final SoundEvent DIMETRODON_LIVING = register("dimetrodon_living");
    public static final SoundEvent DIMETRODON_ROAR = register("dimetrodon_roar");


    public static final SoundEvent INDORAPTOR_LIVING = register("indoraptor_living");
    public static final SoundEvent INDORAPTOR_CALLING = register("indoraptor_call");
    public static final SoundEvent INDORAPTOR_DEATH = register("indoraptor_death");
    public static final SoundEvent INDORAPTOR_HURT = register("indoraptor_hurt");
    public static final SoundEvent INDORAPTOR_THREAT = register("indoraptor_threat");
    public static final SoundEvent INDORAPTOR_MATING = register("indoraptor_mate_call");
    public static final SoundEvent INDORAPTOR_ROAR = register("indoraptor_roar");
    public static final SoundEvent INDORAPTOR_BREATHING = register("indoraptor_breathing");


    public static final SoundEvent SINOCERATOPS_LIVING = register("sinoceratops_living");
    public static final SoundEvent SINOCERATOPS_CALLING = register("sinoceratops_call");
    public static final SoundEvent SINOCERATOPS_DEATH = register("sinoceratops_death");
    public static final SoundEvent SINOCERATOPS_HURT = register("sinoceratops_hurt");
    public static final SoundEvent SINOCERATOPS_THREAT = register("sinoceratops_threat");
    public static final SoundEvent SINOCERATOPS_MATING = register("sinoceratops_mate_call");

    public static final SoundEvent DODO_DEATH = register("dodo_death");
    public static final SoundEvent DODO_HURT = register("dodo_hurt");
    public static final SoundEvent DODO_LIVING = register("dodo_living");

    public static final SoundEvent RUGOPS_DEATH = register("rugops_death");
    public static final SoundEvent RUGOPS_HURT = register("rugops_hurt");
    public static final SoundEvent RUGOPS_LIVING = register("rugops_living");

    public static final SoundEvent DIPLODOCUS_DEATH = register("diplodocus_death");
    public static final SoundEvent DIPLODOCUS_HURT = register("diplodocus_hurt");
    public static final SoundEvent DIPLODOCUS_LIVING = register("diplodocus_living");

    public static final SoundEvent CAMARASAURUS_DEATH = register("camarasaurus_death");
    public static final SoundEvent CAMARASAURUS_HURT = register("camarasaurus_hurt");
    public static final SoundEvent CAMARASAURUS_LIVING = register("camarasaurus_living");

    public static final SoundEvent NIGERSAURUS_DEATH = register("nigersaurus_death");
    public static final SoundEvent NIGERSAURUS_HURT = register("nigersaurus_hurt");
    public static final SoundEvent NIGERSAURUS_LIVING = register("nigersaurus_living");
    public static final SoundEvent NIGERSAURUS_ATTACKING = register("nigersaurus_attack");

    public static final SoundEvent ANKYLODOCUS_DEATH = register("ankylodocus_death");
    public static final SoundEvent ANKYLODOCUS_HURT = register("ankylodocus_hurt");
    public static final SoundEvent ANKYLODOCUS_LIVING = register("ankylodocus_living");

    public static final SoundEvent HYPSILOPHODON_HURT = register("hypsilophodon_hurt");
    public static final SoundEvent HYPSILOPHODON_LIVING = register("hypsilophodon_living");

    public static final SoundEvent PARASAUROLOPHUS_LIVING = register("parasaurolophus_living");
    public static final SoundEvent PARASAUROLOPHUS_CALL = register("parasaurolophus_call");
    public static final SoundEvent PARASAUROLOPHUS_DEATH = register("parasaurolophus_death");
    public static final SoundEvent PARASAUROLOPHUS_HURT = register("parasaurolophus_hurt");

    public static final SoundEvent TRICERATOPS_LIVING = register("triceratops_living");
    public static final SoundEvent TRICERATOPS_DEATH = register("triceratops_death");
    public static final SoundEvent TRICERATOPS_HURT = register("triceratops_hurt");

    public static final SoundEvent STEGOSAURUS_LIVING = register("stegosaurus_living");
    public static final SoundEvent STEGOSAURUS_HURT = register("stegosaurus_hurt");
    public static final SoundEvent STEGOSAURUS_DEATH = register("stegosaurus_death");

    public static final SoundEvent DILOPHOSAURUS_LIVING = register("dilophosaurus_living");
    public static final SoundEvent DILOPHOSAURUS_SPIT = register("dilophosaurus_spit");
    public static final SoundEvent DILOPHOSAURUS_HURT = register("dilophosaurus_hurt");
    public static final SoundEvent DILOPHOSAURUS_DEATH = register("dilophosaurus_death");

    public static final SoundEvent CARNOTAURUS_LIVING = register("carnotaurus_living");
    public static final SoundEvent CARNOTAURUS_HURT = register("carnotaurus_hurt");
    public static final SoundEvent CARNOTAURUS_DEATH = register("carnotaurus_death");

    public static final SoundEvent GALLIMIMUS_LIVING = register("gallimimus_living");
    public static final SoundEvent GALLIMIMUS_DEATH = register("gallimimus_death");
    public static final SoundEvent GALLIMIMUS_HURT = register("gallimimus_hurt");

    public static final SoundEvent ORNITHOMIMUS_LIVING = register("ornithomimus_living");
    public static final SoundEvent ORNITHOMIMUS_DEATH = register("ornithomimus_death");
    public static final SoundEvent ORNITHOMIMUS_HURT = register("ornithomimus_hurt");

    public static final SoundEvent SPINOSAURUS_LIVING = register("spinosaurus_living");
    public static final SoundEvent SPINOSAURUS_HURT = register("spinosaurus_hurt");
    public static final SoundEvent SPINOSAURUS_DEATH = register("spinosaurus_death");
    public static final SoundEvent SPINOSAURUS_CALL = register("spinosaurus_call");
    public static final SoundEvent SPINOSAURUS_ROAR = register("spinosaurus_roar");
    public static final SoundEvent SPINOSAURUS_BREATHING = register("spinosaurus_breathing");
    public static final SoundEvent SPINOSAURUS_THREAT = register("spinosaurus_threat");

    public static final SoundEvent SMILODON_LIVING = register("smilodon_living");
    public static final SoundEvent SMILODON_HURT = register("smilodon_hurt");
    public static final SoundEvent SMILODON_DEATH = register("smilodon_death");
    public static final SoundEvent SMILODON_BREATHING = register("smilodon_breathing");


    public static final SoundEvent STYRACOSAURUS_LIVING = register("styracosaurus_living");
    public static final SoundEvent STYRACOSAURUS_HURT = register("styracosaurus_hurt");
    public static final SoundEvent STYRACOSAURUS_DEATH = register("styracosaurus_death");
    public static final SoundEvent STYRACOSAURUS_CALL = register("styracosaurus_call");
    public static final SoundEvent STYRACOSAURUS_ROAR = register("styracosaurus_roar");
    public static final SoundEvent STYRACOSAURUS_THREAT = register("styracosaurus_threat");

    public static final SoundEvent TROPEOGNATHUS_LIVING = register("tropeognathus_living");
    public static final SoundEvent TROPEOGNATHUS_HURT = register("tropeognathus_hurt");
    public static final SoundEvent TROPEOGNATHUS_DEATH = register("tropeognathus_death");
    public static final SoundEvent TROPEOGNATHUS_ROAR = register("tropeognathus_roar");
    public static final SoundEvent TROPEOGNATHUS_THREAT = register("tropeognathus_threat");

    public static final SoundEvent ZHENYUANOPTERUS_LIVING = register("zhenyuanopterus_living");
    public static final SoundEvent ZHENYUANOPTERUS_HURT = register("zhenyuanopterus_hurt");
    public static final SoundEvent ZHENYUANOPTERUS_DEATH = register("zhenyuanopterus_death");
    public static final SoundEvent ZHENYUANOPTERUS_ROAR = register("zhenyuanopterus_roar");
    public static final SoundEvent ZHENYUANOPTERUS_THREAT = register("zhenyuanopterus_threat");

    public static final SoundEvent MICROCERATUS_LIVING = register("microceratus_living");
    public static final SoundEvent MICROCERATUS_HURT = register("microceratus_hurt");
    public static final SoundEvent MICROCERATUS_DEATH = register("microceratus_death");
    public static final SoundEvent MICROCERATUS_THREAT = register("microceratus_threat");

    public static final SoundEvent METRIACANTHOSAURUS_LIVING = register("metriacanthosaurus_living");
    public static final SoundEvent METRIACANTHOSAURUS_HURT = register("metriacanthosaurus_hurt");
    public static final SoundEvent METRIACANTHOSAURUS_DEATH = register("metriacanthosaurus_death");
    public static final SoundEvent METRIACANTHOSAURUS_CALL = register("metriacanthosaurus_call");
    public static final SoundEvent METRIACANTHOSAURUS_ROAR = register("metriacanthosaurus_roar");
    public static final SoundEvent METRIACANTHOSAURUS_THREAT = register("metriacanthosaurus_threat");

    public static final SoundEvent MAJUNGASAURUS_LIVING = register("majungasaurus_living");
    public static final SoundEvent MAJUNGASAURUS_HURT = register("majungasaurus_hurt");
    public static final SoundEvent MAJUNGASAURUS_DEATH = register("majungasaurus_death");
    public static final SoundEvent MAJUNGASAURUS_CALL = register("majungasaurus_call");
    public static final SoundEvent MAJUNGASAURUS_ROAR = register("majungasaurus_roar");
    public static final SoundEvent MAJUNGASAURUS_BREATHING = register("majungasaurus_breathing");
    public static final SoundEvent MAJUNGASAURUS_THREAT = register("majungasaurus_threat");

    public static final SoundEvent LAMBEOSAURUS_LIVING = register("lambeosaurus_living");
    public static final SoundEvent LAMBEOSAURUS_HURT = register("lambeosaurus_hurt");
    public static final SoundEvent LAMBEOSAURUS_DEATH = register("lambeosaurus_death");
    public static final SoundEvent LAMBEOSAURUS_CALL = register("lambeosaurus_call");
    public static final SoundEvent LAMBEOSAURUS_THREAT = register("lambeosaurus_threat");

    public static final SoundEvent LUDODACTYLUS_LIVING = register("ludodactylus_living");
    public static final SoundEvent LUDODACTYLUS_HURT = register("ludodactylus_hurt");
    public static final SoundEvent LUDODACTYLUS_DEATH = register("ludodactylus_death");
    public static final SoundEvent LUDODACTYLUS_CALL = register("ludodactylus_call");
    public static final SoundEvent LUDODACTYLUS_THREAT = register("ludodactylus_threat");

    public static final SoundEvent LEAELLYNASAURA_LIVING = register("leaellynasaura_living");
    public static final SoundEvent LEAELLYNASAURA_HURT = register("leaellynasaura_hurt");
    public static final SoundEvent LEAELLYNASAURA_DEATH = register("leaellynasaura_death");

    public static final SoundEvent LEPTICTIDIUM_LIVING = register("leptictidium_living");
    public static final SoundEvent LEPTICTIDIUM_HURT = register("leptictidium_hurt");
    public static final SoundEvent LEPTICTIDIUM_DEATH = register("leptictidium_death");

    public static final SoundEvent GIGANOTOSAURUS_LIVING = register("giganotosaurus_living");
    public static final SoundEvent GIGANOTOSAURUS_HURT = register("giganotosaurus_hurt");
    public static final SoundEvent GIGANOTOSAURUS_DEATH = register("giganotosaurus_death");
    public static final SoundEvent GIGANOTOSAURUS_CALL = register("giganotosaurus_call");
    public static final SoundEvent GIGANOTOSAURUS_ROAR = register("giganotosaurus_roar");
    public static final SoundEvent GIGANOTOSAURUS_THREAT = register("giganotosaurus_threat");

    public static final SoundEvent HERRERASAURUS_LIVING = register("herrerasaurus_living");
    public static final SoundEvent HERRERASAURUS_HURT = register("herrerasaurus_hurt");
    public static final SoundEvent HERRERASAURUS_DEATH = register("herrerasaurus_death");
    public static final SoundEvent HERRERASAURUS_CALL = register("herrerasaurus_call");
    public static final SoundEvent HERRERASAURUS_ROAR = register("herrerasaurus_roar");
    public static final SoundEvent HERRERASAURUS_THREAT = register("herrerasaurus_threat");

    public static final SoundEvent EDMONTOSAURUS_LIVING = register("edmontosaurus_living");
    public static final SoundEvent EDMONTOSAURUS_HURT = register("edmontosaurus_hurt");
    public static final SoundEvent EDMONTOSAURUS_DEATH = register("edmontosaurus_death");
    public static final SoundEvent EDMONTOSAURUS_CALL = register("edmontosaurus_call");
    public static final SoundEvent EDMONTOSAURUS_THREAT = register("edmontosaurus_threat");
    public static final SoundEvent MAIASAURA_LIVING = register("maiasaura_living");
    public static final SoundEvent MAIASAURA_HURT = register("maiasaura_hurt");
    public static final SoundEvent MAIASAURA_DEATH = register("maiasaura_death");
    public static final SoundEvent MAIASAURA_CALL = register("maiasaura_call");
    public static final SoundEvent MAIASAURA_THREAT = register("maiasaura_threat");

    public static final SoundEvent DUNKLEOSTEUS_LIVING = register("dunkleosteus_living");
    public static final SoundEvent DUNKLEOSTEUS_HURT = register("dunkleosteus_hurt");
    public static final SoundEvent DUNKLEOSTEUS_DEATH = register("dunkleosteus_death");
    public static final SoundEvent DUNKLEOSTEUS_THREAT = register("dunkleosteus_threat");

    public static final SoundEvent THERIZINOSAURUS_LIVING = register("therizinosaurus_living");
    public static final SoundEvent THERIZINOSAURUS_HURT = register("therizinosaurus_hurt");
    public static final SoundEvent THERIZINOSAURUS_DEATH = register("therizinosaurus_death");
    public static final SoundEvent THERIZINOSAURUS_ROAR = register("therizinosaurus_roar");
    public static final SoundEvent THERIZINOSAURUS_THREAT = register("therizinosaurus_threat");

    public static final SoundEvent BARYONYX_LIVING = register("baryonyx_living");
    public static final SoundEvent BARYONYX_HURT = register("baryonyx_hurt");
    public static final SoundEvent BARYONYX_DEATH = register("baryonyx_death");
    public static final SoundEvent BARYONYX_CALL = register("baryonyx_call");
    public static final SoundEvent BARYONYX_ROAR = register("baryonyx_roar");
    public static final SoundEvent BARYONYX_THREAT = register("baryonyx_threat");

    public static final SoundEvent DIMORPHODON_LIVING = register("dimorphodon_living");
    public static final SoundEvent DIMORPHODON_HURT = register("dimorphodon_hurt");
    public static final SoundEvent DIMORPHODON_DEATH = register("dimorphodon_death");
    public static final SoundEvent DIMORPHODON_CALL = register("dimorphodon_call");
    public static final SoundEvent DIMORPHODON_THREAT = register("dimorphodon_threat");

    public static final SoundEvent COMPSOGNATHUS_LIVING = register("compsognathus_living");
    public static final SoundEvent COMPSOGNATHUS_HURT = register("compsognathus_hurt");
    public static final SoundEvent COMPSOGNATHUS_DEATH = register("compsognathus_death");
    public static final SoundEvent COMPSOGNATHUS_CALL = register("compsognathus_call");
    public static final SoundEvent COMPSOGNATHUS_THREAT = register("compsognathus_threat");

    public static final SoundEvent CORYTHOSAURUS_LIVING = register("corythosaurus_living");
    public static final SoundEvent CORYTHOSAURUS_HURT = register("corythosaurus_hurt");
    public static final SoundEvent CORYTHOSAURUS_DEATH = register("corythosaurus_death");
    public static final SoundEvent CORYTHOSAURUS_THREAT = register("corythosaurus_threat");
    public static final SoundEvent CORYTHOSAURUS_CALL = register("corythosaurus_call");

    public static final SoundEvent CHASMOSAURUS_LIVING = register("chasmosaurus_living");
    public static final SoundEvent CHASMOSAURUS_HURT = register("chasmosaurus_hurt");
    public static final SoundEvent CHASMOSAURUS_DEATH = register("chasmosaurus_death");
    public static final SoundEvent CHASMOSAURUS_THREAT = register("chasmosaurus_threat");

    public static final SoundEvent APATOSAURUS_LIVING = register("apatosaurus_living");
    public static final SoundEvent APATOSAURUS_HURT = register("apatosaurus_hurt");
    public static final SoundEvent APATOSAURUS_DEATH = register("apatosaurus_death");
    public static final SoundEvent APATOSAURUS_CALL = register("apatosaurus_call");
    public static final SoundEvent APATOSAURUS_THREAT = register("apatosaurus_threat");

    public static final SoundEvent CHILESAURUS_LIVING = register("chilesaurus_living");
    public static final SoundEvent CHILESAURUS_HURT = register("chilesaurus_hurt");
    public static final SoundEvent CHILESAURUS_DEATH = register("chilesaurus_death");

    public static final SoundEvent CEARADACTYLUS_LIVING = register("cearadactylus_living");
    public static final SoundEvent CEARADACTYLUS_HURT = register("cearadactylus_hurt");
    public static final SoundEvent CEARADACTYLUS_DEATH = register("cearadactylus_death");
    public static final SoundEvent CEARADACTYLUS_CALL = register("cearadactylus_call");
    public static final SoundEvent CEARADACTYLUS_THREAT = register("cearadactylus_threat");

    public static final SoundEvent PACHYCEPHALOSAURUS_LIVING = register("pachycephalosaurus_living");
    public static final SoundEvent PACHYCEPHALOSAURUS_HURT = register("pachycephalosaurus_hurt");
    public static final SoundEvent PACHYCEPHALOSAURUS_DEATH = register("pachycephalosaurus_death");
    public static final SoundEvent PACHYCEPHALOSAURUS_CALL = register("pachycephalosaurus_call");
    public static final SoundEvent PACHYCEPHALOSAURUS_THREAT = register("pachycephalosaurus_threat");

    public static final SoundEvent PROTOCERATOPS_LIVING = register("protoceratops_living");
    public static final SoundEvent PROTOCERATOPS_HURT = register("protoceratops_hurt");
    public static final SoundEvent PROTOCERATOPS_DEATH = register("protoceratops_death");
    public static final SoundEvent PROTOCERATOPS_THREAT = register("protoceratops_threat");

    public static final SoundEvent MOGANOPTERUS_LIVING = register("moganopterus_living");
    public static final SoundEvent MOGANOPTERUS_HURT = register("moganopterus_hurt");
    public static final SoundEvent MOGANOPTERUS_DEATH = register("moganopterus_death");
    public static final SoundEvent MOGANOPTERUS_THREAT = register("moganopterus_threat");

    public static final SoundEvent COELURUS_LIVING = register("coelurus_living");
    public static final SoundEvent COELURUS_HURT = register("coelurus_hurt");
    public static final SoundEvent COELURUS_DEATH = register("coelurus_death");
    public static final SoundEvent COELURUS_THREAT = register("coelurus_threat");

    public static final SoundEvent SEGISAURUS_LIVING = register("segisaurus_living");
    public static final SoundEvent SEGISAURUS_HURT = register("segisaurus_hurt");
    public static final SoundEvent SEGISAURUS_DEATH = register("segisaurus_death");
    public static final SoundEvent SEGISAURUS_THREAT = register("segisaurus_threat");

    public static final SoundEvent QUETZALCOATLUS_LIVING = register("quetzalcoatlus_living");
    public static final SoundEvent QUETZALCOATLUS_HURT = register("quetzalcoatlus_hurt");
    public static final SoundEvent QUETZALCOATLUS_DEATH = register("quetzalcoatlus_death");
    public static final SoundEvent QUETZALCOATLUS_THREAT = register("quetzalcoatlus_threat");

    public static final SoundEvent OVIRAPTOR_LIVING = register("oviraptor_living");
    public static final SoundEvent OVIRAPTOR_HURT = register("oviraptor_hurt");
    public static final SoundEvent OVIRAPTOR_DEATH = register("oviraptor_death");
    public static final SoundEvent OVIRAPTOR_THREAT = register("oviraptor_threat");

    public static final SoundEvent OTHNIELIA_LIVING = register("othnielia_living");
    public static final SoundEvent OTHNIELIA_HURT = register("othnielia_hurt");
    public static final SoundEvent OTHNIELIA_DEATH = register("othnielia_death");

    public static final SoundEvent CERATOSAURUS_LIVING = register("ceratosaurus_living");
    public static final SoundEvent CERATOSAURUS_HURT = register("ceratosaurus_hurt");
    public static final SoundEvent CERATOSAURUS_DEATH = register("ceratosaurus_death");
    public static final SoundEvent CERATOSAURUS_CALL = register("ceratosaurus_call");
    public static final SoundEvent CERATOSAURUS_ROAR = register("ceratosaurus_roar");
    public static final SoundEvent CERATOSAURUS_BREATHING = register("ceratosaurus_breathing");
    public static final SoundEvent CERATOSAURUS_THREAT = register("ceratosaurus_threat");

    public static final SoundEvent ALVAREZSAURUS_LIVING = register("alvarezsaurus_living");
    public static final SoundEvent ALVAREZSAURUS_HURT = register("alvarezsaurus_hurt");
    public static final SoundEvent ALVAREZSAURUS_DEATH = register("alvarezsaurus_death");
    public static final SoundEvent ALVAREZSAURUS_CALL = register("alvarezsaurus_call");
    public static final SoundEvent ALVAREZSAURUS_THREAT = register("alvarezsaurus_threat");

    public static final SoundEvent TROODON_LIVING = register("troodon_living");
    public static final SoundEvent TROODON_HURT = register("troodon_hurt");
    public static final SoundEvent TROODON_DEATH = register("troodon_death");
    public static final SoundEvent TROODON_CALL = register("troodon_call");
    public static final SoundEvent TROODON_THREAT = register("troodon_threat");

    public static final SoundEvent TITANIS_LIVING = register("titanis_living");
    public static final SoundEvent TITANIS_HURT = register("titanis_hurt");
    public static final SoundEvent TITANIS_DEATH = register("titanis_death");
    public static final SoundEvent TITANIS_THREAT = register("titanis_threat");

    public static final SoundEvent PTERANODON_LIVING = register("pteranodon_living");
    public static final SoundEvent PTERANODON_HURT = register("pteranodon_hurt");
    public static final SoundEvent PTERANODON_DEATH = register("pteranodon_death");
    public static final SoundEvent PTERANODON_CALL = register("pteranodon_call");

    public static final SoundEvent HYAENODON_LIVING = register("hyaenodon_living");
    public static final SoundEvent HYAENODON_HURT = register("hyaenodon_hurt");
    public static final SoundEvent HYAENODON_DEATH = register("hyaenodon_death");
    public static final SoundEvent HYAENODON_CALL = register("hyaenodon_call");

    public static final SoundEvent INDOMINUS_LIVING = register("indominus_living");
    public static final SoundEvent INDOMINUS_DEATH = register("indominus_death");
    public static final SoundEvent INDOMINUS_HURT = register("indominus_hurt");
    public static final SoundEvent INDOMINUS_ROAR = register("indominus_roar");
    public static final SoundEvent INDOMINUS_BREATHING = register("indominus_breathing");

    public static final SoundEvent RAPHUSREX_LIVING = register("raphusrex_living");
    public static final SoundEvent RAPHUSREX_DEATH = register("raphusrex_death");
    public static final SoundEvent RAPHUSREX_HURT = register("raphusrex_hurt");
    public static final SoundEvent RAPHUSREX_ROAR = register("raphusrex_roar");
    public static final SoundEvent RAPHUSREX_BREATHING = register("raphusrex_breathing");

    public static final SoundEvent TYRANNOSAURUS_BREATHING = register("tyrannosaurus_breathing");
    public static final SoundEvent TYRANNOSAURUS_DEATH = register("tyrannosaurus_death");
    public static final SoundEvent TYRANNOSAURUS_HURT = register("tyrannosaurus_hurt");
    public static final SoundEvent TYRANNOSAURUS_ROAR = register("tyrannosaurus_roar");
    public static final SoundEvent TYRANNOSAURUS_LIVING = register("tyrannosaurus_living");

    public static final SoundEvent VELOCIRAPTOR_LIVING = register("velociraptor_living");
    public static final SoundEvent VELOCIRAPTOR_HURT = register("velociraptor_hurt");
    public static final SoundEvent VELOCIRAPTOR_BREATHING = register("velociraptor_breathing");
    public static final SoundEvent VELOCIRAPTOR_CALL = register("velociraptor_call");
    public static final SoundEvent VELOCIRAPTOR_DEATH = register("velociraptor_death");
    public static final SoundEvent VELOCIRAPTOR_ATTACK = register("velociraptor_attack");

    public static final SoundEvent ACHILLOBATOR_LIVING = register("achillobator_living");
    public static final SoundEvent ACHILLOBATOR_HURT = register("achillobator_hurt");
    public static final SoundEvent ACHILLOBATOR_MATE_CALL = register("achillobator_mate_call");
    public static final SoundEvent ACHILLOBATOR_CALL = register("achillobator_call");
    public static final SoundEvent ACHILLOBATOR_DEATH = register("achillobator_death");
    public static final SoundEvent ACHILLOBATOR_ATTACK = register("achillobator_attack");

    public static final SoundEvent SPINORAPTOR_LIVING = register("spinoraptor_living");
    public static final SoundEvent SPINORAPTOR_HURT = register("spinoraptor_hurt");
    public static final SoundEvent SPINORAPTOR_BREATHING = register("spinoraptor_breathing");
    public static final SoundEvent SPINORAPTOR_CALL = register("spinoraptor_call");
    public static final SoundEvent SPINORAPTOR_DEATH = register("spinoraptor_death");


    public static final SoundEvent ANKYLOSAURUS_LIVING = register("ankylosaurus_living");
    public static final SoundEvent ANKYLOSAURUS_HURT = register("ankylosaurus_hurt");
    public static final SoundEvent ANKYLOSAURUS_MATE_CALL = register("ankylosaurus_mate_call");
    public static final SoundEvent ANKYLOSAURUS_CALL = register("ankylosaurus_call");
    public static final SoundEvent ANKYLOSAURUS_DEATH = register("ankylosaurus_death");
    public static final SoundEvent ANKYLOSAURUS_ATTACK = register("ankylosaurus_attack");

    public static final SoundEvent VECTIPELTA_LIVING = register("vectipelta_living");
    public static final SoundEvent VECTIPELTA_HURT = register("vectipelta_hurt");
    public static final SoundEvent VECTIPELTA_MATE_CALL = register("vectipelta_mate_call");
    public static final SoundEvent VECTIPELTA_CALL = register("vectipelta_call");
    public static final SoundEvent VECTIPELTA_DEATH = register("vectipelta_death");
    public static final SoundEvent VECTIPELTA_ATTACK = register("vectipelta_attack");

    public static final SoundEvent MICRORAPTOR_LIVING = register("microraptor_living");
    public static final SoundEvent MICRORAPTOR_HURT = register("microraptor_hurt");
    public static final SoundEvent MICRORAPTOR_DEATH = register("microraptor_death");
    public static final SoundEvent MICRORAPTOR_ATTACK = register("microraptor_attack");

    public static final SoundEvent MUSSAURUS_LIVING = register("mussaurus_living");
    public static final SoundEvent MUSSAURUS_HURT = register("mussaurus_hurt");
    public static final SoundEvent MUSSAURUS_DEATH = register("mussaurus_death");
    public static final SoundEvent MUSSAURUS_ATTACK = register("mussaurus_attack");
    public static final SoundEvent MUSSAURUS_MATE_CALL = register("mussaurus_mate_call");

    public static final SoundEvent MOSASAURUS_LIVING = register("mosasaurus_living");
    public static final SoundEvent MOSASAURUS_HURT = register("mosasaurus_hurt");
    public static final SoundEvent MOSASAURUS_DEATH = register("mosasaurus_death");
    public static final SoundEvent MOSASAURUS_MATE_CALL = register("mosasaurus_mate_call");
    public static final SoundEvent MOSASAURUS_ATTACK = register("mosasaurus_attack");
    public static final SoundEvent MOSASAURUS_ROAR = register("mosasaurus_roar");

    public static final SoundEvent ALLOSAURUS_DEATH = register("allosaurus_death");
    public static final SoundEvent ALLOSAURUS_FIGHT_FEMALE = register("allosaurus_fight_female");
    public static final SoundEvent ALLOSAURUS_HURT = register("allosaurus_hurt");
    public static final SoundEvent ALLOSAURUS_LIVING = register("allosaurus_living");
    public static final SoundEvent ALLOSAURUS_MATE_CALL = register("allosaurus_mate_call");
    public static final SoundEvent ALLOSAURUS_ROAR = register("allosaurus_roar");
    public static final SoundEvent ALLOSAURUS_THREAT = register("allosaurus_threat");

    public static final SoundEvent BEELZEBUFO_CROAK = register("beelzebufo_croak");
    public static final SoundEvent BEELZEBUFO_HURT = register("beelzebufo_hurt");
    public static final SoundEvent BEELZEBUFO_HURT_THREAT = register("beelzebufo_hurt_threat");
    public static final SoundEvent BEELZEBUFO_LONG_DISTANCE_CROAK = register("beelzebufo_long_distance_croak");
    public static final SoundEvent BEELZEBUFO_THREAT = register("beelzebufo_threat");

    public static final SoundEvent CARCHARODONTOSAURUS_GROWL = register("carcharodontosaurus_growl");
    public static final SoundEvent CARCHARODONTOSAURUS_HISS = register("carcharodontosaurus_hiss");
    public static final SoundEvent CARCHARODONTOSAURUS_HURT = register("carcharodontosaurus_hurt");
    public static final SoundEvent CARCHARODONTOSAURUS_LIVING = register("carcharodontosaurus_living");
    public static final SoundEvent CARCHARODONTOSAURUS_ROAR = register("carcharodontosaurus_roar");

    public static final SoundEvent GUANLONG_DEATH = register("guanlong_death");
    public static final SoundEvent GUANLONG_HURT = register("guanlong_hurt");
    public static final SoundEvent GUANLONG_LIVING = register("guanlong_living");

    public static final SoundEvent PROCERATOSAURUS_ATTACK = register("proceratosaurus_attack");
    public static final SoundEvent PROCERATOSAURUS_DEATH = register("proceratosaurus_death");
    public static final SoundEvent PROCERATOSAURUS_HURT = register("proceratosaurus_hurt");
    public static final SoundEvent PROCERATOSAURUS_LIVING = register("proceratosaurus_living");
    public static final SoundEvent PROCERATOSAURUS_MATE_CALL = register("proceratosaurus_mate_call");
    public static final SoundEvent PROCERATOSAURUS_THREAT = register("proceratosaurus_threat");

    public static final SoundEvent SUCHOMIMUS_DEATH = register("suchomimus_death");
    public static final SoundEvent SUCHOMIMUS_HURT = register("suchomimus_hurt");
    public static final SoundEvent SUCHOMIMUS_LIVING = register("suchomimus_living");
    public static final SoundEvent SUCHOMIMUS_MATE_CALL = register("suchomimus_mate_call");
    public static final SoundEvent SUCHOMIMUS_ROAR = register("suchomimus_roar");

    public static final SoundEvent MAMMOTH_ALARM_CALL = register("mammoth_alarm_call");
    public static final SoundEvent MAMMOTH_DEATH = register("mammoth_death");
    public static final SoundEvent MAMMOTH_HURT = register("mammoth_hurt");
    public static final SoundEvent MAMMOTH_LIVING = register("mammoth_living");
    public static final SoundEvent MAMMOTH_MATE_CALL = register("mammoth_mate_call");
    public static final SoundEvent MAMMOTH_THREAT = register("mammoth_threat");

    public static final SoundEvent ELASMOTHERIUM_DEATH = register("elasmotherium_death");
    public static final SoundEvent ELASMOTHERIUM_HURT = register("elasmotherium_hurt");
    public static final SoundEvent ELASMOTHERIUM_LIVING = register("elasmotherium_living");
    public static final SoundEvent ELASMOTHERIUM_THREAT = register("elasmotherium_threat");

    public static final SoundEvent DEINOTHERIUM_ALARM_CALL = register("deinotherium_alarm_call");
    public static final SoundEvent DEINOTHERIUM_DEATH = register("deinotherium_death");
    public static final SoundEvent DEINOTHERIUM_HURT = register("deinotherium_hurt");
    public static final SoundEvent DEINOTHERIUM_LIVING = register("deinotherium_living");
    public static final SoundEvent DEINOTHERIUM_MATE_CALL = register("deinotherium_mate_call");
    public static final SoundEvent DEINOTHERIUM_THREAT = register("deinotherium_threat");

    public static final SoundEvent ARSINOITHERIUM_DEATH = register("arsinoitherium_death");
    public static final SoundEvent ARSINOITHERIUM_HURT = register("arsinoitherium_hurt");
    public static final SoundEvent ARSINOITHERIUM_LIVING = register("arsinoitherium_living");
    public static final SoundEvent ARSINOITHERIUM_THREAT = register("arsinoitherium_threat");

    public static final SoundEvent MEGATHERIUM_DEATH = register("megatherium_death");
    public static final SoundEvent MEGATHERIUM_HURT = register("megatherium_hurt");
    public static final SoundEvent MEGATHERIUM_LIVING = register("megatherium_living");
    public static final SoundEvent MEGATHERIUM_THREAT = register("megatherium_threat");

    public static final SoundEvent POSTOSUCHUS_ATTACK = register("postosuchus_attack");
    public static final SoundEvent POSTOSUCHUS_CALL = register("postosuchus_call");
    public static final SoundEvent POSTOSUCHUS_DEATH = register("postosuchus_death");
    public static final SoundEvent DEINOSUCHUS_ATTACK = register("deinosuchus_attack");
    public static final SoundEvent DEINOSUCHUS_CALL = register("deinosuchus_call");
    public static final SoundEvent DEINOSUCHUS_LIVING = register("deinosuchus_living");
    public static final SoundEvent DEINOSUCHUS_DEATH = register("deinosuchus_death");
    public static final SoundEvent DEINOSUCHUS_INJURED = register("deinosuchus_injured");
    public static final SoundEvent DEINOSUCHUS_DEATH_ROLL = register("deinosuchus_death_roll");
    public static final SoundEvent LIVYATAN_CALL = register("livyatan_call");
    public static final SoundEvent LIVYATAN_DEATH = register("livyatan_death");
    public static final SoundEvent LIVYATAN_HURT = register("livyatan_hurt");
    public static final SoundEvent LIVYATAN_LIVING = register("livyatan_living");
    public static final SoundEvent KAIRUKU_CALL = register("kairuku_call");
    public static final SoundEvent KAIRUKU_DEATH = register("kairuku_death");
    public static final SoundEvent KAIRUKU_HURT = register("kairuku_hurt");
    public static final SoundEvent KAIRUKU_LIVING = register("kairuku_living");
    public static final SoundEvent GOAT_LIVING = register("goat_living");
    public static final SoundEvent GOAT_HURT = register("goat_hurt");
    public static final SoundEvent GOAT_DEATH = register("goat_death");

    public static final SoundEvent FENCE_SHOCK = register("fence_shock");

    public static final SoundEvent FIRE = register("fire");
    public static final SoundEvent EMPTY = register("empty");
    public static final SoundEvent RELOAD = register("reload");





    private static SoundEvent register(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, name);
        SoundEvent event = SoundEvent.createVariableRangeEvent(id);
        SOUNDS.register(name, () -> event);
        ALL_SOUNDS.add(event);
        return event;
    }

    public static List<SoundEvent> getSounds() {
        return ALL_SOUNDS;
    }
}
