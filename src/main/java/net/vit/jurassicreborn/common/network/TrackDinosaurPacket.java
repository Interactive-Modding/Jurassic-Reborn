package net.vit.jurassicreborn.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

import java.util.function.Supplier;

public class TrackDinosaurPacket {
    private final int dinosaurEntityId;

    public TrackDinosaurPacket(int dinosaurEntityId) {
        this.dinosaurEntityId = dinosaurEntityId;
    }

    // Decoder
    public TrackDinosaurPacket(FriendlyByteBuf buf) {
        this.dinosaurEntityId = buf.readInt();
    }

    // Encoder
    public static void toBytes(TrackDinosaurPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.dinosaurEntityId);
    }

    // Handler
    public static void handle(TrackDinosaurPacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) return;
            Level world = player.level();
            Entity e = world.getEntity(msg.dinosaurEntityId);
            if (e instanceof DinosaurEntity dino) {
                dino.addTracker(player.getUUID());
            }
        });
        ctx.setPacketHandled(true);
    }
}
