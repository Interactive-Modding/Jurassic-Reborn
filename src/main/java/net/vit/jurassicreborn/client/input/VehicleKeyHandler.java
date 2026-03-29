// src/main/java/net/vit/jurassicreborn/client/input/VehicleKeyHandler.java
package net.vit.jurassicreborn.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class VehicleKeyHandler {
    public static final KeyMapping SWITCH_SEAT = new KeyMapping(
            "key.jurassicreborn.switch_seat",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "key.categories.jurassicreborn"
    );

    public static final KeyMapping NEXT_STATION = new KeyMapping(
            "key.jurassicreborn.next_station",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_M,
            "key.categories.jurassicreborn"
    );

    public static final KeyMapping HELICOPTER_UP = new KeyMapping(
            "key.jurassicreborn.helicopter_up",
            HelicopterKeyConflictContext.INSTANCE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_SPACE,
            "key.categories.jurassicreborn"
    );

    public static final KeyMapping HELICOPTER_DOWN = new KeyMapping(
            "key.jurassicreborn.helicopter_down",
            HelicopterKeyConflictContext.INSTANCE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_CONTROL,
            "key.categories.jurassicreborn"
    );

    public static final KeyMapping HELICOPTER_AUTOPILOT = new KeyMapping(
            "key.jurassicreborn.helicopter_autopilot",
            HelicopterKeyConflictContext.INSTANCE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_P,
            "key.categories.jurassicreborn"
    );

    public static final KeyMapping HELICOPTER_LOCK = new KeyMapping(
            "key.jurassicreborn.helicopter_lock",
            HelicopterKeyConflictContext.INSTANCE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_L,
            "key.categories.jurassicreborn"
    );

    public static final KeyMapping HELICOPTER_ROTATE_LEFT = new KeyMapping(
            "key.jurassicreborn.helicopter_rotate_left",
            HelicopterKeyConflictContext.INSTANCE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Q,
            "key.categories.jurassicreborn"
    );

    public static final KeyMapping HELICOPTER_ROTATE_RIGHT = new KeyMapping(
            "key.jurassicreborn.helicopter_rotate_right",
            HelicopterKeyConflictContext.INSTANCE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_E,
            "key.categories.jurassicreborn"
    );

    public static final KeyMapping HELICOPTER_THIRD_PERSON_VIEW_ZOOM_OUT = new KeyMapping(
            "key.jurassicreborn.helicopter_zoom_out",
            HelicopterKeyConflictContext.INSTANCE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_Z,
            "key.categories.jurassicreborn"
    );

    public static final KeyMapping HELICOPTER_THIRD_PERSON_VIEW_ZOOM_IN = new KeyMapping(
            "key.jurassicreborn.helicopter_zoom_in",
            HelicopterKeyConflictContext.INSTANCE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            "key.categories.jurassicreborn"
    );

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(SWITCH_SEAT);
        event.register(NEXT_STATION);
        event.register(HELICOPTER_UP);
        event.register(HELICOPTER_DOWN);
        event.register(HELICOPTER_AUTOPILOT);
        event.register(HELICOPTER_LOCK);
        event.register(HELICOPTER_ROTATE_LEFT);
        event.register(HELICOPTER_ROTATE_RIGHT);
        event.register(HELICOPTER_THIRD_PERSON_VIEW_ZOOM_OUT);
        event.register(HELICOPTER_THIRD_PERSON_VIEW_ZOOM_IN);
    }
}