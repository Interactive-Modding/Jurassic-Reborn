package net.vit.jurassicreborn.common.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.vit.jurassicreborn.common.entities.vehicle.FordExplorerSnowEntity;
import java.util.function.Supplier;

public class FordExplorerSnowChangeStateMessage {
    private final int entityId;
    private final boolean onRails;

    public FordExplorerSnowChangeStateMessage(int entityId, boolean onRails) {
        this.entityId = entityId;
        this.onRails = onRails;
    }

    public static void encode(FordExplorerSnowChangeStateMessage msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeBoolean(msg.onRails);
    }

    public static FordExplorerSnowChangeStateMessage decode(FriendlyByteBuf buf) {
        return new FordExplorerSnowChangeStateMessage(buf.readInt(), buf.readBoolean());
    }

    public static void handle(FordExplorerSnowChangeStateMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level level = ctx.get().getDirection().getReceptionSide().isClient()
                    ? DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> Client::getLevel)
                    : ctx.get().getSender().level;
            if (level == null) return;

            Entity e = level.getEntity(msg.entityId);
            if (e instanceof FordExplorerSnowEntity car) {
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
