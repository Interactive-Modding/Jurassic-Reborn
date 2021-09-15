package mod.reborn.server.event;

import mod.reborn.RebornMod;
import mod.reborn.server.networking.RebornNetworking;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class RebornModEvents {

    public static RebornModEvents Register(IEventBus eventBus) {
        return new RebornModEvents(eventBus);
    }

    public void Log() {
        RebornMod.LOGGER.info("Successfully triggered the registry of the Reborn Mod Events.");
    }

    private RebornModEvents(IEventBus eventBus) {
        eventBus.addListener(this::onCommonSetup);
    }

    private void onCommonSetup(final FMLCommonSetupEvent event) {
        RebornNetworking.registerMessages();
    }
}
