package net.vit.jurassicreborn.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.vit.jurassicreborn.common.entities.vehicle.FordExplorerSnowEntity;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/** Server → Client: tell the client which rail-block the Explorer is now on. */
public class FordExplorerSnowUpdatePositionStateMessage {

    private final int   entityId;
    private final long  railPos;   // BlockPos serialised as long (-1 == INACTIVE)

    /* ------------------------------------------------------------------ */
    /*  Constructors                                                      */
    /* ------------------------------------------------------------------ */

    /** Decoder-side ctor */
    public FordExplorerSnowUpdatePositionStateMessage(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.railPos  = buf.readLong();
    }

    /** Server-side send helper                          (may pass null) */
    public FordExplorerSnowUpdatePositionStateMessage(int entityId, @Nullable BlockPos pos) {
        this.entityId = entityId;
        this.railPos  = (pos == null ? FordExplorerSnowEntity.INACTIVE : pos).asLong();
    }

    /* ------------------------------------------------------------------ */
    /*  Encoder                                                           */
    /* ------------------------------------------------------------------ */
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeLong(railPos);
    }

    /* ------------------------------------------------------------------ */
    /*  Handler (runs on client thread)                                   */
    /* ------------------------------------------------------------------ */
    public void handle(Supplier<NetworkEvent.Context> ctxSup) {
        NetworkEvent.Context ctx = ctxSup.get();
        ctx.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Client.handle(this))
        );
        ctx.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static class Client {
        static void handle(FordExplorerSnowUpdatePositionStateMessage msg) {
            Level level = net.minecraft.client.Minecraft.getInstance().level;
            if (level == null) return;

            Entity e = level.getEntity(msg.entityId);
            if (e instanceof FordExplorerSnowEntity car) {
                BlockPos newPos = BlockPos.of(msg.railPos);
                if (msg.railPos == FordExplorerSnowEntity.INACTIVE.asLong()) {
                    newPos = FordExplorerSnowEntity.INACTIVE;
                }
                car.prevRailTracks = car.railTracks;
                car.railTracks     = newPos;
            }
        }
    }
}
