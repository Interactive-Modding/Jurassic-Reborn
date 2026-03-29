package net.vit.jurassicreborn.client.screens.paleopad;

import net.vit.jurassicreborn.common.paleopad.App;
import net.vit.jurassicreborn.common.paleopad.AppHandler;

import java.util.HashMap;
import java.util.Map;

public class GuiAppRegistry {
    private static final Map<App, GuiApp> registeredApps = new HashMap<>();

    public static void registerApp(GuiApp gui) {
        registeredApps.put(gui.app, gui);
    }

    public static void register() {
        // Register all your app GUIs here!
        registerApp(new FlappyDinoGuiApp(AppHandler.INSTANCE.flappy_dino));
        registerApp(new MinimapGuiApp(AppHandler.INSTANCE.minimap));
        registerApp(new FeederTrackerGuiApp(AppHandler.INSTANCE.feederTracker));
    }

    public static GuiApp getGui(App app) {
        return registeredApps.get(app);
    }
}
