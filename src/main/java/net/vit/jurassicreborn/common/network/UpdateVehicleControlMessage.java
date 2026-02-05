package net.vit.jurassicreborn.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.vit.jurassicreborn.common.entities.vehicle.VehicleEntity;

import java.util.function.Supplier;

public class UpdateVehicleControlMessage {
    private final int entityId;
    private final byte state;

    public UpdateVehicleControlMessage(int entityId, byte state) {
        this.entityId = entityId;
        this.state = state;
    }

    public static void encode(UpdateVehicleControlMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeByte(msg.state);
    }

    public static UpdateVehicleControlMessage decode(FriendlyByteBuf buf) {
        return new UpdateVehicleControlMessage(buf.readInt(), buf.readByte());
    }

    public static void handle(UpdateVehicleControlMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            Entity e = player.level.getEntity(msg.entityId);
            if (e instanceof VehicleEntity car && car.getControllingPassenger() == player) {
                car.setControlState(msg.state);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
