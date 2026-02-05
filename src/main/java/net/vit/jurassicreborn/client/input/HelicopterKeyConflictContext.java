package net.vit.jurassicreborn.client.input;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.vit.jurassicreborn.common.entities.vehicle.HelicopterEntity;

/**
 * Key conflict context that is only active while the player is controlling a helicopter.
 */
public enum HelicopterKeyConflictContext implements IKeyConflictContext {
    INSTANCE;

    @Override
    public boolean isActive() {
        Player player = Minecraft.getInstance().player;
        return player != null && player.getVehicle() instanceof HelicopterEntity;
    }

    @Override
    public boolean conflicts(IKeyConflictContext other) {
        // Only conflict with our own context so vanilla "in game" bindings such as the
        // default right-click/"use" action (and other mods' keymaps that rely on it)
        // remain active while our helicopter keys are idle.  Previously we reported a
        // conflict with KeyConflictContext.IN_GAME which effectively shadowed any
        // action bound to the same key, preventing players from aiming or using items
        // while Jurassic Reborn was installed.
        return other == this;
    }
}