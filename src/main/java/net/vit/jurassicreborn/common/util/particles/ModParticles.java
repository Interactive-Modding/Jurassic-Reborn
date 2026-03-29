package net.vit.jurassicreborn.common.util.particles;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vit.jurassicreborn.JurassicReborn;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(Registries.PARTICLE_TYPE, JurassicReborn.MODID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HELICOPTER_ENGINE =
            PARTICLES.register("helicopter_engine", () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> HELICOPTER_GROUND =
            PARTICLES.register("helicopter_ground", () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> WASHING_DROPLET =
            PARTICLES.register("washing_droplet", () -> new SimpleParticleType(false));

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> VENOM =
            PARTICLES.register("venom", () -> new SimpleParticleType(true));

    public static void init(IEventBus bus) {
        PARTICLES.register(bus);
    }
}
