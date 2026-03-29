package net.vit.jurassicreborn.common.entities;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vit.jurassicreborn.JurassicReborn;

public class BlueprintPaintings {
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS =
            DeferredRegister.create(Registries.PAINTING_VARIANT, JurassicReborn.MODID);

    private static DeferredHolder<PaintingVariant, PaintingVariant> reg(String id) {
        // 96 × 64 pixels  → 6 × 4 blocks
        return PAINTING_VARIANTS.register(id, () -> new PaintingVariant(96, 64, ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, id)));
    }

    /* ───────── all blueprint variants, unified size ───────── */
    public static final DeferredHolder<PaintingVariant, PaintingVariant> TYRANNOSAURUS      = reg("tyrannosaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> ANKYLODOCUS        = reg("ankylodocus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> ANKYLOSAURUS       = reg("ankylosaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> APATOSAURUS        = reg("apatosaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> ARSINOITHERIUM     = reg("arsinoitherium");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> BARYONYX           = reg("baryonyx");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> BRACHIOSAURUS      = reg("brachiosaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> CAMARASAURUS       = reg("camarasaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> CARCHARODONTOSAURUS= reg("carcharodontosaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> CARNOTAURUS        = reg("carnotaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> CERATOSAURUS       = reg("ceratosaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> CHASMOSAURUS       = reg("chasmosaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> COMPSOGNATHUS      = reg("compsognathus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> DEINOSUCHUS        = reg("deinosuchus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> DEINOTHERIUM       = reg("deinotherium");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> DIMETRODON         = reg("dimetrodon");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> DIPLODOCUS         = reg("diplodocus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> DREADNOUGHTUS      = reg("dreadnoughtus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> ELASMOTHERIUM      = reg("elasmotherium");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> HERRERASAURUS      = reg("herrerasaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> INDORAPTOR         = reg("indoraptor");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> LEPTICTIDIUM       = reg("leptictidium");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> MAIASAURA          = reg("maiasaura");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> MAMENCHISAURUS     = reg("mamenchisaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> MEGATHERIUM        = reg("megatherium");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> MEGALODON          = reg("megalodon");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> MOSASAURUS         = reg("mosasaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> OVIRAPTOR          = reg("oviraptor");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> PARASAUROLOPHUS    = reg("parasaurolophus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> QUETZALCOATLUS     = reg("quetzalcoatlus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> RAPHUSREX          = reg("raphusrex");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> RUGOPS             = reg("rugops");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> SINOCERATOPS       = reg("sinoceratops");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> SMILODON           = reg("smilodon");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> SPINORAPTOR        = reg("spinoraptor");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> SPINOSAURUS        = reg("spinosaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> STYRACOSAURUS      = reg("styracosaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> TITANIS            = reg("titanis");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> PERISPHINCTES      = reg("perisphinctes");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> MICROCERATUS       = reg("microceratus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> PROTOCERATOPS      = reg("protoceratops");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> TITANITES          = reg("titanites");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> PARAPUZOSIA        = reg("parapuzosia");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> PATAGOTIATN        = reg("patagotitan");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> ASTEROCERAS        = reg("asteroceras");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> CAMEROCERAS        = reg("cameroceras");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> ENDOCERAS          = reg("endoceras");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> ORTHOCERAS         = reg("orthoceras");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> VECTIPELTA         = reg("vectipelta");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> TRICERATOPS        = reg("triceratops");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> PARACERATHERIUM    = reg("paraceratherium");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> KAIRUKU    = reg("kairuku");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> CALYMENE    = reg("calymene");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> NIGERSAURUS    = reg("nigersaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> THERIZINOSAURUS        = reg("therizinosaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> STEGOSAURUS        = reg("stegosaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> GIGANOTOSAURUS        = reg("giganotosaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> EDMONTOSAURUS          = reg("edmontosaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> LAMBEOSAURUS         = reg("lambeosaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> CORYTHOSAURUS         = reg("corythosaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> TROODON        = reg("troodon");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> DILOPHOSAURUS    = reg("dilophosaurus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> DUNKLEOSTEUS    = reg("dunkleosteus");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> LIVYATAN    = reg("livyatan");
    public static final DeferredHolder<PaintingVariant, PaintingVariant> MAJUNGASAURUS    = reg("majungasaurus");


    public static void register(IEventBus eventBus) {
        PAINTING_VARIANTS.register(eventBus);
    }
}
