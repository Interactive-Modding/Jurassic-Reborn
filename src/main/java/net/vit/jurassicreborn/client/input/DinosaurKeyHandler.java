// src/main/java/net/vit/jurassicreborn/client/input/DinosaurKeyHandler.java
package net.vit.jurassicreborn.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

public class DinosaurKeyHandler {
    public static final KeyMapping MICRORAPTOR_DISMOUNT = new KeyMapping(
            "key.jurassicreborn.microraptor_dismount",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "key.categories.jurassicreborn"
    );

    public static void register(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(MICRORAPTOR_DISMOUNT));
    }
}
