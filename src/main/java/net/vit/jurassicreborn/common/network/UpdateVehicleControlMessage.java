package net.vit.jurassicreborn.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.vehicle.VehicleEntity;

public record UpdateVehicleControlMessage(int entityId, byte state) implements CustomPacketPayload {
    public static final Type<UpdateVehicleControlMessage> TYPE = new Type<>(JurassicReborn.resource("update_vehicle_control"));
    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateVehicleControlMessage> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public UpdateVehicleControlMessage decode(RegistryFriendlyByteBuf buf) {
            return new UpdateVehicleControlMessage(buf.readInt(), buf.readByte());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, UpdateVehicleControlMessage msg) {
            buf.writeInt(msg.entityId());
            buf.writeByte(msg.state());
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(UpdateVehicleControlMessage msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer player)) {
                return;
            }
            Entity entity = player.level().getEntity(msg.entityId());
            if (entity instanceof VehicleEntity car && car.getControllingPassenger() == player) {
                car.setControlState(msg.state());
            }
        });
    }
}
