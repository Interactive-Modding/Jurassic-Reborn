package net.vit.jurassicreborn.common.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.paleopad.AppHandler;
import net.vit.jurassicreborn.common.paleopad.FeederTrackerApp;
import net.vit.jurassicreborn.common.util.networking.PlayerData;

/**
 * Sync feeder tracker data from the server to the client.
 */
public record SyncFeederTrackerPacket(CompoundTag appData) implements CustomPacketPayload {
    public static final Type<SyncFeederTrackerPacket> TYPE = new Type<>(JurassicReborn.resource("sync_feeder_tracker"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncFeederTrackerPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public SyncFeederTrackerPacket decode(RegistryFriendlyByteBuf buf) {
            return new SyncFeederTrackerPacket(buf.readNbt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, SyncFeederTrackerPacket msg) {
            buf.writeNbt(msg.appData());
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncFeederTrackerPacket pkt, IPayloadContext ctx) {
        ctx.enqueueWork(() -> handleClient(pkt, ctx));
    }

    private static void handleClient(SyncFeederTrackerPacket pkt, IPayloadContext ctx) {
        Player player = ctx.player();
        if (player == null) {
            return;
        }

        CompoundTag data = pkt.appData() == null ? new CompoundTag() : pkt.appData().copy();
        FeederTrackerApp app = (FeederTrackerApp) AppHandler.INSTANCE.feederTracker;
        app.readAppFromNBT(data);

        PlayerData playerData = PlayerData.get(player);
        playerData.getAppdata().put(app.getName(), data.copy());
    }
}
