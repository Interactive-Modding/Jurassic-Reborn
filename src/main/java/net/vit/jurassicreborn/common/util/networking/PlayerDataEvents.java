package net.vit.jurassicreborn.common.util.networking;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.vit.jurassicreborn.common.paleopad.FeederTrackerServerHelper;

@EventBusSubscriber(modid = "jurassicreborn")
public class PlayerDataEvents {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerData.get(event.getEntity());
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            FeederTrackerServerHelper.syncFromPlayerData(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        PlayerData.save(player);
        PlayerData.remove(player);
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        PlayerData.copy(event.getOriginal(), event.getEntity());
    }
}
