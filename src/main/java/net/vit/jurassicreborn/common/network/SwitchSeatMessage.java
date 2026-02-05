package net.vit.jurassicreborn.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.vit.jurassicreborn.common.entities.vehicle.VehicleEntity;

import java.util.function.Supplier;

public class SwitchSeatMessage {
    private final int entityId;

    public SwitchSeatMessage(int entityId) {
        this.entityId = entityId;
    }

    public SwitchSeatMessage(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
    }

    public static void encode(SwitchSeatMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
    }

    public static SwitchSeatMessage decode(FriendlyByteBuf buf) {
        return new SwitchSeatMessage(buf);
    }

    public static void handle(SwitchSeatMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            Entity e = player.level.getEntity(msg.entityId);
            if (e instanceof VehicleEntity car && player.getVehicle() == car) {
                car.cycleSeat(player);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}