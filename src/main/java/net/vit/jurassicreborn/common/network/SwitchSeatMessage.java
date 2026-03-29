package net.vit.jurassicreborn.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.vehicle.VehicleEntity;

public record SwitchSeatMessage(int entityId) implements CustomPacketPayload {
    public static final Type<SwitchSeatMessage> TYPE = new Type<>(JurassicReborn.resource("switch_seat"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SwitchSeatMessage> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public SwitchSeatMessage decode(RegistryFriendlyByteBuf buf) {
            return new SwitchSeatMessage(buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, SwitchSeatMessage msg) {
            buf.writeInt(msg.entityId());
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SwitchSeatMessage msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer player)) {
                return;
            }
            Entity entity = player.level().getEntity(msg.entityId());
            if (entity instanceof VehicleEntity car && player.getVehicle() == car) {
                car.cycleSeat(player);
            }
        });
    }
}
