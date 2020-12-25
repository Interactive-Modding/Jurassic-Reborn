package mod.reborn.client.sound;

import com.google.common.collect.Lists;
import net.minecraft.client.audio.Sound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import mod.reborn.RebornMod;
import mod.reborn.server.util.RegistryHandler;

import java.util.List;

public class SoundHandler {
    public static final SoundEvent TROODONS_AND_RAPTORS = create("troodons_and_raptors");
    public static final SoundEvent JURASSICRAFT_THEME = create("jurassicraft_theme");
    public static final SoundEvent DONT_MOVE_A_MUSCLE = create("dont_move_a_muscle");

    public static final SoundEvent STOMP = create("stomp");
    public static final SoundEvent FEEDER = create("feeder");
    public static final SoundEvent CAR_MOVE = create("car_move");

    public static final SoundEvent BRACHIOSAURUS_LIVING = create("brachiosaurus_living");
    public static final SoundEvent BRACHIOSAURUS_HURT = create("brachiosaurus_hurt");
    public static final SoundEvent BRACHIOSAURUS_DEATH = create("brachiosaurus_death");

    public static final SoundEvent MAMENCHISAURUS_LIVING = create("mamenchisaurus_living");
    public static final SoundEvent MAMENCHISAURUS_CALLING = create("mamenchisaurus_call");
    public static final SoundEvent MAMENCHISAURUS_DEATH = create("mamenchisaurus_death");
    public static final SoundEvent MAMENCHISAURUS_HURT = create("mamenchisaurus_hurt");
    public static final SoundEvent MAMENCHISAURUS_THREAT = create("mamenchisaurus_threat");
    public static final SoundEvent MAMENCHISAURUS_MATING = create("mamenchisaurus_mate_call");

    public static final SoundEvent DREADNOUGHTUS_LIVING = create("dreadnoughtus_living");
    public static final SoundEvent DREADNOUGHTUS_CALLING = create("dreadnoughtus_call");
    public static final SoundEvent DREADNOUGHTUS_DEATH = create("dreadnoughtus_death");
    public static final SoundEvent DREADNOUGHTUS_HURT = create("dreadnoughtus_hurt");
    public static final SoundEvent DREADNOUGHTUS_THREAT = create("dreadnoughtus_threat");
    public static final SoundEvent DREADNOUGHTUS_MATING = create("dreadnoughtus_mate_call");

    public static final SoundEvent INDORAPTOR_LIVING = create("indoraptor_living");
    public static final SoundEvent INDORAPTOR_CALLING = create("indoraptor_call");
    public static final SoundEvent INDORAPTOR_DEATH = create("indoraptor_death");
    public static final SoundEvent INDORAPTOR_HURT = create("indoraptor_hurt");
    public static final SoundEvent INDORAPTOR_THREAT = create("indoraptor_threat");
    public static final SoundEvent INDORAPTOR_MATING = create("indoraptor_mate_call");
    public static final SoundEvent INDORAPTOR_ROAR = create("indoraptor_roar");
    public static final SoundEvent INDORAPTOR_BREATHING = create("indoraptor_breathing");


    public static final SoundEvent SINOCERATOPS_LIVING = create("sinoceratops_living");
    public static final SoundEvent SINOCERATOPS_CALLING = create("sinoceratops_call");
    public static final SoundEvent SINOCERATOPS_DEATH = create("sinoceratops_death");
    public static final SoundEvent SINOCERATOPS_HURT = create("sinoceratops_hurt");
    public static final SoundEvent SINOCERATOPS_THREAT = create("sinoceratops_threat");
    public static final SoundEvent SINOCERATOPS_MATING = create("sinoceratops_mate_call");

    public static final SoundEvent DODO_DEATH = create("dodo_death");
    public static final SoundEvent DODO_HURT = create("dodo_hurt");
    public static final SoundEvent DODO_LIVING = create("dodo_living");

    public static final SoundEvent RUGOPS_DEATH = create("rugops_death");
    public static final SoundEvent RUGOPS_HURT = create("rugops_hurt");
    public static final SoundEvent RUGOPS_LIVING = create("rugops_living");

    public static final SoundEvent HYPSILOPHODON_HURT = create("hypsilophodon_hurt");
    public static final SoundEvent HYPSILOPHODON_LIVING = create("hypsilophodon_living");

    public static final SoundEvent PARASAUROLOPHUS_LIVING = create("parasaurolophus_living");
    public static final SoundEvent PARASAUROLOPHUS_CALL = create("parasaurolophus_call");
    public static final SoundEvent PARASAUROLOPHUS_DEATH = create("parasaurolophus_death");
    public static final SoundEvent PARASAUROLOPHUS_HURT = create("parasaurolophus_hurt");

    public static final SoundEvent TRICERATOPS_LIVING = create("triceratops_living");
    public static final SoundEvent TRICERATOPS_DEATH = create("triceratops_death");
    public static final SoundEvent TRICERATOPS_HURT = create("triceratops_hurt");

    public static final SoundEvent STEGOSAURUS_LIVING = create("stegosaurus_living");
    public static final SoundEvent STEGOSAURUS_HURT = create("stegosaurus_hurt");
    public static final SoundEvent STEGOSAURUS_DEATH = create("stegosaurus_death");

    public static final SoundEvent DILOPHOSAURUS_LIVING = create("dilophosaurus_living");
    public static final SoundEvent DILOPHOSAURUS_SPIT = create("dilophosaurus_spit");
    public static final SoundEvent DILOPHOSAURUS_HURT = create("dilophosaurus_hurt");
    public static final SoundEvent DILOPHOSAURUS_DEATH = create("dilophosaurus_death");

    public static final SoundEvent CARNOTAURUS_LIVING = create("carnotaurus_living");
    public static final SoundEvent CARNOTAURUS_HURT = create("carnotaurus_hurt");
    public static final SoundEvent CARNOTAURUS_DEATH = create("carnotaurus_death");

    public static final SoundEvent GALLIMIMUS_LIVING = create("gallimimus_living");
    public static final SoundEvent GALLIMIMUS_DEATH = create("gallimimus_death");
    public static final SoundEvent GALLIMIMUS_HURT = create("gallimimus_hurt");

    public static final SoundEvent ORNITHOMIMUS_LIVING = create("ornithomimus_living");
    public static final SoundEvent ORNITHOMIMUS_DEATH = create("ornithomimus_death");
    public static final SoundEvent ORNITHOMIMUS_HURT = create("ornithomimus_hurt");

    public static final SoundEvent SPINOSAURUS_LIVING = create("spinosaurus_living");
    public static final SoundEvent SPINOSAURUS_HURT = create("spinosaurus_hurt");
    public static final SoundEvent SPINOSAURUS_DEATH = create("spinosaurus_death");
    public static final SoundEvent SPINOSAURUS_CALL = create("spinosaurus_call");
    public static final SoundEvent SPINOSAURUS_ROAR = create("spinosaurus_roar");
    public static final SoundEvent SPINOSAURUS_BREATHING = create("spinosaurus_breathing");
    public static final SoundEvent SPINOSAURUS_THREAT = create("spinosaurus_threat");

    public static final SoundEvent STYRACOSAURUS_LIVING = create("styracosaurus_living");
    public static final SoundEvent STYRACOSAURUS_HURT = create("styracosaurus_hurt");
    public static final SoundEvent STYRACOSAURUS_DEATH = create("styracosaurus_death");
    public static final SoundEvent STYRACOSAURUS_CALL = create("styracosaurus_call");
    public static final SoundEvent STYRACOSAURUS_ROAR = create("styracosaurus_roar");
    public static final SoundEvent STYRACOSAURUS_THREAT = create("styracosaurus_threat");

    public static final SoundEvent TROPEOGNATHUS_LIVING = create("tropeognathus_living");
    public static final SoundEvent TROPEOGNATHUS_HURT = create("tropeognathus_hurt");
    public static final SoundEvent TROPEOGNATHUS_DEATH = create("tropeognathus_death");
    public static final SoundEvent TROPEOGNATHUS_ROAR = create("tropeognathus_roar");
    public static final SoundEvent TROPEOGNATHUS_THREAT = create("tropeognathus_threat");

    public static final SoundEvent ZHENYUANOPTERUS_LIVING = create("zhenyuanopterus_living");
    public static final SoundEvent ZHENYUANOPTERUS_HURT = create("zhenyuanopterus_hurt");
    public static final SoundEvent ZHENYUANOPTERUS_DEATH = create("zhenyuanopterus_death");
    public static final SoundEvent ZHENYUANOPTERUS_ROAR = create("zhenyuanopterus_roar");
    public static final SoundEvent ZHENYUANOPTERUS_THREAT = create("zhenyuanopterus_threat");

    public static final SoundEvent MICROCERATUS_LIVING = create("microceratus_living");
    public static final SoundEvent MICROCERATUS_HURT = create("microceratus_hurt");
    public static final SoundEvent MICROCERATUS_DEATH = create("microceratus_death");
    public static final SoundEvent MICROCERATUS_THREAT = create("microceratus_threat");

    public static final SoundEvent METRIACANTHOSAURUS_LIVING = create("metriacanthosaurus_living");
    public static final SoundEvent METRIACANTHOSAURUS_HURT = create("metriacanthosaurus_hurt");
    public static final SoundEvent METRIACANTHOSAURUS_DEATH = create("metriacanthosaurus_death");
    public static final SoundEvent METRIACANTHOSAURUS_CALL = create("metriacanthosaurus_call");
    public static final SoundEvent METRIACANTHOSAURUS_ROAR = create("metriacanthosaurus_roar");
    public static final SoundEvent METRIACANTHOSAURUS_THREAT = create("metriacanthosaurus_threat");

    public static final SoundEvent MAJUNGASAURUS_LIVING = create("majungasaurus_living");
    public static final SoundEvent MAJUNGASAURUS_HURT = create("majungasaurus_hurt");
    public static final SoundEvent MAJUNGASAURUS_DEATH = create("majungasaurus_death");
    public static final SoundEvent MAJUNGASAURUS_CALL = create("majungasaurus_call");
    public static final SoundEvent MAJUNGASAURUS_ROAR = create("majungasaurus_roar");
    public static final SoundEvent MAJUNGASAURUS_BREATHING = create("majungasaurus_breathing");
    public static final SoundEvent MAJUNGASAURUS_THREAT = create("majungasaurus_threat");

    public static final SoundEvent LAMBEOSAURUS_LIVING = create("lambeosaurus_living");
    public static final SoundEvent LAMBEOSAURUS_HURT = create("lambeosaurus_hurt");
    public static final SoundEvent LAMBEOSAURUS_DEATH = create("lambeosaurus_death");
    public static final SoundEvent LAMBEOSAURUS_CALL = create("lambeosaurus_call");
    public static final SoundEvent LAMBEOSAURUS_THREAT = create("lambeosaurus_threat");

    public static final SoundEvent LUDODACTYLUS_LIVING = create("ludodactylus_living");
    public static final SoundEvent LUDODACTYLUS_HURT = create("ludodactylus_hurt");
    public static final SoundEvent LUDODACTYLUS_DEATH = create("ludodactylus_death");
    public static final SoundEvent LUDODACTYLUS_CALL = create("ludodactylus_call");
    public static final SoundEvent LUDODACTYLUS_THREAT = create("ludodactylus_threat");

    public static final SoundEvent LEAELLYNASAURA_LIVING = create("leaellynasaura_living");
    public static final SoundEvent LEAELLYNASAURA_HURT = create("leaellynasaura_hurt");
    public static final SoundEvent LEAELLYNASAURA_DEATH = create("leaellynasaura_death");

    public static final SoundEvent LEPTICTIDIUM_LIVING = create("leptictidium_living");
    public static final SoundEvent LEPTICTIDIUM_HURT = create("leptictidium_hurt");
    public static final SoundEvent LEPTICTIDIUM_DEATH = create("leptictidium_death");

    public static final SoundEvent GIGANOTOSAURUS_LIVING = create("giganotosaurus_living");
    public static final SoundEvent GIGANOTOSAURUS_HURT = create("giganotosaurus_hurt");
    public static final SoundEvent GIGANOTOSAURUS_DEATH = create("giganotosaurus_death");
    public static final SoundEvent GIGANOTOSAURUS_CALL = create("giganotosaurus_call");
    public static final SoundEvent GIGANOTOSAURUS_ROAR = create("giganotosaurus_roar");
    public static final SoundEvent GIGANOTOSAURUS_THREAT = create("giganotosaurus_threat");

    public static final SoundEvent HERRERASAURUS_LIVING = create("herrerasaurus_living");
    public static final SoundEvent HERRERASAURUS_HURT = create("herrerasaurus_hurt");
    public static final SoundEvent HERRERASAURUS_DEATH = create("herrerasaurus_death");
    public static final SoundEvent HERRERASAURUS_CALL = create("herrerasaurus_call");
    public static final SoundEvent HERRERASAURUS_ROAR = create("herrerasaurus_roar");
    public static final SoundEvent HERRERASAURUS_THREAT = create("herrerasaurus_threat");

    public static final SoundEvent EDMONTOSAURUS_LIVING = create("edmontosaurus_living");
    public static final SoundEvent EDMONTOSAURUS_HURT = create("edmontosaurus_hurt");
    public static final SoundEvent EDMONTOSAURUS_DEATH = create("edmontosaurus_death");
    public static final SoundEvent EDMONTOSAURUS_CALL = create("edmontosaurus_call");
    public static final SoundEvent EDMONTOSAURUS_THREAT = create("edmontosaurus_threat");

    public static final SoundEvent DUNKLEOSTEUS_LIVING = create("dunkleosteus_living");
    public static final SoundEvent DUNKLEOSTEUS_HURT = create("dunkleosteus_hurt");
    public static final SoundEvent DUNKLEOSTEUS_DEATH = create("dunkleosteus_death");
    public static final SoundEvent DUNKLEOSTEUS_THREAT = create("dunkleosteus_threat");

    public static final SoundEvent THERIZINOSAURUS_LIVING = create("therizinosaurus_living");
    public static final SoundEvent THERIZINOSAURUS_HURT = create("therizinosaurus_hurt");
    public static final SoundEvent THERIZINOSAURUS_DEATH = create("therizinosaurus_death");
    public static final SoundEvent THERIZINOSAURUS_ROAR = create("therizinosaurus_roar");
    public static final SoundEvent THERIZINOSAURUS_THREAT = create("therizinosaurus_threat");

    public static final SoundEvent BARYONYX_LIVING = create("baryonyx_living");
    public static final SoundEvent BARYONYX_HURT = create("baryonyx_hurt");
    public static final SoundEvent BARYONYX_DEATH = create("baryonyx_death");
    public static final SoundEvent BARYONYX_CALL = create("baryonyx_call");
    public static final SoundEvent BARYONYX_ROAR = create("baryonyx_roar");
    public static final SoundEvent BARYONYX_THREAT = create("baryonyx_threat");

    public static final SoundEvent DIMORPHODON_LIVING = create("dimorphodon_living");
    public static final SoundEvent DIMORPHODON_HURT = create("dimorphodon_hurt");
    public static final SoundEvent DIMORPHODON_DEATH = create("dimorphodon_death");
    public static final SoundEvent DIMORPHODON_CALL = create("dimorphodon_call");
    public static final SoundEvent DIMORPHODON_THREAT = create("dimorphodon_threat");

    public static final SoundEvent COMPSOGNATHUS_LIVING = create("compsognathus_living");
    public static final SoundEvent COMPSOGNATHUS_HURT = create("compsognathus_hurt");
    public static final SoundEvent COMPSOGNATHUS_DEATH = create("compsognathus_death");
    public static final SoundEvent COMPSOGNATHUS_CALL = create("compsognathus_call");
    public static final SoundEvent COMPSOGNATHUS_THREAT = create("compsognathus_threat");

    public static final SoundEvent CORYTHOSAURUS_LIVING = create("corythosaurus_living");
    public static final SoundEvent CORYTHOSAURUS_HURT = create("corythosaurus_hurt");
    public static final SoundEvent CORYTHOSAURUS_DEATH = create("corythosaurus_death");
    public static final SoundEvent CORYTHOSAURUS_THREAT = create("corythosaurus_threat");
    public static final SoundEvent CORYTHOSAURUS_CALL = create("corythosaurus_call");

    public static final SoundEvent CHASMOSAURUS_LIVING = create("chasmosaurus_living");
    public static final SoundEvent CHASMOSAURUS_HURT = create("chasmosaurus_hurt");
    public static final SoundEvent CHASMOSAURUS_DEATH = create("chasmosaurus_death");
    public static final SoundEvent CHASMOSAURUS_THREAT = create("chasmosaurus_threat");

    public static final SoundEvent APATOSAURUS_LIVING = create("apatosaurus_living");
    public static final SoundEvent APATOSAURUS_HURT = create("apatosaurus_hurt");
    public static final SoundEvent APATOSAURUS_DEATH = create("apatosaurus_death");
    public static final SoundEvent APATOSAURUS_CALL = create("apatosaurus_call");
    public static final SoundEvent APATOSAURUS_THREAT = create("apatosaurus_threat");

    public static final SoundEvent CHILESAURUS_LIVING = create("chilesaurus_living");
    public static final SoundEvent CHILESAURUS_HURT = create("chilesaurus_hurt");
    public static final SoundEvent CHILESAURUS_DEATH = create("chilesaurus_death");

    public static final SoundEvent CEARADACTYLUS_LIVING = create("cearadactylus_living");
    public static final SoundEvent CEARADACTYLUS_HURT = create("cearadactylus_hurt");
    public static final SoundEvent CEARADACTYLUS_DEATH = create("cearadactylus_death");
    public static final SoundEvent CEARADACTYLUS_CALL = create("cearadactylus_call");
    public static final SoundEvent CEARADACTYLUS_THREAT = create("cearadactylus_threat");

    public static final SoundEvent PACHYCEPHALOSAURUS_LIVING = create("pachycephalosaurus_living");
    public static final SoundEvent PACHYCEPHALOSAURUS_HURT = create("pachycephalosaurus_hurt");
    public static final SoundEvent PACHYCEPHALOSAURUS_DEATH = create("pachycephalosaurus_death");
    public static final SoundEvent PACHYCEPHALOSAURUS_CALL = create("pachycephalosaurus_call");
    public static final SoundEvent PACHYCEPHALOSAURUS_THREAT = create("pachycephalosaurus_threat");

    public static final SoundEvent PROTOCERATOPS_LIVING = create("protoceratops_living");
    public static final SoundEvent PROTOCERATOPS_HURT = create("protoceratops_hurt");
    public static final SoundEvent PROTOCERATOPS_DEATH = create("protoceratops_death");
    public static final SoundEvent PROTOCERATOPS_THREAT = create("protoceratops_threat");

    public static final SoundEvent MOGANOPTERUS_LIVING = create("moganopterus_living");
    public static final SoundEvent MOGANOPTERUS_HURT = create("moganopterus_hurt");
    public static final SoundEvent MOGANOPTERUS_DEATH = create("moganopterus_death");
    public static final SoundEvent MOGANOPTERUS_THREAT = create("moganopterus_threat");

    public static final SoundEvent COELURUS_LIVING = create("coelurus_living");
    public static final SoundEvent COELURUS_HURT = create("coelurus_hurt");
    public static final SoundEvent COELURUS_DEATH = create("coelurus_death");
    public static final SoundEvent COELURUS_THREAT = create("coelurus_threat");

    public static final SoundEvent SEGISAURUS_LIVING = create("segisaurus_living");
    public static final SoundEvent SEGISAURUS_HURT = create("segisaurus_hurt");
    public static final SoundEvent SEGISAURUS_DEATH = create("segisaurus_death");
    public static final SoundEvent SEGISAURUS_THREAT = create("segisaurus_threat");

    public static final SoundEvent QUETZALCOATLUS_LIVING = create("quetzalcoatlus_living");
    public static final SoundEvent QUETZALCOATLUS_HURT = create("quetzalcoatlus_hurt");
    public static final SoundEvent QUETZALCOATLUS_DEATH = create("quetzalcoatlus_death");
    public static final SoundEvent QUETZALCOATLUS_THREAT = create("quetzalcoatlus_threat");

    public static final SoundEvent OVIRAPTOR_LIVING = create("oviraptor_living");
    public static final SoundEvent OVIRAPTOR_HURT = create("oviraptor_hurt");
    public static final SoundEvent OVIRAPTOR_DEATH = create("oviraptor_death");
    public static final SoundEvent OVIRAPTOR_THREAT = create("oviraptor_threat");

    public static final SoundEvent OTHNIELIA_LIVING = create("othnielia_living");
    public static final SoundEvent OTHNIELIA_HURT = create("othnielia_hurt");
    public static final SoundEvent OTHNIELIA_DEATH = create("othnielia_death");

    public static final SoundEvent CERATOSAURUS_LIVING = create("ceratosaurus_living");
    public static final SoundEvent CERATOSAURUS_HURT = create("ceratosaurus_hurt");
    public static final SoundEvent CERATOSAURUS_DEATH = create("ceratosaurus_death");
    public static final SoundEvent CERATOSAURUS_CALL = create("ceratosaurus_call");
    public static final SoundEvent CERATOSAURUS_ROAR = create("ceratosaurus_roar");
    public static final SoundEvent CERATOSAURUS_BREATHING = create("ceratosaurus_breathing");
    public static final SoundEvent CERATOSAURUS_THREAT = create("ceratosaurus_threat");

    public static final SoundEvent ALVAREZSAURUS_LIVING = create("alvarezsaurus_living");
    public static final SoundEvent ALVAREZSAURUS_HURT = create("alvarezsaurus_hurt");
    public static final SoundEvent ALVAREZSAURUS_DEATH = create("alvarezsaurus_death");
    public static final SoundEvent ALVAREZSAURUS_CALL = create("alvarezsaurus_call");
    public static final SoundEvent ALVAREZSAURUS_THREAT = create("alvarezsaurus_threat");

    public static final SoundEvent TROODON_LIVING = create("troodon_living");
    public static final SoundEvent TROODON_HURT = create("troodon_hurt");
    public static final SoundEvent TROODON_DEATH = create("troodon_death");
    public static final SoundEvent TROODON_CALL = create("troodon_call");
    public static final SoundEvent TROODON_THREAT = create("troodon_threat");

    public static final SoundEvent PTERANODON_LIVING = create("pteranodon_living");
    public static final SoundEvent PTERANODON_HURT = create("pteranodon_hurt");
    public static final SoundEvent PTERANODON_DEATH = create("pteranodon_death");
    public static final SoundEvent PTERANODON_CALL = create("pteranodon_call");

    public static final SoundEvent HYAENODON_LIVING = create("hyaenodon_living");
    public static final SoundEvent HYAENODON_HURT = create("hyaenodon_hurt");
    public static final SoundEvent HYAENODON_DEATH = create("hyaenodon_death");
    public static final SoundEvent HYAENODON_CALL = create("hyaenodon_call");

    public static final SoundEvent INDOMINUS_LIVING = create("indominus_living");
    public static final SoundEvent INDOMINUS_DEATH = create("indominus_death");
    public static final SoundEvent INDOMINUS_HURT = create("indominus_hurt");
    public static final SoundEvent INDOMINUS_ROAR = create("indominus_roar");
    public static final SoundEvent INDOMINUS_BREATHING = create("indominus_breathing");

    public static final SoundEvent TYRANNOSAURUS_BREATHING = create("tyrannosaurus_breathing");
    public static final SoundEvent TYRANNOSAURUS_DEATH = create("tyrannosaurus_death");
    public static final SoundEvent TYRANNOSAURUS_HURT = create("tyrannosaurus_hurt");
    public static final SoundEvent TYRANNOSAURUS_ROAR = create("tyrannosaurus_roar");
    public static final SoundEvent TYRANNOSAURUS_LIVING = create("tyrannosaurus_living");

    public static final SoundEvent VELOCIRAPTOR_LIVING = create("velociraptor_living");
    public static final SoundEvent VELOCIRAPTOR_HURT = create("velociraptor_hurt");
    public static final SoundEvent VELOCIRAPTOR_BREATHING = create("velociraptor_breathing");
    public static final SoundEvent VELOCIRAPTOR_CALL = create("velociraptor_call");
    public static final SoundEvent VELOCIRAPTOR_DEATH = create("velociraptor_death");
    public static final SoundEvent VELOCIRAPTOR_ATTACK = create("velociraptor_attack");

    public static final SoundEvent ACHILLOBATOR_LIVING = create("achillobator_living");
    public static final SoundEvent ACHILLOBATOR_HURT = create("achillobator_hurt");
    public static final SoundEvent ACHILLOBATOR_MATE_CALL = create("achillobator_mate_call");
    public static final SoundEvent ACHILLOBATOR_CALL = create("achillobator_call");
    public static final SoundEvent ACHILLOBATOR_DEATH = create("achillobator_death");
    public static final SoundEvent ACHILLOBATOR_ATTACK = create("achillobator_attack");

    public static final SoundEvent ANKYLOSAURUS_LIVING = create("ankylosaurus_living");
    public static final SoundEvent ANKYLOSAURUS_HURT = create("ankylosaurus_hurt");
    public static final SoundEvent ANKYLOSAURUS_MATE_CALL = create("ankylosaurus_mate_call");
    public static final SoundEvent ANKYLOSAURUS_CALL = create("ankylosaurus_call");
    public static final SoundEvent ANKYLOSAURUS_DEATH = create("ankylosaurus_death");
    public static final SoundEvent ANKYLOSAURUS_ATTACK = create("ankylosaurus_attack");

    public static final SoundEvent MICRORAPTOR_LIVING = create("microraptor_living");
    public static final SoundEvent MICRORAPTOR_HURT = create("microraptor_hurt");
    public static final SoundEvent MICRORAPTOR_DEATH = create("microraptor_death");
    public static final SoundEvent MICRORAPTOR_ATTACK = create("microraptor_attack");

    public static final SoundEvent MUSSAURUS_LIVING = create("mussaurus_living");
    public static final SoundEvent MUSSAURUS_HURT = create("mussaurus_hurt");
    public static final SoundEvent MUSSAURUS_DEATH = create("mussaurus_death");
    public static final SoundEvent MUSSAURUS_ATTACK = create("mussaurus_attack");
    public static final SoundEvent MUSSAURUS_MATE_CALL = create("mussaurus_mate_call");

    public static final SoundEvent MOSASAURUS_LIVING = create("mosasaurus_living");
    public static final SoundEvent MOSASAURUS_HURT = create("mosasaurus_hurt");
    public static final SoundEvent MOSASAURUS_DEATH = create("mosasaurus_death");
    public static final SoundEvent MOSASAURUS_MATE_CALL = create("mosasaurus_mate_call");
    public static final SoundEvent MOSASAURUS_ATTACK = create("mosasaurus_attack");
    public static final SoundEvent MOSASAURUS_ROAR = create("mosasaurus_roar");

    public static final SoundEvent ALLOSAURUS_DEATH = create("allosaurus_death");
    public static final SoundEvent ALLOSAURUS_FIGHT_FEMALE = create("allosaurus_fight_female");
    public static final SoundEvent ALLOSAURUS_HURT = create("allosaurus_hurt");
    public static final SoundEvent ALLOSAURUS_LIVING = create("allosaurus_living");
    public static final SoundEvent ALLOSAURUS_MATE_CALL = create("allosaurus_mate_call");
    public static final SoundEvent ALLOSAURUS_ROAR = create("allosaurus_roar");
    public static final SoundEvent ALLOSAURUS_THREAT = create("allosaurus_threat");

    public static final SoundEvent BEELZEBUFO_CROAK = create("beelzebufo_croak");
    public static final SoundEvent BEELZEBUFO_HURT = create("beelzebufo_hurt");
    public static final SoundEvent BEELZEBUFO_HURT_THREAT = create("beelzebufo_hurt_threat");
    public static final SoundEvent BEELZEBUFO_LONG_DISTANCE_CROAK = create("beelzebufo_long_distance_croak");
    public static final SoundEvent BEELZEBUFO_THREAT = create("beelzebufo_threat");

    public static final SoundEvent CARCHARODONTOSAURUS_GROWL = create("carcharodontosaurus_growl");
    public static final SoundEvent CARCHARODONTOSAURUS_HISS = create("carcharodontosaurus_hiss");
    public static final SoundEvent CARCHARODONTOSAURUS_HURT = create("carcharodontosaurus_hurt");
    public static final SoundEvent CARCHARODONTOSAURUS_LIVING = create("carcharodontosaurus_living");
    public static final SoundEvent CARCHARODONTOSAURUS_ROAR = create("carcharodontosaurus_roar");

    public static final SoundEvent GUANLONG_DEATH = create("guanlong_death");
    public static final SoundEvent GUANLONG_HURT = create("guanlong_hurt");
    public static final SoundEvent GUANLONG_LIVING = create("guanlong_living");

    public static final SoundEvent PROCERATOSAURUS_ATTACK = create("proceratosaurus_attack");
    public static final SoundEvent PROCERATOSAURUS_DEATH = create("proceratosaurus_death");
    public static final SoundEvent PROCERATOSAURUS_HURT = create("proceratosaurus_hurt");
    public static final SoundEvent PROCERATOSAURUS_LIVING = create("proceratosaurus_living");
    public static final SoundEvent PROCERATOSAURUS_MATE_CALL = create("proceratosaurus_mate_call");
    public static final SoundEvent PROCERATOSAURUS_THREAT = create("proceratosaurus_threat");

    public static final SoundEvent SUCHOMIMUS_DEATH = create("suchomimus_death");
    public static final SoundEvent SUCHOMIMUS_HURT = create("suchomimus_hurt");
    public static final SoundEvent SUCHOMIMUS_LIVING = create("suchomimus_living");
    public static final SoundEvent SUCHOMIMUS_MATE_CALL = create("suchomimus_mate_call");
    public static final SoundEvent SUCHOMIMUS_ROAR = create("suchomimus_roar");

    public static final SoundEvent MAMMOTH_ALARM_CALL = create("mammoth_alarm_call");
    public static final SoundEvent MAMMOTH_DEATH = create("mammoth_death");
    public static final SoundEvent MAMMOTH_HURT = create("mammoth_hurt");
    public static final SoundEvent MAMMOTH_LIVING = create("mammoth_living");
    public static final SoundEvent MAMMOTH_MATE_CALL = create("mammoth_mate_call");
    public static final SoundEvent MAMMOTH_THREAT = create("mammoth_threat");

    public static final SoundEvent ELASMOTHERIUM_DEATH = create("elasmotherium_death");
    public static final SoundEvent ELASMOTHERIUM_HURT = create("elasmotherium_hurt");
    public static final SoundEvent ELASMOTHERIUM_LIVING = create("elasmotherium_living");
    public static final SoundEvent ELASMOTHERIUM_THREAT = create("elasmotherium_threat");

    public static final SoundEvent DEINOTHERIUM_ALARM_CALL = create("deinotherium_alarm_call");
    public static final SoundEvent DEINOTHERIUM_DEATH = create("deinotherium_death");
    public static final SoundEvent DEINOTHERIUM_HURT = create("deinotherium_hurt");
    public static final SoundEvent DEINOTHERIUM_LIVING = create("deinotherium_living");
    public static final SoundEvent DEINOTHERIUM_MATE_CALL = create("deinotherium_mate_call");
    public static final SoundEvent DEINOTHERIUM_THREAT = create("deinotherium_threat");

    public static final SoundEvent ARSINOITHERIUM_DEATH = create("arsinoitherium_death");
    public static final SoundEvent ARSINOITHERIUM_HURT = create("arsinoitherium_hurt");
    public static final SoundEvent ARSINOITHERIUM_LIVING = create("arsinoitherium_living");
    public static final SoundEvent ARSINOITHERIUM_THREAT = create("arsinoitherium_threat");

    public static final SoundEvent POSTOSUCHUS_ATTACK = create("postosuchus_attack");
    public static final SoundEvent POSTOSUCHUS_CALL = create("postosuchus_call");
    public static final SoundEvent POSTOSUCHUS_DEATH = create("postosuchus_death");

    public static final SoundEvent GOAT_LIVING = create("goat_living");
    public static final SoundEvent GOAT_HURT = create("goat_hurt");
    public static final SoundEvent GOAT_DEATH = create("goat_death");

    public static final SoundEvent FENCE_SHOCK = create("fence_shock");

    public static final SoundEvent FIRE = create("fire");
    public static final SoundEvent EMPTY = create("empty");
    public static final SoundEvent RELOAD = create("reload");

    private static List<SoundEvent> sounds = Lists.newArrayList();


    public static SoundEvent create(String soundName) {
        SoundEvent sound = new SoundEvent(new ResourceLocation(RebornMod.MODID, soundName));
        RegistryHandler.registerSound(sound, soundName);
        return sound;
    }

    public static List<SoundEvent> getSounds()
    {
        return sounds;
    }
}