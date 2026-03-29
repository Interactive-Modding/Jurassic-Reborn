package net.vit.jurassicreborn.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.vehicle.FordExplorerSnowEntity;

public record FordExplorerSnowChangeStateMessage(int entityId, boolean onRails) implements CustomPacketPayload {
    public static final Type<FordExplorerSnowChangeStateMessage> TYPE = new Type<>(JurassicReborn.resource("ford_explorer_snow_change_state"));
    public static final StreamCodec<RegistryFriendlyByteBuf, FordExplorerSnowChangeStateMessage> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public FordExplorerSnowChangeStateMessage decode(RegistryFriendlyByteBuf buf) {
            return new FordExplorerSnowChangeStateMessage(buf.readInt(), buf.readBoolean());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, FordExplorerSnowChangeStateMessage msg) {
            buf.writeInt(msg.entityId());
            buf.writeBoolean(msg.onRails());
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(FordExplorerSnowChangeStateMessage msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Level level = ctx.player().level();
            if (level == null) {
                return;
            }
            Entity entity = level.getEntity(msg.entityId());
            if (entity instanceof FordExplorerSnowEntity car) {
                car.setOnRails(msg.onRails());
            }
        });
    }
}
