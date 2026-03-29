package net.vit.jurassicreborn.common.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.entities.feeder.FeederBlockEntity;
import net.vit.jurassicreborn.common.paleopad.FeederTrackerApp;

public record NameFeederPacket(BlockPos pos, String name) implements CustomPacketPayload {
    public static final Type<NameFeederPacket> TYPE = new Type<>(JurassicReborn.resource("name_feeder"));
    public static final StreamCodec<RegistryFriendlyByteBuf, NameFeederPacket> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public NameFeederPacket decode(RegistryFriendlyByteBuf buf) {
            return new NameFeederPacket(buf.readBlockPos(), buf.readUtf());
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buf, NameFeederPacket msg) {
            buf.writeBlockPos(msg.pos());
            buf.writeUtf(msg.name());
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(NameFeederPacket pkt, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer player)) {
                return;
            }
            Level level = player.level();
            BlockEntity be = level.getBlockEntity(pkt.pos());
            if (be instanceof FeederBlockEntity feeder) {
                Component nameComp = Component.literal(pkt.name());
                FeederTrackerApp.addFeeder(player, pkt.pos(), nameComp);
                feeder.setChanged();
            }
        });
    }
}
