package net.vit.jurassicreborn.common.paleopad;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.vit.jurassicreborn.common.network.Network;
import net.vit.jurassicreborn.common.network.SyncFeederTrackerPacket;
import net.vit.jurassicreborn.common.util.networking.PlayerData;


/**
 * Utility methods for server-side feeder tracker interactions.
 */
public final class FeederTrackerServerHelper {
    private FeederTrackerServerHelper() {
    }

    /**
     * Send the supplied feeder tracker data to the player.
     */
    public static void sync(Player player, CompoundTag data) {
        if (player instanceof ServerPlayer serverPlayer) {
            CompoundTag payload = data == null ? createEmptyTag() : data.copy();
            Network.sendTo(serverPlayer, new SyncFeederTrackerPacket(payload));
        }
    }

    /**
     * Sync the feeder tracker app using the player's stored app data.
     */
    public static void syncFromPlayerData(ServerPlayer player) {
        PlayerData playerData = PlayerData.get(player);
        FeederTrackerApp app = (FeederTrackerApp) AppHandler.INSTANCE.feederTracker;
        CompoundTag stored = playerData.getAppdata().get(app.getName());
        CompoundTag payload = stored != null ? stored.copy() : createEmptyTag();
        Network.sendTo(player, new SyncFeederTrackerPacket(payload));
    }

    private static CompoundTag createEmptyTag() {
        CompoundTag tag = new CompoundTag();
        tag.put("Feeders", new ListTag());
        tag.putBoolean("PreviouslyOpened", false);
        return tag;
    }
}
