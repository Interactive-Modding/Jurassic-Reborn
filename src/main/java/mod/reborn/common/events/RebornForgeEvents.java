package mod.reborn.common.events;

import mod.reborn.RebornMod;
import mod.reborn.common.commands.SpawnStructureCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class RebornForgeEvents {

    public static RebornForgeEvents Register(IEventBus eventBus) {
        return new RebornForgeEvents(eventBus);
    }

    public void Log() {
        RebornMod.LOGGER.info("Successfully triggered the registry of the Reborn Forge Events.");
    }

    private RebornForgeEvents(IEventBus eventBus) {
        eventBus.addListener(this::onRegisterCommands);
    }

    private void onRegisterCommands(final RegisterCommandsEvent event) {
        SpawnStructureCommand.register(event.getDispatcher());
        //event.registerServerCommand(new ForceAnimationCommand());
    }
}
