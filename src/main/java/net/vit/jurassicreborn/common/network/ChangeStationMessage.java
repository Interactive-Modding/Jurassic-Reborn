package net.vit.jurassicreborn.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.vehicle.VehicleEntity;

public record ChangeStationMessage(int entityId) implements CustomPacketPayload {
    public static final Type<ChangeStationMessage> TYPE = new Type<>(JurassicReborn.resource("change_station"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ChangeStationMessage> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public ChangeStationMessage decode(RegistryFriendlyByteBuf buf) {
            return new ChangeStationMessage(buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, ChangeStationMessage msg) {
            buf.writeInt(msg.entityId());
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ChangeStationMessage msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer player)) {
                return;
            }
            Entity entity = player.level().getEntity(msg.entityId());
            if (entity instanceof VehicleEntity car && player.getVehicle() == car) {
                car.cycleStation();
            }
        });
    }
}
