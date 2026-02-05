package net.vit.jurassicreborn.common.util.particles;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.render.entity.vehicle.HelicopterEngineParticle;
import net.vit.jurassicreborn.client.render.entity.vehicle.HelicopterGroundParticle;
import net.vit.jurassicreborn.common.entities.vehicle.WashingParticle;

@Mod.EventBusSubscriber(modid = JurassicReborn.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientParticles {
    @SubscribeEvent
    public static void onRegisterParticles(RegisterParticleProvidersEvent event) {
        event.register(ModParticles.VENOM.get(), VenomParticle.Provider::new);
        event.register(ModParticles.HELICOPTER_ENGINE.get(), HelicopterEngineParticle.Provider::new);
        event.register(ModParticles.HELICOPTER_GROUND.get(), HelicopterGroundParticle.Provider::new);
        event.register(ModParticles.WASHING_DROPLET.get(), WashingParticle.Provider::new);

    }
}
