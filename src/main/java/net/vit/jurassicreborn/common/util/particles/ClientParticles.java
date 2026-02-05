package net.vit.jurassicreborn.common.util.particles;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.render.entity.vehicle.HelicopterEngineParticle;
import net.vit.jurassicreborn.client.render.entity.vehicle.HelicopterGroundParticle;
import net.vit.jurassicreborn.common.entities.vehicle.WashingParticle;
import net.vit.jurassicreborn.common.util.particles.VenomParticle;

@Mod.EventBusSubscriber(modid = JurassicReborn.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientParticles {
    @SubscribeEvent
    public static void onRegisterParticles(ParticleFactoryRegisterEvent event) {
        var engine = Minecraft.getInstance().particleEngine;
        engine.register(ModParticles.VENOM.get(), VenomParticle.Provider::new);
        engine.register(ModParticles.HELICOPTER_ENGINE.get(), HelicopterEngineParticle.Provider::new);
        engine.register(ModParticles.HELICOPTER_GROUND.get(), HelicopterGroundParticle.Provider::new);
        engine.register(ModParticles.WASHING_DROPLET.get(), WashingParticle.Provider::new);

    }
}
