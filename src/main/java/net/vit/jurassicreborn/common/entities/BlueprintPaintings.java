package net.vit.jurassicreborn.common.entities;


import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vit.jurassicreborn.JurassicReborn;

public class BlueprintPaintings {
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS =
            DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, JurassicReborn.MODID);

    private static RegistryObject<PaintingVariant> reg(String id) {
        // 96 × 64 pixels  → 6 × 4 blocks
        return PAINTING_VARIANTS.register(id, () -> new PaintingVariant(96, 64));
    }

    /* ───────── all blueprint variants, unified size ───────── */
    public static final RegistryObject<PaintingVariant> TYRANNOSAURUS      = reg("tyrannosaurus");
    public static final RegistryObject<PaintingVariant> ANKYLODOCUS        = reg("ankylodocus");
    public static final RegistryObject<PaintingVariant> ANKYLOSAURUS       = reg("ankylosaurus");
    public static final RegistryObject<PaintingVariant> APATOSAURUS        = reg("apatosaurus");
    public static final RegistryObject<PaintingVariant> ARSINOITHERIUM     = reg("arsinoitherium");
    public static final RegistryObject<PaintingVariant> BARYONYX           = reg("baryonyx");
    public static final RegistryObject<PaintingVariant> BRACHIOSAURUS      = reg("brachiosaurus");
    public static final RegistryObject<PaintingVariant> CAMARASAURUS       = reg("camarasaurus");
    public static final RegistryObject<PaintingVariant> CARCHARODONTOSAURUS= reg("carcharodontosaurus");
    public static final RegistryObject<PaintingVariant> CARNOTAURUS        = reg("carnotaurus");
    public static final RegistryObject<PaintingVariant> CERATOSAURUS       = reg("ceratosaurus");
    public static final RegistryObject<PaintingVariant> CHASMOSAURUS       = reg("chasmosaurus");
    public static final RegistryObject<PaintingVariant> COMPSOGNATHUS      = reg("compsognathus");
    public static final RegistryObject<PaintingVariant> DEINOSUCHUS        = reg("deinosuchus");
    public static final RegistryObject<PaintingVariant> DEINOTHERIUM       = reg("deinotherium");
    public static final RegistryObject<PaintingVariant> DIMETRODON         = reg("dimetrodon");
    public static final RegistryObject<PaintingVariant> DIPLODOCUS         = reg("diplodocus");
    public static final RegistryObject<PaintingVariant> DREADNOUGHTUS      = reg("dreadnoughtus");
    public static final RegistryObject<PaintingVariant> ELASMOTHERIUM      = reg("elasmotherium");
    public static final RegistryObject<PaintingVariant> HERRERASAURUS      = reg("herrerasaurus");
    public static final RegistryObject<PaintingVariant> INDORAPTOR         = reg("indoraptor");
    public static final RegistryObject<PaintingVariant> LEPTICTIDIUM       = reg("leptictidium");
    public static final RegistryObject<PaintingVariant> MAIASAURA          = reg("maiasaura");
    public static final RegistryObject<PaintingVariant> MAMENCHISAURUS     = reg("mamenchisaurus");
    public static final RegistryObject<PaintingVariant> MEGATHERIUM        = reg("megatherium");
    public static final RegistryObject<PaintingVariant> MEGALODON          = reg("megalodon");
    public static final RegistryObject<PaintingVariant> MOSASAURUS         = reg("mosasaurus");
    public static final RegistryObject<PaintingVariant> OVIRAPTOR          = reg("oviraptor");
    public static final RegistryObject<PaintingVariant> PARASAUROLOPHUS    = reg("parasaurolophus");
    public static final RegistryObject<PaintingVariant> QUETZALCOATLUS     = reg("quetzalcoatlus");
    public static final RegistryObject<PaintingVariant> RAPHUSREX          = reg("raphusrex");
    public static final RegistryObject<PaintingVariant> RUGOPS             = reg("rugops");
    public static final RegistryObject<PaintingVariant> SINOCERATOPS       = reg("sinoceratops");
    public static final RegistryObject<PaintingVariant> SMILODON           = reg("smilodon");
    public static final RegistryObject<PaintingVariant> SPINORAPTOR        = reg("spinoraptor");
    public static final RegistryObject<PaintingVariant> SPINOSAURUS        = reg("spinosaurus");
    public static final RegistryObject<PaintingVariant> STYRACOSAURUS      = reg("styracosaurus");
    public static final RegistryObject<PaintingVariant> TITANIS            = reg("titanis");
    public static final RegistryObject<PaintingVariant> PERISPHINCTES      = reg("perisphinctes");
    public static final RegistryObject<PaintingVariant> MICROCERATUS       = reg("microceratus");
    public static final RegistryObject<PaintingVariant> PROTOCERATOPS      = reg("protoceratops");
    public static final RegistryObject<PaintingVariant> TITANITES          = reg("titanites");
    public static final RegistryObject<PaintingVariant> PARAPUZOSIA        = reg("parapuzosia");
    public static final RegistryObject<PaintingVariant> PATAGOTIATN        = reg("patagotitan");
    public static final RegistryObject<PaintingVariant> ASTEROCERAS        = reg("asteroceras");
    public static final RegistryObject<PaintingVariant> CAMEROCERAS        = reg("cameroceras");
    public static final RegistryObject<PaintingVariant> ENDOCERAS          = reg("endoceras");
    public static final RegistryObject<PaintingVariant> ORTHOCERAS         = reg("orthoceras");
    public static final RegistryObject<PaintingVariant> VECTIPELTA         = reg("vectipelta");
    public static final RegistryObject<PaintingVariant> TRICERATOPS        = reg("triceratops");
    public static final RegistryObject<PaintingVariant> PARACERATHERIUM    = reg("paraceratherium");
    public static final RegistryObject<PaintingVariant> KAIRUKU    = reg("kairuku");
    public static final RegistryObject<PaintingVariant> CALYMENE    = reg("calymene");
    public static final RegistryObject<PaintingVariant> NIGERSAURUS    = reg("nigersaurus");
    public static final RegistryObject<PaintingVariant> THERIZINOSAURUS        = reg("therizinosaurus");
    public static final RegistryObject<PaintingVariant> STEGOSAURUS        = reg("stegosaurus");
    public static final RegistryObject<PaintingVariant> GIGANOTOSAURUS        = reg("giganotosaurus");
    public static final RegistryObject<PaintingVariant> EDMONTOSAURUS          = reg("edmontosaurus");
    public static final RegistryObject<PaintingVariant> LAMBEOSAURUS         = reg("lambeosaurus");
    public static final RegistryObject<PaintingVariant> CORYTHOSAURUS         = reg("corythosaurus");
    public static final RegistryObject<PaintingVariant> TROODON        = reg("troodon");
    public static final RegistryObject<PaintingVariant> DILOPHOSAURUS    = reg("dilophosaurus");
    public static final RegistryObject<PaintingVariant> DUNKLEOSTEUS    = reg("dunkleosteus");
    public static final RegistryObject<PaintingVariant> LIVYATAN    = reg("livyatan");
    public static final RegistryObject<PaintingVariant> MAJUNGASAURUS    = reg("majungasaurus");


    public static void register(IEventBus eventBus) {
        PAINTING_VARIANTS.register(eventBus);
    }
}
