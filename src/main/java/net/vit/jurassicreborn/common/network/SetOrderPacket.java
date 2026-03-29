package net.vit.jurassicreborn.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

public record SetOrderPacket(int entityId, int orderOrdinal) implements CustomPacketPayload {
    public static final Type<SetOrderPacket> TYPE = new Type<>(JurassicReborn.resource("set_order"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SetOrderPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public SetOrderPacket decode(RegistryFriendlyByteBuf buf) {
            return new SetOrderPacket(buf.readInt(), buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, SetOrderPacket msg) {
            buf.writeInt(msg.entityId());
            buf.writeInt(msg.orderOrdinal());
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SetOrderPacket pkt, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer player)) {
                return;
            }
            Level world = player.level();
            Entity e = world.getEntity(pkt.entityId());
            if (e instanceof DinosaurEntity dinosaur) {
                DinosaurEntity.Order[] orders = DinosaurEntity.Order.values();
                if (pkt.orderOrdinal() >= 0 && pkt.orderOrdinal() < orders.length) {
                    dinosaur.setFieldOrder(orders[pkt.orderOrdinal()]);
                }
            }
        });
    }
}
