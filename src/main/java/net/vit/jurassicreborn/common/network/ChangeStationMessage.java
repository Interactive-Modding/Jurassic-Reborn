package net.vit.jurassicreborn.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.vit.jurassicreborn.common.entities.vehicle.VehicleEntity;

import java.util.function.Supplier;

public class ChangeStationMessage {
    private final int entityId;

    public ChangeStationMessage(int entityId) {
        this.entityId = entityId;
    }

    public ChangeStationMessage(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
    }

    public static void encode(ChangeStationMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
    }

    public static ChangeStationMessage decode(FriendlyByteBuf buf) {
        return new ChangeStationMessage(buf);
    }

    public static void handle(ChangeStationMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            Entity e = player.level.getEntity(msg.entityId);
            if (e instanceof VehicleEntity) {
                VehicleEntity car = (VehicleEntity) e;
                if (player.getVehicle() == car) {
                    car.cycleStation();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}