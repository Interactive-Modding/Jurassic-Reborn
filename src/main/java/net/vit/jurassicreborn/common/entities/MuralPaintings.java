package net.vit.jurassicreborn.common.entities;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vit.jurassicreborn.JurassicReborn;

public class MuralPaintings {
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS =
            DeferredRegister.create(Registries.PAINTING_VARIANT, JurassicReborn.MODID);

    public static final DeferredHolder<PaintingVariant, PaintingVariant> BUNKER =
            PAINTING_VARIANTS.register("bunker", () -> new PaintingVariant(128, 64, ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "bunker")));
    public static final DeferredHolder<PaintingVariant, PaintingVariant> RIVER =
            PAINTING_VARIANTS.register("river", () -> new PaintingVariant(64, 48, ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "river")));
    public static final DeferredHolder<PaintingVariant, PaintingVariant> MAMENCHI =
            PAINTING_VARIANTS.register("mamenchi", () -> new PaintingVariant(64, 64, ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "mamenchi")));
    public static final DeferredHolder<PaintingVariant, PaintingVariant> LAB =
            PAINTING_VARIANTS.register("lab", () -> new PaintingVariant(16, 32, ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "lab")));
    public static final DeferredHolder<PaintingVariant, PaintingVariant> JR =
            PAINTING_VARIANTS.register("jr", () -> new PaintingVariant(16, 16, ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "jr")));
    public static final DeferredHolder<PaintingVariant, PaintingVariant> AMBER =
            PAINTING_VARIANTS.register("amber", () -> new PaintingVariant(16, 16, ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "amber")));
    public static final DeferredHolder<PaintingVariant, PaintingVariant> EGG =
            PAINTING_VARIANTS.register("egg", () -> new PaintingVariant(16, 32, ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "egg")));
    public static final DeferredHolder<PaintingVariant, PaintingVariant> CREATION_LAB =
            PAINTING_VARIANTS.register("creation_lab", () -> new PaintingVariant(64, 32, ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "creation_lab")));
    public static final DeferredHolder<PaintingVariant, PaintingVariant> FOSSILS =
            PAINTING_VARIANTS.register("fossils", () -> new PaintingVariant(128, 64, ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "fossils")));
    public static final DeferredHolder<PaintingVariant, PaintingVariant> HUNT_LEFT =
            PAINTING_VARIANTS.register("hunt_left", () -> new PaintingVariant(64, 64, ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "hunt_left")));
    public static final DeferredHolder<PaintingVariant, PaintingVariant> HUNT_MIDDLE =
            PAINTING_VARIANTS.register("hunt_middle", () -> new PaintingVariant(64, 64, ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "hunt_middle")));
    public static final DeferredHolder<PaintingVariant, PaintingVariant> HUNT_RIGHT =
            PAINTING_VARIANTS.register("hunt_right", () -> new PaintingVariant(64, 64, ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "hunt_right")));
    public static final DeferredHolder<PaintingVariant, PaintingVariant> MOSASAURUS_1 =
            PAINTING_VARIANTS.register("mosasaurus_1", () -> new PaintingVariant(64, 32, ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "mosasaurus_1")));
    public static final DeferredHolder<PaintingVariant, PaintingVariant> PARK_ENTRANCE =
            PAINTING_VARIANTS.register("park_entrance", () -> new PaintingVariant(64, 32, ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "park_entrance")));
    public static final DeferredHolder<PaintingVariant, PaintingVariant> RIDING =
            PAINTING_VARIANTS.register("riding", () -> new PaintingVariant(64, 32, ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "riding")));
    public static final DeferredHolder<PaintingVariant, PaintingVariant> SKETCH =
            PAINTING_VARIANTS.register("sketch", () -> new PaintingVariant(64, 32, ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "sketch")));
    public static final DeferredHolder<PaintingVariant, PaintingVariant> TRICERATOPS_1 =
            PAINTING_VARIANTS.register("triceratops_1", () -> new PaintingVariant(128, 32, ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "triceratops_1")));
    public static final DeferredHolder<PaintingVariant, PaintingVariant> VALLEY =
            PAINTING_VARIANTS.register("valley", () -> new PaintingVariant(64, 32, ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "valley")));
    public static final DeferredHolder<PaintingVariant, PaintingVariant> JOHN_HAMMOND =
            PAINTING_VARIANTS.register("john_hammond", () -> new PaintingVariant(32, 32, ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "john_hammond")));

    public static void register(IEventBus eventBus) {
        PAINTING_VARIANTS.register(eventBus);
    }
}
