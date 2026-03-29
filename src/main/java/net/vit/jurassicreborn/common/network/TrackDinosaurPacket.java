package net.vit.jurassicreborn.common.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

public record TrackDinosaurPacket(int dinosaurEntityId) implements CustomPacketPayload {
    public static final Type<TrackDinosaurPacket> TYPE = new Type<>(JurassicReborn.resource("track_dinosaur"));
    public static final StreamCodec<RegistryFriendlyByteBuf, TrackDinosaurPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public TrackDinosaurPacket decode(RegistryFriendlyByteBuf buf) {
            return new TrackDinosaurPacket(buf.readInt());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, TrackDinosaurPacket msg) {
            buf.writeInt(msg.dinosaurEntityId());
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(TrackDinosaurPacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer player)) {
                return;
            }
            Level world = player.level();
            Entity entity = world.getEntity(msg.dinosaurEntityId());
            if (entity instanceof DinosaurEntity dino) {
                dino.addTracker(player.getUUID());
            }
        });
    }
}
