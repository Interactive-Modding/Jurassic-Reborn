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

    public static final SoundEvent SPINOSAURUS_LIVING = create("spinosaurus_living");
    public static final SoundEvent SPINOSAURUS_HURT = create("spinosaurus_hurt");
    public static final SoundEvent SPINOSAURUS_DEATH = create("spinosaurus_death");
    public static final SoundEvent SPINOSAURUS_CALL = create("spinosaurus_call");
    public static final SoundEvent SPINOSAURUS_ROAR = create("spinosaurus_roar");
    public static final SoundEvent SPINOSAURUS_BREATHING = create("spinosaurus_breathing");
    public static final SoundEvent SPINOSAURUS_THREAT = create("spinosaurus_threat");

    public static final SoundEvent PTERANODON_LIVING = create("pteranodon_living");
    public static final SoundEvent PTERANODON_HURT = create("pteranodon_hurt");
    public static final SoundEvent PTERANODON_DEATH = create("pteranodon_death");
    public static final SoundEvent PTERANODON_CALL = create("pteranodon_call");

    public static final SoundEvent INDOMINUS_LIVING = create("indominus_living");
    public static final SoundEvent INDOMINUS_DEATH = create("indominus_death");
    public static final SoundEvent INDOMINUS_HURT = create("indominus_hurt");
    public static final SoundEvent INDOMINUS_ROAR = create("indominus_roar");
    public static final SoundEvent INDOMINUS_BREATHING = create("indominus_breathing");

    public static final SoundEvent ANKYLOSAURUS_LIVING = create("ankylosaurus_living");
    public static final SoundEvent ANKYLOSAURUS_HURT = create("ankylosaurus_hurt");

    public static final SoundEvent HERRERASAURUS_LIVING = create("herrerasaurus_living");
    public static final SoundEvent HERRERASAURUS_DEATH = create("herrerasaurus_death");

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

    public static final SoundEvent CERATOSAURUS_HURT = create("ceratosaurus_hurt");
    public static final SoundEvent CERATOSAURUS_LIVING = create("ceratosaurus_living");

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

    public static final SoundEvent STYRACOSAURUS_CALL = create("styracosaurus_call");
    public static final SoundEvent STYRACOSAURUS_DEATH = create("styracosaurus_death");
    public static final SoundEvent STYRACOSAURUS_ROAR = create("styracosaurus_roar");

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