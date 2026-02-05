package net.vit.jurassicreborn.common.util.particles;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vit.jurassicreborn.JurassicReborn;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, JurassicReborn.MODID);

    public static final RegistryObject<SimpleParticleType> HELICOPTER_ENGINE =
            PARTICLES.register("helicopter_engine", () -> new SimpleParticleType(false));   // false = not alwaysVisible
    public static final RegistryObject<SimpleParticleType> HELICOPTER_GROUND =
            PARTICLES.register("helicopter_ground", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> WASHING_DROPLET =
            PARTICLES.register("washing_droplet", () -> new SimpleParticleType(false));
    public static final RegistryObject<SimpleParticleType> VENOM =
            PARTICLES.register("venom", () -> new SimpleParticleType(true)); // true = alwaysShow

    public static void init(IEventBus bus) {
        PARTICLES.register(bus);
    }
}
