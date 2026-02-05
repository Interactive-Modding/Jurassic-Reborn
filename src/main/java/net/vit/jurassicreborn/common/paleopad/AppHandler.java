package net.vit.jurassicreborn.common.paleopad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum AppHandler {
    INSTANCE;

    private final List<App> registeredApps = new ArrayList<>();
    public App flappy_dino;
    public App minimap;
    public App feederTracker;

    public void registerApp(App app) {
        registeredApps.add(app);
    }

    public void init() {
        flappy_dino = new FlappyDinoApp();
        minimap = new MinimapApp();
        feederTracker = new FeederTrackerApp();

        registerApp(flappy_dino);
        registerApp(minimap);
        registerApp(feederTracker);
    }

    public List<App> getApps() {
        // return an unmodifiable list for safety
        return Collections.unmodifiableList(registeredApps);
    }
}
