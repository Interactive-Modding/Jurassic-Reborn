package net.vit.jurassicreborn.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.vehicle.FordExplorerEntity;

import javax.annotation.Nullable;

public record FordExplorerUpdatePositionStateMessage(int entityId, long railPos) implements CustomPacketPayload {
    public static final Type<FordExplorerUpdatePositionStateMessage> TYPE = new Type<>(JurassicReborn.resource("ford_explorer_update_position_state"));
    public static final StreamCodec<RegistryFriendlyByteBuf, FordExplorerUpdatePositionStateMessage> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public FordExplorerUpdatePositionStateMessage decode(RegistryFriendlyByteBuf buf) {
            return new FordExplorerUpdatePositionStateMessage(buf.readInt(), buf.readLong());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, FordExplorerUpdatePositionStateMessage msg) {
            buf.writeInt(msg.entityId());
            buf.writeLong(msg.railPos());
        }
    };

    public FordExplorerUpdatePositionStateMessage(int entityId, @Nullable BlockPos pos) {
        this(entityId, (pos == null ? FordExplorerEntity.INACTIVE : pos).asLong());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(FordExplorerUpdatePositionStateMessage msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Level level = ctx.player().level();
            if (level == null) {
                return;
            }
            Entity entity = level.getEntity(msg.entityId());
            if (entity instanceof FordExplorerEntity car) {
                BlockPos newPos = BlockPos.of(msg.railPos());
                if (msg.railPos() == FordExplorerEntity.INACTIVE.asLong()) {
                    newPos = FordExplorerEntity.INACTIVE;
                }
                car.prevRailTracks = car.railTracks;
                car.railTracks = newPos;
            }
        });
    }
}
