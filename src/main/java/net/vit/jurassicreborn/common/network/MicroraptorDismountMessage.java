package net.vit.jurassicreborn.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MicroraptorEntity;
import java.util.function.Supplier;

/**
 * Message sent when the player dismounts a Microraptor from their shoulders.
 * The client sends it to the server, which then broadcasts it to tracking players
 * so their clients can update immediately.
 */
public class MicroraptorDismountMessage {
    private final int entityId;

    public MicroraptorDismountMessage(int entityId) {
        this.entityId = entityId;
    }

    public MicroraptorDismountMessage(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
    }

    public static void encode(MicroraptorDismountMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
    }

    public static MicroraptorDismountMessage decode(FriendlyByteBuf buf) {
        return new MicroraptorDismountMessage(buf);
    }

    public static void handle(MicroraptorDismountMessage msg, Supplier<NetworkEvent.Context> ctxSup) {
        NetworkEvent.Context ctx = ctxSup.get();
        ctx.enqueueWork(() -> {
            ServerPlayer serverPlayer = ctx.getSender();
            if (serverPlayer != null) {
                Entity entity = serverPlayer.level.getEntity(msg.entityId);
                if (entity instanceof MicroraptorEntity microraptor && microraptor.isOwner(serverPlayer)) {
                    microraptor.stopRiding();

                    // use the public sendToAllNear helper instead of channel
                    Network.sendToAllNear(serverPlayer.level, microraptor.blockPosition(), 64.0d, msg);
                }
            } else {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Client.handle(msg));
            }
        });
        ctx.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static class Client {
        static void handle(MicroraptorDismountMessage msg) {
            Level level = net.minecraft.client.Minecraft.getInstance().level;
            if (level == null) return;
            Entity entity = level.getEntity(msg.entityId);
            if (entity != null) {
                entity.stopRiding();
            }
        }
    }
}

