package net.vit.jurassicreborn.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.vit.jurassicreborn.common.entities.vehicle.FordExplorerEntity;
import java.util.function.Supplier;

public class FordExplorerChangeStateMessage {
    private final int entityId;
    private final boolean onRails;

    public FordExplorerChangeStateMessage(int entityId, boolean onRails) {
        this.entityId = entityId;
        this.onRails = onRails;
    }

    public static void encode(FordExplorerChangeStateMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeBoolean(msg.onRails);
    }

    public static FordExplorerChangeStateMessage decode(FriendlyByteBuf buf) {
        return new FordExplorerChangeStateMessage(buf.readInt(), buf.readBoolean());
    }

    public static void handle(FordExplorerChangeStateMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level level = ctx.get().getDirection().getReceptionSide().isClient()
                    ? DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> Client::getLevel)
                    : ctx.get().getSender().level();
            if (level == null) return;

            Entity e = level.getEntity(msg.entityId);
            if (e instanceof FordExplorerEntity car) {
                car.setOnRails(msg.onRails); // add a setter or keep the field public
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static class Client {
        static Level getLevel() {
            return net.minecraft.client.Minecraft.getInstance().level;
        }
    }
}
