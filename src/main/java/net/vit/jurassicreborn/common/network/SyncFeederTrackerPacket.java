package net.vit.jurassicreborn.common.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.vit.jurassicreborn.common.paleopad.AppHandler;
import net.vit.jurassicreborn.common.paleopad.FeederTrackerApp;
import net.vit.jurassicreborn.common.util.networking.PlayerData;

import java.util.function.Supplier;

/**
 * Sync feeder tracker data from the server to the client.
 */
public class SyncFeederTrackerPacket {
    private final CompoundTag appData;

    public SyncFeederTrackerPacket(CompoundTag appData) {
        this.appData = appData;
    }

    public static void encode(SyncFeederTrackerPacket pkt, FriendlyByteBuf buf) {
        buf.writeNbt(pkt.appData);
    }

    public static SyncFeederTrackerPacket decode(FriendlyByteBuf buf) {
        return new SyncFeederTrackerPacket(buf.readNbt());
    }

    public static void handle(SyncFeederTrackerPacket pkt, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            if (ctx.getDirection().getReceptionSide().isClient()) {
                handleClient(pkt);
            }
        });
        ctx.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(SyncFeederTrackerPacket pkt) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) {
            return;
        }

        CompoundTag data = pkt.appData == null ? new CompoundTag() : pkt.appData.copy();
        FeederTrackerApp app = (FeederTrackerApp) AppHandler.INSTANCE.feederTracker;
        app.readAppFromNBT(data);

        PlayerData playerData = PlayerData.get(player);
        playerData.getAppdata().put(app.getName(), data.copy());
    }
}
