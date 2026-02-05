package net.vit.jurassicreborn.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

import java.util.function.Supplier;

public class SetOrderPacket {
    private final int entityId;
    private final int orderOrdinal;

    public SetOrderPacket(int entityId, int orderOrdinal) {
        this.entityId = entityId;
        this.orderOrdinal = orderOrdinal;
    }

    public SetOrderPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.orderOrdinal = buf.readInt();
    }

    public static void toBytes(SetOrderPacket pkt, FriendlyByteBuf buf) {
        buf.writeInt(pkt.entityId);
        buf.writeInt(pkt.orderOrdinal);
    }

    public static void handle(SetOrderPacket pkt, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;
            Level world = player.level;
            Entity e = world.getEntity(pkt.entityId);
            if (e instanceof DinosaurEntity dinosaur) {
                DinosaurEntity.Order[] orders = DinosaurEntity.Order.values();
                if (pkt.orderOrdinal >= 0 && pkt.orderOrdinal < orders.length) {
                    dinosaur.setFieldOrder(orders[pkt.orderOrdinal]);
                }
            }
        });
        context.setPacketHandled(true);
    }
}