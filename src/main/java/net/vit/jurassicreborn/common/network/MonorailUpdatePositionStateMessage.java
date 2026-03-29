package net.vit.jurassicreborn.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.vehicle.MonorailEntity;

import javax.annotation.Nullable;

public record MonorailUpdatePositionStateMessage(int entityId, long railPos) implements CustomPacketPayload {
    public static final Type<MonorailUpdatePositionStateMessage> TYPE = new Type<>(JurassicReborn.resource("monorail_update_position_state"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MonorailUpdatePositionStateMessage> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public MonorailUpdatePositionStateMessage decode(RegistryFriendlyByteBuf buf) {
            return new MonorailUpdatePositionStateMessage(buf.readInt(), buf.readLong());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, MonorailUpdatePositionStateMessage msg) {
            buf.writeInt(msg.entityId());
            buf.writeLong(msg.railPos());
        }
    };

    public MonorailUpdatePositionStateMessage(int entityId, @Nullable BlockPos pos) {
        this(entityId, (pos == null ? MonorailEntity.INACTIVE : pos).asLong());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(MonorailUpdatePositionStateMessage msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Level level = ctx.player().level();
            if (level == null) {
                return;
            }
            Entity entity = level.getEntity(msg.entityId());
            if (entity instanceof MonorailEntity car) {
                BlockPos newPos = BlockPos.of(msg.railPos());
                if (msg.railPos() == MonorailEntity.INACTIVE.asLong()) {
                    newPos = MonorailEntity.INACTIVE;
                }
                car.prevRailTracks = car.railTracks;
                car.railTracks = newPos;
            }
        });
    }
}
