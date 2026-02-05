package net.vit.jurassicreborn.common.entities;


import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vit.jurassicreborn.JurassicReborn;

public class MuralPaintings {
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS =
            DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, JurassicReborn.MODID);

    public static final RegistryObject<PaintingVariant> BUNKER   = PAINTING_VARIANTS.register("bunker",   () -> new PaintingVariant(128,  64));
    public static final RegistryObject<PaintingVariant> RIVER   = PAINTING_VARIANTS.register("river",   () -> new PaintingVariant(64,  48));
    public static final RegistryObject<PaintingVariant> MAMENCHI   = PAINTING_VARIANTS.register("mamenchi",   () -> new PaintingVariant(64,  64));
    public static final RegistryObject<PaintingVariant> LAB   = PAINTING_VARIANTS.register("lab",   () -> new PaintingVariant(16,  32));
    public static final RegistryObject<PaintingVariant> JR   = PAINTING_VARIANTS.register("jr",   () -> new PaintingVariant(16,  16));
    public static final RegistryObject<PaintingVariant> AMBER   = PAINTING_VARIANTS.register("amber",   () -> new PaintingVariant(16,  16));
    public static final RegistryObject<PaintingVariant> EGG   = PAINTING_VARIANTS.register("egg",   () -> new PaintingVariant(16,  32));
    public static final RegistryObject<PaintingVariant> CREATION_LAB   = PAINTING_VARIANTS.register("creation_lab",   () -> new PaintingVariant(64,  32));
    public static final RegistryObject<PaintingVariant> FOSSILS        = PAINTING_VARIANTS.register("fossils",        () -> new PaintingVariant(128, 64));
    public static final RegistryObject<PaintingVariant> HUNT_LEFT      = PAINTING_VARIANTS.register("hunt_left",      () -> new PaintingVariant(64,  64));
    public static final RegistryObject<PaintingVariant> HUNT_MIDDLE    = PAINTING_VARIANTS.register("hunt_middle",    () -> new PaintingVariant(64,  64));
    public static final RegistryObject<PaintingVariant> HUNT_RIGHT     = PAINTING_VARIANTS.register("hunt_right",     () -> new PaintingVariant(64,  64));
    public static final RegistryObject<PaintingVariant> MOSASAURUS_1     = PAINTING_VARIANTS.register("mosasaurus_1",     () -> new PaintingVariant(64,  32));
    public static final RegistryObject<PaintingVariant> PARK_ENTRANCE  = PAINTING_VARIANTS.register("park_entrance",  () -> new PaintingVariant(64,  32));
    public static final RegistryObject<PaintingVariant> RIDING         = PAINTING_VARIANTS.register("riding",         () -> new PaintingVariant(64,  32));
    public static final RegistryObject<PaintingVariant> SKETCH         = PAINTING_VARIANTS.register("sketch",         () -> new PaintingVariant(64,  32));
    public static final RegistryObject<PaintingVariant> TRICERATOPS_1    = PAINTING_VARIANTS.register("triceratops_1",    () -> new PaintingVariant(128, 32));
    public static final RegistryObject<PaintingVariant> VALLEY         = PAINTING_VARIANTS.register("valley",         () -> new PaintingVariant(64,  32));
    public static final RegistryObject<PaintingVariant> JOHN_HAMMOND   = PAINTING_VARIANTS.register("john_hammond",   () -> new PaintingVariant(32,  32));

    public static void register(IEventBus eventBus) {
        PAINTING_VARIANTS.register(eventBus);
    }
}
