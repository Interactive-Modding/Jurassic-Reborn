package net.vit.jurassicreborn.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.vehicle.FordExplorerEntity;

public record FordExplorerChangeStateMessage(int entityId, boolean onRails) implements CustomPacketPayload {
    public static final Type<FordExplorerChangeStateMessage> TYPE = new Type<>(JurassicReborn.resource("ford_explorer_change_state"));
    public static final StreamCodec<RegistryFriendlyByteBuf, FordExplorerChangeStateMessage> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public FordExplorerChangeStateMessage decode(RegistryFriendlyByteBuf buf) {
            return new FordExplorerChangeStateMessage(buf.readInt(), buf.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, FordExplorerChangeStateMessage msg) {
            buf.writeInt(msg.entityId());
            buf.writeBoolean(msg.onRails());
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(FordExplorerChangeStateMessage msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Level level = ctx.player().level();
            if (level == null) {
                return;
            }
            Entity entity = level.getEntity(msg.entityId());
            if (entity instanceof FordExplorerEntity car) {
                car.setOnRails(msg.onRails());
            }
        });
    }
}
