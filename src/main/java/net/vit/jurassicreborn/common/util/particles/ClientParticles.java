package net.vit.jurassicreborn.common.util.particles;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.render.entity.vehicle.HelicopterEngineParticle;
import net.vit.jurassicreborn.client.render.entity.vehicle.HelicopterGroundParticle;
import net.vit.jurassicreborn.common.entities.vehicle.WashingParticle;

@EventBusSubscriber(modid = JurassicReborn.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientParticles {
    @SubscribeEvent
    public static void onRegisterParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.VENOM.get(), VenomParticle.Provider::new);
        event.registerSpriteSet(ModParticles.HELICOPTER_ENGINE.get(), HelicopterEngineParticle.Provider::new);
        event.registerSpriteSet(ModParticles.HELICOPTER_GROUND.get(), HelicopterGroundParticle.Provider::new);
        event.registerSpriteSet(ModParticles.WASHING_DROPLET.get(), WashingParticle.Provider::new);

    }
}
