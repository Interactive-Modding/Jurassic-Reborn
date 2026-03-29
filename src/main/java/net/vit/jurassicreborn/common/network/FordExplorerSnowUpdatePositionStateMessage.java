package net.vit.jurassicreborn.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.vehicle.FordExplorerSnowEntity;

import javax.annotation.Nullable;

public record FordExplorerSnowUpdatePositionStateMessage(int entityId, long railPos) implements CustomPacketPayload {
    public static final Type<FordExplorerSnowUpdatePositionStateMessage> TYPE = new Type<>(JurassicReborn.resource("ford_explorer_snow_update_position_state"));
    public static final StreamCodec<RegistryFriendlyByteBuf, FordExplorerSnowUpdatePositionStateMessage> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public FordExplorerSnowUpdatePositionStateMessage decode(RegistryFriendlyByteBuf buf) {
            return new FordExplorerSnowUpdatePositionStateMessage(buf.readInt(), buf.readLong());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, FordExplorerSnowUpdatePositionStateMessage msg) {
            buf.writeInt(msg.entityId());
            buf.writeLong(msg.railPos());
        }
    };

    public FordExplorerSnowUpdatePositionStateMessage(int entityId, @Nullable BlockPos pos) {
        this(entityId, (pos == null ? FordExplorerSnowEntity.INACTIVE : pos).asLong());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(FordExplorerSnowUpdatePositionStateMessage msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Level level = ctx.player().level();
            if (level == null) {
                return;
            }
            Entity entity = level.getEntity(msg.entityId());
            if (entity instanceof FordExplorerSnowEntity car) {
                BlockPos newPos = BlockPos.of(msg.railPos());
                if (msg.railPos() == FordExplorerSnowEntity.INACTIVE.asLong()) {
                    newPos = FordExplorerSnowEntity.INACTIVE;
                }
                car.prevRailTracks = car.railTracks;
                car.railTracks = newPos;
            }
        });
    }
}
