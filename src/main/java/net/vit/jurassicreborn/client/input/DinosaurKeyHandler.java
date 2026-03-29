// src/main/java/net/vit/jurassicreborn/client/input/DinosaurKeyHandler.java
package net.vit.jurassicreborn.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class DinosaurKeyHandler {
    public static final KeyMapping MICRORAPTOR_DISMOUNT = new KeyMapping(
            "key.jurassicreborn.microraptor_dismount",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "key.categories.jurassicreborn"
    );

    public static void register(RegisterKeyMappingsEvent event) {
        event.register(MICRORAPTOR_DISMOUNT);
    }
}
