package net.vit.jurassicreborn.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.vehicle.MonorailEntity;

public record MonorailChangeStateMessage(int entityId, boolean onRails) implements CustomPacketPayload {
    public static final Type<MonorailChangeStateMessage> TYPE = new Type<>(JurassicReborn.resource("monorail_change_state"));
    public static final StreamCodec<RegistryFriendlyByteBuf, MonorailChangeStateMessage> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public MonorailChangeStateMessage decode(RegistryFriendlyByteBuf buf) {
            return new MonorailChangeStateMessage(buf.readInt(), buf.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, MonorailChangeStateMessage msg) {
            buf.writeInt(msg.entityId());
            buf.writeBoolean(msg.onRails());
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(MonorailChangeStateMessage msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Level level = ctx.player().level();
            if (level == null) {
                return;
            }
            Entity entity = level.getEntity(msg.entityId());
            if (entity instanceof MonorailEntity car) {
                car.setOnRails(msg.onRails());
            }
        });
    }
}
